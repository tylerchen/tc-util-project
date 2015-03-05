package org.iff.infra.util.jdbc.dialet;

import java.util.HashMap;
import java.util.Map;

import org.iff.infra.util.ReflectHelper;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 */
public class Dialect {

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	/**
	 * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符.</br>
	 * 源代码为: getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit))
	 */
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换.
	 * <pre>
	 * 如mysql
	 * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 * @return 包含占位符的分页sql
	 */
	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		throw new UnsupportedOperationException("paged queries not supported");
	}

	public static Dialect getInstanceByUrl(String url) {
		//jdbc:db2://<host>[:<port>]/<database_name>
		//jdbc:derby:[subsubprotocol:][databaseName][;attribute=value]*
		//jdbc:h2:file:data/sample
		//jdbc:hsqldb:file:enrolments;create=false?user=aUserName&password=3xLVz
		//jdbc:mysql://[host][,failoverhost...][:port]/[database]
		//jdbc:oracle:<drivertype>:<user>/<password>@<database>
		//jdbc:postgresql://localhost:5432/test
		//jdbc:sqlserver://[serverName[\instanceName][:portNumber]][.property=value[.property=value]]
		//jdbc:jtds:sybase://<host>[:<port>][/<database_name>]
		Dialect dialect = new Dialect();
		if (url == null) {
			return dialect;
		}
		int index1 = url.indexOf(':');
		if (index1 < 0) {
			return dialect;
		}
		int index2 = url.indexOf(':', index1+1);
		if (index2 < 0) {
			return dialect;
		}
		String dbName = url.substring(index1 + 1, index2);
		Map<String, String> map = new HashMap<String, String>();
		{
			map.put("db2", "org.iff.infra.util.jdbc.dialet.DB2Dialect");
			map.put("derby", "org.iff.infra.util.jdbc.dialet.DerbyDialect");
			map.put("h2", "org.iff.infra.util.jdbc.dialet.H2Dialect");
			map.put("hsqldb", "org.iff.infra.util.jdbc.dialet.HSQLDialect");
			map.put("mysql", "org.iff.infra.util.jdbc.dialet.MySQLDialect");
			map.put("oracle", "org.iff.infra.util.jdbc.dialet.OracleDialect");
			map.put("postgresql", "org.iff.infra.util.jdbc.dialet.PostgreSQLDialect");
			map.put("sqlserver", "org.iff.infra.util.jdbc.dialet.SQLServerDialect");
			map.put("jtds", "org.iff.infra.util.jdbc.dialet.SybaseDialect");
		}
		String driverClass = map.get(dbName);
		if (driverClass == null) {
			return dialect;
		}
		try {
			return (Dialect) ReflectHelper.getConstructor(driverClass).newInstance();
		} catch (Exception e) {
			return dialect;
		}
	}
}
