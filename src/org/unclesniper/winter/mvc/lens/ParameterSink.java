package org.unclesniper.winter.mvc.lens;

public interface ParameterSink {

	void setParameter(String name, String value);

	void addParameter(String name, String value);

}
