/*******************************************************************************
 * Copyright (c) 2014-6-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-6-26
 */
public class EhcacheHelper {
	private static CacheManager manager;

	public static void init(CacheManager init) {
		if (manager != null) {
			return;
		}
		manager = init;
	}

	public static Cache get(String name) {
		return manager.getCache(name);
	}

	public static void put(String name, Object key, Object value) {
		get(name).put(new Element(key, value));
	}

	public static void put(String name, Object key, Object value, int seconds) {
		put(name, key, value, 0, seconds);
	}

	public static void put(String name, Object key, Object value, int timeToIdle, int timeToLive) {
		get(name).put(new Element(key, value, timeToIdle, timeToLive));
	}

	public static <T> T get(String name, String key) {
		Element element = get(name).get(key);
		if (element == null) {
			return null;
		}
		return (T) element.getObjectValue();
	}

	public static boolean remove(String name, String key) {
		return get(name).remove(key);
	}

	public static byte[] serialize(Object value) {
		return KryoRedisSerializer.serialize(value);
	}

	public static <T> T deserialize(byte[] value) {
		return KryoRedisSerializer.deserialize(value);
	}
}
