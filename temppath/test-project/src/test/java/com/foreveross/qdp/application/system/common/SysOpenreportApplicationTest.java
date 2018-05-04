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

import com.foreveross.qdp.application.system.common.SysOpenreportApplication;
import com.foreveross.qdp.infra.vo.system.common.SysOpenreportVO;

/**
 * Test for SysOpenreport.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysOpenreportApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysOpenreport.xml" };
	}

	@Test
	public void test_getSysOpenreport() {
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
	}
	
	@Test
	public void test_getSysOpenreportById(){
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
	}
	
	@Test
	public void test_pageFindSysOpenreport() {
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysOpenreportVO vo = new SysOpenreportVO();
		System.out.println(application.pageFindSysOpenreport(vo, page));
	}
	
	@Test
	public void test_addSysOpenreport() {
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
		SysOpenreportVO vo = new SysOpenreportVO();
		//application.addSysOpenreport(vo);
	}

	@Test
	public void test_updateSysOpenreport() {
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
		SysOpenreportVO vo = new SysOpenreportVO();
		//application.updateSysOpenreport(vo);
	}

	@Test
	public void test_removeSysOpenreport() {
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
	}
	
	@Test
	public void test_removeSysOpenreportById(){
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
	}
	
	@Test
	public void test_removeSysOpenreportByIds(){
		SysOpenreportApplication application = InstanceFactory.getInstance(SysOpenreportApplication.class);
	}
	
}
