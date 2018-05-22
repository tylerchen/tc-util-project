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

import com.foreveross.qdp.application.system.auth.AuthOrganizationApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthOrganizationVO;

/**
 * Test for AuthOrganization.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthOrganizationApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthOrganization.xml" };
	}

	@Test
	public void test_getAuthOrganization() {
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
	}
	
	@Test
	public void test_getAuthOrganizationById(){
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
	}
	
	@Test
	public void test_pageFindAuthOrganization() {
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthOrganizationVO vo = new AuthOrganizationVO();
		System.out.println(application.pageFindAuthOrganization(vo, page));
	}
	
	@Test
	public void test_addAuthOrganization() {
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
		AuthOrganizationVO vo = new AuthOrganizationVO();
		//application.addAuthOrganization(vo);
	}

	@Test
	public void test_updateAuthOrganization() {
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
		AuthOrganizationVO vo = new AuthOrganizationVO();
		//application.updateAuthOrganization(vo);
	}

	@Test
	public void test_removeAuthOrganization() {
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
	}
	
	@Test
	public void test_removeAuthOrganizationById(){
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
	}
	
	@Test
	public void test_removeAuthOrganizationByIds(){
		AuthOrganizationApplication application = InstanceFactory.getInstance(AuthOrganizationApplication.class);
	}
	
}
