package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.URLBuilder;
import org.unclesniper.winter.mvc.util.ValueHolder;

public class MaybeBasedRedirectView<InnerModelT, ParameterT>
		implements ParameterizedView<MaybeRedirectModel<URLBuilder, InnerModelT>, ParameterT> {

	private ValueHolder<? extends URLBuilder> baseURL;

	private ParameterizedView<? super InnerModelT, ? super ParameterT> innerView;

	public MaybeBasedRedirectView(ValueHolder<? extends URLBuilder> baseURL,
			ParameterizedView<? super InnerModelT, ? super ParameterT> innerView) {
		this.baseURL = baseURL;
		this.innerView = innerView;
	}

	public ValueHolder<? extends URLBuilder> getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(ValueHolder<? extends URLBuilder> baseURL) {
		this.baseURL = baseURL;
	}

	public ParameterizedView<? super InnerModelT, ? super ParameterT> getInnerView() {
		return innerView;
	}

	public void setInnerView(ParameterizedView<? super InnerModelT, ? super ParameterT> innerView) {
		this.innerView = innerView;
	}

	public void renderModel(HTTPResponse response, MaybeRedirectModel<URLBuilder, InnerModelT> location,
			ParameterT parameter) throws IOException, HTTPServiceException {
		if(location == null)
			return;
		URLBuilder reference = location.getAddress();
		if(reference != null) {
			URLBuilder base = baseURL == null ? null : baseURL.getCurrentValue();
			String address;
			if(base == null)
				address = reference.toString();
			else
				address = base.resolve(reference).toString();
			response.sendRedirect(response.encodeRedirectURL(address));
		}
		else {
			InnerModelT model = location.getInnerModel();
			if(innerView != null && model != null)
				innerView.renderModel(response, model, parameter);
		}
	}

}
