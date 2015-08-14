/*******************************************************************************
 * Copyright (c) 2014-12-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
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

import org.iff.infra.util.Assert;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.ResourceHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-12-26
 */
public class TCCLassManager {
	private static final Map<String, TCCLassManager> map = new HashMap<String, TCCLassManager>();
	private String name = "default";
	private List<TCMainClassLoader[]> loaders = new ArrayList<TCMainClassLoader[]>();
	private Map<String, TCMainClassLoader[]> classMapLoader = new HashMap<String, TCMainClassLoader[]>();
	private Map<String, String> classNameMapScript = new HashMap<String, String>();
	private Map<String, TCMainClassLoader> scriptMapClassLoader = new HashMap<String, TCMainClassLoader>();

	private TCCLassManager() {
	}

	public static TCCLassManager me() {
		return getManager("default", true);
	}

	public static TCMainClassLoader get() {
		TCMainClassLoader loader = TCGroovyClassLoader.get("default");
		TCCLassManager.me().register(loader);
		return loader;
	}

	public static TCCLassManager getManager(String name, boolean createIfNotExists) {
		Assert.notEmpty(name, "Container name can't be empty");
		TCCLassManager cm = map.get(name);
		if (cm == null && createIfNotExists) {
			synchronized (map) {
				cm = map.get(name);
				if (cm == null) {
					cm = new TCCLassManager();
					cm.name = name;
					if (!"default".equals(name)) {
						cm.loaders.addAll(me().loaders);
						cm.classMapLoader.putAll(me().classMapLoader);
						cm.classNameMapScript.putAll(me().classNameMapScript);
						cm.scriptMapClassLoader.putAll(me().scriptMapClassLoader);
					}
					map.put(name, cm);
				}
			}
		}
		return cm;
	}

	public static Map<String, TCCLassManager> getManagers() {
		Map<String, TCCLassManager> result = new HashMap<String, TCCLassManager>();
		for (Entry<String, TCCLassManager> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static TCMainClassLoader getClassLoader(String managerName) {
		TCCLassManager cm = getManager(managerName, false);
		Assert.notNull(cm, FCS.get("Container[{0}] can't be null.", managerName));
		TCMainClassLoader loader = TCGroovyClassLoader.get(managerName);
		cm.register(loader);
		return loader;
	}

	public Class loadClass(String name) throws Exception {
		if (!loaders.isEmpty()) {
			Exception e = null;
			for (int i = loaders.size() - 1; i > -1; i--) {
				try {
					Class clazz = loaders.get(i)[0].loadClass(name);
					if (clazz != null) {
						return clazz;
					}
				} catch (Exception ee) {
					e = ee;
				}
			}
			if (e != null) {
				throw e;
			}
		}
		return null;
	}

	public void register(TCMainClassLoader loader) {
		loaders.add(new TCMainClassLoader[] { loader });
	}

	public boolean containsScriptFile(String scriptFile) {
		return scriptMapClassLoader.containsKey(scriptFile);
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
		TCMainClassLoader loader = TCGroovyClassLoader.get(file, name);
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

	//new feature 15-07-15
	public TCMainClassLoader compile(Map fileStruct) {
		String file = (String) fileStruct.get("url");
		if (scriptMapClassLoader.get(file) != null) {
			TCMainClassLoader loader = scriptMapClassLoader.get(file);
			loader.recompile();
			return loader;
		}
		TCMainClassLoader loader = TCGroovyClassLoader.get(fileStruct, name);
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
			startClassName = startClassName == null || (startClassName = startClassName.trim()).length() < 1 ? "org.iff.groovy.framework.TCStarter"
					: startClassName;
			Logger.debug(FCS.get("tc_groovy_file:{0},tc_groovy_framework_start_class:{1}", tc_groovy_file,
					startClassName));
			TCCLassManager.me().compile(tc_groovy_file);
			Class clazz = TCCLassManager.me().get().loadClass(startClassName);
			GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
			groovyObject.invokeMethod("start", new Object[0]);
		} catch (Exception e) {
			Logger.debug("Start TC Framework Error!!!", e);
		}
	}

	/**
	 * find the main groovy file: system/framework/TC.groovy
	 * @param map
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-6-29
	 */
	public static String scan_tc_groovy_file(Map map) {
		String tc_groovy_file = null;
		Object o = map.get("/system/framework");
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

	/**
	 * scan groovy files from jar, and return file path and files map.
	 * @param app_root
	 * @param path
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-6-29
	 */
	public static Map scan_groovy_files_from_jar(String app_root, String path) {
		List<String> list = ResourceHelper.loadResourcesInClassPath(path, ".groovy", "*", null);
		Logger.debug(FCS.get("[scan_groovy_files_from_jar]files: {0}", list));
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		String cleanPath = path;
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

	/**
	 * scan groovy files from file system, and return the directory name and files map.
	 * @param app_root
	 * @param dir_root
	 * @param subdirs
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-6-29
	 */
	public static Map scan_groovy_files_from_filesys(String app_root, String dir_root, List<?> subdirs) {
		List<String> list = ResourceHelper.loadResourcesInFileSystem(dir_root, ".groovy", "*", null);
		Logger.debug(FCS.get("[scan_groovy_files_from_filesys]files: {0}", list));
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		String cleanPath = dir_root;
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
