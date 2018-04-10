package org.unclesniper.winter.mvc;

public class HTTPServiceException extends Exception {

	public HTTPServiceException(String message) {
		super(message);
	}

	public HTTPServiceException(Throwable cause) {
		super(cause);
	}

	public HTTPServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
