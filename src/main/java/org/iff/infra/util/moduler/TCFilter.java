/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.TCActionHelper;
import org.iff.infra.util.moduler.TCActionHandler.TCChain;
import org.iff.infra.util.moduler.TCRenderManager.TCRender;

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
		TCActionHelper actionHelper = TCActionHelper.create(request, response);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String target = request.getServletPath();
		if (target.length() < 1 || "/".equals(target)) {
			actionHelper.redirect(TCApplication.me().getProp("tc_webcome_file"));
			return;
		}
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
		if (moduleName.length() < 1 || (target.length() < 1 && moduleName.indexOf(".") > 0)) {
			actionHelper.redirect(TCApplication.me().getProp("tc_webcome_file"));
			return;
		}
		TCModule tcModule = TCModuleManager.me().get(moduleName);
		if (tcModule == null) {
			return;
		}
		if (target.startsWith("/css/") || target.startsWith("/js/") || target.startsWith("/images/")
				|| target.startsWith("/fonts/")) {
			String contentType = ContentType.getContentType(target);
			if (target.endsWith(".css") || target.endsWith(".js")) {
				response.setContentType(contentType);
				String resourceText = tcModule.getResourceText(target.startsWith("/") ? target : ("/" + target));
				if (resourceText != null) {
					response.getWriter().write(resourceText);
				}
				SocketHelper.closeWithoutError(response.getWriter());
			} else {
				response.setContentType(contentType);
				response.getOutputStream().write(
						tcModule.getResourceByte(target.startsWith("/") ? target : ("/" + target)));
				SocketHelper.closeWithoutError(response.getOutputStream());
			}
			return;
		}
		String appContext = StringHelper.pathConcat(request.getContextPath(), targetPrefix, moduleName);
		if (appContext.endsWith("/")) {
			appContext = appContext.substring(0, appContext.length() - 1);
		}
		// url=/app-context/target-prefix/module-context/action/method.subfix/urlparameters
		int dotIndex = target.indexOf('.');
		String subfix = "";
		String view = target;
		List<String> urlParams = new ArrayList<String>();
		if (dotIndex > -1) {
			int splashIndex = target.indexOf('/', dotIndex);
			if (splashIndex > -1) {
				subfix = target.substring(dotIndex + 1, splashIndex);
				view = target.substring(0, splashIndex);
				urlParams.addAll(Arrays.asList(target.substring(splashIndex + 1).split("\\/")));
			} else {
				subfix = target.substring(dotIndex + 1);
			}
		}
		Map params = MapHelper.toMap("request", request, "response", response, "context", request.getContextPath(),
				"appContext", appContext, "actionContext", target, "servletPath", request.getServletPath(), "target",
				target, "urlParams", urlParams, "filterConfig", this.filterConfig, "servletCotext",
				this.servletContext, "targetPrefix", targetPrefix, "moduleName", moduleName, "tcmodule", tcModule);
		if (target.startsWith("/WEB-INF/") || target.startsWith("WEB-INF/")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render("html", "403.html", params)) {
				new TCRenderManager.TCFreemarkerRender("403.html", params).render();
			}
			return;
		} else if (subfix.equals("html") || subfix.equals("htm")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenderManager.TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("btl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenderManager.TCBeetlRender(view, params).render();
			}
			return;
		} else if (subfix.equals("ftl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenderManager.TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("gsp")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenderManager.TCGroovyRender(view, params).render();
			}
			return;
		} else if (subfix.equals("jsp")) {
			response.setContentType("text/html; charset=UTF-8");
			chain.doFilter(request, response);
			return;
		} else if (subfix.length() > 0) {
			String contentType = ContentType.getContentType(subfix);
			response.setContentType(contentType);
			if (!render(subfix, view, params)) {
				if (contentType.indexOf("text") > -1 || contentType.indexOf("html") > -1) {
					String resourceText = tcModule.getResourceText(target.startsWith("/") ? target : ("/" + target));
					if (resourceText != null) {
						response.getWriter().write(resourceText);
					}
					SocketHelper.closeWithoutError(response.getWriter());
				} else {
					response.getOutputStream().write(
							tcModule.getResourceByte(target.startsWith("/") ? target : ("/" + target)));
					SocketHelper.closeWithoutError(response.getOutputStream());
				}
			}
			return;
		}
		if (target.endsWith("/") || target.indexOf(".") < 0) {
			response.setContentType("text/html; charset=UTF-8");
			if (target.endsWith("/")) {
				target = target.substring(0, target.length() - 1);
			}
			if (subfix.length() > 0) {
				target = target.substring(0, target.length() - (subfix.length() + 1));
			}
			handler.process(params);
			return;
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

	public boolean render(String subfix, String target, Map params) {
		try {
			Object object = RegisterHelper.get("TCRender", subfix);
			if (object != null && TCRender.class.isAssignableFrom(object.getClass())) {
				Constructor<?> constructor = ReflectHelper.getConstructor(object.getClass(), "java.lang.String",
						"java.util.Map");
				if (constructor != null) {
					TCRender render = (TCRender) constructor.newInstance(target, params);
					render.render();
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
}
