package org.unclesniper.winter.mvc.util;

import java.net.URLEncoder;
import java.net.URLDecoder;
import org.unclesniper.winter.mvc.Doom;
import java.io.UnsupportedEncodingException;

public class URLBuilder implements URLParser.URLPartSink {

	private String scheme;

	private String user;

	private String host;

	private int port;

	private String path;

	private StringBuilder pathBuilder;

	private String query;

	private StringBuilder queryBuilder;

	private String fragment;

	public URLBuilder() {
		scheme = "http";
	}

	public URLBuilder(String scheme) {
		this.scheme = scheme;
	}

	public URLBuilder(String scheme, String path) {
		this.scheme = scheme;
		this.path = path;
	}

	public URLBuilder(String scheme, String host, String path) {
		this.scheme = scheme;
		this.host = host;
		this.path = path;
	}

	public URLBuilder(String scheme, String host, int port, String path) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	public URLBuilder(String scheme, String host, String path, String query) {
		this.scheme = scheme;
		this.host = host;
		this.path = path;
		this.query = query;
	}

	public URLBuilder(String scheme, String host, int port, String path, String query) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
	}

	public URLBuilder(String scheme, String host, String path, String query, String fragment) {
		this.scheme = scheme;
		this.host = host;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
	}

	public URLBuilder(String scheme, String host, int port, String path, String query, String fragment) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
	}

	public URLBuilder(String scheme, String user, String host, String path, String query, String fragment) {
		this.scheme = scheme;
		this.user = user;
		this.host = host;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
	}

	public URLBuilder(String scheme, String user, String host, int port,
			String path, String query, String fragment) {
		this.scheme = scheme;
		this.user = user;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
	}

	public String getScheme() {
		return scheme;
	}

	public String getUser() {
		return user;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getPath() {
		if(pathBuilder != null) {
			path = pathBuilder.toString();
			pathBuilder = null;
		}
		return path;
	}

	public String getQuery() {
		if(queryBuilder != null) {
			query = queryBuilder.toString();
			queryBuilder = null;
		}
		return query;
	}

	public String getFragment() {
		return fragment;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPath(String path) {
		this.path = path;
		pathBuilder = null;
	}

	public void setQuery(String query) {
		this.query = query;
		queryBuilder = null;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public URLBuilder scheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	public URLBuilder http() {
		scheme = "http";
		return this;
	}

	public URLBuilder defhttp() {
		scheme = "http";
		port = 0;
		return this;
	}

	public URLBuilder https() {
		scheme = "https";
		return this;
	}

	public URLBuilder defhttps() {
		scheme = "https";
		port = 0;
		return this;
	}

	public URLBuilder file() {
		scheme = "file";
		return this;
	}

	public URLBuilder user(String user) {
		this.user = user;
		return this;
	}

	public URLBuilder port(int port) {
		this.port = port;
		return this;
	}

	public URLBuilder defport() {
		port = 0;
		return this;
	}

	public URLBuilder path(String path) {
		this.path = path;
		pathBuilder = null;
		return this;
	}

	private void addPathImpl(String part) {
		if(path != null) {
			pathBuilder = new StringBuilder(path);
			pathBuilder.append(part);
			path = null;
		}
		else if(pathBuilder != null)
			pathBuilder.append(part);
		else
			path = part;
	}

	public URLBuilder addPath(String part) {
		if(part != null)
			addPathImpl(part);
		return this;
	}

	public URLBuilder addPathEsc(String part) {
		if(part != null)
			addPathImpl(URLBuilder.encode(part));
		return this;
	}

	private void addSegImpl(String segment) {
		int slen = segment.length(), plen;
		if(slen == 0)
			return;
		boolean head = segment.charAt(0) == '/', tail;
		if(path != null) {
			plen = path.length();
			tail = plen > 0 && path.charAt(plen - 1) == '/';
			pathBuilder = new StringBuilder(path);
			path = null;
		}
		else if(pathBuilder != null) {
			plen = pathBuilder.length();
			tail = plen > 0 && pathBuilder.charAt(plen - 1) == '/';
		}
		else {
			path = segment;
			return;
		}
		if(head) {
			if(tail) {
				if(slen > 1)
					pathBuilder.append(segment.substring(1));
			}
			else
				pathBuilder.append(segment);
		}
		else {
			if(!tail)
				pathBuilder.append('/');
			pathBuilder.append(segment);
		}
	}

	public URLBuilder addSeg(String segment) {
		if(segment != null)
			addSegImpl(segment);
		return this;
	}

	public URLBuilder addSegEsc(String segment) {
		if(segment != null)
			addSegImpl(URLBuilder.encode(segment));
		return this;
	}

	public URLBuilder slash() {
		int plen;
		if(path != null) {
			plen = path.length();
			if(plen > 0 && path.charAt(plen - 1) == '/')
				return this;
			pathBuilder = new StringBuilder(path);
			pathBuilder.append('/');
			path = null;
		}
		else if(pathBuilder != null) {
			plen = pathBuilder.length();
			if(plen > 0 && pathBuilder.charAt(plen - 1) == '/')
				return this;
			pathBuilder.append('/');
		}
		else
			path = "/";
		return this;
	}

	public URLBuilder query(String query) {
		this.query = query;
		queryBuilder = null;
		return this;
	}

	private void addQueryImpl(String part) {
		if(query != null) {
			queryBuilder = new StringBuilder(query);
			queryBuilder.append(part);
			query = null;
		}
		else if(queryBuilder != null)
			queryBuilder.append(part);
		else
			query = part;
	}

	public URLBuilder addQuery(String part) {
		if(part != null)
			addQueryImpl(part);
		return this;
	}

	public URLBuilder addQueryEsc(String part) {
		if(part != null)
			addQueryImpl(URLBuilder.encode(part));
		return this;
	}

	private void paramImpl(String param, String value) {
		int plen = param.length(), qlen;
		if(plen == 0)
			return;
		boolean head = param.charAt(0) == '&', tail;
		if(query != null) {
			qlen = query.length();
			tail = qlen > 0 && query.charAt(qlen - 1) == '&';
			queryBuilder = new StringBuilder(query);
			query = null;
		}
		else if(queryBuilder != null) {
			qlen = queryBuilder.length();
			tail = qlen > 0 && queryBuilder.charAt(qlen - 1) == '&';
		}
		else if(value != null) {
			queryBuilder = new StringBuilder();
			tail = false;
		}
		else {
			query = param;
			return;
		}
		if(head) {
			if(tail) {
				if(plen > 1)
					queryBuilder.append(param.substring(1));
			}
			else
				queryBuilder.append(param);
		}
		else {
			if(!tail)
				queryBuilder.append('&');
			queryBuilder.append(param);
		}
		if(value != null) {
			queryBuilder.append('=');
			queryBuilder.append(value);
		}
	}

	public URLBuilder param(String param) {
		if(param != null)
			paramImpl(param, null);
		return this;
	}

	public URLBuilder paramEsc(String param) {
		if(param != null)
			paramImpl(URLBuilder.encode(param), null);
		return this;
	}

	public URLBuilder param(String key, String value) {
		if(key != null)
			paramImpl(URLBuilder.encode(key), value == null ? null : URLBuilder.encode(value));
		return this;
	}

	public URLBuilder fragment(String fragment) {
		this.fragment = fragment;
		return this;
	}

	public String toString() {
		if(pathBuilder != null) {
			path = pathBuilder.toString();
			pathBuilder = null;
		}
		if(queryBuilder != null) {
			query = queryBuilder.toString();
			queryBuilder = null;
		}
		StringBuilder builder = new StringBuilder();
		if(scheme != null) {
			builder.append(scheme);
			builder.append(':');
		}
		if(host != null) {
			builder.append("//");
			if(user != null) {
				builder.append(user);
				builder.append('@');
			}
			builder.append(host);
			if(port > 0) {
				builder.append(':');
				builder.append(String.valueOf(port));
			}
		}
		if(path != null) {
			if(host != null && path.length() > 0 && path.charAt(0) != '/')
				builder.append('/');
			builder.append(path);
		}
		else {
			if(host != null)
				builder.append('/');
		}
		if(query != null) {
			builder.append('?');
			builder.append(query);
		}
		if(fragment != null) {
			builder.append('#');
			builder.append(fragment);
		}
		return builder.toString();
	}

	public URLBuilder resolve(final URLBuilder r) {
		if(pathBuilder != null) {
			path = pathBuilder.toString();
			pathBuilder = null;
		}
		if(queryBuilder != null) {
			query = queryBuilder.toString();
			queryBuilder = null;
		}
		if(r.pathBuilder != null) {
			r.path = r.pathBuilder.toString();
			r.pathBuilder = null;
		}
		if(r.queryBuilder != null) {
			r.query = r.queryBuilder.toString();
			r.queryBuilder = null;
		}
		final URLBuilder base = this, t = new URLBuilder(null);
		if(r.scheme != null) {
			t.scheme = r.scheme;
			t.user = r.user;
			t.host = r.host;
			t.port = r.port;
			t.path = r.path == null ? null : URLBuilder.removeDotSegments(r.path);
			t.query = r.query;
		}
		else {
			if(r.host != null) {
				t.user = r.user;
				t.host = r.host;
				t.port = r.port;
				t.path = r.path == null ? null : URLBuilder.removeDotSegments(r.path);
				t.query = r.query;
			}
			else {
				if(r.path == null || r.path.length() == 0) {
					t.path = base.path;
					if(r.query != null)
						t.query = r.query;
					else
						t.query = base.query;
				}
				else {
					if(r.path.charAt(0) == '/')
						t.path = URLBuilder.removeDotSegments(r.path);
					else
						t.path = URLBuilder.removeDotSegments(URLBuilder.mergePaths(base.host != null,
								base.path, r.path));
					t.query = r.query;
				}
				t.user = base.user;
				t.host = base.host;
				t.port = base.port;
			}
			t.scheme = base.scheme;
		}
		t.fragment = r.fragment;
		return t;
	}

	private static String mergePaths(boolean baseHasAuthority, String basePath, String rPath) {
		if(baseHasAuthority && (basePath == null || basePath.length() == 0))
			return '/' + rPath;
		int pos = basePath == null ? -1 : basePath.lastIndexOf('/');
		return pos < 0 ? rPath : basePath.substring(0, pos + 1) + rPath;
	}

	private static String removeDotSegments(String input) {
		int offset = 0, length = input.length();
		StringBuilder output = new StringBuilder();
		while(offset < length) {
			// (A.)
			if(input.startsWith("../", offset)) {
				offset += 3;
				continue;
			}
			if(input.startsWith("./", offset)) {
				offset += 2;
				continue;
			}
			// (B.)
			if(input.startsWith("/./", offset)) {
				offset += 2;
				continue;
			}
			if(length - offset == 2 && input.startsWith("/.", offset)) {
				--length;
				continue;
			}
			// (C.)
			if(input.startsWith("/../", offset))
				offset += 3;
			else if(length - offset == 3 && input.startsWith("/..", offset))
				length -= 2;
			else {
				// (D.)
				if(length - offset == 1 && input.charAt(offset) == '.') {
					offset = length;
					continue;
				}
				if(length - offset == 2 && input.startsWith("..", offset)) {
					offset = length;
					continue;
				}
				// (E.)
				//TODO
			}
			int cpos = output.lastIndexOf("/");
			output.setLength(cpos < 0 ? 0 : cpos);
		}
		return output.toString();
	}

	public static URLBuilder decompose(String spec) {
		URLBuilder sink = new URLBuilder(null);
		URLParser.parse(spec, sink);
		return sink;
	}

	public static String encode(String piece) {
		try {
			return URLEncoder.encode(piece, "UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
			throw new Doom("Your JVM doesn't support UTF-8. Say whaaaaaat?");
		}
	}

	public static String decode(String piece) {
		try {
			return URLDecoder.decode(piece, "UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
			throw new Doom("Your JVM doesn't support UTF-8. Say whaaaaaat?");
		}
	}

}
