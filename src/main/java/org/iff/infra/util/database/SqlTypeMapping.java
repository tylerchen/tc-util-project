/*******************************************************************************
 * Copyright (c) Oct 8, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Oct 8, 2016
 */
public class SqlTypeMapping {

	public static String getMybatisJavaType(int jdbcType) {
		switch (jdbcType) {
		case Types.CHAR:
			return "String";

		case Types.VARCHAR:
			return "String";

		case Types.LONGVARCHAR:
			return "String";
		case Types.NCHAR:
			return "String";

		case Types.NVARCHAR:
			return "String";

		case Types.LONGNVARCHAR:
			return "String";

		case Types.SQLXML:
			return "String";

		case Types.NUMERIC:
			return BigDecimal.class.getName();

		case Types.DECIMAL:
			return BigDecimal.class.getName();

		case Types.BIT:
			return "boolean";

		case Types.BOOLEAN:
			return "boolean";

		case Types.TINYINT:
			return "byte";

		case Types.SMALLINT:
			return "short";

		case Types.INTEGER:
			return "int";

		case Types.BIGINT:
			return "long";

		case Types.REAL:
			return "float";

		case Types.FLOAT:
			return "double";

		case Types.DOUBLE:
			return "double";

		case Types.BINARY:
			return "byte[]";

		case Types.VARBINARY:
			return "byte[]";

		case Types.LONGVARBINARY:
			return "byte[]";

		case Types.DATE:
			return java.sql.Date.class.getName();

		case Types.TIME:
			return java.sql.Time.class.getName();

		case Types.TIMESTAMP:
			return java.sql.Timestamp.class.getName();

		case Types.CLOB:
			return java.sql.Clob.class.getName();

		case Types.NCLOB:
			return java.sql.Clob.class.getName();

		case Types.BLOB:
			return java.sql.Blob.class.getName();

		case Types.ARRAY:
			return java.sql.Array.class.getName();

		case Types.STRUCT:
			return java.sql.Struct.class.getName();

		case Types.REF:
			return java.sql.Ref.class.getName();

		case Types.DATALINK:
			return java.net.URL.class.getName();

		default:
			return "String";
		}
	}

	//VARCHAR|CHAR|DATETIME|TIMESTAMP|INT|BOOLEAN|SMALLINT|DOUBLE|FLOAT|DECIMAL|NUMERIC|BLOB|MEDIUMBLOB|LONGBLOB|TEXT|MEDIUMTEXT|LONGTEXT|CLOB
	public static String getDataBaseType(int jdbcType) {
		switch (jdbcType) {
		case Types.CHAR:
			return "CHAR";

		case Types.VARCHAR:
			return "VARCHAR";

		case Types.LONGVARCHAR:
			return "LONGTEXT";

		case Types.NCHAR:
			return "CHAR";

		case Types.NVARCHAR:
			return "VARCHAR";

		case Types.LONGNVARCHAR:
			return "LONGTEXT";

		case Types.SQLXML:
			return "VARCHAR";

		case Types.NUMERIC:
			return "NUMERIC";

		case Types.DECIMAL:
			return "DECIMAL";

		case Types.BIT:
			return "SMALLINT";

		case Types.BOOLEAN:
			return "BOOLEAN";

		case Types.TINYINT:
			return "SMALLINT";

		case Types.SMALLINT:
			return "SMALLINT";

		case Types.INTEGER:
			return "INT";

		case Types.BIGINT:
			return "DECIMAL";

		case Types.REAL:
			return "FLOAT";

		case Types.FLOAT:
			return "DOUBLE";

		case Types.DOUBLE:
			return "DOUBLE";

		case Types.BINARY:
			return "BLOB";

		case Types.VARBINARY:
			return "BLOB";

		case Types.LONGVARBINARY:
			return "LONGBLOB";

		case Types.DATE:
			return "DATETIME";

		case Types.TIME:
			return "DATETIME";

		case Types.TIMESTAMP:
			return "TIMESTAMP";

		case Types.CLOB:
			return "TEXT";

		case Types.NCLOB:
			return "TEXT";

		case Types.BLOB:
			return "BLOB";

		case Types.ARRAY:
			return "BLOB";

		case Types.STRUCT:
			return "BLOB";

		case Types.REF:
			return "BLOB";

		case Types.DATALINK:
			return "BLOB";

		default:
			return "VARCHAR";
		}
	}
}

/**
[测试]常见数据库字段类型与java.sql.Types的对应
Oracle与java.sql.Types的对应

Oracle                                java.sql.Types
blob                                     blob
char                                     char
clob                                     clob
date                                    date
number                               decimal
long                                     varbinary
nclob,nvarchar2                   other
smallint                                smallint
timestamp                            timstamp
raw                                      varbinary
varchar2                               varchar

Sql server与java.sql.Types的对应

Sql server                           java.sql.Types
   bigint (2005,2008)                bigint
   timstamp,binary                    binary
   bit                                         bit
   char,nchar,unqualified          char
   datetime                               date
   money,smallmoney,decimal  decimal
   float (2005,2008)                  double
   float(2000)                            float
   int                                          integer
   image                                    longvarbinary
   text,ntext,xml                        longvarchar

    numeric                                 numeric
    real                                       real
    smallint                                smallint
    datetime,smalldatetime       timestamp
    tinyint                                  tinyint
    varbinary                             varbinay
    nvarchar,varchar                 varchar

DB2与java.sql.Types的对应

bigint                                       bigint
   blob                                      blob
   character,graphic                 char
   clob                                      clob
   date                                     date
   decimal                                decimal
   double                                 double
    integer                               integer
    longvargraphic                   longvarchar
    longvarchar

real                                        real
smallint                                 smallint
time                                      time
timestamp                            timestamp
vargraphic                            varchar
varchar

MySQL与java.sql.Types的对应

MySQL                          java.sql.Types
  bigint                              bigint
   tinyblob                         binary
   bit                                  bit
   enum,set,char               char
   date,year                      date
   decimal,numeric            decimal
   double,real                   double
   mediumint,int                integer
   blob,mediumblob           blob
   longblob
   float                               real

smallint                           smallint
   time                             time
   timestamp,datetime     timestamp
   tinyint                           tinyint
   varbinary,binary           varbinay
   varchar,tinytext,text     varchar
Sybase与java.sql.Types的对应

Sybase                            java.sql.Types
   binary                                 binary
   bit                                       bit
   char,nchar,                            char
   money,smallmoney,decimal    decimal
   float                                     double
    int                                       integer
   image                                    longvarbinary
   text                                      longvarchar
numeric                                   numeric
    real                                     real
    smallint                              smallint
    datetime,smalldatetime     timestamp
    tinyint                                 tinyint
    varbinar,timestamp            varbinay
    nvarchar,varchar ,sysname   varchar 
**/