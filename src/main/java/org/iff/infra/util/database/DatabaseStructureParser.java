/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
public interface DatabaseStructureParser {

	String getType();

	List<Table> parse(InputStream is);

	List<String> getTableScripts(String sql);

	String removeSqlComment(String input);

	List<Table> parseTables(List<String> tableScripts);

	List<Field> parseFields(String tableScript);

	List<Table> sort(List<Table> tables);

}
