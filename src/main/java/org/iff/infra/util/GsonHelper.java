/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
@SuppressWarnings("unchecked")
public class GsonHelper {

	private GsonHelper() {
	}

	public static Map toJsonMap(String json) {
		Gson gson = JsonHelper.GSON;
		LinkedHashMap fromJson = gson.fromJson(json, LinkedHashMap.class);
		return fromJson;
	}

	public static String toJsonString(Object o) {
		Gson gson = JsonHelper.GSON;
		return gson.toJson(o);
	}
}
