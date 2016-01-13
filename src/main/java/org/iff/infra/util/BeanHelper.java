/*******************************************************************************
 * Copyright (c) 2013-1-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2013-1-31
 */
@SuppressWarnings("unchecked")
public class BeanHelper {

	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static final Map<String, Map<String, Method>> cacheMehtod = new HashMap<String, Map<String, Method>>();
	private static final Map<String, Map<String, Field>> cacheField = new HashMap<String, Map<String, Field>>();
	private static final Map<Object, MethodAccess> cacheMethodAccess = new HashMap<Object, MethodAccess>();

	private static final ObjectMapper mapper = new ObjectMapper()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
					DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disableDefaultTyping()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	static {
		mapper.setVisibility(mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		mapper.registerModule(new SimpleModule().addSerializer(java.util.Date.class, new DateSerializer())
				.addDeserializer(java.util.Date.class, new DateDeserializer()));
	}

	public static class DateSerializer extends JsonSerializer<java.util.Date> {
		public void serialize(java.util.Date date, JsonGenerator jg, SerializerProvider paramSerializerProvider)
				throws IOException, JsonProcessingException {
			jg.writeString(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
		}
	}

	public static class DateDeserializer extends JsonDeserializer<java.util.Date> {
		public java.util.Date deserialize(JsonParser jp, DeserializationContext dc)
				throws IOException, JsonProcessingException {
			String date = null;
			try {
				date = jp.getText();
				return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMM/dd HH:mm:ss",
						"yyyyMM/dd");
			} catch (Exception e) {
				Logger.warn(FCS.get("Input value {0} can't not format to Date", date));
			}
			return null;
		}
	}

	private BeanHelper() {
	}

	public static <T> T copyProperties(Object dest, Object orig) {
		if (orig == null) {
			return (T) null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
			mapper.writeValue(baos, orig);
			mapper.readerForUpdating(dest).readValue(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) dest;
	}

	public static <T> T copyProperties(Class<T> clazz, Object orig) {
		if (orig == null) {
			return (T) null;
		}
		Object fromJson = GSON.fromJson(GSON.toJsonTree(orig), clazz);
		return (T) fromJson;
	}

	//	private static <T> T copyGetSetProperties(Object dest, Object orig) {
	//		TypeConvert destTc = TypeConvertHelper.me().get(dest.getClass().getName());
	//		TypeConvert origTc = TypeConvertHelper.me().get(orig.getClass().getName());
	//		if ("null".equals(destTc.getName()) && "null".equals(origTc.getName())) {
	//			if (dest instanceof Map && orig instanceof Map) {
	//				return (T) MapHelper.combine((Map) dest, (Map) orig);
	//			}else if(dest instanceof List){
	//				
	//			}
	//		} else {
	//			return (T) dest;
	//		}
	//	}

	private static <T> T copyPlainObjectProperties(Object dest, Object orig) {
		String destClassName = dest.getClass().getName();
		String origClassName = orig.getClass().getName();
		{
			Map<String, Method> destMethod = cacheMehtod.get(destClassName);
			Map<String, Method> origMethod = cacheMehtod.get(origClassName);
			for (Entry<String, Method> entry : destMethod.entrySet()) {
				if (entry.getKey().charAt(0) != 's') {
					continue;
				}
				String setterName = "g" + entry.getKey().substring(1);
				Method setter = origMethod.get(setterName);
				if (setter == null) {
					continue;
				}

			}
		}

		return (T) dest;
	}

	private static boolean isPlainClass(Class<?> clazz) {
		if (cacheMehtod.containsKey(clazz.getName())) {
			return true;
		}
		Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();
		{
			while (clazz != Object.class && !clazz.isInterface()) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field f : fields) {
					if (!fieldMap.containsKey(f.getName())) {
						fieldMap.put(f.getName(), f);
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		Map<String, Method> methodMap = new LinkedHashMap<String, Method>();
		{
			while (clazz != Object.class && !clazz.isInterface()) {
				Method[] methods = clazz.getMethods();
				for (Method m : methods) {
					String name = m.getName();
					if (!methodMap.containsKey(name)) {
						if (name.startsWith("set") && name.length() > 3) {
							Class<?> dc = m.getParameterTypes().length == 1 ? m.getParameterTypes()[0] : null;
							Type type = m.getGenericParameterTypes().length == 1 ? m.getGenericParameterTypes()[0]
									: null;
							if (dc != null && (isPrimitive(dc) || isPrimitiveArray(dc) || isSimpleType(dc)
									|| isSimpleTypeArray(dc) || (type != null && isSimpleCollection(dc, type))
									|| (type != null && isSimpleMap(dc, type)))) {
								methodMap.put(name, m);
							} else {
								return false;
							}
						} else if (name.startsWith("get") && name.length() > 3) {
							Class<?> dc = m.getReturnType();
							Type type = m.getGenericReturnType();
							if (dc != null && (isPrimitive(dc) || isPrimitiveArray(dc) || isSimpleType(dc)
									|| isSimpleTypeArray(dc) || (type != null && isSimpleCollection(dc, type))
									|| (type != null && isSimpleMap(dc, type)))) {
								methodMap.put(name, m);
							} else {
								return false;
							}
						} else if (name.startsWith("is")
								&& (m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class)
								&& name.length() > 2) {
							methodMap.put(name, m);
						} else {
							return false;
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		cacheMehtod.put(clazz.getName(), methodMap);
		{
			for (Entry<String, Method> m : methodMap.entrySet()) {
				String name = m.getKey();
				name = name.startsWith("get") || name.startsWith("set") ? name.substring(3)
						: (name.startsWith("is") ? name.substring(2) : "");
				if (name.length() < 1) {
					continue;
				}
				if (fieldMap.containsKey(name) || fieldMap.containsKey((name = StringUtils.uncapitalize(name)))) {
					fieldMap.remove(name);
				}
			}
		}
		cacheField.put(clazz.getName(), fieldMap);
		return true;
	}

	private static boolean isPrimitive(Class<?> clazz) {
		return clazz.isPrimitive();
	}

	private static boolean isSimpleType(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz)
				|| java.util.Date.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)
				|| Character.class.isAssignableFrom(clazz) || Byte.class.isAssignableFrom(clazz);
	}

	private static boolean isPrimitiveArray(Class<?> clazz) {
		String name = clazz.getName();
		// byte[] --> [B, char[] --> [C, short[] --> [S, int[] --> [I, long[] --> [J, float[] --> [F, double[] --> [D, boolean[] -->[Z
		return clazz.isArray() && name.length() == 2 && name.charAt(0) == '[';
	}

	private static boolean isSimpleTypeArray(Class<?> clazz) {
		if (!clazz.isArray()) {
			return false;
		}
		String name = clazz.getName();
		// [Ljava.lang.Byte;, [Ljava.lang.Character;, [Ljava.lang.Short;, [Ljava.lang.Integer;, [Ljava.lang.Long;, [Ljava.lang.Float;, [Ljava.lang.Double;, [Ljava.lang.Boolean;
		try {
			name = name.substring(2, name.length() - 1);
			clazz = Class.forName(name);
			return isSimpleType(clazz);
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isSimpleCollection(Class<?> clazz, Type type) {
		if (!Collection.class.isAssignableFrom(clazz) && !ParameterizedType.class.isInstance(type)) {
			return false;
		}
		try {
			ParameterizedType pType = (ParameterizedType) type;
			type = pType.getActualTypeArguments()[0];
			WildcardType wType = (WildcardType) type;
			return isSimpleType((Class<?>) wType.getUpperBounds()[0]);
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isSimpleMap(Class<?> clazz, Type type) {
		if (!Map.class.isAssignableFrom(clazz) && !ParameterizedType.class.isInstance(type)) {
			return false;
		}
		try {
			ParameterizedType pType = (ParameterizedType) type;
			for (Type t : pType.getActualTypeArguments()) {
				WildcardType wType = (WildcardType) t;
				if (!isSimpleType((Class<?>) wType.getUpperBounds()[0])) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static void main2(String[] args) {
		//System.out.println(Arrays.asList(1,2,3,4,5).toArray(new byte[0]));
		System.out.println(Number.class.isAssignableFrom(int.class));
		System.out.println(isSimpleTypeArray(new Byte[0].getClass()));
		System.out.println((new byte[0]).getClass().getName());
		System.out.println((new byte[0]).getClass().getName());
		System.out.println((new char[0]).getClass().getName());
		System.out.println((new short[0]).getClass().getName());
		System.out.println((new int[0]).getClass().getName());
		System.out.println((new long[0]).getClass().getName());
		System.out.println((new float[0]).getClass().getName());
		System.out.println((new double[0]).getClass().getName());
		System.out.println((new boolean[0]).getClass().getName());
		System.out.println((new Byte[0]).getClass().getName());
		System.out.println((new Character[0]).getClass().getName());
		System.out.println((new Short[0]).getClass().getName());
		System.out.println((new Integer[0]).getClass().getName());
		System.out.println((new Long[0]).getClass().getName());
		System.out.println((new Float[0]).getClass().getName());
		System.out.println((new Double[0]).getClass().getName());
		System.out.println((new Boolean[0]).getClass().getName());
	}

	public static void main(String[] args) {
		B b = new B();
		C c = new C();
		b.print();
		java.util.Map map = new java.util.HashMap();
		{
			map.put("aa", "aa");
			map.put("b", "b");
			map.put("c", 11L);
			map.put("d", 11);
			map.put("e", new java.util.Date());
			map.put("f", "11");
			map.put("g", 111.1D);
			map.put("h", "2013-01-01 00:00:00");
			map.put("i", new java.util.Date());
			map.put("j", 111L);
			map.put("k", new java.sql.Date(new java.util.Date().getTime()));
		}
		{
			long start = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				BeanHelper.copyProperties(c, b);
			}
			System.out.println("Object2Object-ins:" + (System.currentTimeMillis() - start) / 10000.0);
		}
		{
			long start = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				BeanHelper.copyProperties(C.class, b);
			}
			System.out.println("Object2Object-class:" + (System.currentTimeMillis() - start) / 10000.0);
		}
		{
			long start = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				BeanHelper.copyProperties(c, map);
			}
			System.out.println("Map2Object:" + (System.currentTimeMillis() - start) / 10000.0);
		}
		{
			long start = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				BeanHelper.copyProperties(new java.util.HashMap(), b);
			}
			System.out.println("Object2Map:" + (System.currentTimeMillis() - start) / 10000.0);
		}
		{
			long start = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				BeanHelper.copyProperties(new java.util.HashMap(), map);
			}
			System.out.println("Map2Map:" + (System.currentTimeMillis() - start) / 10000.0);
		}
		b.k = new java.sql.Date(10000);
		System.out.println(BeanHelper.copyProperties(c, b));
		System.out.println(BeanHelper.copyProperties(new B(), c));
		System.out.println(BeanHelper.copyProperties(c, map));
		System.out.println(BeanHelper.copyProperties(new java.util.HashMap(), b));
		System.out.println(BeanHelper.copyProperties(new java.util.HashMap(), map));
	}

	static class A {
		private int a = 1;

		void print() {
			System.out.println(getClass());
		}
	}

	static class B extends A {
		private static String aa = "aa";
		// Same Type
		private String b = "b";
		private Long c = 11L;
		private int d = 11;
		private java.util.Date e = new java.util.Date();
		// String to Number
		private String f = "11";
		// Number to String
		private Double g = 111.1D;
		// String to Date
		private String h = "2013-01-01 00:00:00";
		// Date to String
		private java.util.Date i = new java.util.Date();
		// Other
		private Long j = 111L;
		private java.sql.Date k;

		public String toString() {
			return "B [b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h=" + h + ", i="
					+ i + ", j=" + j + ", k=" + k + "]";
		}
	}

	static class C {
		private static String aa = "";
		private int a;
		// Same Type
		private String b;
		private Long c;
		private int d;
		private java.util.Date e;
		// String to Number
		private long f;
		// Number to String
		private String g;
		// String to Date
		private java.util.Date h;
		// Date to String
		private String i;
		// Other
		private Integer j;
		private java.util.Date k;

		@Override
		public String toString() {
			return "C [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h="
					+ h + ", i=" + i + ", j=" + j + ", k=" + k + ", aa=" + aa + "]";
		}
	}
}
