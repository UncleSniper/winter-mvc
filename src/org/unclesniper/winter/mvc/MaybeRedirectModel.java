package org.unclesniper.winter.mvc;

import org.unclesniper.winter.mvc.util.Transform;

public class MaybeRedirectModel<AddressT, InnerModelT> {

	private AddressT address;

	private InnerModelT innerModel;

	public MaybeRedirectModel(AddressT address, InnerModelT innerModel) {
		this.address = address;
		this.innerModel = innerModel;
	}

	public AddressT getAddress() {
		return address;
	}

	public void setAddress(AddressT address) {
		this.address = address;
	}

	public InnerModelT getInnerModel() {
		return innerModel;
	}

	public void setInnerModel(InnerModelT innerModel) {
		this.innerModel = innerModel;
	}

	public <NewAddressT> MaybeRedirectModel<NewAddressT, InnerModelT>
	mapAddress(Transform<? super AddressT, ? extends NewAddressT> transform) {
		return new MaybeRedirectModel<NewAddressT, InnerModelT>(address == null
				? null : transform.transform(address), innerModel);
	}

	public <NewModelT> MaybeRedirectModel<AddressT, NewModelT>
	mapModel(Transform<? super InnerModelT, ? extends NewModelT> transform) {
		return new MaybeRedirectModel<AddressT, NewModelT>(address,
				innerModel == null ? null : transform.transform(innerModel));
	}

	public static <AddressT, InnerModelT> MaybeRedirectModel<AddressT, InnerModelT> redirect(AddressT address) {
		return new MaybeRedirectModel<AddressT, InnerModelT>(address, null);
	}

	public static <AddressT, InnerModelT> MaybeRedirectModel<AddressT, InnerModelT> noRedirect(InnerModelT model) {
		return new MaybeRedirectModel<AddressT, InnerModelT>(null, model);
	}

}
