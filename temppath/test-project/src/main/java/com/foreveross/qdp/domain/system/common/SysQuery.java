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
 * SysQuery
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysQuery implements Serializable {

	/** 主键 **/
	private String id;
	/** 名称 **/
	private String name;
	/** 代码 **/
	private String code;
	/** 数据源 **/
	private String dataSource;
	/** 查询语句 **/
	private String queryContent;
	/** 一级分类 **/
	private String type1;
	/** 二级分类 **/
	private String type2;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public SysQuery() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getQueryContent() {
		return queryContent;
	}

	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
	}
	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}
	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
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
	 * get SysQuery by id	 
	 * Usage : SysQuery.get(id);
	 * </pre>
	 * @param sysQuery
	 * @return SysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysQuery get(SysQuery sysQuery) {
		return Dao.queryOne("SysQuery.getSysQueryById", sysQuery);
	}
	
	/**
	 * <pre>
	 * get SysQuery by id	 
	 * Usage : SysQuery.get(id);
	 * </pre>
	 * @param id
	 * @return SysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysQuery get(String id) {
		SysQuery sysQuery = new SysQuery();
		sysQuery.setId(id);
		return get(sysQuery);
	}
	
	/**
	 * <pre>
	 * remove SysQuery by id
	 * Usage : SysQuery.remove(id)
	 * </pre>
	 * @param sysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(SysQuery sysQuery) {
		sysQuery.remove();
	}
	
	/**
	 * <pre>
	 * remove SysQuery by id
	 * Usage : SysQuery.remove(id)
	 * </pre>
	 * @param sysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		SysQuery sysQuery = new SysQuery();
		sysQuery.setId(id);
		remove(sysQuery);
	}
	
	/**
	 * <pre>
	 * remove SysQuery by id
	 * Usage : SysQuery.remove(id)
	 * </pre>
	 * @param sysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysQuery sysQuery = new SysQuery();
				sysQuery.setId(id);
				remove(sysQuery);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : SysQuery.hasName(SysQuery)
	 * </pre>
	 * @param sysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasName(SysQuery sysQuery) {
		return Dao.querySize("SysQuery.hasName", sysQuery) > 0;
	}
	
	/**
	 * <pre>
	 * has Code
	 * Usage : SysQuery.hasCode(SysQuery)
	 * </pre>
	 * @param sysQuery
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasCode(SysQuery sysQuery) {
		return Dao.querySize("SysQuery.hasCode", sysQuery) > 0;
	}
	
	/**
	 * <pre>
	 * add SysQuery
	 * Usage : SysQuery.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysQuery add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysQuery.insertSysQuery", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysQuery
	 * Usage : SysQuery.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysQuery update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysQuery.updateSysQuery", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysQuery
	 * Usage : SysQuery.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysQuery addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysQuery by id
	 * Usage : SysQuery.remove()
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
		Dao.remove("SysQuery.deleteSysQuery", this);
	}
	
	/**
	 * <pre>
	 * get SysQuery by unique name
	 * Usage : SysQuery.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysQuery getByName(String name) {
		SysQuery domain = new SysQuery();
		domain.setName(name);
		return Dao.queryOne("SysQuery.getSysQueryByName", domain);
	}
	
	/**
	 * <pre>
	 * get SysQuery by unique code
	 * Usage : SysQuery.getByCode(code)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysQuery getByCode(String code) {
		SysQuery domain = new SysQuery();
		domain.setCode(code);
		return Dao.queryOne("SysQuery.getSysQueryByCode", domain);
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
				validate.required("SysQuery.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.queryContent", getQueryContent(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.updateTime", getUpdateTime(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysQuery.name", 
						Dao.querySize("SysQuery.countSysQueryByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("SysQuery.code", 
						Dao.querySize("SysQuery.countSysQueryByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysQuery.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysQuery.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
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
				validate.required("SysQuery.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.queryContent", getQueryContent(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("SysQuery.updateTime", getUpdateTime(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysQuery.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysQuery.name", 
						Dao.querySize("SysQuery.countSysQueryByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("SysQuery.code", 
						Dao.querySize("SysQuery.countSysQueryByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysQuery.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysQuery.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysQuery.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
