package org.unclesniper.winter.mvc.lens;

public class ExtractIntParameterInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	private ExtractIntParameter extractor;

	private SetInt<? super DestinationT> setter;

	public ExtractIntParameterInto(ExtractIntParameter extractor, SetInt<? super DestinationT> setter) {
		this.extractor = extractor;
		this.setter = setter;
	}

	public ExtractIntParameter getExtractor() {
		return extractor;
	}

	public void setExtractor(ExtractIntParameter extractor) {
		this.extractor = extractor;
	}

	public SetInt<? super DestinationT> getSetter() {
		return setter;
	}

	public void setSetter(SetInt<? super DestinationT> setter) {
		this.setter = setter;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		setter.setInt(destination, extractor.extractIntParameter(parameters));
	}

}
