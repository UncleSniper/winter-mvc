package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.dispatch.PathParameters;

public interface PathParameterizedRequestHandler<PathKeyT> {

	void handleRequest(HTTPRequest request, HTTPResponse response, PathParameters<PathKeyT> pathParameters)
			throws IOException, HTTPServiceException;

	public static <PathKeyT> PathParameterizedRequestHandler<PathKeyT> ignore(RequestHandler handler) {
		return (request, response, pathParameters) -> handler.handleRequest(request, response);
	}

}
