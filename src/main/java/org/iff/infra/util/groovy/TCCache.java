/*******************************************************************************
 * Copyright (c) 2015-2-15 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.domain.InstanceProvider;
import org.iff.infra.util.MapHelper;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-2-15
 */
@SuppressWarnings( { "unchecked", "serial" })
public class TCCache extends LinkedHashMap implements InstanceProvider {
	private static final TCCache me = new TCCache();

	private TCCache() {
		super(512);
		initCache();
	}

	public static TCCache me() {
		return me;
	}

	public TCCache initCache() {
		{
			clear();
		}
		setByPath("framework_instance", new LinkedHashMap());
		setByPath("action_instance", new LinkedHashMap());
		setByPath("anno_class/action", new LinkedHashMap());
		setByPath("anno_class/framework", new LinkedHashMap());
		setByPath("groovy_files", new LinkedHashMap());
		setByPath("groovy_files", new LinkedHashMap());
		setByPath("props", new LinkedHashMap());
		setByPath("config", new LinkedHashMap());
		setByPath("framework/inits", new ArrayList());
		setByPath("framework/starts", new ArrayList());
		setByPath("framework/stops", new ArrayList());
		setByPath("framework/action_handlers", new ArrayList());
		setByPath("framework/actions", new LinkedHashMap());
		setByPath("servlet", new LinkedHashMap());
		return this;
	}

	public Object getByPath(String path) {
		return path == null ? null : MapHelper.getByPath(this, path);
	}

	public TCCache setByPath(String path, Object value) {
		if (path != null) {
			MapHelper.setByPath(this, path, value);
		}
		return me;
	}

	public Object getInstance(String name) {
		return name == null ? null : ((Map) get("framework_instance")).get(name);
	}

	public Object getInstance(Class beanClass) {
		if (beanClass == null) {
			return null;
		}
		Map ins = (Map) get("framework_instance");
		for (Object values : ins.values()) {
			if (values != null && beanClass.isAssignableFrom(values.getClass())) {
				return values;
			}
		}
		return null;
	}

	public Object getInstance(Class beanClass, String beanName) {
		if (beanClass == null || beanName == null) {
			return null;
		}
		Map ins = (Map) get("framework_instance");
		for (Object o : ins.entrySet()) {
			Entry entry = (Entry) o;
			Object key = entry.getKey(), value = entry.getValue();
			if (key != null && value != null && beanName.equals(entry.getKey())
					&& beanClass.isAssignableFrom(value.getClass())) {
				return value;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(me());
	}
}
