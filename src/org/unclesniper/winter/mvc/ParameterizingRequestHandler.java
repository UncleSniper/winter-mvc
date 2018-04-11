package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.GenericFactory;

public class ParameterizingRequestHandler<ParameterT> implements RequestHandler {

	private GenericFactory<ParameterT> parameterFactory;

	private ParameterizedRequestHandler<ParameterT> requestHandler;

	public ParameterizingRequestHandler(GenericFactory<ParameterT> parameterFactory,
			ParameterizedRequestHandler<ParameterT> requestHandler) {
		this.parameterFactory = parameterFactory;
		this.requestHandler = requestHandler;
	}

	public GenericFactory<ParameterT> getParameterFactory() {
		return parameterFactory;
	}

	public void setParameterFactory(GenericFactory<ParameterT> parameterFactory) {
		this.parameterFactory = parameterFactory;
	}

	public ParameterizedRequestHandler<ParameterT> getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(ParameterizedRequestHandler<ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response) throws IOException, HTTPServiceException {
		ParameterT parameter = parameterFactory == null ? null : parameterFactory.newInstance();
		requestHandler.handleRequest(request, response, parameter);
	}

}
