package org.iff.infra.util.mybatis.service;

public interface RepositoryService extends QueryService {

	int save(String queryDsl, Object params);

	int update(String queryDsl, Object params);

	int remove(String queryDsl, Object params);

}
