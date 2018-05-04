/*******************************************************************************
 * Copyright (c) 2014-2-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.service;

import java.util.List;

import org.iff.infra.util.Assert;
import org.iff.infra.util.mybatis.plugin.Page;

/**
 * Not support JTA transaction.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2014-2-26
 */
public class Dao {
	private static ExDao defaultDao = null;

	/**
	 * return the Dao with default RepositoryService[tcRepositoryService].
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 11, 2016
	 */
	public static ExDao getDefaultDao() {
		if (defaultDao == null) {
			defaultDao = ExDao.getDefaultDao();
		}
		return defaultDao;
	}

	/**
	 * create and return a new Dao, with the specify RepositoryService.
	 * @param service
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 11, 2016
	 */
	public static ExDao get(RepositoryService service) {
		Assert.notNull(service, "RepositoryService can't be null!");
		ExDao dao = new ExDao();
		dao.setService(service);
		return dao;
	}

	/**
	 * 查询总记录数
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static long querySize(String queryDsl, Object params) {
		return getDefaultDao().querySize(queryDsl, params);
	}

	/**
	 * 查询列表
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static <T> List<T> queryList(String queryDsl, Object params) {
		return getDefaultDao().queryList(queryDsl, params);
	}

	/**
	 * 查询单条记录
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static <T> T queryOne(String queryDsl, Object params) {
		return getDefaultDao().queryOne(queryDsl, params);
	}

	/**
	 * 分面查询
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static Page queryPage(String queryDsl, Object params) {
		return getDefaultDao().queryPage(queryDsl, params);
	}

	/**
	 * 保存
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static int save(String queryDsl, Object params) {
		return getDefaultDao().save(queryDsl, params);
	}

	/**
	 * 更新
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static int update(String queryDsl, Object params) {
		return getDefaultDao().update(queryDsl, params);
	}

	/**
	 * 删除
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static int remove(String queryDsl, Object params) {
		return getDefaultDao().remove(queryDsl, params);
	}
}
