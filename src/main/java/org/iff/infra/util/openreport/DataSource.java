/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.BaseCryptHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RSAHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.StringHelper;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface DataSource {

	javax.sql.DataSource create(String name, String user, String password, String url, String driverClass,
			String testSql, String encryptMethod, String privateKeyBase64, int initConnection, int maxConnection,
			boolean force);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;
		private Map<String, javax.sql.DataSource> dataSourceCacheMap = new HashMap<String, javax.sql.DataSource>();

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("druid", FACTORY.new CacheProxyDataSource(new DruidDataSource()));
				FACTORY.regist("dbcp", FACTORY.new CacheProxyDataSource(new BasicDataSource()));
			}
			return FACTORY;
		}

		public DataSource get(String name) {
			if (registMap != null) {
				return (DataSource) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, DataSource returnType) {
			RegisterHelper.regist(DataSource.class.getSimpleName(), MapHelper.toMap("name", name, "value", returnType));
			registMap = RegisterHelper.get(DataSource.class.getSimpleName());
			return this;
		}

		public List<Map> listRegist() {
			List<Map> list = new ArrayList<Map>();
			for (Entry<String, Object> entry : registMap.entrySet()) {
				list.add(MapHelper.toMap("name", entry.getKey(), "value", entry.getValue()));
			}
			return list;
		}

		class CacheProxyDataSource implements DataSource {
			private DataSource datasource;

			public CacheProxyDataSource(DataSource datasource) {
				this.datasource = datasource;
			}

			public javax.sql.DataSource create(String name, String user, String password, String url,
					String driverClass, String testSql, String encryptMethod, String privateKeyBase64,
					int initConnection, int maxConnection, boolean force) {
				String key = StringHelper.concat(user, "|", url);
				javax.sql.DataSource ds = dataSourceCacheMap.get(key);
				if (ds == null || force) {
					if (ds != null) {
						try {
							String script = "ds.close()";
							Binding binding = new Binding();
							{
								binding.setVariable("ds", ds);
							}
							GroovyShell shell = new GroovyShell(binding);
							shell.evaluate(script);
						} catch (Exception e) {
						}
					}
					ds = datasource.create(name, user, password, url, driverClass, testSql, encryptMethod,
							privateKeyBase64, initConnection, maxConnection, force);
					dataSourceCacheMap.put(key, ds);
				}
				return ds;
			}

		}
	}

	public static class DruidDataSource implements DataSource {
		public javax.sql.DataSource create(String name, String user, String password, String url, String driverClass,
				String testSql, String encryptMethod, String privateKeyBase64, int initConnection, int maxConnection,
				boolean force) {
			String script = "com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(props)";
			Binding binding = new Binding();
			{
				if ("base64".equalsIgnoreCase(encryptMethod)) {
					password = new String(BaseCryptHelper.decodeBase64(password));
				} else if ("rsa".equalsIgnoreCase(encryptMethod) && StringUtils.isNotBlank(privateKeyBase64)) {
					password = RSAHelper.decrypt(password, RSAHelper.getPrivateKeyFromBase64(privateKeyBase64));
				}
				Map props = MapHelper.toMap(/**/
						"username", user, /**/
						"password", password, /**/
						"url", url, /**/
						"driverClassName",
						StringUtils.defaultIfBlank(driverClass, GuessDriverClass.guessByUrl(url)), /**/
						"filters", "stat", /**/
						"initialSize", String.valueOf(Math.max(initConnection, 3)), /**/
						"minIdle", "1", /**/
						"maxActive", String.valueOf(Math.max(maxConnection, 3)), /**/
						"maxWait", "60000", /**/
						"timeBetweenEvictionRunsMillis", "60000", /**/
						"minEvictableIdleTimeMillis", "300000", /**/
						"validationQuery",
						StringUtils.defaultIfBlank(testSql, GuessValidationQuery.guessByUrl(url)), /**/
						"testWhileIdle", "true", /**/
						"testOnBorrow", "false", /**/
						"testOnReturn", "false", /**/
						"poolPreparedStatements", "true", /**/
						"maxPoolPreparedStatementPerConnectionSize", "20", /**/
						"timeBetweenLogStatsMillis", "300000");
				binding.setVariable("props", props);
			}
			GroovyShell shell = new GroovyShell(binding);
			return (javax.sql.DataSource) shell.evaluate(script);
		}
	}

	public static class BasicDataSource implements DataSource {
		public javax.sql.DataSource create(String name, String user, String password, String url, String driverClass,
				String testSql, String encryptMethod, String privateKeyBase64, int initConnection, int maxConnection,
				boolean force) {
			String script = "org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props)";
			Binding binding = new Binding();
			{
				if ("base64".equalsIgnoreCase(encryptMethod)) {
					password = new String(BaseCryptHelper.decodeBase64(password));
				} else if ("rsa".equalsIgnoreCase(encryptMethod) && StringUtils.isNotBlank(privateKeyBase64)) {
					password = RSAHelper.decrypt(password, RSAHelper.getPrivateKeyFromBase64(privateKeyBase64));
				}
				Map map = MapHelper.toMap(/**/
						"username", user, /**/
						"password", password, /**/
						"url", url, /**/
						"driverClassName",
						StringUtils.defaultIfBlank(driverClass, GuessDriverClass.guessByUrl(url)), /**/
						"defaultAutoCommit", "false", /**/
						"initialSize", String.valueOf(Math.max(initConnection, 3)), /**/
						"maxActive", String.valueOf(Math.max(maxConnection, 3)), /**/
						"maxWait", "60000", /**/
						"validationQuery",
						StringUtils.defaultIfBlank(testSql, GuessValidationQuery.guessByUrl(url)), /**/
						"testWhileIdle", "true", /**/
						"testOnBorrow", "false", /**/
						"testOnReturn", "false" /**/
				);
				Properties props = new Properties();
				props.putAll(map);
				binding.setVariable("props", props);
			}
			GroovyShell shell = new GroovyShell(binding);
			return (javax.sql.DataSource) shell.evaluate(script);
		}
	}

}
