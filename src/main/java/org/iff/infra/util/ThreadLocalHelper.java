/*******************************************************************************
 * Copyright (c) Aug 7, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 7, 2015
 */
public class ThreadLocalHelper {

	private static ThreadLocal<Map> params = new ThreadLocal<Map>();

	public static void set(String name, Object value) {
		Map map = params.get();
		if (map == null) {
			map = new LinkedHashMap();
			params.set(map);
		}
		map.put(name, value);
	}

	public static <T> T get(String name) {
		Map map = params.get();
		if (map == null) {
			return null;
		}
		return (T) map.get(name);
	}
}
