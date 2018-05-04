/*******************************************************************************
 * Copyright (c) 2014-6-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.InputStream;
import java.util.Iterator;

/**
 * split file to small file.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-6-20
 */
public class FileSplitHelper {

	/**
	 * split input stream by length.
	 * @param is
	 * @param length
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Iterator<byte[]> split(final InputStream is, final int length) {
		return new Iterator<byte[]>() {
			byte[] buffer = null;

			public boolean hasNext() {
				try {
					if (buffer != null && buffer.length > 0) {
						return true;
					} else {
						buffer = new byte[length];
						int len = is.read(buffer, 0, length);
						if (len < 1) {
							buffer = new byte[0];
						} else if (len < length) {
							byte[] temp = new byte[len];
							System.arraycopy(buffer, 0, temp, 0, len);
							buffer = temp;
						}
						return buffer.length > 0;
					}
				} catch (Exception e) {
					Exceptions.runtime("[9010][FileSplitHelper.Iterator.hasNext()]", e);
				}
				return false;
			}

			public byte[] next() {
				if (buffer == null) {
					hasNext();
				}
				byte[] temp = buffer;
				buffer = null;
				return temp;
			}

			public void remove() {
				Logger.warn("[FileSplitHelper.Iterator.remove is not support!]");
			}
		};
	}
}
