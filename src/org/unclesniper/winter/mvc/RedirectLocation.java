package org.unclesniper.winter.mvc;

import org.unclesniper.winter.mvc.util.Transform;

public final class RedirectLocation<AddressT> {

	private AddressT address;

	public RedirectLocation(AddressT address) {
		this.address = address;
	}

	public AddressT getAddress() {
		return address;
	}

	public void setAddress(AddressT address) {
		this.address = address;
	}

	public <NewAddressT> RedirectLocation<NewAddressT>
	map(Transform<? super AddressT, ? extends NewAddressT> transform) {
		return new RedirectLocation<NewAddressT>(address == null ? null : transform.transform(address));
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if(!(other instanceof RedirectLocation))
			return false;
		Object oa = ((RedirectLocation)other).address;
		if(address == null)
			return oa == null;
		if(oa == null)
			return false;
		return address.equals(oa);
	}

	public int hashCode() {
		return address == null ? 0 : address.hashCode();
	}

	public String toString() {
		return address == null ? null : address.toString();
	}

}
