/*******************************************************************************
 * Copyright (c) 2015-4-8 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

/**
 * loading properties files and combine the properties by version.
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-4-8
 */
public class PropertiesHelper {

	/**
	 * loading multi properties files to map by version.
	 * @param resPath properties file path
	 * @param versionName properties file version key name
	 * @return properties to map
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2015-4-9
	 */
	public static Map<String, String> loadProperties(final String resPath, final String versionName) {
		Map<String, String> map = new HashMap<String, String>();
		Map<Long, Properties> propMap = new HashMap<Long, Properties>();
		List<String> list = ResourceHelper.loadResourcesInClassPath(resPath, ".properties", "*", "");
		Long counter = 1L;
		for (String url : list) {
			FileInputStream is = null;
			try {
				File file = new File(url);
				if (!file.exists() || !file.isFile()) {
					continue;
				}
				Properties prop = new Properties();
				is = new FileInputStream(file);
				prop.load(is);
				Long version = counter++;
				try {
					String versionStr = (String) prop.get(versionName);
					version = Long.valueOf(versionStr);
				} catch (Exception e) {
				}
				while (propMap.containsKey(version)) {
					version = version + 1;
				}
				propMap.put(version, prop);
				Logger.debug(FCS.get("[org.iff.infra.util.PropertiesHelper.loadProperties][{file}]: loaded.", url));
			} catch (Exception e) {
				Logger.warn(FCS.get(
						"[org.iff.infra.util.PropertiesHelper.loadProperties][{file}]: loading property file error! ",
						url));
			} finally {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		Long[] keys = propMap.keySet().toArray(new Long[propMap.keySet().size()]);
		Arrays.sort(keys);
		for (Long key : keys) {
			Properties prop = propMap.get(key);
			for (Entry<Object, Object> entry : prop.entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		return map;
	}
}
