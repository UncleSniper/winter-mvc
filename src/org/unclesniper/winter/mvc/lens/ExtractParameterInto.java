package org.unclesniper.winter.mvc.lens;

public interface ExtractParameterInto<DestinationT> {

	void extractParameterInto(ParameterSource parameters, DestinationT destination);

}
