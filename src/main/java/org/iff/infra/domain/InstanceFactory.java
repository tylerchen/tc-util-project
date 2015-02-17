/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain;

import org.iff.infra.util.spring.SpringContextHelper;
import org.iff.infra.util.spring.factory.SpringInstanceProvider;

/**
 * instance factory, to get bean by instance provider.
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public class InstanceFactory {

	/** instance provider **/
	private static InstanceProvider instanceProvider;

	/**
	 * set instance provider
	 * @param provider
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	public static void setInstanceProvider(InstanceProvider provider) {
		instanceProvider = provider;
	}

	/**
	 * get instance provider, if null and spring context exists then will create the spring instance provider.
	 * @see
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	public static InstanceProvider getInstanceProvider() {
		if (instanceProvider == null && SpringContextHelper.getApplicationContext() != null) {
			instanceProvider = new SpringInstanceProvider(SpringContextHelper.getApplicationContext());
		}
		return instanceProvider;
	}

	/**
	 * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该类型的实例则抛出异常。
	 * 
	 * @param <T> 对象的类型
	 * @param beanClass 对象的类
	 * @return 类型为T的对象实例
	 */
	public static <T> T getInstance(Class<T> beanClass) {
		return (T) getInstanceProvider().getInstance(beanClass);
	}

	/**
	 * 获取指定类型的对象实例。如果IoC容器没配置好或者IoC容器中找不到该实例则抛出异常。
	 * 
	 * @param <T> 对象的类型
	 * @param beanName 实现类在容器中配置的名字
	 * @param beanClass 对象的类
	 * @return 类型为T的对象实例
	 */
	public static <T> T getInstance(Class<T> beanClass, String beanName) {
		return (T) getInstanceProvider().getInstance(beanClass, beanName);
	}

	/**
	 * 获取指定类型的对象实例
	 * @param <T> 对象的类型
	 * @param beanName 实现类在容器中配置的名字
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String beanName) {
		return (T) getInstanceProvider().getInstance(beanName);
	}
}
