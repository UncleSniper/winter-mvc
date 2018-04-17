package org.unclesniper.winter.mvc.util;

public class ConstantValueHolder<ValueT> implements ValueHolder<ValueT> {

	private final ValueT value;

	public ConstantValueHolder(ValueT value) {
		this.value = value;
	}

	public ValueT getCurrentValue() {
		return value;
	}

}
