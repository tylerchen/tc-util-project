/*******************************************************************************
 * Copyright (c) Dec 20, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 20, 2015
 */
public class ZipHelper {
	public static final int BUFFER = 1024 * 10;

	public static void unzip(String zipFile, String dir) {
		BufferedOutputStream dest = null;
		ZipInputStream zis = null;
		try {
			File parent = new File(dir);
			parent.mkdirs();
			FileInputStream fis = new FileInputStream(zipFile);
			CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
			zis = new ZipInputStream(new BufferedInputStream(checksum), Charset.forName("UTF-8"));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				Logger.debug(FCS.get("Extracting: {0}, size: {1}", entry.getName(), entry.getSize()));
				int count;
				byte data[] = new byte[BUFFER];
				File file = new File(parent, entry.getName());
				file.getParentFile().mkdirs();
				try {
					FileOutputStream fos = new FileOutputStream(file);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
				} finally {
					SocketHelper.closeWithoutError(dest);
				}
			}
		} catch (Exception e) {
			Exceptions.runtime("unzip error!", e);
		} finally {
			SocketHelper.closeWithoutError(zis);
		}
	}

	public static void zip(String dir, String zipFileName) {
		BufferedInputStream origin = null;
		ZipOutputStream out = null;
		try {
			FileOutputStream dest = new FileOutputStream(zipFileName);
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			out = new ZipOutputStream(new BufferedOutputStream(checksum));
			//out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
			// get a list of files from current directory
			File f = new File(dir);
			Collection<File> files = FileUtils.listFiles(f, FileFilterUtils.fileFileFilter(),
					FileFilterUtils.directoryFileFilter());

			for (File file : files) {
				System.out.println("Adding: " + file);
				try {
					FileInputStream fi = new FileInputStream(file);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(StringHelper.pathConcat(f.getName(),
							file.getAbsolutePath().substring(f.getAbsolutePath().length())));
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
				} finally {
					SocketHelper.closeWithoutError(origin);
				}
			}
		} catch (Exception e) {
			Exceptions.runtime("zip error!", e);
		} finally {
			SocketHelper.closeWithoutError(out);
		}
	}

	public static void main(String[] args) {
		unzip("/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/main/webapp/WEB-INF/project_base_framework/qdp-1.0.0.zip",
				"/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/main/webapp/WEB-INF/project_base_framework/");
		zip("/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/main/webapp/WEB-INF/project_base_framework/test-project",
				"/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/main/webapp/WEB-INF/project_base_framework/qdp-test.zip");
	}
}
