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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
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

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResource;
import org.beetl.ext.web.WebRender;
import org.iff.infra.util.ContentType;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.TCActionHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.infra.util.groovy2.TCGroovyLoader.TCActionInvoker;

import freemarker.ext.beans.BeansWrapper;

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
		TCActionHelper actionHelper = TCActionHelper.create(request, response);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String target = request.getServletPath();
		{//setting groovy loader
			ThreadLocalHelper.set("groovyLoader", loader);
		}
		if (target.length() < 1 || "/".equals(target)) {
			actionHelper.redirect(welcomeFile);
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
				response.getOutputStream()
						.write(loader.getResourceByte(target.startsWith("/") ? target : ("/" + target)));
				SocketHelper.closeWithoutError(response.getOutputStream());
			}
			return;
		}
		String appContext = StringHelper.pathConcat(request.getContextPath(), targetPrefix);
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
				target, "urlParams", urlParams, "filterConfig", this.filterConfig, "servletCotext", this.servletContext,
				"targetPrefix", targetPrefix, "tcmodule", loader);
		if (target.startsWith("/WEB-INF/") || target.startsWith("WEB-INF/")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render("html", "403.html", params)) {
				new TCFreemarkerRender("403.html", params).render();
			}
			return;
		} else if (subfix.equals("html") || subfix.equals("htm")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("btl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCBeetlRender(view, params).render();
			}
			return;
		} else if (subfix.equals("ftl")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCFreemarkerRender(view, params).render();
			}
			return;
		} else if (subfix.equals("gsp")) {
			response.setContentType("text/html; charset=UTF-8");
			if (!render(subfix, view, params)) {
				new TCGroovyRender(view, params).render();
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
					response.getOutputStream()
							.write(loader.getResourceByte(target.startsWith("/") ? target : ("/" + target)));
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
						if (value != null && value instanceof TCRender) {
							((TCRender) value).render();
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

	//===========renders
	public static class TCRender {
		protected String view;
		protected Map params;

		public TCRender(String view, Map params) {
			this.view = view;
			this.params = params;
		}

		public void render() {
		}

		public String getView() {
			return view;
		}

		public void setView(String view) {
			this.view = view;
		}

		public Map getParams() {
			return params;
		}

		public void setParams(Map params) {
			this.params = params;
		}
	}

	public static class TCJspRender extends TCRender {

		public TCJspRender(String view, Map params) {
			super(view, params);
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) getParams().get("request");
			HttpServletResponse response = (HttpServletResponse) getParams().get("response");
			try {
				request.getRequestDispatcher(getView()).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class TCGspRender extends TCJspRender {
		public TCGspRender(String view, Map params) {
			super(view, params);
		}
	}

	public static class TCBeetlRender extends TCRender {
		public static final String contentType = "text/html; charset=UTF-8";
		private static WebRender webRender = null;
		private static org.beetl.core.Configuration config = null;

		public TCBeetlRender(String view, Map params) {
			super(view, params);
			init();
		}

		public void init() {
			if (config == null) {
				try {
					config = org.beetl.core.Configuration.defaultConfiguration();
					config.setStatementStart("<!--#");
					config.setStatementEnd("#-->");
				} catch (Exception e) {
					Logger.error("Can not init beetl configuration", e);
				}
				try {
					ResourceLoader resourceLoader = new TCBtlResouceLoader();
					GroupTemplate gt = new GroupTemplate(resourceLoader, config);
					webRender = new WebRender(gt) {
						protected void modifyTemplate(Template template, String key, HttpServletRequest request,
								HttpServletResponse response, Object... args) {
							if (args != null && args[0] instanceof Map) {
								template.binding("params", args[0]);
								template.binding("request", ((Map) args[0]).get("request"));
								template.binding("response", ((Map) args[0]).get("response"));
								Enumeration<String> attrs = ((HttpServletRequest) ((Map) args[0]).get("request"))
										.getAttributeNames();
								while (attrs.hasMoreElements()) {
									String attrName = attrs.nextElement();
									template.binding(attrName, request.getAttribute(attrName));
								}
							}
						}
					};
				} catch (Exception e) {
					Logger.error("Can not init beetl WebRender", e);
				}
			}
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			webRender.render(view.startsWith("/") ? view : ("/" + view), request, response, params);
		}
	}

	public static class TCBtlResouceLoader implements ResourceLoader {

		public TCBtlResouceLoader() {
		}

		public boolean isModified(Resource key) {
			return true;
		}

		public void init(GroupTemplate gt) {
		}

		public String getResourceId(Resource resource, String key) {
			return key;
		}

		public Resource getResource(String key) {
			String resourceText = ((TCGroovyLoader) ThreadLocalHelper.get("groovyLoader")).getResourceText(key);
			if (resourceText == null) {
				return null;
			}
			return new StringTemplateResource(resourceText, this);
		}

		public boolean exist(String key) {
			return ((TCGroovyLoader) ThreadLocalHelper.get("groovyLoader")).getResource(key) != null;
		}

		public void close() {
		}
	}

	public static class TCFreemarkerRender extends TCRender {
		public static final String encoding = "UTF-8";
		public static final String contentType = "text/html; charset=UTF-8";
		private static freemarker.template.Configuration config = null;
		private static final BeansWrapper beansWrapper = new freemarker.ext.beans.BeansWrapperBuilder(
				freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
		private static final freemarker.template.TemplateModel helper = beansWrapper.getStaticModels();

		public TCFreemarkerRender(String view, Map params) {
			super(view, params);
		}

		public freemarker.template.Configuration getConfig(ServletContext servletContext) {
			if (config == null) {
				config = new freemarker.template.Configuration(
						freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
				config.setServletContextForTemplateLoading(servletContext, "/");
				config.setTemplateUpdateDelayMilliseconds(0);
				config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);

				config.setObjectWrapper(beansWrapper);
				config.setDefaultEncoding(encoding);
				config.setOutputEncoding(encoding);
				config.setLocale(java.util.Locale.CHINA);
				config.setLocalizedLookup(false);
				config.setNumberFormat("#0.#####");
				config.setDateFormat("yyyy-MM-dd");
				config.setTimeFormat("HH:mm:ss");
				config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
				config.setTemplateLoader(new TCFreeMarkerTemplateLoader());
			}
			return config;
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			Enumeration<String> attrs = request.getAttributeNames();
			Map root = new HashMap();
			{
				root.put("params", params);
				root.put("request", request);
				root.put("response", response);
				root.put("helper", helper);
			}
			while (attrs.hasMoreElements()) {
				String attrName = attrs.nextElement();
				root.put(attrName, request.getAttribute(attrName));
			}
			PrintWriter writer = null;
			try {
				freemarker.template.Configuration config = getConfig((ServletContext) params.get("servletCotext"));
				freemarker.template.Template template = config.getTemplate(view);
				writer = response.getWriter();
				template.process(root, writer); // Merge the data-model and the template
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					e.printStackTrace(writer);
				} catch (Exception ee) {
				}
			} finally {
				SocketHelper.closeWithoutError(writer);
			}
		}
	}

	public static class TCFreeMarkerTemplateLoader implements freemarker.cache.TemplateLoader {

		public TCFreeMarkerTemplateLoader() {
		}

		public Object findTemplateSource(String name) throws IOException {
			return ((TCGroovyLoader) ThreadLocalHelper.get("groovyLoader"))
					.getResourceText(name.startsWith("/") ? name : ("/" + name));
		}

		public long getLastModified(Object templateSource) {
			return 0;
		}

		public Reader getReader(Object templateSource, String encoding) throws IOException {
			return new StringReader((String) templateSource);
		}

		public void closeTemplateSource(Object templateSource) throws IOException {
			//do nothing
		}
	}

	public static class TCGroovyRender extends TCRender {
		private static final groovy.text.TemplateEngine engine = new groovy.text.SimpleTemplateEngine();

		public TCGroovyRender(String view, Map params) {
			super(view, params);
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			groovy.text.Template template = null;
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				template = engine.createTemplate(((TCGroovyLoader) ThreadLocalHelper.get("groovyLoader"))
						.getResourceText(view.startsWith("/") ? view : ("/" + view)));
				groovy.lang.Binding binding = new groovy.lang.Binding();
				{
					binding.setVariable("params", params);
					binding.setVariable("request", request);
					binding.setVariable("response", response);
					Enumeration<String> attrs = request.getAttributeNames();
					while (attrs.hasMoreElements()) {
						String attrName = attrs.nextElement();
						binding.setVariable(attrName, request.getAttribute(attrName));
					}
				}
				template.make(binding.getVariables()).writeTo(writer);
				response.flushBuffer();
			} catch (Exception e) {
				try {
					writer = response.getWriter();
					e.printStackTrace(writer);
				} catch (Exception ee) {
				}
			} finally {
				SocketHelper.closeWithoutError(writer);
			}
		}

	}
}
