/*******************************************************************************
 * Copyright (c) Aug 12, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResource;
import org.beetl.ext.web.WebRender;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 12, 2015
 */
public class TCRenderManager {

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
		private static Map<String, WebRender> renders = new HashMap<String, WebRender>();
		private static org.beetl.core.Configuration config = null;

		public TCBeetlRender(String view, Map params) {
			super(view, params);
			init();
		}

		public void init() {
			if (config == null) {
				try {
					config = org.beetl.core.Configuration.defaultConfiguration();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			String moduleName = (String) params.get("moduleName");
			String target = (String) params.get("target");
			WebRender webRender = renders.get(moduleName);
			if (webRender == null) {
				ResourceLoader resourceLoader = new TCBtlResouceLoader(moduleName);
				GroupTemplate gt = new GroupTemplate(resourceLoader, config);
				webRender = new WebRender(gt) {
					protected void modifyTemplate(Template template, String key, HttpServletRequest request,
							HttpServletResponse response, Object... args) {
						if (args != null && args[0] instanceof Map) {
							template.binding("params", args[0]);
						}
					}
				};
				renders.put(moduleName, webRender);
			}
			if (response.getContentType() == null || response.getContentType().length() < 1) {
				response.setContentType(contentType);
			}
			webRender.render(target.startsWith("/") ? target.substring(1) : target, request, response, params);
		}
	}

	public static class TCBtlResouceLoader implements ResourceLoader {
		private String moduleName;

		public TCBtlResouceLoader(String moduleName) {
			this.moduleName = moduleName;
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
			String resourceText = TCModuleManager.me().get(moduleName).getResourceText(key);
			if (resourceText == null) {
				return null;
			}
			return new StringTemplateResource(resourceText, this);
		}

		public boolean exist(String key) {
			return TCModuleManager.me().get(moduleName).getResource(key) != null;
		}

		public void close() {
		}
	}

	public static class TCFreemarkerRender extends TCRender {
		public static final String encoding = "UTF-8";
		public static final String contentType = "text/html; charset=UTF-8";
		private static freemarker.template.Configuration config = null;

		public TCFreemarkerRender(String view, Map params) {
			super(view, params);
			init();
		}

		public void init() {
			if (config != null) {
				return;
			}
			ServletContext servletContext = (ServletContext) params.get("servletContext");
			config = new freemarker.template.Configuration();
			config.setServletContextForTemplateLoading(servletContext, "/");
			config.setTemplateUpdateDelay(0);
			config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
			config.setObjectWrapper(freemarker.template.ObjectWrapper.BEANS_WRAPPER);
			config.setDefaultEncoding(encoding);
			config.setOutputEncoding(encoding);
			config.setLocale(java.util.Locale.CHINA);
			config.setLocalizedLookup(false);
			config.setNumberFormat("#0.#####");
			config.setDateFormat("yyyy-MM-dd");
			config.setTimeFormat("HH:mm:ss");
			config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		}

		public void render() {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			response.setContentType(contentType);
			Enumeration<String> attrs = request.getAttributeNames();
			Map root = new HashMap();
			while (attrs.hasMoreElements()) {
				String attrName = attrs.nextElement();
				root.put(attrName, request.getAttribute(attrName));
			}
			root.put("request", request);
			PrintWriter writer = null;
			try {
				freemarker.template.Template template = config.getTemplate(view);
				writer = response.getWriter();
				template.process(root, writer); // Merge the data-model and the template
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
	}
}
