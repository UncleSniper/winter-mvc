package org.unclesniper.winter.mvc.lens;

public class ExtractObjectParameterInto<DestinationT, PropertyT> implements ExtractParameterInto<DestinationT> {

	private ExtractObjectParameter<? extends PropertyT> extractor;

	private SetObject<? super DestinationT, ? super PropertyT> setter;

	public ExtractObjectParameterInto(ExtractObjectParameter<? extends PropertyT> extractor,
			SetObject<? super DestinationT, ? super PropertyT> setter) {
		this.extractor = extractor;
		this.setter = setter;
	}

	public ExtractObjectParameter<? extends PropertyT> getExtractor() {
		return extractor;
	}

	public void setExtractor(ExtractObjectParameter<? extends PropertyT> extractor) {
		this.extractor = extractor;
	}

	public SetObject<? super DestinationT, ? super PropertyT> getSetter() {
		return setter;
	}

	public void setSetter(SetObject<? super DestinationT, ? super PropertyT> setter) {
		this.setter = setter;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		setter.setObject(destination, extractor.extractObjectParameter(parameters));
	}

}
