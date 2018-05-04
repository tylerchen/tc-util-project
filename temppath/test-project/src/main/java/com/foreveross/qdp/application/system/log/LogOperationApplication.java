/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.log;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.log.LogOperationVO;

/**
 * LogOperation Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface LogOperationApplication {

	/**
	 * <pre>
	 * get LogOperationVO by id.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	LogOperationVO getLogOperation(LogOperationVO vo);

	/**
	 * <pre>
	 * get LogOperationVO by id.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	LogOperationVO getLogOperationById(String id);
	
	/**
	 * <pre>
	 * page find LogOperationVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindLogOperation(LogOperationVO vo, Page page);
	
	/**
	 * <pre>
	 * page find LogOperation Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindLogOperationMap(LogOperationVO vo, Page page);

	/**
	 * <pre>
	 * add LogOperation.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	LogOperationVO addLogOperation(LogOperationVO vo);
	
	/**
	 * <pre>
	 * update LogOperation.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	LogOperationVO updateLogOperation(LogOperationVO vo);

	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeLogOperation(LogOperationVO vo);
	
	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeLogOperationById(String id);
	
	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeLogOperationByIds(String[] ids);
	
	
	
	
}
