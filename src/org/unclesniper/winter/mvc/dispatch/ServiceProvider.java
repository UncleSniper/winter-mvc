package org.unclesniper.winter.mvc.dispatch;

import java.io.IOException;

public interface ServiceProvider<ServiceT> {

	ServiceT acquireService() throws IOException;

	void releaseService(ServiceT service) throws IOException;

}
