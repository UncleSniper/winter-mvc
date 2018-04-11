package org.unclesniper.winter.mvc.dispatch;

public class RejectingPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	public RejectingPathMatcher() {}

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		return -1;
	}

	public boolean isCacheable() {
		return false;
	}

}
