package org.unclesniper.winter.mvc.util;

import java.util.Iterator;

public class EmptyIterable<E> implements Iterable<E> {

	public EmptyIterable() {}

	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return EmptyIterator.instance;
	}

}
