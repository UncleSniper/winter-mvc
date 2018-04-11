package org.unclesniper.winter.mvc.dispatch;

public final class HolderServiceProvider<ServiceT> implements ServiceProvider<ServiceT> {

	private ServiceHolder<ServiceT> holder;

	public HolderServiceProvider(ServiceHolder<ServiceT> holder) {
		this.holder = holder;
	}

	public ServiceHolder<ServiceT> getHolder() {
		return holder;
	}

	public void setHolder(ServiceHolder<ServiceT> holder) {
		this.holder = holder;
	}

	public ServiceT acquireService() {
		return holder.getService();
	}

	public void releaseService(ServiceT service) {}

}
