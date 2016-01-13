import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.ReflectHelper;

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

			genGroovyProxyClass(ModelProperty.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String genGroovyProxyClass(Class<?> clazz) throws Exception {
		StringBuilder sb = new StringBuilder(1024);

		String packageName = clazz.getName().substring(0, clazz.getCanonicalName().length());
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

		Method[] methods = clazz.getDeclaredMethods();

		return sb.toString();
	}
}
