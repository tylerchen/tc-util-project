/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring.factory;

import org.iff.infra.domain.InstanceProvider;
import org.iff.infra.util.spring.SpringContextHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * the spring instance provider
 * <pre>
 *     <context-param>
 *         <param-name>contextConfigLocation</param-name>
 *         <param-value>WEB-INF/beans.xml</param-value>
 *     </context-param>
 *     <listener>
 *         <listener-class>
 *             org.springframework.web.context.ContextLoaderListener
 *         </listener-class>
 *     </listener>
 * </pre>
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public class SpringInstanceProvider implements InstanceProvider {

	private ApplicationContext applicationContext;

	/**
	 * use spring definition xml to create a spring context.
	 * @param locations
	 */
	public SpringInstanceProvider(String... locations) {
		applicationContext = new ClassPathXmlApplicationContext(locations);
	}

	/**
	 * using the exists spring context
	 * @param applicationContext
	 */
	public SpringInstanceProvider(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * using the exists annotation classes to create spring context
	 * @param annotatedClasses
	 */
	public SpringInstanceProvider(Class<?>... annotatedClasses) {
		applicationContext = new AnnotationConfigApplicationContext(annotatedClasses);
	}

	/**
	 * get spring bean by class type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> beanClass) {
		String[] beanNames = getApplicationContext().getBeanNamesForType(beanClass);
		if (beanNames.length == 0) {
			return null;
		}
		return (T) getApplicationContext().getBean(beanNames[0]);
	}

	/**
	 * get spring bean by class type and bean name
	 */
	public <T> T getInstance(Class<T> beanClass, String beanName) {
		return (T) getApplicationContext().getBean(beanName, beanClass);
	}

	/**
	 * get spring bean by bean name
	 */
	public <T> T getInstance(String beanName) {
		return (T) getApplicationContext().getBean(beanName);
	}

	/**
	 * return spring application context, if not created, using the default context provided by SpringContextHelper.
	 * @see org.iff.infra.util.spring.SpringContextHelper
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2015-2-15
	 */
	public ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = SpringContextHelper.getApplicationContext();
		}
		return applicationContext;
	}

}
