/*******************************************************************************
 * Copyright (c) Jul 12, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.test;

import javax.sql.DataSource;

import org.dbunit.operation.DatabaseOperation;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.util.spring.factory.SpringInstanceProvider;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 12, 2016
 */
public abstract class AbstractIntegratedTestCase extends Dbunit {

	private static Boolean hasInit = false;

	public void init() {
		if (!hasInit) {
			InstanceFactory.setInstanceProvider(new SpringInstanceProvider(springXmlPath()));
			hasInit = true;
		}
	}

	public DataSource getDataSource() {
		return InstanceFactory.getInstance(DataSource.class);
	}

	/**
	 * default to load : classpath*:META-INF/spring/root-test.xml
	 * (non-Javadoc)
	 * @see org.iff.infra.test.Dbunit#springXmlPath()
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public String[] springXmlPath() {
		return new String[] { "classpath*:META-INF/spring/root-test.xml" };
	}

	/**
	 * default to load : jdbc.properties
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public String testPropertiesFile() {
		return "jdbc.properties";
	}

	@Override
	public final void setUp() throws Exception {
		super.setUp();
		action4SetUp();
	}

	@Override
	public final void tearDown() throws Exception {
		super.tearDown();
		action4TearDown();
	}

	/**
	 * 设置单元测试方法是否回滚，true=回滚
	 * 
	 * @return 单元测试方法是否回滚
	 */
	protected boolean rollback() {
		return false;
	}

	/**
	 * 单元测试方法执行前的操作
	 */
	protected void action4SetUp() {
	}

	/**
	 * 单元测试方法执行后的操作
	 */
	protected void action4TearDown() {
	}

	@Override
	protected DataSetStrategy getDataSetStrategy() {
		return DataSetStrategy.FlatXml;
	}

	@Override
	protected DatabaseOperation setUpOp() {
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation tearDownOp() {
		return DatabaseOperation.NONE;
	}

}
