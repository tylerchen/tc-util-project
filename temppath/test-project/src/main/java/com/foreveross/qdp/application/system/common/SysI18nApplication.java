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
import com.foreveross.qdp.infra.vo.system.common.SysI18nVO;

/**
 * SysI18n Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface SysI18nApplication {

	/**
	 * <pre>
	 * get SysI18nVO by id.
	 * </pre>
	 * @param vo
	 * @return SysI18nVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysI18nVO getSysI18n(SysI18nVO vo);

	/**
	 * <pre>
	 * get SysI18nVO by id.
	 * </pre>
	 * @param vo
	 * @return SysI18nVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysI18nVO getSysI18nById(String id);
	
	/**
	 * <pre>
	 * page find SysI18nVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysI18n(SysI18nVO vo, Page page);
	
	/**
	 * <pre>
	 * page find SysI18n Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindSysI18nMap(SysI18nVO vo, Page page);

	/**
	 * <pre>
	 * add SysI18n.
	 * </pre>
	 * @param vo
	 * @return SysI18nVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysI18nVO addSysI18n(SysI18nVO vo);
	
	/**
	 * <pre>
	 * update SysI18n.
	 * </pre>
	 * @param vo
	 * @return SysI18nVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	SysI18nVO updateSysI18n(SysI18nVO vo);

	/**
	 * <pre>
	 * remove SysI18n.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysI18n(SysI18nVO vo);
	
	/**
	 * <pre>
	 * remove SysI18n.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysI18nById(String id);
	
	/**
	 * <pre>
	 * remove SysI18n.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeSysI18nByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get SysI18n by unique messageKey
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	SysI18nVO getByMessageKey(String messageKey);
	
	
	
}
