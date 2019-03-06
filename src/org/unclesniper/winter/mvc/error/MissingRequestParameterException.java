package org.unclesniper.winter.mvc.error;

public class MissingRequestParameterException extends BadRequestException {

	private final String parameterName;

	public MissingRequestParameterException(String parameterName) {
		super("Missing request parameter: " + parameterName);
		this.parameterName = parameterName;
	}

	public String getParameterName() {
		return parameterName;
	}

}
