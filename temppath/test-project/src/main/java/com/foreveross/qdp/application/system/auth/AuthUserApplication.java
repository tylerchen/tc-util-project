/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.auth;

import org.iff.infra.util.mybatis.plugin.Page;
import com.foreveross.qdp.infra.vo.system.auth.AuthUserVO;

/**
 * AuthUser Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface AuthUserApplication {

	/**
	 * <pre>
	 * get AuthUserVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthUserVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthUserVO getAuthUser(AuthUserVO vo);

	/**
	 * <pre>
	 * get AuthUserVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthUserVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthUserVO getAuthUserById(String id);
	
	/**
	 * <pre>
	 * page find AuthUserVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthUser(AuthUserVO vo, Page page);
	
	/**
	 * <pre>
	 * page find AuthUser Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthUserMap(AuthUserVO vo, Page page);

	/**
	 * <pre>
	 * add AuthUser.
	 * </pre>
	 * @param vo
	 * @return AuthUserVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthUserVO addAuthUser(AuthUserVO vo);
	
	/**
	 * <pre>
	 * update AuthUser.
	 * </pre>
	 * @param vo
	 * @return AuthUserVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthUserVO updateAuthUser(AuthUserVO vo);

	/**
	 * <pre>
	 * remove AuthUser.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthUser(AuthUserVO vo);
	
	/**
	 * <pre>
	 * remove AuthUser.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthUserById(String id);
	
	/**
	 * <pre>
	 * remove AuthUser.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthUserByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get AuthUser by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthUserVO getByName(String name);
	
	/**
	 * <pre>
	 * get AuthUser by unique email
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthUserVO getByEmail(String email);
	
	
	
}
