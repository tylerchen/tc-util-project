/*******************************************************************************
 * Copyright (c) Oct 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain;

import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.util.RegisterHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Oct 10, 2015
 */
public class InstanceProviderDelegate implements InstanceProvider {

	public <T> T getInstance(Class<T> beanClass) {
		Map<String, Object> map = RegisterHelper.get(InstanceProvider.class.getName());
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().indexOf("Groovy") < 0) {
				return (T) ((InstanceProvider) ((Map) entry.getValue()).get("value")).getInstance(beanClass);
			}
		}
		return null;
	}

	public <T> T getInstance(Class<T> beanClass, String beanName) {
		Map<String, Object> map = RegisterHelper.get(InstanceProvider.class.getName());
		for (Entry<String, Object> entry : map.entrySet()) {
			return (T) ((InstanceProvider) ((Map) entry.getValue()).get("value")).getInstance(beanClass, beanName);
		}
		return null;
	}

	public <T> T getInstance(String beanName) {
		Map<String, Object> map = RegisterHelper.get(InstanceProvider.class.getName());
		for (Entry<String, Object> entry : map.entrySet()) {
			return (T) ((InstanceProvider) entry.getValue()).getInstance(beanName);
		}
		return null;
	}

}
