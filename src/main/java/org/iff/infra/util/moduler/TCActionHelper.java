/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iff.infra.util.HttpHelper;
import org.iff.infra.util.ThreadLocalHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class TCActionHelper {

	private static final String urlCharset = "UTF-8";
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private Map<String, Object> userAgent = new HashMap<String, Object>();
	private Map<String, Object> requestParams = new LinkedHashMap<String, Object>();

	public static TCActionHelper create(HttpServletRequest request, HttpServletResponse response) {
		TCActionHelper actionHelper = new TCActionHelper();
		{
			actionHelper.setRequest(request);
			actionHelper.setResponse(response);
		}
		ThreadLocalHelper.set("actionHelper", actionHelper);
		return actionHelper;
	}

	public static TCActionHelper get() {
		TCActionHelper actionHelper = ThreadLocalHelper.get("actionHelper");
		if (actionHelper == null) {
			Map params = ThreadLocalHelper.get("params");
			actionHelper = create((HttpServletRequest) params.get("request"),
					(HttpServletResponse) params.get("response"));
			ThreadLocalHelper.set("actionHelper", actionHelper);
		}
		return actionHelper;
	}

	public String urlEncode(String url) {
		if (url != null && url.length() > 0) {
			try {
				return URLEncoder.encode(url, urlCharset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String urlDecode(String url) {
		if (url != null && url.length() > 0) {
			try {
				return URLDecoder.decode(url, urlCharset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public TCActionHelper addUrlParam(String key, Object value) {
		requestParams.put(key, urlEncode(value == null ? null : String.valueOf(value)));
		return this;
	}

	public TCActionHelper forward(String url) {
		if (url != null && url.length() > 0 && requestParams.size() > 0) {
			StringBuilder sb = new StringBuilder(512).append(url);
			if (url.endsWith("?") || url.endsWith("&")) {
			} else if (url.indexOf('?') > -1) {
				sb.append('&');
			} else {
				sb.append('?');
			}
			for (Entry<String, Object> entry : requestParams.entrySet()) {
				sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
			sb.setLength(sb.length() - 1);
			url = sb.toString();
		}
		try {
			request.getRequestDispatcher(url == null ? "" : url).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public TCActionHelper redirect(String url) {
		if (url != null && url.length() > 0 && requestParams.size() > 0) {
			StringBuilder sb = new StringBuilder(512).append(url);
			if (url.endsWith("?") || url.endsWith("&")) {
			} else if (url.indexOf('?') > -1) {
				sb.append('&');
			} else {
				sb.append('?');
			}
			for (Entry<String, Object> entry : requestParams.entrySet()) {
				sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
			sb.setLength(sb.length() - 1);
			url = sb.toString();
		}
		try {
			response.sendRedirect(url == null ? "" : url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public Map<String, Object> userAgent() {
		if (userAgent.isEmpty()) {
			userAgent.putAll(HttpHelper.userAgent(request.getHeader("User-Agent")));
		}
		return userAgent;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
