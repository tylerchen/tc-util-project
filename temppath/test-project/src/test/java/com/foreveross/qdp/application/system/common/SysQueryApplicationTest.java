/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.common;

import java.util.Date;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.test.AbstractIntegratedTestCase;
import org.iff.infra.util.mybatis.plugin.Page;
import org.junit.Test;

import com.foreveross.qdp.application.system.common.SysQueryApplication;
import com.foreveross.qdp.infra.vo.system.common.SysQueryVO;

/**
 * Test for SysQuery.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysQueryApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysQuery.xml" };
	}

	@Test
	public void test_getSysQuery() {
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
	}
	
	@Test
	public void test_getSysQueryById(){
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
	}
	
	@Test
	public void test_pageFindSysQuery() {
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysQueryVO vo = new SysQueryVO();
		System.out.println(application.pageFindSysQuery(vo, page));
	}
	
	@Test
	public void test_addSysQuery() {
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
		SysQueryVO vo = new SysQueryVO();
		//application.addSysQuery(vo);
	}

	@Test
	public void test_updateSysQuery() {
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
		SysQueryVO vo = new SysQueryVO();
		//application.updateSysQuery(vo);
	}

	@Test
	public void test_removeSysQuery() {
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
	}
	
	@Test
	public void test_removeSysQueryById(){
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
	}
	
	@Test
	public void test_removeSysQueryByIds(){
		SysQueryApplication application = InstanceFactory.getInstance(SysQueryApplication.class);
	}
	
}
