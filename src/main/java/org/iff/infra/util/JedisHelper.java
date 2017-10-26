/*******************************************************************************
 * Copyright (c) 2014-6-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.iff.infra.util.KryoRedisSerializer;
import org.iff.infra.util.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

import com.google.common.collect.Lists;

/**
 * NOT Test!! yet!!
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-6-26
 */
@SuppressWarnings("unchecked")
public class JedisHelper {
	private static Pool<Jedis> pool;

	public static JedisSentinelPool get(String masterName, String sentinels, Map<String, String> jedisPoolConfig,
			int timeout, String password) {
		Set<String> sentinelSet = new HashSet<String>();
		{
			String[] splits = sentinels.split(",");
			for (String sentinel : splits) {
				sentinelSet.add(sentinel);
			}
		}
		JedisPoolConfig config = new JedisPoolConfig();
		{

			try {
				config.setMaxTotal(Integer.valueOf(jedisPoolConfig.get("jedis.maxTotal")));
			} catch (Exception e) {
			}
			try {
				config.setMaxIdle(Integer.valueOf(jedisPoolConfig.get("jedis.maxIdle")));
			} catch (Exception e) {
			}
			try {
				config.setMinIdle(Integer.valueOf(jedisPoolConfig.get("jedis.minIdle")));
			} catch (Exception e) {
			}
			try {
				config.setLifo(Boolean.valueOf(jedisPoolConfig.get("jedis.lifo")));
			} catch (Exception e) {
			}
			try {
				config.setMaxWaitMillis(Long.valueOf(jedisPoolConfig.get("jedis.maxWaitMillis")));
			} catch (Exception e) {
			}
			try {
				config.setMinEvictableIdleTimeMillis(
						Long.valueOf(jedisPoolConfig.get("jedis.minEvictableIdleTimeMillis")));
			} catch (Exception e) {
			}
			try {
				config.setSoftMinEvictableIdleTimeMillis(
						Long.valueOf(jedisPoolConfig.get("jedis.softMinEvictableIdleTimeMillis")));
			} catch (Exception e) {
			}
			try {
				config.setNumTestsPerEvictionRun(Integer.valueOf(jedisPoolConfig.get("jedis.numTestsPerEvictionRun")));
			} catch (Exception e) {
			}
			try {
				String value = jedisPoolConfig.get("jedis.evictionPolicyClassName");
				if (value != null && value.length() > 0) {
					config.setEvictionPolicyClassName(value);
				}
			} catch (Exception e) {
			}
			try {
				config.setTestOnBorrow(Boolean.valueOf(jedisPoolConfig.get("jedis.testOnBorrow")));
			} catch (Exception e) {
			}
			try {
				config.setTestOnReturn(Boolean.valueOf(jedisPoolConfig.get("jedis.testOnReturn")));
			} catch (Exception e) {
			}
			try {
				config.setTestWhileIdle(Boolean.valueOf(jedisPoolConfig.get("jedis.testWhileIdle")));
			} catch (Exception e) {
			}
			try {
				config.setTimeBetweenEvictionRunsMillis(
						Long.valueOf(jedisPoolConfig.get("jedis.timeBetweenEvictionRunsMillis")));
			} catch (Exception e) {
			}
			try {
				config.setBlockWhenExhausted(Boolean.valueOf(jedisPoolConfig.get("jedis.blockWhenExhausted")));
			} catch (Exception e) {
			}
		}
		return new JedisSentinelPool(masterName, sentinelSet, config, timeout, password);
	}

	/**
	 * must invoke this method to set the pool before use this kit.
	 * @param init
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2014-7-18
	 */
	public static void init(Pool<Jedis> init) {
		if (pool != null) {
			return;
		}
		pool = init;
	}

	public static List<Object> tx(JedisAtom jedisAtom) {
		Jedis jedis = pool.getResource();
		Transaction trans = jedis.multi();
		jedisAtom.action(trans);
		return trans.exec();

	}

	public static <T> T call(JedisAction<T> jedisAction) {
		T result = null;
		Jedis jedis = pool.getResource();
		try {
			result = jedisAction.action(jedis);
		} catch (Exception e) {
			Logger.error("[JedisAction.action(Jedis)]", e);
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}

	/***
	 * query one Object from Redis with key
	 * @param key using it get value from key-value database
	 * @return the Object which implements Serializable
	 */

	public static <T> T get(final String key) {
		return call(new JedisAction<T>() {
			public T action(Jedis jedis) {
				Object result = null;
				byte[] retVal = jedis.get(encode(key));
				if (null != retVal) {
					try {
						result = deserialize(retVal);
					} catch (Exception e) {
						result = encode(retVal);
					}
				}
				return (T) result;
			}
		});
	}

	/***
	 * set object to key-value database with the specified key
	 * 
	 * @param key
	 *            the unique key to indicate the value Object
	 * @param value
	 *            the value indicated by the key
	 * @return return true while the set operation is succeed,false by failed
	 */

	public static Boolean set(final String key, final Object value) {
		return call(new JedisAction<Boolean>() {

			public Boolean action(Jedis jedis) {
				String retVal;
				if (value instanceof String) {
					retVal = jedis.set(key, (String) value);
				} else {
					retVal = jedis.set(encode(key), serialize(value));
				}
				return "OK".equalsIgnoreCase(retVal);
			}
		});
	}

	/***
	 * set object to key-value database with the specified key and EXPIRE time
	 * 
	 * @param key
	 *            the unique key to indicate the value Object
	 * @param value
	 *            the value indicated by the key
	 * @param seconds
	 *            EXPIRE time
	 * @return return true while the set operation is succeed,false by failed
	 */

	public static Boolean set(final String key, final Object value, final int seconds) {
		return call(new JedisAction<Boolean>() {
			public Boolean action(Jedis jedis) {
				byte[] bytes;
				if (value instanceof String) {
					bytes = encode((String) value);
				} else {
					bytes = serialize(value);
				}
				String retVal = jedis.setex(encode(key), seconds, bytes);
				return "OK".equalsIgnoreCase(retVal);
			}
		});
	}

	/***
	 * query multiple Object from Redis with key
	 * 
	 * @param keys
	 *            using it get value from key-value database
	 * @return the Object list which implements Serializable
	 */

	public static List mquery(final String... keys) {

		return call(new JedisAction<List>() {

			public List action(Jedis jedis) {
				List result = new ArrayList(keys.length);
				for (int index = 0; index < keys.length; index++)
					result.add(null);
				byte[][] encodeKeys = new byte[keys.length][];
				for (int i = 0; i < keys.length; i++)
					encodeKeys[i] = encode(keys[i]);
				List<byte[]> retVals = jedis.mget(encodeKeys);
				if (null != retVals) {
					int index = 0;
					for (byte[] val : retVals) {
						if (null != val)
							result.set(index, deserialize(val));
						index++;
					}
				}
				return result;
			}
		});
	}

	public static List<String> mqueryStr(final String... keys) {

		return call(new JedisAction<List<String>>() {

			public List<String> action(Jedis jedis) {
				return jedis.mget(keys);
			}
		});
	}

	public static Boolean msaveOrUpdate(final Map<String, Object> values) {
		return call(new JedisAction<Boolean>() {

			public Boolean action(Jedis jedis) {
				byte[][] encodeValues = new byte[values.size() * 2][];
				int index = 0;
				Iterator<Entry<String, Object>> iter = values.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> entry = iter.next();
					encodeValues[index++] = entry.getKey().getBytes();
					encodeValues[index++] = serialize(entry.getValue());
				}
				String retVal = jedis.mset(encodeValues);
				return "OK".equalsIgnoreCase(retVal);
			}
		});
	}

	public static Boolean msaveOrUpdateStr(final Map<String, String> values) {

		return call(new JedisAction<Boolean>() {

			public Boolean action(Jedis jedis) {
				Iterator<Entry<String, String>> iter = values.entrySet().iterator();
				int index = 0;
				String[] encodeValues = new String[values.size() * 2];
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					encodeValues[index++] = entry.getKey();
					encodeValues[index++] = entry.getValue();
				}
				return "OK".equalsIgnoreCase(jedis.mset(encodeValues));
			}
		});
	}

	/**
	 * query keys set by pattern
	 */

	public static Set<String> keys(final String pattern) {
		return call(new JedisAction<Set<String>>() {

			public Set<String> action(Jedis jedis) {
				return jedis.keys(pattern);
			}
		});

	}

	public static Long del(final String... keys) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				byte[][] encodeKeys = new byte[keys.length][];
				for (int i = 0; i < keys.length; i++)
					encodeKeys[i] = encode(keys[i]);
				return jedis.del(encodeKeys);
			}
		});
	}

	public static Long listAdd(final String key, final Object value) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.rpush(encode(key), serialize(value));
			}
		});
	}

	public static Long listAddFirst(final String key, final Object value) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.lpush(encode(key), serialize(value));
			}
		});
	}

	public static String type(final String key) {
		return call(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.type(encode(key));
			}
		});
	}

	public static List queryList(final String key, final int start, final int end) {
		return call(new JedisAction<List>() {
			public List action(Jedis jedis) {
				List result = Lists.newArrayList();
				List<byte[]> retVals = jedis.lrange(encode(key), start, end);
				if (retVals != null) {
					for (byte[] val : retVals) {
						if (null != val) {
							result.add(deserialize(val));
						}
					}
				}
				return result;
			}
		});
	}

	public static Long listSize(final String key) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.llen(encode(key));
			}
		});
	}

	public static Boolean listTrim(final String key, final int start, final int end) {
		return call(new JedisAction<Boolean>() {
			public Boolean action(Jedis jedis) {
				return "OK".equalsIgnoreCase(jedis.ltrim(encode(key), start, end));
			}
		});
	}

	public static Long incrementAndGet(final String key) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	public static Long decrementAndGet(final String key) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	public static Long queryLong(final String key) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return Long.valueOf(jedis.get(key));
			}
		});
	}

	public static Boolean hmset(final String key, final Map<String, String> values) {
		return call(new JedisAction<Boolean>() {
			public Boolean action(Jedis jedis) {
				return "OK".equals(jedis.hmset(key, values));
			}
		});
	}

	public static List<String> hvals(final String key) {
		return call(new JedisAction<List<String>>() {
			public List<String> action(Jedis jedis) {
				return jedis.hvals(key);
			}
		});
	}

	public static List<String> hmget(final String key, final String... fields) {
		return call(new JedisAction<List<String>>() {
			public List<String> action(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		});
	}

	public static Double zincrby(final String key, final double score, final String member) {
		return call(new JedisAction<Double>() {
			public Double action(Jedis jedis) {
				return jedis.zincrby(key, score, member);
			}
		});
	}

	public static Double zscore(final String key, final String score) {
		return call(new JedisAction<Double>() {
			public Double action(Jedis jedis) {
				return jedis.zscore(key, score);
			}
		});
	}

	public static Long zadd(final String key, final double score, final String member) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				return jedis.zadd(key, score, member);
			}
		});
	}

	public static Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		return call(new JedisAction<Set<Tuple>>() {
			public Set<Tuple> action(Jedis jedis) {
				return jedis.zrangeWithScores(key, start, end);
			}
		});
	}

	public static String watch(final String... keys) {
		return call(new JedisAction<String>() {
			public String action(Jedis jedis) {
				return jedis.watch(keys);
			}
		});
	}

	public static Long lpush(final String key, final Object value) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				Long retVal;
				if (value instanceof String) {
					retVal = jedis.lpush(key, (String) value);
				} else {
					retVal = jedis.lpush(encode(key), serialize(value));
				}
				return retVal;
			}
		});
	}

	public static <T> T rpop(final String key) {
		return call(new JedisAction<T>() {
			public T action(Jedis jedis) {
				Object result = null;
				byte[] retVal = jedis.rpop(encode(key));
				if (null != retVal) {
					try {
						result = deserialize(retVal);
					} catch (Exception e) {
						result = encode(retVal);
					}
				}
				return (T) result;
			}
		});
	}

	public static List lrange(final String key, final long start, final long end) {
		return call(new JedisAction<List>() {
			public List action(Jedis jedis) {
				List list = Lists.newArrayList();
				List<byte[]> results = jedis.lrange(encode(key), start, end);
				for (byte[] result : results) {
					try {
						list.add(deserialize(result));
					} catch (Exception e) {
						list.add(encode(result));
					}
				}
				return list;
			}
		});
	}

	public static <T> T rpoplpush(final String srckey, final String dstkey) {
		return call(new JedisAction<T>() {
			public T action(Jedis jedis) {
				Object result = null;
				byte[] retVal = jedis.rpoplpush(encode(srckey), encode(dstkey));
				if (null != retVal) {
					try {
						result = deserialize(retVal);
					} catch (Exception e) {
						result = encode(retVal);
					}
				}
				return (T) result;
			}
		});
	}

	public static Long lrem(String key, Object value) {
		return lrem(key, 1, value);
	}

	public static Long lrem(final String key, final long count, final Object value) {
		return call(new JedisAction<Long>() {
			public Long action(Jedis jedis) {
				Long retVal;
				if (value instanceof String) {
					retVal = jedis.lrem(key, count, (String) value);
				} else {
					retVal = jedis.lrem(encode(key), count, serialize(value));
				}
				return retVal;
			}
		});
	}

	public static byte[] encode(final String str) {
		return SafeEncoder.encode(str);
	}

	public static String encode(final byte[] str) {
		return SafeEncoder.encode(str);
	}

	public static byte[] serialize(Object value) {
		return KryoRedisSerializer.serialize(value);
	}

	public static <T> T deserialize(byte[] value) {
		return KryoRedisSerializer.deserialize(value);
	}

	public static interface JedisAction<T> {
		T action(Jedis jedis);
	}

	public static interface JedisAtom {
		void action(Transaction transaction);
	}

	public static interface JedisMessage<T> {
		public void onMessage(T message);
	}
}
