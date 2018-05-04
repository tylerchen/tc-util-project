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

import com.foreveross.qdp.application.system.common.SysExDataApplication;
import com.foreveross.qdp.infra.vo.system.common.SysExDataVO;
import com.foreveross.qdp.domain.system.common.SysExData;

/**
 * SysExData
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("sysExDataApplication")
@Transactional
public class SysExDataApplicationImpl implements SysExDataApplication {

	/**
	 * <pre>
	 * get SysExDataVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#getSysExData(SysExDataVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysExDataVO getSysExData(SysExDataVO vo) {
		SysExData sysExData = BeanHelper.copyProperties(SysExData.class, vo);
		sysExData = SysExData.get(sysExData);
		return BeanHelper.copyProperties(SysExDataVO.class, sysExData);
	}

	/**
	 * <pre>
	 * get SysExDataVO by id.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#getSysExDataById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysExDataVO getSysExDataById(String id){
		SysExData sysExData = SysExData.get(id);
		return BeanHelper.copyProperties(SysExDataVO.class, sysExData);
	}
	
	/**
	 * <pre>
	 * page find SysExDataVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#pageFindSysExData(SysExDataVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysExData(SysExDataVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysExData.pageFindSysExData", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysExDataVO.class);
	}

	/**
	 * <pre>
	 * page find SysExData Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#pageFindSysExDataMap(SysExDataVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysExDataMap(SysExDataVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysExData.pageFindSysExDataMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysExDataVO.class);
	}

	/**
	 * <pre>
	 * add SysExData.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#addSysExData(SysExDataVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysExDataVO addSysExData(SysExDataVO vo) {
		SysExData sysExData = BeanHelper.copyProperties(SysExData.class, vo);
		sysExData.add();
		SysExDataVO sysExDataVO = BeanHelper.copyProperties(SysExDataVO.class, sysExData);
		return sysExDataVO;
	}

	/**
	 * <pre>
	 * update SysExData.
	 * </pre>
	 * @param vo
	 * @return SysExDataVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#updateSysExData(SysExDataVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysExDataVO updateSysExData(SysExDataVO vo) {
		SysExData sysExData = BeanHelper.copyProperties(SysExData.class, vo);
		sysExData.update();
		SysExDataVO sysExDataVO = BeanHelper.copyProperties(SysExDataVO.class, sysExData);
		return sysExDataVO;
	}
	
	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#removeSysExData(SysExDataVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysExData(SysExDataVO vo) {
		SysExData sysExData = BeanHelper.copyProperties(SysExData.class, vo);
		SysExData.remove(sysExData);
	}

	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#removeSysExDataById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysExDataById(String id) {
		if( id instanceof String ) {
			SysExData.remove(StringUtils.split(id, ','));
		} else {
			SysExData.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove SysExData.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysExDataApplication#removeSysExDataByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysExDataByIds(String[] ids) {
		SysExData.remove(ids);
	}
	
	
	
	
}
