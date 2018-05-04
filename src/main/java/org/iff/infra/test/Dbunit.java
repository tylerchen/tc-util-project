/*******************************************************************************
 * Copyright (c) Jul 12, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.iff.infra.test.tester.H2DataSourceDatabaseTester;
import org.iff.infra.test.tester.MySQLDataSourceDatabaseTester;
import org.iff.infra.test.tester.OracleDataSourceDatabaseTester;
import org.iff.infra.util.Logger;
import org.junit.After;
import org.junit.Before;
import org.xml.sax.InputSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * DBUnit
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 12, 2016
 */
public abstract class Dbunit {

	/**
	 * support FlatXml, and Xml
	 * @author zhaochen
	 */
	public static enum DataSetStrategy {
		FlatXml, Xml;
	}

	protected IDatabaseTester databaseTester;

	/**
	 * default to load : classpath*:META-INF/spring/root-test.xml
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public abstract String[] springXmlPath();

	/**
	 * default to load : jdbc.properties
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public abstract String testPropertiesFile();

	public abstract void init();

	public abstract DataSource getDataSource();

	public Dbunit() {
		init();
		if (getDataSource() != null) {
			try {
				DataSource ds = getDataSource();
				databaseTester = new DataSourceDatabaseTester(ds);
			} catch (Exception ex) {
				Logger.error("构造测试环境失败！", ex);
			}
		} else {
			Properties properties = loadJdbcPropertiesFile(testPropertiesFile());
			String jdbc_url = properties.getProperty("jdbc.url");
			String jdbc_driver = properties.getProperty("jdbc.driverClassName");
			String jdbc_user = properties.getProperty("jdbc.username");
			String jdbc_password = properties.getProperty("jdbc.password");
			try {
				ComboPooledDataSource ds = new ComboPooledDataSource();
				ds.setDriverClass(jdbc_driver);
				ds.setUser(jdbc_user);
				ds.setPassword(jdbc_password);
				ds.setJdbcUrl(jdbc_url);

				if (jdbc_driver.toLowerCase().contains("mysql")) {
					Logger.info("use mysql datasource tester.");
					jdbc_url = jdbc_url + "&sessionVariables=FOREIGN_KEY_CHECKS=0";
					ds.setJdbcUrl(jdbc_url);
					databaseTester = new MySQLDataSourceDatabaseTester(ds);
				} else if (jdbc_driver.toLowerCase().contains("oracle")) {
					Logger.info("use oracle datasource tester.");
					databaseTester = new OracleDataSourceDatabaseTester(ds, jdbc_user);
				} else if (jdbc_driver.toLowerCase().contains("h2")) {
					Logger.info("use h2 datasource tester.");
					databaseTester = new H2DataSourceDatabaseTester(ds);
				} else {
					//throw new RuntimeException("不支持的数据库类型！");
					databaseTester = new DataSourceDatabaseTester(ds);
				}
			} catch (Exception ex) {
				Logger.error("构造测试环境失败！", ex);
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		if (isDataSetOK()) {
			// initialize your dataset here

			String[] dataSetPaths = getDataSetFilePaths();

			IDataSet[] dataSet = new IDataSet[dataSetPaths.length];

			for (int i = 0; i < dataSetPaths.length; i++) {
				String path = dataSetPaths[i].startsWith("/") ? dataSetPaths[i] : "/" + dataSetPaths[i];
				// dataSet[i] = new FlatXmlDataSet(DbUnit.class
				// .getResourceAsStream(path));
				dataSet[i] = getDataSetObject(path);
				Logger.debug("载入数据库资源文件：" + path);
			}
			CompositeDataSet compositeDateSet = new CompositeDataSet((IDataSet[]) dataSet);
			databaseTester.setDataSet(compositeDateSet);
			// will call default setUpOperation
			databaseTester.setSetUpOperation(setUpOp());
			databaseTester.onSetup();
		} else {
			Logger.warn("没有指定数据集！");
		}

	}

	private IDataSet getDataSetObject(String path) throws Exception {
		if (getDataSetStrategy().equals(DataSetStrategy.Xml)) {
			return new XmlDataSet(Dbunit.class.getResourceAsStream(path));
		} else if (getDataSetStrategy().equals(DataSetStrategy.FlatXml)) {
			boolean enableColumnSensing = true;
			InputStreamReader inReader = new InputStreamReader(Dbunit.class.getResourceAsStream(path), "UTF-8");
			return new CachedDataSet(new FlatXmlProducer(new InputSource(inReader), true, enableColumnSensing, false));
		} else {
			return new XmlDataSet(Dbunit.class.getResourceAsStream(path));
		}
	}

	@After
	public void tearDown() throws Exception {
		if (isDataSetOK()) {
			databaseTester.setTearDownOperation(tearDownOp());
			databaseTester.onTearDown();
		}
	}

	private boolean isDataSetOK() {
		if (getDataSetFilePaths().length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 解析dbunit数据集的格式
	 * 
	 * @return 数据集格式 
	 */
	protected DataSetStrategy getDataSetStrategy() {
		return DataSetStrategy.Xml;
	}

	/**
	 * 数据集路径数组
	 * 
	 * @return 数据集路径数组
	 */
	protected String[] getDataSetFilePaths() {
		return new String[] {};
	}

	/**
	 * 单元测试方法执行前，针对数据集的操作
	 * 
	 * @return 数据集在单元测试方法执行前的操作
	 */
	protected DatabaseOperation setUpOp() {
		return DatabaseOperation.REFRESH;
	}

	/**
	 * 单元测试方法执行后，针对数据集的操作
	 * 
	 * @return 数据集在单元测试方法执行后的操作
	 */
	protected DatabaseOperation tearDownOp() {
		return DatabaseOperation.NONE;
	}

	Properties loadJdbcPropertiesFile(String fileName) {
		Properties config = new Properties();
		try {
			URL url = getClass().getResource(fileName);
			InputStream inputStream = url.openStream();
			config.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
}
