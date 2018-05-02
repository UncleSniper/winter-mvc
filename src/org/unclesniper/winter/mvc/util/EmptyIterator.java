package org.unclesniper.winter.mvc.util;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.NoSuchElementException;

public class EmptyIterator<E> implements ListIterator<E> {

	public static final EmptyIterator instance = new EmptyIterator();

	public EmptyIterator() {}

	public boolean hasNext() {
		return false;
	}

	public E next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new IllegalStateException();
	}

	public void forEachRemaining(Consumer<? super E> action) {}

	public void set(E e) {
		throw new IllegalStateException();
	}

	public void add(E e) {
		throw new UnsupportedOperationException(EmptyIterator.class.getName() + ".add(E)");
	}

	public int nextIndex() {
		return 0;
	}

	public E previous() {
		throw new NoSuchElementException();
	}

	public int previousIndex() {
		return -1;
	}

	public boolean hasPrevious() {
		return false;
	}

}
