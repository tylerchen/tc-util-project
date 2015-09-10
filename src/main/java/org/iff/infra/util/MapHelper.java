/*******************************************************************************
 * Copyright (c) 2014-2-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
import java.util.Map.Entry;
import java.util.Set;

/**
 * A Map helper
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-2-26
 */
public class MapHelper {

	/**
	 * <pre>
	 * Usage : toMap("KEY1", value1, "KEY2", value2);
	 * Expect: map = new LinkedHashMap(); map.put("KEY1", value1); map.put("KEY2", value2);
	 * </pre>
	 * @param params
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static Map toMap(Object... params) {
		Map map = new LinkedHashMap();
		Assert.notNull(params);
		Assert.isTrue(params.length % 2 == 0);
		for (int i = 0; i < params.length; i++) {
			map.put(params[i++], params[i]);
		}
		return map;
	}

	/**
	 * <pre>
	 * Usage : fillMap(map, "KEY1", value1, "KEY2", value2);
	 * Expect: map.put("KEY1", value1); map.put("KEY2", value2);
	 * </pre>
	 * @param map
	 * @param params
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static Map fillMap(Map map, Object... params) {
		map = map == null ? new LinkedHashMap() : map;
		Assert.notNull(params);
		Assert.isTrue(params.length % 2 == 0);
		for (int i = 0; i < params.length; i++) {
			map.put(params[i++], params[i]);
		}
		return map;
	}

	/**
	 * get map value by path, 'a/b/c' -&gt; map.get('a').get('b').get('c')
	 * @param map
	 * @param path
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static Object getByPath(Map map, String path) {
		if (path == null || map == null || map.isEmpty()) {
			return null;
		}
		String[] paths = path.split("/");
		Map tmp = map;
		for (int i = 0; i < paths.length - 1; i++) {
			if (paths[i].length() < 1) {// consider: /a//b -> /a/b -> a/b
				continue;
			}
			Object value = tmp.get(paths[i]);
			if (value instanceof Map) {
				tmp = (Map) value;
			} else {
				return null;
			}
		}
		return tmp.get(paths[paths.length - 1]);
	}

	/**
	 * set map value by path, 'a/b/c' -&gt; map.get('a').get('b').set('c', value)
	 * @param map
	 * @param path
	 * @param value
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static void setByPath(Map map, String path, Object value) {
		if (path == null || map == null) {
			return;
		}
		String[] paths = path.split("/");
		Map tmp = map;
		for (int i = 0; i < paths.length - 1; i++) {
			if (paths[i].length() < 1) {// consider: /a//b -> /a/b -> a/b
				continue;
			}
			Object val = tmp.get(paths[i]);
			if (val == null) {
				val = new LinkedHashMap<String, Object>();
				tmp.put(paths[i], val);
			}
			if (!(val instanceof Map)) {
				Logger.error(FCS.get("[MapHelper.setByPath][{0}][{1}][ISNOTMAP][{2}]", path, paths[i], val.getClass()
						.getName()));
				return;
			} else {
				tmp = (Map) val;
			}
		}
		tmp.put(paths[paths.length - 1], value);
	}

	/**
	 * <pre>
	 * combine to map values.
	 * Usage:
	 * Map map1 = toMap("aaa", 123, "bbb", 123);
	 * Map map2 = toMap("aaaa", 123, "bbb", toMap("AAA", "123", "BBBB", toMap("@@@", "333")));
	 * System.out.println(combine(map1, map2));
	 * Result:
	 * {aaa=123, bbb={AAA=123, BBBB={@@@=333}}, aaaa=123}
	 * </pre>
	 * @param beCombineMap
	 * @param toCombineMap
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 28, 2015
	 */
	public static Map combine(Map beCombineMap, Map toCombineMap) {
		List<Map> p1 = new ArrayList<Map>(128);
		List<Map> p2 = new ArrayList<Map>(128);
		{
			p1.add(beCombineMap);
			p2.add(toCombineMap);
		}
		while (!p2.isEmpty()) {
			Map map1 = p1.remove(p1.size() - 1);
			Map map2 = p2.remove(p2.size() - 1);
			for (Entry entry : (Set<Entry>) map2.entrySet()) {
				if (entry.getValue() instanceof Map) {
					Object object = map1.get(entry.getKey());
					if (object == null) {
						object = new LinkedHashMap();
						map1.put(entry.getKey(), object);
						{
							p1.add((Map) object);
							p2.add((Map) entry.getValue());
						}
					} else if (!(object instanceof Map)) {
						Logger.warn(FCS.get("[MapHelper.combine][ISNOTMAP][{0}][Result will be overwrite]",
								entry.getKey()));
						object = new LinkedHashMap();
						map1.put(entry.getKey(), object);
						{
							p1.add((Map) object);
							p2.add((Map) entry.getValue());
						}
					}
				} else {
					map1.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return beCombineMap;
	}

	public static void main(String[] args) {

	}
}
