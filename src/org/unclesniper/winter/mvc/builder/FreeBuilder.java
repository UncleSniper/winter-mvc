package org.unclesniper.winter.mvc.builder;

import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;

public class FreeBuilder<PathKeyT, RequestParameterT> extends AbstractMVCBuilder<PathKeyT, RequestParameterT> {

	public FreeBuilder(Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform) {
		super(handlerTransform);
	}

}
