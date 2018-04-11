package org.unclesniper.winter.mvc.dispatch;

public class SeparatorPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		int length = pathInfo.length(), end = offset;
		while(end < length && pathInfo.charAt(end) == '/')
			++end;
		return end == offset ? -1 : end - offset;
	}

	public boolean isCacheable() {
		return true;
	}

}
