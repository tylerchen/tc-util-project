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

import com.foreveross.qdp.application.system.auth.AuthUserApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthUserVO;

/**
 * Test for AuthUser.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthUserApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthUser.xml" };
	}

	@Test
	public void test_getAuthUser() {
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
	}
	
	@Test
	public void test_getAuthUserById(){
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
	}
	
	@Test
	public void test_pageFindAuthUser() {
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthUserVO vo = new AuthUserVO();
		System.out.println(application.pageFindAuthUser(vo, page));
	}
	
	@Test
	public void test_addAuthUser() {
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
		AuthUserVO vo = new AuthUserVO();
		//application.addAuthUser(vo);
	}

	@Test
	public void test_updateAuthUser() {
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
		AuthUserVO vo = new AuthUserVO();
		//application.updateAuthUser(vo);
	}

	@Test
	public void test_removeAuthUser() {
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
	}
	
	@Test
	public void test_removeAuthUserById(){
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
	}
	
	@Test
	public void test_removeAuthUserByIds(){
		AuthUserApplication application = InstanceFactory.getInstance(AuthUserApplication.class);
	}
	
}
