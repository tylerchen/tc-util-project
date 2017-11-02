/*******************************************************************************
 * Copyright (c) Dec 1, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 1, 2015
 */
public class MySqlDatabaseStructureParser implements DatabaseStructureParser {
	/** ( -> ≦ **/
	private static final char RP_BLACKET_LEFT = '≦';
	/** ) -> ≧ **/
	private static final char RP_BLACKET_RIGHT = '≧';
	/** ; -> ± **/
	private static final char RP_SEMICOLON = '±';
	/** ' ' -> ≠ **/
	private static final char RP_BLANK = '≠';
	/** , -> ∞ **/
	private static final char RP_COMMA = '∞';
	/** , -> '∮' **/
	private static final char RP_COMMA2 = '∮';
	/** ` -> √ **/
	private static final char RP_RQOUT = '√';
	/** \r -> ' ' **/
	private static final char RP_RETURN = ' ';

	public String getType() {
		return "mysql";
	}

	public List<Table> parse(InputStream is) {
		String content = SocketHelper.getContent(is, false);
		StringBuilder sb = new StringBuilder(content.length());
		boolean pair = false;
		for (int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if (c == '\'') {
				pair = !pair;
			}
			if (pair) {
				if (c == '(') {
					sb.append(RP_BLACKET_LEFT);
				} else if (c == ')') {
					sb.append(RP_BLACKET_RIGHT);
				} else if (c == ';') {
					sb.append(RP_SEMICOLON);
				} else if (c == ' ') {
					sb.append(RP_BLANK);
				} else if (c == ',') {
					sb.append(RP_COMMA);
				} else if (c == '`') {
					sb.append(RP_RQOUT);
				} else if (c == '\r') {
					sb.append(RP_RETURN);
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}

		List<Table> tables = parseTables(getTableScripts(sb.toString()));
		sort(tables);
		return tables;
	}

	public List<String> getTableScripts(String sql) {
		List<String> tableScripts = new ArrayList<String>();
		String[] split = sql.split(";");
		Pattern p = Pattern.compile("^ *CREATE +TABLE +.*", Pattern.CASE_INSENSITIVE);
		for (String sp : split) {
			String clean = removeSqlComment(sp).trim();
			if (p.matcher(clean).find()) {
				tableScripts.add(clean);
			}
		}
		return tableScripts;
	}

	public String removeSqlComment(String input) {
		// remove /* ... */
		if (input.indexOf("/*") < 0 && input.indexOf("*/") < 0) {
			return input;
		}
		StringBuilder sb = new StringBuilder(input);
		{// remove sql comment
			int start = -1, end = -1;
			while ((start = sb.indexOf("/*")) > -1 && (end = sb.indexOf("*/", start)) > -1) {
				sb.delete(start, end + 2);
			}
		}
		return sb.toString();
	}

	public List<Table> parseTables(List<String> tableScripts) {
		List<Table> tables = new ArrayList<Table>();
		Pattern findComment = Pattern.compile("^ *COMMENT='(.*)'$", Pattern.CASE_INSENSITIVE);
		Pattern findCharset = Pattern.compile("^ *CHARSET=(.*)$", Pattern.CASE_INSENSITIVE);
		Pattern findEngine = Pattern.compile("^ *ENGINE=(.*)$", Pattern.CASE_INSENSITIVE);
		for (String script : tableScripts) {
			String tableName = null;
			{
				//CREATE TABLE `td_ol_crud_template`
				//CREATE [TEMPORARY] TABLE [IF NOT EXISTS] tbl_name
				String preName = script.substring(0, script.indexOf('('));
				String[] preNameSplit = preName.split(" ");
				tableName = preNameSplit[preNameSplit.length - 1];
				tableName = StringUtils.stripStart(tableName, "`");
				tableName = StringUtils.stripEnd(tableName, "`");
			}
			String comment = null;
			String charset = null;
			String engine = null;
			{
				//ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CRUD模板表'
				String subName = script.substring(script.lastIndexOf(')') + 1).trim();
				String[] split = subName.split(" ");
				{//find the comment
					for (String s : split) {
						Matcher matcher = findComment.matcher(s);
						if (matcher.find()) {
							comment = recover(matcher.group(1));
							break;
						}
					}
				}
				{//find the charset
					for (String s : split) {
						Matcher matcher = findCharset.matcher(s);
						if (matcher.find()) {
							charset = matcher.group(1);
							break;
						}
					}
				}
				{//find the engine
					for (String s : split) {
						Matcher matcher = findEngine.matcher(s);
						if (matcher.find()) {
							engine = matcher.group(1);
							break;
						}
					}
				}
			}
			{
				Table table = new Table();
				table.setCharset(charset);
				table.setComment(comment);
				table.setEngine(engine);
				table.setName(tableName);
				table.setOrginScript(script);
				table.setSort(tables.size());
				table.setFields(parseFields(script));
				tables.add(table);
			}
		}
		return tables;
	}

	public List<Field> parseFields(String tableScript) {
		List<Field> fields = new ArrayList<Field>();
		String cleanScript = tableScript;
		Pattern findType = Pattern.compile(
				"^(BIT|BOOLEAN|BOOL|TINYINT|SMALLINT|MEDIUMINT|INTEGER|INT|BIGINT|REAL|DOUBLE|FLOAT|DECIMAL|NUMERIC|DATETIME|DATE|TIME|TIMESTAMP|YEAR|CHAR|VARCHAR|BINARY|VARBINARY|TINYBLOB|BLOB|MEDIUMBLOB|LONGBLOB|TINYTEXT|TEXT|MEDIUMTEXT|LONGTEXT|ENUM|SET).*$",
				Pattern.CASE_INSENSITIVE);
		Pattern findDefine = Pattern.compile(
				"^(PRIMARY +KEY|INDEX|KEY|UNIQUE|FULLTEXT|SPATIAL|CONSTRAINT|FOREIGN +KEY|CHECK).*$",
				Pattern.CASE_INSENSITIVE);
		Pattern findColDef = Pattern.compile(
				"^ *(NOT +NULL|NULL|DEFAULT|AUTO_INCREMENT|UNIQUE|PRIMARY|COLUMN_FORMAT|COMMENT).*$",
				Pattern.CASE_INSENSITIVE);
		{//remove table string
			//`ID` varchar(30) NOT NULL, PRIMARY KEY (`ID`)
			//http://dev.mysql.com/doc/refman/5.7/en/create-table.html
			cleanScript = tableScript.substring(tableScript.indexOf('(') + 1, tableScript.lastIndexOf(')'));
			StringBuilder sb = new StringBuilder(cleanScript.length());
			boolean pair = false;
			for (int i = 0; i < cleanScript.length(); i++) {
				char c = cleanScript.charAt(i);
				if (c == '\r' || c == '\r' || c == '\n' || c == '\t') {
					sb.append(' ');
					continue;
				}
				if (c == '(' || c == ')') {
					pair = !pair;
				}
				if (pair && c == ',') {
					sb.append(RP_COMMA2);
				} else {
					sb.append(c);
				}
			}
			cleanScript = sb.toString().trim();
		}
		String[] split = StringUtils.split(cleanScript, ",");
		List<String> defines = new ArrayList<String>();
		List<String> columns = new ArrayList<String>();
		{// seperate the column define and other define.
			for (String s : split) {
				s = s.trim();
				Matcher matcher = findDefine.matcher(s);
				if (matcher.find()) {
					defines.add(recoverBracket(s));
				} else {
					columns.add(recoverBracket(s));
				}
			}
		}
		{//parse the column define.
			for (String s : columns) {
				String name = null;
				String type = null;
				Integer length = null;
				Integer scale = null;
				String isNullable = "Y";
				String comment = null;
				String defaultValue = null;
				String isUnique = null;
				String isAutoIncrement = null;
				String isPrimaryKey = null;
				int index = -1;
				{
					name = s.substring(0, index = s.indexOf(' ')).trim();
					name = StringUtils.stripStart(name, "`");
					name = StringUtils.stripEnd(name, "`");
				}
				{
					s = s.substring(index + 1).trim();
					index = s.indexOf(' ') > -1 ? s.indexOf(' ') : s.length();
					type = s.substring(0, index).trim();
					Matcher matcher = findType.matcher(type);
					if (matcher.find()) {
						type = matcher.group(1);
					}
				}
				{
					s = s.substring(s.indexOf(type) + type.length()).trim();
					String tmp = null;
					if (s.startsWith("(")) {
						tmp = s.substring(1, index = s.indexOf(')')).trim();
						String[] tmpSplit = StringUtils.split(tmp, ",");
						length = tmpSplit.length > 0 ? Integer.valueOf(tmpSplit[0].trim()) : null;
						scale = tmpSplit.length > 1 ? Integer.valueOf(tmpSplit[1].trim()) : null;
					}
				}
				{
					s = length == null ? s : s.substring(index + 1);
					String tmp = null;
					Matcher matcher = findColDef.matcher(s);
					while (matcher.find()) {
						tmp = matcher.group(1).trim().toUpperCase();
						s = s.substring(s.indexOf(tmp) + tmp.length()).trim();
						if (tmp.startsWith("NOT")) {
							isNullable = "N";
						} else if (tmp.equals("AUTO_INCREMENT")) {
							isAutoIncrement = "Y";
						} else if (tmp.equals("UNIQUE")) {
							isUnique = "Y";
							s = s.toUpperCase().startsWith("KEY") ? s.substring("KEY".length()).trim() : s;
						} else if (tmp.startsWith("PRIMARY")) {
							isPrimaryKey = "Y";
						} else if (tmp.equals("DEFAULT")) {
							int indexOf = s.indexOf(' ');
							defaultValue = indexOf > -1 ? s.substring(0, indexOf) : s;
							s = s.substring(defaultValue.length());
							defaultValue = StringUtils.stripStart(defaultValue, "'");
							defaultValue = StringUtils.stripEnd(defaultValue, "'");
							defaultValue = recover(defaultValue);
						} else if (tmp.equals("COMMENT")) {
							int indexOf = s.indexOf(' ');
							comment = indexOf > -1 ? s.substring(0, indexOf) : s;
							s = s.substring(comment.length());
							comment = StringUtils.stripStart(comment, "'");
							comment = StringUtils.stripEnd(comment, "'");
							comment = recover(comment);
						}
						matcher = findColDef.matcher(s);
					}
				}
				{
					Field field = new Field();
					field.setName(name);
					field.setIsAutoIncrement(isAutoIncrement);
					field.setComment(comment);
					field.setDefaultValue(defaultValue);
					field.setIsPrimaryKey(isPrimaryKey);
					field.setIsUnique(isUnique);
					field.setLength(length);
					field.setIsNullable(isNullable == null && "NULL".equalsIgnoreCase(defaultValue) ? "Y" : isNullable);
					field.setScale(scale);
					field.setType(type);
					field.setSort(fields.size());
					fields.add(field);
				}
			}
		}
		{//parse the other defines.
			for (int size = 0; size < defines.size(); size++) {
				String s = defines.get(size);
				String tmp = null;
				Matcher matcher = findDefine.matcher(s.trim());
				if (matcher.find()) {
					tmp = matcher.group(1).trim().toUpperCase();
					s = s.substring(s.indexOf(tmp) + tmp.length()).trim();
					if (tmp.equals("CONSTRAINT")) {
						// just remove this key word.
						//CONSTRAINT `0_38775` FOREIGN KEY (`A`, `D`) REFERENCES `ibtest11a` (`A`, `D`) ON DELETE CASCADE ON UPDATE CASCADE
						String[] tmpSplit = s.split(" ");

						for (String kw : new String[] { "PRIMARY", "UNIQUE", "INDEX", "FOREIGN" }) {
							if (tmpSplit.length > 0 && tmpSplit[0].toUpperCase().equals(kw)) {
								s = s.substring(s.toUpperCase().indexOf(kw));
								break;
							} else if (tmpSplit.length > 1 && tmpSplit[1].toUpperCase().equals(kw)) {
								s = s.substring(s.toUpperCase().indexOf(kw));
								break;
							}
						}
						{//re-process again
							defines.set(size, s);
							size = size - 1;
							continue;
						}
					} else if (tmp.startsWith("PRIMARY")) {
						//CONSTRAINT `0_38775` PRIMARY KEY (`ID`)
						String idxName = StringHelper.cutOff(s, "(");
						String idx = s.substring(idxName.length());
						idxName = removeOquote(idxName);
						idx = removeBracket(idx);
						String[] idxSplit = StringUtils.split(idx.trim(), ",");
						for (String is : idxSplit) {
							is = removeOquote(is);
							for (Field f : fields) {
								if (is.equalsIgnoreCase(f.getName())) {
									f.setIsPrimaryKey("Y");
								}
							}
						}
					} else if (tmp.startsWith("FOREIGN")) {
						//CONSTRAINT `0_38775` FOREIGN KEY (`A`, `D`) REFERENCES `ibtest11a` (`A`, `D`) ON DELETE CASCADE ON UPDATE CASCADE
						String fieldName = s.substring(0, s.toUpperCase().indexOf(" REFERENCES "));
						String fkTableField = s.substring(fieldName.length() + " REFERENCES ".length());
						fieldName = removeBracket(fieldName);
						fkTableField = StringHelper.cutOff(fkTableField, ")");
						String fkTableName = StringHelper.cutOff(fkTableField, "(");
						String fkFieldName = removeBracket(fkTableField.substring(fkTableName.length()));
						fkTableName = removeOquote(fkTableName);
						String[] fieldNameSplit = StringUtils.split(fieldName, ",");
						String[] fkFieldNameSplit = StringUtils.split(fkFieldName, ",");
						for (int i = 0; i < fieldNameSplit.length; i++) {
							String fn = fieldNameSplit[i];
							String fkn = fkFieldNameSplit.length > i ? fkFieldNameSplit[i] : null;
							if (fkn == null) {
								break;
							}
							fn = removeOquote(fn);
							fkn = removeOquote(fkn);
							for (Field f : fields) {
								if (fn.equalsIgnoreCase(f.getName())) {
									f.setForeignTable(fkTableName);
									f.setForeignField(fkn);
								}
							}
						}
					} else if (tmp.equals("KEY")) {
						//CONSTRAINT `0_38775` KEY `index_name` (`ID`)
						String idxName = StringHelper.cutOff(s, "(");
						String idx = s.substring(idxName.length());
						idxName = removeOquote(idxName);
						idx = removeBracket(idx);
						String[] idxSplit = StringUtils.split(idx.trim(), ",");
						for (String is : idxSplit) {
							is = removeOquote(is);
							for (Field f : fields) {
								if (is.equalsIgnoreCase(f.getName())) {
									f.setIsIndex("Y");
								}
							}
						}
					} else if (tmp.equals("UNIQUE")) {
						//CONSTRAINT `0_38775` UNIQUE KEY `index_name` (`ID`)
						s = removeIfStarts(s, "KEY");
						String idxName = StringHelper.cutOff(s, "(");
						String idx = s.substring(idxName.length());
						idxName = removeOquote(idxName);
						idx = removeBracket(idx);
						String[] idxSplit = StringUtils.split(idx.trim(), ",");
						for (String is : idxSplit) {
							is = removeOquote(is);
							for (Field f : fields) {
								if (is.equalsIgnoreCase(f.getName())) {
									f.setIsUnique("Y");
								}
							}
						}
					}
				}
			}
		}
		return fields;
	}

	public String removeOquote(String src) {
		src = src.trim();
		return StringUtils.stripEnd(StringUtils.stripStart(src, "`"), "`");
	}

	public String removeBracket(String src) {
		src = src.trim();
		return StringUtils.stripEnd(StringUtils.stripStart(src, "("), ")");
	}

	public String removeIfStarts(String src, String start) {
		src = src.trim();
		return src.toUpperCase().startsWith(start.toUpperCase()) ? src.substring(start.length()).trim() : src;
	}

	public String recoverBracket(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == RP_COMMA2) {
				sb.append(',');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public String recover(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == RP_BLACKET_LEFT) {
				sb.append('(');
			} else if (c == RP_BLACKET_RIGHT) {
				sb.append(')');
			} else if (c == RP_SEMICOLON) {
				sb.append(';');
			} else if (c == RP_BLANK) {
				sb.append(' ');
			} else if (c == RP_COMMA) {
				sb.append(',');
			} else if (c == RP_RQOUT) {
				sb.append('`');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public List<Table> sort(List<Table> tables) {
		Map<String, Table> tableMap = new LinkedHashMap<String, Table>();
		Map<Table, List<Table>> fkTableMap = new LinkedHashMap<Table, List<Table>>();
		for (Table table : tables) {
			String tableName = table.getName().toUpperCase();
			tableMap.put(tableName, table);
		}
		for (Table table : tables) {
			String tableName = table.getName().toUpperCase();
			tableMap.put(tableName, table);
			List<Table> list = new ArrayList<Table>();
			for (Field field : table.getFields()) {
				String fkTable = StringUtils.upperCase(field.getForeignTable());
				if (fkTable == null || fkTable.equals(tableName) || !tableMap.containsKey(fkTable)) {
					continue;
				} else {
					list.add(tableMap.get(fkTable));
				}
			}
			fkTableMap.put(table, list);
		}
		int times = 0;
		List<Table> list = new ArrayList<Table>(tableMap.values());
		for (int i = 0; i < list.size(); i++) {
			Table table = list.get(i);
			List<Table> fkList = fkTableMap.get(table);
			int maxPos = -1;
			for (Table fk : fkList) {
				maxPos = Math.max(maxPos, list.indexOf(fk));
			}
			if (maxPos > i) {
				list.remove(i);
				list.add(maxPos, table);
				i = i - 1;
			}
			if (times++ > list.size() * list.size()) {
				break;
			}
		}
		tables.clear();
		tables.addAll(list);
		return tables;
	}
}
