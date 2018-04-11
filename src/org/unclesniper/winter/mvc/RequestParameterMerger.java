package org.unclesniper.winter.mvc;

public interface RequestParameterMerger<ParameterT, ComponentT> {

	void mergeRequestParameter(ParameterT parameter, ComponentT component);

}
