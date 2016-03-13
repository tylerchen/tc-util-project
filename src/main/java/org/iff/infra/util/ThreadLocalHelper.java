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
 * a ThreadLocal util for get and set parameter on the current thread. 
 * the current thread hold a Map type.
 * <pre>
 * Usage:
 * set("test", 123)
 * get("test")=123
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 7, 2015
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ThreadLocalHelper {

	private static final ThreadLocal<Map> params = new ThreadLocal<Map>();

	/**
	 * setting value to current ThreadLocal map.
	 * @param name
	 * @param value
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 18, 2015
	 */
	public static void set(String name, Object value) {
		Map map = params.get();
		if (map == null) {
			map = new LinkedHashMap();
			params.set(map);
		}
		map.put(name, value);
	}

	/**
	 * getting value from current ThreadLocal map.
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 18, 2015
	 */
	public static <T> T get(String name) {
		Map map = params.get();
		if (map == null) {
			return null;
		}
		return (T) map.get(name);
	}

	/**
	 * remove thread variable for the thread pool.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Mar 13, 2016
	 */
	public static void remove() {
		params.remove();
	}
}
