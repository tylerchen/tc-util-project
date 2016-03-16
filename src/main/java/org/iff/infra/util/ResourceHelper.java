/*******************************************************************************
 * Copyright (c) 2015-2-11 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import org.apache.commons.lang3.StringUtils;

/**
 * Find the resources from jar or classpath or file system.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-11
 */
public class ResourceHelper {

	/**
	 * <pre>
	 * Loading resources by protocol.
	 * if protocol starts with "jar://" then return loadResourcesInJar
	 * if protocol starts with "classpath://" then return loadResourcesInClassPath
	 * if protocal starts with "file://" then return loadResourcesInFileSystem
	 * </pre>
	 * @param resPathWithProtocol
	 * @param fileExt
	 * @param include
	 * @param exclude
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-20
	 */
	public static List<String> loadResources(final String resPathWithProtocol, final String fileExt,
			final String include, final String exclude) {
		return loadResources(resPathWithProtocol, fileExt, include, exclude, ResourceHelper.class.getClassLoader());
	}

	public static List<String> loadResources(final String resPathWithProtocol, final String fileExt,
			final String include, final String exclude, ClassLoader cl) {
		if (resPathWithProtocol.startsWith("jar://")) {
			return loadResourcesInJar(resPathWithProtocol.substring("jar://".length()), fileExt, include, exclude, cl);
		} else if (resPathWithProtocol.startsWith("classpath://")) {
			return loadResourcesInClassPath(resPathWithProtocol.substring("classpath://".length()), fileExt, include,
					exclude, cl);
		} else if (resPathWithProtocol.startsWith("file://")) {
			return loadResourcesInFileSystem(resPathWithProtocol.substring("file://".length()), fileExt, include,
					exclude);
		}
		return new ArrayList<String>();
	}

	public static List<String> loadResourcesInJar(final String resPath, final String fileExt, final String include,
			final String exclude) {
		return loadResourcesInJar(resPath, fileExt, include, exclude, Thread.currentThread().getContextClassLoader());
	}

	public static List<String> loadResourcesInJar(final String resPath, final String fileExt, final String include,
			final String exclude, ClassLoader cl) {
		Set<String> list = new LinkedHashSet<String>(64);
		try {
			final String fixPath = StringUtils.removeStart(resPath, "/");
			Enumeration<URL> rs = cl.getResources(fixPath);
			while (rs.hasMoreElements()) {
				String filePath = "";
				try {
					URL url = rs.nextElement();//jar:file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
					filePath = new URLDecoder().decode(url.getFile(), "UTF-8");//file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
					if (filePath.lastIndexOf(".jar!/") > -1) {
						filePath = filePath.indexOf(':') > -1 ? StringUtils.substringAfter(filePath, ":") : filePath;
						filePath = StringUtils.substringBefore(filePath, "!/");
						File file = new File(filePath);
						java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (entry.isDirectory()) {
								continue;
							}
							String entryName = entry.getName();
							if (!entryName.startsWith(fixPath)) {
								continue;
							}
							if (!(fileExt == null || fileExt.length() < 1 || "*".equals(fileExt)
									|| entryName.endsWith(fileExt))) {
								continue;
							}
							if (include != null && include.length() > 0 && !"*".equals(include)
									&& !wildCardMatch(entryName, include)) {
								continue;
							}
							if (exclude != null && exclude.length() > 0 && wildCardMatch(entryName, exclude)) {
								continue;
							}
							String urlString = "jar:file://" + filePath + "!/" + entryName.replaceAll("\\\\", "/");
							list.add(fixUrl(urlString));
						}
						jarFile.close();
					}
				} catch (Exception e) {
					Logger.warn(FCS.get("[loadResourcesInClassPath][{file}]: loading resource error! ", filePath), e);
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
		return loadResourcesInClassPath(resPath, fileExt, include, exclude,
				Thread.currentThread().getContextClassLoader());
	}

	public static List<String> loadResourcesInClassPath(final String resPath, final String fileExt,
			final String include, final String exclude, ClassLoader cl) {
		Set<String> list = new LinkedHashSet<String>(64);
		try {
			final String fixPath = StringUtils.removeStart(resPath, "/");
			Enumeration<URL> rs = cl.getResources(fixPath);
			while (rs.hasMoreElements()) {
				String filePath = "";
				try {
					URL url = rs.nextElement();
					filePath = new URLDecoder().decode(url.getFile(), "UTF-8");
					if (filePath.lastIndexOf(".jar!/") > -1) {
						filePath = filePath.indexOf(':') > -1 ? StringUtils.substringAfter(filePath, ":") : filePath;
						filePath = StringUtils.substringBefore(filePath, "!/");
						File file = new File(filePath);
						java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (entry.isDirectory()) {
								continue;
							}
							String entryName = entry.getName();
							if (!entryName.startsWith(fixPath)) {
								continue;
							}
							if (!(fileExt == null || fileExt.length() < 1 || "*".equals(fileExt)
									|| entryName.endsWith(fileExt))) {
								continue;
							}
							if (include != null && include.length() > 0 && !"*".equals(include)
									&& !wildCardMatch(entryName, include)) {
								continue;
							}
							if (exclude != null && exclude.length() > 0 && wildCardMatch(entryName, exclude)) {
								continue;
							}

							String urlString = "jar:file://" + filePath + "!/" + entryName.replaceAll("\\\\", "/");
							list.add(fixUrl(urlString));
						}
						jarFile.close();
					} else {// is file system in class path
						//String entryName = PlatformHelper.isWindows() ? StringHelper.pathBuild(filePath, "/")
						//		.replaceAll("^/|/$", "") : StringHelper.pathBuild(filePath, "/").replaceAll("/$", "");
						list.addAll(loadResourcesInFileSystem(url.toString(), fileExt, include, exclude));
					}
				} catch (Exception e) {
					Logger.warn(FCS.get("[loadResourcesInClassPath][{file}]: loading resource error! ", filePath), e);
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	/**
	 * load resource in file system(&#42;=*).
	 * @param resPath resource path, example: META-INF/hello/
	 * @param fileExt resource file extension, example: test.properties or .properties
	 * @param include the wild card to match the file, example: META-INF/&#42;/test.properties
	 * @param exclude the wild card to match the file exclude, example: META-INF/&#42;_test.properties
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-8
	 */
	public static List<String> loadResourcesInFileSystem(final String resPath, final String fileExt,
			final String include, final String exclude) {
		Set<String> list = new LinkedHashSet<String>(64);
		java.util.LinkedList<File> linked = new java.util.LinkedList<File>();
		try {
			String urlPath = fixUrl(resPath.startsWith("file:") ? resPath : ("file://" + resPath));
			linked.add(new File(new URL(urlPath).toURI()));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				list.add(entryName.startsWith("/") ? ("file://" + entryName) : ("file:///" + entryName));
			}
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	public static List<String> loadResourcesInFileSystemJar(final String resPath, final String fileExt,
			final String include, final String exclude) {
		Set<String> list = new LinkedHashSet<String>(64);
		try {
			String preDir = "";
			{
				int index = resPath.indexOf("!/");
				if (index > -1) {
					preDir = resPath.substring(index + 2, resPath.length());
				}
			}
			File file = null;
			{
				String realPath = resPath;
				if (resPath.startsWith("jar:")) {
					realPath = resPath.substring(4);
				}
				if (realPath.indexOf("!/") > -1) {
					realPath = realPath.substring(0, realPath.indexOf("!/"));
				}
				if (realPath.startsWith("file:///")) {
					file = new File(new URL(realPath).toURI());
				} else {
					file = new File(realPath);
				}
			}
			java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					continue;
				}
				String entryName = entry.getName();
				if (preDir.length() > 0 && !entryName.startsWith(preDir)) {
					continue;
				}
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
				String urlString = resPath.substring(0, resPath.lastIndexOf("!/") + 2)
						+ entryName.replaceAll("\\\\", "/");
				list.add(fixUrl(urlString));
			}
			jarFile.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		result.addAll(list);
		return result;
	}

	/**
	 * test the text match the wild char "*"
	 * @param text    example: helloworld
	 * @param pattern example: hell*world
	 * @return 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-8
	 */
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

	public static String fixUrl(String urlString) {
		return StringHelper.fixUrl(urlString);
	}

	public static byte[] getByte(String url) {
		try {
			return SocketHelper.getByte(new URL(url).openStream(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getText(String url) {
		try {
			return SocketHelper.getContent(new URL(url).openStream(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//	public static void main(String[] args) {
	//		//System.out.println(loadResources("jar://META-INF/tc-framework-test", ".groovy", "*", "*/TCMain.groovy"));
	//		//System.out.println(loadResources("classpath://META-INF", ".groovy", "*", "*/TCMain.groovy"));
	//		//System.out.println(loadResources("file://media/新加卷/workspace/JeeGalileo/tc-util-project2", ".xml", "", ""));
	//		System.out.println(loadResourcesInFileSystemJar(
	//				"jar:file:///media/新加卷/workspace/JeeGalileo/tc-util-project2/builds/tc-util-project-1.0.1.jar!/org/iff/infra/test",
	//				"*", "*", null));
	//	}
}
