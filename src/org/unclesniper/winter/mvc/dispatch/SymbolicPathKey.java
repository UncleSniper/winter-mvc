package org.unclesniper.winter.mvc.dispatch;

import java.util.concurrent.atomic.AtomicLong;

public final class SymbolicPathKey implements Comparable<SymbolicPathKey> {

	private static final AtomicLong NEXT_KEY = new AtomicLong();

	private final long nativeKey;

	public SymbolicPathKey() {
		nativeKey = SymbolicPathKey.NEXT_KEY.getAndIncrement();
	}

	public long getNativeKey() {
		return nativeKey;
	}

	public int hashCode() {
		return (int)nativeKey ^ (int)(nativeKey >> 32);
	}

	public boolean equals(Object obj) {
		return obj instanceof SymbolicPathKey ? ((SymbolicPathKey)obj).nativeKey == nativeKey : false;
	}

	public int compareTo(SymbolicPathKey other) {
		if(nativeKey < other.nativeKey)
			return -1;
		if(nativeKey > other.nativeKey)
			return 1;
		return 0;
	}

}
