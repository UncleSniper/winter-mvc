package org.unclesniper.winter.mvc.dispatch;

public interface ServiceHolder<ServiceT> {

	ServiceT getService();

	public static <ServiceT> ServiceHolder<ServiceT> constant(ServiceT service) {
		return () -> service;
	}

}
