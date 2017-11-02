/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.StringHelper;

/**
 * database structure convertor, convert the database structure from a database type to other.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
@SuppressWarnings("unchecked")
public class Mysql2H2DatabaseStructureConvertor implements DatabaseStructureConvertor {

	private JavaTypeMapping mysqlJavaType = new MySqlJavaTypeMapping();

	public String getType() {
		return "mysql2h2";
	}

	public String parse(InputStream is) {
		List<Table> tables = new MySqlDatabaseStructureParser().parse(is);
		List<String> parse = new ArrayList<String>();
		for (Table table : tables) {
			String convertTable = convertTable(table);
			parse.add(convertTable);
			parse.add("\r\n\r\n\r\n");
		}
		return StringUtils.join(parse.toArray());
	}

	public String convertTable(Table table) {
		String tableStr = ""/**/
				+ "CREATE TABLE {tableName} (\r\n"/**/
				+ "{columns}"/**/
				+ ");\r\n"/**/
				+ "{fk}"/**/
				+ "{defaultValue}"/**/
				+ "{index}"/**/
				+ "{comment}"/**/
				;
		String tableName = StringUtils.upperCase(table.getName());
		List<String> columns = new ArrayList<String>();
		{
			for (Field field : table.getFields()) {
				String val = convertColumn(table, field).trim();
				if (val.length() > 0) {
					columns.add("    " + val + ",\r\n");
				}
			}
			{/*remove the last column ,\r */
				String last = columns.get(columns.size() - 1);
				columns.set(columns.size() - 1, last.substring(0, last.length() - 3) + "\r\n");
			}
		}
		List<String> fk = new ArrayList<String>();
		{
			for (Field field : table.getFields()) {
				if (field.getForeignField() != null) {
					String val = convertFK(table, field);
					if (val.length() > 0) {
						fk.add(val + "\r\n");
					}
				}
			}
		}
		List<String> defaultValue = new ArrayList<String>();
		{
			for (Field field : table.getFields()) {
				if (field.getDefaultValue() != null) {
					String val = convertDefaultValue(table, field);
					if (val.length() > 0) {
						defaultValue.add(val + "\r\n");
					}
				}
			}
		}
		List<String> index = new ArrayList<String>();
		{
			for (Field field : table.getFields()) {
				if ("Y".equals(field.getIsIndex())) {
					String val = convertIndex(table, field);
					if (val.length() > 0) {
						index.add(val + "\r\n");
					}
				}
			}
		}
		List<String> comment = new ArrayList<String>();
		{
			{
				comment.add(convertComment(table, null) + "\r\n");
			}
			for (Field field : table.getFields()) {
				comment.add(convertComment(table, field) + "\r\n");
			}
		}
		return StringHelper.replaceBlock(tableStr,
				MapHelper.toMap(/**/
						"tableName", tableName, /**/
						"columns", StringUtils.join(columns.toArray()), /**/
						"fk", StringUtils.join(fk.toArray()), /**/
						"defaultValue", StringUtils.join(defaultValue.toArray()), /**/
						"index", StringUtils.join(index.toArray()), /**/
						"comment", StringUtils.join(comment.toArray()) /**/
		), null);
	}

	public String convertColumn(Table table, Field field) {
		/*ID VARCHAR2(40) NOT NULL PRIMARY KEY,*/
		/*ID VARCHAR2(40) NOT NULL UNIQUE,*/
		String column = ""/**/
				+ "{columnName} {type}{lenScale} {notNull} {pk} {unique}";
		{
			String columnName = StringUtils.upperCase(field.getName());
			String type = StringUtils.upperCase(field.getType());
			String lenScale = "";
			{
				if (field.getLength() != null) {
					if (field.getScale() != null) {
						lenScale = StringHelper.replaceBlock("({len},{scale})",
								new Object[] { field.getLength(), field.getScale() }, null);
					} else {
						lenScale = StringHelper.replaceBlock("({len})", new Object[] { field.getLength() }, null);
					}
				}
			}
			String notNull = "Y".equals(field.getIsNullable()) ? "" : "NOT NULL";
			String pk = "Y".equals(field.getIsPrimaryKey()) ? "PRIMARY KEY" : "";
			String unique = "Y".equals(field.getIsUnique()) ? "UNIQUE" : "";
			return StringHelper.replaceBlock(column,
					MapHelper.toMap(/**/
							"columnName", columnName, /**/
							"type", type, /**/
							"lenScale", lenScale, /**/
							"notNull", notNull, /**/
							"pk", pk, /**/
							"unique", unique/**/
			), null);
		}
	}

	public String convertPK(Table table, Field field) {
		return "";
	}

	public String convertFK(Table table, Field field) {
		/*ALTER TABLE AUTH_ORGANIZATION_MENU ADD FOREIGN KEY (ORGANIZATION_ID) REFERENCES AUTH_ORGANIZATION (ID);*/
		String fk = "ALTER TABLE {tableName} ADD FOREIGN KEY ({fkColumn}) REFERENCES {refTable} ({refColumn});";
		return StringHelper.replaceBlock(fk,
				MapHelper.toMap( /**/
						"tableName", StringUtils.upperCase(table.getName()), /**/
						"fkColumn", StringUtils.upperCase(field.getName()), /**/
						"refTable", StringUtils.upperCase(field.getForeignTable()), /**/
						"refColumn", StringUtils.upperCase(field.getForeignField()) /**/
		), null);
	}

	public String convertUnique(Table table, Field field) {
		return "";
	}

	public String convertIndex(Table table, Field field) {
		/*CREATE INDEX AUTH_DATA_FILTER_CONF_STATUS ON AUTH_DATA_FILTER_CONF(STATUS);*/
		String index = "CREATE INDEX ON {tableName}({column});";
		return StringHelper.replaceBlock(index,
				MapHelper.toMap( /**/
						"indexName", StringUtils.upperCase(field.getName()), /**/
						"tableName", StringUtils.upperCase(table.getName()), /**/
						"column", StringUtils.upperCase(field.getName()) /**/
		), null);
	}

	public String convertComment(Table table, Field field) {
		/*COMMENT ON TABLE AUTH_ORGANIZATION_MENU IS '组织机构菜单';*/
		/*COMMENT ON COLUMN AUTH_ORGANIZATION_MENU.ID IS '主键';*/
		String tableComment = "COMMENT ON TABLE {tableName} IS '{comment}';";
		String columnComment = "COMMENT ON COLUMN {tableName}.{columnName} IS '{comment}';";
		if (field == null) {
			return StringHelper.replaceBlock(tableComment,
					MapHelper.toMap( /**/
							"tableName", StringUtils.upperCase(table.getName()), /**/
							"comment",
							StringUtils.defaultIfBlank(table.getComment(), StringUtils.upperCase(table.getName())) /**/
			), null);
		} else {
			return StringHelper.replaceBlock(columnComment,
					MapHelper.toMap( /**/
							"tableName", StringUtils.upperCase(table.getName()), /**/
							"columnName", StringUtils.upperCase(field.getName()), /**/
							"comment",
							StringUtils.defaultIfBlank(field.getComment(), StringUtils.upperCase(field.getName())) /**/
			), null);
		}
	}

	public String convertDefaultValue(Table table, Field field) {
		/*ALTER TABLE <table name> ALTER COLUMN <column name> SET DEFAULT CURRENT_TIMESTAMP;*/
		String defaultValue = "ALTER TABLE {tableName} ALTER COLUMN {columnName} SET DEFAULT {defaultValue};";
		boolean isString = "String".equals(mysqlJavaType.get(field.getType(), field.getLength(), field.getScale()));
		boolean isDate = java.util.Date.class.getName()
				.equals(mysqlJavaType.get(field.getType(), field.getLength(), field.getScale()));
		String defaultVal = field.getDefaultValue();
		if ("NULL".equals(defaultVal)) {
			return "";
		}

		return StringHelper.replaceBlock(defaultValue,
				MapHelper.toMap( /**/
						"tableName", StringUtils.upperCase(table.getName()), /**/
						"columnName", StringUtils.upperCase(field.getName()), /**/
						"defaultValue",
						isString || isDate ? ("'" + field.getDefaultValue() + "'") : field.getDefaultValue() /**/
		), null);
	}

	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream(
					"/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/main/resources/database/001-qdp-auth-table-mysql.sql");
			String parse = new Mysql2H2DatabaseStructureConvertor().parse(is);
			System.out.println(parse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
