package org.unclesniper.winter.mvc.util;

import java.util.Collection;
import java.lang.reflect.Array;

public class EmptyCollection<E> extends EmptyIterable<E> implements Collection<E> {

	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	public static final EmptyCollection instance = new EmptyCollection();

	public EmptyCollection() {}

	public boolean add(E e) {
		throw new UnsupportedOperationException(EmptySet.class.getName() + ".add(E)");
	}

	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException(EmptySet.class.getName() + ".addAll(Collection)");
	}

	public void clear() {}

	public boolean contains(Object o) {
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		return c.isEmpty();
	}

	public boolean equals(Object o) {
		return o != null && o.getClass().equals(getClass());
	}

	public int hashCode() {
		return 823473253;
	}

	public boolean isEmpty() {
		return true;
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException(EmptySet.class.getName() + ".remove(Object)");
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(EmptySet.class.getName() + ".removeAll(Collection)");
	}

	public boolean retainAll(Collection<?> c) {
		return false;
	}

	public int size() {
		return 0;
	}

	public Object[] toArray() {
		return EmptyCollection.EMPTY_OBJECT_ARRAY;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		return (T[])Array.newInstance(a.getClass().getComponentType(), 0);
	}

}
