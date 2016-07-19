/*******************************************************************************
 * Copyright (c) Jul 18, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 18, 2016
 */
public class GuessDriverClass {

	public static String guessByUrl(String url) {
		/*jdbc:mysql://localhost:3306/new_qdp?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull*/
		if (StringUtils.isBlank(url)) {
			return "com.mysql.jdbc.Driver";
		}
		if (url.startsWith("jdbc:mysql:")) {
			return "com.mysql.jdbc.Driver";
		}
		if (url.startsWith("jdbc:oracle:")) {
			return "oracle.jdbc.driver.OracleDriver";
		}
		if (url.startsWith("jdbc:sqlserver:")) {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		if (url.startsWith("jdbc:h2:")) {
			return "org.h2.jdbcx.JdbcDataSource";
		}
		if (url.startsWith("jdbc:db2:")) {
			return "com.ibm.db2.jcc.DB2Driver";
		}
		if (url.startsWith("jdbc:postgresql:")) {
			return "org.postgresql.Driver";
		}
		return "com.mysql.jdbc.Driver";
	}
}
