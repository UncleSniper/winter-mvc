package org.unclesniper.winter.mvc.util;

public class IllegalURIException extends IllegalArgumentException {

	private final String specifier;

	private final int offset;

	public IllegalURIException(String specifier, int offset) {
		this(specifier, offset, null);
	}

	public IllegalURIException(String specifier, int offset, Throwable cause) {
		super("URI is illegal at offset " + offset + ": " + specifier, cause);
		this.specifier = specifier;
		this.offset = offset;
	}

	public String getSpecifier() {
		return specifier;
	}

	public int getOffset() {
		return offset;
	}

}
