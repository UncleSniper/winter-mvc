package org.unclesniper.winter.mvc.lens;

public abstract class AbstractExtractParameter {

	private String parameter;

	private boolean required;

	public AbstractExtractParameter(String parameter) {
		this(parameter, true);
	}

	public AbstractExtractParameter(String parameter, boolean required) {
		this.parameter = parameter;
		this.required = required;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
