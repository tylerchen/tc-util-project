/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
 * LogAccess
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class LogAccess implements Serializable {

	/** 主键 **/
	private String id;
	/** 类型 **/
	private String type;
	/** 访问者 **/
	private String user;
	/** 来源 **/
	private String source;
	/** 目标 **/
	private String target;
	/** 创建时间 **/
	private Date createTime;
	/** 修改时间 **/
	private Date updateTime;

	public LogAccess() {
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
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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
	 * get LogAccess by id	 
	 * Usage : LogAccess.get(id);
	 * </pre>
	 * @param logAccess
	 * @return LogAccess
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static LogAccess get(LogAccess logAccess) {
		return Dao.queryOne("LogAccess.getLogAccessById", logAccess);
	}
	
	/**
	 * <pre>
	 * get LogAccess by id	 
	 * Usage : LogAccess.get(id);
	 * </pre>
	 * @param id
	 * @return LogAccess
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static LogAccess get(String id) {
		LogAccess logAccess = new LogAccess();
		logAccess.setId(id);
		return get(logAccess);
	}
	
	/**
	 * <pre>
	 * remove LogAccess by id
	 * Usage : LogAccess.remove(id)
	 * </pre>
	 * @param logAccess
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(LogAccess logAccess) {
		logAccess.remove();
	}
	
	/**
	 * <pre>
	 * remove LogAccess by id
	 * Usage : LogAccess.remove(id)
	 * </pre>
	 * @param logAccess
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		LogAccess logAccess = new LogAccess();
		logAccess.setId(id);
		remove(logAccess);
	}
	
	/**
	 * <pre>
	 * remove LogAccess by id
	 * Usage : LogAccess.remove(id)
	 * </pre>
	 * @param logAccess
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				LogAccess logAccess = new LogAccess();
				logAccess.setId(id);
				remove(logAccess);
			}
		}
	}
	
	/**
	 * <pre>
	 * add LogAccess
	 * Usage : LogAccess.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public LogAccess add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("LogAccess.insertLogAccess", this);
		return this;
	}

	/**
	 * <pre>
	 * update LogAccess
	 * Usage : LogAccess.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public LogAccess update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("LogAccess.updateLogAccess", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update LogAccess
	 * Usage : LogAccess.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public LogAccess addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove LogAccess by id
	 * Usage : LogAccess.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		// 删除本对象
		Dao.remove("LogAccess.deleteLogAccess", this);
	}
	
	
	
	
	/**
	 * <pre>
	 * validate add/update/delete
	 * </pre>
	 * @param type add/update/delete
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
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
				validate.required("LogAccess.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.user", getUser(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.source", getSource(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.target", getTarget(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"LOGIN", "LOGOUT", "URL", "MENU", "BUTTON", "AUTO"};
				 	validate.inArray("LogAccess.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
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
				validate.required("LogAccess.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.user", getUser(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.source", getSource(), "iff.validate.required", "{0} is required!");
				validate.required("LogAccess.target", getTarget(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("LogAccess.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"LOGIN", "LOGOUT", "URL", "MENU", "BUTTON", "AUTO"};
				 	validate.inArray("LogAccess.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("LogAccess.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
