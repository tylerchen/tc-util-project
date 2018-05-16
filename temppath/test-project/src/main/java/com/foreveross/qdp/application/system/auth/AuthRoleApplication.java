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
import com.foreveross.qdp.infra.vo.system.auth.AuthRoleVO;

/**
 * AuthRole Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface AuthRoleApplication {

	/**
	 * <pre>
	 * get AuthRoleVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthRoleVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthRoleVO getAuthRole(AuthRoleVO vo);

	/**
	 * <pre>
	 * get AuthRoleVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthRoleVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthRoleVO getAuthRoleById(String id);
	
	/**
	 * <pre>
	 * page find AuthRoleVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthRole(AuthRoleVO vo, Page page);
	
	/**
	 * <pre>
	 * page find AuthRole Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthRoleMap(AuthRoleVO vo, Page page);

	/**
	 * <pre>
	 * add AuthRole.
	 * </pre>
	 * @param vo
	 * @return AuthRoleVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthRoleVO addAuthRole(AuthRoleVO vo);
	
	/**
	 * <pre>
	 * update AuthRole.
	 * </pre>
	 * @param vo
	 * @return AuthRoleVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthRoleVO updateAuthRole(AuthRoleVO vo);

	/**
	 * <pre>
	 * remove AuthRole.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthRole(AuthRoleVO vo);
	
	/**
	 * <pre>
	 * remove AuthRole.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthRoleById(String id);
	
	/**
	 * <pre>
	 * remove AuthRole.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthRoleByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get AuthRole by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthRoleVO getByName(String name);
	
	/**
	 * <pre>
	 * get AuthRole by unique code
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthRoleVO getByCode(String code);
	
	

	
	/**
	 * <pre>
	 * page find assign AuthResourceVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAssignAuthResource(AuthRoleVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthResource by id
	 * </pre>
	 * @param ids AuthResource id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthResource(AuthRoleVO vo);

	
	/**
	 * <pre>
	 * page find assign AuthMenuVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAssignAuthMenu(AuthRoleVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthMenu by id
	 * </pre>
	 * @param ids AuthMenu id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthMenu(AuthRoleVO vo);
	
}
