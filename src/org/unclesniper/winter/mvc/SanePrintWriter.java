package org.unclesniper.winter.mvc;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class SanePrintWriter extends Writer {

	private static final String LINE_BREAK;

	static {
		String lb = System.getProperty("line.separator");
		LINE_BREAK = lb == null || lb.length() == 0 ? "\n" : lb;
	}

	private Writer slave;

	private String lineBreak;

	public SanePrintWriter(Writer slave) {
		this.slave = slave;
	}

	public SanePrintWriter(OutputStream stream, String encoding) throws UnsupportedEncodingException {
		slave = new OutputStreamWriter(stream, encoding);
	}

	public Writer getSlave() {
		return slave;
	}

	public void setSlave(Writer slave) {
		this.slave = slave;
	}

	public String getLineBreak() {
		return lineBreak;
	}

	public void setLineBreak(String lineBreak) {
		this.lineBreak = lineBreak;
	}

	public SanePrintWriter append(char c) throws IOException {
		slave.write(c);
		return this;
	}

	public SanePrintWriter append(CharSequence csq) throws IOException {
		slave.append(csq);
		return this;
	}

	public SanePrintWriter append(CharSequence csq, int start, int end) throws IOException {
		slave.append(csq, start, end);
		return this;
	}

	public void close() throws IOException {
		slave.close();
	}

	public void flush() throws IOException {
		slave.flush();
	}

	public void write(char[] cbuf) throws IOException {
		slave.write(cbuf);
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		slave.write(cbuf, off, len);
	}

	public void write(int c) throws IOException {
		slave.write(c);
	}

	public void write(String str) throws IOException {
		slave.write(str);
	}

	public void write(String str, int off, int len) throws IOException {
		slave.write(str, off, len);
	}

	public void print(boolean b) throws IOException {
		slave.write(String.valueOf(b));
	}

	public void println(boolean b) throws IOException {
		slave.write(String.valueOf(b));
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(char c) throws IOException {
		slave.write(c);
	}

	public void println(char c) throws IOException {
		slave.write(c);
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(int i) throws IOException {
		slave.write(String.valueOf(i));
	}

	public void println(int i) throws IOException {
		slave.write(String.valueOf(i));
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(long l) throws IOException {
		slave.write(String.valueOf(l));
	}

	public void println(long l) throws IOException {
		slave.write(String.valueOf(l));
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(float f) throws IOException {
		slave.write(String.valueOf(f));
	}

	public void println(float f) throws IOException {
		slave.write(String.valueOf(f));
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(double d) throws IOException {
		slave.write(String.valueOf(d));
	}

	public void println(double d) throws IOException {
		slave.write(String.valueOf(d));
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(String s) throws IOException {
		slave.write(s == null ? "null" : s);
	}

	public void println(String s) throws IOException {
		slave.write(s == null ? "null" : s);
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void print(Object obj) throws IOException {
		if(obj == null)
			slave.write("null");
		else {
			String s = obj.toString();
			if(s != null)
				slave.write(s);
		}
	}

	public void println(Object obj) throws IOException {
		print(obj);
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

	public void println() throws IOException {
		slave.write(lineBreak == null ? SanePrintWriter.LINE_BREAK : lineBreak);
	}

}
