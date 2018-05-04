/*******************************************************************************
 * Copyright (c) Dec 20, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.service;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public interface RepositoryService extends QueryService {

	int save(String queryDsl, Object params);

	int update(String queryDsl, Object params);

	int remove(String queryDsl, Object params);

}
