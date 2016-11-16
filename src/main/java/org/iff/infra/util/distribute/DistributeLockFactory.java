/*******************************************************************************
 * Copyright (c) Sep 8, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.distribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.StringHelper;

/**
 * <pre>
 * Offer distribute lock zookeeper.
 * Log name:DIST_LOCK.
 * 	&lt;logger name="DIST_LOCK"&gt;&lt;level value="debug" /&gt;&lt;/logger&gt;
 * How to use:
 * new DistributeLockFactory()
 * 	.initDefault("127.0.0.1:2181", "TEST")
 * 		.executeWaitLock(new Runnable() {
 * 			public void run() {
 * 				System.out.println("Thread ID:" + Thread.currentThread().getName());
 * 			}
 * 		}, true);
 * 
 * or you can use: executeWhenLock.
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 8, 2016
 */
public class DistributeLockFactory {
	/*the default lock name*/
	private static final String NAME_DEFAULT = "DEFAULT";
	/*the default lock path*/
	public static final String BASE_PATH = "/foss/dislock";
	/*cache the zk client, you can reuse.*/
	public static final Map<String, DistributeLockFactory> ZK_MAP = new HashMap<String, DistributeLockFactory>();
	private static final Logger.Log LOG = Logger.get("DIST_LOCK");

	/*the name of the lock client*/
	private String name;
	/*the zookeeper server ips*/
	private String zkServers;
	/*the lock type, this value for different lock. the same type lock client share the same lock.*/
	private String type;
	/*zookeeper client*/
	private CuratorFramework zkClient;

	public static void main(final String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				public void run() {
					new DistributeLockFactory().initDefault(args != null && args.length > 1
							? StringUtils.defaultString(args[1], "121.40.74.22:2181") : "121.40.74.22:2181", "TEST")
							.executeWaitLock(new Runnable() {
						public void run() {
							try {
								System.out.println("Thread ID:" + Thread.currentThread().getName());
								TimeUnit.MILLISECONDS.sleep(100 + new Random().nextInt(1000));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, true);
				}
			}).start();
		}
		try {
			TimeUnit.MINUTES.sleep(5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * create DistributeLockFactory.
	 * @param name the lock client name.
	 * @param ips the zookeeeper server ips.
	 * @param type the lock type.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public static DistributeLockFactory create(String name, String ips, String type) {
		return new DistributeLockFactory().init(name, ips, type);
	}

	/**
	 * return the default lock, if you are using one zookeeper and one type, use default.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public static DistributeLockFactory getDefault() {
		return get(NAME_DEFAULT);
	}

	/**
	 * return the lock client by name.
	 * @param name the lock client name.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public static DistributeLockFactory get(String name) {
		return ZK_MAP.get(name);
	}

	/**
	 * release default connection and remove from cache.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public static DistributeLockFactory removeDefault() {
		return remove(NAME_DEFAULT);
	}

	/**
	 * release connection and remove from cache.
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public static DistributeLockFactory remove(String name) {
		DistributeLockFactory remove = ZK_MAP.remove(name);
		if (remove != null) {
			remove.releaseConnection();
		}
		return remove;
	}

	/**
	 * init default lock client.
	 * @param ips
	 * @param type
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public DistributeLockFactory initDefault(String ips, String type) {
		init(NAME_DEFAULT, ips, type);
		return this;
	}

	/**
	 * init the DistributeLockFactory.
	 * @param name the name of lock client.
	 * @param ips  zookeeper server ips.
	 * @param type the type of lock client, this value for different lock. the same type lock client share the same lock.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public DistributeLockFactory init(String name, String ips, String type) {
		if (ZK_MAP.containsKey(name)) {
			Assert.error(FCS.get("Name {name} is exists!", name));
		}
		this.name = name;
		this.zkServers = ips;
		this.type = type;
		this.zkClient = CuratorFrameworkFactory.newClient(this.zkServers, 5000, 3000,
				new ExponentialBackoffRetry(1000, 3));
		this.zkClient.start();
		ZK_MAP.put(this.name, this);
		return this;
	}

	/**
	 * reconnect the zk client if need.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	private DistributeLockFactory reconnectIfNeed() {
		if (zkClient == null) {
			LOG.debug(FCS.get("Reconnect...{name}", name));
			zkClient = CuratorFrameworkFactory.newClient(this.zkServers, 5000, 3000,
					new ExponentialBackoffRetry(1000, 3));
			zkClient.start();
		}
		return this;
	}

	/**
	 * <pre>
	 * waiting the lock and execute the runnable.
	 * removeLock=true,  release the lock and connection.
	 * removeLock=false, keep the lock and reuse next time, like use-to-dead.
	 * </pre> 
	 * @param runnable
	 * @param removeLock true release the lock and release the connection, otherwise the lock exists and block the other lock client to get the lock.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public synchronized DistributeLockFactory executeWaitLock(Runnable runnable, boolean removeLock) {
		InterProcessMutex lock = null;
		try {
			reconnectIfNeed();
			lock = new InterProcessMutex(zkClient, getLockPath());
			LOG.debug(FCS.get("Waiting lock for executeWaitLock...{nodePath}", type));
			lock.acquire();
			LOG.debug(FCS.get("Waiting lock for executeWaitLock DONE...{nodePath}", type));
			runnable.run();
			return this;
		} catch (Exception e) {
			Exceptions.runtime(FCS.get("Waiting lock error!...{nodePath}", type), e);
		} finally {
			try {
				lock.release();
			} catch (Exception e) {
			}
			try {
				releaseConnection();
			} catch (Exception e) {
			}
		}
		return this;
	}

	/**
	 * get the lock path.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 16, 2016
	 */
	private String getLockPath() {
		return StringHelper.pathConcat(BASE_PATH, type);
	}

	/**
	 * close the lock client connection, and reset the properties.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	private DistributeLockFactory releaseConnection() {
		LOG.debug(FCS.get("Release connection...{nodePath}", type));
		zkClient.close();
		zkClient = null;
		return this;
	}

	/**
	 * make current lock client to default if no default client set.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2016
	 */
	public DistributeLockFactory setDefault() {
		if (ZK_MAP.containsKey(NAME_DEFAULT)) {
			Assert.error(FCS.get("Name {name} is exists!", NAME_DEFAULT));
		}
		ZK_MAP.put(NAME_DEFAULT, this);
		return this;
	}

	public String getZkServers() {
		return zkServers;
	}

	public String getType() {
		return type;
	}

	public CuratorFramework getZkClient() {
		return zkClient;
	}
}
