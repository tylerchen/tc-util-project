/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import com.foreveross.qdp.application.system.auth.AuthAccountApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthAccountVO;

/**
 * Test for AuthAccount.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthAccountApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthAccount.xml" };
	}

	@Test
	public void test_getAuthAccount() {
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
	}
	
	@Test
	public void test_getAuthAccountById(){
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
	}
	
	@Test
	public void test_pageFindAuthAccount() {
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthAccountVO vo = new AuthAccountVO();
		System.out.println(application.pageFindAuthAccount(vo, page));
	}
	
	@Test
	public void test_addAuthAccount() {
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
		AuthAccountVO vo = new AuthAccountVO();
		//application.addAuthAccount(vo);
	}

	@Test
	public void test_updateAuthAccount() {
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
		AuthAccountVO vo = new AuthAccountVO();
		//application.updateAuthAccount(vo);
	}

	@Test
	public void test_removeAuthAccount() {
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
	}
	
	@Test
	public void test_removeAuthAccountById(){
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
	}
	
	@Test
	public void test_removeAuthAccountByIds(){
		AuthAccountApplication application = InstanceFactory.getInstance(AuthAccountApplication.class);
	}
	
}
