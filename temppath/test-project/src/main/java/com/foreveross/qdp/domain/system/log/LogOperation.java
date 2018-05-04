/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.domain.system.log;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ValidateHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;

import org.apache.commons.lang3.StringUtils;

/**
 * LogOperation
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class LogOperation implements Serializable {

	/** 主键 **/
	private String id;
	/** 类型 **/
	private String type;
	/** 操作者 **/
	private String operator;
	/** 目标 **/
	private String target;
	/** 开始时间 **/
	private Date startTime;
	/** 结束时间 **/
	private Date endTime;
	/** 耗时 **/
	private Integer costTime;
	/** 结果 **/
	private String result;
	/** 创建时间 **/
	private Date createTime;
	/** 修改时间 **/
	private Date updateTime;

	public LogOperation() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getCostTime() {
		return costTime;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * <pre>
	 * get LogOperation by id	 
	 * Usage : LogOperation.get(id);
	 * </pre>
	 * @param logOperation
	 * @return LogOperation
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static LogOperation get(LogOperation logOperation) {
		return Dao.queryOne("LogOperation.getLogOperationById", logOperation);
	}
	
	/**
	 * <pre>
	 * get LogOperation by id	 
	 * Usage : LogOperation.get(id);
	 * </pre>
	 * @param id
	 * @return LogOperation
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static LogOperation get(String id) {
		LogOperation logOperation = new LogOperation();
		logOperation.setId(id);
		return get(logOperation);
	}
	
	/**
	 * <pre>
	 * remove LogOperation by id
	 * Usage : LogOperation.remove(id)
	 * </pre>
	 * @param logOperation
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(LogOperation logOperation) {
		logOperation.remove();
	}
	
	/**
	 * <pre>
	 * remove LogOperation by id
	 * Usage : LogOperation.remove(id)
	 * </pre>
	 * @param logOperation
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		LogOperation logOperation = new LogOperation();
		logOperation.setId(id);
		remove(logOperation);
	}
	
	/**
	 * <pre>
	 * remove LogOperation by id
	 * Usage : LogOperation.remove(id)
	 * </pre>
	 * @param logOperation
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				LogOperation logOperation = new LogOperation();
				logOperation.setId(id);
				remove(logOperation);
			}
		}
	}
	
	/**
	 * <pre>
	 * add LogOperation
	 * Usage : LogOperation.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public LogOperation add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("LogOperation.insertLogOperation", this);
		return this;
	}

	/**
	 * <pre>
	 * update LogOperation
	 * Usage : LogOperation.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public LogOperation update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("LogOperation.updateLogOperation", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update LogOperation
	 * Usage : LogOperation.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public LogOperation addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove LogOperation by id
	 * Usage : LogOperation.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		// 删除本对象
		Dao.remove("LogOperation.deleteLogOperation", this);
	}
	
	
	
	
	/**
	 * <pre>
	 * validate add/update/delete
	 * </pre>
	 * @param type add/update/delete
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	private ValidateHelper validate(String type) {
		ValidateHelper validate = ValidateHelper.create();
		if ("add".equals(type)) {
			{//初始化值
				setCreateTime(new java.util.Date());
				setUpdateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("LogOperation.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.operator", getOperator(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.target", getTarget(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.startTime", getStartTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.endTime", getEndTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.costTime", getCostTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.result", getResult(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("edit".equals(type)) {
			{//初始化值
				setCreateTime(null);
				setUpdateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("LogOperation.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.operator", getOperator(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.target", getTarget(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.startTime", getStartTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.endTime", getEndTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.costTime", getCostTime(), "iff.validate.required", "{0} is required!");
				validate.required("LogOperation.result", getResult(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("LogOperation.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("LogOperation.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
