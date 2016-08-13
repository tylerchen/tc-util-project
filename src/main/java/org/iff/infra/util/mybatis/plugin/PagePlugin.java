package org.iff.infra.util.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Logger;
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.jdbc.dialet.Dialect;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

	private static Dialect dialectObject = null; // 数据库方言
	private static String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	public Object intercept(Invocation ivk) throws Throwable {

		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY);
			//BaseStatementHandler delegate = (BaseStatementHandler) metaStatementHandler.getValue("delegate");
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			{
				Logger.changeLevel(mappedStatement.getId(), "debug");
			}

			/*String _SYS_CONDITION = "'_SYS_CONDITION'='_SYS_CONDITION'";
			
			//数据过滤部分 只是针对 SELECT 类型的sql语句
			if (mappedStatement.getSqlCommandType().toString().equalsIgnoreCase("SELECT")) {
				//String entityName = mappedStatement.getResultMaps().get(0).getType().getName();
				//System.out.println("entityName: " +  entityName);
				BoundSql boundSql = delegate.getBoundSql();
				String sql = boundSql.getSql();
			
				if (ThreadLocalHelper.get("accountId") != null) {
					AuthDataFilterConfVO authDataFilterConfVO = null;// CacheUtil4user4ehcache.getDataFilterConfig(ThreadUtil.accountId.get(), mappedStatement.getId());
					if (authDataFilterConfVO != null) {
			
						if (sql.contains("'AND _SYS_CONDITION'=_'SYS_CONDITION'")) {
							sql = sql.replace(_SYS_CONDITION, authDataFilterConfVO.getWhereValue());
						} else {
							sql = sql + " " + authDataFilterConfVO.getWhereValue();
						}
			
					}
				}
				//sql += " AND LOGIN_EMAIL ='test@126.com'";
				//sql += " AND US.NAME != '张三'";
			
				ReflectHelper.setValueByFieldName(boundSql, "sql", sql); // 将分页sql语句反射回BoundSql.
			}*/
			if (mappedStatement.getId().matches(pageSqlId)) { /*拦截需要分页的SQL*/
				BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
				Object parameterObject = boundSql
						.getParameterObject();/*分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空*/
				if (parameterObject == null) {
					throw new NullPointerException("boundSql.getParameterObject() is null!");
				} else {
					Page page = null;
					if (parameterObject instanceof Page) { /*参数就是Pages实体*/
						page = (Page) parameterObject;
					} else if (parameterObject instanceof Map) {
						for (Entry entry : (Set<Entry>) ((Map) parameterObject).entrySet()) {
							if (entry.getValue() instanceof Page) {
								page = (Page) entry.getValue();
								break;
							}
						}
					} else { /*参数为某个实体，该实体拥有Pages属性*/
						page = ReflectHelper.getValueByFieldType(parameterObject, Page.class);
					}
					if (page == null) {
						return ivk.proceed();
					}

					{/*采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数*/
						metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
						metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
					}
					String sql = boundSql.getSql();
					PreparedStatement countStmt = null;
					ResultSet rs = null;
					if (!page.isOffsetPage()) {
						try {
							Connection connection = (Connection) ivk.getArgs()[0];
							String countSql = "select count(*) from (" + removeOrders(sql) + ") tmp_count"; // 记录统计
							countStmt = connection.prepareStatement(countSql);
							metaStatementHandler.setValue("delegate.boundSql.sql", countSql);
							DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
									parameterObject, boundSql);
							parameterHandler.setParameters(countStmt);
							/*Constructor<?> defaultParameterHandlerObject = null;
							if (defaultParameterHandlerObject == null) {
								try {// for mybatis 3.1.1
									defaultParameterHandlerObject = ReflectHelper.getConstructor(
											"org.apache.ibatis.executor.parameter.DefaultParameterHandler",
											"org.apache.ibatis.mapping.MappedStatement", "java.lang.Object",
											"org.apache.ibatis.mapping.BoundSql");
								} catch (Exception e) {
									Logger.debug(FCS.get("[LoadDefaultParameterHandlerFail]:{0}",
											"org.apache.ibatis.executor.parameter.DefaultParameterHandler"));
								}
							}
							if (defaultParameterHandlerObject == null) {
								try {// for mybatis 3.2.8
									defaultParameterHandlerObject = ReflectHelper.getConstructor(
											"org.apache.ibatis.scripting.defaults.DefaultParameterHandler",
											"org.apache.ibatis.mapping.MappedStatement", "java.lang.Object",
											"org.apache.ibatis.mapping.BoundSql");
								} catch (Exception e) {
									Logger.debug(FCS.get("[LoadDefaultParameterHandlerFail]:{0}",
											"org.apache.ibatis.scripting.defaults.DefaultParameterHandler"));
								}
							}
							try {
								Object instance = defaultParameterHandlerObject.newInstance(mappedStatement,
										parameterObject, boundSql);
								Method method = ReflectHelper.getMethod(instance.getClass().getName(), "setParameters",
										"java.sql.PreparedStatement");
								method.invoke(instance, countStmt);
							} catch (Exception e) {
								Logger.debug("[DefaultParameterHandler.setParameters]", e);
							}*/
							rs = countStmt.executeQuery();
							int count = 0;
							if (rs.next()) {
								count = ((Number) rs.getObject(1)).intValue();
							}
							page.setTotalCount(count);
						} finally {
							try {
								rs.close();
							} catch (Exception e) {
							}
							try {
								countStmt.close();
							} catch (Exception e) {
							}
						}
					}

					String pageSql = generatePagesSql(sql, page);
					/*将分页sql语句反射回BoundSql*/
					metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
				}
			}
		}
		return ivk.proceed();
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String generatePagesSql(String sql, Page page) {
		if (page != null && dialectObject != null) {
			if (page.isOffsetPage()) {
				return dialectObject.getLimitString(sql, page.getOffset(), page.getPageSize());
			} else {
				return dialectObject.getLimitString(sql, (page.getCurrentPage() - 1) * page.getPageSize(),
						page.getPageSize());
			}
		}
		return sql;
	}

	public Object plugin(Object plugin) {
		return Plugin.wrap(plugin, this);
	}

	public void setProperties(Properties p) {
		String dialect = ""; // 数据库方言
		dialect = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		} else {
			try {
				dialectObject = (Dialect) Class.forName(dialect).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(dialect + ", init fail!\n" + e);
			}
		}
		pageSqlId = p.getProperty("pageSqlId");
		if (StringUtils.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery. 
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	private static Pattern p = Pattern.compile("order *by.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/** 
	 * 去除hql的orderby 子句，用于pagedQuery. 
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
