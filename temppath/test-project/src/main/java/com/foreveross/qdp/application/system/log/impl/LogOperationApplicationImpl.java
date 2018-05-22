/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.application.system.log.impl;

import java.util.List;
import javax.inject.Named;

import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.foreveross.qdp.application.system.log.LogOperationApplication;
import com.foreveross.qdp.infra.vo.system.log.LogOperationVO;
import com.foreveross.qdp.domain.system.log.LogOperation;

/**
 * LogOperation
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("logOperationApplication")
@Transactional
public class LogOperationApplicationImpl implements LogOperationApplication {

	/**
	 * <pre>
	 * get LogOperationVO by id.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#getLogOperation(LogOperationVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public LogOperationVO getLogOperation(LogOperationVO vo) {
		LogOperation logOperation = BeanHelper.copyProperties(LogOperation.class, vo);
		logOperation = LogOperation.get(logOperation);
		return BeanHelper.copyProperties(LogOperationVO.class, logOperation);
	}

	/**
	 * <pre>
	 * get LogOperationVO by id.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#getLogOperationById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public LogOperationVO getLogOperationById(String id){
		LogOperation logOperation = LogOperation.get(id);
		return BeanHelper.copyProperties(LogOperationVO.class, logOperation);
	}
	
	/**
	 * <pre>
	 * page find LogOperationVO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#pageFindLogOperation(LogOperationVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindLogOperation(LogOperationVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("LogOperation.pageFindLogOperation", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(LogOperationVO.class);
	}

	/**
	 * <pre>
	 * page find LogOperation Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#pageFindLogOperationMap(LogOperationVO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindLogOperationMap(LogOperationVO vo, Page page) {
		page = Page.notNullPage(page);
		page = Dao.queryPage("LogOperation.pageFindLogOperationMap", MapHelper.toMap("page", page, "vo", vo));
		return page.toPage(LogOperationVO.class);
	}

	/**
	 * <pre>
	 * add LogOperation.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#addLogOperation(LogOperationVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public LogOperationVO addLogOperation(LogOperationVO vo) {
		LogOperation logOperation = BeanHelper.copyProperties(LogOperation.class, vo);
		logOperation.add();
		LogOperationVO logOperationVO = BeanHelper.copyProperties(LogOperationVO.class, logOperation);
		return logOperationVO;
	}

	/**
	 * <pre>
	 * update LogOperation.
	 * </pre>
	 * @param vo
	 * @return LogOperationVO
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#updateLogOperation(LogOperationVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public LogOperationVO updateLogOperation(LogOperationVO vo) {
		LogOperation logOperation = BeanHelper.copyProperties(LogOperation.class, vo);
		logOperation.update();
		LogOperationVO logOperationVO = BeanHelper.copyProperties(LogOperationVO.class, logOperation);
		return logOperationVO;
	}
	
	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#removeLogOperation(LogOperationVO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeLogOperation(LogOperationVO vo) {
		LogOperation logOperation = BeanHelper.copyProperties(LogOperation.class, vo);
		LogOperation.remove(logOperation);
	}

	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#removeLogOperationById(String)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeLogOperationById(String id) {
		if( id instanceof String ) {
			LogOperation.remove(StringUtils.split(id, ','));
		} else {
			LogOperation.remove(id);
		}
	}
	
	/**
	 * <pre>
	 * remove LogOperation.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see com.foreveross.qdp.application.system.log.LogOperationApplication#removeLogOperationByIds(String[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 * auto generate by qdp v3.0.
	 */
	public void removeLogOperationByIds(String[] ids) {
		LogOperation.remove(ids);
	}
	
	
	
	
}
