package org.unclesniper.winter.mvc.builder;

import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.Controller;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.ParameterizedView;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.ParameterizedController;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;

public interface MVCBuilder<PathKeyT, RequestParameterT> {

	MVCBuilder<PathKeyT, RequestParameterT> on(HTTPVerb... verbs);

	MVCBuilder<PathKeyT, RequestParameterT> andOn(HTTPVerb... verbs);

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> at(PathMatcher<PathKeyT>... path);

	MVCBuilder<PathKeyT, RequestParameterT> at(String... path);

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> andAt(PathMatcher<PathKeyT>... path);

	MVCBuilder<PathKeyT, RequestParameterT> andAt(String... path);

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> followedBy(PathMatcher<PathKeyT>... path);

	MVCBuilder<PathKeyT, RequestParameterT> followedBy(String... path);

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> orFollowedBy(PathMatcher<PathKeyT>... path);

	MVCBuilder<PathKeyT, RequestParameterT> orFollowedBy(String... path);

	MVCBuilder<PathKeyT, RequestParameterT> dot(String extension);

	MVCBuilder<PathKeyT, RequestParameterT> html();

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> html(PathMatcher<PathKeyT>... head);

	MVCBuilder<PathKeyT, RequestParameterT> html(String... head);

	MVCBuilder<PathKeyT, RequestParameterT> json();

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> json(PathMatcher<PathKeyT>... head);

	MVCBuilder<PathKeyT, RequestParameterT> json(String... head);

	MVCBuilder<PathKeyT, RequestParameterT> xml();

	@SuppressWarnings("unchecked")
	MVCBuilder<PathKeyT, RequestParameterT> xml(PathMatcher<PathKeyT>... head);

	MVCBuilder<PathKeyT, RequestParameterT> xml(String... head);

	MVCBuilder<PathKeyT, RequestParameterT> content(String... contentTypes);

	MVCBuilder<PathKeyT, RequestParameterT> orContent(String... contentTypes);

	MVCBuilder<PathKeyT, RequestParameterT> onJSON();

	MVCBuilder<PathKeyT, RequestParameterT> onXML();

	MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedRequestHandler<? super RequestParameterT> handler);

	MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedRequestHandler<? super RequestParameterT> handler);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller, View<? super ModelT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller, View<? super ModelT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ModelT, ControllerParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view);

	<ModelT, ControllerParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view);

	<ModelT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ControllerParameterT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ModelT, ControllerParameterT, ViewParameterT> MVCBuilder<PathKeyT, RequestParameterT>
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

}
