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
import com.foreveross.qdp.infra.vo.system.auth.AuthAccountVO;
import com.foreveross.qdp.infra.vo.system.auth.EditPasswordVO;

/**
 * AuthAccount Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface AuthAccountApplication {

	/**
	 * <pre>
	 * get AuthAccountVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthAccountVO getAuthAccount(AuthAccountVO vo);

	/**
	 * <pre>
	 * get AuthAccountVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthAccountVO getAuthAccountById(String id);
	
	/**
	 * <pre>
	 * page find AuthAccountVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthAccount(AuthAccountVO vo, Page page);
	
	/**
	 * <pre>
	 * page find AuthAccount Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthAccountMap(AuthAccountVO vo, Page page);

	/**
	 * <pre>
	 * add AuthAccount.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthAccountVO addAuthAccount(AuthAccountVO vo);
	
	/**
	 * <pre>
	 * update AuthAccount.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthAccountVO updateAuthAccount(AuthAccountVO vo);

	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthAccount(AuthAccountVO vo);
	
	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthAccountById(String id);
	
	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthAccountByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get AuthAccount by unique loginEmail
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthAccountVO getByLoginEmail(String loginEmail);
	
	

	
	/**
	 * <pre>
	 * page find assign AuthRoleVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAssignAuthRole(AuthAccountVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthRole by id
	 * </pre>
	 * @param ids AuthRole id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthRole(AuthAccountVO vo);
	
	/**
	 * <pre>
	 * editPassword
	 * </pre>
	 * @param vo EditPasswordVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void editPassword(EditPasswordVO vo);
}
