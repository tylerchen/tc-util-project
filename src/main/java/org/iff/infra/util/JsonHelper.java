/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * json helper default use gson.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
@SuppressWarnings("unchecked")
public final class JsonHelper {

	/* gson use yyyy-MM-dd HH:mm:ss date format */
	public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	private JsonHelper() {
	}

	/**
	 * json string to object.
	 * @param clazz
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T toObject(Class<T> clazz, CharSequence json) {
		return (T) GSON.fromJson(json.toString(), clazz);
	}

	/**
	 * json string to List Object.
	 * @param clazz
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> List<T> toObjectList(Class<T> clazz, CharSequence json) {
		return (List<T>) GSON.fromJson(json.toString(), new TypeToken<List<T>>() {
		}.getType());
	}

	/**
	 * json string to list object by class name.
	 * @param className
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> List<T> toObjectList(String className, CharSequence json) {
		try {
			return (List<T>) GSON.fromJson(json.toString(), new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	/**
	 * object to json string.
	 * @param obj
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}
}
