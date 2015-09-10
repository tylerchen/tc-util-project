/*******************************************************************************
 * Copyright (c) Aug 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.iff.infra.util.EventBusHelper;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 2, 2015
 */
public class TCModuleManager {
	/** read write lock to prevent reading when writing data. **/
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private static TCModuleManager me;

	private Map<String, TCModule> moduleManager = new LinkedHashMap<String, TCModule>();

	private String basePath;

	public synchronized static TCModuleManager create(String basePath) {
		if (me == null) {
			me = new TCModuleManager();
			me.basePath = basePath;
		}
		return me;
	}

	public static TCModuleManager me() {
		return me;
	}

	public TCModule get(String name) {
		return moduleManager.get(name);
	}

	public TCModuleManager add(String name, TCModule module) {
		try {
			lock.writeLock().lock();
			if (moduleManager.containsKey(name)) {
				Logger.warn(FCS.get("Module name:{0}, is exists!", name));
			}
			moduleManager.put(name, module);
		} finally {
			lock.writeLock().unlock();
		}
		return this;
	}

	public String[] getModules() {
		try {
			lock.readLock().lock();
			return moduleManager.keySet().toArray(new String[0]);
		} finally {
			lock.readLock().unlock();
		}
	}

	public TCModuleManager load() {
		try {
			lock.writeLock().lock();
			File file = new File(basePath);
			{// load commond module
				File f = new File(file, "commonModule");
				String name = "commonModule";
				if (f.exists() && f.isDirectory()) {
					TCModule module = TCModule.create(
							name,
							StringHelper.concat("file://", basePath.startsWith("/") ? "" : "/", basePath,
									basePath.endsWith("/") ? "" : "/", name));
					module.load();
					add(name, module);
				} else {
					f = new File(file, "commonModule.jar");
					if (f.exists() && f.isFile()) {
						TCModule module = TCModule.create(name, StringHelper.concat("jar:file://",
								basePath.startsWith("/") ? "" : "/", basePath, basePath.endsWith("/") ? "" : "/", name,
								".jar!/"));
						module.load();
						add(name, module);
					} else {
						TCModule module = TCModule.create(
								name,
								StringHelper.concat("file://", basePath.startsWith("/") ? "" : "/", basePath,
										basePath.endsWith("/") ? "" : "/", name));
						module.load();
						add(name, module);
					}
				}
			}
			for (File f : file.listFiles()) {
				String name = f.getName();
				boolean isCommonModule = "commonModule".equals(name) || "commonModule.jar".equals(name);
				if (isCommonModule) {
					continue;
				}
				if (f.isDirectory()) {
					TCModule module = TCModule.create(
							name,
							StringHelper.concat("file://", basePath.startsWith("/") ? "" : "/", basePath,
									basePath.endsWith("/") ? "" : "/", name));
					module.load();
					add(name, module);
				} else if (f.isFile() && name.endsWith(".jar")) {
					name = name.substring(0, name.length() - 4);
					TCModule module = TCModule.create(
							name,
							StringHelper.concat("jar:file://", basePath.startsWith("/") ? "" : "/", basePath,
									basePath.endsWith("/") ? "" : "/", name, ".jar!/"));
					module.load();
					add(name, module);
				}
			}
			{//start listener
				TCModuleReload.start(basePath);
			}
		} finally {
			lock.writeLock().unlock();
		}
		return this;
	}

	public TCModuleManager reload() {
		try {
			lock.writeLock().lock();
			for (TCModule module : moduleManager.values()) {
				module.release();
			}
			moduleManager.clear();
			load();
		} finally {
			lock.writeLock().unlock();
		}
		return this;
	}

	public TCModuleManager reload(String name) {
		try {
			lock.writeLock().lock();
			TCModule module = moduleManager.get(name);
			if (module != null) {
				module.reload();
			}
		} finally {
			lock.writeLock().unlock();
		}
		return this;
	}

	public TCModule getCommonModule() {
		try {
			lock.readLock().lock();
			for (Entry<String, TCModule> entry : moduleManager.entrySet()) {
				if (entry.getValue().isCommonModule()) {
					return entry.getValue();
				}
			}
		} finally {
			lock.readLock().unlock();
		}
		return null;
	}

	public String getBasePath() {
		return basePath;
	}

	public static class TCModuleReload implements Runnable {
		private Map<String, Long> fileTimestamps = new HashMap<String, Long>();
		private String basePath;

		public static TCModuleReload start(String basePath) {
			TCModuleReload mr = new TCModuleReload();
			mr.basePath = basePath;
			new Thread(mr).start();
			return mr;
		}

		public void run() {
			while (true) {
				try {
					Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File(basePath),
							TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
					for (File file : files) {
						String absolutePath = file.getAbsolutePath();
						long lastModified = file.lastModified();
						if (fileTimestamps.get(absolutePath) == null) {
							fileTimestamps.put(absolutePath, lastModified);
						} else if (fileTimestamps.get(absolutePath).longValue() != lastModified) {
							fileTimestamps.put(absolutePath, lastModified);
							String substring = absolutePath.substring(basePath.length());
							substring = substring.startsWith("/") ? substring.substring(1) : substring;
							String moduleName = substring.substring(0, substring.indexOf('/'));
							Set<String> resourceFolders = new HashSet<String>();
							if (absolutePath.endsWith(".groovy") || absolutePath.endsWith(".jar")
									|| absolutePath.endsWith(".class")) {
								System.out.println("-------moduleName-------->" + moduleName);
								TCModuleManager.me().reload(moduleName);
								String resourcePath = substring.substring(substring.indexOf("/META-INF/")
										+ "/META-INF/".length());
								resourceFolders.add(getFolder(resourcePath));
							} else if (absolutePath.indexOf("/META-INF/resource/") > -1) {
								String resourcePath = substring.substring(substring.indexOf("/META-INF/resource/")
										+ "/META-INF/resource".length());
								System.out.println("-------resourcePath-------->" + resourcePath);
								TCModuleManager.me().get(moduleName).reloadResource(resourcePath);
								resourceFolders.add(getFolder(resourcePath));
							} else if (absolutePath.indexOf("/META-INF/I18N/") > -1) {
								String resourcePath = substring.substring(substring.indexOf("/META-INF/I18N/")
										+ "/META-INF/I18N".length());
								System.out.println("-------I18N-------->" + resourcePath);
								TCModuleManager.me().get(moduleName).reloadResource(resourcePath);
								resourceFolders.add(getFolder(resourcePath));
							}
							for (String path : resourceFolders) {
								EventBusHelper.me().asyncEvent(path, "CHANGE");
							}
						}
					}
					TimeUnit.SECONDS.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private String getFolder(String resourcePath) {
			return StringHelper.trim(StringHelper.cutOff("/" + resourcePath, "/"), "/");
		}
	}

	public static void main(String[] args) {
		TCModuleManager t = TCModuleManager.create("/home/tyler/Desktop/work-temp/test");
		t.load();
		//System.out.println(t.get("test-plugin").getActions());
		//System.out.println(t.get("test-plugin").getBeans());
		System.out.println(t.get("test-plugin").getAction("/test/hello/hello/aaa=bbb/c=d/e=f", new HashMap()));
		String resource = t.get("test-plugin").getResource("js/jquery-1.8.2.min.js");
		System.out.println(resource);
		t.get("test3").reload();
		System.out.println(t.get("test3").getResourceText("css/browser/browser.css"));
	}

	public static void main1(String[] args) {
		TCModuleManager t = TCModuleManager.create("/home/tyler/Desktop/work-temp/test");
		t.load();
		System.out.println(t.get("test2").getResource("log4j.properties"));
		//System.out.println(t.get("test2").getResource("log4j.properties"));
		System.out.println(t.get("test2").getClassLoader().getLoadedResources());
		try {
			Object create = t.get("test2").getFactory().create(t.get("test2").getClassLoader(), "org.iff.infra.Hello");
			System.out.println(create.getClass().getName());
			System.out.println(create.getClass().getMethod("sayHello", new Class<?>[0]).invoke(create, new Object[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
