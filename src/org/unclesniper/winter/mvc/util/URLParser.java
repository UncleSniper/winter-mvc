package org.unclesniper.winter.mvc.util;

import org.unclesniper.winter.mvc.Doom;

public final class URLParser {

	public interface URLPartSink {

		void setScheme(String scheme);

		void setUser(String user);

		void setHost(String host);

		void setPort(int port);

		void setPath(String path);

		void setQuery(String query);

		void setFragment(String fragment);

	}

	private enum State {
		EMPTY,
		FRAGMENT,
		FRAGMENT_PERC,
		FRAGMENT_ESC,
		REL_SLASH,
		SCHEME_OR_REL_PATH,
		REL_SEGMENT,
		REL_SEGMENT_PERC,
		REL_SEGMENT_ESC,
		USERINFO_OR_HOST,
		HOST,
		USERINFO_OR_HOST_PERC,
		USERINFO_OR_HOST_ESC,
		USERINFO_OR_PORT,
		PORT,
		USERINFO_OR_PORT_PERC,
		USERINFO_OR_PORT_ESC,
		ABS_PATH,
		ABS_PATH_PERC,
		ABS_PATH_ESC,
		QUERY,
		QUERY_PERC,
		QUERY_ESC,
		HIER_PART,
		HIER_PART_SLASH
	}

	private enum HostnameState {
		EMPTY,
		DOMAIN_LABEL,
		TOP_LABEL,
		DOMAIN_LABEL_MINUS,
		TOP_LABEL_MINUS,
		DOT_AFTER_DOMAIN,
		DOT_AFTER_TOP,
		PORT,
		ERROR
	}

	private enum IPv4AddressState {
		EMPTY,
		OCTET1,
		DOT_AFTER_OCTET1,
		OCTET2,
		DOT_AFTER_OCTET2,
		OCTET3,
		DOT_AFTER_OCTET3,
		OCTET4,
		PORT,
		ERROR
	}

	private URLParser() {}

	public static void parse(String spec, URLPartSink sink) {
		/* ==== RFC 2396 ====
		 * excluded: 00-1F, 7F, 20, "<" | ">" | <">
		 * uric          = reserved | unreserved | escaped
		 * reserved      = ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" |
		 *                 "$" | ","
		 * unreserved    = alphanum | mark
		 * mark          = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
		 * escaped       = "%" hex hex
		 * URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
		 * absoluteURI   = scheme ":" hier_part
		 * relativeURI   = ( net_path | abs_path | rel_path ) [ "?" query ]
		 * hier_part     = ( net_path | abs_path ) [ "?" query ]
		 * net_path      = "//" authority [ abs_path ]
		 * abs_path      = "/" path_segments
		 * scheme        = alpha *( alpha | digit | "+" | "-" | "." )
		 * authority     = server | reg_name
		 * reg_name      = 1*( unreserved | escaped | "$" | "," |
		 *                  ";" | ":" | "@" | "&" | "=" | "+" )
		 * server        = [ [ userinfo "@" ] hostport ]
		 * userinfo      = *( unreserved | escaped |
		 *                  ";" | ":" | "&" | "=" | "+" | "$" | "," )
		 * hostport      = host [ ":" port ]
		 * host          = hostname | IPv4address
		 * hostname      = *( domainlabel "." ) toplabel [ "." ]
		 * domainlabel   = alphanum | alphanum *( alphanum | "-" ) alphanum
		 * toplabel      = alpha | alpha *( alphanum | "-" ) alphanum
		 * IPv4address   = 1*digit "." 1*digit "." 1*digit "." 1*digit
		 * port          = *digit
		 * path_segments = segment *( "/" segment )
		 * segment       = *pchar
		 * pchar         = unreserved | escaped |
		 *                 ":" | "@" | "&" | "=" | "+" | "$" | "," | ";"
		 * query         = *uric
		 * fragment      = *uric
		 * rel_path      = rel_segment [ abs_path ]
		 * rel_segment   = 1*( unreserved | escaped |
		 *                 ";" | "@" | "&" | "=" | "+" | "$" | "," )
		 */
		State state = State.EMPTY;
		HostnameState hostnameState = null;
		IPv4AddressState ipv4AddressState = null;
		int length = spec.length(), start = -1, hostPortOffset = -1;
		for(int i = 0; i < length; ++i) {
			char c = spec.charAt(i);
			switch(state) {
				case EMPTY:
					// absoluteURI > scheme | relativeURI > (net_path | abs_path | rel_path)
					switch(c) {
						case '#':
							start = i + 1;
							state = State.FRAGMENT;
							break;
						case '/':  // net_path | abs_path
							state = State.REL_SLASH;
							break;
						// mark
						case '-':
						case '_':
						case '.':
						case '!':
						case '~':
						case '*':
						case '\'':
						case '(':
						case ')':
						// rel_segment
						case ';':
						case '@':
						case '&':
						case '=':
						case '+':
						case '$':
						case ',':
							state = State.REL_SEGMENT;
							break;
						case '%':
							state = State.REL_SEGMENT_PERC;
							break;
						default:
							if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
								state = State.SCHEME_OR_REL_PATH;
							else if(c >= '0' && c <= '9')
								state = State.REL_SEGMENT;
							else
								throw new IllegalURIException(spec, i);
					}
					break;
				case FRAGMENT:
					if(c == '%')
						state = State.FRAGMENT_PERC;
					else if(!URLParser.isURIC(c))
						throw new IllegalURIException(spec, i);
					break;
				case FRAGMENT_PERC:
				case FRAGMENT_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.FRAGMENT_PERC ? State.FRAGMENT_ESC : State.FRAGMENT;
					else
						throw new IllegalURIException(spec, i);
					break;
				case REL_SLASH:
					switch(c) {
						case '/':
							start = i + 1;
							state = State.USERINFO_OR_HOST;
							hostnameState = HostnameState.EMPTY;
							ipv4AddressState = IPv4AddressState.EMPTY;
							break;
						case '%':
							start = 0;
							state = State.ABS_PATH_PERC;
							break;
						case '?':
							sink.setPath("/");
							start = i + 1;
							state = State.QUERY;
							break;
						case '#':
							sink.setPath("/");
							start = i + 1;
							state = State.FRAGMENT;
							break;
						default:
							if(URLParser.isPChar(c)) {
								start = 0;
								state = State.ABS_PATH;
							}
							else
								throw new IllegalURIException(spec, i);
					}
					break;
				case SCHEME_OR_REL_PATH:
					switch(c) {
						case '-':
						case '.':
						case '+':
							break;
						case '_':
						case '!':
						case '~':
						case '*':
						case '\'':
						case '(':
						case ')':
						case '@':
						case '&':
						case '=':
						case '$':
						case ',':
						case ';':
							state = State.REL_SEGMENT;
							break;
						case ':':
							sink.setScheme(spec.substring(0, i));
							state = State.HIER_PART;
							break;
						default:
							if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9'))
								throw new IllegalURIException(spec, i);
							break;
					}
					break;
				case REL_SEGMENT:
					switch(c) {
						case ':':
							throw new IllegalURIException(spec, i);
						case '?':
							sink.setPath(spec.substring(0, i));
							start = i + 1;
							state = State.QUERY;
							break;
						case '#':
							sink.setPath(spec.substring(0, i));
							start = i + 1;
							state = State.FRAGMENT;
							break;
						case '/':
							start = 0;
							state = State.ABS_PATH;
							break;
						case '%':
							state = State.REL_SEGMENT_PERC;
							break;
						default:
							if(!URLParser.isPChar(c))
								throw new IllegalURIException(spec, i);
							break;
					}
					break;
				case REL_SEGMENT_PERC:
				case REL_SEGMENT_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.REL_SEGMENT_PERC ? State.REL_SEGMENT_ESC : State.REL_SEGMENT;
					else
						throw new IllegalURIException(spec, i);
					break;
				case USERINFO_OR_HOST:
					if(c == '@') {
						sink.setUser(spec.substring(start, i));
						start = i + 1;
						state = State.HOST;
						hostnameState = HostnameState.EMPTY;
						ipv4AddressState = IPv4AddressState.EMPTY;
						break;
					}
				case HOST:
					{
						boolean skip;
						switch(c) {
							case '/':
							case '?':
							case '#':
								if(hostnameState != HostnameState.TOP_LABEL
										&& hostnameState != HostnameState.DOT_AFTER_TOP
										&& ipv4AddressState != IPv4AddressState.OCTET4)
									throw new IllegalURIException(spec, i);
								sink.setHost(spec.substring(start, i));
								switch(c) {
									case '/':
										start = i;
										state = State.ABS_PATH;
										break;
									case '?':
										start = i + 1;
										state = State.QUERY;
										break;
									case '#':
										start = i + 1;
										state = State.FRAGMENT;
										break;
								}
								skip = true;
								break;
							case '%':
								if(state == State.HOST)
									throw new IllegalURIException(spec, i);
								hostnameState = HostnameState.ERROR;
								ipv4AddressState = IPv4AddressState.ERROR;
								state = State.USERINFO_OR_HOST_PERC;
								skip = true;
								break;
							default:
								if(!URLParser.isUserInfoChar(c))
									throw new IllegalURIException(spec, i);
								skip = false;
								break;
						}
						if(skip)
							break;
						boolean isnum = c >= '0' && c <= '9';
						boolean isalpha = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
						switch(hostnameState) {
							case EMPTY:
								if(isalpha)
									hostnameState = HostnameState.TOP_LABEL;
								else if(isnum)
									hostnameState = HostnameState.DOMAIN_LABEL;
								else
									hostnameState = HostnameState.ERROR;
								break;
							case DOMAIN_LABEL:
								switch(c) {
									case '-':
										hostnameState = HostnameState.DOMAIN_LABEL_MINUS;
										break;
									case '.':
										hostnameState = HostnameState.DOT_AFTER_DOMAIN;
										break;
									case ':':
										hostnameState = HostnameState.PORT;
										break;
									default:
										if(!isnum && !isalpha)
											hostnameState = HostnameState.ERROR;
										break;
								}
								break;
							case TOP_LABEL:
								switch(c) {
									case '-':
										hostnameState = HostnameState.TOP_LABEL_MINUS;
										break;
									case '.':
										hostnameState = HostnameState.DOT_AFTER_TOP;
										break;
									case ':':
										hostnameState = HostnameState.PORT;
										break;
									default:
										if(!isnum && !isalpha)
											hostnameState = HostnameState.ERROR;
										break;
								}
								break;
							case DOMAIN_LABEL_MINUS:
								switch(c) {
									case '-':
										break;
									case '.':
									case ':':
										hostnameState = HostnameState.ERROR;
										break;
									default:
										if(isnum || isalpha)
											hostnameState = HostnameState.DOMAIN_LABEL;
										else
											hostnameState = HostnameState.ERROR;
										break;
								}
								break;
							case TOP_LABEL_MINUS:
								switch(c) {
									case '-':
										break;
									case '.':
									case ':':
										hostnameState = HostnameState.ERROR;
										break;
									default:
										if(isnum || isalpha)
											hostnameState = HostnameState.TOP_LABEL;
										else
											hostnameState = HostnameState.ERROR;
										break;
								}
								break;
							case DOT_AFTER_TOP:
								if(c == ':') {
									hostnameState = HostnameState.PORT;
									break;
								}
							case DOT_AFTER_DOMAIN:
								if(isalpha)
									hostnameState = HostnameState.TOP_LABEL;
								else if(isnum)
									hostnameState = HostnameState.DOMAIN_LABEL;
								else
									hostnameState = HostnameState.ERROR;
								break;
							case PORT:
								hostnameState = HostnameState.ERROR;
							case ERROR:
								break;
							default:
								throw new Doom("Unrecognized state: " + hostnameState.name());
						}
						switch(ipv4AddressState) {
							case EMPTY:
								if(isnum)
									ipv4AddressState = IPv4AddressState.OCTET1;
								else
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case OCTET1:
								if(c == '.')
									ipv4AddressState = IPv4AddressState.DOT_AFTER_OCTET1;
								else if(!isnum)
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case DOT_AFTER_OCTET1:
								if(isnum)
									ipv4AddressState = IPv4AddressState.OCTET2;
								else
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case OCTET2:
								if(c == '.')
									ipv4AddressState = IPv4AddressState.DOT_AFTER_OCTET2;
								else if(!isnum)
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case DOT_AFTER_OCTET2:
								if(isnum)
									ipv4AddressState = IPv4AddressState.OCTET3;
								else
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case OCTET3:
								if(c == '.')
									ipv4AddressState = IPv4AddressState.DOT_AFTER_OCTET3;
								else if(!isnum)
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case DOT_AFTER_OCTET3:
								if(isnum)
									ipv4AddressState = IPv4AddressState.OCTET4;
								else
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case OCTET4:
								if(c == ':')
									ipv4AddressState = IPv4AddressState.PORT;
								else if(!isnum)
									ipv4AddressState = IPv4AddressState.ERROR;
								break;
							case PORT:
								ipv4AddressState = IPv4AddressState.ERROR;
							case ERROR:
								break;
							default:
								throw new Doom("Unrecognized state: " + ipv4AddressState.name());
						}
					}
					if(state == State.HOST && hostnameState == HostnameState.ERROR
							&& ipv4AddressState == IPv4AddressState.ERROR)
						throw new IllegalURIException(spec, i);
					if(c == ':') {
						if(state == State.USERINFO_OR_HOST)
							state = State.USERINFO_OR_PORT;
						else if(hostnameState == HostnameState.PORT || ipv4AddressState == IPv4AddressState.PORT)
							state = State.PORT;
						else
							throw new IllegalURIException(spec, i);
						hostPortOffset = i;
					}
					break;
				case USERINFO_OR_HOST_PERC:
				case USERINFO_OR_HOST_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.USERINFO_OR_HOST_PERC
								? State.USERINFO_OR_HOST_ESC : State.USERINFO_OR_HOST;
					else
						throw new IllegalURIException(spec, i);
					break;
				case USERINFO_OR_PORT:
					if(c == '@') {
						sink.setUser(spec.substring(start, i));
						start = i + 1;
						state = State.HOST;
						hostnameState = HostnameState.EMPTY;
						ipv4AddressState = IPv4AddressState.EMPTY;
						break;
					}
				case PORT:
					switch(c) {
						case '/':
						case '?':
						case '#':
							if(hostnameState != HostnameState.TOP_LABEL
									&& hostnameState != HostnameState.DOT_AFTER_TOP
									&& ipv4AddressState != IPv4AddressState.OCTET4)
								throw new IllegalURIException(spec, i);
							sink.setHost(spec.substring(start, hostPortOffset));
							start = hostPortOffset + 1;
							{
								int port;
								try {
									port = start == i ? -1 : Integer.parseInt(spec.substring(start, i));
								}
								catch(NumberFormatException nfe) {
									throw new IllegalURIException(spec, i, nfe);
								}
								sink.setPort(port);
							}
							switch(c) {
								case '/':
									start = i;
									state = State.ABS_PATH;
									break;
								case '?':
									start = i + 1;
									state = State.QUERY;
									break;
								case '#':
									start = i + 1;
									state = State.FRAGMENT;
									break;
							}
							break;
						case '%':
							if(state == State.PORT)
								throw new IllegalURIException(spec, i);
							hostnameState = HostnameState.ERROR;
							ipv4AddressState = IPv4AddressState.ERROR;
							state = State.USERINFO_OR_PORT_PERC;
							break;
						default:
							if(!URLParser.isUserInfoChar(c))
								throw new IllegalURIException(spec, i);
							if(c < '0' || c > '9') {
								hostnameState = HostnameState.ERROR;
								ipv4AddressState = IPv4AddressState.ERROR;
							}
							break;
					}
					break;
				case USERINFO_OR_PORT_PERC:
				case USERINFO_OR_PORT_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.USERINFO_OR_PORT_PERC
								? State.USERINFO_OR_PORT_ESC : State.USERINFO_OR_PORT;
					else
						throw new IllegalURIException(spec, i);
					break;
				case ABS_PATH:
					switch(c) {
						case ':':
							throw new IllegalURIException(spec, i);
						case '?':
							sink.setPath(spec.substring(start, i));
							start = i + 1;
							state = State.QUERY;
							break;
						case '#':
							sink.setPath(spec.substring(start, i));
							start = i + 1;
							state = State.FRAGMENT;
							break;
						case '/':
							break;
						case '%':
							state = State.ABS_PATH_PERC;
							break;
						default:
							if(!URLParser.isPChar(c))
								throw new IllegalURIException(spec, i);
							break;
					}
					break;
				case ABS_PATH_PERC:
				case ABS_PATH_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.ABS_PATH_PERC ? State.ABS_PATH_ESC : State.ABS_PATH;
					else
						throw new IllegalURIException(spec, i);
					break;
				case QUERY:
					switch(c) {
						case '%':
							state = State.QUERY_PERC;
							break;
						case '#':
							sink.setQuery(spec.substring(start, i));
							start = i + 1;
							state = State.FRAGMENT;
							break;
						default:
							if(!URLParser.isURIC(c))
								throw new IllegalURIException(spec, i);
							break;
					}
					break;
				case QUERY_PERC:
				case QUERY_ESC:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						state = state == State.QUERY_PERC ? State.QUERY_ESC : State.QUERY;
					else
						throw new IllegalURIException(spec, i);
					break;
				case HIER_PART:
					if(c != '/')
						throw new IllegalURIException(spec, i);
					state = State.HIER_PART_SLASH;
					break;
				case HIER_PART_SLASH:
					switch(c) {
						case '/':
							start = i + 1;
							state = State.USERINFO_OR_HOST;
							hostnameState = HostnameState.EMPTY;
							ipv4AddressState = IPv4AddressState.EMPTY;
							break;
						case '%':
							start = i - 1;
							state = State.ABS_PATH_PERC;
							break;
						case '?':
							sink.setPath("/");
							start = i + 1;
							state = State.QUERY;
							break;
						case '#':
							sink.setPath("/");
							start = i + 1;
							state = State.FRAGMENT;
							break;
						default:
							if(URLParser.isPChar(c)) {
								start = i - 1;
								state = State.ABS_PATH;
							}
							else
								throw new IllegalURIException(spec, i);
					}
					break;
				default:
					throw new Doom("Unrecognized state: " + state.name());
			}
		}
		switch(state) {
			case FRAGMENT:
				sink.setFragment(spec.substring(start));
				break;
			case REL_SLASH:
			case HIER_PART_SLASH:
				sink.setPath("/");
				break;
			case SCHEME_OR_REL_PATH:
			case REL_SEGMENT:
				sink.setPath(spec);
				break;
			case USERINFO_OR_HOST:
			case HOST:
				if(hostnameState != HostnameState.TOP_LABEL
						&& hostnameState != HostnameState.DOT_AFTER_TOP
						&& ipv4AddressState != IPv4AddressState.OCTET4)
					throw new IllegalURIException(spec, length);
				sink.setHost(spec.substring(start));
				break;
			case USERINFO_OR_PORT:
			case PORT:
				if(hostnameState != HostnameState.TOP_LABEL
						&& hostnameState != HostnameState.DOT_AFTER_TOP
						&& ipv4AddressState != IPv4AddressState.OCTET4)
					throw new IllegalURIException(spec, length);
				sink.setHost(spec.substring(start, hostPortOffset));
				start = hostPortOffset + 1;
				{
					int port;
					try {
						port = start == length ? -1 : Integer.parseInt(spec.substring(start));
					}
					catch(NumberFormatException nfe) {
						throw new IllegalURIException(spec, length, nfe);
					}
					sink.setPort(port);
				}
				break;
			case ABS_PATH:
				sink.setPath(spec.substring(start));
				break;
			case QUERY:
				sink.setQuery(spec.substring(start));
				break;
			case EMPTY:
			case FRAGMENT_PERC:
			case FRAGMENT_ESC:
			case REL_SEGMENT_PERC:
			case REL_SEGMENT_ESC:
			case USERINFO_OR_HOST_PERC:
			case USERINFO_OR_HOST_ESC:
			case USERINFO_OR_PORT_PERC:
			case USERINFO_OR_PORT_ESC:
			case ABS_PATH_PERC:
			case ABS_PATH_ESC:
			case QUERY_PERC:
			case QUERY_ESC:
			case HIER_PART:
				throw new IllegalURIException(spec, length);
			default:
				throw new Doom("Unrecognized state: " + state.name());
		}
	}

	private static boolean isURIC(char c) {
		// need to do 'escaped' separately
		switch(c) {
			// reserved
			case ';':
			case '/':
			case '?':
			case ':':
			case '@':
			case '&':
			case '=':
			case '+':
			case '$':
			case ',':
			// mark
			case '-':
			case '_':
			case '.':
			case '!':
			case '~':
			case '*':
			case '\'':
			case '(':
			case ')':
				return true;
			default:
				return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
		}
	}

	private static boolean isPChar(char c) {
		// need to do 'escaped' separately
		switch(c) {
			// mark
			case '-':
			case '_':
			case '.':
			case '!':
			case '~':
			case '*':
			case '\'':
			case '(':
			case ')':
			// rest
			case ':':
			case '@':
			case '&':
			case '=':
			case '+':
			case '$':
			case ',':
			case ';':
				return true;
			default:
				return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
		}
	}

	private static boolean isUserInfoChar(char c) {
		// need to do 'escaped' separately
		switch(c) {
			// mark
			case '-':
			case '_':
			case '.':
			case '!':
			case '~':
			case '*':
			case '\'':
			case '(':
			case ')':
			// rest
			case ':':
			case '&':
			case '=':
			case '+':
			case '$':
			case ',':
			case ';':
				return true;
			default:
				return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
		}
	}

}
