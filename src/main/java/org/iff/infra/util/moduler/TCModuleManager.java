/*******************************************************************************
 * Copyright (c) Aug 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 2, 2015
 */
public class TCModuleManager {
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
		if (moduleManager.containsKey(name)) {
			Logger.warn(FCS.get("Module name:{0}, is exists!", name));
		}
		moduleManager.put(name, module);
		return this;
	}

	public String[] getModules() {
		return moduleManager.keySet().toArray(new String[0]);
	}

	public TCModuleManager load() {
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
					TCModule module = TCModule.create(
							name,
							StringHelper.concat("jar:file://", basePath.startsWith("/") ? "" : "/", basePath,
									basePath.endsWith("/") ? "" : "/", name, ".jar!/"));
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
		return this;
	}

	public TCModule reload(String name) {
		TCModule module = moduleManager.get(name);
		if (module != null) {
			module.reload();
		}
		return module;
	}

	public TCModule getCommonModule() {
		for (Entry<String, TCModule> entry : moduleManager.entrySet()) {
			if (entry.getValue().isCommonModule()) {
				return entry.getValue();
			}
		}
		return null;
	}

	public String getBasePath() {
		return basePath;
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
