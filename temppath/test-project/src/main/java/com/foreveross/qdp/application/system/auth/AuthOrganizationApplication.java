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
import com.foreveross.qdp.infra.vo.system.auth.AuthOrganizationVO;

/**
 * AuthOrganization Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public interface AuthOrganizationApplication {

	/**
	 * <pre>
	 * get AuthOrganizationVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthOrganizationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthOrganizationVO getAuthOrganization(AuthOrganizationVO vo);

	/**
	 * <pre>
	 * get AuthOrganizationVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthOrganizationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthOrganizationVO getAuthOrganizationById(String id);
	
	/**
	 * <pre>
	 * page find AuthOrganizationVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthOrganization(AuthOrganizationVO vo, Page page);
	
	/**
	 * <pre>
	 * page find AuthOrganization Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAuthOrganizationMap(AuthOrganizationVO vo, Page page);

	/**
	 * <pre>
	 * add AuthOrganization.
	 * </pre>
	 * @param vo
	 * @return AuthOrganizationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthOrganizationVO addAuthOrganization(AuthOrganizationVO vo);
	
	/**
	 * <pre>
	 * update AuthOrganization.
	 * </pre>
	 * @param vo
	 * @return AuthOrganizationVO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	AuthOrganizationVO updateAuthOrganization(AuthOrganizationVO vo);

	/**
	 * <pre>
	 * remove AuthOrganization.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthOrganization(AuthOrganizationVO vo);
	
	/**
	 * <pre>
	 * remove AuthOrganization.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthOrganizationById(String id);
	
	/**
	 * <pre>
	 * remove AuthOrganization.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void removeAuthOrganizationByIds(String[] ids);
	
	
	/**
	 * <pre>
	 * get AuthOrganization by unique name
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthOrganizationVO getByName(String name);
	
	/**
	 * <pre>
	 * get AuthOrganization by unique code
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	AuthOrganizationVO getByCode(String code);
	
	

	
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
	Page pageFindAssignAuthMenu(AuthOrganizationVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthMenu by id
	 * </pre>
	 * @param ids AuthMenu id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthMenu(AuthOrganizationVO vo);

	
	/**
	 * <pre>
	 * page find assign AuthUserVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	Page pageFindAssignAuthUser(AuthOrganizationVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthUser by id
	 * </pre>
	 * @param ids AuthUser id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthUser(AuthOrganizationVO vo);

	
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
	Page pageFindAssignAuthRole(AuthOrganizationVO vo, Page page);
	
	/**
	 * <pre>
	 * assign AuthRole by id
	 * </pre>
	 * @param ids AuthRole id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	void assignAuthRole(AuthOrganizationVO vo);
	
}
