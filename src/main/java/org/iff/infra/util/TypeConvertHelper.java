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
import java.sql.Blob;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Convert a basic type to an other. 
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 28, 2015
 */
public class TypeConvertHelper {
	private static final TypeConvertHelper me = new TypeConvertHelper();
	private Map<String, TypeConvert> typeConverts = new HashMap<String, TypeConvert>();

	protected TypeConvertHelper() {
		registTypeConvert();
	}

	/**
	 * return default instance.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static TypeConvertHelper me() {
		return me;
	}

	/**
	 * resiter all type converter.
	 * 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
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
		typeConverts.put(java.sql.Clob.class.getName(), new ClobTypeConvert());
		typeConverts.put(java.sql.Blob.class.getName(), new BlobTypeConvert());
		//
		typeConverts.put(boolean[].class.getName(), new BooleanArrTypeConvert());
		typeConverts.put(Boolean[].class.getName(), new BooleanArrTypeConvert());
		typeConverts.put(byte[].class.getName(), new ByteArrTypeConvert());
		typeConverts.put(Byte[].class.getName(), new ByteArrTypeConvert());
		typeConverts.put(short[].class.getName(), new ShortArrTypeConvert());
		typeConverts.put(Short[].class.getName(), new ShortArrTypeConvert());
		typeConverts.put(int[].class.getName(), new IntegerArrTypeConvert());
		typeConverts.put(Integer[].class.getName(), new IntegerArrTypeConvert());
		typeConverts.put(long[].class.getName(), new LongArrTypeConvert());
		typeConverts.put(Long[].class.getName(), new LongArrTypeConvert());
		typeConverts.put(float[].class.getName(), new FloatArrTypeConvert());
		typeConverts.put(Float[].class.getName(), new FloatArrTypeConvert());
		typeConverts.put(double[].class.getName(), new DoubleArrTypeConvert());
		typeConverts.put(Double[].class.getName(), new DoubleArrTypeConvert());
		typeConverts.put(BigDecimal[].class.getName(), new BigDecimalArrTypeConvert());
		typeConverts.put(BigInteger[].class.getName(), new BigIntegerArrTypeConvert());
		typeConverts.put(char[].class.getName(), new CharArrTypeConvert());
		typeConverts.put(Character[].class.getName(), new CharArrTypeConvert());
		typeConverts.put(CharSequence[].class.getName(), new CharSequenceArrTypeConvert());
		typeConverts.put(String[].class.getName(), new StringArrTypeConvert());
		typeConverts.put(java.util.Date[].class.getName(), new DateArrTypeConvert());
		typeConverts.put(java.sql.Timestamp[].class.getName(), new DateArrTypeConvert());
		typeConverts.put(java.sql.Date[].class.getName(), new DateArrTypeConvert());
		typeConverts.put(java.sql.Time[].class.getName(), new DateArrTypeConvert());
		//
	}

	/**
	 * get type converter by target class name.
	 * @param targetClassName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public TypeConvert get(String targetClassName) {
		TypeConvert typeConvert = typeConverts.get(targetClassName);
		return typeConvert == null ? typeConverts.get("null") : typeConvert;
	}

	/**
	 * get nullable type converter by target class name.
	 * @param targetClassName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public TypeConvert getNullable(String targetClassName) {
		TypeConvert typeConvert = typeConverts.get(targetClassName);
		return typeConvert;
	}

	/**
	 * type converter interface.
	 * @author zhaochen
	 *
	 */
	public interface TypeConvert {
		/**
		 * return the type converter name.
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Jul 19, 2016
		 */
		String getName();

		/**
		 * convert source value to target class value.
		 * @param targetClassName
		 * @param sourceValue
		 * @param sourceCls
		 * @param sourceType
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Jul 19, 2016
		 */
		Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType);
	}

	/**
	 * Null type converter.
	 * @author zhaochen
	 */
	public class NullTypeConvert implements TypeConvert {
		public String getName() {
			return "null";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			return null;
		}
	}

	/**
	 * boolean type converter.
	 * @author zhaochen
	 */
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

	/**
	 * byte type converter.
	 * @author zhaochen
	 */
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

	/**
	 * short type converter.
	 * @author zhaochen
	 */
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

	/**
	 * integer type converter.
	 * @author zhaochen
	 */
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

	/**
	 * long type converter.
	 * @author zhaochen
	 */
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

	/**
	 * float type converter.
	 * @author zhaochen
	 */
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

	/**
	 * double type converter.
	 * @author zhaochen
	 */
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

	/**
	 * BigDecimal type converter.
	 * @author zhaochen
	 */
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

	/**
	 * BigInteger type converter.
	 * @author zhaochen
	 */
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

	/**
	 * Number type converter.
	 * @author zhaochen
	 */
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

	/**
	 * char type converter.
	 * @author zhaochen
	 */
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

	/**
	 * string type converter.
	 * @author zhaochen
	 */
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

	/**
	 * char sequence type converter.
	 * @author zhaochen
	 */
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

	/**
	 * clob type converter.
	 * @author zhaochen
	 */
	public class ClobTypeConvert implements TypeConvert {
		public String getName() {
			return Clob.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			SerialClob clob = null;
			try {
				clob = new SerialClob(new char[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return clob;
			} else {
				char[] cs = null;
				if (sourceValue instanceof char[]) {
					cs = (char[]) sourceValue;
				} else {
					TypeConvert tc = get(String.class.getName());
					String convert = (String) tc.convert(String.class.getName(), sourceValue, sourceCls, sourceType);
					cs = convert == null ? null : convert.toCharArray();
				}
				if (cs == null) {
					cs = new char[0];
				}
				try {
					clob = new SerialClob(cs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return clob;
			}
		}
	}

	/**
	 * blob type converter.
	 * @author zhaochen
	 */
	public class BlobTypeConvert implements TypeConvert {
		public String getName() {
			return Blob.class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			SerialBlob blob = null;
			try {
				blob = new SerialBlob(new byte[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sourceCls.getName().equals(targetClassName)) {
				return sourceValue;
			} else if (sourceValue == null) {
				return blob;
			} else {
				byte[] bs = null;
				if (sourceValue instanceof byte[]) {
					bs = (byte[]) bs;
				} else {
					TypeConvert tc = get(byte[].class.getName());
					bs = (byte[]) tc.convert(byte[].class.getName(), sourceValue, sourceCls, sourceType);
				}
				if (bs == null) {
					bs = new byte[0];
				}
				try {
					blob = new SerialBlob(bs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return blob;
			}
		}
	}

	/**
	 * date type converter.
	 * @author zhaochen
	 */
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
								"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd");
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

	/**
	 * boolean array type converter.
	 * @author zhaochen
	 */
	public class BooleanArrTypeConvert implements TypeConvert {
		public String getName() {
			return boolean[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Boolean.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(boolean[].class.getName())) {
					return Arrays.copyOf((boolean[]) sourceValue, ((boolean[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Boolean[]) sourceValue, ((Boolean[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(boolean[].class.getName())
					|| sourceCls.getName().equals(Boolean[].class.getName())) {
				if (targetClassName.equals(boolean[].class.getName())) {
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
				if (targetClassName.equals(boolean[].class.getName())) {
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
				if (targetClassName.equals(boolean[].class.getName())) {
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
				if (targetClassName.equals(boolean[].class.getName())) {
					return new boolean[] {
							(Boolean) tc.convert(boolean.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Boolean[] {
							(Boolean) tc.convert(Boolean.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * byte array type converter.
	 * @author zhaochen
	 */
	public class ByteArrTypeConvert implements TypeConvert {
		public String getName() {
			return byte[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Byte.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(byte[].class.getName())) {
					return Arrays.copyOf((byte[]) sourceValue, ((byte[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Byte[]) sourceValue, ((Byte[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(byte[].class.getName())
					|| sourceCls.getName().equals(Byte[].class.getName())) {
				if (targetClassName.equals(byte[].class.getName())) {
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
				if (targetClassName.equals(byte[].class.getName())) {
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
				if (targetClassName.equals(byte[].class.getName())) {
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
				if (targetClassName.equals(byte[].class.getName())) {
					return new byte[] { (Byte) tc.convert(byte.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Byte[] { (Byte) tc.convert(Byte.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * short array type converter.
	 * @author zhaochen
	 */
	public class ShortArrTypeConvert implements TypeConvert {
		public String getName() {
			return short[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Short.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(short[].class.getName())) {
					return Arrays.copyOf((short[]) sourceValue, ((short[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Short[]) sourceValue, ((Short[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(short[].class.getName())
					|| sourceCls.getName().equals(Short[].class.getName())) {
				if (targetClassName.equals(short[].class.getName())) {
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
				if (targetClassName.equals(short[].class.getName())) {
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
				if (targetClassName.equals(short[].class.getName())) {
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
				if (targetClassName.equals(short[].class.getName())) {
					return new short[] {
							(Short) tc.convert(short.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Short[] {
							(Short) tc.convert(Short.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * integer array type converter.
	 * @author zhaochen
	 */
	public class IntegerArrTypeConvert implements TypeConvert {
		public String getName() {
			return int[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Integer.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(int[].class.getName())) {
					return Arrays.copyOf((int[]) sourceValue, ((int[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Integer[]) sourceValue, ((Integer[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(int[].class.getName())
					|| sourceCls.getName().equals(Integer[].class.getName())) {
				if (targetClassName.equals(int[].class.getName())) {
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
				if (targetClassName.equals(int[].class.getName())) {
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
				if (targetClassName.equals(int[].class.getName())) {
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
				if (targetClassName.equals(int[].class.getName())) {
					return new int[] { (Integer) tc.convert(int.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Integer[] {
							(Integer) tc.convert(Integer.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * long array type converter.
	 * @author zhaochen
	 */
	public class LongArrTypeConvert implements TypeConvert {
		public String getName() {
			return long[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Long.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(long[].class.getName())) {
					return Arrays.copyOf((long[]) sourceValue, ((long[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Long[]) sourceValue, ((Long[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(long[].class.getName())
					|| sourceCls.getName().equals(Long[].class.getName())) {
				if (targetClassName.equals(long[].class.getName())) {
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
				if (targetClassName.equals(long[].class.getName())) {
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
				if (targetClassName.equals(long[].class.getName())) {
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
				if (targetClassName.equals(long[].class.getName())) {
					return new long[] { (Long) tc.convert(long.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Long[] { (Long) tc.convert(Long.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * float array type converter.
	 * @author zhaochen
	 */
	public class FloatArrTypeConvert implements TypeConvert {
		public String getName() {
			return float[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Float.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(float[].class.getName())) {
					return Arrays.copyOf((float[]) sourceValue, ((float[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Float[]) sourceValue, ((Float[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(float[].class.getName())
					|| sourceCls.getName().equals(Float[].class.getName())) {
				if (targetClassName.equals(float[].class.getName())) {
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
				if (targetClassName.equals(float[].class.getName())) {
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
				if (targetClassName.equals(float[].class.getName())) {
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
				if (targetClassName.equals(float[].class.getName())) {
					return new float[] {
							(Float) tc.convert(float.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Float[] {
							(Float) tc.convert(Float.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * double array type converter.
	 * @author zhaochen
	 */
	public class DoubleArrTypeConvert implements TypeConvert {
		public String getName() {
			return double[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Double.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(double[].class.getName())) {
					return Arrays.copyOf((double[]) sourceValue, ((double[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Double[]) sourceValue, ((Double[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(double[].class.getName())
					|| sourceCls.getName().equals(Double[].class.getName())) {
				if (targetClassName.equals(double[].class.getName())) {
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
				if (targetClassName.equals(double[].class.getName())) {
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
				if (targetClassName.equals(double[].class.getName())) {
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
				if (targetClassName.equals(double[].class.getName())) {
					return new double[] {
							(Double) tc.convert(double.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Double[] {
							(Double) tc.convert(Double.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * BigDecimal Array type converter.
	 * @author zhaochen
	 */
	public class BigDecimalArrTypeConvert implements TypeConvert {
		public String getName() {
			return BigDecimal[].class.getName();
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

	/**
	 * BigInteger Array type converter.
	 * @author zhaochen
	 */
	public class BigIntegerArrTypeConvert implements TypeConvert {
		public String getName() {
			return BigInteger[].class.getName();
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

	/**
	 * Number Array type converter.
	 * @author zhaochen
	 */
	public class NumberArrTypeConvert implements TypeConvert {
		public String getName() {
			return Number[].class.getName();
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

	/**
	 * char array type converter.
	 * @author zhaochen
	 */
	public class CharArrTypeConvert implements TypeConvert {
		public String getName() {
			return char[].class.getName();
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			TypeConvert tc = get(Character.class.getName());
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals(char[].class.getName())) {
					return Arrays.copyOf((char[]) sourceValue, ((char[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Character[]) sourceValue, ((Character[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals(char[].class.getName())
					|| sourceCls.getName().equals(Character[].class.getName())) {
				if (targetClassName.equals(char[].class.getName())) {
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
				if (targetClassName.equals(char[].class.getName())) {
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
				if (targetClassName.equals(char[].class.getName())) {
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
				if (targetClassName.equals(char[].class.getName())) {
					return new char[] {
							(Character) tc.convert(char.class.getName(), sourceValue, sourceCls, sourceType) };
				} else {
					return new Character[] {
							(Character) tc.convert(Character.class.getName(), sourceValue, sourceCls, sourceType) };
				}
			}
		}
	}

	/**
	 * char sequence array type converter.
	 * @author zhaochen
	 */
	public class CharSequenceArrTypeConvert implements TypeConvert {
		public String getName() {
			return CharSequence[].class.getName();
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

	/**
	 * string array type converter.
	 * @author zhaochen
	 */
	public class StringArrTypeConvert implements TypeConvert {
		public String getName() {
			return String[].class.getName();
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

	/**
	 * date array type converter.
	 * @author zhaochen
	 */
	public class DateArrTypeConvert implements TypeConvert {
		public String getName() {
			return java.util.Date[].class.getName();
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
