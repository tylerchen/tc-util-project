/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iff.infra.util.database.DatabaseStructureParser;
import org.iff.infra.util.database.JavaTypeMapping;
import org.iff.infra.util.database.MySqlDatabaseStructureParser;
import org.iff.infra.util.database.MySqlJavaTypeMapping;
import org.iff.infra.util.database.Table;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
public class DatabaseStructureHelper {

	private static final Map<String, DatabaseStructureParser> parsers = new HashMap<String, DatabaseStructureParser>();
	private static final Map<String, JavaTypeMapping> javaTypes = new HashMap<String, JavaTypeMapping>();

	public static DatabaseStructureParser getParser(String database) {
		if (parsers.isEmpty()) {
			synchronized (parsers) {
				if (parsers.isEmpty()) {
					DatabaseStructureParser mysql = new MySqlDatabaseStructureParser();
					parsers.put(mysql.getType(), mysql);
				}
			}
		}
		return parsers.get(database);
	}

	public static JavaTypeMapping getTypeMapping(String database) {
		if (javaTypes.isEmpty()) {
			synchronized (javaTypes) {
				if (javaTypes.isEmpty()) {
					MySqlJavaTypeMapping mysql = new MySqlJavaTypeMapping();
					javaTypes.put(mysql.getType(), mysql);
				}
			}
		}
		return javaTypes.get(database);
	}

	public static void main(String[] args) {//td_ol_model_property
		try {
			List<Table> parse = getParser("mysql").parse(new FileInputStream(
					"/Users/zhaochen/dev/workspace/cocoa/foss-qdp-project/src/test/resources/test.sql"));
			for (Table t : parse) {
				if (t.getName().equalsIgnoreCase("td_ol_model_property")) {
					System.out.println(t);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
