package org.unclesniper.winter.mvc;

import java.util.Set;
import java.io.Serializable;

public interface HTTPSession {

	Serializable getAttribute(SessionKey key);

	Set<SessionKey> getAttributeKeys();

	long getCreationTime();

	long getLastAccessedTime();

	void invalidate();

	boolean isNew();

	void removeAttribute(SessionKey key);

	void setAttribute(SessionKey key, Serializable value);

}
