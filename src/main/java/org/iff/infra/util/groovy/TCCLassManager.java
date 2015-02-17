/*******************************************************************************
 * Copyright (c) 2014-12-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import groovy.lang.GroovyObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-12-26
 */
public class TCCLassManager {
	private static TCCLassManager me = new TCCLassManager();
	private List<TCMainClassLoader[]> loaders = new ArrayList<TCMainClassLoader[]>();
	private Map<String, TCMainClassLoader[]> classMapLoader = new HashMap<String, TCMainClassLoader[]>();
	private Map<String, String> classNameMapScript = new HashMap<String, String>();
	private Map<String, TCMainClassLoader> scriptMapClassLoader = new HashMap<String, TCMainClassLoader>();

	private TCCLassManager() {
	}

	public static TCCLassManager me() {
		return me;
	}

	public static TCMainClassLoader get() {
		TCMainClassLoader loader = TCGroovyClassLoader.get();
		TCCLassManager.me().register(loader);
		return loader;
	}

	public Class loadClass(String name) throws Exception {
		if (!loaders.isEmpty()) {
			return loaders.get(0)[0].loadClass(name);
		}
		return null;
	}

	public void register(TCMainClassLoader loader) {
		loaders.add(new TCMainClassLoader[] { loader });
	}

	public void addClassNameScriptMapping(TCMainClassLoader loader, String scriptFile) {
		synchronized (classNameMapScript) {
			Class[] classes = loader.getLoadedClasses();
			for (Class clazz : classes) {
				classNameMapScript.put(clazz.getName(), scriptFile);
				classMapLoader.put(clazz.getName(), new TCMainClassLoader[] { loader });
			}
		}
		synchronized (scriptMapClassLoader) {
			scriptMapClassLoader.put(scriptFile, loader);
		}
	}

	public void removeClassNameScriptMapping(String scriptFile) {
		synchronized (classNameMapScript) {
			Iterator<Entry<String, String>> iterator = classNameMapScript.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if (entry.getValue().equals(scriptFile)) {
					iterator.remove();
				}
			}
		}
		TCMainClassLoader loader = scriptMapClassLoader.get(scriptFile);
		if (loader == null) {
			return;
		}
		synchronized (scriptMapClassLoader) {
			scriptMapClassLoader.remove(scriptFile);
		}
		synchronized (loaders) {
			for (int i = 0; i < loaders.size(); i++) {
				TCMainClassLoader[] cl = loaders.get(i);
				if (cl[0] == loader) {
					synchronized (classMapLoader) {
						Iterator<Entry<String, TCMainClassLoader[]>> iterator = classMapLoader.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, TCMainClassLoader[]> entry = iterator.next();
							if (entry.getValue() == cl) {
								iterator.remove();
							}
						}
					}
					loaders.remove(i);
					break;
				}
			}
		}
	}

	public TCMainClassLoader compile(String file) {
		if (scriptMapClassLoader.get(file) != null) {
			TCMainClassLoader loader = scriptMapClassLoader.get(file);
			loader.recompile();
			return loader;
		}
		TCMainClassLoader loader = TCGroovyClassLoader.get(file);
		this.register(loader);
		loader.recompile();
		if (loader.isLastCompileSuccess()) {
			Class[] classes = loader.getLoadedClasses();
			if (classes == null || classes.length < 1) {
				return loader;
			}
			this.addClassNameScriptMapping(loader, file);
		}
		return loader;
	}

	//

	public List<TCMainClassLoader[]> getLoaders() {
		return loaders;
	}

	public Map<String, TCMainClassLoader[]> getClassMapLoader() {
		return classMapLoader;
	}

	public Map<String, String> getClassNameMapScript() {
		return classNameMapScript;
	}

	public Map<String, TCMainClassLoader> getScriptMapClassLoader() {
		return scriptMapClassLoader;
	}

	public static void main(String[] args) {
		try {
			String tc_groovy_file = null;
			String app_root = System.getProperty("app_root");
			String tc_file_path = System.getProperties().containsKey("tc_file_path") ? System
					.getProperty("tc_file_path") : app_root;
			String tc_jar_path = System.getProperty("tc_jar_path");
			String startClassName = System.getProperty("tc_groovy_framework_start_class");
			Logger.debug(FCS
					.get("app_root:{0}, tc_file_path:{1}, tc_jar_path:{2}", app_root, tc_file_path, tc_jar_path));
			if (tc_groovy_file == null && tc_file_path != null && tc_file_path.length() > 1) {
				tc_groovy_file = scan_tc_groovy_file(scan_groovy_files_from_filesys(app_root, tc_file_path, Arrays
						.asList("app", "system")));
			}
			if (tc_groovy_file == null && tc_jar_path != null && tc_jar_path.length() > 1) {
				tc_groovy_file = scan_tc_groovy_file(scan_groovy_files_from_jar(app_root, tc_jar_path));
			}
			Logger.debug(FCS.get("tc_groovy_file:{0},tc_groovy_framework_start_class:{1}", tc_groovy_file,
					startClassName));
			TCCLassManager.me().compile(tc_groovy_file);
			startClassName = startClassName == null || (startClassName = startClassName.trim()).length() < 1 ? "org.iff.groovy.framework.TCStarter"
					: startClassName;
			Class clazz = TCCLassManager.me().get().loadClass(startClassName);
			GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
			groovyObject.invokeMethod("start", new Object[0]);
		} catch (Exception e) {
			Logger.debug("Start TC Framework Error!!!", e);
		}
	}

	public static String scan_tc_groovy_file(Map map) {
		String tc_groovy_file = null;
		Object o = map.get("system/framework");
		if (o == null) {
		} else if (o instanceof List) {
			for (Object l : (List) o) {
				if (l.toString().endsWith("/TC.groovy")) {
					tc_groovy_file = l.toString();
					break;
				}
			}
		} else if (o instanceof Object[]) {
			for (Object l : (Object[]) o) {
				if (l.toString().endsWith("/TC.groovy")) {
					tc_groovy_file = l.toString();
					break;
				}
			}
		}
		return tc_groovy_file;
	}

	public static Map scan_groovy_files_from_jar(String app_root, String path) {
		List<String> list = ResourceHelper.loadResourcesInClassPath(path, ".groovy", "*", null);
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		String cleanPath = StringHelper.pathBuild(path + "/", "/");
		for (String s : list) {
			String dir = "";
			if (s.startsWith("jar:")) {
				dir = s.substring(s.indexOf(cleanPath, s.lastIndexOf("!/")) + cleanPath.length(), s.lastIndexOf('/'));
			} else {
				dir = s.substring(s.indexOf(cleanPath) + cleanPath.length(), s.lastIndexOf('/'));
			}
			List<String> files = map.get(dir);
			if (files == null) {
				files = new ArrayList<String>();
				map.put(dir, files);
			}
			files.add(s);
		}
		return map;
	}

	public static Map scan_groovy_files_from_filesys(String app_root, String dir_root, List<?> subdirs) {
		List<String> list = ResourceHelper.loadResourcesInFileSystem(dir_root, ".groovy", "*", null);
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		String cleanPath = StringHelper.pathBuild(dir_root + "/", "/");
		for (String s : list) {
			String dir = "";
			if (s.startsWith("jar:")) {
				dir = s.substring(s.indexOf(cleanPath, s.lastIndexOf("!/")) + cleanPath.length(), s.lastIndexOf('/'));
			} else {
				dir = s.substring(s.indexOf(cleanPath) + cleanPath.length(), s.lastIndexOf('/'));
			}
			List<String> files = map.get(dir);
			if (files == null) {
				files = new ArrayList<String>();
				map.put(dir, files);
			}
			files.add(s);
		}
		return map;
	}
}
