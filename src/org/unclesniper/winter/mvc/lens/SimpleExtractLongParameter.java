package org.unclesniper.winter.mvc.lens;

import org.unclesniper.winter.mvc.error.MissingRequestParameterException;
import org.unclesniper.winter.mvc.error.MalformedRequestParameterException;

public class SimpleExtractLongParameter extends AbstractExtractParameter implements ExtractLongParameter {

	private int radix;

	private long lowerBound;

	private long upperBound;

	private long defaultValue;

	private boolean emptyIsDefault;

	public SimpleExtractLongParameter(String parameter) {
		this(parameter, true);
	}

	public SimpleExtractLongParameter(String parameter, boolean required) {
		super(parameter, required);
		radix = 10;
		lowerBound = Long.MIN_VALUE;
		upperBound = Long.MAX_VALUE;
	}

	public SimpleExtractLongParameter(String parameter, int radix, long lowerBound, long upperBound) {
		this(parameter, true, radix, lowerBound, upperBound);
	}

	public SimpleExtractLongParameter(String parameter, boolean required,
			int radix, long lowerBound, long upperBound) {
		super(parameter, required);
		setRadix(radix);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public SimpleExtractLongParameter(String parameter, int radix, long lowerBound, long upperBound,
			long defaultValue, boolean emptyIsDefault) {
		this(parameter, true, radix, lowerBound, upperBound, defaultValue, emptyIsDefault);
	}

	public SimpleExtractLongParameter(String parameter, boolean required,
			int radix, long lowerBound, long upperBound, long defaultValue, boolean emptyIsDefault) {
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
		this.radix = radix;
	}

	public long getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(long lowerBound) {
		this.lowerBound = lowerBound;
	}

	public void removeLowerBound() {
		lowerBound = Long.MIN_VALUE;
	}

	public long getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(long upperBound) {
		this.upperBound = upperBound;
	}

	public void removeUpperBound() {
		upperBound = Long.MAX_VALUE;
	}

	public long getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isEmptyIsDefault() {
		return emptyIsDefault;
	}

	public void setEmptyIsDefault(boolean emptyIsDefault) {
		this.emptyIsDefault = emptyIsDefault;
	}

	@Override
	public long extractLongParameter(ParameterSource parameters) {
		String spec = parameters.getParameter(getParameter());
		if(spec == null) {
			if(isRequired())
				throw new MissingRequestParameterException(getParameter());
			return defaultValue;
		}
		spec = spec.trim();
		if(emptyIsDefault && spec.length() == 0)
			return defaultValue;
		long value;
		try {
			value = Long.parseLong(spec, radix);
		}
		catch(NumberFormatException nfe) {
			throw new MalformedRequestParameterException(getParameter(), "a long", spec);
		}
		if(value < lowerBound)
			throw new MalformedRequestParameterException(getParameter(), ">= " + lowerBound, spec);
		if(value > upperBound)
			throw new MalformedRequestParameterException(getParameter(), "<= " + upperBound, spec);
		return value;
	}

}
