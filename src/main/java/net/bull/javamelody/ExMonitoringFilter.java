/*
 * Copyright 2008-2016 by Emeric Vernat
 *
 *     This file is part of Java Melody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bull.javamelody;

import static net.bull.javamelody.HttpParameters.COLLECTOR_PARAMETER;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filtre de servlet pour le monitoring.
 * C'est la classe de ce filtre qui doit être déclarée dans le fichier web.xml de la webapp.
 * @author Emeric Vernat
 */
public class ExMonitoringFilter {

	// Ces variables httpCounter et errorCounter conservent un état qui est global au filtre
	// et à l'application (donc thread-safe).

	private boolean logEnabled;
	private HttpAuth httpAuth;
	private String monitoringUrl;
	private FilterContext filterContext;

	/**
	 * Constructeur.
	 */
	public ExMonitoringFilter(String monitoringUrl) {
		super();
		this.monitoringUrl = monitoringUrl;
	}

	public void init(HttpServletRequest request, HttpServletResponse response) {
		final long start = System.currentTimeMillis(); // NOPMD
		LOG.debug("JavaMelody filter init started");
		Parameters.initialize(request.getSession(false).getServletContext());
		this.filterContext = new FilterContext();
		this.httpAuth = new HttpAuth();

		logEnabled = Boolean.parseBoolean(Parameters.getParameter(Parameter.LOG));

		final long duration = System.currentTimeMillis() - start;
		LOG.debug("JavaMelody filter init done in " + duration + " ms");
	}

	public void doFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (httpRequest.getRequestURI().equals(getMonitoringUrl(httpRequest))) {
			doMonitoring(httpRequest, httpResponse);
			return;
		}
	}

	public String getMonitoringUrl(HttpServletRequest httpRequest) {
		if (monitoringUrl == null) {
			monitoringUrl = httpRequest.getContextPath() + Parameters.getMonitoringPath();
		}
		return monitoringUrl;
	}

	public void doMonitoring(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		if (!isAllowed(httpRequest, httpResponse)) {
			return;
		}

		final Collector collector = filterContext.getCollector();
		final MonitoringController monitoringController = new MonitoringController(collector, null);
		monitoringController.doActionIfNeededAndReport(httpRequest, httpResponse,
				httpRequest.getSession(false).getServletContext());

		if ("stop".equalsIgnoreCase(httpRequest.getParameter(COLLECTOR_PARAMETER))) {
			// on a été appelé par un serveur de collecte qui fera l'aggrégation dans le temps,
			// le stockage et les courbes, donc on arrête le timer s'il est démarré
			// et on vide les stats pour que le serveur de collecte ne récupère que les deltas
			for (final Counter counter : collector.getCounters()) {
				counter.clear();
			}

			if (!collector.isStopped()) {
				LOG.debug("Stopping the javamelody collector in this webapp, because a collector server from "
						+ httpRequest.getRemoteAddr() + " wants to collect the data itself");
				filterContext.stopCollector();
			}
		}
	}

	// cette méthode est protected pour pouvoir être surchargée dans une classe définie par l'application
	public boolean isAllowed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		return httpAuth.isAllowed(httpRequest, httpResponse);
	}

	// cette méthode est protected pour pouvoir être surchargée dans une classe définie par l'application
	public void log(HttpServletRequest httpRequest, String requestName, long duration, boolean systemError,
			int responseSize) {
		if (!logEnabled) {
			return;
		}
		final String filterName = "javamelody";
		LOG.logHttpRequest(httpRequest, requestName, duration, systemError, responseSize, filterName);
	}

	public static void throwException(Throwable t) throws IOException, ServletException {
		if (t instanceof Error) {
			throw (Error) t;
		} else if (t instanceof RuntimeException) {
			throw (RuntimeException) t;
		} else if (t instanceof IOException) {
			throw (IOException) t;
		} else if (t instanceof ServletException) {
			throw (ServletException) t;
		} else {
			// n'arrive à priori pas car chain.doFilter ne déclare que IOException et ServletException
			// mais au cas où
			throw new ServletException(t.getMessage(), t);
		}
	}

	public FilterContext getFilterContext() {
		return filterContext;
	}
}
