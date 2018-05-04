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
import com.foreveross.qdp.infra.vo.system.common.SysScriptVO;

/**
 * SysScript Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysScriptApplication {

	/**
	 * <pre>
	 * get SysScriptVO by id.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysScriptVO getSysScript(SysScriptVO vo);

	/**
	 * <pre>
	 * get SysScriptVO by id.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysScriptVO getSysScriptById(String id);
	
	/**
	 * <pre>
	 * page find SysScriptVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysScript(SysScriptVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysScript Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysScriptMap(SysScriptVO vo, Page page);

	/**
	 * <pre>
	 * add SysScript.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysScriptVO addSysScript(SysScriptVO vo);
	
	/**
	 * <pre>
	 * update SysScript.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	SysScriptVO updateSysScript(SysScriptVO vo);

	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysScript(SysScriptVO vo);
	
	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysScriptById(String id);
	
	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeSysScriptByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get SysScript by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	SysScriptVO getByName(String name);
	
	/**
	 * <pre>
	 * get SysScript by unique code
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	SysScriptVO getByCode(String code);
	
	
	
}
