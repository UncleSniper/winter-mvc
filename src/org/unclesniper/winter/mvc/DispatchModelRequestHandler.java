package org.unclesniper.winter.mvc;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.dispatch.DispatchRule;
import org.unclesniper.winter.mvc.dispatch.PathParameters;
import org.unclesniper.winter.mvc.dispatch.NoRouteForRequestException;

public class DispatchModelRequestHandler<PathKeyT, ParameterT> implements ParameterizedRequestHandler<ParameterT> {

	private static final class PerVerb<PathKeyT, ParameterT> {

		public final List<DispatchRule<PathKeyT, ParameterT>> rules
				= new LinkedList<DispatchRule<PathKeyT, ParameterT>>();

		public final Map<String, PerPath<PathKeyT, ParameterT>> cachedHandlers
				= new ConcurrentHashMap<String, PerPath<PathKeyT, ParameterT>>();

	}

	private static final class PerPath<PathKeyT, ParameterT> {

		public ParameterizedRequestHandler<? super ParameterT> defaultHandler;

		public final Map<String, ParameterizedRequestHandler<? super ParameterT>> handlerMap
				= new HashMap<String, ParameterizedRequestHandler<? super ParameterT>>();

		public void mergeRule(DispatchRule<PathKeyT, ParameterT> rule) {
			ParameterizedRequestHandler<? super ParameterT> requestHandler = rule.getRequestHandler();
			for(String contentType : rule.getContentTypes()) {
				if(contentType == null) {
					if(defaultHandler == null)
						defaultHandler = requestHandler;
				}
				else {
					if(!handlerMap.containsKey(contentType))
						handlerMap.put(contentType, requestHandler);
				}
			}
		}

	}

	private final List<DispatchRule<PathKeyT, ParameterT>> rules
			= new LinkedList<DispatchRule<PathKeyT, ParameterT>>();

	private RequestParameterMerger<? super ParameterT, ? super PathParameters<PathKeyT>> parameterMerger;

	@SuppressWarnings("unchecked")
	private final PerVerb<PathKeyT, ParameterT>[] perVerb = new PerVerb[HTTPVerb.values().length];

	public DispatchModelRequestHandler() {
		for(int i = 0; i < perVerb.length; ++i)
			perVerb[i] = new PerVerb<PathKeyT, ParameterT>();
	}

	public RequestParameterMerger<? super ParameterT, ? super PathParameters<PathKeyT>> getParameterMerger() {
		return parameterMerger;
	}

	public void setParameterMerger(RequestParameterMerger<? super ParameterT, ? super PathParameters<PathKeyT>>
			parameterMerger) {
		this.parameterMerger = parameterMerger;
	}

	public Iterable<DispatchRule<PathKeyT, ParameterT>> getRules() {
		return rules;
	}

	public void addRule(DispatchRule<PathKeyT, ParameterT> rule) {
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
			PerVerb<PathKeyT, ParameterT> pv = perVerb[verb.ordinal()];
			pv.rules.add(rule);
		}
	}

	public boolean removeRule(DispatchRule<PathKeyT, ParameterT> rule) {
		if(!rules.remove(rule))
			return false;
		for(int i = 0; i < perVerb.length; ++i) {
			perVerb[i].rules.remove(rule);
			perVerb[i].cachedHandlers.clear();
		}
		return true;
	}

	public void clearCache() {
		for(int i = 0; i < perVerb.length; ++i)
			perVerb[i].cachedHandlers.clear();
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response, ParameterT parameter)
			throws IOException, HTTPServiceException {
		String pathInfo = request.getPath();
		if(pathInfo == null)
			pathInfo = "";
		PerVerb<PathKeyT, ParameterT> pv = perVerb[request.getMethod().ordinal()];
		PathParameters<PathKeyT> parameters = new PathParameters<PathKeyT>(pathInfo);
		PerPath<PathKeyT, ParameterT> pp = pv.cachedHandlers.get(pathInfo);
		if(pp == null) {
			int length = pathInfo.length();
			int headOffset = 0;
			while(headOffset < length && pathInfo.charAt(headOffset) == '/')
				++headOffset;
			pp = new PerPath<PathKeyT, ParameterT>();
			boolean haveMatch = false, isCacheable = true;
			for(DispatchRule<PathKeyT, ParameterT> rule : pv.rules) {
				boolean candidate = true;
				int offset = headOffset;
				for(PathMatcher<PathKeyT> matcher : rule.getPathMatchers()) {
					int consumed = matcher.matchPath(pathInfo, offset, haveMatch ? null : parameters);
					if(consumed < 0) {
						candidate = false;
						break;
					}
					offset += consumed;
				}
				if(!candidate) {
					if(!haveMatch)
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
					if(!haveMatch)
						parameters.clearParameters();
					continue;
				}
				haveMatch = true;
				if(!rule.isCacheable())
					isCacheable = false;
				pp.mergeRule(rule);
			}
			if(isCacheable)
				pv.cachedHandlers.put(pathInfo, pp);
		}
		ParameterizedRequestHandler<? super ParameterT> handler = pp.handlerMap.get(request.getContentType());
		if(handler == null) {
			handler = pp.defaultHandler;
			if(handler == null)
				throw new NoRouteForRequestException(request.getMethod(), pathInfo);
		}
		if(parameterMerger != null)
			parameterMerger.mergeRequestParameter(parameter, parameters);
		handler.handleRequest(request, response, parameter);
	}

}
