/*******************************************************************************
 * Copyright (c) 2015-4-8 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * loading properties files and combine the properties by version.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-4-8
 */
public class PropertiesHelper {

	/**
	 * load properties files. default versionName=order.loading.configure
	 * @param resPaths such as: [classpath://META-INF/tc-framework.properties, file:///opt/tc-framework.properties]
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-5-28
	 */
	public static Map<String, String> loadPropertyFiles(final String[] resPaths) {
		return loadPropertyFiles(resPaths, "order.loading.configure");
	}

	/**
	 * load properties files.
	 * @param resPaths such as: [classpath://META-INF/tc-framework.properties, file:///opt/tc-framework.properties]
	 * @param versionName version name in properties file
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-5-28
	 */
	public static Map<String, String> loadPropertyFiles(final String[] resPaths, final String versionName) {
		Map<String, String> map = new HashMap<String, String>();
		Map<Long, List<Properties>> propMap = new HashMap<Long, List<Properties>>();
		List<String> list = new ArrayList<String>();
		for (String resPath : resPaths) {
			list.addAll(ResourceHelper.loadResources(resPath, ".properties", "*", ""));
		}
		loadPropertyFilesFromUrl(list, propMap, versionName);
		combineProperties(propMap, map);
		return map;
	}

	/**
	 * loading multi properties files to map by version.
	 * @param resPath properties file path, default as: classpath://META-INF/tc-framework.properties
	 * @param versionName properties file version key name
	 * @return properties to map
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-9
	 */
	public static Map<String, String> loadProperties(final String resPath, final String versionName) {
		return loadPropertyFiles(new String[] { resPath }, versionName);
	}

	/**
	 * load properties files form url.
	 * @param list
	 * @param propMap
	 * @param versionName
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-5-28
	 */
	private static void loadPropertyFilesFromUrl(List<String> list, Map<Long, List<Properties>> propMap,
			final String versionName) {
		for (String url : list) {
			FileInputStream is = null;
			try {
				File file = new File(new URL(url).toURI());
				{
					if (!file.exists() || !file.isFile()) {
						continue;
					}
				}
				Properties prop = new Properties();
				{
					is = new FileInputStream(file);
					prop.load(new StringReader(SocketHelper.getContent(is, false)));
				}
				Long version = 0L;
				{
					try {
						String versionStr = (String) prop.get(versionName);
						version = Long.valueOf(versionStr);
					} catch (Exception e) {
					}
				}
				{
					List<Properties> propList = propMap.get(version);
					if (propList == null) {
						propList = new ArrayList<Properties>();
						propMap.put(version, propList);
					}
					{
						propList.add(prop);
					}
				}
				Logger.debug(FCS.get("[org.iff.infra.util.PropertiesHelper.loadProperties][{file}]: loaded.", url));
			} catch (Exception e) {
				Logger.warn(
						FCS.get("[org.iff.infra.util.PropertiesHelper.loadProperties][{file}]: loading property file error! ",
								url));
			} finally {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * combine all properties by version
	 * @param propMap {version:propeties}
	 * @param targetMap
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-5-28
	 */
	private static void combineProperties(Map<Long, List<Properties>> propMap, Map<String, String> targetMap) {
		Long[] keys = propMap.keySet().toArray(new Long[propMap.keySet().size()]);
		Arrays.sort(keys);
		for (Long key : keys) {
			for (Properties prop : propMap.get(key)) {
				for (Entry<Object, Object> entry : prop.entrySet()) {
					targetMap.put(entry.getKey().toString(), entry.getValue().toString());
				}
			}
		}
	}
}
