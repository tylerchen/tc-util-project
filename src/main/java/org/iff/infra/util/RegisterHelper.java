package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterHelper {
	private static Map<String, Map<String, Object>> registerBus = new LinkedHashMap<String, Map<String, Object>>();

	public static boolean setRegisterBus(LinkedHashMap<String, Map<String, Object>> bus) {
		if (registerBus.isEmpty()) {
			registerBus = bus;
			return true;
		}
		return false;
	}

	public static boolean regist(String type, Map<String, Object> value) {
		Assert.notBlank(type, "[RegisterHelper.regist]:type is required!");
		Assert.notEmpty(value, "[RegisterHelper.regist]:value is required!");
		Assert.notBlank((String) value.get("name"), "[RegisterHelper.regist]:value name is required!");
		Map<String, Object> registors = registerBus.get(type);
		if (registors == null) {
			registors = new LinkedHashMap<String, Object>();
			registerBus.put(type, value);
		}
		registors.put((String) value.get("name"), value.get("value"));
		return true;
	}

	public static Map<String, Object> get(String type) {
		Assert.notBlank(type, "[RegisterHelper.get]:type is required!");
		Map<String, Object> registors = registerBus.get(type);
		if (registors == null) {
			registors = new LinkedHashMap<String, Object>();
		}
		return registors;
	}

	public static Object get(String type, String name) {
		Assert.notBlank(name, "[RegisterHelper.get]:name is required!");
		return get(type).get(name);
	}
}
