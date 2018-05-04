/*******************************************************************************
 * Copyright (c) Jul 12, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.test.tester;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;

/**
 * H2 datasource support. 
 * @author zhaochen
 */
public class H2DataSourceDatabaseTester extends DataSourceDatabaseTester {

	public H2DataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {

		IDatabaseConnection conn = super.getConnection();

		DefaultDataTypeFactory datatypeFactory = new HsqldbDataTypeFactory();
		conn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, datatypeFactory);
		return conn;
	}

}
