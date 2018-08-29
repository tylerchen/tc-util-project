/*******************************************************************************
 * Copyright (c) 2018-07-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/

import org.iff.infra.util.StreamHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * TestPresto
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-07-20
 * auto generate by qdp.
 */
public class TestPresto {
    public static void main(String[] args) {
//        String url = "jdbc:presto://121.40.127.45:8080/hive/testdb";
//        try {
//            Connection connection = DriverManager.getConnection(url, "test", null);
//            {
//                Statement statement = connection.createStatement();
//                StringBuilder sb = new StringBuilder(1024000).append("insert into test values");
//                int maxLen = 1000000 - 100;
//                for (int i = 50000; i < 1000000; i++) {
//                    sb.append("(").append(i).append(",'").append("test-").append(i).append("'),");
//                    if (sb.length() > maxLen) {
//                        sb.setLength(sb.length() - 1);
//                        statement.execute(sb.toString());
//                        sb.setLength(0);
//                        sb.append("insert into test values");
//                        System.out.println("Insert-" + i);
//                    }
//                }
//                sb.setLength(sb.length() - 1);
//                statement.execute(sb.toString());
//                StreamHelper.closeWithoutError(statement);
//            }
//            {
//                Statement statement = connection.createStatement();
//                ResultSet rs = statement.executeQuery("select * from test");
//                int columnCount = rs.getMetaData().getColumnCount();
//                while (rs.next()) {
//                    for (int i = 1; i < columnCount + 1; i++) {
//                        System.out.println(rs.getMetaData().getColumnLabel(i) + "=" + rs.getObject(i));
//                    }
//                }
//                StreamHelper.closeWithoutError(rs);
//            }
//            StreamHelper.closeWithoutError(connection);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
