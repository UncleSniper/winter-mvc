package org.unclesniper.winter.mvc.dispatch;

public class TailStringPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	private String matchString;

	public TailStringPathMatcher(String matchString) {
		this.matchString = matchString;
	}

	public String getMatchString() {
		return matchString;
	}

	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		int length = pathInfo.length(), mslen = matchString.length();
		if(offset + mslen > length || !pathInfo.startsWith(matchString, length - mslen))
			return -1;
		return length - offset;
	}

	public boolean isCacheable() {
		return true;
	}

}
