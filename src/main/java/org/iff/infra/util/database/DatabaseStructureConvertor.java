/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.InputStream;

/**
 * database structure convertor, convert the database structure from a database type to other.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
public interface DatabaseStructureConvertor {

	/**
	 * the type such as mysql2oracle or mysql2h2
	 * @return mysql2oracle or mysql2h2 etc.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Apr 21, 2016
	 */
	String getType();

	String parse(InputStream is);

	String convertTable(Table table);

	String convertColumn(Table table, Field field);

	String convertPK(Table table, Field field);

	String convertFK(Table table, Field field);

	String convertUnique(Table table, Field field);

	String convertIndex(Table table, Field field);

	String convertComment(Table table, Field field);

	String convertDefaultValue(Table table, Field field);
}
