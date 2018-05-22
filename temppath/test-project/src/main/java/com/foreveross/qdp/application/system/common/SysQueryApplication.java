/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.common;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.common.SysQueryVO;

/**
 * SysQuery Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysQueryApplication {

	/**
	 * <pre>
	 * get SysQueryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysQueryVO getSysQuery(SysQueryVO vo);

	/**
	 * <pre>
	 * get SysQueryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysQueryVO getSysQueryById(String id);
	
	/**
	 * <pre>
	 * page find SysQueryVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysQuery(SysQueryVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysQuery Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysQueryMap(SysQueryVO vo, Page page);

	/**
	 * <pre>
	 * add SysQuery.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysQueryVO addSysQuery(SysQueryVO vo);
	
	/**
	 * <pre>
	 * update SysQuery.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysQueryVO updateSysQuery(SysQueryVO vo);

	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysQuery(SysQueryVO vo);
	
	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysQueryById(String id);
	
	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysQueryByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get SysQuery by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	SysQueryVO getByName(String name);
	
	/**
	 * <pre>
	 * get SysQuery by unique code
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	SysQueryVO getByCode(String code);
	
	
	
}
