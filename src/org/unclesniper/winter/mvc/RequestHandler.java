package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.GenericFactory;

public interface RequestHandler {

	void handleRequest(HTTPRequest request, HTTPResponse response) throws IOException, HTTPServiceException;

	public static <ParameterT> RequestHandler start(ParameterizedRequestHandler<ParameterT> requestHandler,
			GenericFactory<ParameterT> parameterFactory) {
		return new ParameterizingRequestHandler<ParameterT>(parameterFactory, requestHandler);
	}

}
