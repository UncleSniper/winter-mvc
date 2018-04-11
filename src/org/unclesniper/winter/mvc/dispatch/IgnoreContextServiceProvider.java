package org.unclesniper.winter.mvc.dispatch;

import java.io.IOException;

public class IgnoreContextServiceProvider<ServiceT, ContextT> implements ContextServiceProvider<ServiceT, ContextT> {

	private ServiceProvider<ServiceT> provider;

	public IgnoreContextServiceProvider(ServiceProvider<ServiceT> provider) {
		this.provider = provider;
	}

	public ServiceProvider<ServiceT> getProvider() {
		return provider;
	}

	public void setProvider(ServiceProvider<ServiceT> provider) {
		this.provider = provider;
	}

	public ServiceT acquireService(ContextT context) throws IOException {
		return provider.acquireService();
	}

	public void releaseService(ContextT context, ServiceT service) throws IOException {
		provider.releaseService(service);
	}

}
