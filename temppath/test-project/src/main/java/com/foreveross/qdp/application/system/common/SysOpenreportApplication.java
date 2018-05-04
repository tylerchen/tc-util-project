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
import com.foreveross.qdp.infra.vo.system.common.SysOpenreportVO;

/**
 * SysOpenreport Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysOpenreportApplication {

	/**
	 * <pre>
	 * get SysOpenreportVO by id.
	 * </pre>
	 * @param vo
	 * @return SysOpenreportVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysOpenreportVO getSysOpenreport(SysOpenreportVO vo);

	/**
	 * <pre>
	 * get SysOpenreportVO by id.
	 * </pre>
	 * @param vo
	 * @return SysOpenreportVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysOpenreportVO getSysOpenreportById(String id);
	
	/**
	 * <pre>
	 * page find SysOpenreportVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysOpenreport(SysOpenreportVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysOpenreport Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysOpenreportMap(SysOpenreportVO vo, Page page);

	/**
	 * <pre>
	 * add SysOpenreport.
	 * </pre>
	 * @param vo
	 * @return SysOpenreportVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysOpenreportVO addSysOpenreport(SysOpenreportVO vo);
	
	/**
	 * <pre>
	 * update SysOpenreport.
	 * </pre>
	 * @param vo
	 * @return SysOpenreportVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysOpenreportVO updateSysOpenreport(SysOpenreportVO vo);

	/**
	 * <pre>
	 * remove SysOpenreport.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysOpenreport(SysOpenreportVO vo);
	
	/**
	 * <pre>
	 * remove SysOpenreport.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysOpenreportById(String id);
	
	/**
	 * <pre>
	 * remove SysOpenreport.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysOpenreportByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get SysOpenreport by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	SysOpenreportVO getByName(String name);
	
	
	
}
