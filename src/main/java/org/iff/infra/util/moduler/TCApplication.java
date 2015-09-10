/*******************************************************************************
 * Copyright (c) Aug 11, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.iff.infra.util.MapHelper;
import org.iff.infra.util.NumberHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 11, 2015
 */
public class TCApplication {

	private static TCApplication me = new TCApplication();
	private Map<String, String> props = new LinkedHashMap<String, String>();
	private TCServer server = null;

	public static TCApplication me() {
		return me;
	}

	public TCApplication loadSystemProperties() {
		for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue() == null ? "" : entry.getValue().toString();
			if (key.startsWith("tc_")) {
				props.put(key, value);
			}
		}
		return this;
	}

	public String getProp(String key) {
		return props.get(key);
	}

	public TCApplication initModule() {
		TCModuleManager.create(getProp("tc_app_path")).load();
		return this;
	}

	public TCApplication initByConf() {
		Integer port = Integer.valueOf(getProp("tc_port"));
		{// start server
			server = TCServer.create(port, getProp("tc_context"));
			server.start();
		}
		return this;
	}

	public Map<String, Object> getMenus() {
		Map menus = new HashMap();
		String[] modules = TCModuleManager.me().getModules();
		for (String moduleName : modules) {
			TCModule module = TCModuleManager.me().get(moduleName);
			Map<String, Map> config = module.getModuleConfig();
			Map menu = (Map) MapHelper.getByPath(config, "tcmodule/menus");
			MapHelper.combine(menus, menu);
		}
		List<Map> root = new ArrayList<Map>();
		{// root menu
			for (Entry entry : (Set<Entry>) menus.entrySet()) {
				Map mapLevel1 = (Map) entry.getValue();
				Map subMenus = (Map) mapLevel1.get("sub-menus");
				List<Map> subMenu = new ArrayList<Map>();
				for (Entry entrySub : (Set<Entry>) subMenus.entrySet()) {
					Map mapLevel2 = (Map) entrySub.getValue();
					Map menuItems = (Map) mapLevel2.get("menu-items");
					List<Map> menuItem = new ArrayList<Map>();
					for (Entry entryItem : (Set<Entry>) menuItems.entrySet()) {
						menuItem.add(removeMapValue((Map) entryItem.getValue()));
					}
					Collections.sort(menuItem, new MapOrderComparator());
					removeMapValue(mapLevel2).put("menuItems", menuItem);
					subMenu.add(mapLevel2);
				}
				Collections.sort(subMenu, new MapOrderComparator());
				removeMapValue(mapLevel1).put("subMenus", subMenu);
				root.add(mapLevel1);
			}
		}
		Collections.sort(root, new MapOrderComparator());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("root", root);
		return result;
	}

	private Map removeMapValue(Map map) {
		Set set = new HashSet(map.keySet());
		for (Object key : set) {
			if (map.get(key) instanceof Map) {
				map.remove(key);
			}
		}
		return map;
	}

	private class MapOrderComparator implements Comparator<Map> {

		public int compare(Map map1, Map map2) {
			float v1 = NumberHelper.get(map1.get("order"), 0F).floatValue();
			float v2 = NumberHelper.get(map2.get("order"), 0F).floatValue();
			return v1 == v2 ? 0 : v1 > v2 ? 1 : -1;
		}
	}
}
