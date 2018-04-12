package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public class MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT, ViewParameterT>
		implements ParameterizedRequestHandler<RequestParameterT> {

	private ParameterizedController<? extends ModelT, ? super ControllerParameterT> controller;

	private ParameterizedView<? super ModelT, ? super ViewParameterT> view;

	private Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform;

	private Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform;

	public MVCRequestHandler() {}

	public MVCRequestHandler(ParameterizedController<? extends ModelT, ? super ControllerParameterT> controller,
			ParameterizedView<? super ModelT, ? super ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		this.controller = controller;
		this.view = view;
		this.controllerParameterTransform = controllerParameterTransform;
		this.viewParameterTransform = viewParameterTransform;
	}

	public ParameterizedController<? extends ModelT, ? super ControllerParameterT> getController() {
		return controller;
	}

	public void setController(ParameterizedController<? extends ModelT, ? super ControllerParameterT> controller) {
		this.controller = controller;
	}

	public ParameterizedView<? super ModelT, ? super ViewParameterT> getView() {
		return view;
	}

	public void setView(ParameterizedView<? super ModelT, ? super ViewParameterT> view) {
		this.view = view;
	}

	public Transform<? super RequestParameterT, ? extends ControllerParameterT> getControllerParameterTransform() {
		return controllerParameterTransform;
	}

	public void setControllerParameterTransform(
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		this.controllerParameterTransform = controllerParameterTransform;
	}

	public Transform<? super RequestParameterT, ? extends ViewParameterT> getViewParameterTransform() {
		return viewParameterTransform;
	}

	public void setViewParameterTransform(
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		this.viewParameterTransform = viewParameterTransform;
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response, RequestParameterT parameter)
			throws IOException, HTTPServiceException {
		ModelT model = controller.performAction(request, controllerParameterTransform.transform(parameter));
		if(model != null)
			view.renderModel(response, model, viewParameterTransform.transform(parameter));
	}

}
