package org.unclesniper.winter.mvc.lens;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import org.unclesniper.winter.mvc.error.MissingRequestParameterException;
import org.unclesniper.winter.mvc.error.MalformedRequestParameterException;

public class SimpleExtractBooleanParameter extends AbstractExtractParameter implements ExtractBooleanParameter {

	private static final Set<String> DEFAULT_TRUE_NAMES;

	private static final Set<String> DEFAULT_FALSE_NAMES;

	static {
		DEFAULT_TRUE_NAMES = new HashSet<String>();
		DEFAULT_TRUE_NAMES.add("true");
		DEFAULT_TRUE_NAMES.add("yes");
		DEFAULT_TRUE_NAMES.add("on");
		DEFAULT_TRUE_NAMES.add("1");
		DEFAULT_FALSE_NAMES = new HashSet<String>();
		DEFAULT_FALSE_NAMES.add("false");
		DEFAULT_FALSE_NAMES.add("no");
		DEFAULT_FALSE_NAMES.add("off");
		DEFAULT_FALSE_NAMES.add("0");
	}

	private boolean defaultValue;

	private boolean emptyIsDefault;

	private Set<String> trueNames;

	private Set<String> falseNames;

	public SimpleExtractBooleanParameter(String parameter) {
		super(parameter);
	}

	public SimpleExtractBooleanParameter(String parameter, boolean required, boolean defaultValue) {
		this(parameter, required, defaultValue, false);
	}

	public SimpleExtractBooleanParameter(String parameter, boolean required, boolean defaultValue,
			boolean emptyIsDefault) {
		super(parameter, required);
		this.defaultValue = defaultValue;
		this.emptyIsDefault = emptyIsDefault;
	}

	public boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isEmptyIsDefault() {
		return emptyIsDefault;
	}

	public void setEmptyIsDefault(boolean emptyIsDefault) {
		this.emptyIsDefault = emptyIsDefault;
	}

	public Set<String> getTrueNames() {
		return Collections.unmodifiableSet(trueNames == null
				? SimpleExtractBooleanParameter.DEFAULT_TRUE_NAMES : trueNames);
	}

	public void addTrueName(String name) {
		if(trueNames == null)
			trueNames = new HashSet<String>(SimpleExtractBooleanParameter.DEFAULT_TRUE_NAMES);
		trueNames.add(name.trim().toLowerCase());
	}

	public boolean removeTrueName(String name) {
		if(name == null)
			return false;
		if(trueNames == null)
			trueNames = new HashSet<String>(SimpleExtractBooleanParameter.DEFAULT_TRUE_NAMES);
		return trueNames.remove(name.trim().toLowerCase());
	}

	public Set<String> getFalseNames() {
		return Collections.unmodifiableSet(falseNames == null
				? SimpleExtractBooleanParameter.DEFAULT_FALSE_NAMES : falseNames);
	}

	public void addFalseName(String name) {
		if(falseNames == null)
			falseNames = new HashSet<String>(SimpleExtractBooleanParameter.DEFAULT_FALSE_NAMES);
		falseNames.add(name.trim().toLowerCase());
	}

	public boolean removeFalseName(String name) {
		if(name == null)
			return false;
		if(falseNames == null)
			falseNames = new HashSet<String>(SimpleExtractBooleanParameter.DEFAULT_FALSE_NAMES);
		return falseNames.remove(name.trim().toLowerCase());
	}

	@Override
	public boolean extractBooleanParameter(ParameterSource parameters) {
		String spec = parameters.getParameter(getParameter());
		if(spec == null) {
			if(isRequired())
				throw new MissingRequestParameterException(getParameter());
			return defaultValue;
		}
		spec = spec.trim();
		if(emptyIsDefault && spec.length() == 0)
			return defaultValue;
		String lcspec = spec.toLowerCase();
		Set<String> names = trueNames == null ? SimpleExtractBooleanParameter.DEFAULT_TRUE_NAMES : trueNames;
		if(names.contains(lcspec))
			return true;
		names = falseNames == null ? SimpleExtractBooleanParameter.DEFAULT_FALSE_NAMES : falseNames;
		if(names.contains(lcspec))
			return false;
		throw new MalformedRequestParameterException(getParameter(), "a boolean", spec);
	}

}
