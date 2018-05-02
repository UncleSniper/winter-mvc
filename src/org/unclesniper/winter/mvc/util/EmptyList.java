package org.unclesniper.winter.mvc.util;

import java.util.List;
import java.util.Collection;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

public class EmptyList<E> extends EmptyCollection<E> implements List<E> {

	public static final EmptyList instance = new EmptyList();

	public EmptyList() {}

	public void add(int index, E element) {
		throw new UnsupportedOperationException(EmptyList.class.getName() + ".add(int, E)");
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException(EmptyList.class.getName() + ".addAll(int, Collection)");
	}

	public E get(int index) {
		throw new IndexOutOfBoundsException(String.valueOf(index));
	}

	public int indexOf(Object o) {
		return -1;
	}

	public int lastIndexOf(Object o) {
		return -1;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<E> listIterator() {
		return EmptyIterator.instance;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<E> listIterator(int index) {
		if(index != 0)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		return EmptyIterator.instance;
	}

	public E remove(int index) {
		throw new UnsupportedOperationException(EmptyList.class.getName() + ".remove(int)");
	}

	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException(EmptyList.class.getName() + ".replaceAll(UnaryOperator)");
	}

	public E set(int index, E element) {
		throw new UnsupportedOperationException(EmptyList.class.getName() + ".set(int, E)");
	}

	public List<E> subList(int fromIndex, int toIndex) {
		if(fromIndex != 0 || toIndex != 0)
			throw new IndexOutOfBoundsException(String.valueOf(fromIndex != 0 ? fromIndex : toIndex));
		return this;
	}

}
