package org.unclesniper.winter.mvc.util;

public interface ValueHolder<ValueT> {

	ValueT getCurrentValue();

	public static <ValueT> ValueHolder<ValueT> always(ValueT value) {
		return new ConstantValueHolder<ValueT>(value);
	}

	public static <OldValueT extends NewValueT, NewValueT>
	ValueHolder<NewValueT> widen(ValueHolder<OldValueT> holder) {
		return () -> holder.getCurrentValue();
	}

	public static <OldValueT, NewValueT> ValueHolder<NewValueT>
	map(ValueHolder<OldValueT> holder, Transform<? super OldValueT, ? extends NewValueT> transform) {
		return () -> transform.transform(holder.getCurrentValue());
	}

	public static <OldValueT, NewValueT> ValueHolder<NewValueT>
	mapNull(ValueHolder<OldValueT> holder, Transform<? super OldValueT, ? extends NewValueT> transform) {
		return () -> {
			OldValueT value = holder.getCurrentValue();
			return value == null ? null : transform.transform(value);
		};
	}

}
