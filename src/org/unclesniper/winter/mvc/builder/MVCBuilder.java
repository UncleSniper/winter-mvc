package org.unclesniper.winter.mvc.builder;

import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.Controller;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.RequestHandler;
import org.unclesniper.winter.mvc.ParameterizedView;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.ParameterizedController;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;
import org.unclesniper.winter.mvc.DispatchModelRequestHandler;

public interface MVCBuilder<PathKeyT, RequestParameterT,
		FreeReturnT extends MVCBuilder<PathKeyT, RequestParameterT, FreeReturnT, RefreeReturnT>,
		RefreeReturnT extends MVCBuilder<PathKeyT, RequestParameterT, RefreeReturnT, RefreeReturnT>> {

	DispatchModelRequestHandler<PathKeyT, RequestParameterT> dispatcher();

	FreeReturnT on(HTTPVerb... verbs);

	FreeReturnT andOn(HTTPVerb... verbs);

	@SuppressWarnings("unchecked")
	FreeReturnT at(PathMatcher<PathKeyT>... path);

	FreeReturnT at(String... path);

	@SuppressWarnings("unchecked")
	FreeReturnT andAt(PathMatcher<PathKeyT>... path);

	FreeReturnT andAt(String... path);

	@SuppressWarnings("unchecked")
	FreeReturnT followedBy(PathMatcher<PathKeyT>... path);

	FreeReturnT followedBy(String... path);

	@SuppressWarnings("unchecked")
	FreeReturnT orFollowedBy(PathMatcher<PathKeyT>... path);

	FreeReturnT orFollowedBy(String... path);

	FreeReturnT dot(String extension);

	FreeReturnT html();

	@SuppressWarnings("unchecked")
	FreeReturnT html(PathMatcher<PathKeyT>... head);

	FreeReturnT html(String... head);

	FreeReturnT json();

	@SuppressWarnings("unchecked")
	FreeReturnT json(PathMatcher<PathKeyT>... head);

	FreeReturnT json(String... head);

	FreeReturnT xml();

	@SuppressWarnings("unchecked")
	FreeReturnT xml(PathMatcher<PathKeyT>... head);

	FreeReturnT xml(String... head);

	FreeReturnT content(String... contentTypes);

	FreeReturnT orContent(String... contentTypes);

	FreeReturnT onJSON();

	FreeReturnT onXML();

	RefreeReturnT handle(RequestHandler handler);

	RefreeReturnT handleAnd(RequestHandler handler);

	RefreeReturnT handle(ParameterizedRequestHandler<? super RequestParameterT> handler);

	RefreeReturnT handleAnd(ParameterizedRequestHandler<? super RequestParameterT> handler);

	<ModelT> RefreeReturnT handle(Controller<? extends ModelT> controller, View<? super ModelT> view);

	<ModelT> RefreeReturnT handleAnd(Controller<? extends ModelT> controller, View<? super ModelT> view);

	<ModelT> RefreeReturnT handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view);

	<ModelT> RefreeReturnT handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view);

	<ModelT> RefreeReturnT handle(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> RefreeReturnT handleAnd(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> RefreeReturnT handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> RefreeReturnT handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT, ControllerParameterT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view);

	<ModelT, ControllerParameterT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view);

	<ModelT, ViewParameterT> RefreeReturnT
	handle(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ViewParameterT> RefreeReturnT
	handleAnd(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT> MVCBuilderWithController<PathKeyT, RequestParameterT,
			? extends MVCBuilderWithController<PathKeyT, RequestParameterT, ?, ?, RequestParameterT, ModelT>,
			? extends MVCBuilder<PathKeyT, RequestParameterT, ?, ?>,
			RequestParameterT, ModelT>
	perform(Controller<ModelT> controller);

	<ModelT> MVCBuilderWithController<PathKeyT, RequestParameterT,
			? extends MVCBuilderWithController<PathKeyT, RequestParameterT, ?, ?, RequestParameterT, ModelT>,
			? extends MVCBuilder<PathKeyT, RequestParameterT, ?, ?>,
			RequestParameterT, ModelT>
	perform(ParameterizedController<ModelT, ? super RequestParameterT> controller);

	<ModelT, ControllerParameterT>
	MVCBuilderWithController<PathKeyT, RequestParameterT,
			? extends MVCBuilderWithController<PathKeyT, RequestParameterT, ?, ?, ControllerParameterT, ModelT>,
			? extends MVCBuilder<PathKeyT, RequestParameterT, ?, ?>,
			ControllerParameterT, ModelT>
	perform(ParameterizedController<ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform);

	public static <PathKeyT, RequestParameterT> FreeBuilder<PathKeyT, RequestParameterT>
	route(Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform) {
		return new FreeBuilder<PathKeyT, RequestParameterT>(handlerTransform);
	}

	public static <PathKeyT, RequestParameterT> FreeBuilder<PathKeyT, RequestParameterT> route() {
		return MVCBuilder.route(null);
	}

}
