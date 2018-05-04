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
import org.dbunit.ext.oracle.OracleDataTypeFactory;

/**
 * oracle datasource support.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 12, 2016
 */
public class OracleDataSourceDatabaseTester extends DataSourceDatabaseTester {

	public OracleDataSourceDatabaseTester(DataSource dataSource) {
		super(dataSource);
	}

	public OracleDataSourceDatabaseTester(DataSource dataSource, String schema) {
		super(dataSource, schema);
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {

		IDatabaseConnection conn = super.getConnection();

		DefaultDataTypeFactory datatypeFactory = new OracleDataTypeFactory();
		conn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, datatypeFactory);
		return conn;
	}

}
