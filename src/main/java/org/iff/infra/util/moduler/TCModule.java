/*******************************************************************************
 * Copyright (c) Aug 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.iff.infra.util.Assert;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.XmlHelper;
import org.iff.infra.util.groovy.TCAction;
import org.iff.infra.util.groovy.TCBean;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.ProxyClassLoader;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 2, 2015
 */
public class TCModule {
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private String name;
	private String basePath;
	private boolean commonModule = false;
	private JarClassLoader classLoader = new JarClassLoader();
	private TCGroovyProxyClassLoader groovyLoader = null;
	private JclObjectFactory factory = JclObjectFactory.getInstance();
	private Map<String, Map> resourceMap = new HashMap<String, Map>();
	private Map<String, Map> groovyMap = new HashMap<String, Map>();
	private Map<String, Map> beans = new HashMap<String, Map>();
	private Map<String, Map> actions = new HashMap<String, Map>();
	private Map<String, Map> moduleConfig = new LinkedHashMap<String, Map>();

	public static TCModule create(String name, String basePath) {
		{
			Assert.notBlank("TCModule name is required!");
			Assert.notBlank("TCModule base path is required!");
		}
		TCModule m = new TCModule();
		{
			m.setName(name);
			m.setBasePath(basePath);
			if (!"commonModule".equals(name)) {
				m.getClassLoader().addLoader(new TCCommonProxyClassLoader());
			} else {
				m.commonModule = true;
			}
		}
		return m;
	}

	public void release() {
		try {
			lock.writeLock().lock();
			System.out.println("On release........");
			resourceMap.clear();
			groovyMap.clear();
			beans.clear();
			actions.clear();
			classLoader = new JarClassLoader();
			if (groovyLoader != null) {
				SocketHelper.closeWithoutError(groovyLoader.getClassLoader());
			}
			groovyLoader = null;
			moduleConfig.clear();
			System.out.println("Release Finished.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void load() {
		try {
			lock.writeLock().lock();
			System.out.println("----------->" + basePath);
			try {
				if (basePath.startsWith("jar:")) {//jar:file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
					{// load module xml
						List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(basePath + "META-INF/",
								"tcmodule.xml", "tcmodule.xml", null);
						for (String resource : resources) {
							XmlHelper.parseXmlToMap(moduleConfig, resource);
						}
					}
					{// load jar lib
						List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(
								basePath + "META-INF/lib/", ".jar", "*", null);
						for (String s : resources) {
							System.out.println("Load jar lib:" + s);
							classLoader.add(new URL(s).openStream());
						}
					}
					{// load classes
						classLoader.add(basePath.substring("jar:file://".length(), basePath.length() - 2));
					}
					{// load resource
						String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
								"META-INF/resource/");
						List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(basePath
								+ "META-INF/resource/", "*", "*", null);
						for (String url : resources) {
							if (url.startsWith(resDir)) {
								resourceMap.put(url.substring(resDir.length()), MapHelper.toMap("url", url));
							}
						}
					}
					{// load groovy
						String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
								"META-INF/groovy/");
						List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(basePath
								+ "META-INF/groovy/", ".groovy", "*", null);
						for (String url : resources) {
							if (url.startsWith(resDir)) {
								groovyMap.put(url.substring(resDir.length()), MapHelper.toMap("url", url));
							}
						}
					}
				} else if (basePath.startsWith("file:")) {
					{// load module xml
						String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/", "META-INF/");
						List<String> resources = ResourceHelper.loadResources(resDir, "tcmodule.xml", "tcmodule.xml",
								null);
						for (String resource : resources) {
							XmlHelper.parseXmlToMap(moduleConfig, resource);
						}
					}
					{// load jar lib
						List<String> resources = ResourceHelper.loadResources(basePath, ".jar", "*", null);
						for (String s : resources) {
							classLoader.add(new URL(s).openStream());
						}
					}
					{// load classes
						classLoader.add(new URL(basePath));
					}
					{// load resources
						String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
								"META-INF/resource/");
						List<String> resources = ResourceHelper.loadResources(resDir, "*", "*", null);
						for (String url : resources) {
							if (url.startsWith(resDir)) {
								resourceMap.put(url.substring(resDir.length()), MapHelper.toMap("url", url));
							}
						}
					}
					{// load groovy
						String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
								"META-INF/groovy/");
						List<String> resources = ResourceHelper.loadResources(resDir, ".groovy", "*", null);
						for (String url : resources) {
							if (url.startsWith(resDir)) {
								groovyMap.put(url, MapHelper.toMap("url", url));
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			initGroovy();
			initBeans();
			initActions();
		} finally {
			lock.writeLock().unlock();
		}
	}

	protected void initGroovy() {
		try {
			if (groovyMap.isEmpty()) {
				return;
			}
			groovyLoader = new TCGroovyProxyClassLoader();
			classLoader.addLoader(groovyLoader);
			Map<String, List<String>> pathUrlMap = new HashMap<String, List<String>>();
			String prefix = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/", "META-INF/groovy/");
			for (String url : groovyMap.keySet()) {
				String dir = url.substring(prefix.length());
				dir = dir.substring(0, dir.lastIndexOf("/"));
				List<String> list = pathUrlMap.get(dir);
				if (list == null) {
					list = new ArrayList<String>();
					pathUrlMap.put(dir, list);
				}
				list.add(url);
			}
			String[] order = new String[] { "common", "module", "server", "view" };
			List<String> orderList = new ArrayList<String>();
			for (String o : order) {
				orderList.clear();
				for (String dir : pathUrlMap.keySet()) {
					if (dir.equals(o) || dir.startsWith(o + "/")) {
						orderList.add(dir);
					}
				}
				Collections.sort(orderList);
				List<String> parseFail = new ArrayList<String>();
				for (String dir : orderList) {
					List<String> list = pathUrlMap.get(dir);
					for (String groovy : list) {
						try {
							String text = ResourceHelper.getText(groovy);
							groovyLoader.getClassLoader().parseClass(text);
						} catch (Exception e) {
							parseFail.add(groovy);
						}
					}
					for (int i = 0, j = parseFail.size(); i < j; i++) {
						String[] reparse = parseFail.toArray(new String[parseFail.size()]);
						parseFail.clear();
						for (String groovy : reparse) {
							try {
								String text = ResourceHelper.getText(groovy);
								groovyLoader.getClassLoader().parseClass(text);
							} catch (Exception e) {
								parseFail.add(groovy);
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initBeans() {
		try {
			if (groovyLoader != null) {
				Class[] loadedClasses = groovyLoader.getClassLoader().getLoadedClasses();
				for (Class<?> clazz : loadedClasses) {
					TCBean annotation = clazz.getAnnotation(TCBean.class);
					if (annotation == null) {
						continue;
					}
					//[clazz: cls, class_name: name, name: anno_name, order: anno_order, url:events.file_struct.url, instance: null]
					beans.put(
							annotation.name(),
							MapHelper.toMap("className", clazz.getName(), "name", annotation.name(), "type",
									annotation.type(), "order", annotation.order(), "instance", null));
					//System.out.println(beans.get(annotation.name()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Map<String, byte[]> resources = classLoader.getLoadedResources();
			for (String key : resources.keySet()) {
				if (!key.endsWith(".class")) {
					continue;
				}
				//System.out.println(key.substring(0, key.length() - 6).replaceAll("/", "."));
				String className = key.substring(0, key.length() - 6).replaceAll("/", ".");
				Class<?> clazz = classLoader.loadClass(className, false);
				TCBean annotation = clazz.getAnnotation(TCBean.class);
				if (annotation == null) {
					continue;
				}
				//[clazz: cls, class_name: name, name: anno_name, order: anno_order, url:events.file_struct.url, instance: null]
				beans.put(annotation.name(), MapHelper.toMap("className", className, "name", annotation.name(), "type",
						annotation.type(), "order", annotation.order(), "instance", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void initActions() {
		try {
			if (groovyLoader != null) {
				Class[] loadedClasses = groovyLoader.getClassLoader().getLoadedClasses();
				for (Class<?> clazz : loadedClasses) {
					TCAction annotation = clazz.getAnnotation(TCAction.class);
					if (annotation == null) {
						continue;
					}
					//[clazz: cls, 'action':name,'method':method_name,'context':context]
					Map<String, Object> url = new HashMap<String, Object>();
					for (Method method : clazz.getDeclaredMethods()) {
						Map<?, ?> exclude = MapHelper.toMap("invokeMethod", true, "getMetaClass", true, "setMetaClass",
								true, "setProperty", true, "getProperty", true);
						String name = method.getName();
						int modifiers = method.getModifiers();
						if (name.indexOf("$") > -1) {
							continue;
						}
						if (exclude.containsKey(name)) {
							continue;
						}
						if (Modifier.isStatic(modifiers)) {
							continue;
						}
						if (!Modifier.isPublic(modifiers)) {
							continue;
						}
						url.put(name, method);
					}
					url.put("@className", clazz.getName());
					{
						if (GroovyObject.class.isAssignableFrom(clazz)) {
							GroovyShell shell = new GroovyShell();
							shell.setVariable("cls", clazz);
							shell.evaluate(actionMethod);
						}
					}
					actions.put(annotation.name(), url);
					//System.out.println(actions.get(annotation.name()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Map<String, byte[]> resources = classLoader.getLoadedResources();
			for (String key : resources.keySet()) {
				if (!key.endsWith(".class")) {
					continue;
				}
				//System.out.println(key.substring(0, key.length() - 6).replaceAll("/", "."));
				String className = key.substring(0, key.length() - 6).replaceAll("/", ".");
				Class<?> clazz = classLoader.loadClass(className, false);
				TCAction annotation = clazz.getAnnotation(TCAction.class);
				if (annotation == null) {
					continue;
				}
				//[clazz: cls, 'action':name,'method':method_name,'context':context]
				Map<String, Object> url = new HashMap<String, Object>();
				for (Method method : clazz.getDeclaredMethods()) {
					Map<?, ?> exclude = MapHelper.toMap("invokeMethod", true, "getMetaClass", true, "setMetaClass",
							true, "setProperty", true, "getProperty", true);
					String name = method.getName();
					int modifiers = method.getModifiers();
					if (name.indexOf("$") > -1) {
						continue;
					}
					if (exclude.containsKey(name)) {
						continue;
					}
					if (Modifier.isStatic(modifiers)) {
						continue;
					}
					if (!Modifier.isPublic(modifiers)) {
						continue;
					}
					url.put(name, method);
				}
				url.put("@className", className);
				actions.put(annotation.name(), url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		try {
			lock.writeLock().lock();
			System.out.println("On Reload........");
			resourceMap.clear();
			groovyMap.clear();
			beans.clear();
			actions.clear();
			classLoader = new JarClassLoader();
			if (groovyLoader != null) {
				SocketHelper.closeWithoutError(groovyLoader.getClassLoader());
			}
			groovyLoader = null;
			moduleConfig.clear();
			load();
			System.out.println("Reload Finished.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public TCModule reloadResource(String resourcePath) {
		try {
			lock.writeLock().lock();
			Map map = resourceMap.get(resourcePath);
			if (map != null) {
				map.put("text", null);
				map.put("byte", null);
			} else if (!isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					module.reload();
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
		return this;
	}

	public String getResource(String resourcePath) {
		try {
			lock.readLock().lock();
			String resource = (String) resourceMap.get(resourcePath).get("url");
			if (resource == null && !isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					resource = module.getResource(resourcePath);
				}
			}
			return resource;
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getResourceText(String resourcePath) {
		try {
			lock.writeLock().lock();
			Map map = resourceMap.get(resourcePath);
			String text = null;
			if (map != null) {
				text = (String) map.get("text");
				if (text == null) {
					text = ResourceHelper.getText((String) map.get("url"));
					map.put("text", text);
				}
			} else if (!isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					text = module.getResourceText(resourcePath);
				}
			}
			return text;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public byte[] getResourceByte(String resourcePath) {
		try {
			lock.writeLock().lock();
			Map map = resourceMap.get(resourcePath);
			byte[] bs = null;
			if (map != null) {
				bs = (byte[]) map.get("byte");
				if (bs == null) {
					bs = ResourceHelper.getByte((String) map.get("url"));
					map.put("byte", bs);
				}
			} else if (!isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					bs = module.getResourceByte(resourcePath);
				}
			}
			return bs;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public <T> T getBean(String name) {
		try {
			lock.writeLock().lock();
			Map map = beans.get(name);
			Object instance = null;
			if (map != null) {
				instance = map.get("instance");
				if (instance == null) {
					instance = factory.create(classLoader, (String) map.get("className"));
					map.put("instance", instance);
				}
			} else if (!isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					instance = module.getBean(name);
				}
			}
			return (T) instance;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public boolean containsActionContext(String context) {
		try {
			lock.writeLock().lock();
			boolean contains = actions.containsKey(context);
			if (!contains) {
				if (!isCommonModule()) {
					TCModule module = TCModuleManager.me().getCommonModule();
					if (module != null) {
						contains = module.containsActionContext(context);
					}
				}
			}
			return contains;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public TCActionInvoker getAction(String context, String methodName, Map params) {
		try {
			lock.writeLock().lock();
			Map map = actions.get(context);
			TCActionInvoker actionInvoker = null;
			if (map != null) {
				Method method = (Method) map.get(methodName);
				if (method != null) {
					actionInvoker = new TCActionInvoker(method, map.get("@className").toString(), classLoader, params);
				}
			} else if (!isCommonModule()) {
				TCModule module = TCModuleManager.me().getCommonModule();
				if (module != null) {
					actionInvoker = module.getAction(context, methodName, params);
				}
			}
			if (actionInvoker != null) {
				params.put("actionContext", context.startsWith("/") ? context.substring(1) : context);
			}
			return actionInvoker;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public TCActionInvoker getAction(String target, Map params) {
		try {
			lock.writeLock().lock();
			TCActionInvoker actionInvoker = getAction(target, "index", params);
			if (actionInvoker != null) {
				return actionInvoker;
			}
			String[] split = target.split("\\/");
			String context = target;
			for (int i = split.length - 1; i > -1; i--) {
				context = StringHelper.pathConcat(Arrays.copyOfRange(split, 0, i));
				//System.out.println(Arrays.toString(split) + ":Context: " + context);
				Map map = actions.get(context);
				if (map != null) {
					params.put("urlParams", Arrays.copyOfRange(split, i + 1, split.length));
					Method m = "@className".equals(split[i]) ? null : (Method) map.get(split[i]);
					if (m != null) {
						actionInvoker = new TCActionInvoker(m, map.get("@className").toString(), classLoader, params);
						break;
					}
				} else if (!isCommonModule()) {
					TCModule module = TCModuleManager.me().getCommonModule();
					if (module != null) {
						actionInvoker = module.getAction(target, params);
						break;
					}
				}
			}
			if (actionInvoker != null) {
				params.put("actionContext", context.startsWith("/") ? context.substring(1) : context);
			}
			return actionInvoker;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static class TCActionInvoker {
		private Method method;
		private String className;
		private ClassLoader classLoader;
		private JclObjectFactory factory;
		private Map params;

		public TCActionInvoker(Method method, String className, ClassLoader classLoader, Map params) {
			this.method = method;
			this.className = className;
			this.classLoader = classLoader;
			this.params = params;
		}

		public Object invoke(Object... params) {
			try {
				Class<?> loadClass = classLoader.loadClass(getClassName());
				Object newInstance = loadClass.newInstance();
				if (newInstance instanceof GroovyObject) {
					GroovyObject go = (GroovyObject) newInstance;
					go.setProperty("params", this.params);
					return getMethod().invoke(go, params);
				} else {
					return getMethod().invoke(newInstance, params);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public Method getMethod() {
			return method;
		}

		public String getClassName() {
			return className;
		}
	}

	public static class TCCommonProxyClassLoader extends ProxyClassLoader {

		public TCCommonProxyClassLoader() {
			setOrder(21);
		}

		public Class loadClass(String className, boolean resolveIt) {
			try {
				return TCModuleManager.me().getCommonModule().getClassLoader().loadClass(className, resolveIt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public InputStream loadResource(String name) {
			try {
				return TCModuleManager.me().getCommonModule().getClassLoader().getResourceAsStream(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public URL findResource(String name) {
			try {
				return TCModuleManager.me().getCommonModule().getClassLoader().getResource(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public static class TCGroovyProxyClassLoader extends ProxyClassLoader {

		private GroovyClassLoader classLoader = null;

		public TCGroovyProxyClassLoader() {
			classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(),
					getCompilerConfiguration());
		}

		public Class loadClass(String className, boolean resolveIt) {
			try {
				return classLoader.loadClass(className);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public InputStream loadResource(String name) {
			return classLoader.getResourceAsStream(name);
		}

		public URL findResource(String name) {
			return classLoader.findResource(name);
		}

		public GroovyClassLoader getClassLoader() {
			return classLoader;
		}

		public static CompilerConfiguration getCompilerConfiguration() {
			CompilerConfiguration config = new CompilerConfiguration();
			{
				config.setSourceEncoding("UTF-8");
				config.setRecompileGroovySource(true);
			}
			{
				ImportCustomizer customizer = new ImportCustomizer();
				customizer.addStarImports("org.iff.infra.util", "org.iff.infra.util.moduler", "org.iff.groovy.util",
						"org.iff.infra.util.groovy");
				config.addCompilationCustomizers(customizer);
			}
			return config;
		}
	}

	public boolean isCommonModule() {
		return commonModule;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getBasePath() {
		return basePath;
	}

	protected void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public JarClassLoader getClassLoader() {
		try {
			lock.writeLock().lock();
			return classLoader;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public JclObjectFactory getFactory() {
		try {
			lock.writeLock().lock();
			return factory;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Map<String, Map> getBeans() {
		try {
			lock.writeLock().lock();
			return beans;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Map<String, Map> getActions() {
		try {
			lock.writeLock().lock();
			return actions;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static final String actionMethod = ""
			+ //
			"cls.metaClass.params=[:]                                                                                           \n"
			+ "cls.metaClass._request_params=[:]                                                                                  \n"
			+ "cls.metaClass._request_userAgent=[:]                                                                               \n"
			+ "if(!cls.metaClass.hasMetaMethod('addUrlParam', [Object,Object] as Class[])){                                       \n"
			+ "	cls.metaClass.addUrlParam={key, value->                                                                         \n"
			+ "		_request_params.put(key, urlEncode(value))                                                                  \n"
			+ "		delegate                                                                                                    \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n"
			+ "if(!cls.metaClass.hasMetaMethod('urlEncode', [Object] as Class[])){                                                \n"
			+ "	cls.metaClass.urlEncode={url->                                                                                  \n"
			+ "		url ? java.net.URLEncoder.encode(url,'UTF-8') : ''                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n"
			+ "if(!cls.metaClass.hasMetaMethod('urlDecode', [Object] as Class[])){                                                \n"
			+ "	cls.metaClass.urlDecode={url->                                                                                  \n"
			+ "		url ? java.net.URLDecoder.decode(url,'UTF-8') : ''                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n"
			+ "if(!cls.metaClass.hasMetaMethod('redirect', [Object] as Class[])){                                                 \n"
			+ "	cls.metaClass.redirect={url->                                                                                   \n"
			+ "		if(url && _request_params.size()>0){                                                                        \n"
			+ "			if(url.endsWith('?')||url.endsWith('&')){                                                               \n"
			+ "				url=url+_request_params.collect{k,v-> k+'='+v}.join('&')                                            \n"
			+ "			}else if(url.indexOf('?')>-1){                                                                          \n"
			+ "				url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')                                        \n"
			+ "			}else{                                                                                                  \n"
			+ "				url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')                                        \n"
			+ "			}                                                                                                       \n"
			+ "		}                                                                                                           \n"
			+ "		params.response.sendRedirect(url ? url.toString() : '')                                                     \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n"
			+ "if(!cls.metaClass.hasMetaMethod('forward', [Object] as Class[])){                                                  \n"
			+ "	cls.metaClass.forward={url->                                                                                    \n"
			+ "		if(url && _request_params.size()>0){                                                                        \n"
			+ "			if(url.endsWith('?')||url.endsWith('&')){                                                               \n"
			+ "				url=url+_request_params.collect{k,v-> k+'='+v}.join('&')                                            \n"
			+ "			}else if(url.indexOf('?')>-1){                                                                          \n"
			+ "				url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')                                        \n"
			+ "			}else{                                                                                                  \n"
			+ "				url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')                                        \n"
			+ "			}                                                                                                       \n"
			+ "		}                                                                                                           \n"
			+ "		params.request.getRequestDispatcher(url ? url.toString() : '').forward(params.request, params.response)     \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n"
			+ "if(!cls.metaClass.hasMetaMethod('userAgent', [] as Class[])){                                                      \n"
			+ "	cls.metaClass.userAgent={                                                                                       \n"
			+ "		if(_request_userAgent.size()<1){                                                                            \n"
			+ "			_request_userAgent << org.iff.infra.util.HttpHelper.userAgent(params.request.getHeader('User-Agent'))   \n"
			+ "		}                                                                                                           \n"
			+ "		_request_userAgent                                                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                  \n";
}
