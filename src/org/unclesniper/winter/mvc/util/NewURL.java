package org.unclesniper.winter.mvc.util;

public final class NewURL {

	private NewURL() {}

	public static URLBuilder scheme(String scheme) {
		return new URLBuilder(scheme);
	}

	public static URLBuilder http() {
		return new URLBuilder("http");
	}

	public static URLBuilder https() {
		return new URLBuilder("https");
	}

	public static URLBuilder file() {
		return new URLBuilder("file");
	}

	public static URLBuilder user(String user) {
		return new URLBuilder(null, user, null, null, null, null);
	}

	public static URLBuilder userEsc(String user) {
		if(user != null)
			user = URLBuilder.encode(user);
		return new URLBuilder(null, user, null, null, null, null);
	}

	public static URLBuilder port(int port) {
		return new URLBuilder(null, null, port, null);
	}

	public static URLBuilder path(String path) {
		return new URLBuilder(null, path);
	}

	public static URLBuilder pathEsc(String path) {
		if(path != null)
			path = URLBuilder.encode(path);
		return new URLBuilder(null, path);
	}

	public static URLBuilder slash() {
		return new URLBuilder(null, "/");
	}

	public static URLBuilder query(String query) {
		return new URLBuilder(null, null, null, query);
	}

	public static URLBuilder queryEsc(String query) {
		if(query != null)
			query = URLBuilder.encode(query);
		return new URLBuilder(null, null, null, query);
	}

	public static URLBuilder param(String param) {
		return new URLBuilder((String)null).param(param);
	}

	public static URLBuilder paramEsc(String param) {
		return new URLBuilder((String)null).paramEsc(param);
	}

	public static URLBuilder param(String key, String value) {
		return new URLBuilder((String)null).param(key, value);
	}

	public static URLBuilder fragment(String fragment) {
		return new URLBuilder(null, null, null, null, fragment);
	}

}
