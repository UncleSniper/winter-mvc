package org.unclesniper.winter.mvc;

public final class ErrorContext {

	private Throwable exception;

	private HTTPRequest request;

	public ErrorContext(Throwable exception, HTTPRequest request) {
		this.exception = exception;
		this.request = request;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public HTTPRequest getRequest() {
		return request;
	}

	public void setRequest(HTTPRequest request) {
		this.request = request;
	}

}
