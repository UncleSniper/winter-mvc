package org.unclesniper.winter.mvc.dispatch;

public class SegmentParameterPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	private PathKeyT key;

	private boolean allowEmpty;

	public SegmentParameterPathMatcher(PathKeyT key) {
		this.key = key;
	}

	public SegmentParameterPathMatcher(PathKeyT key, boolean allowEmpty) {
		this.key = key;
		this.allowEmpty = allowEmpty;
	}

	public PathKeyT getKey() {
		return key;
	}

	public void setKey(PathKeyT key) {
		this.key = key;
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		int length = pathInfo.length(), end = offset;
		while(end < length && pathInfo.charAt(end) != '/')
			++end;
		if(end == offset && !allowEmpty)
			return -1;
		pathParameters.putParameter(key, offset, end);
		return end - offset;
	}

	public boolean isCacheable() {
		return false;
	}

}
