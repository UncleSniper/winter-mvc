package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.dispatch.ServiceHolder;
import org.unclesniper.winter.mvc.dispatch.ServiceProvider;
import org.unclesniper.winter.mvc.dispatch.HolderServiceProvider;
import org.unclesniper.winter.mvc.dispatch.ContextServiceProvider;
import org.unclesniper.winter.mvc.dispatch.NoRouteForRequestException;

public class ServiceBoundRequestHandler<AppServiceT, RequestServiceT, ParameterT>
		implements ParameterizedRequestHandler<ParameterT> {

	public static final class RequestContext<AppServiceT> {

		private final AppServiceT appService;

		private final HTTPRequest request;

		public RequestContext(AppServiceT appService, HTTPRequest request) {
			this.appService = appService;
			this.request = request;
		}

		public AppServiceT getAppService() {
			return appService;
		}

		public HTTPRequest getRequest() {
			return request;
		}

	}

	public static final class AppServiceOnlyRequestServiceProvider<AppServiceT, RequestServiceT>
			implements ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> {

		private ContextServiceProvider<RequestServiceT, AppServiceT> requestServiceProvider;

		public AppServiceOnlyRequestServiceProvider(ContextServiceProvider<RequestServiceT, AppServiceT>
				requestServiceProvider) {
			this.requestServiceProvider = requestServiceProvider;
		}

		public ContextServiceProvider<RequestServiceT, AppServiceT> getRequestServiceProvider() {
			return requestServiceProvider;
		}

		public void setRequestServiceProvider(ContextServiceProvider<RequestServiceT, AppServiceT>
				requestServiceProvider) {
			this.requestServiceProvider = requestServiceProvider;
		}

		public RequestServiceT acquireService(RequestContext<AppServiceT> context) throws IOException {
			return requestServiceProvider.acquireService(context == null ? null : context.getAppService());
		}

		public void releaseService(RequestContext<AppServiceT> context, RequestServiceT service)
				throws IOException {
			requestServiceProvider.releaseService(context == null ? null : context.getAppService(), service);
		}

	}

	public static final class RequestOnlyRequestServiceProvider<AppServiceT, RequestServiceT>
			implements ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> {

		private ContextServiceProvider<RequestServiceT, HTTPRequest> requestServiceProvider;

		public RequestOnlyRequestServiceProvider(ContextServiceProvider<RequestServiceT, HTTPRequest>
				requestServiceProvider) {
			this.requestServiceProvider = requestServiceProvider;
		}

		public ContextServiceProvider<RequestServiceT, HTTPRequest> getRequestServiceProvider() {
			return requestServiceProvider;
		}

		public void setRequestServiceProvider(ContextServiceProvider<RequestServiceT, HTTPRequest>
				requestServiceProvider) {
			this.requestServiceProvider = requestServiceProvider;
		}

		public RequestServiceT acquireService(RequestContext<AppServiceT> context) throws IOException {
			return requestServiceProvider.acquireService(context == null ? null : context.getRequest());
		}

		public void releaseService(RequestContext<AppServiceT> context, RequestServiceT service)
				throws IOException {
			requestServiceProvider.releaseService(context == null ? null : context.getRequest(), service);
		}

	}

	private static final class RequestServiceHolder<AppServiceT, RequestServiceT> implements AutoCloseable {

		private ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> requestServiceProvider;

		private RequestContext<AppServiceT> requestContext;

		private RequestServiceT requestService;

		public RequestServiceHolder(ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>>
				requestServiceProvider, RequestContext<AppServiceT> requestContext,
				RequestServiceT requestService) {
			this.requestServiceProvider = requestServiceProvider;
			this.requestContext = requestContext;
			this.requestService = requestService;
		}

		public void close() throws IOException {
			if(requestServiceProvider != null && requestService != null) {
				ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> rsp = requestServiceProvider;
				RequestServiceT rs = requestService;
				requestServiceProvider = null;
				requestService = null;
				rsp.releaseService(requestContext, rs);
			}
		}

	}

	private ParameterizedRequestHandler<ParameterT> requestHandler;

	private final Object appServiceLock = new Object();

	private volatile ServiceProvider<AppServiceT> appServiceProvider;

	private volatile AppServiceT appService;

	private ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> requestServiceProvider;

	private RequestParameterMerger<ParameterT, AppServiceT> appServiceMerger;

	private RequestParameterMerger<ParameterT, RequestServiceT> requestServiceMerger;

	public ServiceBoundRequestHandler() {}

	public ServiceBoundRequestHandler(ParameterizedRequestHandler<ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public ServiceBoundRequestHandler(ParameterizedRequestHandler<ParameterT> requestHandler,
			ServiceProvider<AppServiceT> appServiceProvider,
			ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> requestServiceProvider,
			RequestParameterMerger<ParameterT, AppServiceT> appServiceMerger,
			RequestParameterMerger<ParameterT, RequestServiceT> requestServiceMerger) {
		this.requestHandler = requestHandler;
		this.appServiceProvider = appServiceProvider;
		this.requestServiceProvider = requestServiceProvider;
		this.appServiceMerger = appServiceMerger;
		this.requestServiceMerger = requestServiceMerger;
	}

	public ParameterizedRequestHandler<ParameterT> getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(ParameterizedRequestHandler<ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler == null ? null : ParameterizedRequestHandler.ignore(requestHandler);
	}

	public ServiceProvider<AppServiceT> getAppServiceProvider() {
		return appServiceProvider;
	}

	public void setAppServiceProvider(ServiceProvider<AppServiceT> appServiceProvider) throws IOException {
		synchronized(appServiceLock) {
			if(appService != null) {
				try {
					if(appServiceProvider != null)
						appServiceProvider.releaseService(appService);
				}
				finally {
					appService = null;
					appServiceProvider = null;
				}
			}
			this.appServiceProvider = appServiceProvider;
		}
	}

	public void setAppServiceProvider(ServiceHolder<AppServiceT> appServiceHolder) throws IOException {
		setAppServiceProvider(appServiceHolder == null
				? null : new HolderServiceProvider<AppServiceT>(appServiceHolder));
	}

	public ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> getRequestServiceProvider() {
		return requestServiceProvider;
	}

	public void setRequestServiceProvider(ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>>
			requestServiceProvider) {
		this.requestServiceProvider = requestServiceProvider;
	}

	public void setAppServiceOnlyRequestServiceProvider(ContextServiceProvider<RequestServiceT, AppServiceT>
			requestServiceProvider) {
		this.requestServiceProvider = requestServiceProvider == null ? null
				: new AppServiceOnlyRequestServiceProvider<AppServiceT, RequestServiceT>(requestServiceProvider);
	}

	public void setRequestOnlyRequestServiceProvider(ContextServiceProvider<RequestServiceT, HTTPRequest>
			requestServiceProvider) {
		this.requestServiceProvider = requestServiceProvider == null ? null
				: new RequestOnlyRequestServiceProvider<AppServiceT, RequestServiceT>(requestServiceProvider);
	}

	public void setRequestServiceProvider(ServiceProvider<RequestServiceT> requestServiceProvider) {
		this.requestServiceProvider = requestServiceProvider == null ? null
				: ContextServiceProvider.ignore(requestServiceProvider);
	}

	public RequestParameterMerger<ParameterT, AppServiceT> getAppServiceMerger() {
		return appServiceMerger;
	}

	public void setAppServiceMerger(RequestParameterMerger<ParameterT, AppServiceT> appServiceMerger) {
		this.appServiceMerger = appServiceMerger;
	}

	public RequestParameterMerger<ParameterT, RequestServiceT> getRequestServiceMerger() {
		return requestServiceMerger;
	}

	public void setRequestServiceMerger(RequestParameterMerger<ParameterT, RequestServiceT> requestServiceMerger) {
		this.requestServiceMerger = requestServiceMerger;
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response, ParameterT parameter)
			throws IOException, HTTPServiceException {
		AppServiceT as = appService;
		RequestParameterMerger<ParameterT, AppServiceT> asm = appServiceMerger;
		if(as == null && appServiceProvider != null && asm != null) {
			synchronized(appServiceLock) {
				if(appService == null) {
					if(appServiceProvider != null)
						appService = as = appServiceProvider.acquireService();
				}
				else
					as = appService;
			}
		}
		ContextServiceProvider<RequestServiceT, RequestContext<AppServiceT>> rsp = requestServiceProvider;
		RequestServiceT rs;
		RequestContext<AppServiceT> rctx;
		RequestParameterMerger<ParameterT, RequestServiceT> rsm = requestServiceMerger;
		if(rsp == null || rsm == null) {
			rctx = null;
			rs = null;
		}
		else {
			rctx = new RequestContext<AppServiceT>(as, request);
			rs = rsp.acquireService(rctx);
		}
		try(RequestServiceHolder<AppServiceT, RequestServiceT> holder
				= new RequestServiceHolder<AppServiceT, RequestServiceT>(rsp, rctx, rs)) {
			if(as != null && asm != null)
				asm.mergeRequestParameter(parameter, as);
			if(rs != null && rsm != null)
				rsm.mergeRequestParameter(parameter, rs);
			if(requestHandler == null)
				throw new NoRouteForRequestException(request.getMethod(), request.getPath());
			requestHandler.handleRequest(request, response, parameter);
		}
	}

}
