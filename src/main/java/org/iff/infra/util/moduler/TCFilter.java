/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iff.infra.util.ContentType;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.moduler.TCActionHandler.TCChain;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class TCFilter implements Filter {

	private String targetPrefix;
	private TCChain handler;
	private FilterConfig filterConfig;
	private ServletContext servletContext;

	public void init(FilterConfig config) throws ServletException {
		{
			this.filterConfig = config;
			this.servletContext = config.getServletContext();
		}
		{
			targetPrefix = config.getInitParameter("target_prefix");
			if (targetPrefix == null) {
				targetPrefix = "";
			}
		}
		{
			TCChain before = handler = new TCActionHandler.TCBeforeActionHandler();
			TCChain process = before.chain = new TCActionHandler.TCProcessActionHandler();
			TCChain after = process.chain = new TCActionHandler.TCAfterActionHandler();
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String target = request.getServletPath();
		if (targetPrefix.length() > 0) {
			if (target.length() >= targetPrefix.length()) {
				target = target.substring(targetPrefix.length());
			}
			if (!target.startsWith("/")) {
				target = "/" + target;
			}
		}
		String moduleName = "";
		{
			int indexOf = target.indexOf('/', 1);
			if (indexOf > -1) {
				moduleName = target.substring(1, indexOf);
				target = target.substring(indexOf);
			}
		}
		if (target.startsWith("/css/") || target.startsWith("/js/") || target.startsWith("/images/")) {
			String contentType = ContentType.getContentType(target);
			if (target.endsWith(".css") || target.endsWith(".js")) {
				response.setContentType(contentType);
				response.getWriter().write(TCModuleManager.me().get(moduleName).getResourceText(target.substring(1)));
				SocketHelper.closeWithoutError(response.getWriter());
			}
			return;
		}
		String appContext = StringHelper.pathConcat(request.getContextPath(), targetPrefix, moduleName);
		if (appContext.endsWith("/")) {
			appContext = appContext.substring(0, appContext.length() - 1);
		}
		if (target.endsWith(".btl")) {
			new TCRenderManager.TCBeetlRender(target, MapHelper.toMap("request", request, "response", response,
					"context", request.getContextPath(), "appContext", appContext, "actionContext", target,
					"servletPath", request.getServletPath(), "target", target, "urlParams", new ArrayList<String>(),
					"filterConfig", this.filterConfig, "servletCotext", this.servletContext, "targetPrefix",
					targetPrefix, "moduleName", moduleName)).render();
			return;
		}
		if (target.endsWith(".gsp") || target.endsWith(".ftl") || target.endsWith(".jsp")) {
			chain.doFilter(request, response);
			return;
		}
		if (target.endsWith(".html") || target.endsWith("/") || target.indexOf(".") < 0) {
			if (target.endsWith("/")) {
				target = target.substring(0, target.length() - 1);
			}
			if (target.endsWith(".html")) {
				target = target.substring(0, target.length() - 5);
			}
			Map params = MapHelper.toMap("request", request, "response", response, "context", request.getContextPath(),
					"appContext", appContext, "actionContext", target, "servletPath", request.getServletPath(),
					"target", target, "urlParams", new ArrayList<String>(), "filterConfig", this.filterConfig,
					"servletCotext", this.servletContext, "targetPrefix", targetPrefix, "moduleName", moduleName);
			handler.process(params);
			return;
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}
