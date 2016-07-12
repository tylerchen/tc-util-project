/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy2;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.iff.infra.util.ActionHelper;
import org.iff.infra.util.ContentType;
import org.iff.infra.util.FCS;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.infra.util.groovy2.TCGroovyLoader.TCActionInvoker;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class TCGroovyLoaderFilter implements Filter {

	private String targetPrefix;
	private String welcomeFile;
	private String[] basePaths;
	private TCChain handler;
	private FilterConfig filterConfig;
	private ServletContext servletContext;
	private TCGroovyLoader loader;

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
			welcomeFile = config.getInitParameter("tc_webcome_file");
			if (welcomeFile == null) {
				welcomeFile = "index.html";
			}
		}
		{
			String tmpPath = config.getInitParameter("tc_base_paths");
			if (tmpPath == null) {
				tmpPath = System.getProperty("tc_base_paths");
			}
			if (tmpPath != null) {
				basePaths = tmpPath.split(",");
			}
		}
		{
			TCChain before = handler = new TCBeforeActionHandler();
			TCChain process = before.chain = new TCProcessActionHandler();
			TCChain after = process.chain = new TCAfterActionHandler();
		}
		{
			if (basePaths.length > 0 && basePaths[0].length() > 1) {
				loader = TCGroovyLoader.create(getClass().getSimpleName(), basePaths);
				loader.load();
			}
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		ActionHelper actionHelper = ActionHelper.create(request, response);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String target = request.getServletPath();
		String appContext = StringHelper.pathConcat(request.getContextPath(), targetPrefix);
		if (appContext.endsWith("/")) {
			appContext = appContext.substring(0, appContext.length() - 1);
		}
		{//setting groovy loader
			ThreadLocalHelper.set("groovyLoader", loader);
		}
		if (targetPrefix.length() > 0) {
			if (target.length() >= targetPrefix.length()) {
				target = target.substring(targetPrefix.length());
			}
			if (!target.startsWith("/")) {
				target = "/" + target;
			}
		}
		if (target.length() < 1 || "/".equals(target)) {
			actionHelper.redirect(StringHelper.concat(appContext, "/", welcomeFile));
			return;
		}
		if (target.startsWith("/css/") || target.startsWith("/js/") || target.startsWith("/images/")
				|| target.startsWith("/fonts/")) {
			String contentType = ContentType.getContentType(target);
			if (target.endsWith(".css") || target.endsWith(".js")) {
				response.setContentType(contentType);
				String resourceText = loader.getResourceText(target.startsWith("/") ? target : ("/" + target));
				if (resourceText != null) {
					response.getWriter().write(resourceText);
				}
				SocketHelper.closeWithoutError(response.getWriter());
			} else {
				response.setContentType(contentType);
				byte[] resourceByte = loader.getResourceByte(target.startsWith("/") ? target : ("/" + target));
				if (resourceByte != null && resourceByte.length > 0) {
					response.getOutputStream().write(resourceByte);
				}
				SocketHelper.closeWithoutError(response.getOutputStream());
			}
			return;
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
				target, "urlParams", urlParams, "filterConfig", this.filterConfig, "servletCotext", this.servletContext,
				"targetPrefix", targetPrefix, "loader", loader);
		if (target.startsWith("/WEB-INF/") || target.startsWith("WEB-INF/")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render("html", "403.html", params)) {
				new TCRenders.TCFreemarkerRender("403.html", params).render();
			}
			return;
		} else if (subfix.equals("html") || subfix.equals("htm")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenders.TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("btl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenders.TCBeetlRender(view, params).render();
			}
			return;
		} else if (subfix.equals("ftl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenders.TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("gsp")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCRenders.TCGroovyRender(view, params).render();
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
					String resourceText = loader.getResourceText(target.startsWith("/") ? target : ("/" + target));
					if (resourceText != null) {
						response.getWriter().write(resourceText);
					}
					SocketHelper.closeWithoutError(response.getWriter());
				} else {
					byte[] resourceByte = loader.getResourceByte(target.startsWith("/") ? target : ("/" + target));
					if (resourceByte != null && resourceByte.length > 0) {
						response.getOutputStream().write(resourceByte);
					}
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
			if (object != null && TCRenders.TCRender.class.isAssignableFrom(object.getClass())) {
				Constructor<?> constructor = ReflectHelper.getConstructor(object.getClass(), "java.lang.String",
						"java.util.Map");
				if (constructor != null) {
					TCRenders.TCRender render = (TCRenders.TCRender) constructor.newInstance(target, params);
					render.render();
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	//===========handlers
	public static class TCChain {
		protected TCChain chain;

		public Object process(Map params) {
			return null;
		}
	}

	public static class TCBeforeActionHandler extends TCChain {
		public Object process(Map params) {
			return chain == null ? true : chain.process(params);
		}
	}

	public static class TCProcessActionHandler extends TCChain {
		public Object process(Map params) {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			{
				String target = (String) params.get("target");
				TCActionInvoker actionInvoker = ((TCGroovyLoader) ThreadLocalHelper.get("groovyLoader"))
						.getAction(target, params);
				if (actionInvoker != null) {
					PrintWriter writer = null;
					try {
						writer = response.getWriter();
						Object value = actionInvoker.invoke(new Object[0]);
						if (value != null && value instanceof TCRenders.TCRender) {
							((TCRenders.TCRender) value).render();
						}
						if (response.getContentType() == null || response.getContentType().length() < 1) {
							response.setContentType("text/html; charset=UTF-8");
						}
					} catch (Exception e) {
						e.printStackTrace(writer);
					} finally {
						SocketHelper.closeWithoutError(writer);
					}
				} else {
					PrintWriter writer = null;
					try {
						writer = response.getWriter();
						writer.write(
								FCS.get("<html><body><h1>Target not found</h1><div>{0}</div></body></html>", target)
										.toString());
					} catch (Exception e) {
						e.printStackTrace(writer);
					} finally {
						SocketHelper.closeWithoutError(writer);
					}
				}
			}
			return chain == null ? true : chain.process(params);
		}
	}

	public static class TCAfterActionHandler extends TCChain {
		public Object process(Map params) {
			return chain == null ? true : chain.process(params);
		}

	}
}
