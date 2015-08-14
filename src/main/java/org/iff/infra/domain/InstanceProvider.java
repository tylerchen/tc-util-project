/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain;

/**
 * the instance provider
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public interface InstanceProvider {

	/**
	 * get instance by bean class
	 * @param <T>
	 * @param beanClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	<T> T getInstance(Class<T> beanClass);

	/**
	 * get instance by bean class and name
	 * @param <T>
	 * @param beanClass
	 * @param beanName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	<T> T getInstance(Class<T> beanClass, String beanName);

	/**
	 * get instance by bean name
	 * @param <T>
	 * @param beanName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	<T> T getInstance(String beanName);
}
