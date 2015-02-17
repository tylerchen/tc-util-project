package org.iff.infra.com.dayatang.spring.webapp;

import javax.servlet.ServletContextEvent;

import org.iff.infra.com.dayatang.domain.InstanceFactory;
import org.iff.infra.com.dayatang.spring.factory.SpringInstanceProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * DO NOT use this class again!!!!! Please using spring mvc listener instead!!!
 * <pre>
 *     <context-param>
 *         <param-name>contextConfigLocation</param-name>
 *         <param-value>WEB-INF/beans.xml</param-value>
 *     </context-param>
 *     <listener>
 *         <listener-class>
 *             org.springframework.web.context.ContextLoaderListener
 *         </listener-class>
 *     </list
 * @author Tyler
 */
@Deprecated
public class DayatangContextLoaderListener extends ContextLoaderListener {

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event
				.getServletContext());
		SpringInstanceProvider springProvider = new SpringInstanceProvider(applicationContext);
		InstanceFactory.setInstanceProvider(springProvider);
	}

}
