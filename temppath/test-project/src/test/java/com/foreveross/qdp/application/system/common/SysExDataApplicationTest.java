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

import com.foreveross.qdp.application.system.common.SysExDataApplication;
import com.foreveross.qdp.infra.vo.system.common.SysExDataVO;

/**
 * Test for SysExData.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysExDataApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysExData.xml" };
	}

	@Test
	public void test_getSysExData() {
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
	}
	
	@Test
	public void test_getSysExDataById(){
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
	}
	
	@Test
	public void test_pageFindSysExData() {
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysExDataVO vo = new SysExDataVO();
		System.out.println(application.pageFindSysExData(vo, page));
	}
	
	@Test
	public void test_addSysExData() {
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
		SysExDataVO vo = new SysExDataVO();
		//application.addSysExData(vo);
	}

	@Test
	public void test_updateSysExData() {
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
		SysExDataVO vo = new SysExDataVO();
		//application.updateSysExData(vo);
	}

	@Test
	public void test_removeSysExData() {
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
	}
	
	@Test
	public void test_removeSysExDataById(){
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
	}
	
	@Test
	public void test_removeSysExDataByIds(){
		SysExDataApplication application = InstanceFactory.getInstance(SysExDataApplication.class);
	}
	
}
