package org.unclesniper.winter.mvc;

import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.dispatch.ServicePair;
import org.unclesniper.winter.mvc.dispatch.PathParameters;

public class DefaultRequestParameter<PathKeyT, AppServiceT, RequestServiceT>
		implements ServicePair<AppServiceT, RequestServiceT> {

	private PathParameters<PathKeyT> pathParameters;

	private AppServiceT appService;

	private RequestServiceT requestService;

	private ErrorContext errorContext;

	public DefaultRequestParameter() {}

	public DefaultRequestParameter(PathParameters<PathKeyT> pathParameters, AppServiceT appService,
			RequestServiceT requestService, ErrorContext errorContext) {
		this.pathParameters = pathParameters;
		this.appService = appService;
		this.requestService = requestService;
		this.errorContext = errorContext;
	}

	public PathParameters<PathKeyT> getPathParameters() {
		return pathParameters;
	}

	public void setPathParameters(PathParameters<PathKeyT> pathParameters) {
		this.pathParameters = pathParameters;
	}

	public AppServiceT getAppService() {
		return appService;
	}

	public void setAppService(AppServiceT appService) {
		this.appService = appService;
	}

	public RequestServiceT getRequestService() {
		return requestService;
	}

	public void setRequestService(RequestServiceT requestService) {
		this.requestService = requestService;
	}

	public ErrorContext getErrorContext() {
		return errorContext;
	}

	public void setErrorContext(ErrorContext errorContext) {
		this.errorContext = errorContext;
	}

	public <NewAppServiceT, NewRequestServiceT>
	DefaultRequestParameter<PathKeyT, NewAppServiceT, NewRequestServiceT> mapServices(
			Transform<? super AppServiceT, ? extends NewAppServiceT> appServiceTransform,
			Transform<? super RequestServiceT, ? extends NewRequestServiceT> requestServiceTransform) {
		return new DefaultRequestParameter<PathKeyT, NewAppServiceT, NewRequestServiceT>(pathParameters,
				appServiceTransform.transform(appService), requestServiceTransform.transform(requestService),
				errorContext);
	}

}
