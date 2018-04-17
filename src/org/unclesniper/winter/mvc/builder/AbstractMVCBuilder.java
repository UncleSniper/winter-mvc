package org.unclesniper.winter.mvc.builder;

import java.net.URL;
import java.net.URI;
import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.Controller;
import org.unclesniper.winter.mvc.RedirectView;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.RequestHandler;
import org.unclesniper.winter.mvc.util.URLBuilder;
import org.unclesniper.winter.mvc.RedirectLocation;
import org.unclesniper.winter.mvc.util.ValueHolder;
import org.unclesniper.winter.mvc.ParameterizedView;
import org.unclesniper.winter.mvc.MVCRequestHandler;
import org.unclesniper.winter.mvc.BasedRedirectView;
import org.unclesniper.winter.mvc.MaybeRedirectModel;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.ParameterizedController;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;
import org.unclesniper.winter.mvc.DispatchModelRequestHandler;
import org.unclesniper.winter.mvc.ParameterizingRequestHandler;

public abstract class AbstractMVCBuilder<PathKeyT, RequestParameterT,
		FreeReturnT extends AbstractMVCBuilder<PathKeyT, RequestParameterT, FreeReturnT, RefreeReturnT>,
		RefreeReturnT extends AbstractMVCBuilder<PathKeyT, RequestParameterT, RefreeReturnT, RefreeReturnT>>
		implements MVCBuilder<PathKeyT, RequestParameterT, FreeReturnT, RefreeReturnT> {

	protected final BuildState<PathKeyT, RequestParameterT> state;

	protected FreeReturnT freeThis;

	public AbstractMVCBuilder(Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform) {
		state = new BuildState<PathKeyT, RequestParameterT>(handlerTransform);
	}

	public AbstractMVCBuilder(BuildState<PathKeyT, RequestParameterT> state) {
		this.state = state;
	}

	public DispatchModelRequestHandler<PathKeyT, RequestParameterT> dispatcher() {
		DispatchModelRequestHandler<PathKeyT, RequestParameterT> handler
				= new DispatchModelRequestHandler<PathKeyT, RequestParameterT>();
		state.putRules(handler);
		return handler;
	}

	public FreeReturnT withRedirectBase(ValueHolder<? extends URLBuilder> baseURL) {
		state.setBaseURL(baseURL);
		return freeThis;
	}

	public FreeReturnT withRedirectBase(String baseURL) {
		state.setBaseURL(baseURL == null ? null : ValueHolder.always(URLBuilder.decompose(baseURL)));
		return freeThis;
	}

	public FreeReturnT withRedirectBase(URL baseURL) {
		state.setBaseURL(baseURL == null ? null : ValueHolder.always(new URLBuilder(baseURL)));
		return freeThis;
	}

	public FreeReturnT withRedirectBase(URI baseURL) {
		state.setBaseURL(baseURL == null ? null : ValueHolder.always(new URLBuilder(baseURL)));
		return freeThis;
	}

	public FreeReturnT withoutRedirectBase() {
		return freeThis;
	}

	public FreeReturnT on(HTTPVerb... verbs) {
		state.addVerbs(verbs);
		return freeThis;
	}

	public FreeReturnT andOn(HTTPVerb... verbs) {
		state.clearVerbs();
		state.addVerbs(verbs);
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT at(PathMatcher<PathKeyT>... path) {
		state.addHead(path);
		state.clearTail();
		return freeThis;
	}

	public FreeReturnT at(String... path) {
		state.addHead(path);
		state.clearTail();
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT andAt(PathMatcher<PathKeyT>... path) {
		state.clearHead();
		state.addHead(path);
		state.clearTail();
		return freeThis;
	}

	public FreeReturnT andAt(String... path) {
		state.clearHead();
		state.addHead(path);
		state.clearTail();
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT followedBy(PathMatcher<PathKeyT>... path) {
		state.clearTail();
		state.addTail(path);
		return freeThis;
	}

	public FreeReturnT followedBy(String... path) {
		state.clearTail();
		state.addTail(path);
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT orFollowedBy(PathMatcher<PathKeyT>... path) {
		state.addTail(path);
		return freeThis;
	}

	public FreeReturnT orFollowedBy(String... path) {
		state.addTail(path);
		return freeThis;
	}

	public FreeReturnT dot(String extension) {
		state.clearTail();
		if(extension != null)
			state.addTail('.' + extension);
		return freeThis;
	}

	public FreeReturnT html() {
		state.clearTail();
		state.addTail(".html");
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT html(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".html");
		return freeThis;
	}

	public FreeReturnT html(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".html");
		return freeThis;
	}

	public FreeReturnT json() {
		state.clearTail();
		state.addTail(".json");
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT json(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".json");
		return freeThis;
	}

	public FreeReturnT json(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".json");
		return freeThis;
	}

	public FreeReturnT xml() {
		state.clearTail();
		state.addTail(".xml");
		return freeThis;
	}

	@SuppressWarnings("unchecked")
	public FreeReturnT xml(PathMatcher<PathKeyT>... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".xml");
		return freeThis;
	}

	public FreeReturnT xml(String... head) {
		state.clearHead();
		state.addHead(head);
		state.clearTail();
		state.addTail(".xml");
		return freeThis;
	}

	public FreeReturnT content(String... contentTypes) {
		state.clearContentTypes();
		state.addContentTypes(contentTypes);
		return freeThis;
	}

	public FreeReturnT orContent(String... contentTypes) {
		state.addContentTypes(contentTypes);
		return freeThis;
	}

	public FreeReturnT onJSON() {
		state.clearContentTypes();
		state.addContentTypes(ContentTypes.JSON);
		return freeThis;
	}

	public FreeReturnT onXML() {
		state.clearContentTypes();
		state.addContentTypes(ContentTypes.XML);
		return freeThis;
	}

	protected abstract RefreeReturnT refree();

	protected void reset() {
		state.clearVerbs();
		state.clearHead();
		state.clearTail();
		state.clearContentTypes();
	}

	public RefreeReturnT handle(RequestHandler handler) {
		state.emitRule(ParameterizedRequestHandler.ignore(handler));
		return refree();
	}

	public RefreeReturnT handleAnd(RequestHandler handler) {
		state.emitRule(ParameterizedRequestHandler.ignore(handler));
		reset();
		return refree();
	}

	public RefreeReturnT handle(ParameterizedRequestHandler<? super RequestParameterT> handler) {
		state.emitRule(handler);
		return refree();
	}

	public RefreeReturnT handleAnd(ParameterizedRequestHandler<? super RequestParameterT> handler) {
		state.emitRule(handler);
		reset();
		return refree();
	}

	private <ModelT, ControllerParameterT, ViewParameterT>
	RefreeReturnT mvc(boolean shouldReset,
			ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		state.emitRule(new MVCRequestHandler<RequestParameterT, ModelT, ControllerParameterT,
				ViewParameterT>(controller, view, controllerParameterTransform, viewParameterTransform));
		if(shouldReset)
			reset();
		return refree();
	}

	public <ModelT> RefreeReturnT handle(Controller<? extends ModelT> controller, View<? super ModelT> view) {
		return mvc(false, ParameterizedController.ignore(controller), ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT handleAnd(Controller<? extends ModelT> controller, View<? super ModelT> view) {
		return mvc(true, ParameterizedController.ignore(controller), ParameterizedView.ignore(view),
				Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view) {
		return mvc(false, controller, ParameterizedView.ignore(view), Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			View<? super ModelT> view) {
		return mvc(true, controller, ParameterizedView.ignore(view), Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT handle(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return mvc(false, ParameterizedController.ignore(controller), view, Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT handleAnd(Controller<? extends ModelT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return mvc(true, ParameterizedController.ignore(controller), view, Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return mvc(false, controller, view, Transform.widen(), Transform.widen());
	}

	public <ModelT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ? super RequestParameterT> controller,
			ParameterizedView<? super ModelT, ? super RequestParameterT> view) {
		return mvc(true, controller, view, Transform.widen(), Transform.widen());
	}

	public <ModelT, ControllerParameterT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view) {
		return mvc(false, controller, ParameterizedView.ignore(view),
				controllerParameterTransform, Transform.widen());
	}

	public <ModelT, ControllerParameterT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super ModelT> view) {
		return mvc(true, controller, ParameterizedView.ignore(view),
				controllerParameterTransform, Transform.widen());
	}

	public <ModelT, ViewParameterT> RefreeReturnT
	handle(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return mvc(false, ParameterizedController.ignore(controller), view,
				Transform.widen(), viewParameterTransform);
	}

	public <ModelT, ViewParameterT> RefreeReturnT
	handleAnd(Controller<? extends ModelT> controller, ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return mvc(true, ParameterizedController.ignore(controller), view,
				Transform.widen(), viewParameterTransform);
	}

	public <ModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handle(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return mvc(false, controller, view, controllerParameterTransform, viewParameterTransform);
	}

	public <ModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handleAnd(ParameterizedController<? extends ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		return mvc(true, controller, view, controllerParameterTransform, viewParameterTransform);
	}

	public <AddressT> RefreeReturnT
	performAndRedirect(Controller<? extends RedirectLocation<AddressT>> controller) {
		return mvc(false, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new RedirectView<AddressT>()),
				Transform.widen(), Transform.widen());
	}

	public <AddressT> RefreeReturnT
	performAndRedirectAnd(Controller<? extends RedirectLocation<AddressT>> controller) {
		return mvc(true, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new RedirectView<AddressT>()),
				Transform.widen(), Transform.widen());
	}

	public <AddressT> RefreeReturnT performAndRedirect(ParameterizedController<? extends RedirectLocation<AddressT>,
			? super RequestParameterT> controller) {
		return mvc(false, controller, ParameterizedView.ignore(new RedirectView<AddressT>()),
				Transform.widen(), Transform.widen());
	}

	public <AddressT> RefreeReturnT
	performAndRedirectAnd(ParameterizedController<? extends RedirectLocation<AddressT>,
			? super RequestParameterT> controller) {
		return mvc(true, controller, ParameterizedView.ignore(new RedirectView<AddressT>()),
				Transform.widen(), Transform.widen());
	}

	public <AddressT, ControllerParameterT>
	RefreeReturnT performAndRedirect(ParameterizedController<? extends RedirectLocation<AddressT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		return mvc(false, controller, ParameterizedView.ignore(new RedirectView<AddressT>()),
				controllerParameterTransform, Transform.widen());
	}

	public <AddressT, ControllerParameterT>
	RefreeReturnT performAndRedirectAnd(ParameterizedController<? extends RedirectLocation<AddressT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		return mvc(true, controller, ParameterizedView.ignore(new RedirectView<AddressT>()),
				controllerParameterTransform, Transform.widen());
	}

	public RefreeReturnT performAndRedirect(Controller<? extends RedirectLocation<URLBuilder>> controller,
			ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(false, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirectAnd(Controller<? extends RedirectLocation<URLBuilder>> controller,
			ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(true, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirect(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			? super RequestParameterT> controller, ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(false, controller, ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirectAnd(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			? super RequestParameterT> controller, ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(true, controller, ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				Transform.widen(), Transform.widen());
	}

	public <ControllerParameterT> RefreeReturnT
	performAndRedirect(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(false, controller, ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				controllerParameterTransform, Transform.widen());
	}

	public <ControllerParameterT> RefreeReturnT
	performAndRedirectAnd(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ValueHolder<? extends URLBuilder> baseURL) {
		return mvc(true, controller, ParameterizedView.ignore(new BasedRedirectView(baseURL)),
				controllerParameterTransform, Transform.widen());
	}

	public RefreeReturnT performAndRedirectBased(Controller<? extends RedirectLocation<URLBuilder>> controller) {
		return mvc(false, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirectBasedAnd(Controller<? extends RedirectLocation<URLBuilder>> controller) {
		return mvc(true, ParameterizedController.ignore(controller),
				ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirectBased(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			? super RequestParameterT> controller) {
		return mvc(false, controller, ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				Transform.widen(), Transform.widen());
	}

	public RefreeReturnT performAndRedirectBasedAnd(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			? super RequestParameterT> controller) {
		return mvc(true, controller, ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				Transform.widen(), Transform.widen());
	}

	public <ControllerParameterT> RefreeReturnT
	performAndRedirectBased(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		return mvc(false, controller, ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				controllerParameterTransform, Transform.widen());
	}

	public <ControllerParameterT> RefreeReturnT
	performAndRedirectBasedAnd(ParameterizedController<? extends RedirectLocation<URLBuilder>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		return mvc(true, controller, ParameterizedView.ignore(new BasedRedirectView(state.getBaseURL())),
				controllerParameterTransform, Transform.widen());
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirect(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirectAnd(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirect(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			? super RequestParameterT> controller, View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirectAnd(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			? super RequestParameterT> controller, View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirect(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			ParameterizedView<? super InnerModelT, ? super RequestParameterT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirectAnd(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			ParameterizedView<? super InnerModelT, ? super RequestParameterT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirect(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			? super RequestParameterT> controller,
			ParameterizedView<? super InnerModelT, ? super RequestParameterT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT> RefreeReturnT
	handleAndMaybeRedirectAnd(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			? super RequestParameterT> controller,
			ParameterizedView<? super InnerModelT, ? super RequestParameterT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ControllerParameterT> RefreeReturnT
	handleAndMaybeRedirect(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ControllerParameterT> RefreeReturnT
	handleAndMaybeRedirectAnd(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			View<? super InnerModelT> view) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ViewParameterT> RefreeReturnT
	handleAndMaybeRedirect(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			ParameterizedView<? super InnerModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ViewParameterT> RefreeReturnT
	handleAndMaybeRedirectAnd(Controller<? extends MaybeRedirectModel<AddressT, InnerModelT>> controller,
			ParameterizedView<? super InnerModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handleAndMaybeRedirect(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super InnerModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		//TODO
		return null;
	}

	public <AddressT, InnerModelT, ControllerParameterT, ViewParameterT> RefreeReturnT
	handleAndMaybeRedirectAnd(ParameterizedController<? extends MaybeRedirectModel<AddressT, InnerModelT>,
			ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform,
			ParameterizedView<? super InnerModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform) {
		//TODO
		return null;
	}

	public <ModelT> ControllerBoundBuilder<PathKeyT, RequestParameterT, RequestParameterT, ModelT>
	perform(Controller<ModelT> controller) {
		return new ControllerBoundBuilder<PathKeyT, RequestParameterT, RequestParameterT, ModelT>(state,
				ParameterizedController.ignore(controller), Transform.widen());
	}

	public <ModelT> ControllerBoundBuilder<PathKeyT, RequestParameterT, RequestParameterT, ModelT>
	perform(ParameterizedController<ModelT, ? super RequestParameterT> controller) {
		return new ControllerBoundBuilder<PathKeyT, RequestParameterT, RequestParameterT, ModelT>(state,
				controller, Transform.widen());
	}

	public <ModelT, ControllerParameterT>
	ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>
	perform(ParameterizedController<ModelT, ControllerParameterT> controller,
			Transform<? super RequestParameterT, ? extends ControllerParameterT> controllerParameterTransform) {
		return new ControllerBoundBuilder<PathKeyT, RequestParameterT, ControllerParameterT, ModelT>(state,
				controller, controllerParameterTransform);
	}

}
