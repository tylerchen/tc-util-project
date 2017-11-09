/*******************************************************************************
 * Copyright (c) Feb 4, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.TypeConvertHelper.TypeConvert;

import com.esotericsoftware.minlog.Log;

import groovy.lang.GroovyClassLoader;

/**
 * Not deep copy.
 * Efficiency for po jo copy.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 4, 2016
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class POVOCopyHelper {

	private static final POVOCopyHelper me = new POVOCopyHelper();
	private static final GroovyClassLoader gcl = new GroovyClassLoader();
	private static final Map<Class, Map<String, Method>[]> getterAndSetterMaps = new HashMap<Class, Map<String, Method>[]>();

	/**
	 * get POVOCopyHelper instance.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static POVOCopyHelper me() {
		return me;
	}

	/**
	 * copy source object to destination object.
	 * @param src
	 * @param dest
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T copyTo(Object src, Object dest) {
		return me().get(src, dest).copyTo(src, dest);
	}

	/**
	 * copy source list to dest list.
	 * @param src
	 * @param destWildcardTypeClass
	 * @param dest
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T copyListTo(Collection<?> src, Class<?> destWildcardTypeClass, Collection dest) {
		if (src == null || dest == null) {
			return me().get(src, dest).copyTo(src, dest);
		}
		if (destWildcardTypeClass == null) {
			return (T) src;
		}
		for (Object o : src) {
			dest.add((Object) me().get(o, destWildcardTypeClass));
		}
		return (T) dest;
	}

	private Map<String, PoVoCopy> map = Collections.synchronizedMap(new HashMap<String, PoVoCopy>());

	protected POVOCopyHelper() {
		super();
		init();
	}

	/**
	 * regist default Copy.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	void init() {
		map.put("null", new NullPoVoCopy());
		map.put("default", new DefaultPoVoCopy());
		map.put("TypeConvert", new TypeConvertPoVoCopy());
		map.put("Direct", new DirectPoVoCopy());
	}

	/**
	 * po vo copy instance.
	 * @author zhaochen
	 */
	public static interface PoVoCopy {
		<T> T copyTo(Object src, Object dest);
	}

	/**
	 * null able po vo copy, only return null.
	 * @author zhaochen
	 */
	class NullPoVoCopy implements PoVoCopy {
		public <T> T copyTo(Object src, Object dest) {
			Logger.debug("Using NullPoVoCopy to copy objects, return dest object!");
			if (dest instanceof Class) {
				return null;
			} else {
				return (T) dest;
			}
		}
	}

	/**
	 * only return src.
	 * @author zhaochen
	 */
	class DirectPoVoCopy implements PoVoCopy {
		public <T> T copyTo(Object src, Object dest) {
			Logger.warn("Using DirectPoVoCopy to copy objects!");
			return (T) src;
		}
	}

	/**
	 * default po vo copy.
	 * @author zhaochen
	 */
	class DefaultPoVoCopy implements PoVoCopy {
		public <T> T copyTo(Object src, Object dest) {
			Logger.warn("Using DefaultPoVoCopy to copy objects, lower efficiency!");
			return BeanHelper.copyProperties(dest, src, false);
		}
	}

	/**
	 * type convert copy.
	 * @author zhaochen
	 */
	class TypeConvertPoVoCopy implements PoVoCopy {
		public <T> T copyTo(Object src, Object dest) {
			Logger.debug("Using TypeConvertHelper to copy objects for java base type!");
			return (T) TypeConvertHelper.me().get(getClassName(dest)).convert(getClassName(dest), src, src.getClass(),
					null);
		}
	}

	/**
	 * delegate po vo copy .
	 * @author zhaochen
	 */
	class BlockingDelegatePoVoCopy implements PoVoCopy {
		PoVoCopy delegate = null;

		public void unlock(PoVoCopy delegate) {
			this.delegate = delegate;
		}

		public <T> T copyTo(Object src, Object dest) {
			int count = 10;// 100ms
			while (delegate == null && count-- > 0) {
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (Exception e) {
				}
			}
			if (delegate == null) {
				Logger.warn("Create PoVoCopy fail, use DefaultPoVoCopy!");
				delegate = map.get("default");
			}
			return delegate.copyTo(src, dest);
		}
	}

	/**
	 * return po vo copy.
	 * @param src
	 * @param dest
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public PoVoCopy get(Object src, Object dest) {
		if (src == null || dest == null) {
			return map.get("null");
		}
		Class<?> srcClass = src instanceof Class ? (Class<?>) src : src.getClass();
		Class<?> destClass = dest instanceof Class ? (Class<?>) dest : dest.getClass();
		TypeConvert typeConvertSrc = TypeConvertHelper.me().getNullable(srcClass.getName());
		TypeConvert typeConvertDest = TypeConvertHelper.me().getNullable(destClass.getName());
		boolean assignableMapSrc = isAssignTo(srcClass, Map.class);
		boolean assignableMapDest = isAssignTo(destClass, Map.class);
		boolean assignableCollectionSrc = isAssignTo(srcClass, Collection.class);
		boolean assignableCollectionDest = isAssignTo(destClass, Collection.class);
		if (typeConvertSrc != null && typeConvertDest != null) {
			return map.get("TypeConvert");
		}
		if (assignableMapSrc && assignableMapDest) {
			return map.get("default");
		}
		if ((assignableCollectionSrc || assignableCollectionDest)
				&& assignableCollectionSrc != assignableCollectionDest) {
			return map.get("default");
		}
		if (typeConvertSrc != null || typeConvertDest != null) {
			return map.get("default");
		}
		if (assignableMapSrc && typeConvertDest == null) {
			String key = new StringBuilder().append(Map.class.getName()).append('-').append(destClass.getName())
					.toString();
			PoVoCopy copy = map.get(key);
			if (copy == null) {
				Logger.debug(FCS.get("Can't find PoVoCopy for {0} from map, try to create.", key));
				BlockingDelegatePoVoCopy block = new BlockingDelegatePoVoCopy();
				{
					map.put(key, block);
				}
				copy = createCopyFromMap(key, srcClass, destClass);
				if (copy == null) {
					Logger.error(FCS.get("Try to create PoVoCopy for {0} fail, use DefaultPoVoCopy instead.", key));
					copy = map.get("default");
				}
				{
					block.unlock(copy);
					map.put(key, copy);
				}
			}
			return copy;
		}
		if (assignableMapDest && typeConvertSrc == null) {
			String key = new StringBuilder().append(srcClass.getName()).append('-').append(Map.class.getName())
					.toString();
			PoVoCopy copy = map.get(key);
			if (copy == null) {
				Logger.debug(FCS.get("Can't find PoVoCopy for {0} from map, try to create.", key));
				BlockingDelegatePoVoCopy block = new BlockingDelegatePoVoCopy();
				{
					map.put(key, block);
				}
				copy = createCopyToMap(key, srcClass, destClass);
				if (copy == null) {
					Logger.error(FCS.get("Try to create PoVoCopy for {0} fail, use DefaultPoVoCopy instead.", key));
					copy = map.get("default");
				}
				{
					block.unlock(copy);
					map.put(key, copy);
				}
			}
			return copy;
		}
		if (srcClass.getName().startsWith("java") && destClass.getName().startsWith("java")) {
			return map.get("Direct");
		}
		{
			String key = new StringBuilder().append(getClassName(srcClass)).append('-').append(getClassName(destClass))
					.toString();
			PoVoCopy copy = map.get(key);
			if (copy == null) {
				Logger.debug(FCS.get("Can't find PoVoCopy for {0} from map, try to create.", key));
				BlockingDelegatePoVoCopy block = new BlockingDelegatePoVoCopy();
				{
					map.put(key, block);
				}
				copy = createCopyPoVo(key, srcClass, destClass);
				if (copy == null) {
					Logger.error(FCS.get("Try to create PoVoCopy for {0} fail, use DefaultPoVoCopy instead.", key));
					copy = map.get("default");
				}
				{
					block.unlock(copy);
					map.put(key, copy);
				}
			}
			return copy;
		}
	}

	/**
	 * create map to po copy convertor.
	 * @param key
	 * @param srcClass
	 * @param destClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	protected synchronized PoVoCopy createCopyFromMap(String key, Class<?> srcClass, Class<?> destClass) {
		String source = ""/**/
				+ "\npackage org.iff.autogen.povocopy"/**/
				+ "\npublic class {className} implements {PoVoCopy} {"/**/
				+ "\n    @groovy.transform.CompileStatic"/**/
				+ "\n    public <T> T copyTo( Object src, Object dest ){"/**/
				+ "\n        if ( dest ==null || src == null) {"/**/
				+ "\n            return null;"/**/
				+ "\n        }"/**/
				+ "\n        java.util.Map srcMap = (java.util.Map) src;"/**/
				+ "\n        dest = (dest == null || dest instanceof Class) ? new {destClass}() : dest;"/**/
				+ "\n        {destClass} destCast = ({destClass}) dest;"/**/
				+ "\n        {LookSameMapGetter} mapGetter=new {LookSameMapGetter}(srcMap);"/**/
				+ "{copyMap}"/**/
				+ "\n        return (T) destCast;"/**/
				+ "\n    }"/**/
				+ "\n}"/**/
				;
		StringBuilder sb = new StringBuilder(512);
		{
			Map<String, Method>[] getterAndSetterMap = getterAndSetterMap(destClass);
			Map<String, Method> setterMap = getterAndSetterMap[1];
			for (Entry<String, Method> entry : setterMap.entrySet()) {
				String fieldName = entry.getKey();
				Method setter = entry.getValue();
				Class<?> setterParamType = setter.getParameterTypes()[0];
				Class<?> wildcardType = getParameter0WildcardType(setter);
				String fragment = ""/**/
						+ "\n        try {"/**/
						+ "\n            Object obj = mapGetter.get(\"{fieldName}\");"/**/
						+ "\n            if(obj != null) {"/**/
						+ "\n                if(obj instanceof {setterParamClass} && !(obj instanceof java.util.Collection)) {"/**/
						+ "\n                    destCast.{setterName}( ({setterParamClass}) obj );"/**/
						+ "\n                } else {"/**/
						+ "\n                    obj = {POVOCopyHelper}.{copyTo}( obj, {parameter} );"/**/
						+ "\n                    if(obj != null) {"/**/
						+ "\n                        destCast.{setterName}( ({setterParamClass}) obj );"/**/
						+ "\n                    }"/**/
						+ "\n                }"/**/
						+ "\n            }"/**/
						+ "\n        } catch(Exception e) {"/**/
						+ "\n            org.iff.infra.util.Logger.error( \" Copy map key value to property [{fieldName}] error! \", e );"/**/
						+ "\n        }"/**/
						;
				String parameter = setterParamType.getName() + ".class";
				String copyTo = "copyTo";
				if (isAssignTo(srcClass, Collection.class) && isAssignTo(destClass, Collection.class)) {
					parameter = (wildcardType == null ? "null, " : (wildcardType.getName() + ".class, ")) + "new "
							+ getDefaultListClass(setterParamType) + "() ";
					copyTo = "copyListTo";
				}
				sb.append(StringHelper.replaceBlock(fragment,
						MapHelper.toMap(/**/
								"fieldName", fieldName, /**/
								"setterParamClass", getObjectClassName(setterParamType.getName()), /**/
								"setterName", setter.getName(), /**/
								"POVOCopyHelper", POVOCopyHelper.class.getName(), /**/
								"copyTo", copyTo, /**/
								"parameter", parameter/**/
				), null));
			}
		}
		{
			source = StringHelper.replaceBlock(source,
					MapHelper.toMap(/**/
							"className",
							(StringUtils.replace(Map.class.getName(), ".", "_") + "$"
									+ StringUtils.replace(destClass.getName(), ".", "_")), /**/
							"PoVoCopy", PoVoCopy.class.getName(), /**/
							"destClass", getDefaultClass(destClass), /**/
							"copyMap", sb.toString(), /**/
							"LookSameMapGetter", LookSameMapGetter.class.getName()/**/
			), null);
		}
		//System.out.println(source);
		try {
			Class<PoVoCopy> clazz = gcl.parseClass(source);
			PoVoCopy poVoCopy = clazz.newInstance();
			return poVoCopy;
		} catch (Exception e) {
			Logger.error(FCS.get("Create PoVoCopy class by source error, source: \n", source), e);
		}
		return null;
	}

	/**
	 * create po to map copy convertor.
	 * @param key
	 * @param srcClass
	 * @param destClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	protected synchronized PoVoCopy createCopyToMap(String key, Class<?> srcClass, Class<?> destClass) {
		String source = ""/**/
				+ "\npackage org.iff.autogen.povocopy"/**/
				+ "\npublic class {className} implements {PoVoCopy} {"/**/
				+ "\n    @groovy.transform.CompileStatic"/**/
				+ "\n    public <T> T copyTo( Object src, Object dest ){"/**/
				+ "\n        if ( dest ==null || src == null) {"/**/
				+ "\n            return null;"/**/
				+ "\n        }"/**/
				+ "\n        dest = (dest == null || dest instanceof Class) ? new {destClass}() : dest;"/**/
				+ "\n        java.util.Map destCast = (java.util.Map) dest;"/**/
				+ "\n        {srcClass} srcCast = ({srcClass}) src;"/**/
				+ "{copyToMap}"/**/
				+ "\n        return (T) destCast;"/**/
				+ "\n    }"/**/
				+ "\n}"/**/
				;
		StringBuilder sb = new StringBuilder(512);
		{
			Map<String, Method>[] getterAndSetterMap = getterAndSetterMap(srcClass);
			Map<String, Method> getterMap = getterAndSetterMap[0];
			for (Entry<String, Method> entry : getterMap.entrySet()) {
				String fieldName = entry.getKey();
				Method getter = entry.getValue();
				String fragment = ""/**/
						+ "\n        destCast.put( \"{fieldName}\", srcCast.{getterName}() );"/**/
						;
				sb.append(StringHelper.replaceBlock(fragment,
						MapHelper.toMap("fieldName", fieldName, "getterName", getter.getName()), null));
			}
		}
		{
			source = StringHelper.replaceBlock(source,
					MapHelper.toMap("className",
							StringUtils.replace(getClassName(srcClass), ".", "_") + "$"
									+ StringUtils.replace(getClassName(Map.class), ".", "_"),
							"PoVoCopy", getClassName(PoVoCopy.class), "destClass", getDefaultClass(destClass),
							"srcClass", srcClass.getName(), "copyToMap", sb),
					null);
		}
		//System.out.println(source);
		try {
			Class<PoVoCopy> clazz = gcl.parseClass(source);
			PoVoCopy poVoCopy = clazz.newInstance();
			return poVoCopy;
		} catch (Exception e) {
			Logger.error(FCS.get("Create PoVoCopy class by source error, source: \n", source), e);
		}
		return null;
	}

	/**
	 * create po-vo copy convertor.
	 * @param key
	 * @param srcClass
	 * @param destClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	protected synchronized PoVoCopy createCopyPoVo(String key, Class<?> srcClass, Class<?> destClass) {
		String source = ""/**/
				+ "\npackage org.iff.autogen.povocopy"/**/
				+ "\npublic class {className} implements {PoVoCopy} {"/**/
				+ "\n    @groovy.transform.CompileStatic"/**/
				+ "\n    public <T> T copyTo( Object src, Object dest ){"/**/
				+ "\n        if ( dest ==null || src == null) {"/**/
				+ "\n            return null;"/**/
				+ "\n        }"/**/
				+ "\n        dest = (dest == null || dest instanceof Class) ? new {destClass}() : dest;"/**/
				+ "\n        {destClass} destCast = ({destClass}) dest;"/**/
				+ "\n        {srcClass} srcCast = ({srcClass}) src;"/**/
				+ "{copy}"/**/
				+ "\n        return (T) destCast;"/**/
				+ "\n    }"/**/
				+ "\n}"/**/
				;
		StringBuilder sb = new StringBuilder(512);
		{
			Map<String, Method> getterMap = getterAndSetterMap(srcClass)[0];
			Map<String, Method> setterMap = getterAndSetterMap(destClass)[1];
			for (Entry<String, Method> entry : setterMap.entrySet()) {
				String fieldName = entry.getKey();
				Method setter = entry.getValue();
				Method getter = getterMap.get(fieldName);
				if (getter == null) {
					continue;
				}
				if (isAssignTo(setter.getParameterTypes()[0], Collection.class)
						&& isAssignTo(getter.getReturnType(), Collection.class)) {
					String fragment = ""/**/
							+ "\n        destCast.{setterName}( ({setterType}) {POVOCopyHelper}.copyListTo( srcCast.{getterName}(), {wildType}, new {setterDefaultType}() ) );"/**/
							;
					sb.append(StringHelper.replaceBlock(fragment,
							MapHelper.toMap("setterName", setter.getName(), "setterType",
									getClassName(setter.getParameterTypes()[0]), "POVOCopyHelper",
									getClassName(POVOCopyHelper.class), "getterName", getter.getName(), "wildType",
									getClassName(getParameter0WildcardType(setter)), "setterDefaultType",
									getDefaultClass(setter.getParameterTypes()[0])),
							null));
				} else if (setter.getParameterTypes()[0] == getter.getReturnType()) {
					String fragment = ""/**/
							+ "\n        destCast.{setterName}( srcCast.{getterName}() );"/**/
							;
					sb.append(StringHelper.replaceBlock(fragment,
							MapHelper.toMap("setterName", setter.getName(), "getterName", getter.getName()), null));
				} else {
					String fragment = ""/**/
							+ "\n        try {"/**/
							+ "\n            Object obj = srcCast.{getterName}();"/**/
							+ "\n            if(obj != null) {"/**/
							+ "\n                //if((obj instanceof {setterType}) && !(obj instanceof java.util.Collection)) {"/**/
							+ "\n                //    destCast.{setterName}( ({setterType}) obj );"/**/
							+ "\n                //} else {"/**/
							+ "\n                    obj = {POVOCopyHelper}.copyTo( obj, {setterType}.class );"/**/
							+ "\n                    if(obj != null) {"/**/
							+ "\n                        destCast.{setterName}( ({setterType}) obj );"/**/
							+ "\n                    }"/**/
							+ "\n                //}"/**/
							+ "\n            }"/**/
							+ "\n        } catch(Exception e) {"/**/
							+ "\n            org.iff.infra.util.Logger.error( \" Copy map key value to property [{fieldName}] error! \", e );"/**/
							+ "\n        }"/**/
							;
					sb.append(StringHelper.replaceBlock(fragment,
							MapHelper.toMap("getterName", getter.getName(), "POVOCopyHelper",
									getClassName(POVOCopyHelper.class), "setterName", setter.getName(), "setterType",
									getObjectClassName(getClassName(setter.getParameterTypes()[0])), "fieldName",
									fieldName),
							null));
				}
			}
		}
		{
			source = StringHelper.replaceBlock(source,
					MapHelper.toMap("className",
							StringUtils.replace(getClassName(srcClass), ".", "_") + "$"
									+ StringUtils.replace(getClassName(destClass), ".", "_"),
							"PoVoCopy", PoVoCopy.class.getName(), "destClass", getDefaultClass(destClass), "srcClass",
							srcClass.getName(), "copy", sb),
					null);
		}
		Log.debug(source);
		try {
			Class<PoVoCopy> clazz = gcl.parseClass(source);
			PoVoCopy poVoCopy = clazz.newInstance();
			return poVoCopy;
		} catch (Exception e) {
			Logger.error(FCS.get("Create PoVoCopy class by source error, source: \n", source), e);
		}
		return null;
	}

	/**
	 * return getter and setter.
	 * @param clazz
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private Map<String, Method>[] getterAndSetterMap(Class<?> clazz) {
		Map<String, Method>[] maps = getterAndSetterMaps.get(clazz);
		if (maps != null) {
			return maps;
		}
		Map<String, Method> setterMap = new LinkedHashMap<String, Method>();
		Map<String, Method> getterMap = new LinkedHashMap<String, Method>();
		{
			Class<?> tmpClass = clazz;
			while (tmpClass != Object.class && !tmpClass.isInterface()) {
				Method[] methods = tmpClass.getMethods();
				for (Method m : methods) {
					String name = m.getName();
					if (name.startsWith("set") && name.length() > 3) {
						String fieldName = StringUtils.uncapitalize(name.substring(3));
						if (setterMap.containsKey(fieldName)) {
							continue;
						}
						if (m.getParameterTypes().length != 1) {
							continue;
						}
						setterMap.put(fieldName, m);
					} else if (name.startsWith("get") && name.length() > 3 && !"getClass".equals(name)) {
						String fieldName = StringUtils.uncapitalize(name.substring(3));
						if (getterMap.containsKey(fieldName)) {
							continue;
						}
						Class<?> dc = m.getReturnType();
						if (dc == null || void.class == dc) {
							continue;
						}
						getterMap.put(fieldName, m);
					} else if (name.startsWith("is")
							&& (m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class)
							&& name.length() > 2) {
						String fieldName = StringUtils.uncapitalize(name.substring(2));
						if (getterMap.containsKey(fieldName)) {
							continue;
						}
						getterMap.put(fieldName, m);
					}
				}
				tmpClass = tmpClass.getSuperclass();
			}
		}
		maps = new Map[] { getterMap, setterMap };
		getterAndSetterMaps.put(clazz, maps);
		return maps;
	}

	/**
	 * return default class, @see getDefaultListClass, @see getDefaultMapClass.
	 * @param clazz
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static String getDefaultClass(Class<?> clazz) {
		if (hasDefaultConstructor(clazz)) {
			return clazz.getName();
		}
		if (isAssignTo(clazz, Map.class)) {
			return getDefaultMapClass(clazz);
		}
		if (isAssignTo(clazz, Collection.class)) {
			return getDefaultListClass(clazz);
		}
		return clazz.getName();
	}

	/**
	 * return default list class name. if the class is instance-able with default constructor then return this class name else return default.
	 * default is java.util.ArrayList.
	 * @param clazz
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static String getDefaultListClass(Class<?> clazz) {
		if (clazz != null && isAssignTo(clazz, Collection.class)) {
			if (hasDefaultConstructor(clazz)) {
				return clazz.getName();
			}
			return ArrayList.class.getName();
		}
		return null;
	}

	/**
	 * return default map class name. if the class is instance-able with default constructor then return this class name else return default.
	 * default is java.util.LinkedHashMap.
	 * @param clazz
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static String getDefaultMapClass(Class<?> clazz) {
		if (clazz != null && isAssignTo(clazz, Map.class)) {
			if (hasDefaultConstructor(clazz)) {
				return clazz.getName();
			}
			return LinkedHashMap.class.getName();
		}
		return null;
	}

	/**
	 * test has default constructor.
	 * @param clazz
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static boolean hasDefaultConstructor(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		try {
			return clazz.getConstructor(new Class[0]) != null;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Return the wildcard type of the method for first parameter. For setter.
	 * @param method
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static Class<?> getParameter0WildcardType(Method method) {
		try {
			Type[] types = method.getGenericParameterTypes();
			if (types.length < 1) {
				return null;
			}
			if (ParameterizedType.class.isInstance(types[0])) {
				Type[] args = ((ParameterizedType) types[0]).getActualTypeArguments();
				if (args[0] instanceof Class) {
					Class<?> clazz = (Class<?>) args[0];
					if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
						return clazz;
					}
					Logger.warn(FCS.get("The method [{0}] parameter 0 WildcardType CANNOT be interface or abstract",
							method));
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Return the wildcard type of the method result type. For getter. 
	 * @param method
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static Class<?> getReturnWildcardType(Method method) {
		try {
			Type type = method.getGenericReturnType();
			if (ParameterizedType.class.isInstance(type)) {
				Type[] args = ((ParameterizedType) type).getActualTypeArguments();
				if (args[0] instanceof Class) {
					Class<?> clazz = (Class<?>) args[0];
					if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
						return clazz;
					}
					Logger.warn(
							FCS.get("The method [{0}] return WildcardType CANNOT be interface or abstract", method));
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * return class name if the object is class or an instance
	 * @param classOrInstance
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static String getClassName(Object classOrInstance) {
		if (classOrInstance == null) {
			return null;
		}
		if (classOrInstance instanceof Class) {
			return ((Class) classOrInstance).getName();
		}
		return classOrInstance.getClass().getName();
	}

	/**
	 * type parent is assign child type.
	 * @param child the child type
	 * @param parent the parent type you think
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	private static boolean isAssignTo(Class<?> child, Class<?> parent) {
		return parent.isAssignableFrom(child);
	}

	/**
	 * get primitive type's Object class name.
	 * @param className
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Oct 21, 2016
	 */
	private static String getObjectClassName(String className) {
		if ("boolean".equals(className)) {
			return Boolean.class.getName();
		} else if ("byte".equals(className)) {
			return Byte.class.getName();
		} else if ("short".equals(className)) {
			return Short.class.getName();
		} else if ("int".equals(className)) {
			return Integer.class.getName();
		} else if ("long".equals(className)) {
			return Long.class.getName();
		} else if ("float".equals(className)) {
			return Float.class.getName();
		} else if ("double".equals(className)) {
			return Double.class.getName();
		} else if ("char".equals(className)) {
			return Character.class.getName();
		}
		return className;
	}

	public static class LookSameMapGetter {
		private Map origMap;
		private Map lookSameMap;

		public LookSameMapGetter(Map map) {
			this.origMap = map == null ? new HashMap() : map;
		}

		public Object get(String fieldName) {
			if (origMap.containsKey(fieldName)) {
				return origMap.get(fieldName);
			}
			return getLookSameValue(fieldName);
		}

		public Object getLookSameValue(String fieldName) {
			StringBuilder sb = new StringBuilder(32);
			{
				for (int i = 0; i < fieldName.length(); i++) {
					char c = fieldName.charAt(i);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
						sb.append(c);
					}
				}
				fieldName = sb.toString().toLowerCase();
			}
			if (lookSameMap == null) {
				lookSameMap = new HashMap();
				for (Entry entry : (Set<Entry>) origMap.entrySet()) {
					Object key = entry.getKey();
					if (key instanceof String) {
						sb.setLength(0);
						String strKey = (String) key;
						for (int i = 0; i < strKey.length(); i++) {
							char c = strKey.charAt(i);
							if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
								sb.append(c);
							}
						}
						lookSameMap.put(sb.toString().toLowerCase(), entry.getValue());
					}
				}
			}
			return lookSameMap.get(fieldName);
		}
	}
}
