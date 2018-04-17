package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public interface ParameterizedController<ModelT, ParameterT> {

	ModelT performAction(HTTPRequest request, ParameterT parameter) throws IOException, HTTPServiceException;

	public static <ModelT, ParameterT>
	ParameterizedController<ModelT, ParameterT> ignore(Controller<ModelT> controller) {
		return (request, parameter) -> controller.performAction(request);
	}

	public static <OldModelT extends NewModelT, NewModelT, ParameterT>
	ParameterizedController<NewModelT, ParameterT>
	widenModel(ParameterizedController<OldModelT, ParameterT> controller) {
		return (request, parameter) -> controller.performAction(request, parameter);
	}

	public static <OldModelT, NewModelT, ParameterT> ParameterizedController<NewModelT, ParameterT>
	mapModel(ParameterizedController<OldModelT, ParameterT> controller,
			Transform<? super OldModelT, ? extends NewModelT> transform) {
		return (request, parameter) -> transform.transform(controller.performAction(request, parameter));
	}

	public static <ModelT, OldParameterT, NewParameterT extends OldParameterT>
	ParameterizedController<ModelT, NewParameterT>
	narrowParameter(ParameterizedController<ModelT, OldParameterT> controller) {
		return (request, parameter) -> controller.performAction(request, parameter);
	}

	public static <ModelT, OldParameterT, NewParameterT> ParameterizedController<ModelT, NewParameterT>
	mapParameter(ParameterizedController<ModelT, OldParameterT> controller,
			Transform<? super NewParameterT, ? extends OldParameterT> transform) {
		return (request, parameter) -> controller.performAction(request, transform.transform(parameter));
	}

}
