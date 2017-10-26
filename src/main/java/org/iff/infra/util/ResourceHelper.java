/*******************************************************************************
 * Copyright (c) 2015-2-11 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.jarurl.*;

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
					//file:/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v4/target/foss-qdp-project-v4-4.0.0.war!/WEB-INF/classes!/META-INF/i18n
					//file:/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v4/target/foss-qdp-project-v4-4.0.0.jar!/META-INF/i18n
					boolean isWar = StringUtils.contains(filePath, ".war!/");
					boolean isJar = StringUtils.contains(filePath, ".jar!/");
					if (isJar || isWar) {
						filePath = StringUtils.contains(filePath, ":") ? StringUtils.substringAfter(filePath, ":")
								: filePath;
						filePath = StringUtils.substringBefore(filePath, "!/");
						File file = new File(filePath);
						JarFile jarFile = new JarFile(file);
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
					//file:/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v4/target/foss-qdp-project-v4-4.0.0.war!/WEB-INF/classes!/META-INF/i18n
					//file:/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v4/target/foss-qdp-project-v4-4.0.0.jar!/META-INF/i18n
					boolean isWar = StringUtils.contains(filePath, ".war!/");
					boolean isJar = StringUtils.contains(filePath, ".jar!/");
					if (isJar || isWar) {
						filePath = StringUtils.contains(filePath, ":") ? StringUtils.substringAfter(filePath, ":")
								: filePath;
						filePath = StringUtils.substringBefore(filePath, "!/");
						String preDir = "";
						{
							//: WEB-INF/classes!/META-INF/i18n
							//: WEB-INF/classes/META-INF/i18n
							preDir = url.getFile();
							preDir = StringUtils.contains(preDir, "!/") ? StringUtils.substringAfter(preDir, "!/")
									: filePath;
							preDir = StringUtils.replace(preDir, "!/", "/");
						}
						File file = new File(filePath);
						JarFile jarFile = new JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if (entry.isDirectory()) {
								continue;
							}
							String entryName = entry.getName();
							if (!entryName.startsWith(preDir)) {
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

	public static void main(String[] args) throws MalformedURLException, IOException {
		String file = "jar:file:///Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v4/target/foss-qdp-project-v4-4.0.0.war!/WEB-INF/lib/tc-util-project-1.0.19.jar!/qdp-designer/index.html";
		System.out.println(StreamHelper.getContent(openUrlStream(file), false));
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
				//: WEB-INF/classes!/META-INF/i18n
				//: WEB-INF/classes/META-INF/i18n
				preDir = StringUtils.contains(resPath, "!/") ? StringUtils.substringAfter(resPath, "!/") : resPath;
				preDir = StringUtils.replace(preDir, "!/", "/");
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
			JarFile jarFile = new JarFile(file);
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
		return StringHelper.wildCardMatch(text, pattern);
	}

	/**
	 * fix url.
	 * @param urlString
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String fixUrl(String urlString) {
		return StringHelper.fixUrl(urlString);
	}

	/**
	 * get bytes from url.
	 * @param url
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static byte[] getByte(String url) {
		return StreamHelper.getByte(openUrlStream(url), false);
	}

	/**
	 * get the text from url.
	 * @param url    example: helloworld
	 * @return 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-8
	 */
	public static String getText(String url) {
		return StreamHelper.getContent(openUrlStream(url), false);
	}

	/**
	 * get the input stream from url.
	 * @param url    example: helloworld
	 * @return 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-8
	 */
	public static InputStream openUrlStream(String url) {
		try {
			return url(url).openStream();
		} catch (Exception e) {
			Logger.warn("Open URL stream: " + url);
		}
		return null;
	}

	/**
	 * 考虑到 fat jar （JAR、WAR中包含JAR）的情况，需要返回不同的URL。
	 * @param url
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Oct 25, 2017
	 */
	public static URL url(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		try {
			URL u = new URL(url);
			boolean hasMore = url.indexOf("!/") != url.lastIndexOf("!/");
			if (hasMore && "jar".equalsIgnoreCase(u.getProtocol())) {
				return new URL(u.getProtocol(), u.getHost(), u.getPort(), u.getFile(), new Handler());
			}
			return u;
		} catch (Exception e) {
			Logger.warn("Create URL error: " + url);
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
