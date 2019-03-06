package org.unclesniper.winter.mvc.lens;

import java.util.List;

public interface ParameterSource {

	String getParameter(String name);

	List<String> getParameters(String name);

}
