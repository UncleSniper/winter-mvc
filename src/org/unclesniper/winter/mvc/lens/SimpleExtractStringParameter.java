package org.unclesniper.winter.mvc.lens;

import org.unclesniper.winter.mvc.Doom;
import org.unclesniper.winter.mvc.error.MissingRequestParameterException;

public class SimpleExtractStringParameter extends AbstractExtractParameter
		implements ExtractObjectParameter<String> {

	public enum Casing {
		KEEP,
		UPPER,
		LOWER
	}

	private Casing casing;

	private boolean trimValue;

	private String defaultValue;

	private boolean emptyIsDefault;

	public SimpleExtractStringParameter(String parameter) {
		this(parameter, true);
	}

	public SimpleExtractStringParameter(String parameter, boolean required) {
		super(parameter, required);
		casing = Casing.KEEP;
	}

	public SimpleExtractStringParameter(String parameter, Casing casing, boolean trimValue) {
		this(parameter, true, casing, trimValue);
	}

	public SimpleExtractStringParameter(String parameter, boolean required, Casing casing, boolean trimValue) {
		super(parameter, required);
		this.casing = casing == null ? Casing.KEEP : casing;
		this.trimValue = trimValue;
	}

	public SimpleExtractStringParameter(String parameter, String defaultValue, boolean emptyIsDefault) {
		this(parameter, true, defaultValue, emptyIsDefault);
	}

	public SimpleExtractStringParameter(String parameter, boolean required,
			String defaultValue, boolean emptyIsDefault) {
		super(parameter, required);
		casing = Casing.KEEP;
		this.defaultValue = defaultValue;
		this.emptyIsDefault = emptyIsDefault;
	}

	public SimpleExtractStringParameter(String parameter, Casing casing, boolean trimValue,
			String defaultValue, boolean emptyIsDefault) {
		this(parameter, true, casing, trimValue, defaultValue, emptyIsDefault);
	}

	public SimpleExtractStringParameter(String parameter, boolean required, Casing casing, boolean trimValue,
			String defaultValue, boolean emptyIsDefault) {
		super(parameter, required);
		this.casing = casing == null ? Casing.KEEP : casing;
		this.trimValue = trimValue;
		this.defaultValue = defaultValue;
		this.emptyIsDefault = emptyIsDefault;
	}

	public Casing getCasing() {
		return casing;
	}

	public void setCasing(Casing casing) {
		this.casing = casing == null ? Casing.KEEP : casing;
	}

	public boolean isTrimValue() {
		return trimValue;
	}

	public void setTrimValue(boolean trimValue) {
		this.trimValue = trimValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isEmptyIsDefault() {
		return emptyIsDefault;
	}

	public void setEmptyIsDefault(boolean emptyIsDefault) {
		this.emptyIsDefault = emptyIsDefault;
	}

	@Override
	public String extractObjectParameter(ParameterSource parameters) {
		String spec = parameters.getParameter(getParameter());
		if(spec == null) {
			if(isRequired())
				throw new MissingRequestParameterException(getParameter());
			return defaultValue;
		}
		if(trimValue)
			spec = spec.trim();
		if(emptyIsDefault && spec.length() == 0)
			return defaultValue;
		switch(casing) {
			case KEEP:
				return spec;
			case UPPER:
				return spec.toUpperCase();
			case LOWER:
				return spec.toLowerCase();
			default:
				throw new Doom("Unrecognized casing: " + casing.name());
		}
	}

}
