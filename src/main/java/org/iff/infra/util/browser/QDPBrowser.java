/*******************************************************************************
 * Copyright (c) Feb 3, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.browser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.ZipHelper;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 3, 2016
 */
public class QDPBrowser implements Browser.ActionListener {

	private static final String DEFAULT_URL = "http://www.tc.com";

	private static final GroovyClassLoader gcl = new GroovyClassLoader();

	public static void main(String[] args) {
		try {
			QDPBrowser qdp = new QDPBrowser();
			System.setProperty("qdp.client.url", "file:///Users/zhaochen/dev/workspace/cocoa/foss-qdp-project-v3/src/main/webapp/WEB-INF/project_base_framework/client.zip");
			InputStream is = qdp.getRemoteStream(qdp.getRemoteUrl());
			Map<String, byte[]> loadZip = qdp.loadZip(is);
			Class<?> parseClass = gcl.parseClass(new String(loadZip.get("groovy/QDPClient.groovy"), "UTF-8"));
			GroovyObject instance = (GroovyObject) parseClass.newInstance();
			instance.invokeMethod("init",
					MapHelper.toMap("resource", loadZip, "defaultUrl", DEFAULT_URL, "classLoader", gcl));
		} catch (Exception e) {
			Exceptions.runtime("start QDPBrowser error!", e);
		}
	}

	public Object doAction(String url, String parameterString) {
		if (DEFAULT_URL.equalsIgnoreCase(url)) {
			try {
				return new URL("http://layer.layui.com");
			} catch (Exception e) {
			}
		}
		return null;
	}

	public String getContextPath(String url) {
		if (StringUtils.startsWithIgnoreCase(url, DEFAULT_URL)) {
			return StringUtils.prependIfMissing(url.substring(DEFAULT_URL.length()), "/");
		}
		return url;
	}

	public String getRemoteUrl() {
		return System.getProperty("qdp.client.url", "http://182.254.204.165:8080/client/download/client.zip");
	}

	public InputStream getRemoteStream(String url) {
		try {
			byte[] bs = SocketHelper.getByte(new URL(url).openStream(), false);
			return new ByteArrayInputStream(bs);
		} catch (Exception e) {
			Exceptions.runtime(FCS.get("Error getRemoteStream from URL: {0} !", url), e);
		}
		return null;
	}

	public Map<String, byte[]> loadZip(InputStream zipStream) {
		return ZipHelper.loadZip(zipStream);
	}
}
