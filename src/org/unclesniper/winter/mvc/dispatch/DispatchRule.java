package org.unclesniper.winter.mvc.dispatch;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;

public final class DispatchRule<PathKeyT, ParameterT> {

	private int verbMask;

	private final List<PathMatcher<PathKeyT>> pathMatchers = new LinkedList<PathMatcher<PathKeyT>>();

	private final Set<String> contentTypes = new HashSet<String>();

	private ParameterizedRequestHandler<? super ParameterT> requestHandler;

	private boolean cacheable;

	public DispatchRule(int verbMask, ParameterizedRequestHandler<? super ParameterT> requestHandler) {
		this.verbMask = verbMask;
		this.requestHandler = requestHandler;
	}

	public int getVerbMask() {
		return verbMask;
	}

	public void setVerbMask(int verbMask) {
		this.verbMask = verbMask;
	}

	public Iterable<PathMatcher<PathKeyT>> getPathMatchers() {
		return pathMatchers;
	}

	public void addPathMatcher(PathMatcher<PathKeyT> matcher) {
		pathMatchers.add(matcher == null ? new RejectingPathMatcher<PathKeyT>() : matcher);
	}

	public void addPathMatchers(Iterable<PathMatcher<PathKeyT>> matchers) {
		for(PathMatcher<PathKeyT> matcher : matchers)
			addPathMatcher(matcher);
	}

	public Iterable<String> getContentTypes() {
		return contentTypes;
	}

	public void addContentType(String contentType) {
		contentTypes.add(contentType);
	}

	public void addContentTypes(Iterable<String> contentType) {
		for(String type : contentTypes)
			contentTypes.add(type);
	}

	public ParameterizedRequestHandler<? super ParameterT> getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(ParameterizedRequestHandler<? super ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

}
