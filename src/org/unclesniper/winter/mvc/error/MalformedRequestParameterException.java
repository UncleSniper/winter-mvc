package org.unclesniper.winter.mvc.error;

public class MalformedRequestParameterException extends BadRequestException {

	private final String parameterName;

	private final String expectedForm;

	private final String actualValue;

	public MalformedRequestParameterException(String parameterName, String expectedForm, String actualValue) {
		this(parameterName, expectedForm, actualValue, null);
	}

	public MalformedRequestParameterException(String parameterName, String expectedForm, String actualValue,
			Throwable cause) {
		super("Request parameter '" + parameterName + "' must be " + expectedForm
				+ ", but was: " + actualValue, cause);
		this.parameterName = parameterName;
		this.expectedForm = expectedForm;
		this.actualValue = actualValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public String getExpectedForm() {
		return expectedForm;
	}

	public String getActualValue() {
		return actualValue;
	}

}
