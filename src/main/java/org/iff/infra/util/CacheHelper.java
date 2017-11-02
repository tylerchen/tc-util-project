/*******************************************************************************
 * Copyright (c) 2014-6-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-6-26
 */
public class CacheHelper {
	private static Cacheable cache = null;

	public static void init(Cacheable init) {
		if (init == null) {
			return;
		}
		CacheHelper.cache = init;
	}

	public static Cacheable getCache() {
		return cache == null ? (cache = new MapCacheable()) : cache;
	}

	public static <T> T get(String key) {
		return (T) getCache().get(key);
	}

	public static void set(String key, Object value) {
		getCache().set(key, value);
	}

	public static void set(String key, Object value, int seconds) {
		set(key, value, 0, seconds);
	}

	public static void set(String key, Object value, int timeToIdle, int timeToLive) {
		getCache().set(key, value, timeToIdle, timeToLive);
	}

	public static void del(String key) {
		getCache().del(key);
	}

	public interface Cacheable {

		<T> T get(String key);

		void set(String key, Object value);

		void set(String key, Object value, int seconds);

		void set(String key, Object value, int timeToIdle, int timeToLive);

		void del(String key);
	}

	public static class MapCacheable implements Cacheable {

		private Map<String, Object> map = new LinkedHashMap<String, Object>(1024);

		public void del(String key) {
			map.remove(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T get(String key) {
			return (T) map.get(key);
		}

		public void set(String key, Object value) {
			if (map.size() > 1000) {
				int count = 500;
				for (String k : map.keySet()) {
					if (count-- > 0) {
						map.remove(k);
					}
				}
			}
			map.put(key, value);
		}

		public void set(String key, Object value, int seconds) {
			set(key, value);
		}

		public void set(String key, Object value, int timeToIdle, int timeToLive) {
			set(key, value);
		}
	}

	public static class JedisCacheable implements Cacheable {

		public JedisCacheable() {
		}

		public void del(String key) {
			JedisHelper.del(key);
		}

		public <T> T get(String key) {
			return (T) JedisHelper.get(key);
		}

		public void set(String key, Object value) {
			JedisHelper.set(key, value);
		}

		public void set(String key, Object value, int seconds) {
			JedisHelper.set(key, value, seconds);
		}

		public void set(String key, Object value, int timeToIdle, int timeToLive) {
			JedisHelper.set(key, value, timeToLive);
		}
	}

	public static class EhCacheCacheable implements Cacheable {

		public EhCacheCacheable() {
		}

		public void del(String key) {
			EhcacheHelper.remove("DEFAULT_CACHE", key);
		}

		public <T> T get(String key) {
			return (T) EhcacheHelper.get("DEFAULT_CACHE", key);
		}

		public void set(String key, Object value) {
			EhcacheHelper.put("DEFAULT_CACHE", key, value);
		}

		public void set(String key, Object value, int seconds) {
			EhcacheHelper.put("DEFAULT_CACHE", key, value, seconds);
		}

		public void set(String key, Object value, int timeToIdle, int timeToLive) {
			EhcacheHelper.put("DEFAULT_CACHE", key, value, timeToIdle, timeToLive);
		}
	}

	public static class DisabledCacheable implements Cacheable {

		public <T> T get(String key) {
			return null;
		}

		public void set(String key, Object value) {
		}

		public void set(String key, Object value, int seconds) {
		}

		public void set(String key, Object value, int timeToIdle, int timeToLive) {
		}

		public void del(String key) {
		}
	}

	public static class CacheCallback<T> {
		public static <T> T process(String key, int timeToIdle, int timeToLive, CacheCallback<T> callback) {
			key = MD5Helper.string2MD5(key);
			try {// for cache
				Object value = CacheHelper.get(key);
				if (value != null) {
					return (T) value;
				}
			} catch (Exception e) {
				Logger.error("Cache is not available.", e);
			}
			T value = callback.call();
			try {//for cache
				CacheHelper.set(key, value, timeToIdle, timeToLive);
				callback.storeKey(key);
			} catch (Throwable e) {
				Logger.error("Cache is not available.", e);
			}
			return value;
		}

		public T call() {
			return null;
		}

		public void storeKey(String key) {
		}
	}
}
