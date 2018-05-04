/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.auth;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.auth.AuthResourceVO;

/**
 * AuthResource Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface AuthResourceApplication {

	/**
	 * <pre>
	 * get AuthResourceVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthResourceVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	AuthResourceVO getAuthResource(AuthResourceVO vo);

	/**
	 * <pre>
	 * get AuthResourceVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthResourceVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	AuthResourceVO getAuthResourceById(String id);
	
	/**
	 * <pre>
	 * page find AuthResourceVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthResource(AuthResourceVO vo, Page page);
	
	/**
	 * <pre>
	 * page find AuthResource Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthResourceMap(AuthResourceVO vo, Page page);

	/**
	 * <pre>
	 * add AuthResource.
	 * </pre>
	 * @param vo
	 * @return AuthResourceVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	AuthResourceVO addAuthResource(AuthResourceVO vo);
	
	/**
	 * <pre>
	 * update AuthResource.
	 * </pre>
	 * @param vo
	 * @return AuthResourceVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	AuthResourceVO updateAuthResource(AuthResourceVO vo);

	/**
	 * <pre>
	 * remove AuthResource.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeAuthResource(AuthResourceVO vo);
	
	/**
	 * <pre>
	 * remove AuthResource.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeAuthResourceById(String id);
	
	/**
	 * <pre>
	 * remove AuthResource.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	void removeAuthResourceByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get AuthResource by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	AuthResourceVO getByName(String name);
	
	/**
	 * <pre>
	 * get AuthResource by unique code
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	AuthResourceVO getByCode(String code);
	
	
	
}
