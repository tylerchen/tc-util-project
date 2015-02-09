/*******************************************************************************
 * Copyright (c) 2015-2-2 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-2-2
 */
public class TCGroovyFilter implements javax.servlet.Filter {
	private String tc_groovy_framework_dir = "";
	private String target_prefix = "";
	private javax.servlet.Filter delegate = null;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		request.setAttribute("tc_groovy_target_prefix", target_prefix);
		delegate.doFilter(request, response, chain);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		{
			System.setProperty("app_mode", "embedded");
			Enumeration<String> names = filterConfig.getInitParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				if ("system_props".equals(name)) {//system_props="a=b@@@b=c" -> a:b, b:c
					String value = filterConfig.getInitParameter(name);
					value = value == null ? "" : value.trim();
					String[] split = value.split("@@@");
					for (String s : split) {
						String[] propsPaire = s.split("=");
						if (propsPaire.length == 2) {
							System.setProperty(propsPaire[0], propsPaire[1]);
						}
					}
				} else {
					System.setProperty(name, filterConfig.getInitParameter(name));
				}
			}
		}
		{
			tc_groovy_framework_dir = filterConfig.getInitParameter("tc_groovy_framework_dir");
			target_prefix = filterConfig.getInitParameter("target_prefix");
			System.out.println("---tc_groovy_framework_dir--->" + tc_groovy_framework_dir);
			if (tc_groovy_framework_dir == null || tc_groovy_framework_dir.length() < 1) {
				return;
			} else {
				tc_groovy_framework_dir = tc_groovy_framework_dir.trim().replaceAll("^/|/$", "");
			}
			Map<String, String> map = getGroovyFiles(tc_groovy_framework_dir);
			try {
				TCCLassManager.me().compile(map.get("/system/framework/TC.groovy"));
				{
					Class clazz = TCCLassManager.me().get().loadClass("org.iff.groovy.framework.Starter");
					GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
					groovyObject.invokeMethod("start", map);
				}
				{
					Class clazz = TCCLassManager.me().get().loadClass("org.iff.groovy.framework.TCFilter");
					GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
					delegate = (Filter) groovyObject;
					delegate.init(filterConfig);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	Map<String, String> getGroovyFiles(String tc_groovy_framework_dir) {
		Map<String, String> map = new TreeMap<String, String>();
		try {
			Enumeration<URL> rs = getClass().getClassLoader().getResources(tc_groovy_framework_dir);
			while (rs.hasMoreElements()) {
				URL url = rs.nextElement();
				String filePath = url.getFile();
				System.out.println("--url-->" + filePath);
				String protocol = url.getProtocol();
				if ("file".equals(protocol) || "jar".equals(protocol)) {//||"jar".equals(protocol)
					try {
						System.out.println(filePath);
						File file = new File(filePath.substring(filePath.indexOf("file:") + 5, filePath
								.lastIndexOf("!/")));
						java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String entryName = entry.getName();
							if (entryName.startsWith(tc_groovy_framework_dir) && entryName.endsWith(".groovy")) {
								entryName = entryName.substring(
										entryName.indexOf(tc_groovy_framework_dir) + tc_groovy_framework_dir.length())
										.replaceAll("\\\\", "/");
								map.put(entryName, protocol + ":"
										+ filePath.substring(0, filePath.lastIndexOf("!/") + 2)
										+ entry.getName().replaceAll("\\\\", "/"));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		System.out.println(map);
		return map;
	}

	public static void main(String[] args) {
		Path path = Paths.get("G:/app_roo");
		//path.toFile().exists()
		//		try {
		//			URL url = new URL("jar:file:/E:/workspace/JeeGalileo/jfinal-groovy-project/lib/tc-groovy-framework.jar!/META-INF/app_root/system/framework/TC.groovy");
		//			JarURLConnection conn = (JarURLConnection) url.openConnection();
		//			System.out.println(SocketHelper.getContent(conn.getInputStream(), false));
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}
}
