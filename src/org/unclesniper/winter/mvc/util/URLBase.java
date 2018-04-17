package org.unclesniper.winter.mvc.util;

public interface URLBase {

	String getScheme();

	String getUser();

	String getHost();

	int getPort();

	String getPath();

	String getQuery();

	String getFragment();

	URLBuilder resolve(URLBuilder relative);

	URLBuilder copy();

}
