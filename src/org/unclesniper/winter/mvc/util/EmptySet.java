package org.unclesniper.winter.mvc.util;

import java.util.Set;

public class EmptySet<E> extends EmptyCollection<E> implements Set<E> {

	public static final EmptySet instance = new EmptySet();

	public EmptySet() {}

}
