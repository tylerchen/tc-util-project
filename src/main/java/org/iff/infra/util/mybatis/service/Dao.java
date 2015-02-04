package org.iff.infra.util.mybatis.service;

import java.util.List;

import org.iff.infra.com.dayatang.domain.InstanceFactory;
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

	public static long querySize(String queryDsl, Object params) {
		return getService().querySize(queryDsl, params);
	}

	public static <T> List<T> queryList(String queryDsl, Object params) {
		return getService().queryList(queryDsl, params);
	}

	public static <T> T queryOne(String queryDsl, Object params) {
		return getService().queryOne(queryDsl, params);
	}

	public static <T> Page queryPage(String queryDsl, Object params) {
		return getService().queryPage(queryDsl, params);
	}

	public static void save(String queryDsl, Object params) {
		getService().save(queryDsl, params);
	}

	public static void update(String queryDsl, Object params) {
		getService().update(queryDsl, params);
	}

	public static void remove(String queryDsl, Object params) {
		getService().remove(queryDsl, params);
	}
}
