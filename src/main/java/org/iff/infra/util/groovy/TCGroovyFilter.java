/*******************************************************************************
 * Copyright (c) 2015-2-2 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import groovy.lang.GroovyObject;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.iff.infra.util.Logger;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-2
 */
public class TCGroovyFilter implements javax.servlet.Filter {
	private javax.servlet.Filter delegate = null;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		delegate.doFilter(request, response, chain);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		{
			System.setProperty("app_mode", "embedded");
			Enumeration<String> names = filterConfig.getInitParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				if ("system_props".equals(name)) {//system_props="a=b@@@b=c" -> a:b, b:c
					String value = filterConfig.getInitParameter(name);
					value = value == null ? "" : value.trim();
					String[] split = value.split("@@@");
					for (String s : split) {
						String[] propsPaire = s.split("=");
						if (propsPaire.length == 2) {
							System.setProperty(propsPaire[0], propsPaire[1]);
						}
					}
				} else {
					System.setProperty(name, filterConfig.getInitParameter(name));
				}
			}
		}
		{
			try {
				TCCLassManager.main(null);
				{
					Class clazz = TCCLassManager.me().get().loadClass("org.iff.groovy.framework.TCFilter");
					GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
					delegate = (Filter) groovyObject;
					delegate.init(filterConfig);
				}
			} catch (Exception e) {
				Logger.debug("Start TC Framework Error!!!", e);
			}
		}
	}

}
