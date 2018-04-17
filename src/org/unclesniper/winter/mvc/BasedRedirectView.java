package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.URLBuilder;
import org.unclesniper.winter.mvc.util.ValueHolder;

public class BasedRedirectView implements View<RedirectLocation<URLBuilder>> {

	private ValueHolder<? extends URLBuilder> baseURL;

	public BasedRedirectView(ValueHolder<? extends URLBuilder> baseURL) {
		this.baseURL = baseURL;
	}

	public ValueHolder<? extends URLBuilder> getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(ValueHolder<? extends URLBuilder> baseURL) {
		this.baseURL = baseURL;
	}

	public void renderModel(HTTPResponse response, RedirectLocation<URLBuilder> location) throws IOException {
		URLBuilder base = baseURL == null ? null : baseURL.getCurrentValue();
		URLBuilder reference = location == null ? null : location.getAddress();
		String address;
		if(base == null) {
			if(reference == null)
				address = null;
			else
				address = reference.toString();
		}
		else {
			if(reference == null)
				address = base.toString();
			else
				address = base.resolve(reference).toString();
		}
		if(address == null)
			address = "?";
		response.sendRedirect(response.encodeRedirectURL(address));
	}

}
