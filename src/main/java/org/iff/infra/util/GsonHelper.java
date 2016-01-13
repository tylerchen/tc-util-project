/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
public class GsonHelper {

	private GsonHelper() {
	}

	@SuppressWarnings("rawtypes")
	public static Map toJsonMap(String json) {
		Gson gson = JsonHelper.GSON;
		LinkedHashMap fromJson = gson.fromJson(json, LinkedHashMap.class);
		return fromJson;
	}

	@SuppressWarnings("rawtypes")
	public static List toJsonList(String json) {
		Gson gson = JsonHelper.GSON;
		List fromJson = (List) gson.fromJson(json, ArrayList.class);
		return fromJson;
	}

	@SuppressWarnings(value = "unchecked")
	public static <T> T toJson(String json) {
		if (json != null && (json = json.trim()).startsWith("[")) {
			return (T) toJsonList(json);
		} else {
			return (T) toJsonMap(json);
		}
	}

	public static String toJsonString(Object o) {
		Gson gson = JsonHelper.GSON;
		return gson.toJson(o);
	}
}
