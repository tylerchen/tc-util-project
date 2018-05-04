/*******************************************************************************
 * Copyright (c) Dec 20, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.service;

import java.util.List;

import org.iff.infra.util.mybatis.plugin.Page;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public interface QueryService {

	long querySize(String queryDsl, Object params);

	<T> List<T> queryList(String queryDsl, Object params);

	<T> T queryOne(String queryDsl, Object params);

	<T> Page queryPage(String queryDsl, Object params);
}
