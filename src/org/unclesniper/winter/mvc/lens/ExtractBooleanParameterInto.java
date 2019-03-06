package org.unclesniper.winter.mvc.lens;

public class ExtractBooleanParameterInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	private ExtractBooleanParameter extractor;

	private SetBoolean<? super DestinationT> setter;

	public ExtractBooleanParameterInto(ExtractBooleanParameter extractor,
			SetBoolean<? super DestinationT> setter) {
		this.extractor = extractor;
		this.setter = setter;
	}

	public ExtractBooleanParameter getExtractor() {
		return extractor;
	}

	public void setExtractor(ExtractBooleanParameter extractor) {
		this.extractor = extractor;
	}

	public SetBoolean<? super DestinationT> getSetter() {
		return setter;
	}

	public void setSetter(SetBoolean<? super DestinationT> setter) {
		this.setter = setter;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		setter.setBoolean(destination, extractor.extractBooleanParameter(parameters));
	}

}
