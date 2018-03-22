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

	public static <T extends CharSequence> T checkBlank(T text, CharSequence message) {
		if (StringUtils.isBlank(text)) {
			Exceptions.runtime(message, "FOSS-CHK-006");
		}
		return text;
	}

	public static <T extends CharSequence> T checkBlank(T text) {
		return checkBlank(text, "Check failed - this CharSequence argument must be null or blank.");
	}

	public static <T extends CharSequence> T checkNotBlank(T text, CharSequence message) {
		if (StringUtils.isNotBlank(text)) {
			Exceptions.runtime(message, "FOSS-CHK-007");
		}
		return text;
	}

	public static <T extends CharSequence> T checkNotBlank(T text) {
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

	/**
	 * 数组包含对象。
	 * @param objects
	 * @param object
	 * @param message
	 * @return 如果数组不包含对象，则抛出FOSS-CHK-010异常，否则返回原数组。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T[] checkContain(T[] objects, T object, CharSequence message) {
		if (object != null && objects != null && !ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-010");
		}
		return objects;
	}

	/**
	 * 数组包含对象。
	 * @param objects
	 * @param object
	 * @return 如果数组不包含对象，则抛出FOSS-CHK-010异常，否则返回原数组。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T[] checkContain(T[] objects, T object) {
		return checkContain(objects, object, "Check failed - the first argument must contain the second argument.");
	}

	/**
	 * 字符串不被包含。
	 * @param substring
	 * @param textToSearch
	 * @param message
	 * @return 如果字符串不被包含，则抛出FOSS-CHK-011异常，否则返回原字符串。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static String checkNotBeContained(String substring, String textToSearch, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) != -1) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return substring;
	}

	/**
	 * 字符串不被包含。
	 * @param substring
	 * @param textToSearch
	 * @return 如果字符串不被包含，则抛出FOSS-CHK-011异常，否则返回原字符串。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static String checkNotBeContained(String substring, String textToSearch) {
		return checkNotBeContained(substring, textToSearch,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	/**
	 * 对象不被包含在列表中。
	 * @param object
	 * @param objects
	 * @param message
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, Collection<T> objects, CharSequence message) {
		if (object != null && objects != null && objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	/**
	 * 对象不被包含在列表中。
	 * @param object
	 * @param objects
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, Collection<T> objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	/**
	 * 对象不被包含在Map中。
	 * @param object
	 * @param objects
	 * @param message
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, Map<T, ?> objects, CharSequence message) {
		if (object != null && objects != null && objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	/**
	 * 对象不被包含在Map中。
	 * @param object
	 * @param objects
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, Map<T, ?> objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	/**
	 * 对象不被包含在数组中。
	 * @param object
	 * @param objects
	 * @param message
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, T[] objects, CharSequence message) {
		if (object != null && objects != null && ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-011");
		}
		return object;
	}

	/**
	 * 对象不被包含在数组中。
	 * @param object
	 * @param objects
	 * @return 如果对象不被包含，则抛出FOSS-CHK-011异常，否则返回原对象。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkNotBeContained(T object, T[] objects) {
		return checkNotBeContained(object, objects,
				"Check failed - the first argument must not be contained in the second argument.");
	}

	/**
	 * 不包含字符串。
	 * @param textToSearch
	 * @param substring
	 * @param message
	 * @return 如果包含字符串，则抛出FOSS-CHK-012异常，否则返回原字符串。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static String checkNotContain(String textToSearch, String substring, CharSequence message) {
		if (substring != null && textToSearch != null && textToSearch.indexOf(substring) != -1) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return textToSearch;
	}

	/**
	 * 不包含字符串。
	 * @param textToSearch
	 * @param substring
	 * @return 如果包含字符串，则抛出FOSS-CHK-012异常，否则返回原字符串。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static String checkNotContain(String textToSearch, String substring) {
		return checkNotContain(textToSearch, substring,
				"Check failed - the first argument must not contain the second argument.");
	}

	/**
	 * 列表不包含对象。
	 * @param objects
	 * @param object
	 * @param message
	 * @return 如果包含对象，则抛出FOSS-CHK-012异常，否则返回原列表。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Collection<T> checkNotContain(Collection<T> objects, T object, CharSequence message) {
		if (object != null && objects != null && objects.contains(object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	/**
	 * 列表不包含对象。
	 * @param objects
	 * @param object
	 * @return 如果包含对象，则抛出FOSS-CHK-012异常，否则返回原列表。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Collection<T> checkNotContain(Collection<T> objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	/**
	 * Map不包含Key。
	 * @param objects
	 * @param object
	 * @param message
	 * @return 如果Map不包含Key，则抛出FOSS-CHK-012异常，否则返回原Map。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Map<T, ?> checkNotContain(Map<T, ?> objects, T object, CharSequence message) {
		if (object != null && objects != null && objects.containsKey(object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	/**
	 * Map不包含Key。
	 * @param objects
	 * @param object
	 * @return 如果Map不包含Key，则抛出FOSS-CHK-012异常，否则返回原Map。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Map<T, ?> checkNotContain(Map<T, ?> objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	/**
	 * 数组不包含对象。
	 * @param objects
	 * @param object
	 * @param message
	 * @return 如果包含对象，则抛出FOSS-CHK-012异常，否则返回原数组。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T[] checkNotContain(T[] objects, T object, CharSequence message) {
		if (object != null && objects != null && ArrayUtils.contains(objects, object)) {
			Exceptions.runtime(message, "FOSS-CHK-012");
		}
		return objects;
	}

	/**
	 * 数组不包含对象。
	 * @param objects
	 * @param object
	 * @return 如果包含对象，则抛出FOSS-CHK-012异常，否则返回原数组。
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T[] checkNotContain(T[] objects, T object) {
		return checkNotContain(objects, object,
				"Check failed - the first argument must not contain the second argument.");
	}

	/**
	 * 对象是否为类型的实例。
	 * @param object
	 * @param type
	 * @param message
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkInstanceOf(T object, Class<?> type, CharSequence message) {
		if (!type.isInstance(object)) {
			Exceptions.runtime(message, "FOSS-CHK-013");
		}
		return object;
	}

	/**
	 * 对象是否为类型的实例。
	 * @param object
	 * @param type
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> T checkInstanceOf(T object, Class<?> type) {
		return checkInstanceOf(object, type,
				"Check failed - the first argument must be instanceof the second argument.");
	}

	/**
	 * 参数1 subType 是否为参数2 superType 的子类型。
	 * @param subType
	 * @param superType
	 * @param message
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Class<T> checkSubType(Class<T> subType, Class<?> superType, CharSequence message) {
		if (!subType.isAssignableFrom(subType)) {
			Exceptions.runtime(message, "FOSS-CHK-014");
		}
		return subType;
	}

	/**
	 * 参数1 subType 是否为参数2 superType 的子类型。
	 * @param subType
	 * @param superType
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static <T> Class<T> checkSubType(Class<T> subType, Class<?> superType) {
		return checkSubType(subType, superType,
				"Check failed - the first argument must be assignable by the second argument.");
	}

	/**
	 * 把对象的null值删除。
	 * <pre>
	 * 输入值        : 返回值
	 * =============:======
	 * null         : null
	 * CharSequence : str
	 * Collection   : remove null value, [null, 1, " a ", " "] => [1, " a ", " "]
	 * Map          : remove null value, {"1": null, "2": " a ", " ": " "} => {"2": " a ", " ": " "}
	 * Array        : remove null value, [null, 1, " a ", " "] => [1, " a ", " "]
	 * =============:======
	 * </pre>
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
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

	/**
	 * 把对象的null值及空值删除。
	 * <pre>
	 * 输入值        : 返回值
	 * =============:======
	 * null         : null
	 * CharSequence : blank=null or str
	 * Collection   : remove null value and blank value, [null, 1, " a ", " "] => [1, " a "]
	 * Map          : remove null value and blank value, {"1": null, "2": " a ", " ": " "} => {"2": " a "}
	 * Array        : remove null value and blank value, [null, 1, " a ", " "] => [1, " a "]
	 * =============:======
	 * </pre>
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T removeBlank(T object) {
		if (object == null) {
			return null;
		} else if (object instanceof CharSequence) {
			String str = object.toString();
			return (T) (str == null || StringUtils.isBlank(str) ? null : str);
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
				Object obj = map.remove(key);
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				key = (key instanceof CharSequence) && StringUtils.isBlank(key.toString()) ? null : key;
				if (!(key == null || map.get(key) == null)) {
					map.put(key, obj);
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

	/**
	 * 把对象的null值及Blank值删除，并把字符串进行trim。
	 * <pre>
	 * 输入值        : 返回值
	 * =============:======
	 * null         : null
	 * CharSequence : blank=null or str.trim()
	 * Collection   : remove null value and trim string value, [null, 1, " a ", ""] => [1, "a"]
	 * Map          : remove null value and trim string value, {"1": null, " 2 ": " a ", "3": ""} => {"2": "a"}
	 * Array        : remove null value and trim string value, [null, 1, " a ", ""] => [1, "a"]
	 * =============:======
	 * </pre>
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T trimAndRemoveBlank(T object) {
		if (object == null) {
			return null;
		} else if (object instanceof CharSequence) {
			String str = object.toString();
			return (T) (str == null || StringUtils.isBlank(str) ? null : str.trim());
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
				Object obj = map.remove(key);
				key = (key instanceof CharSequence) ? (key.toString().trim()) : key;
				key = (key instanceof CharSequence) && StringUtils.isBlank(key.toString()) ? null : key;
				obj = (obj instanceof CharSequence) ? (obj.toString().trim()) : obj;
				obj = (obj instanceof CharSequence) && StringUtils.isBlank(obj.toString()) ? null : obj;
				if (!(key == null || map.get(key) == null)) {
					map.put(key, obj);
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

	/**
	 * 如果为null，则返回空字符串。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static String nullToEmpty(String object) {
		return object == null ? "" : object;
	}

	/**
	 * 如果为null，则返回new ArrayList()。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	@SuppressWarnings("rawtypes")
	public static Collection<?> nullToEmpty(Collection<?> object) {
		return object == null ? new ArrayList() : object;
	}

	/**
	 * 如果为null，则返回new LinkedHashMap()。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	@SuppressWarnings("rawtypes")
	public static Map<?, ?> nullToEmpty(Map<?, ?> object) {
		return object == null ? new LinkedHashMap() : object;
	}

	/**
	 * 如果为null，则返回new Object[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Object[] nullToEmpty(Object[] object) {
		return object == null ? new Object[0] : object;
	}

	/**
	 * 如果为null，则返回new boolean[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static boolean[] nullToEmpty(boolean[] object) {
		return object == null ? new boolean[0] : object;
	}

	/**
	 * 如果为null，则返回new short[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static short[] nullToEmpty(short[] object) {
		return object == null ? new short[0] : object;
	}

	/**
	 * 如果为null，则返回new int[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static int[] nullToEmpty(int[] object) {
		return object == null ? new int[0] : object;
	}

	/**
	 * 如果为null，则返回new long[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static long[] nullToEmpty(long[] object) {
		return object == null ? new long[0] : object;
	}

	/**
	 * 如果为null，则返回new float[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static float[] nullToEmpty(float[] object) {
		return object == null ? new float[0] : object;
	}

	/**
	 * 如果为null，则返回new double[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static double[] nullToEmpty(double[] object) {
		return object == null ? new double[0] : object;
	}

	/**
	 * 如果为null，则返回new Boolean[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Boolean[] nullToEmpty(Boolean[] object) {
		return object == null ? new Boolean[0] : object;
	}

	/**
	 * 如果为null，则返回new Short[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Short[] nullToEmpty(Short[] object) {
		return object == null ? new Short[0] : object;
	}

	/**
	 * 如果为null，则返回new Integer[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Integer[] nullToEmpty(Integer[] object) {
		return object == null ? new Integer[0] : object;
	}

	/**
	 * 如果为null，则返回new Long[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Long[] nullToEmpty(Long[] object) {
		return object == null ? new Long[0] : object;
	}

	/**
	 * 如果为null，则返回new Float[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Float[] nullToEmpty(Float[] object) {
		return object == null ? new Float[0] : object;
	}

	/**
	 * 如果为null，则返回new Double[0]。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static Double[] nullToEmpty(Double[] object) {
		return object == null ? new Double[0] : object;
	}

	/**
	 * 测试是否为空，为空情况：null，空字符串，空列表，空Map。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		} else if (object instanceof CharSequence) {
			return StringUtils.isEmpty((CharSequence) object);
		} else if (object instanceof Collection) {
			return ((Collection<?>) object).isEmpty();
		} else if (object instanceof Map) {
			return ((Map<?, ?>) object).isEmpty();
		} else if (object.getClass().isArray()) {
			return Array.getLength(object) < 1;
		}
		return false;
	}

	/**
	 * 测试是否不为空，不为空情况：非null，非空字符串，非空列表，非空Map。
	 * @param object
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 4, 2018
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}
}
