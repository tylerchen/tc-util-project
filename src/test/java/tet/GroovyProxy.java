package tet;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.poi.ss.formula.functions.T;
import org.iff.infra.domain.InstanceProviderDelegate;
import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RSAHelper;
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.TypeConvertHelper;
import org.springframework.beans.BeanUtils;

import com.thoughtworks.xstream.XStream;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.util.Proxy;

/*******************************************************************************
 * Copyright (c) Jan 12, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 12, 2016
 */
public class GroovyProxy {
	public static void main(String[] args) {
		String code = ""//
				+ "\npublic class ModelProperty_TcProxy extends tet.ModelProperty{"//
				+ "\n    private transient java.util.Map __tc__=org.iff.infra.util.MapHelper.toMap(\"id\",123);"//
				+ "\n    @groovy.transform.CompileStatic"//
				+ "\n    public String getId() {"//
				+ "\n        if(super.getId()==null){"//
				+ "\n            super.setId((String)__tc__.get(\"id\"));"//
				+ "\n        }"//
				+ "\n        return super.getId();"//
				+ "\n    }"//
				+ "\n}";//
		GroovyClassLoader gcl = new GroovyClassLoader();
		Class clazz = gcl.parseClass(code);
		try {
			ModelProperty proxy = (ModelProperty) clazz.newInstance();
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					proxy.getId();
				}
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(GsonHelper.toJsonString(proxy));
			}
			{
				long start = System.currentTimeMillis();
				Method method = ReflectHelper.getMethod(clazz, "getId");
				for (int i = 0; i < 1000 * 1000; i++) {
					method.invoke(proxy);
				}
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				long start = System.currentTimeMillis();
				ModelProperty mp = new ModelProperty();
				for (int i = 0; i < 1000 * 1000; i++) {
					mp.getId();
				}
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					BeanHelper.copyProperties(ModelProperty.class, proxy);
				}
				System.out.println(System.currentTimeMillis() - start);
			}
			Logger.debug(new XStream().toXML(proxy));

			{
				String genGroovyPoVoCopyClass = genGroovyPoVoCopyClass(ModelProperty.class, ModelPropertyVO.class);
				Logger.debug(genGroovyPoVoCopyClass);
				Class povo = gcl.parseClass(genGroovyPoVoCopyClass);
				GroovyObject object = (GroovyObject) povo.newInstance();
				ModelProperty invokeMethod = (ModelProperty) object.invokeMethod("copy",
						new Object[] { new ModelProperty(), MapHelper.toMap("id", "test") });
				System.out.println(invokeMethod.getId());
			}
			//System.out.println(genGroovyProxyClass(ModelProperty.class));
			//RSAHelper.decryptByDefaultKey("test".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer copy(String a, Integer b) {
		return null;
	}

	public String copy(Integer b, String a) {
		return null;
	}

	private static Map[] getterAndSetterMap(Class<?> clazz) {
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
					} else if (name.startsWith("get") && name.length() > 3) {
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
		return new Map[] { getterMap, setterMap };
	}

	public static String genGroovyPoVoCopyClass(Class<?> dest, Class<?> src) throws Exception {
		Assert.notNull(dest);
		Assert.notNull(src);
		if (!TypeUtils.isAssignable(Map.class, dest)) {
			Assert.isTrue(!(dest.isInterface() || Modifier.isAbstract(dest.getModifiers())));
		}
		if (!TypeUtils.isAssignable(Map.class, src)) {
			Assert.isTrue(!(src.isInterface() || Modifier.isAbstract(src.getModifiers())));
		}
		StringBuilder sb = new StringBuilder(1024);
		Map[] destMap = getterAndSetterMap(dest);
		Map[] srcMap = getterAndSetterMap(src);
		Map<Class<?>, Map[]> map = MapHelper.toMap(dest, destMap, src, srcMap);
		String packageName = "org.iff.autogen.povocopy";
		String className = FCS.get("{destClassName}_{srcClassName}_{uuid}", dest.getSimpleName(), src.getSimpleName(),
				StringHelper.uuid()).toString();
		{// gen code

			{// package and class
				if (!StringUtils.isBlank(packageName)) {
					sb.append(FCS.get("\npackage {packageName};", packageName).toString());
				}
				sb.append(FCS
						.get("\npublic class {className} implements {PoVoCopy} {", className, PoVoCopy.class.getName())
						.toString());
			}
			{// implement PoVoCopy
				String s = ""//
						+ "\n    @groovy.transform.CompileStatic"//
						+ "\n    public <T> T copy( Object dest, Object src ){"//
						+ "\n        if ( dest ==null || src == null) {"//
						+ "\n            return null;"//
						+ "\n        }"//
						+ "\n        return (T) copy_{toClass}_{fromClass}( ({toClass}) dest, ({fromClass}) src );"//
						+ "\n    }"//
						;
				sb.append(FCS.get(s,
						TypeUtils.isAssignable(Map.class, dest) ? Map.class.getSimpleName() : dest.getSimpleName(),
						TypeUtils.isAssignable(Map.class, src) ? Map.class.getSimpleName() : src.getSimpleName(),
						TypeUtils.isAssignable(Map.class, dest) ? Map.class.getName() : dest.getName(),
						TypeUtils.isAssignable(Map.class, src) ? Map.class.getName() : src.getName()).toString());
			}
			List<Class<?>[]> list = new ArrayList<Class<?>[]>();
			{// this will add public <T> T copy( Class1, Class2 ), if Class1 == Class2, then one method is fine.
				list.add(new Class<?>[] { dest, src });
				if (!Map.class.isAssignableFrom(dest)) {
					list.add(new Class<?>[] { dest, Map.class });
					list.add(new Class<?>[] { Map.class, dest });
				}
				if (!dest.getName().equals(src.getName())) {
					// add public <T> T copy( Class2, Class1 )
					list.add(new Class<?>[] { src, dest });
					// add public <T> T copy( Class1, Class1 )
					list.add(new Class<?>[] { dest, dest });
					// add public <T> T copy( Class2, Class2 )
					list.add(new Class<?>[] { src, src });
					if (!Map.class.isAssignableFrom(src)) {
						list.add(new Class<?>[] { src, Map.class });
						list.add(new Class<?>[] { Map.class, src });
					}
				}
			}
			for (Class<?>[] classes : list) {
				Class<?> to = classes[0];
				Class<?> from = classes[1];

				sb.append(FCS.get("\n{tab}@groovy.transform.CompileStatic", StringUtils.repeat(' ', 4)).toString());
				sb.append(
						FCS.get("\n{tab}public <T> T copy_{toClass}_{fromClass}( {toClass} dest, {fromClass} src ){",
								StringUtils.repeat(' ', 4), TypeUtils.isAssignable(Map.class, to)
										? Map.class.getSimpleName() : to.getSimpleName(),
						TypeUtils.isAssignable(Map.class, from) ? Map.class.getSimpleName() : from.getSimpleName(),
						from.getName(), from.getName()).toString());
				Map<String, Method> toSetter = Map.class == to ? new HashMap<String, Method>() : map.get(to)[1];
				Map<String, Method> fromGetter = Map.class == from ? new HashMap<String, Method>() : map.get(from)[0];
				if (Map.class == to) {
					// convert object to map
					// java.util.Map map = new java.util.LinkedHashMap();
					// for-all-getter{
					//   map.put( fieldName, src.getterName() );
					// }
					// return (Map) map;
					sb.append(FCS.get("\n{tab}java.util.Map map = new java.util.LinkedHashMap();",
							StringUtils.repeat(' ', 8)).toString());
					for (Entry<String, Method> entry : fromGetter.entrySet()) {
						String fieldName = entry.getKey();
						Method getter = entry.getValue();
						sb.append(FCS.get("\n{tab}map.put( \"{fieldName}\", src.{getterName}() );",
								StringUtils.repeat(' ', 8), fieldName, getter.getName()).toString());
					}
					sb.append(FCS.get("\n{tab}return (T) map;", StringUtils.repeat(' ', 8)).toString());
				} else if (Map.class == from) {
					// convert map to object
					// for-all-setter{
					//   Object obj = map.get(fieldName);
					//   GroovyProxy.get(type.getName(), obj.getClass().getName())
					//   dest.setterName( obj );
					// }
					// return dest;
					sb.append(FCS.get("\n{tab}Object obj = null;", StringUtils.repeat(' ', 8)).toString());
					sb.append(FCS.get("\n{tab}tet.GroovyProxy px = null;", StringUtils.repeat(' ', 8)).toString());
					for (Entry<String, Method> entry : toSetter.entrySet()) {
						String fieldName = entry.getKey();
						Method setter = entry.getValue();
						Class<?> setterParamType = setter.getParameterTypes()[0];
						sb.append(
								FCS.get("\n{tab}obj = src.get(\"{fieldName}\");", StringUtils.repeat(' ', 8), fieldName)
										.toString());
						//TODO
						sb.append(FCS.get("\n{tab}if (obj !=null ){", StringUtils.repeat(' ', 8)).toString());
						sb.append(
								FCS.get("\n{tab}px = tet.GroovyProxy.get( {setterClass}.class, obj.getClass().getName());",
										StringUtils.repeat(' ', 8 + 4), setterParamType.getName()).toString());
						sb.append(FCS.get("\n{tab}//obj = px.copy( null, null );", StringUtils.repeat(' ', 8 + 4))
								.toString());
						sb.append(FCS.get("\n{tab}}", StringUtils.repeat(' ', 8)).toString());
						sb.append(FCS.get("\n{tab}dest.{setterName}( ({setterClass}) obj );",
								StringUtils.repeat(' ', 8), setter.getName(), setterParamType.getName()).toString());
					}
					sb.append(FCS.get("\n{tab}return (T) dest;", StringUtils.repeat(' ', 8)).toString());
				} else {// copy method body, for not map convert
					for (Entry<String, Method> entry : toSetter.entrySet()) {
						String fieldName = entry.getKey();
						Method setter = entry.getValue();
						Method getter = fromGetter.get(fieldName);
						Class<?> setterParamType = setter.getParameterTypes()[0];
						Class<?> getterReturnType = getter != null ? getter.getReturnType() : null;
						if (getter == null) {
							// if not  getter for setter print a warning: Can't find getter from class fromClassName for toClassName.setterName.
							sb.append(
									FCS.get("\n{tab}org.iff.infra.util.Logger.warn(\"Can't find getter from class {fromClass} for {toClass}.{setter}()\");",
											StringUtils.repeat(' ', 8), from.getSimpleName(), to.getSimpleName(),
											setter.getName()).toString());
						} else if (setterParamType.getName().equals(getterReturnType.getName())) {// if setter and getter has same value type.
							boolean notNeedConvert = false;
							// 1. if the type is List type, and the ParameterType type is not the java basic type, then need to output the loop to convert each other
							if (List.class.isAssignableFrom(setterParamType)) {
								// 1.1 if ParameterType is not the java basic type
								if (TypeConvertHelper.me().get(setterParamType.getName()) != null) {
									Class<?> type = getParameter0WildcardType(setter);
									if (type == null) {
										Logger.debug(
												"1. if the type is List type, and ParameterType is not the java basic type, and WITHOUT WildcardType.");
										notNeedConvert = true;
									} else {
										Logger.debug(
												"1. if the type is List type, and ParameterType is not the java basic type, and CONTAINS WildcardType.");
										String s = ""//
												+ "\n        java.util.List gets = src.{getter}();"//
												+ "\n        java.util.List sets = new {defaultListClass}();"//
												+ "\n        if ( gets == null || gets.isEmpty() ) {"//
												+ "\n            for ( Object obj : gets ) {"//
												+ "\n                if ( obj == null ) {"//
												+ "\n                    continue;"//
												+ "\n                }"//
												+ "\n                tet.GroovyProxy px = tet.GroovyProxy.get({typeName}, obj.getClass().getName());"//
												+ "\n                Object tmp = copy( {typeName}.class, obj );"//
												+ "\n                sets.add( tmp );"//
												+ "\n            }"//
												+ "\n            dest.{setter}( sets );"//
												+ "\n        }"//
												;
										sb.append(FCS.get(s, getter.getName(), getDefaultListClass(setterParamType),
												type.getName(), type.getName(), setter.getName()).toString());
									}
								} else {
									notNeedConvert = true;
								}
								if (notNeedConvert) {
									sb.append('\n').append(StringUtils.repeat(' ', 8))//
											// dest.setterName( src.getterName() ); 
											.append("dest.").append(setter.getName()).append("( src.")
											.append(getter.getName()).append("() );");
								}
							} else if (Map.class.isAssignableFrom(setterParamType)
									|| Map.class.isAssignableFrom(getterReturnType)) {
								// 2. if the type is Map type, and the ParameterType type is not the java basic type, then need to output the loop to convert each other
								// GroovyProxy.get(type.getName(), obj.getClass().getName()).copy(new type.getName(), src);
								// Object obj = src.getterName();
								// dest.setterName( null );
								sb.append('\n').append(StringUtils.repeat(' ', 8))//
										.append("tet.GroovyProxy.get(")
										.append(Map.class.isAssignableFrom(setterParamType) ? Map.class.getName()
												: setterParamType.getName())
										.append(", ").append(Map.class.isAssignableFrom(getterReturnType)
												? Map.class.getName() : getterReturnType.getName())
										.append(" );");
								sb.append('\n').append(StringUtils.repeat(' ', 8))//
										.append("Object obj = src.").append(getter.getName()).append("();");
								sb.append('\n').append(StringUtils.repeat(' ', 8))//
										.append("dest.").append(setter.getName()).append("( null );");
							} else {
								sb.append('\n').append(StringUtils.repeat(' ', 8))//
										// dest.setterName( src.getterName() );
										.append("dest.").append(setter.getName()).append("( src.")
										.append(getter.getName()).append("() );");
							}
						} else {// the setter and getter has different type
							// 1. get converter from the POVO mapping
							{

							}
							// 2. get converter from TypeConvertHelper
							{

							}
							// 3. out put warning: can't convert getter type to setter type
							{

							}
						}
					}
					sb.append(FCS.get("\n{tab}return (T) dest;", StringUtils.repeat(' ', 8)).toString());
				}
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("}");
			}
			{// class end
				sb.append("\n}");
			}
		}
		return sb.toString();
	}

	//	public static String genGroovyProxyClass(Class<?> clazz) throws Exception {
	//		StringBuilder sb = new StringBuilder(1024);
	//
	//		String packageName = clazz.getName().substring(0, clazz.getName().length() - clazz.getSimpleName().length());
	//		packageName = packageName.length() > 0 ? packageName.substring(0, packageName.length() - 1) : packageName;
	//
	//		System.out.println(packageName);
	//
	//		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
	//
	//		for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
	//			Method read = propertyDescriptor.getReadMethod();
	//			Method write = propertyDescriptor.getWriteMethod();
	//			System.out.println(propertyDescriptor.getName());
	//			if (read != null) {
	//				if (Modifier.isPublic(read.getModifiers()) || Modifier.isProtected(read.getModifiers())) {
	//					System.out.println(read);
	//				}
	//			}
	//			if (write != null) {
	//				if (Modifier.isPublic(write.getModifiers()) || Modifier.isProtected(write.getModifiers())) {
	//					System.out.println(write);
	//				}
	//			}
	//		}
	//
	//		Map<String, Method> setterMap = new LinkedHashMap<String, Method>();
	//		Map<String, Method> getterMap = new LinkedHashMap<String, Method>();
	//		{
	//			Class<?> tmpClass = clazz;
	//			while (tmpClass != Object.class && !tmpClass.isInterface()) {
	//				Method[] methods = tmpClass.getMethods();
	//				for (Method m : methods) {
	//					String name = m.getName();
	//					if (name.startsWith("set") && name.length() > 3) {
	//						String fieldName = StringUtils.uncapitalize(name.substring(3));
	//						if (setterMap.containsKey(fieldName)) {
	//							continue;
	//						}
	//						Class<?> dc = m.getParameterTypes().length == 1 ? m.getParameterTypes()[0] : null;
	//						if (dc == null) {
	//							continue;
	//						}
	//						setterMap.put(fieldName, m);
	//					} else if (name.startsWith("get") && name.length() > 3) {
	//						String fieldName = StringUtils.uncapitalize(name.substring(3));
	//						if (getterMap.containsKey(fieldName)) {
	//							continue;
	//						}
	//						Class<?> dc = m.getReturnType();
	//						if (dc == null) {
	//							continue;
	//						}
	//						getterMap.put(fieldName, m);
	//					} else if (name.startsWith("is")
	//							&& (m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class)
	//							&& name.length() > 2) {
	//						String fieldName = StringUtils.uncapitalize(name.substring(2));
	//						if (getterMap.containsKey(fieldName)) {
	//							continue;
	//						}
	//						getterMap.put(fieldName, m);
	//					}
	//				}
	//				tmpClass = tmpClass.getSuperclass();
	//			}
	//		}
	//
	//		{// gen code
	//
	//			{// package and class
	//				if (!StringUtils.isBlank(packageName)) {
	//					sb.append('\n').append("package ").append(packageName).append(';');
	//				}
	//				sb.append('\n').append("public class ").append(clazz.getSimpleName()).append("_TcProxy")
	//						.append(" extends ").append(clazz.getName()).append('{');
	//			}
	//			{// private map property
	//				sb.append('\n').append(StringUtils.repeat(' ', 4)).append(
	//						"private transient java.util.Map __tc_value__ = org.iff.infra.util.MapHelper.toMap(\"id\",123);");
	//				sb.append('\n').append(StringUtils.repeat(' ', 4)).append(
	//						"private transient java.util.Map<String, Inteter> __tc_mark__ = new java.util.HashMap<String, Inteter>();");
	//			}
	//			for (Entry<String, Method> entry : getterMap.entrySet()) {
	//				String fieldName = entry.getKey();
	//				Method getter = entry.getValue();
	//				Method setter = setterMap.remove(fieldName);
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("@groovy.transform.CompileStatic");
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("public ").append(getter.getReturnType().getName()).append(' ').append(getter.getName())
	//						.append("() {");
	//				{//getter block
	//					sb.append('\n').append(StringUtils.repeat(' ', 8))//
	//							.append("if (__tc_mark__.get(\"").append(fieldName).append("\") == null) {");
	//					{
	//						sb.append('\n').append(StringUtils.repeat(' ', 12))//
	//								.append("__tc_mark__.set(\"").append(fieldName).append("\", 1);");
	//						if (setter != null) {
	//							sb.append('\n').append(StringUtils.repeat(' ', 12))//
	//									.append("super.").append(setter.getName()).append("((")
	//									.append(setter.getParameterTypes()[0].getName()).append(")")
	//									.append("__tc_value__.get(\"").append(fieldName).append("\"));");
	//						}
	//					}
	//					sb.append('\n').append(StringUtils.repeat(' ', 8))//
	//							.append("}");
	//					sb.append('\n').append(StringUtils.repeat(' ', 8))//
	//							.append("return supper.").append(getter.getName()).append("();");
	//				}
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("}");
	//				if (setter == null) {
	//					continue;
	//				}
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("@groovy.transform.CompileStatic");
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("public ").append(setter.getReturnType().getName()).append(' ').append(setter.getName())
	//						.append("(").append(setter.getParameterTypes()[0].getName()).append(" ").append(fieldName)
	//						.append(")").append(" {");
	//				{//setter block
	//					sb.append('\n').append(StringUtils.repeat(' ', 8))//
	//							.append("__tc_value__.set(\"").append(fieldName).append("\", ").append(fieldName)
	//							.append(");");
	//					sb.append('\n').append(StringUtils.repeat(' ', 8))//
	//							.append("supper.").append(setter.getName()).append("(").append(fieldName).append(");");
	//				}
	//				sb.append('\n').append(StringUtils.repeat(' ', 4))//
	//						.append("}");
	//			}
	//			{// class end
	//				sb.append("\n}");
	//			}
	//		}
	//
	//		return sb.toString();
	//	}

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

	private static String getDefaultListClass(Class<?> clazz) {
		if (clazz != null && List.class.isAssignableFrom(clazz)) {
			if (hasDefaultConstructor(clazz)) {
				return clazz.getName();
			}
			if (ArrayList.class.isAssignableFrom(clazz)) {
				return ArrayList.class.getName();
			}
		}
		return null;
	}

	private static String getDefaultMapClass(Class<?> clazz) {
		if (clazz != null && Map.class.isAssignableFrom(clazz)) {
			if (hasDefaultConstructor(clazz)) {
				return clazz.getName();
			}
			if (LinkedHashMap.class.isAssignableFrom(clazz)) {
				return LinkedHashMap.class.getName();
			}
		}
		return null;
	}

	private static Class<?> getParameter0WildcardType(Method method) {
		try {
			Type[] types = method.getGenericParameterTypes();
			if (types.length < 1) {
				return null;
			}
			if (ParameterizedType.class.isInstance(types[0])) {
				Type[] args = ((ParameterizedType) types[0]).getActualTypeArguments();
				if (WildcardType.class.isInstance(args[0])) {
					if (args[0] instanceof Class) {
						Class<?> clazz = (Class<?>) args[0];
						if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
							return clazz;
						}
						Logger.warn(FCS.get("The method [{0}] parameter 0 WildcardType CANNOT be interface or abstract",
								method));
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	private static WildcardType getReturnWildcardType(Method method) {
		try {
			Type type = method.getGenericReturnType();
			if (ParameterizedType.class.isInstance(type)) {
				Type[] args = ((ParameterizedType) type).getActualTypeArguments();
				if (WildcardType.class.isInstance(args[0])) {
					return (WildcardType) args[0];
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

}
