package org.unclesniper.winter.mvc;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.dispatch.DispatchRule;
import org.unclesniper.winter.mvc.dispatch.PathParameters;
import org.unclesniper.winter.mvc.dispatch.NoRouteForRequestException;

public class DispatchModelRequestHandler<PathKeyT> implements RequestHandler {

	private static final class PerVerb<PathKeyT> {

		public final List<DispatchRule<PathKeyT>> rules = new LinkedList<DispatchRule<PathKeyT>>();

		public final Map<String, PathParameterizedRequestHandler<PathKeyT>> cachedHandlers
				= new ConcurrentHashMap<String, PathParameterizedRequestHandler<PathKeyT>>();

	}

	private final List<DispatchRule<PathKeyT>> rules = new LinkedList<DispatchRule<PathKeyT>>();

	@SuppressWarnings("unchecked")
	private final PerVerb<PathKeyT>[] perVerb = new PerVerb[HTTPVerb.values().length];

	public DispatchModelRequestHandler() {
		for(int i = 0; i < perVerb.length; ++i)
			perVerb[i] = new PerVerb<PathKeyT>();
	}

	public Iterable<DispatchRule<PathKeyT>> getRules() {
		return rules;
	}

	public void addRule(DispatchRule<PathKeyT> rule) {
		if(rules.contains(rule))
			return;
		boolean isCacheable = true;
		for(PathMatcher<PathKeyT> matcher : rule.getPathMatchers()) {
			if(!matcher.isCacheable()) {
				isCacheable = false;
				break;
			}
		}
		rule.setCacheable(isCacheable);
		rules.add(rule);
		int verbMask = rule.getVerbMask();
		for(HTTPVerb verb : HTTPVerb.values()) {
			if((verbMask & (1 << verb.ordinal())) == 0)
				continue;
			perVerb[verb.ordinal()].rules.add(rule);
		}
	}

	public boolean removeRule(DispatchRule<PathKeyT> rule) {
		if(!rules.remove(rule))
			return false;
		for(int i = 0; i < perVerb.length; ++i)
			perVerb[i].rules.remove(rule);
		return true;
	}

	public void clearCache() {
		for(int i = 0; i < perVerb.length; ++i)
			perVerb[i].cachedHandlers.clear();
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response) throws IOException, HTTPServiceException {
		String pathInfo = request.getPath();
		if(pathInfo == null)
			pathInfo = "";
		PerVerb<PathKeyT> pv = perVerb[request.getMethod().ordinal()];
		PathParameters<PathKeyT> parameters = new PathParameters<PathKeyT>(pathInfo);
		PathParameterizedRequestHandler<PathKeyT> handler = pv.cachedHandlers.get(pathInfo);
		if(handler == null) {
			int length = pathInfo.length();
			int headOffset = 0;
			while(headOffset < length && pathInfo.charAt(headOffset) == '/')
				++headOffset;
			for(DispatchRule<PathKeyT> rule : pv.rules) {
				boolean candidate = true;
				int offset = headOffset;
				for(PathMatcher<PathKeyT> matcher : rule.getPathMatchers()) {
					int consumed = matcher.matchPath(pathInfo, offset, parameters);
					if(consumed < 0) {
						candidate = false;
						break;
					}
					offset += consumed;
				}
				if(!candidate) {
					parameters.clearParameters();
					continue;
				}
				for(; offset < length; ++offset) {
					if(pathInfo.charAt(offset) != '/') {
						candidate = false;
						break;
					}
				}
				if(!candidate) {
					parameters.clearParameters();
					continue;
				}
				handler = rule.getRequestHandler();
				if(handler != null) {
					if(rule.isCacheable())
						pv.cachedHandlers.put(pathInfo, handler);
					break;
				}
			}
			if(handler == null)
				throw new NoRouteForRequestException(request.getMethod(), pathInfo);
		}
		handler.handleRequest(request, response, parameters);
	}

}
