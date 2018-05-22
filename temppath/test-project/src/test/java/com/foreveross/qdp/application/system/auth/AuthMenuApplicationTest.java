/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.auth;

import java.util.Date;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.test.AbstractIntegratedTestCase;
import org.iff.infra.util.mybatis.plugin.Page;
import org.junit.Test;

import com.foreveross.qdp.application.system.auth.AuthMenuApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthMenuVO;

/**
 * Test for AuthMenu.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthMenuApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthMenu.xml" };
	}

	@Test
	public void test_getAuthMenu() {
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
	}
	
	@Test
	public void test_getAuthMenuById(){
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
	}
	
	@Test
	public void test_pageFindAuthMenu() {
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthMenuVO vo = new AuthMenuVO();
		System.out.println(application.pageFindAuthMenu(vo, page));
	}
	
	@Test
	public void test_addAuthMenu() {
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
		AuthMenuVO vo = new AuthMenuVO();
		//application.addAuthMenu(vo);
	}

	@Test
	public void test_updateAuthMenu() {
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
		AuthMenuVO vo = new AuthMenuVO();
		//application.updateAuthMenu(vo);
	}

	@Test
	public void test_removeAuthMenu() {
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
	}
	
	@Test
	public void test_removeAuthMenuById(){
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
	}
	
	@Test
	public void test_removeAuthMenuByIds(){
		AuthMenuApplication application = InstanceFactory.getInstance(AuthMenuApplication.class);
	}
	
}
