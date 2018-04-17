package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public interface ParameterizedView<ModelT, ParameterT> {

	void renderModel(HTTPResponse response, ModelT model, ParameterT parameter)
			throws IOException, HTTPServiceException;

	public static <ModelT, ParameterT> ParameterizedView<ModelT, ParameterT> ignore(View<ModelT> view) {
		return (response, model, parameter) -> view.renderModel(response, model);
	}

	public static <OldModelT, NewModelT extends OldModelT, ParameterT>
	ParameterizedView<NewModelT, ParameterT> narrowModel(ParameterizedView<OldModelT, ParameterT> view) {
		return (response, model, parameter) -> view.renderModel(response, model, parameter);
	}

	public static <OldModelT, NewModelT, ParameterT>
	ParameterizedView<NewModelT, ParameterT> mapModel(ParameterizedView<OldModelT, ParameterT> view,
			Transform<? super NewModelT, ? extends OldModelT> transform) {
		return (response, model, parameter) -> view.renderModel(response, transform.transform(model), parameter);
	}

	public static <ModelT, OldParameterT, NewParameterT extends OldParameterT>
	ParameterizedView<ModelT, NewParameterT> narrowParameter(ParameterizedView<ModelT, OldParameterT> view) {
		return (response, model, parameter) -> view.renderModel(response, model, parameter);
	}

	public static <ModelT, OldParameterT, NewParameterT>
	ParameterizedView<ModelT, NewParameterT> mapParameter(ParameterizedView<ModelT, OldParameterT> view,
			Transform<? super NewParameterT, ? extends OldParameterT> transform) {
		return (response, model, parameter) -> view.renderModel(response, model, transform.transform(parameter));
	}

}
