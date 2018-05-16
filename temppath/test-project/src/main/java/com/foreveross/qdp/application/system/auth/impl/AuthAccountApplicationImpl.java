/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.auth.impl;

import java.util.List;
import javax.inject.Named;

import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.foreveross.qdp.application.system.auth.AuthAccountApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthAccountVO;
import com.foreveross.qdp.infra.vo.system.auth.EditPasswordVO;

import com.foreveross.qdp.infra.vo.system.auth.AuthRoleVO;
import com.foreveross.qdp.domain.system.auth.AuthAccount;

import com.foreveross.qdp.domain.system.auth.AuthRole;

/**
 * AuthAccount
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("authAccountApplication")
@Transactional
public class AuthAccountApplicationImpl implements AuthAccountApplication {

	/**
	 * <pre>
	 * get AuthAccountVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#getAuthAccount(AuthAccountVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public AuthAccountVO getAuthAccount(AuthAccountVO vo) {
		AuthAccount authAccount = BeanHelper.copyProperties(AuthAccount.class, vo);
		authAccount = AuthAccount.get(authAccount);
		return BeanHelper.copyProperties(AuthAccountVO.class, authAccount);
	}

	/**
	 * <pre>
	 * get AuthAccountVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#getAuthAccountById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public AuthAccountVO getAuthAccountById(String id){
		AuthAccount authAccount = AuthAccount.get(id);
		return BeanHelper.copyProperties(AuthAccountVO.class, authAccount);
	}
	
	/**
	 * <pre>
	 * page find AuthAccountVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#pageFindAuthAccount(AuthAccountVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAuthAccount(AuthAccountVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("AuthAccount.pageFindAuthAccount", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(AuthAccountVO.class);
	}

	/**
	 * <pre>
	 * page find AuthAccount Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#pageFindAuthAccountMap(AuthAccountVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAuthAccountMap(AuthAccountVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("AuthAccount.pageFindAuthAccountMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(AuthAccountVO.class);
	}

	/**
	 * <pre>
	 * add AuthAccount.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#addAuthAccount(AuthAccountVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public AuthAccountVO addAuthAccount(AuthAccountVO vo) {
		AuthAccount authAccount = BeanHelper.copyProperties(AuthAccount.class, vo);
		authAccount.add();

		// 关联中间表设值: 分配角色管理，当值为null时，表示不更新该字段，如果设置为长度为0，则表示清空所有分配值。
		if(vo.getRoleId() != null) {
			authAccount.assignAuthRoleByIds(StringUtils.split(vo.getRoleId(), ','));
		}
		AuthAccountVO authAccountVO = BeanHelper.copyProperties(AuthAccountVO.class, authAccount);
		return authAccountVO;
	}

	/**
	 * <pre>
	 * update AuthAccount.
	 * </pre>
	 * @param vo
	 * @return AuthAccountVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#updateAuthAccount(AuthAccountVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public AuthAccountVO updateAuthAccount(AuthAccountVO vo) {
		AuthAccount authAccount = BeanHelper.copyProperties(AuthAccount.class, vo);
		authAccount.update();

		// 关联中间表设值: 分配角色管理，当值为null时，表示不更新该字段，如果设置为长度为0，则表示清空所有分配值。
		if(vo.getRoleId() != null) {
			authAccount.assignAuthRoleByIds(StringUtils.split(vo.getRoleId(), ','));
		}
		AuthAccountVO authAccountVO = BeanHelper.copyProperties(AuthAccountVO.class, authAccount);
		return authAccountVO;
	}
	
	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#removeAuthAccount(AuthAccountVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthAccount(AuthAccountVO vo) {
		AuthAccount authAccount = BeanHelper.copyProperties(AuthAccount.class, vo);
		AuthAccount.remove(authAccount);
	}

	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#removeAuthAccountById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthAccountById(String id) {
		if( id instanceof String ) {
			AuthAccount.remove(StringUtils.split(id, ','));
		} else {
			AuthAccount.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove AuthAccount.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#removeAuthAccountByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthAccountByIds(String[] ids) {
		AuthAccount.remove(ids);
	}
	
	
	/**
	 * <pre>
	 * get AuthAccount by unique loginEmail
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#getByLoginEmail(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAccountVO getByLoginEmail(String loginEmail) {
		AuthAccount authAccount = AuthAccount.getByLoginEmail(loginEmail);
		return BeanHelper.copyProperties(AuthAccountVO.class, authAccount);
	}
	
	

	
	/**
	 * <pre>
	 * page find assign AuthRoleVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#pageFindAssignAuthRole(AuthAccountVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAssignAuthRole(AuthAccountVO vo, Page page) {
		page = Dao.queryPage("AuthAccount.pageFindAuthRoleByAuthAutherRoleMap", MapHelper.toMap("page", Page.notNullPage(page), "vo", vo));
		return page.toPage(AuthRoleVO.class);
	}
	
	/**
	 * <pre>
	 * assign AuthRole by id
	 * </pre>
	 * @param ids AuthRole id
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#assignAuthRole(AuthAccountVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void assignAuthRole(AuthAccountVO vo) {
		AuthAccount domain = BeanHelper.copyProperties(AuthAccount.class, vo);
		domain.assignAuthRoleByIds(StringUtils.split(vo.getRoleId(), ","));
	}
	
	/**
	 * <pre>
	 * editPassword
	 * </pre>
	 * @param vo EditPasswordVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthAccountApplication#editPassword(EditPasswordVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void editPassword(EditPasswordVO vo) {
		//TODO
		AuthAccount domain = AuthAccount.get(vo.getId());
		Assert.notNull(domain);
		Assert.notBlank(vo.getLoginPasswd());
		domain.setLoginPasswd(vo.getLoginPasswd());
		domain.update();
	}
}
