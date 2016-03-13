/*******************************************************************************
 * Copyright (c) Nov 28, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 28, 2015
 */
public class TypeConvertHelper {
	private static final TypeConvertHelper me = new TypeConvertHelper();
	private Map<String, TypeConvert> typeConverts = new HashMap<String, TypeConvert>();

	protected TypeConvertHelper() {
		registTypeConvert();
	}

	public static TypeConvertHelper me() {
		return me;
	}

	private void registTypeConvert() {
		typeConverts.put("null", new NullTypeConvert());
		//
		typeConverts.put("boolean", new BooleanTypeConvert());
		typeConverts.put(Boolean.class.getName(), new BooleanTypeConvert());
		typeConverts.put("byte", new ByteTypeConvert());
		typeConverts.put(Byte.class.getName(), new ByteTypeConvert());
		typeConverts.put("short", new ShortTypeConvert());
		typeConverts.put(Short.class.getName(), new ShortTypeConvert());
		typeConverts.put("int", new IntegerTypeConvert());
		typeConverts.put(Integer.class.getName(), new IntegerTypeConvert());
		typeConverts.put("long", new LongTypeConvert());
		typeConverts.put(Long.class.getName(), new LongTypeConvert());
		typeConverts.put("float", new FloatTypeConvert());
		typeConverts.put(Float.class.getName(), new FloatTypeConvert());
		typeConverts.put("double", new DoubleTypeConvert());
		typeConverts.put(Double.class.getName(), new DoubleTypeConvert());
		typeConverts.put(BigDecimal.class.getName(), new BigDecimalTypeConvert());
		typeConverts.put(BigInteger.class.getName(), new BigIntegerTypeConvert());
		typeConverts.put("char", new CharTypeConvert());
		typeConverts.put(Character.class.getName(), new CharTypeConvert());
		typeConverts.put(CharSequence.class.getName(), new CharSequenceTypeConvert());
		typeConverts.put(String.class.getName(), new StringTypeConvert());
		typeConverts.put(java.util.Date.class.getName(), new DateTypeConvert());
		typeConverts.put(java.sql.Timestamp.class.getName(), new DateTypeConvert());
		typeConverts.put(java.sql.Date.class.getName(), new DateTypeConvert());
		typeConverts.put(java.sql.Time.class.getName(), new DateTypeConvert());
		//
		typeConverts.put("[Z", new BooleanArrTypeConvert());
		typeConverts.put("[Ljava.lang.Boolean;", new BooleanArrTypeConvert());
		typeConverts.put("[B", new ByteArrTypeConvert());
		typeConverts.put("[Ljava.lang.Byte;", new ByteArrTypeConvert());
		typeConverts.put("[S", new ShortArrTypeConvert());
		typeConverts.put("[Ljava.lang.Short;", new ShortArrTypeConvert());
		typeConverts.put("[I", new IntegerArrTypeConvert());
		typeConverts.put("[Ljava.lang.Integer;", new IntegerArrTypeConvert());
		typeConverts.put("[L", new LongArrTypeConvert());
		typeConverts.put("[Ljava.lang.Long;", new LongArrTypeConvert());
		typeConverts.put("[F", new FloatArrTypeConvert());
		typeConverts.put("[Ljava.lang.Float;", new FloatArrTypeConvert());
		typeConverts.put("[D", new DoubleArrTypeConvert());
		typeConverts.put("[Ljava.lang.Double;", new DoubleArrTypeConvert());
		typeConverts.put("[Ljava.math.BigDecimal;", new BigDecimalArrTypeConvert());
		typeConverts.put("[Ljava.math.BigInteger;", new BigIntegerArrTypeConvert());
		typeConverts.put("[C", new CharArrTypeConvert());
		typeConverts.put("[Ljava.lang.Character;", new CharArrTypeConvert());
		typeConverts.put("[Ljava.lang.CharSequence;", new CharSequenceArrTypeConvert());
		typeConverts.put("[Ljava.lang.String;", new StringArrTypeConvert());
		typeConverts.put("[Ljava.util.Date;", new DateArrTypeConvert());
		typeConverts.put("[Ljava.sql.Timestamp;", new DateArrTypeConvert());
		typeConverts.put("[Ljava.sql.Date;", new DateArrTypeConvert());
		typeConverts.put("[Ljava.sql.Time;", new DateArrTypeConvert());
		//
	}

	public TypeConvert get(String targetClassName) {
		TypeConvert typeConvert = typeConverts.get(targetClassName);
		return typeConvert == null ? typeConverts.get("null") : typeConvert;
	}

	public TypeConvert getNullable(String targetClassName) {
		TypeConvert typeConvert = typeConverts.get(targetClassName);
		return typeConvert;
	}

	public interface TypeConvert {
		String getName();

		Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType);
	}

	public class NullTypeConvert implements TypeConvert {
		public String getName() {
			return "null";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			return null;
		}
	}

	public class BooleanTypeConvert implements TypeConvert {
		public String getName() {
			return "boolean";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == boolean.class.getName()) ? false : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return sourceValue;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.intValue() == 0 ? false : true;
			} else {
				String s = sourceValue.toString();
				return "Y".equalsIgnoreCase(s) || "Yes".equalsIgnoreCase(s);
			}
		}
	}

	public class ByteTypeConvert implements TypeConvert {
		public String getName() {
			return "byte";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == byte.class.getName()) ? (byte) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (byte) 1 : (byte) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.byteValue();
			} else {
				String s = sourceValue.toString();
				return new Byte(s);
			}
		}
	}

	public class ShortTypeConvert implements TypeConvert {
		public String getName() {
			return "short";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == short.class.getName()) ? (short) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (short) 1 : (short) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.shortValue();
			} else {
				String s = sourceValue.toString();
				return new Short(s);
			}
		}
	}

	public class IntegerTypeConvert implements TypeConvert {
		public String getName() {
			return "int";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == int.class.getName()) ? (int) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (int) 1 : (int) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.intValue();
			} else {
				String s = sourceValue.toString();
				return new Integer(s);
			}
		}
	}

	public class LongTypeConvert implements TypeConvert {
		public String getName() {
			return "long";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == long.class.getName()) ? (long) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (long) 1 : (long) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.longValue();
			} else {
				String s = sourceValue.toString();
				return new Long(s);
			}
		}
	}

	public class FloatTypeConvert implements TypeConvert {
		public String getName() {
			return "float";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == float.class.getName()) ? (float) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (float) 1 : (float) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.floatValue();
			} else {
				String s = sourceValue.toString();
				return new Float(s);
			}
		}
	}

	public class DoubleTypeConvert implements TypeConvert {
		public String getName() {
			return "double";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == double.class.getName()) ? (double) 0 : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? (double) 1 : (double) 0;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.doubleValue();
			} else {
				String s = sourceValue.toString();
				return new Double(s);
			}
		}
	}

	public class BigDecimalTypeConvert implements TypeConvert {
		public String getName() {
			return BigDecimal.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0);
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return BigDecimal.valueOf(number.doubleValue());
			} else {
				String s = sourceValue.toString();
				return new BigDecimal(s);
			}
		}
	}

	public class BigIntegerTypeConvert implements TypeConvert {
		public String getName() {
			return BigInteger.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? BigInteger.valueOf(1) : BigInteger.valueOf(0);
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return BigInteger.valueOf(number.longValue());
			} else {
				String s = sourceValue.toString();
				return new BigInteger(s);
			}
		}
	}

	public class NumberTypeConvert implements TypeConvert {
		public String getName() {
			return "number";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			Constructor<?> doubleCons = null, intCons = null, stringCons = null;
			try {
				doubleCons = Class.forName(targetClassName).getConstructor(double.class);
			} catch (Exception e) {
			}
			try {
				intCons = Class.forName(targetClassName).getConstructor(int.class);
			} catch (Exception e) {
			}
			try {
				stringCons = Class.forName(targetClassName).getConstructor(String.class);
			} catch (Exception e) {
			}
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				try {
					return doubleCons == null
							? (intCons == null
									? (stringCons == null ? null
											: stringCons.newInstance(((Boolean) sourceValue) ? "0" : "1"))
									: intCons.newInstance(((Boolean) sourceValue) ? 0 : 1))
							: doubleCons.newInstance(((Boolean) sourceValue) ? 0D : 1D);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				return number.doubleValue();
			} else {
				String s = sourceValue.toString();
				return new Double(s);
			}
		}
	}

	public class CharTypeConvert implements TypeConvert {
		public String getName() {
			return "char";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return (targetClassName == char.class.getName()) ? ' ' : null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? 'Y' : 'N';
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				String s = number.toString();
				return s.length() == 1 ? s.charAt(0) : ((targetClassName == char.class.getName()) ? ' ' : null);
			} else {
				String s = sourceValue.toString();
				return s.length() == 1 ? s.charAt(0) : ((targetClassName == char.class.getName()) ? ' ' : null);
			}
		}
	}

	public class StringTypeConvert implements TypeConvert {
		public String getName() {
			return String.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return ((Boolean) sourceValue) ? "Y" : "N";
			} else {
				return (sourceValue instanceof Date) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sourceValue)
						: sourceValue.toString();
			}
		}
	}

	public class CharSequenceTypeConvert implements TypeConvert {
		public String getName() {
			return CharSequence.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			Constructor<?> stringCons = null;
			try {
				stringCons = Class.forName(targetClassName).getConstructor(String.class);
			} catch (Exception e) {
			}
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				try {
					return stringCons == null ? null : stringCons.newInstance(((Boolean) sourceValue) ? "Y" : "N");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				try {
					return stringCons == null ? null : stringCons.newInstance(sourceValue.toString());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class DateTypeConvert implements TypeConvert {
		public String getName() {
			return java.util.Date.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceCls.getName().equals(targetClassName) || (sourceValue instanceof java.util.Date
					&& java.util.Date.class.getName().equals(targetClassName))) {
				return sourceValue;
			} else if (sourceValue == null) {
				return null;
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				return null;
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				try {
					return Class.forName(targetClassName).getConstructor(long.class).newInstance(number.longValue());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				java.util.Date date = null;
				try {
					if (sourceValue instanceof java.util.Date) {
						date = (Date) sourceValue;
					} else {
						date = DateUtils.parseDate(sourceValue.toString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
								"yyyy/MMdd HH:mm:ss", "yyyy/MM/dd");
					}
				} catch (Exception e) {
				}
				try {
					if (date == null) {
						return null;
					} else if (sourceCls == java.sql.Time.class) {
						return new java.sql.Time(date.getTime());
					} else if (sourceCls == java.sql.Timestamp.class) {
						return new java.sql.Timestamp(date.getTime());
					} else if (sourceCls == java.sql.Date.class) {
						return new java.sql.Date(date.getTime());
					} else {
						return date;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class BooleanArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Z";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Boolean.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[Z")) {
					return Arrays.copyOf((boolean[]) sourceValue, ((boolean[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Boolean[]) sourceValue, ((Boolean[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[Z") || sourceCls.getName().equals("[Ljava.lang.Boolean;")) {
				if (targetClassName.equals("[Z")) {
					boolean[] arr = new boolean[((Boolean[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Boolean[]) sourceValue)[i] == null ? false : ((Boolean[]) sourceValue)[i];
					}
					return arr;
				} else {
					Boolean[] arr = new Boolean[((boolean[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((boolean[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[Z")) {
					boolean[] arr = new boolean[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Boolean) tc.convert(boolean.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Boolean[] arr = new Boolean[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Boolean) tc.convert(Boolean.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[Z")) {
					boolean[] arr = new boolean[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Boolean) tc.convert(boolean.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Boolean[] arr = new Boolean[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Boolean) tc.convert(Boolean.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[Z")) {
					return new boolean[] {
							(Boolean) tc.convert(boolean.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Boolean[] {
							(Boolean) tc.convert(Boolean.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class ByteArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[B";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Byte.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[B")) {
					return Arrays.copyOf((byte[]) sourceValue, ((byte[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Byte[]) sourceValue, ((Byte[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[B") || sourceCls.getName().equals("[Ljava.lang.Byte;")) {
				if (targetClassName.equals("[B")) {
					byte[] arr = new byte[((Byte[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Byte[]) sourceValue)[i] == null ? 0 : ((Byte[]) sourceValue)[i];
					}
					return arr;
				} else {
					Byte[] arr = new Byte[((byte[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((byte[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[B")) {
					byte[] arr = new byte[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Byte) tc.convert(byte.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Byte[] arr = new Byte[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Byte) tc.convert(Byte.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[B")) {
					byte[] arr = new byte[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Byte) tc.convert(byte.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Byte[] arr = new Byte[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Byte) tc.convert(Byte.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[B")) {
					return new byte[] { (Byte) tc.convert(byte.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Byte[] { (Byte) tc.convert(Byte.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class ShortArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[S";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Short.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[S")) {
					return Arrays.copyOf((short[]) sourceValue, ((short[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Short[]) sourceValue, ((Short[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[S") || sourceCls.getName().equals("[Ljava.lang.Short;")) {
				if (targetClassName.equals("[S")) {
					short[] arr = new short[((Short[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Short[]) sourceValue)[i] == null ? 0 : ((Short[]) sourceValue)[i];
					}
					return arr;
				} else {
					Short[] arr = new Short[((short[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((short[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[S")) {
					short[] arr = new short[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Short) tc.convert(short.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Short[] arr = new Short[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Short) tc.convert(Short.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[S")) {
					short[] arr = new short[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Short) tc.convert(short.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Short[] arr = new Short[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Short) tc.convert(Short.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[S")) {
					return new short[] {
							(Short) tc.convert(short.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Short[] {
							(Short) tc.convert(Short.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class IntegerArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[I";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Integer.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[I")) {
					return Arrays.copyOf((int[]) sourceValue, ((int[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Integer[]) sourceValue, ((Integer[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[I") || sourceCls.getName().equals("[Ljava.lang.Integer;")) {
				if (targetClassName.equals("[I")) {
					int[] arr = new int[((Integer[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Integer[]) sourceValue)[i] == null ? 0 : ((Integer[]) sourceValue)[i];
					}
					return arr;
				} else {
					Integer[] arr = new Integer[((int[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((int[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[I")) {
					int[] arr = new int[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Integer) tc.convert(int.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Integer[] arr = new Integer[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Integer) tc.convert(Integer.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[I")) {
					int[] arr = new int[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Integer) tc.convert(int.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Integer[] arr = new Integer[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Integer) tc.convert(Integer.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[I")) {
					return new int[] { (Integer) tc.convert(int.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Integer[] {
							(Integer) tc.convert(Integer.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class LongArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[J";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Long.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[J")) {
					return Arrays.copyOf((long[]) sourceValue, ((long[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Long[]) sourceValue, ((Long[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[J") || sourceCls.getName().equals("[Ljava.lang.Long;")) {
				if (targetClassName.equals("[J")) {
					long[] arr = new long[((Long[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Long[]) sourceValue)[i] == null ? 0 : ((Long[]) sourceValue)[i];
					}
					return arr;
				} else {
					Long[] arr = new Long[((long[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((long[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[J")) {
					long[] arr = new long[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Long) tc.convert(long.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Long[] arr = new Long[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Long) tc.convert(Long.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[J")) {
					long[] arr = new long[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Long) tc.convert(long.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Long[] arr = new Long[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Long) tc.convert(Long.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[J")) {
					return new long[] { (Long) tc.convert(long.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Long[] { (Long) tc.convert(Long.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class FloatArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[F";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Float.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[F")) {
					return Arrays.copyOf((float[]) sourceValue, ((float[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Float[]) sourceValue, ((Float[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[F") || sourceCls.getName().equals("[Ljava.lang.Float;")) {
				if (targetClassName.equals("[F")) {
					float[] arr = new float[((Float[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Float[]) sourceValue)[i] == null ? 0 : ((Float[]) sourceValue)[i];
					}
					return arr;
				} else {
					Float[] arr = new Float[((float[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((float[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[F")) {
					float[] arr = new float[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Float) tc.convert(float.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Float[] arr = new Float[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Float) tc.convert(Float.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[F")) {
					float[] arr = new float[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Float) tc.convert(float.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Float[] arr = new Float[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Float) tc.convert(Float.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[F")) {
					return new float[] {
							(Float) tc.convert(float.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Float[] {
							(Float) tc.convert(Float.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class DoubleArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[D";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Double.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[D")) {
					return Arrays.copyOf((double[]) sourceValue, ((double[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Double[]) sourceValue, ((Double[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[D") || sourceCls.getName().equals("[Ljava.lang.Double;")) {
				if (targetClassName.equals("[D")) {
					double[] arr = new double[((Double[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Double[]) sourceValue)[i] == null ? 0 : ((Double[]) sourceValue)[i];
					}
					return arr;
				} else {
					Double[] arr = new Double[((double[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((double[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[D")) {
					double[] arr = new double[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Double) tc.convert(double.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Double[] arr = new Double[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Double) tc.convert(Double.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[D")) {
					double[] arr = new double[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Double) tc.convert(double.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Double[] arr = new Double[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Double) tc.convert(Double.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[D")) {
					return new double[] {
							(Double) tc.convert(double.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Double[] {
							(Double) tc.convert(Double.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class BigDecimalArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.math.BigDecimal;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(BigDecimal.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(getName())) {
				return Arrays.copyOf((BigDecimal[]) sourceValue, ((BigDecimal[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				BigDecimal[] arr = new BigDecimal[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (BigDecimal) tc.convert(BigDecimal.class.getName(), object.getClass(), sourceCls, null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				BigDecimal[] arr = new BigDecimal[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (BigDecimal) tc.convert(BigDecimal.class.getName(), object.getClass(), sourceCls, null);
					i++;
				}
				return arr;
			} else {
				return new BigDecimal[] {
						(BigDecimal) tc.convert(BigDecimal.class.getName(), sourceValue, sourceCls, sourceType) };
			}
		}
	}

	public class BigIntegerArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.math.BigInteger;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(BigInteger.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(getName())) {
				return Arrays.copyOf((BigInteger[]) sourceValue, ((BigInteger[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				BigInteger[] arr = new BigInteger[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (BigInteger) tc.convert(BigInteger.class.getName(), object.getClass(), sourceCls, null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				BigInteger[] arr = new BigInteger[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (BigInteger) tc.convert(BigInteger.class.getName(), object.getClass(), sourceCls, null);
					i++;
				}
				return arr;
			} else {
				return new BigInteger[] {
						(BigInteger) tc.convert(BigInteger.class.getName(), sourceValue, sourceCls, sourceType) };
			}
		}
	}

	public class NumberArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.lang.Number;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Number.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((Number[]) sourceValue), ((Number[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				Number[] arr = new Number[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (Number) tc.convert(Number.class.getName(), object.getClass(), sourceCls, null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				Number[] arr = new Number[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (Number) tc.convert(Number.class.getName(), object.getClass(), sourceCls, null);
					i++;
				}
				return arr;
			} else {
				try {
					String tName = targetClassName.substring(2, targetClassName.length() - 1);
					String sName = sourceCls.getName().substring(2, sourceCls.getName().length() - 1);
					Object arr = Array.newInstance(Class.forName(tName), 1);
					Array.set(arr, 0, tc.convert(tName, sourceValue, Class.forName(sName), sourceType));
					return arr;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class CharArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[C";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Character.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[C")) {
					return Arrays.copyOf((char[]) sourceValue, ((char[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Character[]) sourceValue, ((Character[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[C") || sourceCls.getName().equals("[Ljava.lang.Character;")) {
				if (targetClassName.equals("[C")) {
					char[] arr = new char[((Character[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Character[]) sourceValue)[i] == null ? 0 : ((Character[]) sourceValue)[i];
					}
					return arr;
				} else {
					Character[] arr = new Character[((char[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((char[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls.isArray()) {
				if (targetClassName.equals("[C")) {
					char[] arr = new char[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Character) tc.convert(char.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				} else {
					Character[] arr = new Character[Array.getLength(sourceValue)];
					for (int i = 0; i < arr.length; i++) {
						Object object = Array.get(sourceValue, i);
						arr[i] = (Character) tc.convert(Character.class.getName(), object.getClass(), sourceCls, null);
					}
					return arr;
				}
			} else if (sourceValue instanceof Collection<?>) {
				if (targetClassName.equals("[C")) {
					char[] arr = new char[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Character) tc.convert(char.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				} else {
					Character[] arr = new Character[((Collection<?>) sourceValue).size()];
					int i = 0;
					for (Object object : (Collection<?>) sourceValue) {
						arr[i] = (Character) tc.convert(Character.class.getName(), object.getClass(), sourceCls, null);
						i++;
					}
					return arr;
				}
			} else {
				if (targetClassName.equals("[C")) {
					return new char[] {
							(Character) tc.convert(char.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Character[] {
							(Character) tc.convert(Character.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	public class CharSequenceArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.lang.CharSequence;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(CharSequence.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((CharSequence[]) sourceValue), ((CharSequence[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				CharSequence[] arr = new CharSequence[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (CharSequence) tc.convert(CharSequence.class.getName(), object.getClass(), sourceCls,
							null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				CharSequence[] arr = new CharSequence[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (CharSequence) tc.convert(CharSequence.class.getName(), object.getClass(), sourceCls,
							null);
					i++;
				}
				return arr;
			} else {
				try {
					String tName = targetClassName.substring(2, targetClassName.length() - 1);
					String sName = sourceCls.getName().substring(2, sourceCls.getName().length() - 1);
					Object arr = Array.newInstance(Class.forName(tName), 1);
					Array.set(arr, 0, new CharSequenceTypeConvert().convert(tName, sourceValue, Class.forName(sName),
							sourceType));
					return arr;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class StringArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.lang.String;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(String.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((String[]) sourceValue), ((String[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				String[] arr = new String[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (String) tc.convert(String.class.getName(), object.getClass(), sourceCls, null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				String[] arr = new String[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (String) tc.convert(String.class.getName(), object.getClass(), sourceCls, null);
					i++;
				}
				return arr;
			} else {
				try {
					String tName = targetClassName.substring(2, targetClassName.length() - 1);
					String sName = sourceCls.getName().substring(2, sourceCls.getName().length() - 1);
					Object arr = Array.newInstance(Class.forName(tName), 1);
					Array.set(arr, 0,
							new StringTypeConvert().convert(tName, sourceValue, Class.forName(sName), sourceType));
					return arr;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class DateArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.util.Date;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Date.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((Date[]) sourceValue), ((Date[]) sourceValue).length);
			} else if (sourceCls.isArray()) {
				Date[] arr = new Date[Array.getLength(sourceValue)];
				for (int i = 0; i < arr.length; i++) {
					Object object = Array.get(sourceValue, i);
					arr[i] = (Date) tc.convert(Date.class.getName(), object.getClass(), sourceCls, null);
				}
				return arr;
			} else if (sourceValue instanceof Collection<?>) {
				Date[] arr = new Date[((Collection<?>) sourceValue).size()];
				int i = 0;
				for (Object object : (Collection<?>) sourceValue) {
					arr[i] = (Date) tc.convert(Date.class.getName(), object.getClass(), sourceCls, null);
					i++;
				}
				return arr;
			} else {
				try {
					String tName = targetClassName.substring(2, targetClassName.length() - 1);
					String sName = sourceCls.getName().substring(2, sourceCls.getName().length() - 1);
					Object arr = Array.newInstance(Class.forName(tName), 1);
					Array.set(arr, 0,
							new DateTypeConvert().convert(tName, sourceValue, Class.forName(sName), sourceType));
					return arr;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
