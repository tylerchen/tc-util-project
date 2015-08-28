/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class TCServer {
	private Server server;
	private String appMode = "standarlone";//embedded, standarlone
	private int port = 8080;
	private String contenxt = "/";

	public static TCServer create(int port, String context, String appMode) {
		TCServer tcServer = new TCServer();
		{
			tcServer.setPort(port);
			tcServer.setContenxt(context);
			tcServer.setAppMode(appMode);
		}
		return tcServer;
	}

	public static TCServer create(int port, String context) {
		TCServer tcServer = new TCServer();
		{
			tcServer.setPort(port);
			tcServer.setContenxt(context);
		}
		return tcServer;
	}

	public TCServer start() {
		new Thread() {
			public void run() {
				try {
					server = new Server(port);
					ServerConnector connector = new ServerConnector(server);
					WebAppContext webApp = new WebAppContext();
					webApp.setContextPath(getContenxt());
					webApp.addFilter("org.iff.infra.util.moduler.TCFilter", "/*", null);
					webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
					webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
					// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
					webApp.setResourceBase(String.valueOf(port));
					//webApp.classLoader=TCGetter.get_class_loader()
					//webApp.setInitParameter("container_name", container_name)
					server.setHandler(webApp);
					Logger.debug(FCS.get("[TCServer] Starting web server on port: {0}", port));
					server.start();
					Logger.debug("[TCServer] Starting Complete. Welcome To The TC World :-)");
					server.join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return this;
	}

	public TCServer stop() {
		server.destroy();
		return this;
	}

	public String getAppMode() {
		return appMode;
	}

	public void setAppMode(String appMode) {
		this.appMode = appMode;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContenxt() {
		return contenxt;
	}

	public void setContenxt(String contenxt) {
		this.contenxt = contenxt;
	}

}
