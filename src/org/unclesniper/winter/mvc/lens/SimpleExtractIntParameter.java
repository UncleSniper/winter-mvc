package org.unclesniper.winter.mvc.lens;

import org.unclesniper.winter.mvc.error.MissingRequestParameterException;
import org.unclesniper.winter.mvc.error.MalformedRequestParameterException;

public class SimpleExtractIntParameter extends AbstractExtractParameter implements ExtractIntParameter {

	private int radix;

	private int lowerBound;

	private int upperBound;

	private int defaultValue;

	private boolean emptyIsDefault;

	public SimpleExtractIntParameter(String parameter) {
		this(parameter, true);
	}

	public SimpleExtractIntParameter(String parameter, boolean required) {
		super(parameter, required);
		radix = 10;
		lowerBound = Integer.MIN_VALUE;
		upperBound = Integer.MAX_VALUE;
	}

	public SimpleExtractIntParameter(String parameter, int radix, int lowerBound, int upperBound) {
		this(parameter, true, radix, lowerBound, upperBound);
	}

	public SimpleExtractIntParameter(String parameter, boolean required,
			int radix, int lowerBound, int upperBound) {
		super(parameter, required);
		setRadix(radix);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public SimpleExtractIntParameter(String parameter, int radix, int lowerBound, int upperBound,
			int defaultValue, boolean emptyIsDefault) {
		this(parameter, true, radix, lowerBound, upperBound, defaultValue, emptyIsDefault);
	}

	public SimpleExtractIntParameter(String parameter, boolean required,
			int radix, int lowerBound, int upperBound, int defaultValue, boolean emptyIsDefault) {
		super(parameter, required);
		setRadix(radix);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.defaultValue = defaultValue;
		this.emptyIsDefault = emptyIsDefault;
	}

	public int getRadix() {
		return radix;
	}

	public void setRadix(int radix) {
		if(radix > 0 && (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX))
			throw new IllegalArgumentException("Illegal radix: " + radix);
		this.radix = radix > 0 ? radix : 10;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public void removeLowerBound() {
		lowerBound = Integer.MIN_VALUE;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public void removeUpperBound() {
		upperBound = Integer.MAX_VALUE;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isEmptyIsDefault() {
		return emptyIsDefault;
	}

	public void setEmptyIsDefault(boolean emptyIsDefault) {
		this.emptyIsDefault = emptyIsDefault;
	}

	@Override
	public int extractIntParameter(ParameterSource parameters) {
		String spec = parameters.getParameter(getParameter());
		if(spec == null) {
			if(isRequired())
				throw new MissingRequestParameterException(getParameter());
			return defaultValue;
		}
		spec = spec.trim();
		if(emptyIsDefault && spec.length() == 0)
			return defaultValue;
		int value;
		try {
			value = Integer.parseInt(spec, radix);
		}
		catch(NumberFormatException nfe) {
			throw new MalformedRequestParameterException(getParameter(), "an int", spec);
		}
		if(value < lowerBound)
			throw new MalformedRequestParameterException(getParameter(), ">= " + lowerBound, spec);
		if(value > upperBound)
			throw new MalformedRequestParameterException(getParameter(), "<= " + upperBound, spec);
		return value;
	}

}
