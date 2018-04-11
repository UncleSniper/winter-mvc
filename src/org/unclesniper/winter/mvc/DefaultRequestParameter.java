package org.unclesniper.winter.mvc;

import org.unclesniper.winter.mvc.dispatch.ServicePair;
import org.unclesniper.winter.mvc.dispatch.PathParameters;

public class DefaultRequestParameter<PathKeyT, AppServiceT, RequestServiceT>
		implements ServicePair<AppServiceT, RequestServiceT> {

	private PathParameters<PathKeyT> pathParameters;

	private AppServiceT appService;

	private RequestServiceT requestService;

	private ErrorContext errorContext;

	public DefaultRequestParameter() {}

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

}
