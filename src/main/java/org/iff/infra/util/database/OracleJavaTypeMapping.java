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
public class OracleJavaTypeMapping implements JavaTypeMapping {

	public String getType() {
		return "oracle";
	}

	public String get(String dbType, Integer length, Integer scale) {
		String key = dbType.toUpperCase();
		length = NumberHelper.getInt(length, 0);
		scale = NumberHelper.getInt(scale, -1);
		if ("RAW".equals(key) || "LONG RAW".equals(key)) {
			return "byte[]";
		} else if ("BOOLEAN".equals(key)) {
			return "Boolean";
		} else if ("SMALLINT".equals(key) || "INTEGER".equals(key) || "INT".equals(key) || "BINARY_INTEGER".equals(key)
				|| "NATURAL".equals(key) || "NATURALN".equals(key) || "PLS_INTEGER".equals(key)
				|| "POSITIVE".equals(key) || "POSITIVEN".equals(key) || "SIGNTYPE".equals(key)) {
			return "Integer";
		} else if ("DOUBLE".equals(key) || "FLOAT".equals(key)) {
			return "Double";
		} else if ("REAL".equals(key)) {
			return "Float";
		} else if ("NUMERIC".equals(key) || "NUMBER".equals(key) || "DECIMAL".equals(key) || "DEC".equals(key)) {
			return BigDecimal.class.getName();
		} else if ("DATE".equals(key) || "TIMESTAMP".equals(key) || "TIMESTAMP WITH TZ".equals(key)
				|| "TIMESTAMP WITH LOCAL TZ".equals(key)) {
			return java.util.Date.class.getName();
		} else if ("CHAR".equals(key) || "CHARACTER".equals(key) || "LONG".equals(key) || "STRING".equals(key)
				|| "VARCHAR".equals(key) || "VARCHAR2".equals(key)) {
			return "String";
		}
		return "String";
	}

	public String getDbType(String javaType) {
		if ("[B".equals(javaType)) {
			return "byte[]";
		} else if (byte.class.getName().equals(javaType) || Byte.class.getName().equals(javaType)
				|| "Byte".equals(javaType)) {
			return "BIT";
		} else if (boolean.class.getName().equals(javaType) || Boolean.class.getName().equals(javaType)
				|| "Boolean".equals(javaType)) {
			return "BIT";
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
			return "DOUBLE";
		} else if (float.class.getName().equals(javaType) || Float.class.getName().equals(javaType)
				|| "Float".equals(javaType)) {
			return "REAL";
		} else if (java.math.BigDecimal.class.getName().equals(javaType)) {
			return "NUMERIC";
		} else if (java.util.Date.class.getName().equals(javaType)) {
			return "DATE";
		} else {
			return "VARCHAR2";
		}
	}

}
