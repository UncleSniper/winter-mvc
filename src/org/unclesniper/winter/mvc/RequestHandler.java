package org.unclesniper.winter.mvc;

import java.io.IOException;

public interface RequestHandler {

	void handleRequest(HTTPRequest request, HTTPResponse response) throws IOException, HTTPServiceException;

}
