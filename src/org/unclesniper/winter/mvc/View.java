package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.Transform;

public interface View<ModelT> {

	void renderModel(HTTPResponse response, ModelT model) throws IOException, HTTPServiceException;

	public static <OldModelT, NewModelT extends OldModelT> View<NewModelT> narrowModel(View<OldModelT> view) {
		return (response, model) -> view.renderModel(response, model);
	}

	public static <OldModelT, NewModelT> View<NewModelT> mapModel(View<OldModelT> view,
			Transform<? super NewModelT, ? extends OldModelT> transform) {
		return (response, model) -> view.renderModel(response, transform.transform(model));
	}

}
