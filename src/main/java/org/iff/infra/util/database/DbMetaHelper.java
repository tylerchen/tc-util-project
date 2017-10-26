/*******************************************************************************
 * Copyright (c) Jun 26, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.MapHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 26, 2017
 */
public class DbMetaHelper {

	public static DataSource create(String user, String password, String url, String driver, String validationQuery,
			int initConnection, int maxConnection) {
		Assert.notBlank(user);
		Assert.notBlank(url);
		Assert.notBlank(driver);
		Assert.notBlank(validationQuery);
		Properties props = new Properties();
		{
			Map map = MapHelper.toMap(/**/
					"username", user, /**/
					"password", password, /**/
					"url", url, /**/
					"driverClassName", driver, /**/
					"defaultAutoCommit", "false", /**/
					"initialSize", String.valueOf(Math.max(initConnection, 3)), /**/
					"maxActive", String.valueOf(Math.max(maxConnection, 3)), /**/
					"maxWait", "60000", /**/
					"validationQuery", validationQuery, /**/
					"testWhileIdle", "true", /**/
					"testOnBorrow", "false", /**/
					"testOnReturn", "false" /**/
			);
			props.putAll(map);
		}
		try {
			DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
			return dataSource;
		} catch (Exception e) {
			Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
		}
		return null;
	}

	public static Map<String, DescTableModel> findAllTableDesc(DataSource ds) {
		//		DataSource ds = DataSourceFactory.create("test", "iff", "iff",
		//				"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
		//				"com.mysql.jdbc.Driver", "select 1", 3, 10);
		Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
		Connection conn = null;
		try {
			String catalog = "";
			String schema = "";
			conn = ds.getConnection();
			{
				DatabaseMetaData dbmd = conn.getMetaData();
				ResultSet rs = dbmd.getTables(catalog, schema, null, new String[] { "TABLE", "VIEW" });
				while (rs.next()) {
					catalog = rs.getString("TABLE_CAT");
					schema = rs.getString("TABLE_SCHEM");
					String name = rs.getString("TABLE_NAME");
					String type = rs.getString("TABLE_TYPE");
					String remarks = rs.getString("REMARKS");
					table2Desc.put(name, DescTableModel.create(catalog, schema, name, type, remarks));
				}
				rs.close();
			}
			{
				DatabaseMetaData dbmd = conn.getMetaData();
				for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
					ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
					Map<String, String> pkMap = new HashMap<String, String>();
					while (rs.next()) {
						String idName = rs.getString("COLUMN_NAME");
						pkMap.put(StringUtils.upperCase(idName), idName);
						System.out.println("table=" + entry.getKey() + ", pk=" + idName);
					}
					rs.close();

					rs = dbmd.getColumns(catalog, schema, entry.getKey(), "%");
					while (rs.next()) {
						String colName = rs.getString("COLUMN_NAME");
						Integer sqlType = rs.getInt("DATA_TYPE");
						String sqlTypeName = rs.getString("TYPE_NAME");
						Integer size = rs.getInt("COLUMN_SIZE");
						Object o = rs.getObject("DECIMAL_DIGITS");
						Integer digit = null;
						if (o != null) {
							digit = ((Number) o).intValue();
						}
						int nullable = rs.getInt("NULLABLE");
						String defaultValue = rs.getString("COLUMN_DEF");
						String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
						String remark = rs.getString("REMARKS");
						colName = StringUtils.upperCase(colName);
						entry.getValue()
								.add(DescColumnModel.create(colName, SqlTypeMapping.getMybatisJavaType(sqlType),
										SqlTypeMapping.getDataBaseType(sqlType), size, digit, defaultValue, remark,
										StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
										nullable == 0 ? "N" : "Y", pkMap.containsKey(colName) ? "Y" : "N"));
						System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
								+ ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
								+ ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
								+ remark);
					}
					rs.close();
				}
			}
			for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
				System.out.println(entry.getValue());
			}
		} catch (Exception e) {
			Exceptions.runtime("findAllTableDesc error!", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return table2Desc;
	}
}
