package org.unclesniper.winter.mvc.builder;

import java.net.URL;
import java.net.URI;
import org.unclesniper.winter.mvc.View;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.util.URLBuilder;
import org.unclesniper.winter.mvc.ParameterizedView;

public interface MVCBuilderWithController<PathKeyT, RequestParameterT,
		FreeReturnT extends MVCBuilder<PathKeyT, RequestParameterT, FreeReturnT, RefreeReturnT>,
		RefreeReturnT extends MVCBuilder<PathKeyT, RequestParameterT, RefreeReturnT, RefreeReturnT>,
		ControllerParameterT, ModelT>
		extends MVCBuilder<PathKeyT, RequestParameterT, FreeReturnT, RefreeReturnT> {

	FreeReturnT render(View<? super ModelT> view);

	RefreeReturnT renderAnd(View<? super ModelT> view);

	FreeReturnT render(ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	RefreeReturnT renderAnd(ParameterizedView<? super ModelT, ? super RequestParameterT> view);

	<ViewParameterT> FreeReturnT render(ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	<ViewParameterT> RefreeReturnT renderAnd(ParameterizedView<? super ModelT, ViewParameterT> view,
			Transform<? super RequestParameterT, ? extends ViewParameterT> viewParameterTransform);

	FreeReturnT redirectTo(String destination);

	RefreeReturnT redirectToAnd(String destination);

	FreeReturnT redirectTo(URLBuilder destination);

	RefreeReturnT redirectToAnd(URLBuilder destination);

	FreeReturnT redirectTo(URL destination);

	RefreeReturnT redirectToAnd(URL destination);

	FreeReturnT redirectTo(URI destination);

	RefreeReturnT redirectToAnd(URI destination);

}
