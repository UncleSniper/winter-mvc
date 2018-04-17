package org.unclesniper.winter.mvc;

import java.net.URL;
import java.net.URI;
import java.io.IOException;
import org.unclesniper.winter.mvc.util.URLBuilder;

public class MaybeStaticRedirectView implements View<DoRedirect> {

	private String destination;

	public MaybeStaticRedirectView(String destination) {
		this.destination = destination;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setDestination(URLBuilder destination) {
		this.destination = destination == null ? null : destination.toString();
	}

	public void setDestination(URL destination) {
		this.destination = destination == null ? null : destination.toString();
	}

	public void setDestination(URI destination) {
		this.destination = destination == null ? null : destination.toString();
	}

	public void renderModel(HTTPResponse response, DoRedirect trigger) throws IOException {
		if(trigger == DoRedirect.YES)
			response.sendRedirect(response.encodeRedirectURL(destination == null ? "?" : destination));
	}

}
