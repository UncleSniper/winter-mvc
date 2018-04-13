package org.unclesniper.winter.mvc.builder;

import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.ParameterizedView;
import org.unclesniper.winter.mvc.MVCRequestHandler;
import org.unclesniper.winter.mvc.ParameterizedController;

public class ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>
		extends AbstractMVCBuilder<PathKeyT, RequestParameterT,
			ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>,
			FreeBuilder<PathKeyT, RequestParameterT>>
		implements MVCBuilderWithController<PathKeyT, RequestParameterT,
			ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>,
			FreeBuilder<PathKeyT, RequestParameterT>,
			ControllerParameterT, ModelT> {

	private ParameterizedController<? extends ModelT, ? super ControllerParameterT> controller;

	private Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform;

	public ControllerBoundBuilder(BuildState<PathKeyT, RequestParameterT> state,
			ParameterizedController<? extends ModelT, ? super ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		super(state);
		freeThis = this;
		this.controller = controller;
		this.controllerParameterTransform = controllerParameterTransform;
	}

	protected FreeBuilder<PathKeyT, RequestParameterT> refree() {
		return new FreeBuilder<PathKeyT, RequestParameterT>(state);
	}

	private <ViewParameterT> void mvc(ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		state.emitRule(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				ViewParameterT>(controller, view, controllerParameterTransform, viewParameterTransform));
	}

	public ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>
	render(View<? super ModelT> view) {
		mvc(ParameterizedView.ignore(view), Transform.widen());
		return this;
	}

	public FreeBuilder<PathKeyT, RequestParameterT> renderAnd(View<? super ModelT> view) {
		mvc(ParameterizedView.ignore(view), Transform.widen());
		reset();
		return refree();
	}

	public ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>
	render(ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		mvc(view, Transform.widen());
		return this;
	}

	public FreeBuilder<PathKeyT, RequestParameterT>
	renderAnd(ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		mvc(view, Transform.widen());
		reset();
		return refree();
	}

	public <ViewParameterT> ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>
	render(ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		mvc(view, viewParameterTransform);
		return this;
	}

	public <ViewParameterT> FreeBuilder<PathKeyT, RequestParameterT>
	renderAnd(ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		mvc(view, viewParameterTransform);
		reset();
		return refree();
	}

}
