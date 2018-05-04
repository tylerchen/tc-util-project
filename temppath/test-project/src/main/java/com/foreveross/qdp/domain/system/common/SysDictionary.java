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
 * SysDictionary
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysDictionary implements Serializable {

	/** 主键 **/
	private String id;
	/** 一级分类 **/
	private String type1;
	/** 二级分类 **/
	private String type2;
	/** 一级名称 **/
	private String name1;
	/** 二级名称 **/
	private String name2;
	/** 编码 **/
	private String code;
	/** 字典值 **/
	private String value;
	/** 排序 **/
	private int sort;
	/** 描述 **/
	private String desciption;
	/** 状态 **/
	private String status;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public SysDictionary() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	 * get SysDictionary by id	 
	 * Usage : SysDictionary.get(id);
	 * </pre>
	 * @param sysDictionary
	 * @return SysDictionary
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysDictionary get(SysDictionary sysDictionary) {
		return Dao.queryOne("SysDictionary.getSysDictionaryById", sysDictionary);
	}
	
	/**
	 * <pre>
	 * get SysDictionary by id	 
	 * Usage : SysDictionary.get(id);
	 * </pre>
	 * @param id
	 * @return SysDictionary
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysDictionary get(String id) {
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setId(id);
		return get(sysDictionary);
	}
	
	/**
	 * <pre>
	 * remove SysDictionary by id
	 * Usage : SysDictionary.remove(id)
	 * </pre>
	 * @param sysDictionary
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(SysDictionary sysDictionary) {
		sysDictionary.remove();
	}
	
	/**
	 * <pre>
	 * remove SysDictionary by id
	 * Usage : SysDictionary.remove(id)
	 * </pre>
	 * @param sysDictionary
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setId(id);
		remove(sysDictionary);
	}
	
	/**
	 * <pre>
	 * remove SysDictionary by id
	 * Usage : SysDictionary.remove(id)
	 * </pre>
	 * @param sysDictionary
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysDictionary sysDictionary = new SysDictionary();
				sysDictionary.setId(id);
				remove(sysDictionary);
			}
		}
	}
	
	/**
	 * <pre>
	 * add SysDictionary
	 * Usage : SysDictionary.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysDictionary add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysDictionary.insertSysDictionary", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysDictionary
	 * Usage : SysDictionary.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysDictionary update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysDictionary.updateSysDictionary", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysDictionary
	 * Usage : SysDictionary.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysDictionary addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysDictionary by id
	 * Usage : SysDictionary.remove()
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
		Dao.remove("SysDictionary.deleteSysDictionary", this);
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
				validate.required("SysDictionary.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.name1", getName1(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.name2", getName2(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.number("SysDictionary.sort", getSort(), "iff.validate.number", "{0} is not valid number!");
				validate.required("SysDictionary.status", getStatus(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("SysDictionary.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
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
				validate.required("SysDictionary.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.name1", getName1(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.name2", getName2(), "iff.validate.required", "{0} is required!");
				validate.required("SysDictionary.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.number("SysDictionary.sort", getSort(), "iff.validate.number", "{0} is not valid number!");
				validate.required("SysDictionary.status", getStatus(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysDictionary.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("SysDictionary.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysDictionary.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
