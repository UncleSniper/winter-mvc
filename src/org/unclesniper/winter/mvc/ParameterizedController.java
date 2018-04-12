package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface ParameterizedController<ModelT, ParameterT> {

	ModelT performAction(HTTPRequest request, ParameterT parameter) throws IOException, HTTPServiceException;

	public static <ModelT, ParameterT>
	ParameterizedController<ModelT, ParameterT> ignore(Controller<ModelT> controller) {
		return (request, parameter) -> controller.performAction(request);
	}

}
