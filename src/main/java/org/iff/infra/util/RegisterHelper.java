package org.iff.infra.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegisterHelper {
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private static Map<String, Map<String, Object>> registerBus = new LinkedHashMap<String, Map<String, Object>>();

	public static boolean setRegisterBus(LinkedHashMap<String, Map<String, Object>> bus) {
		try {
			lock.writeLock().lock();
			if (registerBus.isEmpty()) {
				registerBus = bus;
				return true;
			}
			return false;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static boolean regist(String type, Map<String, Object> value) {
		Assert.notBlank(type, "[RegisterHelper.regist]:type is required!");
		Assert.notEmpty(value, "[RegisterHelper.regist]:value is required!");
		Assert.notBlank((String) value.get("name"), "[RegisterHelper.regist]:value name is required!");
		try {
			lock.writeLock().lock();
			Map<String, Object> registors = registerBus.get(type);
			if (registors == null) {
				registors = new LinkedHashMap<String, Object>();
				registerBus.put(type, value);
			}
			registors.put((String) value.get("name"), value.get("value"));
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static boolean unRegist(String type) {
		try {
			lock.writeLock().lock();
			return registerBus.remove(type) != null;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static boolean unRegist(String type, String name) {
		try {
			lock.writeLock().lock();
			Map<String, Object> registors = registerBus.remove(type);
			if (registors != null) {
				return registors.remove(name) != null;
			}
			return false;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static Map<String, Object> get(String type) {
		Assert.notBlank(type, "[RegisterHelper.get]:type is required!");
		try {
			lock.readLock().lock();
			Map<String, Object> registors = registerBus.get(type);
			if (registors == null) {
				registors = new LinkedHashMap<String, Object>();
			}
			return Collections.unmodifiableMap(registors);
		} finally {
			lock.readLock().unlock();
		}
	}

	public static Object get(String type, String name) {
		Assert.notBlank(name, "[RegisterHelper.get]:name is required!");
		try {
			lock.readLock().lock();
			return get(type).get(name);
		} finally {
			lock.readLock().unlock();
		}
	}
}
