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
import com.foreveross.qdp.infra.vo.system.common.SysExTableVO;

/**
 * SysExTable Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysExTableApplication {

	/**
	 * <pre>
	 * get SysExTableVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExTableVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysExTableVO getSysExTable(SysExTableVO vo);

	/**
	 * <pre>
	 * get SysExTableVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExTableVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysExTableVO getSysExTableById(String id);
	
	/**
	 * <pre>
	 * page find SysExTableVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysExTable(SysExTableVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysExTable Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysExTableMap(SysExTableVO vo, Page page);

	/**
	 * <pre>
	 * add SysExTable.
	 * </pre>
	 * @param vo
	 * @return SysExTableVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysExTableVO addSysExTable(SysExTableVO vo);
	
	/**
	 * <pre>
	 * update SysExTable.
	 * </pre>
	 * @param vo
	 * @return SysExTableVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysExTableVO updateSysExTable(SysExTableVO vo);

	/**
	 * <pre>
	 * remove SysExTable.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysExTable(SysExTableVO vo);
	
	/**
	 * <pre>
	 * remove SysExTable.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysExTableById(String id);
	
	/**
	 * <pre>
	 * remove SysExTable.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysExTableByIds(String[] ids);
	
	
	
	
}
