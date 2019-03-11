package org.unclesniper.winter.mvc.lens;

public interface GetObject<BaseT, PropertyT> {

	PropertyT getObject(BaseT base);

}
