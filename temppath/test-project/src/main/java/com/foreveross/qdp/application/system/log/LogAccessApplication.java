/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.log;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.log.LogAccessVO;

/**
 * LogAccess Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface LogAccessApplication {

	/**
	 * <pre>
	 * get LogAccessVO by id.
	 * </pre>
	 * @param vo
	 * @return LogAccessVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	LogAccessVO getLogAccess(LogAccessVO vo);

	/**
	 * <pre>
	 * get LogAccessVO by id.
	 * </pre>
	 * @param vo
	 * @return LogAccessVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	LogAccessVO getLogAccessById(String id);
	
	/**
	 * <pre>
	 * page find LogAccessVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindLogAccess(LogAccessVO vo, Page page);
	
	/**
	 * <pre>
	 * page find LogAccess Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindLogAccessMap(LogAccessVO vo, Page page);

	/**
	 * <pre>
	 * add LogAccess.
	 * </pre>
	 * @param vo
	 * @return LogAccessVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	LogAccessVO addLogAccess(LogAccessVO vo);
	
	/**
	 * <pre>
	 * update LogAccess.
	 * </pre>
	 * @param vo
	 * @return LogAccessVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	LogAccessVO updateLogAccess(LogAccessVO vo);

	/**
	 * <pre>
	 * remove LogAccess.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeLogAccess(LogAccessVO vo);
	
	/**
	 * <pre>
	 * remove LogAccess.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeLogAccessById(String id);
	
	/**
	 * <pre>
	 * remove LogAccess.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeLogAccessByIds(String[] ids);
	
	
	
	
}
