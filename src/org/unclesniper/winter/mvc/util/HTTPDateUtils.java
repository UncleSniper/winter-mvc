package org.unclesniper.winter.mvc.util;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class HTTPDateUtils {

	private static final String[] DATE_FORMAT_STRINGS = new String[] {
		"EEE, dd MMM yyyy HH:mm:ss zzz",
		"EEEEEE, dd-MMM-yy HH:mm:ss zzz",
		"EEE MMMM d HH:mm:ss yyyy"
	};

	private static final int DATE_FORMAT_POOL_SIZE = 16;

	private static final DateFormat[][] DATE_FORMAT_POOL;

	static {
		DATE_FORMAT_POOL = new DateFormat[DATE_FORMAT_POOL_SIZE][];
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		for(int pi = 0; pi < DATE_FORMAT_POOL_SIZE; ++pi) {
			DATE_FORMAT_POOL[pi] = new DateFormat[DATE_FORMAT_STRINGS.length];
			for(int fi = 0; fi < DATE_FORMAT_STRINGS.length; ++fi) {
				DATE_FORMAT_POOL[pi][fi] = new SimpleDateFormat(DATE_FORMAT_STRINGS[fi], Locale.US);
				DATE_FORMAT_POOL[pi][fi].setTimeZone(gmt);
			}
		}
	}

	private static volatile int DATE_FORMAT_POOL_RR = 0xCAFEBABE;

	private HTTPDateUtils() {}

	private static DateFormat[] getDateFormatsFromPool() {
		int rr = HTTPDateUtils.DATE_FORMAT_POOL_RR;
		rr ^= rr << 13;
		rr ^= rr >>> 17;
		rr ^= rr << 5;
		HTTPDateUtils.DATE_FORMAT_POOL_RR = rr;
		return HTTPDateUtils.DATE_FORMAT_POOL[rr % HTTPDateUtils.DATE_FORMAT_POOL_SIZE];
	}

	public static long parseDateHeader(String spec) {
		DateFormat[] formats = HTTPDateUtils.getDateFormatsFromPool();
		ParsePosition pos = new ParsePosition(0);
		for(int i = 0; i < formats.length; ++i) {
			DateFormat format = formats[i];
			Date date;
			synchronized(format) {
				date = format.parse(spec, pos);
			}
			if(date != null && spec.substring(pos.getIndex()).trim().length() == 0)
				return date.getTime();
			pos.setIndex(0);
		}
		return -1l;
	}

	public static String formatDateHeader(Date date) {
		DateFormat format = HTTPDateUtils.getDateFormatsFromPool()[0];
		synchronized(format) {
			return format.format(date);
		}
	}

	public static String formatDateHeader(long date) {
		return HTTPDateUtils.formatDateHeader(new Date(date));
	}

}
