/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import com.foreveross.qdp.application.system.common.SysExTableApplication;
import com.foreveross.qdp.infra.vo.system.common.SysExTableVO;

/**
 * Test for SysExTable.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysExTableApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysExTable.xml" };
	}

	@Test
	public void test_getSysExTable() {
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
	}
	
	@Test
	public void test_getSysExTableById(){
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
	}
	
	@Test
	public void test_pageFindSysExTable() {
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysExTableVO vo = new SysExTableVO();
		System.out.println(application.pageFindSysExTable(vo, page));
	}
	
	@Test
	public void test_addSysExTable() {
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
		SysExTableVO vo = new SysExTableVO();
		//application.addSysExTable(vo);
	}

	@Test
	public void test_updateSysExTable() {
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
		SysExTableVO vo = new SysExTableVO();
		//application.updateSysExTable(vo);
	}

	@Test
	public void test_removeSysExTable() {
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
	}
	
	@Test
	public void test_removeSysExTableById(){
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
	}
	
	@Test
	public void test_removeSysExTableByIds(){
		SysExTableApplication application = InstanceFactory.getInstance(SysExTableApplication.class);
	}
	
}
