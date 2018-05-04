/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.domain.system.common;

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
 * SysExData
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysExData implements Serializable {

	/** 主键 **/
	private String id;
	/** 表名 **/
	private String refTable;
	/** 关联主键 **/
	private String refId;
	/** 字段名称 **/
	private String colName;
	/** 字段值 **/
	private String colValue;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public SysExData() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getRefTable() {
		return refTable;
	}

	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColValue() {
		return colValue;
	}

	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * <pre>
	 * get SysExData by id	 
	 * Usage : SysExData.get(id);
	 * </pre>
	 * @param sysExData
	 * @return SysExData
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysExData get(SysExData sysExData) {
		return Dao.queryOne("SysExData.getSysExDataById", sysExData);
	}
	
	/**
	 * <pre>
	 * get SysExData by id	 
	 * Usage : SysExData.get(id);
	 * </pre>
	 * @param id
	 * @return SysExData
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysExData get(String id) {
		SysExData sysExData = new SysExData();
		sysExData.setId(id);
		return get(sysExData);
	}
	
	/**
	 * <pre>
	 * remove SysExData by id
	 * Usage : SysExData.remove(id)
	 * </pre>
	 * @param sysExData
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(SysExData sysExData) {
		sysExData.remove();
	}
	
	/**
	 * <pre>
	 * remove SysExData by id
	 * Usage : SysExData.remove(id)
	 * </pre>
	 * @param sysExData
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		SysExData sysExData = new SysExData();
		sysExData.setId(id);
		remove(sysExData);
	}
	
	/**
	 * <pre>
	 * remove SysExData by id
	 * Usage : SysExData.remove(id)
	 * </pre>
	 * @param sysExData
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysExData sysExData = new SysExData();
				sysExData.setId(id);
				remove(sysExData);
			}
		}
	}
	
	/**
	 * <pre>
	 * add SysExData
	 * Usage : SysExData.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysExData add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysExData.insertSysExData", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysExData
	 * Usage : SysExData.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysExData update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysExData.updateSysExData", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysExData
	 * Usage : SysExData.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysExData addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysExData by id
	 * Usage : SysExData.remove()
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
		Dao.remove("SysExData.deleteSysExData", this);
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
				setUpdateTime(new java.util.Date());
				setCreateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("SysExData.refTable", getRefTable(), "iff.validate.required", "{0} is required!");
				validate.required("SysExData.refId", getRefId(), "iff.validate.required", "{0} is required!");
				validate.required("SysExData.colName", getColName(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("edit".equals(type)) {
			{//初始化值
				setUpdateTime(new java.util.Date());
				setCreateTime(null);
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("SysExData.refTable", getRefTable(), "iff.validate.required", "{0} is required!");
				validate.required("SysExData.refId", getRefId(), "iff.validate.required", "{0} is required!");
				validate.required("SysExData.colName", getColName(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysExData.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysExData.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
