/*******************************************************************************
 * Copyright (c) Aug 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy2;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.iff.infra.domain.InstanceProvider;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.XmlHelper;
import org.iff.infra.util.groovy.TCAction;
import org.iff.infra.util.groovy.TCBean;
import org.iff.infra.util.moduler.TCModuleManager;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.ProxyClassLoader;

/**
 * <pre>
 * === Usage ===
 * --- loading groovy and resources ---
 * TCGroovyLoader can load groovy and resource file from file system, jar and classpath.
 * example:
 * TCGroovyLoader.create("moduleName", new String[] {"file://modules/commonModule" });
 * TCGroovyLoader.create("moduleName", new String[] {"jar://modules/commonModule" });
 * TCGroovyLoader.create("moduleName", new String[] {"classpath://modules/commonModule" });
 * --- groovy and resources structure ---
 * -basedir/META-INF
 *  |-tcmodule.xml
 *  |-resource
 *   |-*.*
 *  |-groovy/common
 *   |-*.groovy
 *  |-groovy/module
 *   |-*.groovy
 *  |-groovy/service
 *   |-*.groovy
 *  |-groovy/view
 *   |-*.groovy
 * the TCGroovyLoader loading groovy file order: tcmodule.xml，resource, common, module, service, view.
 * if the dir has child-dir and files, TCGroovyLoader will sort files path first and load, the file in parent dir will load first.
 * -- tcmodule.xml --
 * this file currently not using.
 * keep this content in file: &lt;tcmodule&gt;&lt;/tcmodule&gt;
 * -- load and parse --
 * when using create method to create a instanceof TCGroovyLoader, then use load() to load and parse all files.
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 2, 2015
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TCGroovyLoader {
	/**
	 * this lock to keep the resource update syncronize.
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * the TCGroovyLoader name.
	 */
	private String name;
	/**
	 * the paths of the TCGroovyLoader to load the resources.
	 */
	private String[] basePaths;
	/**
	 * this jar class loader to load jar from the base paths.
	 */
	private JarClassLoader classLoader = new JarClassLoader();
	/**
	 * this is the groovy class loader.
	 */
	private TCGroovyProxyClassLoader groovyLoader = null;
	/**
	 * the jcl factory. to load the object from jcl, such as from JarClassLoader.
	 */
	private JclObjectFactory factory = JclObjectFactory.getInstance();
	/**
	 * all resource files from resources dir.
	 */
	private Map<String, Map> resourceMap = new HashMap<String, Map>();
	/**
	 * all groovy files from groovy dir.
	 */
	private Map<String, Map> groovyMap = new HashMap<String, Map>();
	/**
	 * all groovy beans.
	 */
	private Map<String, Map> beans = new HashMap<String, Map>();
	/**
	 * all groovy actions.
	 */
	private Map<String, Map> actions = new HashMap<String, Map>();
	/**
	 * tcmodule.xml data.
	 */
	private Map<String, Map> moduleConfig = new LinkedHashMap<String, Map>();

	/**
	 * create a TCGroovyLoader and load all files from base paths.
	 * @param name
	 * @param basePaths
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since May 31, 2016
	 */
	public static TCGroovyLoader create(String name, String[] basePaths) {
		{
			Assert.notBlank("TCModule name is required!");
			Assert.notBlank("TCModule base path is required!");
		}
		TCGroovyLoader m = new TCGroovyLoader();
		{
			m.setName(name);
			m.setBasePaths(basePaths);
		}
		{
			RegisterHelper.regist(InstanceProvider.class.getName(), MapHelper.toMap("name",
					TCGroovyInstanceProvider.class.getSimpleName(), "value", TCGroovyInstanceProvider.create(m)));
			RegisterHelper.regist(TCGroovyLoader.class.getName(), MapHelper.toMap("name", m.getName(), "value", m));
		}
		return m;
	}

	/**
	 * reset the TCGroovyLoader.
	 * 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since May 31, 2016
	 */
	public void release() {
		try {
			lock.writeLock().lock();
			Logger.info("On release........");
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
			Logger.info("Release Finished.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * load and parse files.
	 * 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since May 31, 2016
	 */
	public void load() {
		try {
			lock.writeLock().lock();
			Logger.debug(FCS.get("groovy paths: {0}", (Object[]) basePaths));
			for (String basePath : basePaths) {
				try {
					if (basePath.startsWith("classpath:")) {
						String resDir = StringUtils.substringAfterLast(basePath, "classpath:");
						resDir = StringHelper.pathConcat(StringUtils.stripStart(resDir, "/"), "META-INF");
						Set<String> pathSet = new HashSet<String>();
						{// load module xml
							List<String> resources = ResourceHelper.loadResourcesInClassPath(resDir, "tcmodule.xml",
									"tcmodule.xml", null);
							for (String resource : resources) {
								XmlHelper.parseXmlToMap(moduleConfig, resource);
								pathSet.add(StringHelper.cutOff(resource, "tcmodule.xml"));
							}
						}
						{// load resource
							String resourceDir = StringHelper.pathConcat(resDir, "resource");
							List<String> resources = ResourceHelper.loadResourcesInClassPath(resourceDir, "*", "*",
									null);
							for (String url : resources) {
								for (String pathDir : pathSet) {
									if (url.startsWith(pathDir)) {
										resourceMap.put(
												StringHelper.pathConcat("/", StringHelper.cutTo(url, resourceDir)),
												MapHelper.toMap("url", url));
									}
								}
							}
						}
						{// load groovy
							String groovyDir = StringHelper.pathConcat(resDir, "groovy");
							List<String> resources = ResourceHelper.loadResourcesInClassPath(groovyDir, ".groovy", "*",
									null);
							for (String url : resources) {
								for (String pathDir : pathSet) {
									if (url.startsWith(pathDir)) {
										groovyMap.put(StringHelper.pathConcat("/", StringHelper.cutTo(url, groovyDir)),
												MapHelper.toMap("url", url));
									}
								}
							}
						}
					} else if (basePath.startsWith("file:")) {
						boolean isFile = false;
						try {
							File basePathFile = new File(new URL(basePath).toURI());
							isFile = basePathFile.exists() && basePathFile.isFile();
						} catch (Exception e) {
						}
						if (basePath.endsWith(".jar") && isFile) {//is file system jar file
							//jar:file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
							Set<String> pathSet = new HashSet<String>();
							{// load module xml
								String resDir = StringHelper.concat("jar:", basePath, "!/META-INF/");
								List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(resDir,
										"tcmodule.xml", "tcmodule.xml", null);
								for (String resource : resources) {
									XmlHelper.parseXmlToMap(moduleConfig, resource);
									pathSet.add(StringHelper.cutOff(resource, "tcmodule.xml"));
								}
							}
							{// load resource
								String resDir = StringHelper.concat("jar:", basePath, "!/META-INF/resource/");
								List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(resDir, "*", "*",
										null);
								for (String url : resources) {
									for (String resPath : pathSet) {
										if (url.startsWith(resPath)) {
											resourceMap.put(
													StringHelper.pathConcat("/",
															StringHelper.cutTo(url, "META-INF/resource/")),
													MapHelper.toMap("url", url));
										}
									}
								}
							}
							{// load groovy
								String resDir = StringHelper.concat("jar:", basePath, "!/META-INF/groovy/");
								List<String> resources = ResourceHelper.loadResourcesInFileSystemJar(resDir, ".groovy",
										"*", null);
								for (String url : resources) {
									for (String resPath : pathSet) {
										if (url.startsWith(resPath)) {
											groovyMap.put(
													StringHelper.pathConcat("/",
															StringHelper.cutTo(url, "META-INF/groovy/")),
													MapHelper.toMap("url", url));
										}
									}
								}
							}
						} else {//is directory
							Set<String> pathSet = new HashSet<String>();
							{// load module xml
								String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
										"META-INF/");
								List<String> resources = ResourceHelper.loadResources(resDir, "tcmodule.xml",
										"tcmodule.xml", null);
								for (String resource : resources) {
									XmlHelper.parseXmlToMap(moduleConfig, resource);
									pathSet.add(StringHelper.cutOff(resource, "tcmodule.xml"));
								}
							}
							{// load resource
								String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
										"META-INF/resource/");
								List<String> resources = ResourceHelper.loadResources(resDir, "*", "*", null);
								for (String url : resources) {
									for (String resPath : pathSet) {
										if (url.startsWith(resPath)) {
											resourceMap.put(
													StringHelper.pathConcat("/",
															StringHelper.cutTo(url, "META-INF/resource/")),
													MapHelper.toMap("url", url));
										}
									}
								}
							}
							{// load groovy
								String resDir = StringHelper.concat(basePath, basePath.endsWith("/") ? "" : "/",
										"META-INF/groovy/");
								List<String> resources = ResourceHelper.loadResources(resDir, ".groovy", "*", null);
								for (String url : resources) {
									for (String resPath : pathSet) {
										if (url.startsWith(resPath)) {
											groovyMap.put(
													StringHelper.pathConcat("/",
															StringHelper.cutTo(url, "META-INF/groovy/")),
													MapHelper.toMap("url", url));
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					Logger.warn(FCS.get("parse basePath error {0}", basePaths), e);
				}
			}
			Logger.debug(FCS.get("tcmodule.xml:{0}", moduleConfig));
			Logger.debug(FCS.get("resource:{0}", resourceMap.keySet()));
			Logger.debug(FCS.get("groovy:{0}", groovyMap.keySet()));
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
			for (String groovyFileName : groovyMap.keySet()) {
				String dir = groovyFileName;
				dir = dir.endsWith("/") ? dir : (dir + "/");
				List<String> list = pathUrlMap.get(dir);
				if (list == null) {
					list = new ArrayList<String>();
					pathUrlMap.put(dir, list);
				}
				list.add((String) groovyMap.get(groovyFileName).get("url"));
			}
			String[] order = new String[] { "/common/", "/module/", "/service/", "/view/" };
			List<String> orderList = new ArrayList<String>();
			for (String o : order) {
				orderList.clear();
				for (String dir : pathUrlMap.keySet()) {
					if (dir.equals(o) || dir.startsWith(o)) {
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
					beans.put(annotation.name(),
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
					Map<String, Object> actionMap = new HashMap<String, Object>();
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
						actionMap.put(name, method);
					}
					actionMap.put("@className", clazz.getName());
					{
						if (GroovyObject.class.isAssignableFrom(clazz)) {
							GroovyShell shell = new GroovyShell();
							shell.setVariable("cls", clazz);
							shell.evaluate(actionMethod);
						}
					}
					actions.put(annotation.name(), actionMap);
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

	public TCGroovyLoader reloadResource(String resourcePath) {
		try {
			lock.writeLock().lock();
			Map map = resourceMap.get(resourcePath);
			if (map != null) {
				map.put("text", null);
				map.put("byte", null);
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
			return resource;
		} finally {
			lock.readLock().unlock();
		}
	}

	public List<String> findResources(String resourcePathWithwildCardMatch) {
		List<String> result = new ArrayList<String>(32);
		for (String key : resourceMap.keySet()) {
			if (ResourceHelper.wildCardMatch(key, resourcePathWithwildCardMatch)) {
				result.add(key);
			}
		}
		return result;
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

	public static class TCGroovyObjectInvoker {
		private GroovyObject groovyObject;
		private String methodName;
		private String className;

		public TCGroovyObjectInvoker(String groovyBeanName, String methodName) {
			this.groovyObject = getDefaultGroovyLoader().getBean(groovyBeanName);
			this.methodName = methodName;
		}

		public static TCGroovyObjectInvoker get(String groovyBeanName, String methodName) {
			TCGroovyObjectInvoker invoker = new TCGroovyObjectInvoker(groovyBeanName, methodName);
			return invoker;
		}

		public Object invoke(Object... params) {
			try {
				return groovyObject.invokeMethod(methodName, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
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

	public static class TCGroovyInstanceProvider implements InstanceProvider {
		TCGroovyLoader loader = null;

		public static TCGroovyInstanceProvider create(TCGroovyLoader loader) {
			TCGroovyInstanceProvider provider = new TCGroovyInstanceProvider();
			provider.loader = loader;
			return provider;
		}

		public <T> T getInstance(Class<T> beanClass) {
			Exceptions.runtime("Not Support!");
			return null;
		}

		public <T> T getInstance(Class<T> beanClass, String beanName) {
			return (T) loader.getBean(beanName);
		}

		public <T> T getInstance(String beanName) {
			return (T) loader.getBean(beanName);
		}
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String[] getBasePaths() {
		return basePaths;
	}

	protected void setBasePaths(String[] basePaths) {
		this.basePaths = basePaths;
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

	public Map<String, Map> getModuleConfig() {
		return Collections.unmodifiableMap(moduleConfig);
	}

	public static TCGroovyLoader getDefaultGroovyLoader() {
		TCGroovyLoader gl = (TCGroovyLoader) RegisterHelper.get(TCGroovyLoader.class.getName(), "default");
		if (gl != null) {
			return gl;
		}
		Map<String, Object> map = RegisterHelper.get(TCGroovyLoader.class.getName());
		if (map.size() > 0) {
			return (TCGroovyLoader) map.values().iterator().next();
		}
		return null;
	}

	public static final String actionMethod = "" //
			+ "cls.metaClass.params=[:]                                                                                         \n"
			+ "cls.metaClass._request_params=[:]                                                                                \n"
			+ "cls.metaClass._request_userAgent=[:]                                                                             \n"
			+ "if(!cls.metaClass.hasMetaMethod('addUrlParam', [Object,Object] as Class[])){                                     \n"
			+ "	cls.metaClass.addUrlParam={key, value->                                                                         \n"
			+ "		_request_params.put(key, urlEncode(value))                                                                  \n"
			+ "		delegate                                                                                                    \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                \n"
			+ "if(!cls.metaClass.hasMetaMethod('urlEncode', [Object] as Class[])){                                              \n"
			+ "	cls.metaClass.urlEncode={url->                                                                                  \n"
			+ "		url ? java.net.URLEncoder.encode(url,'UTF-8') : ''                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                \n"
			+ "if(!cls.metaClass.hasMetaMethod('urlDecode', [Object] as Class[])){                                              \n"
			+ "	cls.metaClass.urlDecode={url->                                                                                  \n"
			+ "		url ? java.net.URLDecoder.decode(url,'UTF-8') : ''                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                \n"
			+ "if(!cls.metaClass.hasMetaMethod('redirect', [Object] as Class[])){                                               \n"
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
			+ "}                                                                                                                \n"
			+ "if(!cls.metaClass.hasMetaMethod('forward', [Object] as Class[])){                                                \n"
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
			+ "}                                                                                                                \n"
			+ "if(!cls.metaClass.hasMetaMethod('userAgent', [] as Class[])){                                                    \n"
			+ "	cls.metaClass.userAgent={                                                                                       \n"
			+ "		if(_request_userAgent.size()<1){                                                                            \n"
			+ "			_request_userAgent << org.iff.infra.util.HttpHelper.userAgent(params.request.getHeader('User-Agent'))   \n"
			+ "		}                                                                                                           \n"
			+ "		_request_userAgent                                                                                          \n"
			+ "	}                                                                                                               \n"
			+ "}                                                                                                                \n";
}
