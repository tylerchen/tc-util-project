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

import com.foreveross.qdp.application.system.common.SysDictionaryApplication;
import com.foreveross.qdp.infra.vo.system.common.SysDictionaryVO;

/**
 * Test for SysDictionary.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class SysDictionaryApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/SysDictionary.xml" };
	}

	@Test
	public void test_getSysDictionary() {
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
	}
	
	@Test
	public void test_getSysDictionaryById(){
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
	}
	
	@Test
	public void test_pageFindSysDictionary() {
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
		Page page = Page.pageable(10, 1, 0, null);
		SysDictionaryVO vo = new SysDictionaryVO();
		System.out.println(application.pageFindSysDictionary(vo, page));
	}
	
	@Test
	public void test_addSysDictionary() {
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
		SysDictionaryVO vo = new SysDictionaryVO();
		//application.addSysDictionary(vo);
	}

	@Test
	public void test_updateSysDictionary() {
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
		SysDictionaryVO vo = new SysDictionaryVO();
		//application.updateSysDictionary(vo);
	}

	@Test
	public void test_removeSysDictionary() {
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
	}
	
	@Test
	public void test_removeSysDictionaryById(){
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
	}
	
	@Test
	public void test_removeSysDictionaryByIds(){
		SysDictionaryApplication application = InstanceFactory.getInstance(SysDictionaryApplication.class);
	}
	
}
