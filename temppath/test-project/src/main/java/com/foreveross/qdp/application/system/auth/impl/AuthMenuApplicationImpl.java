/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import com.foreveross.qdp.application.system.auth.AuthMenuApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthMenuVO;
import com.foreveross.qdp.domain.system.auth.AuthMenu;

/**
 * AuthMenu
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("authMenuApplication")
@Transactional
public class AuthMenuApplicationImpl implements AuthMenuApplication {

	/**
	 * <pre>
	 * get AuthMenuVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthMenuVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#getAuthMenu(AuthMenuVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public AuthMenuVO getAuthMenu(AuthMenuVO vo) {
		AuthMenu authMenu = BeanHelper.copyProperties(AuthMenu.class, vo);
		authMenu = AuthMenu.get(authMenu);
		return BeanHelper.copyProperties(AuthMenuVO.class, authMenu);
	}

	/**
	 * <pre>
	 * get AuthMenuVO by id.
	 * </pre>
	 * @param vo
	 * @return AuthMenuVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#getAuthMenuById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public AuthMenuVO getAuthMenuById(String id){
		AuthMenu authMenu = AuthMenu.get(id);
		return BeanHelper.copyProperties(AuthMenuVO.class, authMenu);
	}
	
	/**
	 * <pre>
	 * page find AuthMenuVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#pageFindAuthMenu(AuthMenuVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAuthMenu(AuthMenuVO vo, Page page) {
		page = Page.notNullPage(page);
		// 添加树节点的排序
		page.addAscOrderBy("menuIndex");
		page = Dao.queryPage("AuthMenu.pageFindAuthMenu", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(AuthMenuVO.class);
	}

	/**
	 * <pre>
	 * page find AuthMenu Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#pageFindAuthMenuMap(AuthMenuVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAuthMenuMap(AuthMenuVO vo, Page page) {
		page = Page.notNullPage(page);
		// 添加树节点的排序
		page.addAscOrderBy("menuIndex");
		page = Dao.queryPage("AuthMenu.pageFindAuthMenuMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(AuthMenuVO.class);
	}

	/**
	 * <pre>
	 * add AuthMenu.
	 * </pre>
	 * @param vo
	 * @return AuthMenuVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#addAuthMenu(AuthMenuVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public AuthMenuVO addAuthMenu(AuthMenuVO vo) {
		AuthMenu authMenu = BeanHelper.copyProperties(AuthMenu.class, vo);
		authMenu.add();
		AuthMenuVO authMenuVO = BeanHelper.copyProperties(AuthMenuVO.class, authMenu);
		return authMenuVO;
	}

	/**
	 * <pre>
	 * update AuthMenu.
	 * </pre>
	 * @param vo
	 * @return AuthMenuVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#updateAuthMenu(AuthMenuVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public AuthMenuVO updateAuthMenu(AuthMenuVO vo) {
		AuthMenu authMenu = BeanHelper.copyProperties(AuthMenu.class, vo);
		authMenu.update();
		AuthMenuVO authMenuVO = BeanHelper.copyProperties(AuthMenuVO.class, authMenu);
		return authMenuVO;
	}
	
	/**
	 * <pre>
	 * remove AuthMenu.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#removeAuthMenu(AuthMenuVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthMenu(AuthMenuVO vo) {
		AuthMenu authMenu = BeanHelper.copyProperties(AuthMenu.class, vo);
		AuthMenu.remove(authMenu);
	}

	/**
	 * <pre>
	 * remove AuthMenu.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#removeAuthMenuById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthMenuById(String id) {
		if( id instanceof String ) {
			AuthMenu.remove(StringUtils.split(id, ','));
		} else {
			AuthMenu.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove AuthMenu.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#removeAuthMenuByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeAuthMenuByIds(String[] ids) {
		AuthMenu.remove(ids);
	}
	
	
	/**
	 * <pre>
	 * get AuthMenu by unique name
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.auth.AuthMenuApplication#getByName(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthMenuVO getByName(String name) {
		AuthMenu authMenu = AuthMenu.getByName(name);
		return BeanHelper.copyProperties(AuthMenuVO.class, authMenu);
	}
	
	
	
}
