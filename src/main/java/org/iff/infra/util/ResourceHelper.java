/*******************************************************************************
 * Copyright (c) 2015-2-11 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

/**
 * Find the resources from jar or classpath or file system.
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-2-11
 */
public class ResourceHelper {

	public static List<String> loadResourcesInJar(final String resPath, final String fileExt, final String include,
			final String exclude) {
		Set<String> list = new LinkedHashSet<String>(64);
		try {
			Enumeration<URL> rs = ResourceHelper.class.getClassLoader().getResources(resPath);
			while (rs.hasMoreElements()) {
				URL url = rs.nextElement();
				String filePath = url.getFile();
				String protocol = url.getProtocol();
				if ("file".equals(protocol) || "jar".equals(protocol)) {
					if (filePath.startsWith("file:")) {
						File file = new File(filePath.substring("file:".length(), filePath.lastIndexOf("!/")));
						java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (entry.isDirectory()) {
								continue;
							}
							String entryName = entry.getName();
							if (!entryName.startsWith(resPath)) {
								continue;
							}
							if (!(fileExt == null || fileExt.length() < 1 || "*".equals(fileExt) || entryName
									.endsWith(fileExt))) {
								continue;
							}
							if (include != null && include.length() > 0 && !"*".equals(include)
									&& !wildCardMatch(entryName, include)) {
								continue;
							}
							if (exclude != null && exclude.length() > 0 && wildCardMatch(entryName, exclude)) {
								continue;
							}
							list.add(protocol + ":" + filePath.substring(0, filePath.lastIndexOf("!/") + 2)
									+ entryName.replaceAll("\\\\", "/"));
						}
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	public static List<String> loadResourcesInClassPath(final String resPath, final String fileExt,
			final String include, final String exclude) {
		Set<String> list = new LinkedHashSet<String>(64);
		try {
			Enumeration<URL> rs = ResourceHelper.class.getClassLoader().getResources(resPath);
			while (rs.hasMoreElements()) {
				URL url = rs.nextElement();
				String filePath = url.getFile();
				String protocol = url.getProtocol();
				if ("file".equals(protocol) || "jar".equals(protocol)) {
					if (filePath.startsWith("file:")) {
						File file = new File(filePath.substring("file:".length(), filePath.lastIndexOf("!/")));
						java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (entry.isDirectory()) {
								continue;
							}
							String entryName = entry.getName();
							if (!entryName.startsWith(resPath)) {
								continue;
							}
							if (!(fileExt == null || fileExt.length() < 1 || "*".equals(fileExt) || entryName
									.endsWith(fileExt))) {
								continue;
							}
							if (include != null && include.length() > 0 && !"*".equals(include)
									&& !wildCardMatch(entryName, include)) {
								continue;
							}
							if (exclude != null && exclude.length() > 0 && wildCardMatch(entryName, exclude)) {
								continue;
							}
							list.add(protocol + ":" + filePath.substring(0, filePath.lastIndexOf("!/") + 2)
									+ entryName.replaceAll("\\\\", "/"));
						}
					} else {// is file system in class path
						String entryName = PlatformHelper.isWindows() ? StringHelper.pathBuild(filePath, "/")
								.replaceAll("^/|/$", "") : StringHelper.pathBuild(filePath, "/").replaceAll("/$", "");
						list.addAll(loadResourcesInFileSystem(entryName, fileExt, include, exclude));
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	public static List<String> loadResourcesInFileSystem(final String resPath, final String fileExt,
			final String include, final String exclude) {
		Set<String> list = new LinkedHashSet<String>(64);
		java.util.LinkedList<File> linked = new java.util.LinkedList<File>();
		linked.add(new File(resPath));
		File file = null;
		while ((file = linked.pollLast()) != null) {
			if (!file.exists()) {
				continue;
			}
			List<File> files = new ArrayList<File>();
			if (file.isDirectory()) {
				File[] fileSet = file.listFiles();
				for (File f : fileSet) {
					if (f.isDirectory()) {
						linked.add(f);
					} else {
						files.add(f);
					}
				}
			} else {
				files.add(file);
			}
			for (File f : files) {
				String entryName = StringHelper.pathBuild(f.getAbsolutePath(), "/");
				if (!(fileExt == null || fileExt.length() < 1 || "*".equals(fileExt) || entryName.endsWith(fileExt))) {
					continue;
				}
				if (include != null && include.length() > 0 && !"*".equals(include)
						&& !wildCardMatch(entryName, include)) {
					continue;
				}
				if (exclude != null && exclude.length() > 0 && wildCardMatch(entryName, exclude)) {
					continue;
				}
				list.add(entryName);
			}
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	public static boolean wildCardMatch(String text, String pattern) {
		if (pattern == null || pattern.length() < 1 || "*".equals(pattern)) {
			return true;
		}
		if (text == null || text.length() < 1) {
			return false;
		}
		String[] cards = pattern.split("\\*");
		for (String card : cards) {
			int idx = text.indexOf(card);
			if (idx == -1) {
				return false;
			}
			text = text.substring(idx + card.length());
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(loadResourcesInJar("META-INF/tc-framework-test", ".groovy", "*", "*/TCMain.groovy"));
		System.out.println(loadResourcesInClassPath("META-INF", ".groovy", "*", "*/TCMain.groovy"));
		System.out.println(loadResourcesInFileSystem("g:/bak/app_root/webapp", ".xml", "", ""));
	}
}
