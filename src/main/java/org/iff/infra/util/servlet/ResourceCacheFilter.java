/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cache javascript, css and images
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public class ResourceCacheFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String path = request.getServletPath();
			if (path.startsWith("/images/") || path.startsWith("/style/") || path.startsWith("/js/")) {
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				{
					Date date = new Date();
					response.setDateHeader("Last-Modified", date.getTime()); //Last-Modified:页面的最后生成时间 
					response.setDateHeader("Expires", date.getTime() * 10); //Expires:过时期限值 
					response.setHeader("Cache-Control", "public"); //Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息；
					response.setHeader("Cache-Control", "max-age=" + date.getTime() * 10);
					response.setHeader("Pragma", "Pragma"); //Pragma:设置页面是否缓存，为Pragma则缓存，no-cache则不缓存
				}
			}
		} finally {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	public void destroy() {
	}

}