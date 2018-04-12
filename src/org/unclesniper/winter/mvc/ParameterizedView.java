package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface ParameterizedView<ModelT, ParameterT> {

	void renderModel(HTTPResponse response, ModelT model, ParameterT parameter)
			throws IOException, HTTPServiceException;

	public static <ModelT, ParameterT> ParameterizedView<ModelT, ParameterT> ignore(View<ModelT> view) {
		return (response, model, parameter) -> view.renderModel(response, model);
	}

}
