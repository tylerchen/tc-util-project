/*******************************************************************************
 * Copyright (c) Dec 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 2, 2015
 */
public interface JavaTypeMapping {

	String getType();

	String get(String dbType, Integer length, Integer scale);

	String getDbType(String javaType);
}
