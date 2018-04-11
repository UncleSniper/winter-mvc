package org.unclesniper.winter.mvc.dispatch;

import java.io.IOException;

public interface ContextServiceProvider<ServiceT, ContextT> {

	ServiceT acquireService(ContextT context) throws IOException;

	void releaseService(ContextT context, ServiceT service) throws IOException;

	public static <ServiceT, ContextT>
	ContextServiceProvider<ServiceT, ContextT> ignore(ServiceProvider<ServiceT> provider) {
		return new IgnoreContextServiceProvider<ServiceT, ContextT>(provider);
	}

}
