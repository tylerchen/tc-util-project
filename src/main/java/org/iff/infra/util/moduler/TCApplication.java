/*******************************************************************************
 * Copyright (c) Aug 11, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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
}
