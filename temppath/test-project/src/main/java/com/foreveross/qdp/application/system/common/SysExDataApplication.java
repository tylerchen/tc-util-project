/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.common;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.common.SysExDataVO;

/**
 * SysExData Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysExDataApplication {

	/**
	 * <pre>
	 * get SysExDataVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysExDataVO getSysExData(SysExDataVO vo);

	/**
	 * <pre>
	 * get SysExDataVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysExDataVO getSysExDataById(String id);
	
	/**
	 * <pre>
	 * page find SysExDataVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysExData(SysExDataVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysExData Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysExDataMap(SysExDataVO vo, Page page);

	/**
	 * <pre>
	 * add SysExData.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysExDataVO addSysExData(SysExDataVO vo);
	
	/**
	 * <pre>
	 * update SysExData.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysExDataVO updateSysExData(SysExDataVO vo);

	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysExData(SysExDataVO vo);
	
	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysExDataById(String id);
	
	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysExDataByIds(String[] ids);
	
	
	
	
}
