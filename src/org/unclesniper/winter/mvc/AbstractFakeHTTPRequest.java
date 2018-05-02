package org.unclesniper.winter.mvc;

import java.util.Set;
import java.util.Map;
import java.io.Reader;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.io.UnsupportedEncodingException;
import org.unclesniper.winter.mvc.util.EmptySet;
import org.unclesniper.winter.mvc.util.HTTPDateUtils;

public abstract class AbstractFakeHTTPRequest implements HTTPRequest {

	private HTTPRequest realRequest;

	private List<HTTPCookie> cookies;

	private Map<String, List<String>> headers;

	private HTTPVerb method;

	public AbstractFakeHTTPRequest(HTTPRequest realRequest) {
		this.realRequest = realRequest;
	}

	public HTTPRequest getRealRequest() {
		return realRequest;
	}

	public void setRealRequest(HTTPRequest realRequest) {
		this.realRequest = realRequest;
	}

	public List<HTTPCookie> getCookieList() {
		return cookies;
	}

	public AbstractFakeHTTPRequest setCookieList(List<HTTPCookie> cookies) {
		this.cookies = cookies;
		return this;
	}

	public AbstractFakeHTTPRequest initCookies(boolean drawFromReal) {
		if(cookies != null)
			return this;
		cookies = new LinkedList<HTTPCookie>();
		if(drawFromReal && realRequest != null) {
			Iterable<HTTPCookie> rrc = realRequest.getCookies();
			if(rrc != null) {
				for(HTTPCookie cookie : rrc)
					cookies.add(cookie);
			}
		}
		return this;
	}

	public Iterable<HTTPCookie> getCookies() {
		if(cookies != null)
			return cookies;
		if(realRequest != null)
			return realRequest.getCookies();
		return null;
	}

	public AbstractFakeHTTPRequest withCookie(HTTPCookie cookie) {
		if(cookie == null)
			return this;
		if(cookies == null)
			cookies = new LinkedList<HTTPCookie>();
		cookies.add(cookie);
		return this;
	}

	public long getDateHeader(String name) {
		if(headers != null) {
			if(name == null)
				return -1l;
			List<String> values = headers.get(name.toLowerCase());
			if(values == null || values.isEmpty())
				return -1l;
			String value = values.get(0);
			if(value == null)
				return -1l;
			long date = HTTPDateUtils.parseDateHeader(value);
			if(date == -1l)
				throw new IllegalArgumentException(value);
			return date;
		}
		if(realRequest != null)
			return realRequest.getDateHeader(name);
		return -1l;
	}

	public String getHeader(String name) {
		if(headers != null) {
			if(name == null)
				return null;
			List<String> values = headers.get(name.toLowerCase());
			if(values == null || values.isEmpty())
				return null;
			return values.get(0);
		}
		if(realRequest != null)
			return realRequest.getHeader(name);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getHeaderNames() {
		if(headers != null)
			return headers.keySet();
		if(realRequest != null)
			return realRequest.getHeaderNames();
		return EmptySet.instance;
	}

	public List<String> getHeaders(String name) {
		if(headers != null) {
			if(name == null)
				return null;
			String key = name.toLowerCase();
			List<String> values = headers.get(key);
			if(values == null) {
				values = new LinkedList<String>();
				headers.put(key, values);
			}
			return values;
		}
		if(realRequest != null)
			return realRequest.getHeaders(name);
		return null;
	}

	public int getIntHeader(String name) {
		if(headers != null) {
			if(name == null)
				return -1;
			List<String> values = headers.get(name.toLowerCase());
			if(values == null || values.isEmpty())
				return -1;
			return Integer.parseInt(values.get(0));
		}
		if(realRequest != null)
			return realRequest.getIntHeader(name);
		return -1;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public AbstractFakeHTTPRequest initHeaders(boolean drawFromReal) {
		if(headers != null)
			return this;
		headers = new HashMap<String, List<String>>();
		if(drawFromReal && realRequest != null) {
			for(String key : realRequest.getHeaderNames()) {
				if(key == null)
					continue;
				List<String> values = new LinkedList<String>();
				List<String> oldValues = realRequest.getHeaders(key);
				if(oldValues != null)
					values.addAll(oldValues);
				headers.put(key.toLowerCase(), values);
			}
		}
		return this;
	}

	private List<String> prepareWithHeader(String name) {
		String key = name.toLowerCase();
		List<String> vlist;
		if(headers == null) {
			headers = new HashMap<String, List<String>>();
			vlist = null;
		}
		else
			vlist = headers.get(key);
		if(vlist == null) {
			vlist = new LinkedList<String>();
			headers.put(key, vlist);
		}
		return vlist;
	}

	public AbstractFakeHTTPRequest withHeader(String name, String... values) {
		List<String> vlist = prepareWithHeader(name);
		for(String value : values) {
			if(value != null)
				vlist.add(value);
		}
		return this;
	}

	public AbstractFakeHTTPRequest withHeader(String name, int value) {
		prepareWithHeader(name).add(String.valueOf(value));
		return this;
	}

	public AbstractFakeHTTPRequest withHeader(String name, long value) {
		prepareWithHeader(name).add(String.valueOf(value));
		return this;
	}

	public AbstractFakeHTTPRequest withDateHeader(String name, long... values) {
		List<String> vlist = prepareWithHeader(name);
		for(long value : values) {
			vlist.add(HTTPDateUtils.formatDateHeader(value));
		}
		return this;
	}

	public AbstractFakeHTTPRequest withDateHeader(String name, Date... values) {
		List<String> vlist = prepareWithHeader(name);
		for(Date value : values) {
			if(value != null)
				vlist.add(HTTPDateUtils.formatDateHeader(value));
		}
		return this;
	}

	public HTTPVerb getMethod() {
		if(method != null)
			return method;
		if(realRequest != null)
			return realRequest.getMethod();
		return null;
	}

	public void setMethod(HTTPVerb method) {
		this.method = method;
	}

	public RequestPart getPart(String name) throws IOException, HTTPServiceException {
		//TODO
		return null;
	}

	public List<RequestPart> getParts() throws IOException, HTTPServiceException {
		//TODO
		return null;
	}

	public String getPath() {
		//TODO
		return null;
	}

	public String getQueryString() {
		//TODO
		return null;
	}

	public HTTPSession getSession(boolean create) {
		//TODO
		return null;
	}

	public String getCharacterEncoding() {
		//TODO
		return null;
	}

	public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
		//TODO
	}

	public long getContentLength() {
		//TODO
		return 0l;
	}

	public String getContentType() {
		//TODO
		return null;
	}

	public InputStream getInputStream() throws IOException {
		//TODO
		return null;
	}

	public Locale getLocale() {
		//TODO
		return null;
	}

	public Iterable<Locale> getLocales() {
		//TODO
		return null;
	}

	public String getParameter(String name) {
		//TODO
		return null;
	}

	public Map<String, List<String>> getParameterMap() {
		//TODO
		return null;
	}

	public Set<String> getParameterNames() {
		//TODO
		return null;
	}

	public List<String> getParameters(String name) {
		//TODO
		return null;
	}

	public Reader getReader() {
		//TODO
		return null;
	}

	public String getRemoteAddress() {
		//TODO
		return null;
	}

}
