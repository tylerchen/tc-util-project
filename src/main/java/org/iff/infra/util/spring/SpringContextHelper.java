/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <pre>
 * add this xml definition to spring xml:
 * <bean id="springContextUtil" class="org.iff.infra.util.spring.SpringContextHelper" />
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext; // Spring应用上下文环境

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * 
	 * @param applicationContext
	 * @throws org.springframework.beans.BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHelper.applicationContext = applicationContext;
	}

	/**
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static String hello(String test) {
		return "HHHHeloll:" + test;
	}

	/**
	 * 获取对象
	 * 
	 * @param name
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws org.springframework.beans.BeansException
	 */
	public static <T> T getBean(String name) throws BeansException {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 获取类型为requiredType的对象
	 * 
	 * @param clz
	 * @return
	 * @throws org.springframework.beans.BeansException
	 */
	public static <T> T getBean(Class<T> clz) throws BeansException {
		T result = (T) applicationContext.getBean(clz);
		return result;
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * 
	 * @param name
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

}
