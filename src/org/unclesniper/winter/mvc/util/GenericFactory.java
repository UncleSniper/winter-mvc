package org.unclesniper.winter.mvc.util;

public interface GenericFactory<T> {

	T newInstance();

	public static <T> GenericFactory<T> constant(T instance) {
		return () -> instance;
	}

}
