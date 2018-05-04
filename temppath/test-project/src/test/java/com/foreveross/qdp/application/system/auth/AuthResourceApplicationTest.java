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

import com.foreveross.qdp.application.system.auth.AuthResourceApplication;
import com.foreveross.qdp.infra.vo.system.auth.AuthResourceVO;

/**
 * Test for AuthResource.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class AuthResourceApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/AuthResource.xml" };
	}

	@Test
	public void test_getAuthResource() {
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
	}
	
	@Test
	public void test_getAuthResourceById(){
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
	}
	
	@Test
	public void test_pageFindAuthResource() {
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		AuthResourceVO vo = new AuthResourceVO();
		System.out.println(application.pageFindAuthResource(vo, page));
	}
	
	@Test
	public void test_addAuthResource() {
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
		AuthResourceVO vo = new AuthResourceVO();
		//application.addAuthResource(vo);
	}

	@Test
	public void test_updateAuthResource() {
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
		AuthResourceVO vo = new AuthResourceVO();
		//application.updateAuthResource(vo);
	}

	@Test
	public void test_removeAuthResource() {
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
	}
	
	@Test
	public void test_removeAuthResourceById(){
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
	}
	
	@Test
	public void test_removeAuthResourceByIds(){
		AuthResourceApplication application = InstanceFactory.getInstance(AuthResourceApplication.class);
	}
	
}
