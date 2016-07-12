/*******************************************************************************
 * Copyright (c) Aug 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <pre>
 * RegisterHelper use to register values, type and mapping values.
 * such as:
 * regist('view', {'name':'Jsp', 'value':jspInstance});
 * regist('view', {'name':'Freemarker', 'value':FreemarkerInstance});
 * regist('view', {'name':'Gsp', 'value':gspInstance});
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 2, 2015
 */
public class RegisterHelper {
	/**
	 * this read write lock to protected read write.
	 */
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * this is the bus to register. data struct: Map<String, Map<String, Object>>.
	 * you can set your bus instance if there is no register.
	 */
	private static Map<String, Map<String, Object>> registerBus = new LinkedHashMap<String, Map<String, Object>>();

	/**
	 * you can set your bus instance if there is no register.
	 * use this method on program init.
	 * @param bus
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
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

	/**
	 * register a type, the value struct:{name, value}
	 * @param type
	 * @param value
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
	public static boolean regist(String type, Map<String, Object> value) {
		Assert.notBlank(type, "[RegisterHelper.regist]:type is required!");
		Assert.notEmpty(value, "[RegisterHelper.regist]:value is required!");
		Assert.notBlank((String) value.get("name"), "[RegisterHelper.regist]:value name is required!");
		try {
			lock.writeLock().lock();
			Map<String, Object> registors = registerBus.get(type);
			if (registors == null) {
				registors = new LinkedHashMap<String, Object>();
				registerBus.put(type, registors);
			}
			registors.put((String) value.get("name"), value.get("value"));
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * remove a register by the type.
	 * @param type
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
	public static boolean unRegist(String type) {
		try {
			lock.writeLock().lock();
			return registerBus.remove(type) != null;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * remove a register by the type and name.
	 * @param type
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
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

	/**
	 * get all register by the type.
	 * @param type
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
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

	/**
	 * get the register by the tyler and name.
	 * @param type
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
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
