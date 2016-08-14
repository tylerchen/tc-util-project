/*******************************************************************************
 * Copyright (c) 2014-6-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * a helper for http.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-6-28
 */
public class HttpHelper {

	/**
	 * return all address.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Map<String, InetAddress> getAddress() {
		Map<String, InetAddress> map = new HashMap<String, InetAddress>();
		try {
			for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
					.hasMoreElements();) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
					continue;
				}
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				if (addresses.hasMoreElements()) {
					InetAddress inetAddress = addresses.nextElement();
					map.put(inetAddress.getHostAddress(), inetAddress);
				}
			}
		} catch (SocketException e) {
			System.out.println("Error when getting host ip address: <{}>." + e.getMessage());
		}
		return map;
	}

	/**
	 * hash ip address by md5.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String ipsMd5() {
		StringBuilder sb = new StringBuilder(256);
		Map<String, InetAddress> address = getAddress();
		for (Entry<String, InetAddress> entry : address.entrySet()) {
			String md5 = md5(entry.getKey());
			md5 = StringHelper.reverse(md5);
			md5 = md5(md5);
			sb.append(md5);
		}
		return sb.toString();
	}

	/**
	 * hash ip by md5.
	 * @param ipAddress
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String ipMd5(String ipAddress) {
		StringBuilder sb = new StringBuilder(64);
		String md5 = md5(ipAddress);
		md5 = StringHelper.reverse(md5);
		md5 = md5(md5);
		sb.append(md5);
		return sb.toString();
	}

	/**
	 * validate ip match md5 string.
	 * @param ip
	 * @param ipsMd5
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static boolean validateIpMd5(String ip, String ipsMd5) {
		return ip != null && ipsMd5 != null && ipsMd5.indexOf(ipMd5(ip)) > -1;
	}

	/**
	 * encrypt string to md5.
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			Logger.error("[MessageDigest.getInstance(\"MD5\")]", e);
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append('0');
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * put url and return content.
	 * @param request
	 * @param data
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String put(String request, String data) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		DataOutputStream wr = null;
		try {
			URL url = new URL(request);
			connection = (HttpURLConnection) url.openConnection();
			String content = data;
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Content-Length", "" + content.length());
			connection.setUseCaches(false);
			connection.setConnectTimeout(30 * 1000);
			connection.setReadTimeout(60 * 1000);
			{
				wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(content);
				wr.flush();
				wr.close();
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder(2048);
			for (String line; (line = reader.readLine()) != null;) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			Exceptions.runtime("Request Error: " + request, e);
		} finally {
			StreamHelper.closeWithoutError(reader);
			StreamHelper.closeWithoutError(wr);
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * post url and return content.
	 * @param request
	 * @param data
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String post(String request, String data) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		DataOutputStream wr = null;
		try {
			URL url = new URL(request);
			connection = (HttpURLConnection) url.openConnection();
			String content = data;
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Content-Length", "" + content.getBytes("UTF-8").length);
			connection.setUseCaches(false);
			connection.setConnectTimeout(30 * 1000);
			connection.setReadTimeout(60 * 1000);
			{
				wr = new DataOutputStream(connection.getOutputStream());
				wr.write(content.getBytes("UTF-8"));
				wr.flush();
				wr.close();
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder(2048);
			for (String line; (line = reader.readLine()) != null;) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			Exceptions.runtime("Request Error: " + request, e);
		} finally {
			StreamHelper.closeWithoutError(reader);
			StreamHelper.closeWithoutError(wr);
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * get url content.
	 * @param requestUrl
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String get(String requestUrl) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			{
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("charset", "UTF-8");
				connection.setRequestProperty("accept", "*/*");
				connection.setConnectTimeout(30 * 1000);
				connection.setReadTimeout(60 * 1000);
				connection.connect();
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder(2048);
			for (String line; (line = reader.readLine()) != null;) {
				sb.append(line).append("\n");
			}
			{
				Map<String, List<String>> fields = connection.getHeaderFields();
				for (Entry<String, List<String>> entry : fields.entrySet()) {
					System.out.println(entry.getKey() + "=" + Arrays.toString(entry.getValue().toArray()));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			Exceptions.runtime("Request Error: " + requestUrl, e);
		} finally {
			StreamHelper.closeWithoutError(reader);
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * get url content.
	 * @param requestUrl
	 * @param paramString
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String get(String requestUrl, String paramString) {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			{
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0");
				connection.setRequestProperty("charset", "UTF-8");
				connection.setRequestProperty("accept", "*/*");
				connection.setConnectTimeout(30 * 1000);
				connection.setReadTimeout(60 * 1000);
				connection.connect();
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder(2048);
			for (String line; (line = reader.readLine()) != null;) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			Exceptions.runtime("Request Error: " + requestUrl, e);
		} finally {
			StreamHelper.closeWithoutError(reader);
			try {
				connection.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * get ip address, get proxy id first.
	 * @param request
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * get direct or remote access ip address.
	 * @param request
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String getRemoteIpAddr(HttpServletRequest request) {
		String mark = request.getHeader("proxy-enable");
		if (mark == "1") {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * get Authorization header.
	 * @param request
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String getAuthorization(HttpServletRequest request) {
		return request.getHeader("Authorization");
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

}