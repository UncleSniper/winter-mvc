package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public interface ParameterizedRequestHandler<ParameterT> {

	void handleRequest(HTTPRequest request, HTTPResponse response, ParameterT parameter)
			throws IOException, HTTPServiceException;

	public static <ParameterT> ParameterizedRequestHandler<ParameterT> ignore(RequestHandler handler) {
		return (request, response, parameter) -> handler.handleRequest(request, response);
	}

	public static <SourceT, DestinationT>
	ParameterizedRequestHandler<DestinationT> transform(ParameterizedRequestHandler<? super SourceT> handler,
			Transform<? super DestinationT, ? extends SourceT> transform) {
		return (request, response, parameter) ->
				handler.handleRequest(request, response, transform.transform(parameter));
	}

}
