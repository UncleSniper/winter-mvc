package org.unclesniper.winter.mvc;

public class Doom extends Error {

	public Doom(String message) {
		super(message);
	}

	public Doom(String message, Throwable cause) {
		super(message, cause);
	}

}
