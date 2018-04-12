package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface View<ModelT> {

	void renderModel(HTTPResponse response, ModelT model) throws IOException, HTTPServiceException;

}
