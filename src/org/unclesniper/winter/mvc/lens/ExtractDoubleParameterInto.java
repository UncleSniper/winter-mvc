package org.unclesniper.winter.mvc.lens;

public class ExtractDoubleParameterInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	private ExtractDoubleParameter extractor;

	private SetDouble<? super DestinationT> setter;

	public ExtractDoubleParameterInto(ExtractDoubleParameter extractor, SetDouble<? super DestinationT> setter) {
		this.extractor = extractor;
		this.setter = setter;
	}

	public ExtractDoubleParameter getExtractor() {
		return extractor;
	}

	public void setExtractor(ExtractDoubleParameter extractor) {
		this.extractor = extractor;
	}

	public SetDouble<? super DestinationT> getSetter() {
		return setter;
	}

	public void setSetter(SetDouble<? super DestinationT> setter) {
		this.setter = setter;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		setter.setDouble(destination, extractor.extractDoubleParameter(parameters));
	}

}
