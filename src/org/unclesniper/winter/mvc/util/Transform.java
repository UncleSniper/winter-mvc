package org.unclesniper.winter.mvc.util;

public interface Transform<SourceT, DestinationT> {

	DestinationT transform(SourceT source);

	public static <SourceT extends DestinationT, DestinationT> Transform<SourceT, DestinationT> widen() {
		return value -> value;
	}

}
