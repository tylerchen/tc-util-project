/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 用于参数检查，并返回值，不符合的参数会抛出异常。
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-3-14
 */
@SuppressWarnings("unchecked")
public class PreCheckHelper {

	public static <T> T checkTrue(T expression, CharSequence message) {
		if (expression == null || !Boolean.TRUE.equals(expression)) {
			Exceptions.runtime(message, "FOSS-CHK-001");
		}
		return expression;
	}

	public static <T> T checkTrue(T expression) {
		return checkTrue(expression, "Check failed - this expression must be true.");
	}

	public static <T> T checkFalse(T expression, CharSequence message) {
		if (expression == null || !Boolean.FALSE.equals(expression)) {
			Exceptions.runtime(message, "FOSS-CHK-001");
		}
		return expression;
	}

	public static <T> T checkFalse(T expression) {
		return checkFalse(expression, "Check failed - this expression must be false.");
	}

	public static <T> T checkNull(T object, CharSequence message) {
		if (object != null) {
			Exceptions.runtime(message, "FOSS-CHK-002");
		}
		return object;
	}

	public static <T> T checkNull(T object) {
		return checkNull(object, "Check failed - the object argument must be null.");
	}

	public static <T> T checkNotNull(T object, CharSequence message) {
		if (object == null) {
			Exceptions.runtime(message, "FOSS-CHK-003");
		}
		return object;
	}

	public static <T> T checkNotNull(T object) {
		return checkNotNull(object, "Check failed - this argument is required; it must not be null.");
	}

	public static <T> T checkEmpty(T object, CharSequence message) {
		boolean checkFail = false;
		if (object == null) {
		} else if (object instanceof CharSequence) {
			checkFail = StringUtils.isNotEmpty((CharSequence) object);
		} else if (object instanceof Collection) {
			checkFail = !((Collection<?>) object).isEmpty();
		} else if (object instanceof Map) {
			checkFail = !((Map<?, ?>) object).isEmpty();
		} else if (object.getClass().isArray()) {
			checkFail = Array.getLength(object) > 0;
		} else {
			checkFail = true;
		}
		if (checkFail) {
			Exceptions.runtime(message, "FOSS-CHK-004");
		}
		return object;
	}

	public static <T> T checkEmpty(T object) {
		String message = "";
		if (object == null) {
		} else if (object instanceof CharSequence) {
			message = "Check failed - this CharSequence argument must be null or empty.";
		} else if (object instanceof Collection) {
			message = "Check failed - this Collection argument must be null or empty.";
		} else if (object instanceof Map) {
			message = "Check failed - this Map argument must be null or empty.";
		} else if (object.getClass().isArray()) {
			message = "Check failed - this Array argument must be null or empty.";
		} else {
			message = "Check failed - this argument must be null or empty.";
		}
		return checkEmpty(object, message);
	}

	public static <T> T checkNotEmpty(T object, CharSequence message) {
		boolean checkFail = false;
		if ((checkFail = object == null)) {
		} else if (object instanceof CharSequence) {
			checkFail = StringUtils.isEmpty((CharSequence) object);
		} else if (object instanceof Collection) {
			checkFail = ((Collection<?>) object).isEmpty();
		} else if (object instanceof Map) {
			checkFail = ((Map<?, ?>) object).isEmpty();
		} else if (object.getClass().isArray()) {
			checkFail = Array.getLength(object) < 1;
		}
		if (checkFail) {
			Exceptions.runtime(message, "FOSS-CHK-005");
		}
		return object;
	}

	public static <T> T checkNotEmpty(T object) {
		String message = "";
		if (object == null) {
			message = "Check failed - this argument is required; it must not be null.";
		} else if (object instanceof CharSequence) {
			message = "Check failed - this CharSequence argument must not empty.";
		} else if (object instanceof Collection) {
			message = "Check failed - this Collection argument must not empty.";
		} else if (object instanceof Map) {
			message = "Check failed - this Map argument must not empty.";
		} else if (object.getClass().isArray()) {
			message = "Check failed - this Array argument must not empty.";
		}
		return checkNotEmpty(object, message);
	}

	public static CharSequence checkBlank(CharSequence text, CharSequence message) {
		if (StringUtils.isBlank(text)) {
			Exceptions.runtime(message, "FOSS-CHK-006");
		}
		return text;
	}

	public static CharSequence checkBlank(CharSequence text) {
		return checkBlank(text, "Check failed - this CharSequence argument must be null or blank.");
	}

	public static CharSequence checkNotBlank(CharSequence text, CharSequence message) {
		if (StringUtils.isNotBlank(text)) {
			Exceptions.runtime(message, "FOSS-CHK-007");
		}
		return text;
	}

	public static CharSequence checkNotBlank(CharSequence text) {
		return checkNotBlank(text, "Check failed - this CharSequence argument must not be null or blank.");
	}

	public static String checkBeContained(String substring, String textToSearch, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) == -1) {
			Exceptions.runtime(message, "FOSS-CHK-008");
		}
		return substring;
	}

	public static String checkBeContained(String substring, String textToSearch) {
		return checkBeContained(substring, textToSearch,
				"Check failed - the first argument must be contained in the second argument.");
	}

	public static <T> T checkBeContained(T object, Collection<T> objects, CharSequence message) {
		if (object != null && objects != null && !objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-009");
		}
		return object;
	}

	public static <T> T checkBeContained(T object, Collection<T> objects) {
		return checkBeContained(object, objects,
				"Check failed - the first argument must be contained in the second argument.");
	}

	public static <T> T checkBeContained(T object, Map<T, ?> objects, CharSequence message) {
		if (object != null && objects != null && !objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-009");
		}
		return object;
	}

	public static <T> T checkBeContained(T object, Map<T, ?> objects) {
		return checkBeContained(object, objects,
				"Check failed - the first argument must be contained in the second argument.");
	}

	public static <T> T checkBeContained(T object, T[] objects, CharSequence message) {
		if (object != null && objects != null && !ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-009");
		}
		return object;
	}

	public static <T> T checkBeContained(T object, T[] objects) {
		return checkBeContained(object, objects,
				"Check failed - the first argument must be contained in the second argument.");
	}

	public static String checkContain(String textToSearch, String substring, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) == -1) {
			Exceptions.runtime(message, "FOSS-CHK-010");
		}
		return textToSearch;
	}

	public static String checkContain(String textToSearch, String substring) {
		return checkContain(textToSearch, substring,
				"Check failed - the first argument must contain the second argument.");
	}

	public static <T> Collection<T> checkContain(Collection<T> objects, T object, CharSequence message) {
		if (object != null && objects != null && !objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-010");
		}
		return objects;
	}

	public static <T> Collection<T> checkContain(Collection<T> objects, T object) {
		return checkContain(objects, object, "Check failed - the first argument must contain the second argument.");
	}

	public static <T> Map<T, ?> checkContain(Map<T, ?> objects, T object, CharSequence message) {
		if (object != null && objects != null && !objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-010");
		}
		return objects;
	}

	public static <T> Map<T, ?> checkContain(Map<T, ?> objects, T object) {
		return checkContain(objects, object, "Check failed - the first argument must contain the second argument.");
	}

	public static <T> T[] checkContain(T[] objects, T object, CharSequence message) {
		if (object != null && objects != null && !ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-010");
		}
		return objects;
	}

	public static <T> T[] checkContain(T[] objects, T object) {
		return checkContain(objects, object, "Check failed - the first argument must contain the second argument.");
	}

	public static String checkNotBeContained(String substring, String textToSearch, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) != -1) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return substring;
	}

	public static String checkNotBeContained(String substring, String textToSearch) {
		return checkNotBeContained(substring, textToSearch,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	public static <T> T checkNotBeContained(T object, Collection<T> objects, CharSequence message) {
		if (object != null && objects != null && objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	public static <T> T checkNotBeContained(T object, Collection<T> objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	public static <T> T checkNotBeContained(T object, Map<T, ?> objects, CharSequence message) {
		if (object != null && objects != null && objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	public static <T> T checkNotBeContained(T object, Map<T, ?> objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	public static <T> T checkNotBeContained(T object, T[] objects, CharSequence message) {
		if (object != null && objects != null && ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	public static <T> T checkNotBeContained(T object, T[] objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	public static String checkNotContain(String textToSearch, String substring, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) != -1) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return textToSearch;
	}

	public static String checkNotContain(String textToSearch, String substring) {
		return checkNotContain(textToSearch, substring,
				"Check failed - the first argument must not contain the second argument.");
	}

	public static <T> Collection<T> checkNotContain(Collection<T> objects, T object, CharSequence message) {
		if (object != null && objects != null && objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	public static <T> Collection<T> checkNotContain(Collection<T> objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	public static <T> Map<T, ?> checkNotContain(Map<T, ?> objects, T object, CharSequence message) {
		if (object != null && objects != null && objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	public static <T> Map<T, ?> checkNotContain(Map<T, ?> objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	public static <T> T[] checkNotContain(T[] objects, T object, CharSequence message) {
		if (object != null && objects != null && ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	public static <T> T[] checkNotContain(T[] objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	public static <T> T checkInstanceOf(T object, Class<?> type, CharSequence message) {
		if (!type.isInstance(object)) {
			Exceptions.runtime(message, "FOSS-CHK-013");
		}
		return object;
	}

	public static <T> T checkInstanceOf(T object, Class<?> type) {
		return checkInstanceOf(object, type,
				"Check failed - the first argument must be instanceof the second argument.");
	}

	public static <T> Class<T> checkSubType(Class<T> subType, Class<?> superType, CharSequence message) {
		if (!subType.isAssignableFrom(subType)) {
			Exceptions.runtime(message, "FOSS-CHK-014");
		}
		return subType;
	}

	public static <T> Class<T> checkSubType(Class<T> subType, Class<?> superType) {
		return checkSubType(subType, superType,
				"Check failed - the first argument must be assignable by the second argument.");
	}

	@SuppressWarnings("rawtypes")
	public static <T> T removeNull(T object) {
		if (object == null) {
			return null;
		} else if (object instanceof CharSequence) {
			String str = object.toString();
			return (T) (str == null ? null : str.trim());
		} else if (object instanceof Collection) {
			Collection clt = (Collection) object;
			Object[] array = clt.toArray();
			clt.clear();
			for (Object obj : array) {
				if (obj != null) {
					clt.add(obj);
				}
			}
			return (T) clt;
		} else if (object instanceof Map) {
			Map map = (Map) object;
			Object[] array = map.keySet().toArray();
			for (Object key : array) {
				if (key == null || map.get(key) == null) {
					map.remove(array);
				}
			}
			return (T) object;
		} else if (object.getClass().isArray()) {
			int len = Array.getLength(object);
			int count = 0;
			for (int i = 0; i < len; i++) {
				count = count + (Array.get(object, i) == null ? 0 : 1);
			}
			Object newInstance = Array.newInstance(object.getClass().getComponentType(), count);
			int pos = 0;
			for (int i = 0; i < len; i++) {
				Object value = Array.get(object, i);
				if (value != null) {
					Array.set(newInstance, pos, value);
					pos++;
				}
			}
			return (T) newInstance;
		}
		return object;
	}

	@SuppressWarnings("rawtypes")
	public static <T> T removeBlank(T object) {
		if (object == null) {
			return null;
		} else if (object instanceof CharSequence) {
			String str = object.toString();
			return (T) (str == null ? null : str.trim());
		} else if (object instanceof Collection) {
			Collection clt = (Collection) object;
			Object[] array = clt.toArray();
			clt.clear();
			for (Object obj : array) {
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (obj != null) {
					clt.add(obj);
				}
			}
			return (T) clt;
		} else if (object instanceof Map) {
			Map map = (Map) object;
			Object[] array = map.keySet().toArray();
			for (Object key : array) {
				Object obj = map.get(key);
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				key = (key instanceof CharSequence) && StringUtils.isBlank(key.toString()) ? null : key;
				if (key == null || obj == null) {
					map.remove(array);
				}
			}
			return (T) object;
		} else if (object.getClass().isArray()) {
			int len = Array.getLength(object);
			int count = 0;
			for (int i = 0; i < len; i++) {
				Object obj = Array.get(object, i);
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				count = count + (obj == null ? 0 : 1);
			}
			Object newInstance = Array.newInstance(object.getClass().getComponentType(), count);
			int pos = 0;
			for (int i = 0; i < len; i++) {
				Object obj = Array.get(object, i);
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (obj != null) {
					Array.set(newInstance, pos, obj);
					pos++;
				}
			}
			return (T) newInstance;
		}
		return object;
	}

	@SuppressWarnings("rawtypes")
	public static <T> T trimAndRemoveBlank(T object) {
		if (object == null) {
			return null;
		} else if (object instanceof CharSequence) {
			String str = object.toString();
			return (T) (str == null ? null : str.trim());
		} else if (object instanceof Collection) {
			Collection clt = (Collection) object;
			Object[] array = clt.toArray();
			clt.clear();
			for (Object obj : array) {
				obj = (obj instanceof CharSequence) ? obj.toString().trim() : obj;
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (obj != null) {
					clt.add(obj);
				}
			}
			return (T) clt;
		} else if (object instanceof Map) {
			Map map = (Map) object;
			Object[] array = map.keySet().toArray();
			for (Object key : array) {
				Object obj = map.get(key);
				key = (key instanceof CharSequence) ? (key.toString().trim()) : key;
				key = (key instanceof CharSequence) && StringUtils.isBlank(key.toString()) ? null : key;
				obj = (obj instanceof CharSequence) ? (obj.toString().trim()) : obj;
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (key == null || map.get(key) == null) {
					map.remove(array);
				}
			}
			return (T) object;
		} else if (object.getClass().isArray()) {
			int len = Array.getLength(object);
			int count = 0;
			for (int i = 0; i < len; i++) {
				Object obj = Array.get(object, i);
				obj = (obj instanceof CharSequence) ? obj.toString().trim() : obj;
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				count = count + (obj == null ? 0 : 1);
			}
			Object newInstance = Array.newInstance(object.getClass().getComponentType(), count);
			int pos = 0;
			for (int i = 0; i < len; i++) {
				Object obj = Array.get(object, i);
				obj = (obj instanceof CharSequence) ? obj.toString().trim() : obj;
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (obj != null) {
					Array.set(newInstance, pos, obj);
					pos++;
				}
			}
			return (T) newInstance;
		}
		return object;
	}

	public static String nullToEmpty(String object) {
		return object == null ? "" : object;
	}

	@SuppressWarnings("rawtypes")
	public static Collection<?> nullToEmpty(Collection<?> object) {
		return object == null ? new ArrayList() : object;
	}

	@SuppressWarnings("rawtypes")
	public static Map<?, ?> nullToEmpty(Map<?, ?> object) {
		return object == null ? new LinkedHashMap() : object;
	}

	public static Object[] nullToEmpty(Object[] object) {
		return object == null ? new Object[0] : object;
	}

	public static boolean[] nullToEmpty(boolean[] object) {
		return object == null ? new boolean[0] : object;
	}

	public static short[] nullToEmpty(short[] object) {
		return object == null ? new short[0] : object;
	}

	public static int[] nullToEmpty(int[] object) {
		return object == null ? new int[0] : object;
	}

	public static long[] nullToEmpty(long[] object) {
		return object == null ? new long[0] : object;
	}

	public static float[] nullToEmpty(float[] object) {
		return object == null ? new float[0] : object;
	}

	public static double[] nullToEmpty(double[] object) {
		return object == null ? new double[0] : object;
	}

	public static Boolean[] nullToEmpty(Boolean[] object) {
		return object == null ? new Boolean[0] : object;
	}

	public static Short[] nullToEmpty(Short[] object) {
		return object == null ? new Short[0] : object;
	}

	public static Integer[] nullToEmpty(Integer[] object) {
		return object == null ? new Integer[0] : object;
	}

	public static Long[] nullToEmpty(Long[] object) {
		return object == null ? new Long[0] : object;
	}

	public static Float[] nullToEmpty(Float[] object) {
		return object == null ? new Float[0] : object;
	}

	public static Double[] nullToEmpty(Double[] object) {
		return object == null ? new Double[0] : object;
	}
}
