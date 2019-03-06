package org.unclesniper.winter.mvc.lens;

public class ExtractLongParameterInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	private ExtractLongParameter extractor;

	private SetLong<? super DestinationT> setter;

	public ExtractLongParameterInto(ExtractLongParameter extractor, SetLong<? super DestinationT> setter) {
		this.extractor = extractor;
		this.setter = setter;
	}

	public ExtractLongParameter getExtractor() {
		return extractor;
	}

	public void setExtractor(ExtractLongParameter extractor) {
		this.extractor = extractor;
	}

	public SetLong<? super DestinationT> getSetter() {
		return setter;
	}

	public void setSetter(SetLong<? super DestinationT> setter) {
		this.setter = setter;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		setter.setLong(destination, extractor.extractLongParameter(parameters));
	}

}
