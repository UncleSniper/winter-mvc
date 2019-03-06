package org.unclesniper.winter.mvc.lens;

public class ExtractCompoundObjectParameter<CompoundT> implements ExtractObjectParameter<CompoundT> {

	private ExtractObjectParameter<? extends CompoundT> constructor;

	private ExtractParameterInto<? super CompoundT> injector;

	public ExtractCompoundObjectParameter(ExtractObjectParameter<? extends CompoundT> constructor,
			ExtractParameterInto<? super CompoundT> injector) {
		this.constructor = constructor;
		this.injector = injector;
	}

	public ExtractObjectParameter<? extends CompoundT> getConstructor() {
		return constructor;
	}

	public void setConstructor(ExtractObjectParameter<? extends CompoundT> constructor) {
		this.constructor = constructor;
	}

	public ExtractParameterInto<? super CompoundT> getInjector() {
		return injector;
	}

	public void setInjector(ExtractParameterInto<? super CompoundT> injector) {
		this.injector = injector;
	}

	@Override
	public CompoundT extractObjectParameter(ParameterSource parameters) {
		CompoundT obj = constructor.extractObjectParameter(parameters);
		injector.extractParameterInto(parameters, obj);
		return obj;
	}

}
