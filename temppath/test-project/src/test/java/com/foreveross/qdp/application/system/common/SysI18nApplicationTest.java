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

import com.foreveross.qdp.application.system.common.SysI18nApplication;
import com.foreveross.qdp.infra.vo.system.common.SysI18nVO;

/**
 * Test for SysI18n.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysI18nApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysI18n.xml" };
	}

	@Test
	public void test_getSysI18n() {
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
	}
	
	@Test
	public void test_getSysI18nById(){
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
	}
	
	@Test
	public void test_pageFindSysI18n() {
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysI18nVO vo = new SysI18nVO();
		System.out.println(application.pageFindSysI18n(vo, page));
	}
	
	@Test
	public void test_addSysI18n() {
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
		SysI18nVO vo = new SysI18nVO();
		//application.addSysI18n(vo);
	}

	@Test
	public void test_updateSysI18n() {
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
		SysI18nVO vo = new SysI18nVO();
		//application.updateSysI18n(vo);
	}

	@Test
	public void test_removeSysI18n() {
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
	}
	
	@Test
	public void test_removeSysI18nById(){
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
	}
	
	@Test
	public void test_removeSysI18nByIds(){
		SysI18nApplication application = InstanceFactory.getInstance(SysI18nApplication.class);
	}
	
}
