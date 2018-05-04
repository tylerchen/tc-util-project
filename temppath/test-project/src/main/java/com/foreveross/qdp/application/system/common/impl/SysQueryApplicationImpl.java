/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import com.foreveross.qdp.application.system.common.SysQueryApplication;
import com.foreveross.qdp.infra.vo.system.common.SysQueryVO;
import com.foreveross.qdp.domain.system.common.SysQuery;

/**
 * SysQuery
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("sysQueryApplication")
@Transactional
public class SysQueryApplicationImpl implements SysQueryApplication {

	/**
	 * <pre>
	 * get SysQueryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#getSysQuery(SysQueryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysQueryVO getSysQuery(SysQueryVO vo) {
		SysQuery sysQuery = BeanHelper.copyProperties(SysQuery.class, vo);
		sysQuery = SysQuery.get(sysQuery);
		return BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
	}

	/**
	 * <pre>
	 * get SysQueryVO by id.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#getSysQueryById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysQueryVO getSysQueryById(String id){
		SysQuery sysQuery = SysQuery.get(id);
		return BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
	}
	
	/**
	 * <pre>
	 * page find SysQueryVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#pageFindSysQuery(SysQueryVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysQuery(SysQueryVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysQuery.pageFindSysQuery", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysQueryVO.class);
	}

	/**
	 * <pre>
	 * page find SysQuery Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#pageFindSysQueryMap(SysQueryVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysQueryMap(SysQueryVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysQuery.pageFindSysQueryMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysQueryVO.class);
	}

	/**
	 * <pre>
	 * add SysQuery.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#addSysQuery(SysQueryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysQueryVO addSysQuery(SysQueryVO vo) {
		SysQuery sysQuery = BeanHelper.copyProperties(SysQuery.class, vo);
		sysQuery.add();
		SysQueryVO sysQueryVO = BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
		return sysQueryVO;
	}

	/**
	 * <pre>
	 * update SysQuery.
	 * </pre>
	 * @param vo
	 * @return SysQueryVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#updateSysQuery(SysQueryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysQueryVO updateSysQuery(SysQueryVO vo) {
		SysQuery sysQuery = BeanHelper.copyProperties(SysQuery.class, vo);
		sysQuery.update();
		SysQueryVO sysQueryVO = BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
		return sysQueryVO;
	}
	
	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#removeSysQuery(SysQueryVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysQuery(SysQueryVO vo) {
		SysQuery sysQuery = BeanHelper.copyProperties(SysQuery.class, vo);
		SysQuery.remove(sysQuery);
	}

	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#removeSysQueryById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysQueryById(String id) {
		if( id instanceof String ) {
			SysQuery.remove(StringUtils.split(id, ','));
		} else {
			SysQuery.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove SysQuery.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#removeSysQueryByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysQueryByIds(String[] ids) {
		SysQuery.remove(ids);
	}
	
	
	/**
	 * <pre>
	 * get SysQuery by unique name
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#getByName(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysQueryVO getByName(String name) {
		SysQuery sysQuery = SysQuery.getByName(name);
		return BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
	}
	
	/**
	 * <pre>
	 * get SysQuery by unique code
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysQueryApplication#getByCode(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysQueryVO getByCode(String code) {
		SysQuery sysQuery = SysQuery.getByCode(code);
		return BeanHelper.copyProperties(SysQueryVO.class, sysQuery);
	}
	
	
	
}
