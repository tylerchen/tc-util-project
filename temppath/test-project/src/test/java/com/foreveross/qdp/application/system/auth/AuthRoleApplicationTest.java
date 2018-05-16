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

import com.foreveross.qdp.application.system.auth.AuthRoleApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthRoleVO;

/**
 * Test for AuthRole.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthRoleApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthRole.xml" };
	}

	@Test
	public void test_getAuthRole() {
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
	}
	
	@Test
	public void test_getAuthRoleById(){
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
	}
	
	@Test
	public void test_pageFindAuthRole() {
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthRoleVO vo = new AuthRoleVO();
		System.out.println(application.pageFindAuthRole(vo, page));
	}
	
	@Test
	public void test_addAuthRole() {
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
		AuthRoleVO vo = new AuthRoleVO();
		//application.addAuthRole(vo);
	}

	@Test
	public void test_updateAuthRole() {
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
		AuthRoleVO vo = new AuthRoleVO();
		//application.updateAuthRole(vo);
	}

	@Test
	public void test_removeAuthRole() {
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
	}
	
	@Test
	public void test_removeAuthRoleById(){
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
	}
	
	@Test
	public void test_removeAuthRoleByIds(){
		AuthRoleApplication application = InstanceFactory.getInstance(AuthRoleApplication.class);
	}
	
}
