/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.iff.infra.util.validation.ValidationMethods;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 用于参数检查，并返回值，不符合的参数会抛出异常。
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-08-19
 */
@SuppressWarnings("unchecked")
public class PreRequiredHelper {

    /**
     * 表达式需要为 true ，否则抛出异常。
     * <pre>
     *     expression != true, throw RuntimeException.
     * </pre>
     *
     * @param expression 表达式
     * @param message    异常信息
     * @param <T>        表达式的类型
     * @return 表达式
     * @since 2018-08-19
     */
    public static <T> T requireTrue(T expression, CharSequence message) {
        if (!Boolean.TRUE.equals(expression)) {
            Exceptions.runtime(message, "FOSS-REQ-001");
        }
        return expression;
    }

    /**
     * 表达式需要为 true ，否则抛出异常。
     * <pre>
     *     expression != true, throw RuntimeException.
     * </pre>
     *
     * @param expression 表达式
     * @param <T>        表达式的类型
     * @return 表达式
     * @since 2018-08-19
     */
    public static <T> T requireTrue(T expression) {
        return requireTrue(expression, "Check failed - this expression must be true.");
    }

    /**
     * 表达式需要为 false ，否则抛出异常。
     * <pre>
     *     expression != false, throw RuntimeException.
     * </pre>
     *
     * @param expression 表达式
     * @param message    异常信息
     * @param <T>        表达式的类型
     * @return 表达式
     * @since 2018-08-19
     */
    public static <T> T requireFalse(T expression, CharSequence message) {
        if (!Boolean.FALSE.equals(expression)) {
            Exceptions.runtime(message, "FOSS-REQ-001");
        }
        return expression;
    }

    /**
     * 表达式需要为 false ，否则抛出异常。
     * <pre>
     *     expression != false, throw RuntimeException.
     * </pre>
     *
     * @param expression 表达式
     * @param <T>        表达式的类型
     * @return 表达式
     * @since 2018-08-19
     */
    public static <T> T requireFalse(T expression) {
        return requireFalse(expression, "Check failed - this expression must be false.");
    }

    /**
     * 对象需要为 null ，否则抛出异常。
     * <pre>
     *     object != null, throw RuntimeException.
     * </pre>
     *
     * @param object  对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @since 2018-08-19
     */
    public static <T> T requireNull(T object, CharSequence message) {
        if (object != null) {
            Exceptions.runtime(message, "FOSS-REQ-002");
        }
        return object;
    }

    /**
     * 对象需要为 null ，否则抛出异常。
     * <pre>
     *     object != null, throw RuntimeException.
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @since 2018-08-19
     */
    public static <T> T requireNull(T object) {
        return requireNull(object, "Check failed - the object argument must be null.");
    }

    /**
     * 对象需要不为 null ，否则抛出异常。
     * <pre>
     *     object == null, throw RuntimeException.
     * </pre>
     *
     * @param object  对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     */
    public static <T> T requireNotNull(T object, CharSequence message) {
        if (object == null) {
            Exceptions.runtime(message, "FOSS-REQ-003");
        }
        return object;
    }

    /**
     * 对象需要不为 null ，否则抛出异常。
     * <pre>
     *     object == null, throw RuntimeException.
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     */
    public static <T> T requireNotNull(T object) {
        return requireNotNull(object, "Check failed - this argument is required; it must not be null.");
    }

    /**
     * 对象需要为 empty ，否则抛出异常。
     * <pre>
     *     object != null && object != "" && object != [] && object != {}, throw RuntimeException.
     * </pre>
     *
     * @param object  对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     */
    public static <T> T requireEmpty(T object, CharSequence message) {
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
            Exceptions.runtime(message, "FOSS-REQ-004");
        }
        return object;
    }

    /**
     * 对象需要为 empty ，否则抛出异常。
     * <pre>
     *     object != null && object != "" && object != [] && object != {}, throw RuntimeException.
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     */
    public static <T> T requireEmpty(T object) {
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
        return requireEmpty(object, message);
    }

    /**
     * 对象需要不为 empty ，否则抛出异常。
     * <pre>
     *     object == null || object == "" || object == [] || object == {}, throw RuntimeException.
     * </pre>
     *
     * @param object  对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     */
    public static <T> T requireNotEmpty(T object, CharSequence message) {
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
            Exceptions.runtime(message, "FOSS-REQ-005");
        }
        return object;
    }

    /**
     * 对象需要不为 empty ，否则抛出异常。
     * <pre>
     *     object == null || object == "" || object == [] || object == {}, throw RuntimeException.
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     */
    public static <T> T requireNotEmpty(T object) {
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
        return requireNotEmpty(object, message);
    }

    /**
     * 对象需要为 blank ，否则抛出异常。
     * <pre>
     *     text != null && text != "" && text != " ", StringUtils.isNotBlank(text), throw RuntimeException.
     * </pre>
     *
     * @param text    字符串
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     */
    public static <T extends CharSequence> T requireBlank(T text, CharSequence message) {
        if (StringUtils.isNotBlank(text)) {
            Exceptions.runtime(message, "FOSS-REQ-006");
        }
        return text;
    }

    /**
     * 对象需要为 blank ，否则抛出异常。
     * <pre>
     *     text != null && text != "" && text != " ", StringUtils.isNotBlank(text), throw RuntimeException.
     * </pre>
     *
     * @param text 字符串
     * @param <T>  对象的类型
     * @return 对象
     */
    public static <T extends CharSequence> T requireBlank(T text) {
        return requireBlank(text, "Check failed - this CharSequence argument must be null or blank.");
    }

    /**
     * 对象需要不为 blank ，否则抛出异常。
     * <pre>
     *     text == null || text == "" && text != " ", StringUtils.isBlank(text), throw RuntimeException.
     * </pre>
     *
     * @param text    字符串
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     */
    public static <T extends CharSequence> T requireNotBlank(T text, CharSequence message) {
        if (StringUtils.isBlank(text)) {
            Exceptions.runtime(message, "FOSS-REQ-007");
        }
        return text;
    }

    /**
     * 对象需要不为 blank ，否则抛出异常。
     * <pre>
     *     text == null || text == "" && text != " ", StringUtils.isBlank(text), throw RuntimeException.
     * </pre>
     *
     * @param text 字符串
     * @param <T>  对象的类型
     * @return 对象
     */
    public static <T extends CharSequence> T requireNotBlank(T text) {
        return requireNotBlank(text, "Check failed - this CharSequence argument must not be null or blank.");
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) == -1 , throw RuntimeException.
     * </pre>
     *
     * @param substring    被包含的字符串
     * @param textToSearch 字符串
     * @param message      异常信息
     * @return 被包含的字符串
     */
    public static String requireBeContained(String substring, String textToSearch, CharSequence message) {
        if (substring == null || textToSearch == null || textToSearch.indexOf(substring) == -1) {
            Exceptions.runtime(message, "FOSS-REQ-008");
        }
        return substring;
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) == -1, throw RuntimeException.
     * </pre>
     *
     * @param substring    被包含的字符串
     * @param textToSearch 字符串
     * @return 被包含的字符串
     */
    public static String requireBeContained(String substring, String textToSearch) {
        return requireBeContained(substring, textToSearch,
                "Check failed - the first argument must be contained in the second argument.");
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象
     * @param objects 对象集合
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, Collection<T> objects, CharSequence message) {
        if (object == null || objects == null || !objects.contains(object)) {
            Exceptions.runtime(message, "FOSS-REQ-009");
        }
        return object;
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象
     * @param objects 对象集合
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, Collection<T> objects) {
        return requireBeContained(object, objects,
                "Check failed - the first argument must be contained in the second argument.");
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象，KEY 检测
     * @param objects 对象集合
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, Map<T, ?> objects, CharSequence message) {
        if (object == null || objects == null || !objects.containsKey(object)) {
            Exceptions.runtime(message, "FOSS-REQ-009");
        }
        return object;
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象，KEY 检测
     * @param objects 对象集合
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, Map<T, ?> objects) {
        return requireBeContained(object, objects,
                "Check failed - the first argument must be contained in the second argument.");
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象
     * @param objects 对象集合
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, T[] objects, CharSequence message) {
        if (object == null || objects == null || !ArrayUtils.contains(objects, object)) {
            Exceptions.runtime(message, "FOSS-REQ-009");
        }
        return object;
    }

    /**
     * 对象需要被包含在第二参数内，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param object  被包含的对象
     * @param objects 对象集合
     * @param <T>     对象的类型
     * @return 被包含的对象
     */
    public static <T> T requireBeContained(T object, T[] objects) {
        return requireBeContained(object, objects,
                "Check failed - the first argument must be contained in the second argument.");
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring), throw RuntimeException.
     * </pre>
     *
     * @param textToSearch 字符串
     * @param substring    被包含的对象
     * @param message      异常信息
     * @return 字符串
     */
    public static String requireContains(String textToSearch, String substring, CharSequence message) {
        if (substring == null || textToSearch == null || textToSearch.indexOf(substring) == -1) {
            Exceptions.runtime(message, "FOSS-REQ-010");
        }
        return textToSearch;
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring), throw RuntimeException.
     * </pre>
     *
     * @param textToSearch 字符串
     * @param substring    被包含的对象
     * @return 字符串
     */
    public static String requireContains(String textToSearch, String substring) {
        return requireContains(textToSearch, substring,
                "Check failed - the first argument must contain the second argument.");
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Collection<T> requireContains(Collection<T> objects, T object, CharSequence message) {
        if (object == null || objects == null || !objects.contains(object)) {
            Exceptions.runtime(message, "FOSS-REQ-010");
        }
        return objects;
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Collection<T> requireContains(Collection<T> objects, T object) {
        return requireContains(objects, object, "Check failed - the first argument must contain the second argument.");
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Map<T, ?> requireContains(Map<T, ?> objects, T object, CharSequence message) {
        if (object == null || objects == null || !objects.containsKey(object)) {
            Exceptions.runtime(message, "FOSS-REQ-010");
        }
        return objects;
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Map<T, ?> requireContains(Map<T, ?> objects, T object) {
        return requireContains(objects, object, "Check failed - the first argument must contain the second argument.");
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> T[] requireContains(T[] objects, T object, CharSequence message) {
        if (object == null || objects == null || !ArrayUtils.contains(objects, object)) {
            Exceptions.runtime(message, "FOSS-REQ-010");
        }
        return objects;
    }

    /**
     * 对象需要包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || !ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> T[] requireContains(T[] objects, T object) {
        return requireContains(objects, object, "Check failed - the first argument must contain the second argument.");
    }

    /**
     * 对象需要不能被第二参数包含，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1, throw RuntimeException.
     * </pre>
     *
     * @param substring    不被包含的字符串
     * @param textToSearch 字符串
     * @param message      异常信息
     * @return 不被包含的字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireNotBeContained(String substring, String textToSearch, CharSequence message) {
        if (substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1) {
            Exceptions.runtime(message, "FOSS-REQ-011");
        }
        return substring;
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1, throw RuntimeException.
     * </pre>
     *
     * @param substring    不被包含的字符串
     * @param textToSearch 字符串
     * @return 不被包含的字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireNotBeContained(String substring, String textToSearch) {
        return requireNotBeContained(substring, textToSearch,
                "Check failed - the first argument must not be contained in the second argument.");
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @param message 异常信息
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, Collection<T> objects, CharSequence message) {
        if (object == null || objects == null || objects.contains(object)) {
            Exceptions.runtime(message, "FOSS-REQ-011");
        }
        return object;
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, Collection<T> objects) {
        return requireNotBeContained(object, objects,
                "Check failed - the first argument must not be contained in the second argument.");
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @param message 异常信息
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, Map<T, ?> objects, CharSequence message) {
        if (object == null || objects == null || objects.containsKey(object)) {
            Exceptions.runtime(message, "FOSS-REQ-011");
        }
        return object;
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, Map<T, ?> objects) {
        return requireNotBeContained(object, objects,
                "Check failed - the first argument must not be contained in the second argument.");
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @param message 异常信息
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, T[] objects, CharSequence message) {
        if (object == null || objects == null || ArrayUtils.contains(objects, object)) {
            Exceptions.runtime(message, "FOSS-REQ-011");
        }
        return object;
    }

    /**
     * 对象不能被第二参数包含，否则抛出异常。
     * <pre>
     *     object == null || objects == null || ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param object  不被包含的对象
     * @param objects 对象集合
     * @return 不被包含的对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireNotBeContained(T object, T[] objects) {
        return requireNotBeContained(object, objects,
                "Check failed - the first argument must not be contained in the second argument.");
    }

    /**
     * 对象需要不能包含第二参数，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1, throw RuntimeException.
     * </pre>
     *
     * @param textToSearch 字符串
     * @param substring    不被包含的对象
     * @param message      异常信息
     * @return 字符串
     */
    public static String requireNotContain(String textToSearch, String substring, CharSequence message) {
        if (substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1) {
            Exceptions.runtime(message, "FOSS-REQ-012");
        }
        return textToSearch;
    }

    /**
     * 对象需要不能包含第二参数，否则抛出异常。
     * <pre>
     *     substring == null || textToSearch == null || textToSearch.indexOf(substring) != -1, throw RuntimeException.
     * </pre>
     *
     * @param textToSearch 字符串
     * @param substring    不被包含的对象
     * @return 字符串
     */
    public static String requireNotContain(String textToSearch, String substring) {
        return requireNotContain(textToSearch, substring,
                "Check failed - the first argument must not contain the second argument.");
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Collection<T> requireNotContain(Collection<T> objects, T object, CharSequence message) {
        if (object == null || objects == null || objects.contains(object)) {
            Exceptions.runtime(message, "FOSS-REQ-012");
        }
        return objects;
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.contains(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Collection<T> requireNotContain(Collection<T> objects, T object) {
        return requireNotContain(objects, object,
                "Check failed - the first argument must not contain the second argument.");
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Map<T, ?> requireNotContain(Map<T, ?> objects, T object, CharSequence message) {
        if (object == null || objects == null || objects.containsKey(object)) {
            Exceptions.runtime(message, "FOSS-REQ-012");
        }
        return objects;
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || objects.containsKey(object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> Map<T, ?> requireNotContain(Map<T, ?> objects, T object) {
        return requireNotContain(objects, object,
                "Check failed - the first argument must not contain the second argument.");
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> T[] requireNotContain(T[] objects, T object, CharSequence message) {
        if (object == null || objects == null || ArrayUtils.contains(objects, object)) {
            Exceptions.runtime(message, "FOSS-REQ-012");
        }
        return objects;
    }

    /**
     * 对象需要不包含第二参数，否则抛出异常。
     * <pre>
     *     object == null || objects == null || ArrayUtils.contains(objects, object), throw RuntimeException.
     * </pre>
     *
     * @param objects 对象集合
     * @param object  不被包含的对象
     * @param <T>     对象的类型
     * @return 对象集合
     */
    public static <T> T[] requireNotContain(T[] objects, T object) {
        return requireNotContain(objects, object,
                "Check failed - the first argument must not contain the second argument.");
    }

    /**
     * 对象需要为类型的实例，否则抛出异常。
     * <pre>
     *     !type.isInstance(object), throw RuntimeException.
     * </pre>
     *
     * @param object  对象
     * @param type    类型
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireInstanceOf(T object, Class<?> type, CharSequence message) {
        if (type == null || !type.isInstance(object)) {
            Exceptions.runtime(message, "FOSS-REQ-013");
        }
        return object;
    }

    /**
     * 对象需要为类型的实例，否则抛出异常。
     * <pre>
     *     !type.isInstance(object), throw RuntimeException.
     * </pre>
     *
     * @param object 对象
     * @param type   类型
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T requireInstanceOf(T object, Class<?> type) {
        return requireInstanceOf(object, type,
                "Check failed - the first argument must be instanceof the second argument.");
    }

    /**
     * 对象类型需要为第二参数类型的子类型，否则抛出异常。
     *
     * @param subType   对象类型
     * @param superType 父类型
     * @param message   异常信息
     * @param <T>       对象的类型
     * @return 对象类型
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Class<T> requireSubType(Class<T> subType, Class<?> superType, CharSequence message) {
        if (subType == null || superType == null || !superType.isAssignableFrom(subType)) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return subType;
    }

    /**
     * 对象类型需要为第二参数类型的子类型，否则抛出异常。
     *
     * @param subType   对象类型
     * @param superType 父类型
     * @param <T>       对象的类型
     * @return 对象类型
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Class<T> requireSubType(Class<T> subType, Class<?> superType) {
        return requireSubType(subType, superType,
                "Check failed - the first argument must be assignable by the second argument.");
    }

    /**
     * 对象需要为 Email 地址，否则抛出异常。
     *
     * @param email   Email
     * @param message 异常信息
     * @return Email
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireEmail(String email, CharSequence message) {
        if (!(ValidationMethods.email(email))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return email;
    }

    /**
     * 对象需要为 Email 地址，否则抛出异常。
     *
     * @param email Email
     * @return Email
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireEmail(String email) {
        return requireEmail(email,
                "Check failed - the first argument must be email address.");
    }

    /**
     * 对象需要为 Url 地址，否则抛出异常。
     *
     * @param url     URL
     * @param message 异常信息
     * @return URL
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireUrl(String url, CharSequence message) {
        if (!(ValidationMethods.url(url))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return url;
    }

    /**
     * 对象需要为 URL 地址，否则抛出异常。
     *
     * @param url URL
     * @return URL
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireUrl(String url) {
        return requireUrl(url,
                "Check failed - the first argument must be url address.");
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value   字符串
     * @param min     最小长度
     * @param message 异常信息
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMinLength(String value, int min, CharSequence message) {
        if (!(ValidationMethods.minLength(value, min))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value 字符串
     * @param min   最小长度
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMinLength(String value, int min) {
        return requireMinLength(value, min,
                "Check failed - the first argument must have min length.");
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value   对象集合
     * @param min     最小长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireMinLength(T[] value, int min, CharSequence message) {
        if (value != null && value.length < min) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value 字符串
     * @param min   最小长度
     * @param <T>   对象的类型
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireMinLength(T[] value, int min) {
        return requireMinLength(value, min,
                "Check failed - the first argument must have min length.");
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value   对象集合
     * @param min     最小长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireMinLength(Collection<T> value, int min, CharSequence message) {
        if (value != null && value.size() < min) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value 字符串
     * @param min   最小长度
     * @param <T>   对象的类型
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireMinLength(Collection<T> value, int min) {
        return requireMinLength(value, min,
                "Check failed - the first argument must have min length.");
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value   对象集合
     * @param min     最小长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象集合
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireMinLength(Map<T, ?> value, int min, CharSequence message) {
        if (value != null && value.size() < min) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要达到最小长度，否则抛出异常。
     *
     * @param value 字符串
     * @param min   最小长度
     * @param <T>   对象的类型
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireMinLength(Map<T, ?> value, int min) {
        return requireMinLength(value, min,
                "Check failed - the first argument must have min length.");
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value   字符串
     * @param max     最大长度
     * @param message 异常信息
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMaxLength(String value, int max, CharSequence message) {
        if (!(ValidationMethods.maxLength(value, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 字符串
     * @param max   最大长度
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMaxLength(String value, int max) {
        return requireMaxLength(value, max,
                "Check failed - the first argument must not over max length.");
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value   对象
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireMaxLength(T[] value, int max, CharSequence message) {
        if (value != null && value.length > max) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 对象
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireMaxLength(T[] value, int max) {
        return requireMaxLength(value, max,
                "Check failed - the first argument must not over max length.");
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value   对象
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireMaxLength(Collection<T> value, int max, CharSequence message) {
        if (value != null && value.size() > max) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 对象
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireMaxLength(Collection<T> value, int max) {
        return requireMaxLength(value, max,
                "Check failed - the first argument must not over max length.");
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value   对象
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireMaxLength(Map<T, ?> value, int max, CharSequence message) {
        if (value != null && value.size() > max) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 对象
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireMaxLength(Map<T, ?> value, int max) {
        return requireMaxLength(value, max,
                "Check failed - the first argument must not over max length.");
    }

    /**
     * 对象需要在指定长度范围内，否则抛出异常。
     *
     * @param value   字符串
     * @param min     最小长度
     * @param max     最大长度
     * @param message 异常信息
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireRangeLength(String value, int min, int max, CharSequence message) {
        if (!(ValidationMethods.rangeLength(value, min, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要在指定长度范围内，否则抛出异常。
     *
     * @param value 字符串
     * @param min   最小长度
     * @param max   最大长度
     * @return 字符串
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireRangeLength(String value, int min, int max) {
        return requireRangeLength(value, min, max,
                "Check failed - the first argument must between min and max length.");
    }

    /**
     * 对象需要在指定长度范围内，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小长度
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireRangeLength(T[] value, int min, int max, CharSequence message) {
        if (value != null && !(value.length <= max && value.length >= min)) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要在指定长度范围内，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小长度
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> T[] requireRangeLength(T[] value, int min, int max) {
        return requireRangeLength(value, min, max,
                "Check failed - the first argument must between min and max length.");
    }

    /**
     * 对象需要在指定长度范围内，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小长度
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireRangeLength(Collection<T> value, int min, int max, CharSequence message) {
        if (value != null && !(value.size() <= max && value.size() >= min)) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小长度
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Collection<T> requireRangeLength(Collection<T> value, int min, int max) {
        return requireRangeLength(value, min, max,
                "Check failed - the first argument must between min and max length.");
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小长度
     * @param max     最大长度
     * @param message 异常信息
     * @param <T>     对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireRangeLength(Map<T, ?> value, int min, int max, CharSequence message) {
        if (value != null && !(value.size() <= max && value.size() >= min)) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不超过最大长度，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小长度
     * @param max   最大长度
     * @param <T>   对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static <T> Map<T, ?> requireRangeLength(Map<T, ?> value, int min, int max) {
        return requireRangeLength(value, min, max,
                "Check failed - the first argument must between min and max length.");
    }


    /**
     * 对象需要不小于最小值，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireGreaterEqualThan(String value, Number min, CharSequence message) {
        if (!(ValidationMethods.min(value, min))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不小于最小值，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireGreaterEqualThan(String value, Number min) {
        return requireGreaterEqualThan(value, min,
                "Check failed - the first argument must greater or equal than min value.");
    }

    /**
     * 对象需要不小于最小值，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireGreaterEqualThan(Number value, Number min, CharSequence message) {
        if (!(ValidationMethods.min(value, min))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不小于最小值，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireGreaterEqualThan(Number value, Number min) {
        return requireGreaterEqualThan(value, min,
                "Check failed - the first argument must greater or equal than min value.");
    }

    /**
     * 对象需要不大于最大值，否则抛出异常。
     *
     * @param value   对象
     * @param max     最大值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireLessEqualThan(String value, Number max, CharSequence message) {
        if (!(ValidationMethods.max(value, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不大于最大值，否则抛出异常。
     *
     * @param value 对象
     * @param max   最大值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireLessEqualThan(String value, Number max) {
        return requireLessEqualThan(value, max,
                "Check failed - the first argument must less or equal than max value.");
    }

    /**
     * 对象需要不大于最大值，否则抛出异常。
     *
     * @param value   对象
     * @param max     最大值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireLessEqualThan(Number value, Number max, CharSequence message) {
        if (!(ValidationMethods.max(value, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要不大于最大值，否则抛出异常。
     *
     * @param value 对象
     * @param max   最大值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireLessEqualThan(Number value, Number max) {
        return requireLessEqualThan(value, max,
                "Check failed - the first argument must less or equal than max value.");
    }


    /**
     * 对象需要在最小值与最大值之间，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小值
     * @param max     最大值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireRange(String value, Number min, Number max, CharSequence message) {
        if (!(ValidationMethods.range(value, min, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要在最小值与最大值之间，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小值
     * @param max   最大值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireRange(String value, Number min, Number max) {
        return requireRange(value, min, max,
                "Check failed - the first argument must between min and max value.");
    }

    /**
     * 对象需要在最小值与最大值之间，否则抛出异常。
     *
     * @param value   对象
     * @param min     最小值
     * @param max     最大值
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireRange(Number value, Number min, Number max, CharSequence message) {
        if (!(ValidationMethods.range(value, min, max))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要在最小值与最大值之间，否则抛出异常。
     *
     * @param value 对象
     * @param min   最小值
     * @param max   最大值
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Number requireRange(Number value, Number min, Number max) {
        return requireRange(value, min, max,
                "Check failed - the first argument must between min and max value.");
    }

    /**
     * 对象需要为日期格式，否则抛出异常。
     * <pre>
     *     日期格式：yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy/MM/dd HH:mm:ss, yyyy/MM/dd, yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss.SSSXXX, dd MMM yyyy
     * </pre>
     *
     * @param value   对象
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Date requireDate(String value, CharSequence message) {
        try {
            return DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss",
                    "yyyy/MM/dd", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "dd MMM yyyy");
        } catch (Exception e) {
        }
        Exceptions.runtime(message, "FOSS-REQ-014");
        return null;
    }

    /**
     * 对象需要为日期格式，否则抛出异常。
     * <pre>
     *     日期格式：yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy/MM/dd HH:mm:ss, yyyy/MM/dd, yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss.SSSXXX, dd MMM yyyy
     * </pre>
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static Date requireDate(String value) {
        return requireDate(value,
                "Check failed - the first argument must be date format.");
    }

    /**
     * 对象需要为ipv4格式，否则抛出异常。
     *
     * @param value   对象
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireIpv4(String value, CharSequence message) {
        if (!(ValidationMethods.ipv4(value))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要为ipv4格式，否则抛出异常。
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireIpv4(String value) {
        return requireIpv4(value,
                "Check failed - the first argument must be ipv4 format.");
    }

    /**
     * 对象需要为ipv6格式，否则抛出异常。
     *
     * @param value   对象
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireIpv6(String value, CharSequence message) {
        if (!(ValidationMethods.ipv6(value))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要为ipv6格式，否则抛出异常。
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireIpv6(String value) {
        return requireIpv6(value,
                "Check failed - the first argument must be ipv6 format.");
    }

    /**
     * 对象需要匹配指定的正则表达式，否则抛出异常。
     *
     * @param value   对象
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMatch(String value, String pattern, CharSequence message) {
        if (!(ValidationMethods.pattern(value, pattern))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要匹配指定的正则表达式，否则抛出异常。
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireMatch(String value, String pattern) {
        return requireMatch(value, pattern,
                "Check failed - the first argument must match the pattern.");
    }

    /**
     * 对象需要为中文，否则抛出异常。
     *
     * @param value   对象
     * @param message 异常信息
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireChinese(String value, CharSequence message) {
        if (!(ValidationMethods.chinese(value))) {
            Exceptions.runtime(message, "FOSS-REQ-014");
        }
        return value;
    }

    /**
     * 对象需要为中文，否则抛出异常。
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     */
    public static String requireChinese(String value) {
        return requireChinese(value,
                "Check failed - the first argument must be chinese character.");
    }


    /**
     * 把对象的null值删除。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * CharSequence : str
     * =============:======
     * </pre>
     *
     * @param value 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static String removeNull(String value) {
        if (value == null) {
            return null;
        }
        return value;
    }

    /**
     * 把对象的null值删除。
     * <pre>
     * 如果当前 Collection 可以被修改就直接修改，否则返回一个新的 Collection 。
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Collection   : remove null value, [null, 1, " a ", " "] => [1, " a ", " "]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象，仅返回ArrayList, SortedSet, Set
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> Collection<T> removeNull(Collection<T> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Collection 可以被修改就直接修改，否则返回一个新的 Collection 。
            Object[] array = object.toArray();
            object.clear();
            for (Object obj : array) {
                if (obj != null) {
                    object.add((T) obj);
                }
            }
            return object;
        } catch (Exception e) {
            Collection clt = new ArrayList<T>();
            if (object instanceof SortedSet) {
                clt = new TreeSet<T>();
            } else if (object instanceof Set) {
                clt = new HashSet<T>();
            }
            Object[] array = ((Collection) object).toArray();
            for (Object obj : array) {
                if (obj != null) {
                    clt.add((T) obj);
                }
            }
            return clt;
        }
    }

    /**
     * 把对象的null值删除。
     * <pre>
     * 如果当前 Map 可以被修改就直接修改，否则返回一个新的 Map 。
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Map          : remove null value, {"1": null, "2": " a ", " ": " "} => {"2": " a ", " ": " "}
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static Map<?, ?> removeNull(Map<?, ?> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Map 可以被修改就直接修改，否则返回一个新的 Map 。
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                if (key == null || object.get(key) == null) {
                    object.remove(key);
                }
            }
            return object;
        } catch (Exception e) {
            Map result = new HashMap();
            if (object instanceof SortedMap) {
                result = new TreeMap();
            } else if (object instanceof LinkedHashMap) {
                result = new LinkedHashMap();
            }
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                if (key == null || object.get(key) == null) {
                    continue;
                }
                result.put(key, object.get(key));
            }
            return result;
        }
    }

    /**
     * 把对象的null值删除。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Array        : remove null value, [null, 1, " a ", " "] => [1, " a ", " "]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> T[] removeNull(T[] object) {
        if (object == null || object.length == 0) {
            return object;
        }
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
        return (T[]) newInstance;
    }

    /**
     * 把对象的null值及空值删除。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * String : StringUtils.isBlank(object) ? "" : object
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static String removeBlank(String object) {
        return object == null ? object : (StringUtils.isBlank(object) ? "" : object);
    }

    /**
     * 把对象的null值及空值删除。
     * <pre>
     * 如果当前 Collection 可以被修改就直接修改，否则返回一个新的 Collection 。
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Collection   : remove null value and blank value, [null, 1, " a ", " "] => [1, " a "]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> Collection<T> removeBlank(Collection<T> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Collection 可以被修改就直接修改，否则返回一个新的 Collection 。
            Object[] array = object.toArray();
            object.clear();
            for (Object obj : array) {
                if (obj != null && !(obj instanceof CharSequence && StringUtils.isBlank((CharSequence) obj))) {
                    object.add((T) obj);
                }
            }
            return object;
        } catch (Exception e) {
            Collection clt = new ArrayList<T>();
            if (object instanceof SortedSet) {
                clt = new TreeSet<T>();
            } else if (object instanceof Set) {
                clt = new HashSet<T>();
            }
            Object[] array = ((Collection) object).toArray();
            for (Object obj : array) {
                if (obj != null && !(obj instanceof CharSequence && StringUtils.isBlank((CharSequence) obj))) {
                    clt.add((T) obj);
                }
            }
            return clt;
        }
    }

    /**
     * 把对象的null值及空值删除。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Map          : remove null value and blank value, {"1": null, "2": " a ", " ": " "} => {"2": " a "}
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static Map<?, ?> removeBlank(Map<?, ?> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Map 可以被修改就直接修改，否则返回一个新的 Map 。
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                Object key1 = (key instanceof CharSequence) && StringUtils.isBlank((CharSequence) key) ? null : key;
                Object value = object.get(key);
                value = (value instanceof CharSequence) && StringUtils.isBlank((CharSequence) value) ? null : value;
                if (key1 == null || value == null) {
                    object.remove(key);
                }
            }
            return object;
        } catch (Exception e) {
            Map result = new HashMap();
            if (object instanceof SortedMap) {
                result = new TreeMap();
            } else if (object instanceof LinkedHashMap) {
                result = new LinkedHashMap();
            }
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                Object key1 = (key instanceof CharSequence) && StringUtils.isBlank((CharSequence) key) ? null : key;
                Object value = object.get(key);
                value = (value instanceof CharSequence) && StringUtils.isBlank((CharSequence) value) ? null : value;
                if (key1 == null || value == null) {
                    continue;
                }
                result.put(key, object.get(key));
            }
            return result;
        }
    }

    /**
     * 把对象的null值及空值删除。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Array        : remove null value and blank value, [null, 1, " a ", " "] => [1, " a "]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> T[] removeBlank(T[] object) {
        if (object == null || object.length < 1) {
            return object;
        }
        int len = Array.getLength(object);
        Object newInstance = Array.newInstance(object.getClass().getComponentType(), len);
        int count = 0;
        for (int i = 0; i < len; i++) {
            Object obj = Array.get(object, i);
            obj = (obj instanceof CharSequence) && StringUtils.isBlank((CharSequence) obj) ? null : obj;
            if (obj != null) {
                Array.set(newInstance, count, obj);
                count++;
            }
        }
        return (T[]) ArrayUtils.subarray((T[]) newInstance, 0, count);
    }

    /**
     * 把对象的null值及Blank值删除，并把字符串进行trim。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * String : blank=null or str.trim()
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static String trimAndRemoveBlank(String object) {
        if (object == null || object.length() < 1) {
            return object;
        }
        return StringUtils.trim(object);
    }

    /**
     * 把对象的null值及Blank值删除，并把字符串进行trim。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Collection   : remove null value and trim string value, [null, 1, " a ", ""] => [1, "a"]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> Collection<T> trimAndRemoveBlank(Collection<T> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Collection 可以被修改就直接修改，否则返回一个新的 Collection 。
            Object[] array = object.toArray();
            object.clear();
            for (Object obj : array) {
                if (obj != null
                        && !(obj instanceof String && StringUtils.trim((String) obj).length() < 1
                        || obj instanceof CharSequence && StringUtils.isBlank((CharSequence) obj))) {
                    object.add((T) obj);
                }
            }
            return object;
        } catch (Exception e) {
            Collection clt = new ArrayList<T>();
            if (object instanceof SortedSet) {
                clt = new TreeSet<T>();
            } else if (object instanceof Set) {
                clt = new HashSet<T>();
            }
            Object[] array = ((Collection) object).toArray();
            for (Object obj : array) {
                if (obj != null
                        && !(obj instanceof String && StringUtils.trim((String) obj).length() < 1
                        || obj instanceof CharSequence && StringUtils.isBlank((CharSequence) obj))) {
                    clt.add((T) obj);
                }
            }
            return clt;
        }
    }

    /**
     * 把对象的null值及Blank值删除，并把字符串进行trim。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Map          : remove null value and trim string value, {"1": null, " 2 ": " a ", "3": ""} => {"2": "a"}
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static Map<?, ?> trimAndRemoveBlank(Map<?, ?> object) {
        if (object == null || object.isEmpty()) {
            return object;
        }
        try {//如果当前 Map 可以被修改就直接修改，否则返回一个新的 Map 。
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                Object key1 = key instanceof String && StringUtils.trim((String) key).length() < 1
                        || (key instanceof CharSequence) && StringUtils.isBlank((CharSequence) key)
                        ? null : key;
                Object value = object.get(key);
                value = value instanceof String && StringUtils.trim((String) value).length() < 1
                        || (value instanceof CharSequence) && StringUtils.isBlank((CharSequence) value)
                        ? null : value;
                if (key1 == null || value == null) {
                    object.remove(key);
                }
            }
            return object;
        } catch (Exception e) {
            Map result = new HashMap();
            if (object instanceof SortedMap) {
                result = new TreeMap();
            } else if (object instanceof LinkedHashMap) {
                result = new LinkedHashMap();
            }
            Object[] array = object.keySet().toArray();
            for (Object key : array) {
                Object key1 = key instanceof String && StringUtils.trim((String) key).length() < 1
                        || (key instanceof CharSequence) && StringUtils.isBlank((CharSequence) key)
                        ? null : key;
                Object value = object.get(key);
                value = value instanceof String && StringUtils.trim((String) value).length() < 1
                        || (value instanceof CharSequence) && StringUtils.isBlank((CharSequence) value)
                        ? null : value;
                if (key1 == null || value == null) {
                    continue;
                }
                result.put(key, object.get(key));
            }
            return result;
        }
    }

    /**
     * 把对象的null值及Blank值删除，并把字符串进行trim。
     * <pre>
     * 输入值        : 返回值
     * =============:======
     * null         : null
     * Array        : remove null value and trim string value, [null, 1, " a ", ""] => [1, "a"]
     * =============:======
     * </pre>
     *
     * @param object 对象
     * @param <T>    对象的类型
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static <T> T[] trimAndRemoveBlank(T[] object) {
        if (object == null || object.length < 1) {
            return object;
        }
        int len = Array.getLength(object);
        Object newInstance = Array.newInstance(object.getClass().getComponentType(), len);
        int count = 0;
        for (int i = 0; i < len; i++) {
            Object obj = Array.get(object, i);
            obj = obj instanceof String && StringUtils.trim((String) obj).length() < 1
                    || (obj instanceof CharSequence) && StringUtils.isBlank((CharSequence) obj) ? null : obj;
            if (obj != null) {
                Array.set(newInstance, count, obj);
                count++;
            }
        }
        return (T[]) ArrayUtils.subarray((T[]) newInstance, 0, count);
    }

    /**
     * 如果为null，则返回空字符串。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static String nullToEmpty(String object) {
        return object == null ? "" : object;
    }

    /**
     * 如果为null，则返回new ArrayList()。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static Collection<?> nullToEmpty(Collection<?> object) {
        return object == null ? new ArrayList() : object;
    }

    /**
     * 如果为null，则返回new LinkedHashMap()。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    @SuppressWarnings("rawtypes")
    public static Map<?, ?> nullToEmpty(Map<?, ?> object) {
        return object == null ? new LinkedHashMap() : object;
    }

    /**
     * 如果为null，则返回new Object[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Object[] nullToEmpty(Object[] object) {
        return object == null ? new Object[0] : object;
    }

    /**
     * 如果为null，则返回new boolean[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static boolean[] nullToEmpty(boolean[] object) {
        return object == null ? new boolean[0] : object;
    }

    /**
     * 如果为null，则返回new short[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static short[] nullToEmpty(short[] object) {
        return object == null ? new short[0] : object;
    }

    /**
     * 如果为null，则返回new int[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static int[] nullToEmpty(int[] object) {
        return object == null ? new int[0] : object;
    }

    /**
     * 如果为null，则返回new long[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static long[] nullToEmpty(long[] object) {
        return object == null ? new long[0] : object;
    }

    /**
     * 如果为null，则返回new float[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static float[] nullToEmpty(float[] object) {
        return object == null ? new float[0] : object;
    }

    /**
     * 如果为null，则返回new double[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static double[] nullToEmpty(double[] object) {
        return object == null ? new double[0] : object;
    }

    /**
     * 如果为null，则返回new Boolean[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Boolean[] nullToEmpty(Boolean[] object) {
        return object == null ? new Boolean[0] : object;
    }

    /**
     * 如果为null，则返回new Short[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Short[] nullToEmpty(Short[] object) {
        return object == null ? new Short[0] : object;
    }

    /**
     * 如果为null，则返回new Integer[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Integer[] nullToEmpty(Integer[] object) {
        return object == null ? new Integer[0] : object;
    }

    /**
     * 如果为null，则返回new Long[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Long[] nullToEmpty(Long[] object) {
        return object == null ? new Long[0] : object;
    }

    /**
     * 如果为null，则返回new Float[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Float[] nullToEmpty(Float[] object) {
        return object == null ? new Float[0] : object;
    }

    /**
     * 如果为null，则返回new Double[0]。
     *
     * @param object 对象
     * @return 对象
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static Double[] nullToEmpty(Double[] object) {
        return object == null ? new Double[0] : object;
    }

    /**
     * 测试是否为空，为空情况：null，空字符串，空列表，空Map。
     *
     * @param object 对象
     * @return true if is empty, else return false.
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
     *
     * @param object 对象
     * @return true if is not empty, else return false.
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Mar 4, 2018
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 测试两个是否相等，使用equals判断，相等则返回 NULL ，否则返回原对象。
     *
     * @param object 对象
     * @param equal  对象
     * @param <T>    对象类型
     * @return null if is equal, else return object.
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since 2018-06-29
     */
    public static <T> T equalsToNull(T object, T equal) {
        if (equal == null) {
            return object;
        } else if (object == null) {
            return null;
        } else {
            return (T) (object.equals(equal) ? null : object);
        }
    }
}
