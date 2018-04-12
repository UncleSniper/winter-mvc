package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface Controller<ModelT> {

	ModelT performAction(HTTPRequest request) throws IOException, HTTPServiceException;

}
