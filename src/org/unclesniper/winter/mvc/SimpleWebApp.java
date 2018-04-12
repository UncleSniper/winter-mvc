package org.unclesniper.winter.mvc;

public class SimpleWebApp implements WebApp {

	private RequestHandler rootRequestHandler;

	public SimpleWebApp(RequestHandler rootRequestHandler) {
		this.rootRequestHandler = rootRequestHandler;
	}

	public RequestHandler getRootRequestHandler() {
		return rootRequestHandler;
	}

	public void setRootRequestHandler(RequestHandler rootRequestHandler) {
		this.rootRequestHandler = rootRequestHandler;
	}

}
