package org.unclesniper.winter.mvc;

import java.util.concurrent.atomic.AtomicLong;

public final class SessionKey implements Comparable<SessionKey> {

	private static final AtomicLong NEXT_KEY = new AtomicLong();

	private final long nativeKey;

	public SessionKey() {
		nativeKey = SessionKey.NEXT_KEY.getAndIncrement();
	}

	public long getNativeKey() {
		return nativeKey;
	}

	public int hashCode() {
		return (int)nativeKey ^ (int)(nativeKey >> 32);
	}

	public boolean equals(Object obj) {
		return obj instanceof SessionKey ? ((SessionKey)obj).nativeKey == nativeKey : false;
	}

	public int compareTo(SessionKey other) {
		if(nativeKey < other.nativeKey)
			return -1;
		if(nativeKey > other.nativeKey)
			return 1;
		return 0;
	}

}
