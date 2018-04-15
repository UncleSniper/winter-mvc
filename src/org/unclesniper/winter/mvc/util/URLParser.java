package org.unclesniper.winter.mvc.util;

import org.unclesniper.winter.mvc.Doom;

public final class URLParser {

	public interface URLPartSink {

		void setScheme(String scheme);

		void setPath(String path);

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
		ABS_PATH,
		ABS_PATH_PERC,
		QUERY,
		QUERY_PERC,
		QUERY_ESC,
		HIER_PART
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
		int length = spec.length(), start = -1;
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
				case ABS_PATH:
				case ABS_PATH_PERC:
				case QUERY:
				case QUERY_PERC:
				case QUERY_ESC:
				case HIER_PART:
					//TODO
				default:
					throw new Doom("Unrecognized state: " + state.name());
			}
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

}
