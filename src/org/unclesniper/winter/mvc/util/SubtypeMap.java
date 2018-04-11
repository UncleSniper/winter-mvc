package org.unclesniper.winter.mvc.util;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class SubtypeMap<RootT, ValueT> {

	private final Map<Class<?>, ValueT> bindings = new HashMap<Class<?>, ValueT>();

	private final Map<Class<?>, ValueT> cache = new ConcurrentHashMap<Class<?>, ValueT>();

	public SubtypeMap() {}

	public ValueT get(Class<?> key) {
		ValueT value = cache.get(key);
		if(value != null)
			return value;
		value = bindings.get(key);
		if(value != null) {
			cache.put(key, value);
			return value;
		}
		Class<?> parent = key == null ? null : key.getSuperclass();
		if(parent == null)
			return null;
		value = get(parent);
		if(value != null)
			cache.put(key, value);
		return value;
	}

	public void put(Class<? extends RootT> key, ValueT value) {
		bindings.put(key, value);
		cache.clear();
	}

	public void clear() {
		bindings.clear();
		cache.clear();
	}

	public void clearCache() {
		cache.clear();
	}

}
