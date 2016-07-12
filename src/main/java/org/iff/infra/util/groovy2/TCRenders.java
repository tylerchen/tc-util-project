/*******************************************************************************
 * Copyright (c) Jun 7, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResource;
import org.beetl.ext.web.WebRender;
import org.iff.infra.util.Logger;
import org.iff.infra.util.SocketHelper;

import freemarker.ext.beans.BeansWrapper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 7, 2016
 */
public class TCRenders {

	public static TCGroovyLoader getTCGroovyLoader() {
		return TCGroovyLoader.getDefaultGroovyLoader();
	}

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
			String resourceText = getTCGroovyLoader().getResourceText(key);
			if (resourceText == null) {
				return null;
			}
			return new StringTemplateResource(resourceText, this);
		}

		public boolean exist(String key) {
			return getTCGroovyLoader().getResource(key) != null;
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
				//config.setAllSharedVariables(new SimpleHash(this.freemarkerVariables, config.getObjectWrapper()));
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
			return getTCGroovyLoader().getResourceText(name.startsWith("/") ? name : ("/" + name));
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
				template = engine.createTemplate(
						getTCGroovyLoader().getResourceText(view.startsWith("/") ? view : ("/" + view)));
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
