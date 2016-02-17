/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class ActionHelper {

	private static final String urlCharset = "UTF-8";
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private Map<String, Object> userAgent = new HashMap<String, Object>();
	private Map<String, Object> requestParams = new LinkedHashMap<String, Object>();

	public static ActionHelper create(HttpServletRequest request, HttpServletResponse response) {
		ActionHelper actionHelper = new ActionHelper();
		{
			actionHelper.setRequest(request);
			actionHelper.setResponse(response);
		}
		ThreadLocalHelper.set("actionHelper", actionHelper);
		return actionHelper;
	}

	public static ActionHelper get() {
		ActionHelper actionHelper = ThreadLocalHelper.get("actionHelper");
		if (actionHelper == null) {
			Object obj = ThreadLocalHelper.get("servletParams");
			if (obj != null && obj instanceof Map) {
				Map params = (Map) obj;
				actionHelper = create((HttpServletRequest) params.get("request"),
						(HttpServletResponse) params.get("response"));
				ThreadLocalHelper.set("actionHelper", actionHelper);
			}
		}
		return actionHelper;
	}

	public static String fixQueryString(String queryString, String... removeParams) {
		if (queryString == null || queryString.length() < 1) {
			return "";
		}
		Map<String, String> map = new HashMap<String, String>();
		{
			if (removeParams != null) {
				for (String rp : removeParams) {
					map.put(rp, rp);
				}
			}
		}
		int indexOf = queryString.indexOf('?');
		String requestUrl = indexOf > -1 ? queryString.substring(0, indexOf + 1) : "";
		String queryParam = indexOf > -1 ? queryString.substring(indexOf + 1) : queryString;
		StringBuilder sb = new StringBuilder(queryString.length()).append(requestUrl);
		String[] params = queryParam.split("&");
		for (String p : params) {
			String name = p.indexOf('=') > -1 ? p.substring(0, p.indexOf('=')) : "";
			if (name.length() > 0 && !map.containsKey(name)) {
				sb.append(p).append('&');
				map.put(name, name);
			}
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
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

	public ActionHelper addUrlParam(String key, Object value) {
		requestParams.put(key, urlEncode(value == null ? null : String.valueOf(value)));
		return this;
	}

	public String urlParamToString(String url) {
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
		return url;
	}

	public String urlParamToString() {
		String url = null;
		if (requestParams.size() > 0) {
			StringBuilder sb = new StringBuilder(512);
			for (Entry<String, Object> entry : requestParams.entrySet()) {
				sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
			sb.setLength(sb.length() - 1);
			url = sb.toString();
		}
		return url;
	}

	public ActionHelper forward(String url) {
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

	public ActionHelper redirect(String url) {
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

	public Map<String, Object> requestParameterToMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (request != null) {
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				map.put(name, request.getParameter(name));
			}
		}
		return map;
	}

	public void download(String fileName, InputStream is, long size) {
		byte[] buffer = new byte[1024 * 10];
		int len = 0;
		OutputStream os = null;
		try {
			response.reset();
			// 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.replaceAll(" ", "").getBytes("UTF-8"), "ISO8859-1"));
			response.addHeader("Content-Length", "" + size);
			response.setContentType("application/octet-stream");
			os = response.getOutputStream();
			while ((len = is.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} catch (Exception e) {
			Exceptions.runtime("download error.", e);
		} finally {
			SocketHelper.closeWithoutError(is);
			SocketHelper.closeWithoutError(os);
		}
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
