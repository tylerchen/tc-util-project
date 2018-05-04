/*******************************************************************************
 * Copyright (c) 2013-2-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import groovy.lang.GroovyObject;

/**
 * A Reflect helper provides a set of utility methods to process the java class.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-14
 */
public class ReflectHelper {

	private static boolean CACHE = false;
	//private static Multimap<Class<?>, Field> CACHE_FIELD = ArrayListMultimap.create();
	//private static Multimap<Class<?>, Method> CACHE_METHOD = ArrayListMultimap.create();

	private ReflectHelper() {
	}

	/**
	 * return if is use cache.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static boolean isCache() {
		return CACHE;
	}

	/**
	 * if true then cache then class fields and method.
	 * @param cache
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static void setCache(boolean cache) {
		CACHE = cache;
	}

	/**
	 * get constructor by class name and parameter type names.
	 * @param className
	 * @param parameterTypes
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Constructor<?> getConstructor(String className, String... parameterTypes) {
		try {
			Class<?> clazz = Class.forName(className);
			return getConstructor(clazz, parameterTypes);
		} catch (Exception e) {
			Logger.error(FCS.get("[NoConstructorFound] className:{0}, parameterTypes:{1}", className, parameterTypes));
		}
		return null;
	}

	/**
	 * get constructor by class and parameter type names.
	 * @param clazz
	 * @param parameterTypes
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Constructor<?> getConstructor(Class clazz, String... parameterTypes) {
		try {
			if (parameterTypes == null || parameterTypes.length < 1) {
				try {
					return clazz.getConstructor();
				} catch (Exception e) {
					return null;
				}
			}
			Constructor<?>[] constructors = clazz.getConstructors();
			for (Constructor<?> c : constructors) {
				Class<?>[] types = c.getParameterTypes();
				if (types.length != parameterTypes.length) {
					continue;
				}
				int i = 0;
				for (; i < types.length; i++) {
					if (!types[i].getName().equals(parameterTypes[i])) {
						break;
					}
				}
				if (i == types.length) {
					return c;
				}
			}
		} catch (Exception e) {
			Logger.error(
					FCS.get("[NoConstructorFound] className:{0}, parameterTypes:{1}", clazz.getName(), parameterTypes));
		}
		return null;
	}

	/**
	 * get method by class name and method name and parameter type names.
	 * @param className
	 * @param method
	 * @param parameterTypes
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Method getMethod(String className, String method, String... parameterTypes) {

		try {
			Class<?> clazz = Class.forName(className);
			return getMethod(clazz, method, parameterTypes);
		} catch (Exception e) {
			Logger.error(FCS.get("[NoMethodFound] className:{0}, method:{1}, parameterTypes:{2}", className, method,
					parameterTypes));
		}
		return null;
	}

	/**
	 * get method by class and method name and parameter type names.
	 * @param clazz
	 * @param method
	 * @param parameterTypes
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Method getMethod(Class clazz, String method, String... parameterTypes) {
		try {
			if (clazz == null) {
				return null;
			}
			parameterTypes = parameterTypes == null ? new String[0] : parameterTypes;
			for (Class<?> superClass = clazz; superClass != null
					&& superClass != Object.class; superClass = superClass.getSuperclass()) {
				try {
					Method[] methods = superClass.getDeclaredMethods();
					for (Method m : methods) {
						if (!m.getName().equals(method)) {
							continue;
						}
						Class<?>[] types = m.getParameterTypes();
						if (types.length != parameterTypes.length) {
							continue;
						}
						int i = 0;
						for (; i < types.length; i++) {
							if (!types[i].getName().equals(parameterTypes[i])) {
								break;
							}
						}
						if (i == types.length) {
							m.setAccessible(true);
							return m;
						}
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			Logger.error(FCS.get("[NoMethodFound] className:{0}, method:{1}, parameterTypes:{2}", clazz.getName(),
					method, parameterTypes));
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的Field
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		if (obj == null || fieldName == null) {
			return null;
		}
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValueByFieldName(Object obj, String fieldName) {
		Object value = null;
		try {
			Field field = getFieldByFieldName(obj, fieldName);
			if (field != null) {
				if (field.isAccessible()) {
					value = field.get(obj);
				} else {
					field.setAccessible(true);
					value = field.get(obj);
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueByFieldType(Object obj, Class<T> fieldType) {
		Object value = null;
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field[] fields = superClass.getDeclaredFields();
				for (Field f : fields) {
					if (f.getType() == fieldType) {
						if (f.isAccessible()) {
							value = f.get(obj);
							break;
						} else {
							f.setAccessible(true);
							value = f.get(obj);
							f.setAccessible(false);
							break;
						}
					}
				}
				if (value != null) {
					break;
				}
			} catch (Exception e) {
			}
		}
		return (T) value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static boolean setValueByFieldName(Object obj, String fieldName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field.isAccessible()) {
				field.set(obj, value);
			} else {
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(false);
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * invoke object by method name and arguments.
	 * @param instance
	 * @param methodName
	 * @param args
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Object invoke(Object instance, String methodName, Object[] args) {
		Assert.notNull(instance, "args [instance] is required!");
		Assert.notBlank(methodName, "args [methodName] is required!");
		try {
			if (instance instanceof GroovyObject) {
				GroovyObject go = (GroovyObject) instance;
				return go.invokeMethod(methodName, args);
			} else {//static invoke
				List<String> argsType = new ArrayList<String>();
				if (args != null) {
					for (Object arg : args) {
						argsType.add(arg.getClass().getName());
					}
				}
				Method method = getMethod((Class) instance, methodName, argsType.toArray(new String[argsType.size()]));
				if (instance instanceof Class) {
					return method.invoke(null, args);
				} else {
					return method.invoke(instance, args);
				}
			}
		} catch (Exception e) {
			Exceptions.runtime(FCS.get("invoke method {0} error.", methodName), e);
		}
		return null;
	}

	/**
	 * invoke object by unique method name and json or xstream arguments.
	 * @param instance
	 * @param methodName
	 * @param args
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Object invokeByUniqueMethod(Object instance, String methodName, List<String> args) {
		Assert.notNull(instance, "args [instance] is required!");
		Assert.notBlank(methodName, "args [methodName] is required!");
		try {
			if (instance instanceof GroovyObject) {
				GroovyObject go = (GroovyObject) instance;
				return go.invokeMethod(methodName, args);
			} else {//static invoke
				List<String> argsType = new ArrayList<String>();
				if (args != null) {
					for (Object arg : args) {
						argsType.add(arg.getClass().getName());
					}
				}
				Method method = getMethod((Class) instance, methodName, argsType.toArray(new String[argsType.size()]));
				if (instance instanceof Class) {
					return method.invoke(null, args);
				} else {
					return method.invoke(instance, args);
				}
			}
		} catch (Exception e) {
			Exceptions.runtime(FCS.get("invoke method {0} error.", methodName), e);
		}
		return null;
	}
}
