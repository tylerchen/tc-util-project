package org.iff.infra.util.mybatis.service;

public interface RepositoryService extends QueryService {

	void save(String queryDsl, Object params);

	void update(String queryDsl, Object params);

	void remove(String queryDsl, Object params);

}
