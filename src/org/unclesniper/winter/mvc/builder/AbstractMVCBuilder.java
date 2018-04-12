package org.unclesniper.winter.mvc.builder;

import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.Controller;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.ParameterizedView;
import org.unclesniper.winter.mvc.MVCRequestHandler;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.ParameterizedController;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;

public abstract class AbstractMVCBuilder<PathKeyT, RequestParameterT>
		implements MVCBuilder<PathKeyT, RequestParameterT> {

	protected final BuildState<PathKeyT, RequestParameterT> state;

	public AbstractMVCBuilder(Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform) {
		state = new BuildState<PathKeyT, RequestParameterT>(handlerTransform);
	}

	public MVCBuilder<PathKeyT, RequestParameterT> on(HTTPVerb... verbs) {
		state.addVerbs(verbs);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> andOn(HTTPVerb... verbs) {
		state.clearVerbs();
		state.addVerbs(verbs);
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> at(PathMatcher<PathKeyT>... path) {
		state.addHead(path);
		state.clearTail();
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> at(String... path) {
		state.addHead(path);
		state.clearTail();
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> andAt(PathMatcher<PathKeyT>... path) {
		state.clearHead();
		state.addHead(path);
		state.clearTail();
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> andAt(String... path) {
		state.clearHead();
		state.addHead(path);
		state.clearTail();
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> followedBy(PathMatcher<PathKeyT>... path) {
		state.clearTail();
		state.addTail(path);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> followedBy(String... path) {
		state.clearTail();
		state.addTail(path);
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> orFollowedBy(PathMatcher<PathKeyT>... path) {
		state.addTail(path);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> orFollowedBy(String... path) {
		state.addTail(path);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> dot(String extension) {
		state.clearTail();
		if(extension != null)
			state.addTail('.' + extension);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> html() {
		state.clearTail();
		state.addTail(".html");
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> html(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".html");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> html(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".html");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> json() {
		state.clearTail();
		state.addTail(".json");
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> json(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".json");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> json(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".json");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> xml() {
		state.clearTail();
		state.addTail(".xml");
		return this;
	}

	@SuppressWarnings("unchecked")
	public MVCBuilder<PathKeyT, RequestParameterT> xml(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".xml");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> xml(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".xml");
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> content(String... contentTypes) {
		state.clearContentTypes();
		state.addContentTypes(contentTypes);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> orContent(String... contentTypes) {
		state.addContentTypes(contentTypes);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> onJSON() {
		state.clearContentTypes();
		state.addContentTypes(ContentTypes.JSON);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT> onXML() {
		state.clearContentTypes();
		state.addContentTypes(ContentTypes.XML);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedRequestHandler<? super RequestParameterT> handler) {
		state.emitRule(handler);
		return this;
	}

	public MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedRequestHandler<? super RequestParameterT> handler) {
		state.emitRule(handler);
		state.clearVerbs();
		state.clearHead();
		state.clearTail();
		state.clearContentTypes();
		return this;
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller, View<? super ModelT> view) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(ParameterizedController.ignore(controller), ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller, View<? super ModelT> view) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(ParameterizedController.ignore(controller), ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(controller, ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(controller, ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(ParameterizedController.ignore(controller), view,
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(ParameterizedController.ignore(controller), view,
				Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(controller, view, Transform.widen(), Transform.widen()));
	}

	public <ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				RequestParameterT>(controller, view, Transform.widen(), Transform.widen()));
	}

	public <ModelT, ControllerParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				RequestParameterT>(controller, ParameterizedView.ignore(view),
				controllerParameterTransform, Transform.widen()));
	}

	public <ModelT, ControllerParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				RequestParameterT>(controller, ParameterizedView.ignore(view),
				controllerParameterTransform, Transform.widen()));
	}

	public <ModelT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				ViewParameterT>(ParameterizedController.ignore(controller), view,
				Transform.widen(), viewParameterTransform));
	}

	public <ModelT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, RequestParameterT,
				ViewParameterT>(ParameterizedController.ignore(controller), view,
				Transform.widen(), viewParameterTransform));
	}

	public <ModelT, ControllerParameterT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return handle(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				ViewParameterT>(controller, view, controllerParameterTransform, viewParameterTransform));
	}

	public <ModelT, ControllerParameterT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return handleAnd(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				ViewParameterT>(controller, view, controllerParameterTransform, viewParameterTransform));
	}

}
