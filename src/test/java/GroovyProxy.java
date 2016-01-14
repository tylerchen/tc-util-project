import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.ReflectHelper;
import org.springframework.beans.BeanUtils;

import groovy.lang.GroovyClassLoader;
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
				+ "\npublic class ModelProperty_TcProxy extends ModelProperty{"//
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

			System.out.println(genGroovyProxyClass(ModelProperty.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String genGroovyProxyClass(Class<?> clazz) throws Exception {
		StringBuilder sb = new StringBuilder(1024);

		String packageName = clazz.getName().substring(0, clazz.getName().length() - clazz.getSimpleName().length());
		packageName = packageName.length() > 0 ? packageName.substring(0, packageName.length() - 1) : packageName;

		System.out.println(packageName);

		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

		for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
			Method read = propertyDescriptor.getReadMethod();
			Method write = propertyDescriptor.getWriteMethod();
			System.out.println(propertyDescriptor.getName());
			if (read != null) {
				if (Modifier.isPublic(read.getModifiers()) || Modifier.isProtected(read.getModifiers())) {
					System.out.println(read);
				}
			}
			if (write != null) {
				if (Modifier.isPublic(write.getModifiers()) || Modifier.isProtected(write.getModifiers())) {
					System.out.println(write);
				}
			}
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
						Class<?> dc = m.getParameterTypes().length == 1 ? m.getParameterTypes()[0] : null;
						if (dc == null) {
							continue;
						}
						setterMap.put(fieldName, m);
					} else if (name.startsWith("get") && name.length() > 3) {
						String fieldName = StringUtils.uncapitalize(name.substring(3));
						if (getterMap.containsKey(fieldName)) {
							continue;
						}
						Class<?> dc = m.getReturnType();
						if (dc == null) {
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

		{// gen code

			{// package and class
				if (!StringUtils.isBlank(packageName)) {
					sb.append('\n').append("package ").append(packageName).append(';');
				}
				sb.append('\n').append("public class ").append(clazz.getSimpleName()).append("_TcProxy")
						.append(" extends ").append(clazz.getName()).append('{');
			}
			{// private map property
				sb.append('\n').append(StringUtils.repeat(' ', 4)).append(
						"private transient java.util.Map __tc_value__ = org.iff.infra.util.MapHelper.toMap(\"id\",123);");
				sb.append('\n').append(StringUtils.repeat(' ', 4)).append(
						"private transient java.util.Map<String, Inteter> __tc_mark__ = new java.util.HashMap<String, Inteter>();");
			}
			for (Entry<String, Method> entry : getterMap.entrySet()) {
				String fieldName = entry.getKey();
				Method getter = entry.getValue();
				Method setter = setterMap.remove(fieldName);
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("@groovy.transform.CompileStatic");
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("public ").append(getter.getReturnType().getName()).append(' ').append(getter.getName())
						.append("() {");
				{//getter block
					sb.append('\n').append(StringUtils.repeat(' ', 8))//
							.append("if (__tc_mark__.get(\"").append(fieldName).append("\") == null) {");
					{
						sb.append('\n').append(StringUtils.repeat(' ', 12))//
								.append("__tc_mark__.set(\"").append(fieldName).append("\", 1);");
						if (setter != null) {
							sb.append('\n').append(StringUtils.repeat(' ', 12))//
									.append("super.").append(setter.getName()).append("((")
									.append(setter.getParameterTypes()[0].getName()).append(")")
									.append("__tc_value__.get(\"").append(fieldName).append("\"));");
						}
					}
					sb.append('\n').append(StringUtils.repeat(' ', 8))//
							.append("}");
					sb.append('\n').append(StringUtils.repeat(' ', 8))//
							.append("return supper.").append(getter.getName()).append("();");
				}
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("}");
				if (setter == null) {
					continue;
				}
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("@groovy.transform.CompileStatic");
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("public ").append(setter.getReturnType().getName()).append(' ').append(setter.getName())
						.append("(").append(setter.getParameterTypes()[0].getName()).append(" ").append(fieldName)
						.append(")").append(" {");
				{//setter block
					sb.append('\n').append(StringUtils.repeat(' ', 8))//
							.append("__tc_value__.set(\"").append(fieldName).append("\", ").append(fieldName)
							.append(");");
					sb.append('\n').append(StringUtils.repeat(' ', 8))//
							.append("supper.").append(setter.getName()).append("(").append(fieldName).append(");");
				}
				sb.append('\n').append(StringUtils.repeat(' ', 4))//
						.append("}");
			}
		}

		return sb.toString();
	}
}
