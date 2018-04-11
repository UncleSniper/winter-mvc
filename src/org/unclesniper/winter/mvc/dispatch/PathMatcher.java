package org.unclesniper.winter.mvc.dispatch;

public interface PathMatcher<PathKeyT> {

	int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters);

	boolean isCacheable();

}
