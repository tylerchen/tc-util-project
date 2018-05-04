/*******************************************************************************
 * Copyright (c) 2015-2-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.PrimitiveUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.PerRequestResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.apache.cxf.jaxrs.utils.ResourceUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.resource.ResourceManager;
import org.apache.cxf.service.invoker.Invoker;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.ServletContextResourceResolver;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.servicelist.ServiceListGeneratorServlet;
import org.iff.infra.util.jaxrs.gson.GsonProvider;
import org.iff.infra.util.jaxrs.xstream.XStreamProvider;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-16
 */
public class TCCXFServlet extends HttpServlet implements Filter {

	private static final long serialVersionUID = -8916352798780577499L;

	private static final Logger LOG = LogUtils.getL7dLogger(TCCXFServlet.class);

	private static final String USER_MODEL_PARAM = "user.model";
	private static final String SERVICE_ADDRESS_PARAM = "jaxrs.address";
	private static final String IGNORE_APP_PATH_PARAM = "jaxrs.application.address.ignore";
	private static final String SERVICE_CLASSES_PARAM = "jaxrs.serviceClasses";
	private static final String PROVIDERS_PARAM = "jaxrs.providers";
	private static final String OUT_INTERCEPTORS_PARAM = "jaxrs.outInterceptors";
	private static final String OUT_FAULT_INTERCEPTORS_PARAM = "jaxrs.outFaultInterceptors";
	private static final String IN_INTERCEPTORS_PARAM = "jaxrs.inInterceptors";
	private static final String INVOKER_PARAM = "jaxrs.invoker";
	private static final String SERVICE_SCOPE_PARAM = "jaxrs.scope";
	private static final String EXTENSIONS_PARAM = "jaxrs.extensions";
	private static final String LANGUAGES_PARAM = "jaxrs.languages";
	private static final String PROPERTIES_PARAM = "jaxrs.properties";
	private static final String SCHEMAS_PARAM = "jaxrs.schemaLocations";
	private static final String DOC_LOCATION_PARAM = "jaxrs.documentLocation";
	private static final String STATIC_SUB_RESOLUTION_PARAM = "jaxrs.static.subresources";
	private static final String SERVICE_SCOPE_SINGLETON = "singleton";
	private static final String SERVICE_SCOPE_REQUEST = "prototype";

	private static final String PARAMETER_SPLIT_CHAR = "class.parameter.split.char";
	private static final String DEFAULT_PARAMETER_SPLIT_CHAR = ",";
	private static final String SPACE_PARAMETER_SPLIT_CHAR = "space";

	private static final String JAXRS_APPLICATION_PARAM = "javax.ws.rs.Application";

	private ClassLoader classLoader;

	//////////////
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
		// Ensure that the correct JAX-RS implementation is loaded 
		RuntimeDelegate runtimeDelegate = new org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl();
		RuntimeDelegate.setInstance(runtimeDelegate);
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
		/////////
		String splitChar = getParameterSplitChar(servletConfig);

		String applicationClass = servletConfig.getInitParameter(JAXRS_APPLICATION_PARAM);
		if (applicationClass != null) {
			createServerFromApplication(applicationClass, servletConfig, splitChar);
			return;
		}

		JAXRSServerFactoryBean bean = new JAXRSServerFactoryBean();
		bean.setBus(getBus());

		String address = servletConfig.getInitParameter(SERVICE_ADDRESS_PARAM);
		if (address == null) {
			address = "/";
		}
		bean.setAddress(address);

		bean.setStaticSubresourceResolution(getStaticSubResolutionValue(servletConfig));

		String modelRef = servletConfig.getInitParameter(USER_MODEL_PARAM);
		if (modelRef != null) {
			bean.setModelRef(modelRef.trim());
		}
		setDocLocation(bean, servletConfig);
		setSchemasLocations(bean, servletConfig);
		setAllInterceptors(bean, servletConfig, splitChar);
		setInvoker(bean, servletConfig);

		Map<Class<?>, Map<String, List<String>>> resourceClasses = getServiceClasses(servletConfig, modelRef != null,
				splitChar);
		Map<Class<?>, ResourceProvider> resourceProviders = getResourceProviders(servletConfig, resourceClasses);

		List<?> providers = getProviders(servletConfig, splitChar);

		bean.setResourceClasses(new ArrayList<Class<?>>(resourceClasses.keySet()));
		bean.setProviders(providers);
		for (Map.Entry<Class<?>, ResourceProvider> entry : resourceProviders.entrySet()) {
			bean.setResourceProvider(entry.getKey(), entry.getValue());
		}
		setExtensions(bean, servletConfig);

		bean.create();
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

	@Override
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

	//////////////

	protected String getParameterSplitChar(ServletConfig servletConfig) {
		String param = servletConfig.getInitParameter(PARAMETER_SPLIT_CHAR);
		if (!StringUtils.isEmpty(param) && SPACE_PARAMETER_SPLIT_CHAR.equals(param.trim())) {
			return " ";
		} else {
			return DEFAULT_PARAMETER_SPLIT_CHAR;
		}
	}

	protected boolean getStaticSubResolutionValue(ServletConfig servletConfig) {
		String param = servletConfig.getInitParameter(STATIC_SUB_RESOLUTION_PARAM);
		if (param != null) {
			return Boolean.valueOf(param.trim());
		} else {
			return false;
		}
	}

	protected void setExtensions(JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
		bean.setExtensionMappings(CastUtils.cast((Map<?, ?>) parseMapSequence(servletConfig
				.getInitParameter(EXTENSIONS_PARAM))));
		bean.setLanguageMappings(CastUtils.cast((Map<?, ?>) parseMapSequence(servletConfig
				.getInitParameter(LANGUAGES_PARAM))));
		bean.setProperties(CastUtils.cast(parseMapSequence(servletConfig.getInitParameter(PROPERTIES_PARAM)),
				String.class, Object.class));
	}

	protected void setAllInterceptors(JAXRSServerFactoryBean bean, ServletConfig servletConfig, String splitChar)
			throws ServletException {
		setInterceptors(bean, servletConfig, OUT_INTERCEPTORS_PARAM, splitChar);
		setInterceptors(bean, servletConfig, OUT_FAULT_INTERCEPTORS_PARAM, splitChar);
		setInterceptors(bean, servletConfig, IN_INTERCEPTORS_PARAM, splitChar);
	}

	protected void setSchemasLocations(JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
		String schemas = servletConfig.getInitParameter(SCHEMAS_PARAM);
		if (schemas == null) {
			return;
		}
		String[] locations = StringUtils.split(schemas, " ");
		List<String> list = new ArrayList<String>();
		for (String loc : locations) {
			String theLoc = loc.trim();
			if (theLoc.length() != 0) {
				list.add(theLoc);
			}
		}
		if (list.size() > 0) {
			bean.setSchemaLocations(list);
		}
	}

	protected void setDocLocation(JAXRSServerFactoryBean bean, ServletConfig servletConfig) {
		String wadlLoc = servletConfig.getInitParameter(DOC_LOCATION_PARAM);
		if (wadlLoc != null) {
			bean.setDocLocation(wadlLoc);
		}
	}

	@SuppressWarnings("unchecked")
	protected void setInterceptors(JAXRSServerFactoryBean bean, ServletConfig servletConfig, String paramName,
			String splitChar) throws ServletException {
		String value = servletConfig.getInitParameter(paramName);
		if (value == null) {
			return;
		}
		String[] values = StringUtils.split(value, splitChar);
		List<Interceptor<? extends Message>> list = new ArrayList<Interceptor<? extends Message>>();
		for (String interceptorVal : values) {
			Map<String, List<String>> props = new HashMap<String, List<String>>();
			String theValue = getClassNameAndProperties(interceptorVal, props);
			if (theValue.length() != 0) {
				try {
					Class<?> intClass = loadClass(theValue, "Interceptor");
					Object object = intClass.newInstance();
					injectProperties(object, props);
					list.add((Interceptor<? extends Message>) object);
				} catch (ServletException ex) {
					throw ex;
				} catch (Exception ex) {
					LOG.warning("Interceptor class " + theValue + " can not be created");
					throw new ServletException(ex);
				}
			}
		}
		if (list.size() > 0) {
			if (OUT_INTERCEPTORS_PARAM.equals(paramName)) {
				bean.setOutInterceptors(list);
			} else if (OUT_FAULT_INTERCEPTORS_PARAM.equals(paramName)) {
				bean.setOutFaultInterceptors(list);
			} else {
				bean.setInInterceptors(list);
			}
		}
	}

	protected void setInvoker(JAXRSServerFactoryBean bean, ServletConfig servletConfig) throws ServletException {
		String value = servletConfig.getInitParameter(INVOKER_PARAM);
		if (value == null) {
			return;
		}
		Map<String, List<String>> props = new HashMap<String, List<String>>();
		String theValue = getClassNameAndProperties(value, props);
		if (theValue.length() != 0) {
			try {
				Class<?> intClass = loadClass(theValue, "Invoker");
				Object object = intClass.newInstance();
				injectProperties(object, props);
				bean.setInvoker((Invoker) object);
			} catch (ServletException ex) {
				throw ex;
			} catch (Exception ex) {
				LOG.warning("Invoker class " + theValue + " can not be created");
				throw new ServletException(ex);
			}
		}

	}

	protected Map<Class<?>, Map<String, List<String>>> getServiceClasses(ServletConfig servletConfig,
			boolean modelAvailable, String splitChar) throws ServletException {
		String serviceBeans = servletConfig.getInitParameter(SERVICE_CLASSES_PARAM);
		//--
		if (serviceBeans == null) {
			serviceBeans = classNames;
		}
		//--
		if (serviceBeans == null) {
			if (modelAvailable) {
				return Collections.emptyMap();
			}
			throw new ServletException("At least one resource class should be specified");
		}
		String[] classNames = StringUtils.split(serviceBeans, splitChar);
		Map<Class<?>, Map<String, List<String>>> map = new HashMap<Class<?>, Map<String, List<String>>>();
		for (String cName : classNames) {
			Map<String, List<String>> props = new HashMap<String, List<String>>();
			String theName = getClassNameAndProperties(cName, props);
			if (theName.length() != 0) {
				Class<?> cls = loadClass(theName);
				map.put(cls, props);
			}
		}
		if (map.isEmpty()) {
			throw new ServletException("At least one resource class should be specified");
		}
		return map;
	}

	protected List<?> getProviders(ServletConfig servletConfig, String splitChar) throws ServletException {
		String providersList = servletConfig.getInitParameter(PROVIDERS_PARAM);
		if (providersList == null) {
			return Arrays.asList(new GsonProvider(), new XStreamProvider());
		}
		String[] classNames = StringUtils.split(providersList, splitChar);
		List<Object> providers = new ArrayList<Object>();
		for (String cName : classNames) {
			Map<String, List<String>> props = new HashMap<String, List<String>>();
			String theName = getClassNameAndProperties(cName, props);
			if (theName.length() != 0) {
				Class<?> cls = loadClass(theName);
				providers.add(createSingletonInstance(cls, props, servletConfig));
			}
		}
		return providers;
	}

	private String getClassNameAndProperties(String cName, Map<String, List<String>> props) {
		String theName = cName.trim();
		int ind = theName.indexOf("(");
		if (ind != -1 && theName.endsWith(")")) {
			props.putAll(parseMapListSequence(theName.substring(ind + 1, theName.length() - 1)));
			theName = theName.substring(0, ind).trim();
		}
		return theName;
	}

	protected static Map<String, List<String>> parseMapListSequence(String sequence) {
		if (sequence != null) {
			sequence = sequence.trim();
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			String[] pairs = StringUtils.split(sequence, " ");
			for (String pair : pairs) {
				String thePair = pair.trim();
				if (thePair.length() == 0) {
					continue;
				}
				String[] values = StringUtils.split(thePair, "=");
				String key;
				String value;
				if (values.length == 2) {
					key = values[0].trim();
					value = values[1].trim();
				} else {
					key = thePair;
					value = "";
				}
				List<String> list = map.get(key);
				if (list == null) {
					list = new LinkedList<String>();
					map.put(key, list);
				}
				list.add(value);
			}
			return map;
		} else {
			return Collections.emptyMap();
		}
	}

	protected Map<Class<?>, ResourceProvider> getResourceProviders(ServletConfig servletConfig,
			Map<Class<?>, Map<String, List<String>>> resourceClasses) throws ServletException {
		String scope = servletConfig.getInitParameter(SERVICE_SCOPE_PARAM);
		if (scope != null && !SERVICE_SCOPE_SINGLETON.equals(scope) && !SERVICE_SCOPE_REQUEST.equals(scope)) {
			throw new ServletException("Only singleton and prototype scopes are supported");
		}
		boolean isPrototype = SERVICE_SCOPE_REQUEST.equals(scope);
		Map<Class<?>, ResourceProvider> map = new HashMap<Class<?>, ResourceProvider>();
		for (Map.Entry<Class<?>, Map<String, List<String>>> entry : resourceClasses.entrySet()) {
			Class<?> c = entry.getKey();
			map.put(c, isPrototype ? new PerRequestResourceProvider(c) : new SingletonResourceProvider(
					createSingletonInstance(c, entry.getValue(), servletConfig), true));
		}
		return map;
	}

	protected Object createSingletonInstance(Class<?> cls, Map<String, List<String>> props, ServletConfig sc)
			throws ServletException {
		Constructor<?> c = ResourceUtils.findResourceConstructor(cls, false);
		if (c == null) {
			throw new ServletException("No valid constructor found for " + cls.getName());
		}
		boolean isDefault = c.getParameterTypes().length == 0;
		if (!isDefault
				&& (c.getParameterTypes().length != 1 || c.getParameterTypes()[0] != ServletConfig.class
						&& c.getParameterTypes()[0] != ServletContext.class)) {
			throw new ServletException("Resource classes with singleton scope can only have "
					+ "ServletConfig or ServletContext instances injected through their constructors");
		}
		Object[] values = isDefault ? new Object[] {}
				: new Object[] { c.getParameterTypes()[0] == ServletConfig.class ? sc : sc.getServletContext() };
		try {
			Object instance = c.newInstance(values);
			injectProperties(instance, props);
			configureSingleton(instance);
			return instance;
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new ServletException("Resource class " + cls.getName() + " can not be instantiated");
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new ServletException("Resource class " + cls.getName()
					+ " can not be instantiated due to IllegalAccessException");
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			throw new ServletException("Resource class " + cls.getName()
					+ " can not be instantiated due to InvocationTargetException");
		}
	}

	private void injectProperties(Object instance, Map<String, List<String>> props) {
		if (props == null || props.isEmpty()) {
			return;
		}
		Method[] methods = instance.getClass().getMethods();
		Map<String, Method> methodsMap = new HashMap<String, Method>();
		for (Method m : methods) {
			methodsMap.put(m.getName(), m);
		}
		for (Map.Entry<String, List<String>> entry : props.entrySet()) {
			Method m = methodsMap.get("set" + Character.toUpperCase(entry.getKey().charAt(0))
					+ entry.getKey().substring(1));
			if (m != null) {
				Class<?> type = m.getParameterTypes()[0];
				Object value;
				if (InjectionUtils.isPrimitive(type)) {
					value = PrimitiveUtils.read(entry.getValue().get(0), type);
				} else {
					value = entry.getValue();
				}
				InjectionUtils.injectThroughMethod(instance, m, value);
			}
		}
	}

	protected void configureSingleton(Object instance) {

	}

	protected void createServerFromApplication(String cName, ServletConfig servletConfig, String splitChar)
			throws ServletException {
		Map<String, List<String>> props = new HashMap<String, List<String>>();
		cName = getClassNameAndProperties(cName, props);
		Class<?> appClass = loadClass(cName, "Application");
		Application app = (Application) createSingletonInstance(appClass, props, servletConfig);

		String ignoreParam = servletConfig.getInitParameter(IGNORE_APP_PATH_PARAM);
		JAXRSServerFactoryBean bean = ResourceUtils.createApplication(app, MessageUtils.isTrue(ignoreParam),
				getStaticSubResolutionValue(servletConfig));
		setAllInterceptors(bean, servletConfig, splitChar);
		setInvoker(bean, servletConfig);
		setExtensions(bean, servletConfig);
		setDocLocation(bean, servletConfig);
		setSchemasLocations(bean, servletConfig);

		bean.setBus(getBus());
		bean.create();
	}

	protected Class<?> loadClass(String cName) throws ServletException {
		return loadClass(cName, "Resource");
	}

	protected Class<?> loadClass(String cName, String classType) throws ServletException {
		try {

			Class<?> cls = null;
			if (classLoader == null) {
				cls = ClassLoaderUtils.loadClass(cName, TCCXFServlet.class);
			} else {
				cls = classLoader.loadClass(cName);
			}
			return cls;
		} catch (ClassNotFoundException ex) {
			throw new ServletException("No " + classType + " class " + cName.trim() + " can be found", ex);
		}
	}

	public void setClassLoader(ClassLoader loader) {
		this.classLoader = loader;
	}

	//======
	private String classNames = "";

	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}

}
