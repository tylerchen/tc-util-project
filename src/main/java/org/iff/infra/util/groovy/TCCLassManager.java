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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			TCCLassManager.me().compile(System.getProperty("app_root") + "/system/framework/TC.groovy");
			Class clazz = TCCLassManager.me().get().loadClass("org.iff.groovy.framework.TCStarter");
			GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
			groovyObject.invokeMethod("start", new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
