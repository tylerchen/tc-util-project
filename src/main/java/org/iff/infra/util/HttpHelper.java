/*******************************************************************************
 * Copyright (c) 2014-6-28 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-6-28
 */
public class HttpHelper {
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

	public static String ipMd5(String ipAddress) {
		StringBuilder sb = new StringBuilder(64);
		String md5 = md5(ipAddress);
		md5 = StringHelper.reverse(md5);
		md5 = md5(md5);
		sb.append(md5);
		return sb.toString();
	}

	public static boolean validateIpMd5(String ip, String ipsMd5) {
		return ip != null && ipsMd5 != null && ipsMd5.indexOf(ipMd5(ip)) > -1;
	}

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

	public static String put(String request, String data) {
		BufferedReader reader = null;
		try {
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String content = "{\"user\": \"kimchy\", \"postDate\": \"2009-11-15T14:12:12\", \"message\": \"Another tweet, will it be indexed?\" }";
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + content.length());
			connection.setUseCaches(false);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(content);
			wr.flush();
			wr.close();
			connection.disconnect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			for (String line; (line = reader.readLine()) != null;) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException ignore) {
				}
		}
		return null;
	}

	public static String get(String request, String data) {
		BufferedReader reader = null;
		try {
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "text/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			for (String line; (line = reader.readLine()) != null;) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException ignore) {
				}
		}
		return null;
	}

	/**
	 * userAgent(request.getHeader("User-Agent"))
	 * @param userAgent
	 * @return {os:"windows|mac|unix|android|iphone|unknown", browser:"IE-?|Safari-?|Opera-?|Chrome-?|Netscape-?|Firefox-?"}
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
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
			MapHelper.fillMap(map, "isWindows", "windows".equals(os), "isMac", "mac".equals(os), "isUnix", "unix"
					.equals(os), "isAndroid", "android".equals(os), "isIphone", "iphone".equals(os));
			map.put("browser", browser);
			MapHelper.fillMap(map, "isIE", browser.startsWith("IE-"), "isSafari",
					browser.startsWith("Safari-"), "isOpera", browser.startsWith("Opera-"), "isChrome", browser
							.startsWith("Chrome-"), "isNetscape", browser.startsWith("Netscape-"), "isFirefox", browser
							.startsWith("Firefox-"));
		}
		return map;
	}

	public static void main(String[] args) {
		put("http://localhost:9200/twitter/tweet/1", null);
		get("http://localhost:9200/twitter/tweet/1", null);
	}
}
