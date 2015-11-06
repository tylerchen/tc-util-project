package org.iff.infra.util.mybatis.service;

import java.util.List;

import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.util.mybatis.plugin.Page;

public class Dao {
	private static RepositoryService service;

	public static RepositoryService getService() {
		if (service == null) {
			service = InstanceFactory.getInstance(RepositoryService.class);
		}
		return service;
	}

	public static void setService(RepositoryService service) {
		Dao.service = service;
	}

	/**
	 * 查询总记录数
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static long querySize(String queryDsl, Object params) {
		return getService().querySize(queryDsl, params);
	}

	/**
	 * 查询列表
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static <T> List<T> queryList(String queryDsl, Object params) {
		return getService().queryList(queryDsl, params);
	}

	/**
	 * 查询单条记录
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static <T> T queryOne(String queryDsl, Object params) {
		return getService().queryOne(queryDsl, params);
	}

	/**
	 * 分面查询
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static <T> Page queryPage(String queryDsl, Object params) {
		return getService().queryPage(queryDsl, params);
	}

	/**
	 * 保存
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static void save(String queryDsl, Object params) {
		getService().save(queryDsl, params);
	}

	/**
	 * 更新
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static void update(String queryDsl, Object params) {
		getService().update(queryDsl, params);
	}

	/**
	 * 删除
	 * @param queryDsl 查询名称
	 * @param params   参数
	 */
	public static void remove(String queryDsl, Object params) {
		getService().remove(queryDsl, params);
	}
}
