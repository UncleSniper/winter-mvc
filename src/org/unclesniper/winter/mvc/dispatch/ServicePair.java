package org.unclesniper.winter.mvc.dispatch;

public interface ServicePair<AppServiceT, RequestServiceT> {

	AppServiceT getAppService();

	RequestServiceT getRequestService();

}
