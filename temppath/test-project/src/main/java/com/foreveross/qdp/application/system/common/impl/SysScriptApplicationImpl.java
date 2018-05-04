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

import com.foreveross.qdp.application.system.common.SysScriptApplication;
import com.foreveross.qdp.infra.vo.system.common.SysScriptVO;
import com.foreveross.qdp.domain.system.common.SysScript;

/**
 * SysScript
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("sysScriptApplication")
@Transactional
public class SysScriptApplicationImpl implements SysScriptApplication {

	/**
	 * <pre>
	 * get SysScriptVO by id.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#getSysScript(SysScriptVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysScriptVO getSysScript(SysScriptVO vo) {
		SysScript sysScript = BeanHelper.copyProperties(SysScript.class, vo);
		sysScript = SysScript.get(sysScript);
		return BeanHelper.copyProperties(SysScriptVO.class, sysScript);
	}

	/**
	 * <pre>
	 * get SysScriptVO by id.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#getSysScriptById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysScriptVO getSysScriptById(String id){
		SysScript sysScript = SysScript.get(id);
		return BeanHelper.copyProperties(SysScriptVO.class, sysScript);
	}
	
	/**
	 * <pre>
	 * page find SysScriptVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#pageFindSysScript(SysScriptVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysScript(SysScriptVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysScript.pageFindSysScript", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysScriptVO.class);
	}

	/**
	 * <pre>
	 * page find SysScript Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#pageFindSysScriptMap(SysScriptVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindSysScriptMap(SysScriptVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("SysScript.pageFindSysScriptMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(SysScriptVO.class);
	}

	/**
	 * <pre>
	 * add SysScript.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#addSysScript(SysScriptVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysScriptVO addSysScript(SysScriptVO vo) {
		SysScript sysScript = BeanHelper.copyProperties(SysScript.class, vo);
		sysScript.add();
		SysScriptVO sysScriptVO = BeanHelper.copyProperties(SysScriptVO.class, sysScript);
		return sysScriptVO;
	}

	/**
	 * <pre>
	 * update SysScript.
	 * </pre>
	 * @param vo
	 * @return SysScriptVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#updateSysScript(SysScriptVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public SysScriptVO updateSysScript(SysScriptVO vo) {
		SysScript sysScript = BeanHelper.copyProperties(SysScript.class, vo);
		sysScript.update();
		SysScriptVO sysScriptVO = BeanHelper.copyProperties(SysScriptVO.class, sysScript);
		return sysScriptVO;
	}
	
	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#removeSysScript(SysScriptVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysScript(SysScriptVO vo) {
		SysScript sysScript = BeanHelper.copyProperties(SysScript.class, vo);
		SysScript.remove(sysScript);
	}

	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#removeSysScriptById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysScriptById(String id) {
		if( id instanceof String ) {
			SysScript.remove(StringUtils.split(id, ','));
		} else {
			SysScript.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove SysScript.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#removeSysScriptByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 * auto generate by qdp v3.0.
	 */
	public void removeSysScriptByIds(String[] ids) {
		SysScript.remove(ids);
	}
	
	
	/**
	 * <pre>
	 * get SysScript by unique name
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#getByName(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysScriptVO getByName(String name) {
		SysScript sysScript = SysScript.getByName(name);
		return BeanHelper.copyProperties(SysScriptVO.class, sysScript);
	}
	
	/**
	 * <pre>
	 * get SysScript by unique code
	 * </pre>
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.common.SysScriptApplication#getByCode(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysScriptVO getByCode(String code) {
		SysScript sysScript = SysScript.getByCode(code);
		return BeanHelper.copyProperties(SysScriptVO.class, sysScript);
	}
	
	
	
}
