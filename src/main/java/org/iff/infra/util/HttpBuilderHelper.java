/*******************************************************************************
 * Copyright (c) 2014-6-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * a helper for http.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-6-28
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HttpBuilderHelper {

	public static final String CONTENT_TYPE_ALL = "*/*";
	public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
	public static final String CONTENT_TYPE_JSON = "application/JSON";
	public static final String CONTENT_TYPE_XML = "text/xml";

	public static void main(String[] args) {
		post().s02_toUrl("http://localhost:8989/default/set/127.0.0.1/test").s03_connect().s04_doOutput().s05_doInput()
				.s06_redirect().s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset()
				.s13_userAgent().s14_contentType().s15_data("data", "S我们是中国人sss").s16_request()
				.s17_json(new Process<Post, String>() {
					public Object run(Post get, String result) {
						System.out.println(result);
						return result;
					}
				});
		get().s02_toUrl("http://localhost:8989/default/list/127.0.0.1/").s03_connect().s04_doOutput().s05_doInput()
				.s06_redirect().s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset()
				.s13_userAgent().s14_contentType().s15_request().s16_json(new Process<Get, String>() {
					public Object run(Get get, String result) {
						System.out.println(result);
						return result;
					}
				});
		get().s02_toUrl("http://www.myip.cn/").s03_connectByHttpProxy("182.37.5.12", 8888)
				.s04_doOutput().s05_doInput().s06_redirect().s07_useCaches().s08_connectTimeout().s09_readTimeout()
				.s11_accept().s12_charset().s13_userAgent().s14_contentType().s15_request()
				.s16_html(new Process<Get, String>() {
					public Object run(Get get, String result) {
						System.out.println(result);
						return result;
					}
				});
	}

	/**
	 * process result.
	 */
	public static interface Process<T extends Request, E> {
		Object run(T request, E result);
	}

	public static Get get() {
		Get get = new HttpBuilderHelper().new Get();
		get.method = "GET";
		return get;
	}

	public static Post post() {
		Post post = new HttpBuilderHelper().new Post();
		post.method = "POST";
		return post;
	}

	public static Put put() {
		Put put = new HttpBuilderHelper().new Put();
		put.method = "PUT";
		return put;
	}

	public static Delete delete() {
		Delete del = new HttpBuilderHelper().new Delete();
		del.method = "DELETE";
		return del;
	}

	public static Head head() {
		Head head = new HttpBuilderHelper().new Head();
		head.method = "HEAD";
		return head;
	}

	public static Options options() {
		Options options = new HttpBuilderHelper().new Options();
		options.method = "OPTIONS";
		return options;
	}

	public static Trace trace() {
		Trace trace = new HttpBuilderHelper().new Trace();
		trace.method = "TRACE";
		return trace;
	}

	public abstract class Request<T extends Request> {
		HttpURLConnection connection;
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Map<String, Object> datas = new LinkedHashMap<String, Object>();
		Map<String, List<String>> headers;
		String url;
		String method;

		public T s01_param(String name, Object value) {
			params.put(name, urlEncode(value == null ? null : String.valueOf(value)));
			return (T) this;
		}

		public T s02_toUrl(String url) {
			Assert.notBlank("Url is requrired!");
			this.url = paramsToString(url, params);
			return (T) this;
		}

		public T s03_connect() {
			try {
				this.connection = (HttpURLConnection) new URL(url).openConnection();
				this.connection.setRequestMethod(method);
			} catch (Exception e) {
				Exceptions.runtime(FCS.get("Can't not open connection for url: {url}!", url), e);
			}
			return (T) this;
		}

		public T s03_connectByHttpProxy(String ip, int port) {
			try {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
				this.connection = (HttpURLConnection) new URL(url).openConnection(proxy);
				this.connection.setRequestMethod(method);
			} catch (Exception e) {
				Exceptions.runtime(FCS.get("Can't not open connection for url: {url}!", url), e);
			}
			return (T) this;
		}

		public T s03_connectBySocketProxy(String ip, int port) {
			try {
				Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
				this.connection = (HttpURLConnection) new URL(url).openConnection(proxy);
				this.connection.setRequestMethod(method);
			} catch (Exception e) {
				Exceptions.runtime(FCS.get("Can't not open connection for url: {url}!", url), e);
			}
			return (T) this;
		}

		public T s04_doOutput(boolean ouput) {
			connection.setDoOutput(ouput);
			return (T) this;
		}

		public T s04_doOutput() {
			return s04_doOutput(true);
		}

		public T s05_doInput(boolean ouput) {
			connection.setDoInput(ouput);
			return (T) this;
		}

		public T s05_doInput() {
			return s05_doInput(true);
		}

		public T s06_redirect(boolean redirect) {
			connection.setInstanceFollowRedirects(redirect);
			return (T) this;
		}

		public T s06_redirect() {
			return s06_redirect(true);
		}

		public T s07_useCaches(boolean useCaches) {
			connection.setUseCaches(useCaches);
			return (T) this;
		}

		public T s07_useCaches() {
			return s07_useCaches(false);
		}

		public T s08_connectTimeout(int timeoutMs) {
			connection.setConnectTimeout(timeoutMs);
			return (T) this;
		}

		public T s08_connectTimeout() {
			return s08_connectTimeout(30 * 1000);
		}

		public T s09_readTimeout(int timeoutMs) {
			connection.setReadTimeout(timeoutMs);
			return (T) this;
		}

		public T s09_readTimeout() {
			return s09_readTimeout(30 * 1000);
		}

		public T s10_head(String name, String value) {
			connection.setRequestProperty(name, value);
			return (T) this;
		}

		public T s10_baseAuth(String username, String password) {
			String encoded = new String(BaseCryptHelper.encodeBase64(username + ":" + password));
			s10_head("Proxy-Authorization", "Basic " + encoded);
			return (T) this;
		}

		public T s11_accept(String value) {
			return s10_head("accept", value);
		}

		public T s11_accept() {
			return s11_accept("*/*");
		}

		public T s12_charset(String value) {
			return s10_head("charset", value);
		}

		public T s12_charset() {
			return s12_charset("UTF-8");
		}

		public T s13_userAgent(String value) {
			return s10_head("User-Agent", value);
		}

		public T s13_userAgent() {
			return s13_userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0");
		}

		public T s14_contentType(String value) {
			return s10_head("Content-Type", value);
		}

		public T s14_contentType() {
			return s14_contentType("application/x-www-form-urlencoded");
		}

		T s_header(Process<T, Map<String, String>> process) {
			try {
				Map<String, String> newHeader = new LinkedHashMap<String, String>();
				for (Entry<String, List<String>> entry : headers.entrySet()) {
					String value = entry.getValue() != null && entry.getValue().size() > 0 ? entry.getValue().get(0)
							: null;
					newHeader.put(entry.getKey(), value);
				}
				process.run((T) this, newHeader);
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				try {
					connection.disconnect();
				} catch (Exception e) {
				}
			}
			return (T) this;
		}

		T s_header2(Process<T, Map<String, List<String>>> process) {
			try {
				process.run((T) this, headers);
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				try {
					connection.disconnect();
				} catch (Exception e) {
				}
			}
			return (T) this;
		}

		T s_text(Process<T, String> process) {
			try {
				String json = SocketHelper.getContent(connection.getInputStream(), true);
				headers = connection.getHeaderFields();
				process.run((T) this, json);
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				try {
					connection.disconnect();
				} catch (Exception e) {
				}
			}
			return (T) this;
		}

		T s_binnary(Process<T, byte[]> process) {
			try {
				byte[] bs = SocketHelper.getByte(connection.getInputStream(), true);
				headers = connection.getHeaderFields();
				process.run((T) this, bs);
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				try {
					connection.disconnect();
				} catch (Exception e) {
				}
			}
			return (T) this;
		}

		String paramsToString(String url, Map<String, Object> map) {
			if (url != null && url.length() > 0 && map.size() > 0) {
				StringBuilder sb = new StringBuilder(512).append(url);
				if (url.endsWith("?") || url.endsWith("&")) {
				} else if (url.indexOf('?') > -1) {
					sb.append('&');
				} else {
					sb.append('?');
				}
				for (Entry<String, Object> entry : map.entrySet()) {
					sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
				}
				sb.setLength(sb.length() - 1);
				url = sb.toString();
			}
			return url;
		}

		String paramsToString(Map<String, Object> map) {
			String url = null;
			if (map.size() > 0) {
				StringBuilder sb = new StringBuilder(512);
				for (Entry<String, Object> entry : map.entrySet()) {
					sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
				}
				sb.setLength(sb.length() - 1);
				url = sb.toString();
			}
			return url;
		}
	}

	public class Get extends Request<Get> {
		public Get s15_request() {
			try {
				connection.connect();
			} catch (Exception e) {
				Exceptions.runtime("Http connect error!", e);
			}
			return this;
		}

		public Get s16_text(Process<Get, String> process) {
			return s_text(process);
		}

		public Get s16_json(Process<Get, String> process) {
			return s16_text(process);
		}

		public Get s16_html(Process<Get, String> process) {
			return s16_text(process);
		}

		public Get s16_binnary(Process<Get, byte[]> process) {
			return s_binnary(process);
		}

		public Get s17_header(Process<Get, Map<String, String>> process) {
			return s_header(process);
		}

		public Get s17_header2(Process<Get, Map<String, List<String>>> process) {
			return s_header2(process);
		}
	}

	public class Post extends Request<Post> {
		byte[] bs;

		public Post s15_data(String name, Object value) {
			datas.put(name, urlEncode(value == null ? null : String.valueOf(value)));
			return this;
		}

		public Post s15_data(byte[] bs) {
			bs = bs == null ? new byte[0] : bs;
			return this;
		}

		public Post s16_request() {
			DataOutputStream wr = null;
			try {
				String data = bs == null ? super.paramsToString(datas) : null;
				byte[] byteData = bs == null ? data.getBytes("UTF-8") : bs;
				s10_head("Content-Length", String.valueOf(byteData.length));
				wr = new DataOutputStream(connection.getOutputStream());
				wr.write(byteData);
				wr.flush();
				wr.close();
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				StreamHelper.closeWithoutError(wr);
			}
			return this;
		}

		public Post s17_text(Process<Post, String> process) {
			return s_text(process);
		}

		public Post s17_json(Process<Post, String> process) {
			return s17_text(process);
		}

		public Post s17_html(Process<Post, String> process) {
			return s17_text(process);
		}

		public Post s17_binnary(Process<Post, byte[]> process) {
			return s_binnary(process);
		}

		public Post s18_header(Process<Post, Map<String, String>> process) {
			return s_header(process);
		}

		public Post s18_header2(Process<Post, Map<String, List<String>>> process) {
			return s_header2(process);
		}
	}

	public class Put extends Request<Put> {
		byte[] bs;

		public Put s15_data(String name, Object value) {
			datas.put(name, urlEncode(value == null ? null : String.valueOf(value)));
			return this;
		}

		public Put s15_data(byte[] bs) {
			bs = bs == null ? new byte[0] : bs;
			return this;
		}

		public Put s16_request() {
			DataOutputStream wr = null;
			try {
				String data = bs == null ? super.paramsToString(datas) : null;
				byte[] byteData = bs == null ? data.getBytes("UTF-8") : bs;
				s10_head("Content-Length", String.valueOf(byteData.length));
				wr = new DataOutputStream(connection.getOutputStream());
				wr.write(byteData);
				wr.flush();
				wr.close();
			} catch (Exception e) {
				Exceptions.runtime("Request Error: " + url, e);
			} finally {
				StreamHelper.closeWithoutError(wr);
			}
			return this;
		}

		public Put s17_text(Process<Put, String> process) {
			return s_text(process);
		}

		public Put s17_json(Process<Put, String> process) {
			return s17_text(process);
		}

		public Put s17_html(Process<Put, String> process) {
			return s17_text(process);
		}

		public Put s17_binnary(Process<Put, byte[]> process) {
			return s_binnary(process);
		}

		public Put s18_header(Process<Put, Map<String, String>> process) {
			return s_header(process);
		}

		public Put s18_header2(Process<Put, Map<String, List<String>>> process) {
			return s_header2(process);
		}
	}

	public class Delete extends Request<Delete> {
		public Delete s15_request() {
			try {
				connection.connect();
			} catch (Exception e) {
				Exceptions.runtime("Http connect error!", e);
			}
			return this;
		}

		public Delete s16_text(Process<Delete, String> process) {
			return s_text(process);
		}

		public Delete s16_json(Process<Delete, String> process) {
			return s16_text(process);
		}

		public Delete s16_html(Process<Delete, String> process) {
			return s16_text(process);
		}

		public Delete s16_binnary(Process<Delete, byte[]> process) {
			return s_binnary(process);
		}

		public Delete s17_header(Process<Delete, Map<String, String>> process) {
			return s_header(process);
		}

		public Delete s17_header2(Process<Delete, Map<String, List<String>>> process) {
			return s_header2(process);
		}
	}

	/**
	 * create http head.
	 * @author zhaochen
	 */
	public class Head extends Request<Head> {

	}

	/**
	 * create http options.
	 * @author zhaochen
	 */
	public class Options extends Request<Options> {

	}

	/**
	 * create http Trace.
	 * @author zhaochen
	 */
	public class Trace extends Request<Trace> {

	}

	/**
	 * userAgent(request.getHeader("User-Agent"))
	 * @param userAgent
	 * @return {os:"windows|mac|unix|android|iphone|unknown", browser:"IE-?|Safari-?|Opera-?|Chrome-?|Netscape-?|Firefox-?"}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-3-9
	 */
	public static Map<String, Object> userAgent(String userAgent) {
		Map<String, Object> map = new HashMap<String, Object>();
		String user = userAgent.toLowerCase();
		String os = "";
		String browser = "";
		{//=================OS=======================
			if (user.indexOf("windows") >= 0) {
				os = "windows";
			} else if (user.indexOf("mac") >= 0) {
				os = "mac";
			} else if (user.indexOf("x11") >= 0) {
				os = "unix";
			} else if (user.indexOf("android") >= 0) {
				os = "android";
			} else if (user.indexOf("iphone") >= 0) {
				os = "iphone";
			} else {
				os = "unknown, more-info: " + userAgent;
			}
		}
		{//===============Browser===========================
			if (user.contains("msie")) {
				try {
					String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
					browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
				} catch (Exception e) {
					browser = "IE-?";
				}
			} else if (user.contains("safari") && user.contains("version")) {
				try {
					browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
							+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
				} catch (Exception e) {
					browser = "Safari-?";
				}
			} else if (user.contains("opr") || user.contains("opera")) {
				try {
					if (user.contains("opera")) {
						browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
								+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
					} else if (user.contains("opr")) {
						browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
								.replace("OPR", "Opera");
					}
				} catch (Exception e) {
					browser = "Opera-?";
				}
			} else if (user.contains("chrome")) {
				try {
					browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
				} catch (Exception e) {
					browser = "Chrome-?";
				}
			} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
					|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
					|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
				//browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
				browser = "Netscape-?";
			} else if (user.contains("firefox")) {
				try {
					browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
				} catch (Exception e) {
					browser = "Firefox-?";
				}
			} else if (user.contains("rv")) {
				browser = "IE";
			} else {
				browser = "UnKnown, More-Info: " + userAgent;
			}
		}
		{
			map.put("os", os);
			MapHelper.fillMap(map, "isWindows", "windows".equals(os), "isMac", "mac".equals(os), "isUnix",
					"unix".equals(os), "isAndroid", "android".equals(os), "isIphone", "iphone".equals(os));
			map.put("browser", browser);
			MapHelper.fillMap(map, "isIE", browser.startsWith("IE-"), "isSafari", browser.startsWith("Safari-"),
					"isOpera", browser.startsWith("Opera-"), "isChrome", browser.startsWith("Chrome-"), "isNetscape",
					browser.startsWith("Netscape-"), "isFirefox", browser.startsWith("Firefox-"));
		}
		return map;
	}

	/**
	 * encode url.
	 * @param url
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 21, 2016
	 */
	private static String urlEncode(String url) {
		if (url != null && url.length() > 0) {
			try {
				return URLEncoder.encode(url, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * decode url.
	 * @param url
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 21, 2016
	 */
	private static String urlDecode(String url) {
		if (url != null && url.length() > 0) {
			try {
				return URLDecoder.decode(url, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}