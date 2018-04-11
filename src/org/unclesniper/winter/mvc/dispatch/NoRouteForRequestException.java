package org.unclesniper.winter.mvc.dispatch;

import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.HTTPServiceException;

public class NoRouteForRequestException extends HTTPServiceException {

	private final HTTPVerb verb;

	private final String pathInfo;

	public NoRouteForRequestException(HTTPVerb verb, String pathInfo) {
		super("No route for " + verb.name() + ' ' + pathInfo);
		this.verb = verb;
		this.pathInfo = pathInfo;
	}

	public HTTPVerb getVerb() {
		return verb;
	}

	public String getPathInfo() {
		return pathInfo;
	}

}
