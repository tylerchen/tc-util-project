/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.common.impl;

import java.util.List;
import javax.inject.Named;

import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.foreveross.qdp.application.system.common.SysDictionaryApplication;
import com.foreveross.qdp.infra.vo.system.common.SysDictionaryVO;
import com.foreveross.qdp.domain.system.common.SysDictionary;

/**
 * SysDictionary
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("sysDictionaryApplication")
@Transactional
public class SysDictionaryApplicationImpl implements SysDictionaryApplication {

	/**
	 * <pre>
	 * get SysDictionaryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysDictionaryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#getSysDictionary(SysDictionaryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public SysDictionaryVO getSysDictionary(SysDictionaryVO vo) {
		SysDictionary sysDictionary = BeanHelper.copyProperties(SysDictionary.class, vo);
		sysDictionary = SysDictionary.get(sysDictionary);
		return BeanHelper.copyProperties(SysDictionaryVO.class, sysDictionary);
	}

	/**
	 * <pre>
	 * get SysDictionaryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysDictionaryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#getSysDictionaryById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public SysDictionaryVO getSysDictionaryById(String id){
		SysDictionary sysDictionary = SysDictionary.get(id);
		return BeanHelper.copyProperties(SysDictionaryVO.class, sysDictionary);
	}
	
	/**
	 * <pre>
	 * page find SysDictionaryVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#pageFindSysDictionary(SysDictionaryVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysDictionary(SysDictionaryVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysDictionary.pageFindSysDictionary", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysDictionaryVO.class);
	}

	/**
	 * <pre>
	 * page find SysDictionary Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#pageFindSysDictionaryMap(SysDictionaryVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysDictionaryMap(SysDictionaryVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysDictionary.pageFindSysDictionaryMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysDictionaryVO.class);
	}

	/**
	 * <pre>
	 * add SysDictionary.
	 * </pre>
	 * @param vo
	 * @return SysDictionaryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#addSysDictionary(SysDictionaryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public SysDictionaryVO addSysDictionary(SysDictionaryVO vo) {
		SysDictionary sysDictionary = BeanHelper.copyProperties(SysDictionary.class, vo);
		sysDictionary.add();
		SysDictionaryVO sysDictionaryVO = BeanHelper.copyProperties(SysDictionaryVO.class, sysDictionary);
		return sysDictionaryVO;
	}

	/**
	 * <pre>
	 * update SysDictionary.
	 * </pre>
	 * @param vo
	 * @return SysDictionaryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#updateSysDictionary(SysDictionaryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public SysDictionaryVO updateSysDictionary(SysDictionaryVO vo) {
		SysDictionary sysDictionary = BeanHelper.copyProperties(SysDictionary.class, vo);
		sysDictionary.update();
		SysDictionaryVO sysDictionaryVO = BeanHelper.copyProperties(SysDictionaryVO.class, sysDictionary);
		return sysDictionaryVO;
	}
	
	/**
	 * <pre>
	 * remove SysDictionary.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#removeSysDictionary(SysDictionaryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeSysDictionary(SysDictionaryVO vo) {
		SysDictionary sysDictionary = BeanHelper.copyProperties(SysDictionary.class, vo);
		SysDictionary.remove(sysDictionary);
	}

	/**
	 * <pre>
	 * remove SysDictionary.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#removeSysDictionaryById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeSysDictionaryById(String id) {
		if( id instanceof String ) {
			SysDictionary.remove(StringUtils.split(id, ','));
		} else {
			SysDictionary.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove SysDictionary.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysDictionaryApplication#removeSysDictionaryByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeSysDictionaryByIds(String[] ids) {
		SysDictionary.remove(ids);
	}
	
	
	
	
}
