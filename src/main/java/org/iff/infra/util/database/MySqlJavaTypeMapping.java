/*******************************************************************************
 * Copyright (c) Dec 2, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.math.BigDecimal;

import org.iff.infra.util.NumberHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 2, 2015
 */
public class MySqlJavaTypeMapping implements JavaTypeMapping {

	public String getType() {
		return "mysql";
	}

	public String get(String dbType, Integer length, Integer scale) {
		String key = dbType.toUpperCase();
		length = NumberHelper.getInt(length, 0);
		scale = NumberHelper.getInt(scale, -1);
		if ("BIT".equals(key)) {
			if (length > 1) {
				return "byte[]";
			} else {
				return "byte";
			}
		} else if ("TINYINT".equals(key) || "BOOL".equals(key) || "BOOLEAN".equals(key)) {
			return "Boolean";
		} else if ("SMALLINT".equals(key) || "MEDIUMINT".equals(key) || "INTEGER".equals(key) || "INT".equals(key)) {
			return "Integer";
		} else if ("BIGINT".equals(key)) {
			return "Long";
		} else if ("REAL".equals(key) || "DOUBLE".equals(key)) {
			return "Double";
		} else if ("FLOAT".equals(key)) {
			return "Float";
		} else if ("NUMERIC".equals(key) || "DECIMAL".equals(key)) {
			return BigDecimal.class.getName();
		} else if ("DATETIME".equals(key) || "DATE".equals(key) || "TIME".equals(key) || "TIMESTAMP".equals(key)) {
			return java.util.Date.class.getName();
		} else if ("YEAR".equals(key)) {
			return "Short";
		} else if ("CHAR".equals(key) || "VARCHAR".equals(key) || "TINYTEXT".equals(key) || "TEXT".equals(key)
				|| "MEDIUMTEXT".equals(key) || "LONGTEXT".equals(key) || "ENUM".equals(key) || "SET".equals(key)) {
			return "String";
		} else if ("BINARY".equals(key) || "VARBINARY".equals(key) || "TINYBLOB".equals(key) || "BLOB".equals(key)
				|| "MEDIUMBLOB".equals(key)) {
			return "String";
		}
		return "String";
	}

	public String getDbType(String javaType) {
		if (byte.class.getName().equals(javaType) || Byte.class.getName().equals(javaType) || "Byte".equals(javaType)) {
			return "BIT";
		} else if (boolean.class.getName().equals(javaType) || Boolean.class.getName().equals(javaType)
				|| "Boolean".equals(javaType)) {
			return "BOOLEAN";
		} else if (short.class.getName().equals(javaType) || Short.class.getName().equals(javaType)
				|| "Short".equals(javaType)) {
			return "SMALLINT";
		} else if (int.class.getName().equals(javaType) || Integer.class.getName().equals(javaType)
				|| "Integer".equals(javaType)) {
			return "INT";
		} else if (long.class.getName().equals(javaType) || Long.class.getName().equals(javaType)
				|| "Long".equals(javaType)) {
			return "BIGINT";
		} else if (double.class.getName().equals(javaType) || Double.class.getName().equals(javaType)
				|| "Double".equals(javaType)) {
			return "REAL";
		} else if (float.class.getName().equals(javaType) || Float.class.getName().equals(javaType)
				|| "Float".equals(javaType)) {
			return "FLOAT";
		} else if (java.math.BigDecimal.class.getName().equals(javaType)) {
			return "NUMERIC";
		} else if (java.util.Date.class.getName().equals(javaType)) {
			return "DATE";
		} else {
			return "VARCHAR2";
		}
	}

}
