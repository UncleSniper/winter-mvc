package org.unclesniper.winter.mvc.lens;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

public class ExtractManyParametersInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	private final List<ExtractParameterInto<? super DestinationT>> extractors
			= new LinkedList<ExtractParameterInto<? super DestinationT>>();

	public ExtractManyParametersInto(Iterable<? extends ExtractParameterInto<? super DestinationT>> extractors) {
		if(extractors != null) {
			for(ExtractParameterInto<? super DestinationT> extractor : extractors)
				this.extractors.add(extractor);
		}
	}

	@SafeVarargs
	public ExtractManyParametersInto(ExtractParameterInto<? super DestinationT>... extractors) {
		if(extractors != null) {
			for(ExtractParameterInto<? super DestinationT> extractor : extractors)
				this.extractors.add(extractor);
		}
	}

	public Collection<ExtractParameterInto<? super DestinationT>> getExtractors() {
		return extractors;
	}

	public void addExtractor(ExtractParameterInto<? super DestinationT> extractor) {
		extractors.add(extractor);
	}

	public void clearExtractors() {
		extractors.clear();
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		for(ExtractParameterInto<? super DestinationT> extractor : extractors)
			extractor.extractParameterInto(parameters, destination);
	}

}
