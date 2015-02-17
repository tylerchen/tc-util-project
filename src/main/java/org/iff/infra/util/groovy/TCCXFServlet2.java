/*******************************************************************************
 * Copyright (c) 2015-2-16 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.Bus;
import org.apache.cxf.BusException;
import org.apache.cxf.BusFactory;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.common.classloader.ClassLoaderUtils.ClassLoaderHolder;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.resource.ResourceManager;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.ServletContextResourceResolver;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.servicelist.ServiceListGeneratorServlet;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-2-16
 */
public class TCCXFServlet2 extends HttpServlet implements Filter {

	/**
	 * List of well-known HTTP 1.1 verbs, with POST and GET being the most used verbs at the top 
	 */
	private static final List<String> KNOWN_HTTP_VERBS = Arrays.asList(new String[] { "POST", "GET", "PUT", "DELETE",
			"HEAD", "OPTIONS", "TRACE" });

	private static final String STATIC_RESOURCES_PARAMETER = "static-resources-list";
	private static final String STATIC_WELCOME_FILE_PARAMETER = "static-welcome-file";

	private static final String REDIRECTS_PARAMETER = "redirects-list";
	private static final String REDIRECT_SERVLET_NAME_PARAMETER = "redirect-servlet-name";
	private static final String REDIRECT_SERVLET_PATH_PARAMETER = "redirect-servlet-path";
	private static final String REDIRECT_ATTRIBUTES_PARAMETER = "redirect-attributes";
	private static final String REDIRECT_QUERY_CHECK_PARAMETER = "redirect-query-check";

	private static final Map<String, String> STATIC_CONTENT_TYPES;

	static {
		STATIC_CONTENT_TYPES = new HashMap<String, String>();
		STATIC_CONTENT_TYPES.put("html", "text/html");
		STATIC_CONTENT_TYPES.put("txt", "text/plain");
		STATIC_CONTENT_TYPES.put("css", "text/css");
		STATIC_CONTENT_TYPES.put("pdf", "application/pdf");
		STATIC_CONTENT_TYPES.put("xsd", "application/xml");
		// TODO : add more types if needed
	}

	private List<Pattern> staticResourcesList;
	private String staticWelcomeFile;
	private List<Pattern> redirectList;
	private String dispatcherServletPath;
	private String dispatcherServletName;
	private Map<String, String> redirectAttributes;
	private boolean redirectQueryCheck;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		staticResourcesList = parseListSequence(servletConfig.getInitParameter(STATIC_RESOURCES_PARAMETER));
		staticWelcomeFile = servletConfig.getInitParameter(STATIC_WELCOME_FILE_PARAMETER);

		redirectList = parseListSequence(servletConfig.getInitParameter(REDIRECTS_PARAMETER));
		redirectQueryCheck = Boolean.valueOf(servletConfig.getInitParameter(REDIRECT_QUERY_CHECK_PARAMETER));
		dispatcherServletName = servletConfig.getInitParameter(REDIRECT_SERVLET_NAME_PARAMETER);
		dispatcherServletPath = servletConfig.getInitParameter(REDIRECT_SERVLET_PATH_PARAMETER);

		redirectAttributes = parseMapSequence(servletConfig.getInitParameter(REDIRECT_ATTRIBUTES_PARAMETER));

		//
		if (this.bus == null && loadBus) {
			loadBus(servletConfig);
		}
		if (this.bus != null) {
			loader = bus.getExtension(ClassLoader.class);
			ResourceManager resourceManager = bus.getExtension(ResourceManager.class);
			resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
			if (destinationRegistry == null) {
				this.destinationRegistry = getDestinationRegistryFromBus(this.bus);
			}
		}

		this.controller = createServletController(servletConfig);
	}

	public final void init(final FilterConfig filterConfig) throws ServletException {
		init(new ServletConfig() {
			public String getServletName() {
				return filterConfig.getFilterName();
			}

			public ServletContext getServletContext() {
				return filterConfig.getServletContext();
			}

			public String getInitParameter(String name) {
				return filterConfig.getInitParameter(name);
			}

			public Enumeration<String> getInitParameterNames() {
				return filterConfig.getInitParameterNames();
			}
		});
	}

	protected List<Pattern> parseListSequence(String values) {
		if (values != null) {
			List<Pattern> list = new LinkedList<Pattern>();
			String[] pathValues = StringUtils.split(values, " ");
			for (String value : pathValues) {
				String theValue = value.trim();
				if (theValue.length() > 0) {
					list.add(Pattern.compile(theValue));
				}
			}
			return list;
		} else {
			return null;
		}
	}

	protected Map<String, String> parseMapSequence(String sequence) {
		if (sequence != null) {
			sequence = sequence.trim();
			Map<String, String> map = new HashMap<String, String>();
			String[] pairs = StringUtils.split(sequence, " ");
			for (String pair : pairs) {
				String thePair = pair.trim();
				if (thePair.length() == 0) {
					continue;
				}
				String[] value = StringUtils.split(thePair, "=");
				if (value.length == 2) {
					map.put(value[0].trim(), value[1].trim());
				} else {
					map.put(thePair, "");
				}
			}
			return map;
		} else {
			return Collections.emptyMap();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		handleRequest(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		handleRequest(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		handleRequest(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}

	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		handleRequest(request, response);
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		handleRequest(request, response);
	}

	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

		HttpServletRequest request;
		HttpServletResponse response;

		try {
			request = (HttpServletRequest) req;
			response = (HttpServletResponse) res;
		} catch (ClassCastException e) {
			throw new ServletException("Unrecognized HTTP request or response object");
		}

		String method = request.getMethod();
		if (KNOWN_HTTP_VERBS.contains(method)) {
			super.service(request, response);
		} else {
			handleRequest(request, response);
		}
	}

	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if ((dispatcherServletPath != null || dispatcherServletName != null)
				&& (redirectList != null && matchPath(redirectList, request) || redirectList == null)) {
			// if no redirectList is provided then this servlet is redirecting only
			redirect(request, response, request.getPathInfo());
			return;
		}
		boolean staticResourcesMatch = staticResourcesList != null && matchPath(staticResourcesList, request);
		boolean staticWelcomeFileMatch = staticWelcomeFile != null
				&& (StringUtils.isEmpty(request.getPathInfo()) || request.getPathInfo().equals("/"));
		if (staticResourcesMatch || staticWelcomeFileMatch) {
			serveStaticContent(request, response, staticWelcomeFileMatch ? staticWelcomeFile : request.getPathInfo());
			return;
		}
		invoke(request, response);
	}

	private boolean matchPath(List<Pattern> values, HttpServletRequest request) {
		String path = request.getPathInfo();
		if (path == null) {
			path = "/";
		}
		if (redirectQueryCheck) {
			String queryString = request.getQueryString();
			if (queryString != null && queryString.length() > 0) {
				path += "?" + queryString;
			}
		}
		for (Pattern pattern : values) {
			if (pattern.matcher(path).matches()) {
				return true;
			}
		}
		return false;
	}

	protected void serveStaticContent(HttpServletRequest request, HttpServletResponse response, String pathInfo)
			throws ServletException {
		InputStream is = super.getServletContext().getResourceAsStream(pathInfo);
		if (is == null) {
			throw new ServletException("Static resource " + pathInfo + " is not available");
		}
		try {
			int ind = pathInfo.lastIndexOf(".");
			if (ind != -1 && ind < pathInfo.length()) {
				String type = STATIC_CONTENT_TYPES.get(pathInfo.substring(ind + 1));
				if (type != null) {
					response.setContentType(type);
				}
			}

			ServletOutputStream os = response.getOutputStream();
			IOUtils.copy(is, os);
			os.flush();
		} catch (IOException ex) {
			throw new ServletException("Static resource " + pathInfo + " can not be written to the output stream");
		}

	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String pathInfo)
			throws ServletException {

		String theServletPath = dispatcherServletPath == null ? "/" : dispatcherServletPath;

		ServletContext sc = super.getServletContext();
		RequestDispatcher rd = dispatcherServletName != null ? sc.getNamedDispatcher(dispatcherServletName) : sc
				.getRequestDispatcher(theServletPath + pathInfo);
		if (rd == null) {
			String errorMessage = "No RequestDispatcher can be created for path " + pathInfo;
			if (dispatcherServletName != null) {
				errorMessage += ", dispatcher name: " + dispatcherServletName;
			}
			throw new ServletException(errorMessage);
		}
		try {
			for (Map.Entry<String, String> entry : redirectAttributes.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
			HttpServletRequestFilter servletRequest = new HttpServletRequestFilter(request, pathInfo, theServletPath);
			rd.forward(servletRequest, response);
		} catch (Throwable ex) {
			throw new ServletException("RequestDispatcher for path " + pathInfo + " has failed");
		}
	}

	private static class HttpServletRequestFilter extends HttpServletRequestWrapper {

		private String pathInfo;
		private String servletPath;

		public HttpServletRequestFilter(HttpServletRequest request, String pathInfo, String servletPath) {
			super(request);
			this.pathInfo = pathInfo;
			this.servletPath = servletPath;
		}

		@Override
		public String getServletPath() {
			return servletPath;
		}

		@Override
		public String getPathInfo() {
			return pathInfo;
		}

		@Override
		public String getRequestURI() {
			String contextPath = getContextPath();
			if ("/".equals(contextPath)) {
				contextPath = "";
			}
			return contextPath + servletPath + pathInfo;
		}

		@Override
		public String getParameter(String name) {
			if (AbstractHTTPDestination.SERVICE_REDIRECTION.equals(name)) {
				return "true";
			}
			return super.getParameter(name);
		}
	}

	//=============CXFNonSpringServlet
	private DestinationRegistry destinationRegistry;
	private boolean globalRegistry;
	private Bus bus;
	private ServletController controller;
	private ClassLoader loader;
	private boolean loadBus = true;

	public TCCXFServlet2() {
	}

	public TCCXFServlet2(DestinationRegistry destinationRegistry) {
		this(destinationRegistry, true);
	}

	public TCCXFServlet2(DestinationRegistry destinationRegistry, boolean loadBus) {
		this.destinationRegistry = destinationRegistry;
		this.globalRegistry = destinationRegistry != null;
		this.loadBus = loadBus;
	}

	private static DestinationRegistry getDestinationRegistryFromBus(Bus bus) {
		DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
		try {
			DestinationFactory df = dfm.getDestinationFactory("http://cxf.apache.org/transports/http/configuration");
			if (df instanceof HTTPTransportFactory) {
				HTTPTransportFactory transportFactory = (HTTPTransportFactory) df;
				return transportFactory.getRegistry();
			}
		} catch (BusException e) {
			// why are we throwing a busexception if the DF isn't found?
		}
		return null;
	}

	protected void loadBus(ServletConfig sc) {
		this.bus = BusFactory.newInstance().createBus();
		JAXRSServerFactoryBean sf = RuntimeDelegate.getInstance().createEndpoint(new Application(), JAXRSServerFactoryBean.class);
		sf.setAddress("/");
		sf.setResourceClasses(classes);
		//sf.setResourceProvider(TestWebService.class, new org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider(new TestWebService()))
		sf.create();
	}

	private ServletController createServletController(ServletConfig servletConfig) {
		HttpServlet serviceListGeneratorServlet = new ServiceListGeneratorServlet(destinationRegistry, bus);
		ServletController newController = new ServletController(destinationRegistry, servletConfig,
				serviceListGeneratorServlet);
		return newController;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		ClassLoaderHolder origLoader = null;
		Bus origBus = null;
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			try {
				if (loader != null) {
					origLoader = ClassLoaderUtils.setThreadContextClassloader(loader);
				}
				if (bus != null) {
					origBus = BusFactory.getAndSetThreadDefaultBus(bus);
				}
				if (controller.filter((HttpServletRequest) request, (HttpServletResponse) response)) {
					return;
				}
			} finally {
				if (origBus != bus) {
					BusFactory.setThreadDefaultBus(origBus);
				}
				if (origLoader != null) {
					origLoader.reset();
				}
			}
		}
		chain.doFilter(request, response);
	}

	protected void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		ClassLoaderHolder origLoader = null;
		Bus origBus = null;
		try {
			if (loader != null) {
				origLoader = ClassLoaderUtils.setThreadContextClassloader(loader);
			}
			if (bus != null) {
				origBus = BusFactory.getAndSetThreadDefaultBus(bus);
			}
			controller.invoke(request, response);
		} finally {
			if (origBus != bus) {
				BusFactory.setThreadDefaultBus(null);
			}
			if (origLoader != null) {
				origLoader.reset();
			}
		}
	}

	public void destroy() {
		if (!globalRegistry) {
			for (String path : destinationRegistry.getDestinationsPaths()) {
				// clean up the destination in case the destination itself can 
				// no longer access the registry later
				AbstractHTTPDestination dest = destinationRegistry.getDestinationForPath(path);
				synchronized (dest) {
					destinationRegistry.removeDestination(path);
					dest.releaseRegistry();
				}
			}
			destinationRegistry = null;
		}
		destroyBus();
	}

	public void destroyBus() {
		if (bus != null) {
			bus.shutdown(true);
			bus = null;
		}
	}

	//==========
	private Class<?>[] classes = new Class<?>[0];

	public void setClasses(Class<?>[] classes) {
		this.classes = classes;
	}

}
