package org.unclesniper.winter.mvc;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.io.Reader;
import java.util.Locale;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface HTTPRequest {

	Iterable<HTTPCookie> getCookies();

	long getDateHeader(String name);

	String getHeader(String name);

	Set<String> getHeaderNames();

	List<String> getHeaders(String name);

	int getIntHeader(String name);

	HTTPVerb getMethod();

	RequestPart getPart(String name) throws IOException, HTTPServiceException;

	List<RequestPart> getParts() throws IOException, HTTPServiceException;

	String getPath();

	String getQueryString();

	HTTPSession getSession(boolean create);

	String getCharacterEncoding();

	void setCharacterEncoding(String encoding) throws UnsupportedEncodingException;

	long getContentLength();

	String getContentType();

	InputStream getInputStream() throws IOException;

	Locale getLocale();

	Iterable<Locale> getLocales();

	String getParameter(String name);

	Map<String, List<String>> getParameterMap();

	Set<String> getParameterNames();

	List<String> getParameters(String name);

	Reader getReader();

	String getRemoteAddress();

}
