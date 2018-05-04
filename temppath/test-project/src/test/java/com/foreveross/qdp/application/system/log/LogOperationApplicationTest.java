/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import com.foreveross.qdp.application.system.log.LogOperationApplication;
import com.foreveross.qdp.infra.vo.system.log.LogOperationVO;

/**
 * Test for LogOperation.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class LogOperationApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/LogOperation.xml" };
	}

	@Test
	public void test_getLogOperation() {
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
	}
	
	@Test
	public void test_getLogOperationById(){
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
	}
	
	@Test
	public void test_pageFindLogOperation() {
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		LogOperationVO vo = new LogOperationVO();
		System.out.println(application.pageFindLogOperation(vo, page));
	}
	
	@Test
	public void test_addLogOperation() {
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
		LogOperationVO vo = new LogOperationVO();
		//application.addLogOperation(vo);
	}

	@Test
	public void test_updateLogOperation() {
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
		LogOperationVO vo = new LogOperationVO();
		//application.updateLogOperation(vo);
	}

	@Test
	public void test_removeLogOperation() {
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
	}
	
	@Test
	public void test_removeLogOperationById(){
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
	}
	
	@Test
	public void test_removeLogOperationByIds(){
		LogOperationApplication application = InstanceFactory.getInstance(LogOperationApplication.class);
	}
	
}
