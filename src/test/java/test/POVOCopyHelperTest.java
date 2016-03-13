/*******************************************************************************
 * Copyright (c) Feb 5, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.POVOCopyHelper;
import org.iff.infra.util.ReflectHelper;

import com.thoughtworks.xstream.XStream;

import groovy.lang.GroovyClassLoader;
import tet.ModelProperty;
import tet.ModelPropertyVO;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 5, 2016
 */
public class POVOCopyHelperTest {
	public static void main0(String[] args) {
		Method m = ReflectHelper.getMethod(tet.ModelPropertyVO.class, "setList", java.util.List.class.getName());
		System.out.println(m);
		//System.out.println(POVOCopyHelper.getDefaultClass(m.getParameterTypes()[0]));
		Object[] a = new Object[] { (int) 1 };
		System.out.println(a[0] instanceof Integer);
	}

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
			Map map = MapHelper.toMap("id", "test", "createTime", new Date(), "sort", 1, "isNull", "Y", "showName",
					"ttt", "dbName", "dbName", "isIndex", "isIndex", "name", "name", "modelid", "modelid",
					"defaultValue", "defaultValue", "type", "type", "size", "size");
			ModelProperty test = BeanHelper.copyProperties(ModelProperty.class, map);
			Logger.changeLevel("FOSS", "info");
			Logger.changeLevel("org.apache", "info");
			{
				ModelProperty proxy = (ModelProperty) clazz.newInstance();
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					proxy.getId();
				}
				System.out.print("Groovy Proxy:");
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(GsonHelper.toJsonString(proxy));
			}
			{
				long start = System.currentTimeMillis();
				Method method = ReflectHelper.getMethod(ModelProperty.class, "getId");
				for (int i = 0; i < 1000 * 1000; i++) {
					method.invoke(test);
				}
				System.out.print("ReflectHelper:");
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				long start = System.currentTimeMillis();
				ModelProperty mp = new ModelProperty();
				for (int i = 0; i < 1000 * 1000; i++) {
					mp.getId();
				}
				System.out.print("native:");
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				BeanHelper.setUsePOVOCopyHelper(false);
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					BeanHelper.copyProperties(ModelProperty.class, test);
				}
				System.out.print("BeanHelper:");
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				POVOCopyHelper.copyTo(map, ModelProperty.class);
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					POVOCopyHelper.copyTo(map, test);
				}
				System.out.print("POVOCopyHelper-from-map:");
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(new XStream().toXML(test));
			}
			{
				test.setSelf(BeanHelper.copyProperties(ModelProperty.class, test));
				test.setList(Arrays.asList(BeanHelper.copyProperties(ModelProperty.class, test)));
				Map tmp = new HashMap();
				POVOCopyHelper.copyTo(test, tmp);
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					POVOCopyHelper.copyTo(test, tmp);
				}
				System.out.print("POVOCopyHelper-to-map:");
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(new XStream().toXML(tmp));
			}
			{
				test.setSelf(BeanHelper.copyProperties(ModelProperty.class, map));
				test.setList(Arrays.asList(BeanHelper.copyProperties(ModelProperty.class, map)));
				ModelPropertyVO tmp = new ModelPropertyVO();
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					POVOCopyHelper.copyTo(test, tmp);
				}
				System.out.print("POVOCopyHelper-po-vo:");
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(new XStream().toXML(tmp));
			}
			{
				BeanHelper.setUsePOVOCopyHelper(true);
				test.setSelf(BeanHelper.copyProperties(ModelProperty.class, map));
				test.setList(Arrays.asList(BeanHelper.copyProperties(ModelProperty.class, map)));
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					BeanHelper.copyProperties(ModelPropertyVO.class, test);
				}
				System.out.print("BeanHelper-class-usePOVOCopyHelper:");
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				BeanHelper.setUsePOVOCopyHelper(true);
				test.setSelf(BeanHelper.copyProperties(ModelProperty.class, map));
				test.setList(Arrays.asList(BeanHelper.copyProperties(ModelProperty.class, map)));
				ModelPropertyVO tmp = new ModelPropertyVO();
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000 * 1000; i++) {
					BeanHelper.copyProperties(tmp, test);
				}
				System.out.print("BeanHelper-obj-usePOVOCopyHelper:");
				System.out.println(System.currentTimeMillis() - start);
			}
			{
				BeanHelper.setUsePOVOCopyHelper(false);
				ModelPropertyVO tmp = new ModelPropertyVO();
				test = BeanHelper.copyProperties(ModelProperty.class, map);
				long start = System.currentTimeMillis();
				try {
					for (int i = 0; i < 1000 * 1000; i++) {
						BeanUtils.copyProperties(tmp, test);
					}
				} catch (Exception e) {
				}
				System.out.print("BeanUtils:");
				System.out.println(System.currentTimeMillis() - start);
				System.out.println(new XStream().toXML(tmp));
			}
			Logger.changeLevel("FOSS", "debug");
			Logger.debug(new XStream().toXML(test));
			//System.out.println(genGroovyProxyClass(ModelProperty.class));
			//RSAHelper.decryptByDefaultKey("test".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
