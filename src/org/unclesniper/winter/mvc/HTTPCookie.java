package org.unclesniper.winter.mvc;

import java.io.Serializable;

public interface HTTPCookie extends Cloneable, Serializable {

	HTTPCookie clone();

	String getComment();

	void setComment(String purpose);

	String getDomain();

	void setDomain(String pattern);

	int getMaxAge();

	void setMaxAge(int expiry);

	String getName();

	String getPath();

	void setPath(String uri);

	boolean isSecure();

	void setSecure(boolean flag);

	String getValue();

	void setValue(String newValue);

	int getVersion();

	void setVersion(int v);

	boolean isHttpOnly();

	void setHttpOnly(boolean httpOnly);

}
