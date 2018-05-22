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

import com.foreveross.qdp.application.system.common.SysScriptApplication;
import com.foreveross.qdp.infra.vo.system.common.SysScriptVO;

/**
 * Test for SysScript.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysScriptApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysScript.xml" };
	}

	@Test
	public void test_getSysScript() {
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
	}
	
	@Test
	public void test_getSysScriptById(){
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
	}
	
	@Test
	public void test_pageFindSysScript() {
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysScriptVO vo = new SysScriptVO();
		System.out.println(application.pageFindSysScript(vo, page));
	}
	
	@Test
	public void test_addSysScript() {
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
		SysScriptVO vo = new SysScriptVO();
		//application.addSysScript(vo);
	}

	@Test
	public void test_updateSysScript() {
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
		SysScriptVO vo = new SysScriptVO();
		//application.updateSysScript(vo);
	}

	@Test
	public void test_removeSysScript() {
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
	}
	
	@Test
	public void test_removeSysScriptById(){
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
	}
	
	@Test
	public void test_removeSysScriptByIds(){
		SysScriptApplication application = InstanceFactory.getInstance(SysScriptApplication.class);
	}
	
}
