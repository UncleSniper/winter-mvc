package org.unclesniper.winter.mvc.lens;

import org.unclesniper.winter.mvc.error.MissingRequestParameterException;
import org.unclesniper.winter.mvc.error.MalformedRequestParameterException;

public class SimpleExtractDoubleParameter extends AbstractExtractParameter implements ExtractDoubleParameter {

	private double lowerBound;

	private double upperBound;

	private double defaultValue;

	private boolean emptyIsDefault;

	public SimpleExtractDoubleParameter(String parameter) {
		this(parameter, true);
	}

	public SimpleExtractDoubleParameter(String parameter, boolean required) {
		super(parameter, required);
		lowerBound = Double.MIN_VALUE;
		upperBound = Double.MAX_VALUE;
	}

	public SimpleExtractDoubleParameter(String parameter, double lowerBound, double upperBound) {
		this(parameter, true, lowerBound, upperBound);
	}

	public SimpleExtractDoubleParameter(String parameter, boolean required,
			double lowerBound, double upperBound) {
		super(parameter, required);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public SimpleExtractDoubleParameter(String parameter, double lowerBound, double upperBound,
			double defaultValue, boolean emptyIsDefault) {
		this(parameter, true, lowerBound, upperBound, defaultValue, emptyIsDefault);
	}

	public SimpleExtractDoubleParameter(String parameter, boolean required,
			double lowerBound, double upperBound, double defaultValue, boolean emptyIsDefault) {
		super(parameter, required);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.defaultValue = defaultValue;
		this.emptyIsDefault = emptyIsDefault;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public void removeLowerBound() {
		lowerBound = Double.MIN_VALUE;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public void removeUpperBound() {
		upperBound = Double.MAX_VALUE;
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isEmptyIsDefault() {
		return emptyIsDefault;
	}

	public void setEmptyIsDefault(boolean emptyIsDefault) {
		this.emptyIsDefault = emptyIsDefault;
	}

	@Override
	public double extractDoubleParameter(ParameterSource parameters) {
		String spec = parameters.getParameter(getParameter());
		if(spec == null) {
			if(isRequired())
				throw new MissingRequestParameterException(getParameter());
			return defaultValue;
		}
		spec = spec.trim();
		if(emptyIsDefault && spec.length() == 0)
			return defaultValue;
		double value;
		try {
			value = Double.parseDouble(spec);
		}
		catch(NumberFormatException nfe) {
			throw new MalformedRequestParameterException(getParameter(), "a double", spec);
		}
		if(value < lowerBound)
			throw new MalformedRequestParameterException(getParameter(), ">= " + lowerBound, spec);
		if(value > upperBound)
			throw new MalformedRequestParameterException(getParameter(), "<= " + upperBound, spec);
		return value;
	}

}
