package org.unclesniper.winter.mvc.dispatch;

public class IntegerParameterPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	private PathKeyT key;

	public IntegerParameterPathMatcher(PathKeyT key) {
		this.key = key;
	}

	public PathKeyT getKey() {
		return key;
	}

	public void setKey(PathKeyT key) {
		this.key = key;
	}

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		int length = pathInfo.length(), end = offset;
		while(end < length) {
			char c = pathInfo.charAt(end);
			if(c >= '0' && c <= '9')
				++end;
			else
				break;
		}
		if(end == length)
			return -1;
		pathParameters.putParameter(key, offset, end);
		return end - offset;
	}

	public boolean isCacheable() {
		return false;
	}

}
