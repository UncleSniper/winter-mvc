package org.unclesniper.winter.mvc;

import java.io.IOException;

public class RedirectView<AddressT> implements View<RedirectLocation<AddressT>> {

	public RedirectView() {}

	public void renderModel(HTTPResponse response, RedirectLocation<AddressT> location) throws IOException {
		String address = location == null ? null : location.toString();
		if(address == null)
			address = "?";
		response.sendRedirect(response.encodeRedirectURL(address));
	}

}
