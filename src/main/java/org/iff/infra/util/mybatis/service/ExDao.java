/*******************************************************************************
 * Copyright (c) 2014-2-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.service;

import java.util.List;

import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Logger;
import org.iff.infra.util.mybatis.plugin.Page;

/**
 * Not support JTA transaction.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2014-2-26
 */
public class ExDao {
	private static ExDao defaultDao = null;
	private RepositoryService service;

	/**
	 * return the Dao with default RepositoryService[tcRepositoryService].
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 11, 2016
	 */
	public static ExDao getDefaultDao() {
		if (defaultDao == null) {
			defaultDao = new ExDao();
			try {
				defaultDao.service = InstanceFactory.getInstance("tcRepositoryService");
				Assert.notNull(defaultDao.service,
						"RepositoryService[tcRepositoryService], was not found, please configure a spring bean: <bean id=\"tcRepositoryService\" class=\"org.iff.infra.util.mybatis.service.impl.MybatisRepositoryServiceImpl\" />");
			} catch (Exception e) {
				Logger.debug("Dao.getService ERROR:", e);
				defaultDao.service = InstanceFactory.getInstance(RepositoryService.class);
			}
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
		dao.service = service;
		return dao;
	}

	/**
	 * return the RepositoryService of Dao.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 11, 2016
	 */
	public RepositoryService getService() {
		return service;
	}

	protected void setService(RepositoryService service) {
		this.service = service;
	}

	/**
	 * 查询总记录数
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public long querySize(String queryDsl, Object params) {
		return getService().querySize(queryDsl, params);
	}

	/**
	 * 查询列表
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public <T> List<T> queryList(String queryDsl, Object params) {
		return getService().queryList(queryDsl, params);
	}

	/**
	 * 查询单条记录
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public <T> T queryOne(String queryDsl, Object params) {
		return getService().queryOne(queryDsl, params);
	}

	/**
	 * 分面查询
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public Page queryPage(String queryDsl, Object params) {
		return getService().queryPage(queryDsl, params);
	}

	/**
	 * 保存
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public int save(String queryDsl, Object params) {
		return getService().save(queryDsl, params);
	}

	/**
	 * 更新
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public int update(String queryDsl, Object params) {
		return getService().update(queryDsl, params);
	}

	/**
	 * 删除
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public int remove(String queryDsl, Object params) {
		return getService().remove(queryDsl, params);
	}
}
