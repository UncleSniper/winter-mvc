package org.unclesniper.winter.mvc;

import java.io.File;
import java.util.Set;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;

public interface RequestPart {

	void delete() throws IOException;

	String getContentType();

	String getHeader(String name);

	Set<String> getHeaderNames();

	List<String> getHeaders(String name);

	InputStream getInputStream() throws IOException;

	String getName();

	long getSize();

	String getSubmittedFileName();

	void write(File fileName) throws IOException;

}
