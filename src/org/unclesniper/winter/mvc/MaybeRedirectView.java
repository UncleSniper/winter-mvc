package org.unclesniper.winter.mvc;

import java.io.IOException;

public class MaybeRedirectView<AddressT, InnerModelT, ParameterT>
		implements ParameterizedView<MaybeRedirectModel<AddressT, InnerModelT>, ParameterT> {

	private ParameterizedView<? super InnerModelT, ? super ParameterT> innerView;

	public MaybeRedirectView(ParameterizedView<? super InnerModelT, ? super ParameterT> innerView) {
		this.innerView = innerView;
	}

	public ParameterizedView<? super InnerModelT, ? super ParameterT> getInnerView() {
		return innerView;
	}

	public void setInnerView(ParameterizedView<? super InnerModelT, ? super ParameterT> innerView) {
		this.innerView = innerView;
	}

	public void renderModel(HTTPResponse response, MaybeRedirectModel<AddressT, InnerModelT> location,
			ParameterT parameter) throws IOException, HTTPServiceException {
		if(location == null)
			return;
		AddressT address = location.getAddress();
		if(address != null) {
			String sa = address.toString();
			if(sa == null)
				sa = "?";
			response.sendRedirect(response.encodeRedirectURL(sa));
		}
		else {
			InnerModelT model = location.getInnerModel();
			if(innerView != null && model != null)
				innerView.renderModel(response, model, parameter);
		}
	}

}
