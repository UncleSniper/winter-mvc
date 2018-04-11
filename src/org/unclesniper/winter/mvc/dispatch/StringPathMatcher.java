package org.unclesniper.winter.mvc.dispatch;

public class StringPathMatcher<PathKeyT> implements PathMatcher<PathKeyT> {

	private String matchString;

	public StringPathMatcher(String matchString) {
		this.matchString = matchString;
	}

	public String getMatchString() {
		return matchString;
	}

	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}

	public int matchPath(String pathInfo, int offset, PathParameters<PathKeyT> pathParameters) {
		return pathInfo.startsWith(matchString, offset) ? matchString.length() : -1;
	}

	public boolean isCacheable() {
		return true;
	}

}
