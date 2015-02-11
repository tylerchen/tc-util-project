/*******************************************************************************
 * Copyright (c) 2014-12-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;

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
		Binding binding = new Binding();
		binding.setVariable("app_root", app_root);
		binding.setVariable("path", path);
		GroovyShell shell = new GroovyShell(binding);
		return (Map) shell.evaluate(scan_groovy_files_from_jar);
	}

	public static Map scan_groovy_files_from_filesys(String app_root, String dir_root, List<?> subdirs) {
		Binding binding = new Binding();
		binding.setVariable("app_root", app_root);
		binding.setVariable("dir_root", dir_root);
		binding.setVariable("subdirs", subdirs);
		GroovyShell shell = new GroovyShell(binding);
		return (Map) shell.evaluate(scan_groovy_files_from_filesys);
	}

	public static final String scan_groovy_files_from_filesys = ""//
			+ "    def scan_groovy_files_from_filesys(app_root, dir_root, subdirs){\r\n"
			+ "        def files=[:]//{parentPath:[fileSet]}\r\n"
			+ "        if(!dir_root || dir_root.size()<1 || !(new File(dir_root).exists())){\r\n"
			+ "            return files\r\n"
			+ "        }\r\n"
			+ "        dir_root=pathClean(dir_root)\r\n"
			+ "        (subdirs ?: ['']).each{subdir->\r\n"
			+ "            new File(dir_root+'/'+subdir).eachFileRecurse(groovy.io.FileType.FILES){ file ->\r\n"
			+ "                if(file.path.endsWith('.groovy')){\r\n"
			+ "                    def parent=pathClean(pathClean(file.parent)-dir_root), path=pathClean(file.path)\r\n"
			+ "                    if(files[parent]){\r\n"
			+ "                        files[parent].add(path)\r\n"
			+ "                    }else{\r\n"
			+ "                        files[parent]=[path]\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "        return files\r\n"
			+ "    }\r\n"
			+ "    def pathClean(path){\r\n"
			+ "        return path ? org.iff.infra.util.StringHelper.pathBuild(path.toString(), '/').replaceAll('^/|/$','') : null\r\n"
			+ "    }\r\n" + "	return scan_groovy_files_from_filesys(app_root, dir_root, subdirs)\r\n";

	public static final String scan_groovy_files_from_jar = ""//
			+ "    def scan_groovy_files_from_jar(app_root, path){\r\n"
			+ "        def files=[:]\r\n"
			+ "        if(!path || path.size()<1){\r\n"
			+ "            return files\r\n"
			+ "        }\r\n"
			+ "        path=pathClean(path)\r\n"
			+ "        def rs=getClass().getClassLoader().getResources(path)\r\n"
			+ "        while(rs.hasMoreElements()){\r\n"
			+ "            def url=rs.nextElement()\r\n"
			+ "            def filePath=url.getFile(), protocol=url.getProtocol()\r\n"
			+ "            if (protocol in ['file','jar'] && filePath.startsWith('file:')) {// path will starts with file:/\r\n"
			+ "                def file = new File(filePath.substring('file:'.size(), filePath.lastIndexOf(\"!/\")))\r\n"
			+ "                def jarFile = new java.util.jar.JarFile(file)\r\n"
			+ "                def entries=jarFile.entries()\r\n"
			+ "                while(entries.hasMoreElements()){\r\n"
			+ "                    def entry=entries.nextElement()\r\n"
			+ "                    def entryName=pathClean(entry.name)\r\n"
			+ "                    if (entryName.startsWith(path) && entryName.endsWith('.groovy')) {\r\n"
			+ "                        def fileName=pathClean(entryName.substring(0, entryName.lastIndexOf('/'))-path)\r\n"
			+ "                        def fileSet=files[fileName]\r\n"
			+ "                        if(fileName in files){\r\n"
			+ "                            files[fileName].add(\"${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}\")\r\n"
			+ "                        }else{\r\n"
			+ "                            files[fileName]=[\"${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}\"]\r\n"
			+ "                        }\r\n"
			+ "                    }\r\n"
			+ "                }\r\n"
			+ "            }\r\n"
			+ "        }\r\n"
			+ "        return files\r\n"
			+ "    }\r\n"
			+ "    def pathClean(path){\r\n"
			+ "        return path ? org.iff.infra.util.StringHelper.pathBuild(path.toString(), '/').replaceAll('^/|/$','') : null\r\n"
			+ "    }\r\n" + "   return scan_groovy_files_from_jar(app_root, path)\r\n";
}
