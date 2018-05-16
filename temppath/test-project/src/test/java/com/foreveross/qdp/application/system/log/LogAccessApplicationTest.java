/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.log;

import java.util.Date;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.test.AbstractIntegratedTestCase;
import org.iff.infra.util.mybatis.plugin.Page;
import org.junit.Test;

import com.foreveross.qdp.application.system.log.LogAccessApplication;
import com.foreveross.qdp.infra.vo.system.log.LogAccessVO;

/**
 * Test for LogAccess.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class LogAccessApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/LogAccess.xml" };
	}

	@Test
	public void test_getLogAccess() {
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
	}
	
	@Test
	public void test_getLogAccessById(){
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
	}
	
	@Test
	public void test_pageFindLogAccess() {
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		LogAccessVO vo = new LogAccessVO();
		System.out.println(application.pageFindLogAccess(vo, page));
	}
	
	@Test
	public void test_addLogAccess() {
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
		LogAccessVO vo = new LogAccessVO();
		//application.addLogAccess(vo);
	}

	@Test
	public void test_updateLogAccess() {
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
		LogAccessVO vo = new LogAccessVO();
		//application.updateLogAccess(vo);
	}

	@Test
	public void test_removeLogAccess() {
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
	}
	
	@Test
	public void test_removeLogAccessById(){
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
	}
	
	@Test
	public void test_removeLogAccessByIds(){
		LogAccessApplication application = InstanceFactory.getInstance(LogAccessApplication.class);
	}
	
}
