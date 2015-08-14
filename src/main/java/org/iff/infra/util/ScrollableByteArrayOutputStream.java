/*******************************************************************************
 * Copyright (c) 2015-2-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * A scroll-able byte array output stream for holding the recently content, specially for console output. 
 * double buffer design to reduce the array copy times.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-17
 */
public class ScrollableByteArrayOutputStream extends ByteArrayOutputStream {

	public ScrollableByteArrayOutputStream() {
		this(1024 * 1024 * 10 * 2);//10Mbytes // * 2 for double buffer
	}

	public ScrollableByteArrayOutputStream(int size) {
		super(size * 2);// * 2 for double buffer
	}

	public synchronized void write(byte[] b, int off, int len) {
		if (len > super.buf.length) {// input length more than buf length
			super.count = 0;
			super.write(b, off + (len - super.buf.length), super.buf.length);
		} else if (super.count + len > super.buf.length) {// content length and input length more than buf length, remove old data
			byte[] bs = Arrays.copyOfRange(super.buf, super.count - (super.buf.length - len), super.count);
			System.arraycopy(bs, 0, super.buf, 0, bs.length);
			super.count = bs.length;
			super.write(b, off, len);
		} else {
			super.write(b, off, len);
		}
	}

	public synchronized void write(int b) {
		if (super.count + 1 > super.buf.length) {
			byte[] bs = Arrays.copyOfRange(super.buf, super.count - (super.buf.length - 1), super.count);
			System.arraycopy(bs, 0, super.buf, 0, bs.length);
			super.count = bs.length;
			super.write(b);
		} else {
			super.write(b);
		}
	}

	public synchronized int size() {
		return super.count > super.buf.length / 2 ? super.buf.length / 2 : super.count;
	}

	public synchronized byte[] toByteArray() {
		return Arrays.copyOfRange(super.buf, super.count > super.buf.length / 2 ? super.count - super.buf.length / 2
				: 0, super.count);
	}

	public synchronized String toString() {
		return new String(super.buf, super.count > super.buf.length / 2 ? super.count - super.buf.length / 2 : 0,
				super.count > super.buf.length / 2 ? super.buf.length / 2 : super.count);
	}

	@Deprecated
	public synchronized String toString(int hibyte) {
		return new String(super.buf, hibyte, super.count > super.buf.length / 2 ? super.count - super.buf.length / 2
				: 0, super.count > super.buf.length / 2 ? super.buf.length / 2 : super.count);
	}

	public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(super.buf, super.count > super.buf.length / 2 ? super.count - super.buf.length / 2 : 0,
				super.count > super.buf.length / 2 ? super.buf.length / 2 : super.count, charsetName);
	}

	public synchronized void writeTo(OutputStream out) throws IOException {
		out.write(super.buf, super.count > super.buf.length / 2 ? super.count - super.buf.length / 2 : 0,
				super.count > super.buf.length / 2 ? super.buf.length / 2 : super.count);
	}

	public static void main(String[] args) {
		ScrollableByteArrayOutputStream baos = new ScrollableByteArrayOutputStream(3);
		baos.write(new byte[] { 'a', 'b' }, 0, 2);
		System.out.println(baos.toString() + ", size:" + baos.size() + "----->expect: ab");
		baos.write(new byte[] { 'a', 'b' }, 0, 2);
		System.out.println(baos.toString() + ", size:" + baos.size() + "----->expect: bab");
		baos.write('c');
		System.out.println(baos.toString() + ", size:" + baos.size() + "----->expect: abc");
		baos.write(new byte[] { 'a', 'b', 'c', 'd' }, 0, 4);
		System.out.println(baos.toString() + ", size:" + baos.size() + "----->expect: bcd");
	}
}
