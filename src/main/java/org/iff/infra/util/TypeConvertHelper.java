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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 28, 2015
 */
public class TypeConvertHelper {
	private static final TypeConvertHelper me = new TypeConvertHelper();

	public static TypeConvertHelper me() {
		return me;
	}

	public interface TypeConvert {
		String getName();

		Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType);
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
			if (sourceCls.getName().equals(targetClassName)) {
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
					date = DateUtils.parseDate(sourceValue.toString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
							"yyyy/MMdd HH:mm:ss", "yyyy/MM/dd");
				} catch (Exception e) {
				}
				try {
					return date == null ? null
							: Class.forName(targetClassName).getConstructor(long.class).newInstance(date.getTime());
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
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[Z")) {
					return Arrays.copyOf((boolean[]) sourceValue, ((boolean[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Boolean[]) sourceValue, ((Boolean[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[Z") || sourceCls.getName().equals("[Ljava.lang.Boolean;")) {
				if (sourceCls.getName().equals("[Z")) {
					Boolean[] arr = new Boolean[((boolean[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Boolean[]) sourceValue)[i];
					}
					return arr;
				} else {
					boolean[] arr = new boolean[((Byte[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Boolean[]) sourceValue)[i] == null ? false : ((Boolean[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[Z")) {
					return new Boolean[] { number.intValue() == 0 ? false : true };
				} else {
					return new boolean[] { number.intValue() == 0 ? false : true };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[Z")) {
					return new Boolean[] { "Y".equalsIgnoreCase(s) || "Yes".equalsIgnoreCase(s) };
				} else {
					return new boolean[] { "Y".equalsIgnoreCase(s) || "Yes".equalsIgnoreCase(s) };
				}
			}
		}
	}

	public class ByteArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[B";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[B")) {
					return Arrays.copyOf((byte[]) sourceValue, ((byte[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Byte[]) sourceValue, ((Byte[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[B") || sourceCls.getName().equals("[Ljava.lang.Byte;")) {
				if (sourceCls.getName().equals("[B")) {
					Byte[] arr = new Byte[((byte[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((byte[]) sourceValue)[i];
					}
					return arr;
				} else {
					byte[] arr = new byte[((Byte[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Byte[]) sourceValue)[i] == null ? 0 : ((Byte[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[B")) {
					return new Byte[] { ((Boolean) sourceValue) ? (byte) 1 : (byte) 0 };
				} else {
					return new byte[] { ((Boolean) sourceValue) ? (byte) 1 : (byte) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[B")) {
					return new Byte[] { number.byteValue() };
				} else {
					return new byte[] { number.byteValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[B")) {
					return new Byte[] { new Byte(s) };
				} else {
					return new byte[] { new Byte(s) };
				}
			}
		}
	}

	public class ShortArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[S";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[S")) {
					return Arrays.copyOf((short[]) sourceValue, ((short[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Short[]) sourceValue, ((Short[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[S") || sourceCls.getName().equals("[Ljava.lang.Short;")) {
				if (sourceCls.getName().equals("[S")) {
					Short[] arr = new Short[((short[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((short[]) sourceValue)[i];
					}
					return arr;
				} else {
					short[] arr = new short[((Short[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Short[]) sourceValue)[i] == null ? 0 : ((Short[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[S")) {
					return new Short[] { ((Boolean) sourceValue) ? (short) 1 : (short) 0 };
				} else {
					return new short[] { ((Boolean) sourceValue) ? (short) 1 : (short) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[S")) {
					return new Short[] { number.shortValue() };
				} else {
					return new short[] { number.shortValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[S")) {
					return new Short[] { new Short(s) };
				} else {
					return new short[] { new Short(s) };
				}
			}
		}
	}

	public class IntegerArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[I";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[I")) {
					return Arrays.copyOf((int[]) sourceValue, ((int[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Integer[]) sourceValue, ((Integer[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[I") || sourceCls.getName().equals("[Ljava.lang.Integer;")) {
				if (sourceCls.getName().equals("[I")) {
					Integer[] arr = new Integer[((int[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((int[]) sourceValue)[i];
					}
					return arr;
				} else {
					int[] arr = new int[((Integer[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Integer[]) sourceValue)[i] == null ? 0 : ((Integer[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[I")) {
					return new Integer[] { ((Boolean) sourceValue) ? (int) 1 : (int) 0 };
				} else {
					return new int[] { ((Boolean) sourceValue) ? (int) 1 : (int) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[I")) {
					return new Integer[] { number.intValue() };
				} else {
					return new int[] { number.intValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[I")) {
					return new Integer[] { new Integer(s) };
				} else {
					return new int[] { new Integer(s) };
				}
			}
		}
	}

	public class LongArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[J";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[J")) {
					return Arrays.copyOf((long[]) sourceValue, ((long[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Long[]) sourceValue, ((Long[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[J") || sourceCls.getName().equals("[Ljava.lang.Long;")) {
				if (sourceCls.getName().equals("[J")) {
					Long[] arr = new Long[((long[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((long[]) sourceValue)[i];
					}
					return arr;
				} else {
					long[] arr = new long[((Long[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Long[]) sourceValue)[i] == null ? 0 : ((Long[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[J")) {
					return new Long[] { ((Boolean) sourceValue) ? (long) 1 : (long) 0 };
				} else {
					return new long[] { ((Boolean) sourceValue) ? (long) 1 : (long) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[J")) {
					return new Long[] { number.longValue() };
				} else {
					return new long[] { number.longValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[J")) {
					return new Long[] { new Long(s) };
				} else {
					return new long[] { new Long(s) };
				}
			}
		}
	}

	public class FloatArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[F";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[F")) {
					return Arrays.copyOf((float[]) sourceValue, ((float[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Float[]) sourceValue, ((Float[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[F") || sourceCls.getName().equals("[Ljava.lang.Float;")) {
				if (sourceCls.getName().equals("[F")) {
					Float[] arr = new Float[((float[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((float[]) sourceValue)[i];
					}
					return arr;
				} else {
					float[] arr = new float[((Float[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Float[]) sourceValue)[i] == null ? 0 : ((Float[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[F")) {
					return new Float[] { ((Boolean) sourceValue) ? (float) 1 : (float) 0 };
				} else {
					return new float[] { ((Boolean) sourceValue) ? (float) 1 : (float) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[F")) {
					return new Float[] { number.floatValue() };
				} else {
					return new float[] { number.floatValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[F")) {
					return new Float[] { new Float(s) };
				} else {
					return new float[] { new Float(s) };
				}
			}
		}
	}

	public class DoubleArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[D";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[D")) {
					return Arrays.copyOf((double[]) sourceValue, ((double[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Double[]) sourceValue, ((Double[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[D") || sourceCls.getName().equals("[Ljava.lang.Double;")) {
				if (sourceCls.getName().equals("[D")) {
					Double[] arr = new Double[((double[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((double[]) sourceValue)[i];
					}
					return arr;
				} else {
					double[] arr = new double[((Double[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Double[]) sourceValue)[i] == null ? 0 : ((Double[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[D")) {
					return new Double[] { ((Boolean) sourceValue) ? (double) 1 : (double) 0 };
				} else {
					return new double[] { ((Boolean) sourceValue) ? (double) 1 : (double) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				if (sourceCls.getName().equals("[D")) {
					return new Double[] { number.doubleValue() };
				} else {
					return new double[] { number.doubleValue() };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[D")) {
					return new Double[] { new Double(s) };
				} else {
					return new double[] { new Double(s) };
				}
			}
		}
	}

	public class NumberArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.lang.Number;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((Number[]) sourceValue), ((Number[]) sourceValue).length);
			} else {
				try {
					String tName = targetClassName.substring(2, targetClassName.length() - 1);
					String sName = sourceCls.getName().substring(2, sourceCls.getName().length() - 1);
					Object arr = Array.newInstance(Class.forName(tName), 1);
					Array.set(arr, 0,
							new NumberTypeConvert().convert(tName, sourceValue, Class.forName(sName), sourceType));
					return arr;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public class CharacterArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[C";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				if (targetClassName.equals("[C")) {
					return Arrays.copyOf((char[]) sourceValue, ((char[]) sourceValue).length);
				} else {
					return Arrays.copyOf((Character[]) sourceValue, ((Character[]) sourceValue).length);
				}
			} else if (sourceCls.getName().equals("[C") || sourceCls.getName().equals("[Ljava.lang.Character;")) {
				if (sourceCls.getName().equals("[C")) {
					Character[] arr = new Character[((char[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((char[]) sourceValue)[i];
					}
					return arr;
				} else {
					char[] arr = new char[((Character[]) sourceValue).length];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = ((Character[]) sourceValue)[i] == null ? 0 : ((Character[]) sourceValue)[i];
					}
					return arr;
				}
			} else if (sourceCls == boolean.class || sourceCls == Boolean.class) {
				if (sourceCls.getName().equals("[C")) {
					return new Character[] { ((Boolean) sourceValue) ? (char) 1 : (char) 0 };
				} else {
					return new char[] { ((Boolean) sourceValue) ? (char) 1 : (char) 0 };
				}
			} else if (Number.class.isAssignableFrom(sourceCls)) {
				Number number = (Number) sourceValue;
				String s = number.toString();
				if (sourceCls.getName().equals("[C")) {
					return new Character[] { s.charAt(0) };
				} else {
					return new char[] { s.charAt(0) };
				}
			} else {
				String s = sourceValue.toString();
				if (sourceCls.getName().equals("[C")) {
					return new Character[] { s.length() == 1 ? s.charAt(0) : null };
				} else {
					return new char[] { s.length() == 1 ? s.charAt(0) : ' ' };
				}
			}
		}
	}

	public class CharSequenceArrTypeConvert implements TypeConvert {
		public String getName() {
			return "[Ljava.lang.CharSequence;";
		}

		public Object convert(String targetClassName, Object sourceValue, Class<?> sourceCls, Type sourceType) {
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((CharSequence[]) sourceValue), ((CharSequence[]) sourceValue).length);
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
			if (sourceValue == null) {
				return null;
			} else if (sourceCls.getName().equals(targetClassName)) {
				return Arrays.copyOf(((String[]) sourceValue), ((String[]) sourceValue).length);
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

}
