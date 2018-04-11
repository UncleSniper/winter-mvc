package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface ParameterizedRequestHandler<ParameterT> {

	void handleRequest(HTTPRequest request, HTTPResponse response, ParameterT parameter)
			throws IOException, HTTPServiceException;

	public static <ParameterT> ParameterizedRequestHandler<ParameterT> ignore(RequestHandler handler) {
		return (request, response, parameter) -> handler.handleRequest(request, response);
	}

}
