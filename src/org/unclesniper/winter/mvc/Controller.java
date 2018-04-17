package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public interface Controller<ModelT> {

	ModelT performAction(HTTPRequest request) throws IOException, HTTPServiceException;

	public static <OldModelT extends NewModelT, NewModelT>
	Controller<NewModelT> widenModel(Controller<OldModelT> controller) {
		return request -> controller.performAction(request);
	}

	public static <OldModelT, NewModelT> Controller<NewModelT> mapModel(Controller<OldModelT> controller,
			Transform<? super OldModelT, ? extends NewModelT> transform) {
		return request -> transform.transform(controller.performAction(request));
	}

}
