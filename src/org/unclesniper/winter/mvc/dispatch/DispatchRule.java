package org.unclesniper.winter.mvc.dispatch;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.winter.mvc.PathParameterizedRequestHandler;

public final class DispatchRule<PathKeyT> {

	private int verbMask;

	private final List<PathMatcher<PathKeyT>> pathMatchers = new LinkedList<PathMatcher<PathKeyT>>();

	private PathParameterizedRequestHandler<PathKeyT> requestHandler;

	private boolean cacheable;

	public DispatchRule(int verbMask, PathParameterizedRequestHandler<PathKeyT> requestHandler) {
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

	public PathParameterizedRequestHandler<PathKeyT> getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(PathParameterizedRequestHandler<PathKeyT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

}
