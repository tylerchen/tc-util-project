/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring.remoting.httpinvoker;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 关闭连接池的无效链接
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2017-04-09
 */
public class HttpIdleConnectionEvictor extends Thread {

	private static final org.iff.infra.util.Logger.Log Logger = org.iff.infra.util.Logger.get("HttpInvoker");

	private final HttpClientConnectionManager connMgr;

	private volatile boolean shutdown;

	public HttpIdleConnectionEvictor(HttpClientConnectionManager connMgr) {
		this.connMgr = connMgr;
		this.start();// 启动线程  
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					// 每隔5秒执行一个，关闭失效的http连接  
					wait(5000);
					Logger.debug("HttpInvoker HttpClientConnectionManager closeExpiredConnections.");
					// 关闭失效的连接  
					connMgr.closeExpiredConnections();
				}
			}
		} catch (InterruptedException ex) {
			// 结束  
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

}