/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.domain.system.auth;

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
 * AuthResource
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthResource implements Serializable {

	/** 主键 **/
	private String id;
	/** 名称 **/
	private String name;
	/** 编码 **/
	private String code;
	/** 状态 **/
	private String status;
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

	public AuthResource() {
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
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	 * get AuthResource by id	 
	 * Usage : AuthResource.get(id);
	 * </pre>
	 * @param authResource
	 * @return AuthResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthResource get(AuthResource authResource) {
		return Dao.queryOne("AuthResource.getAuthResourceById", authResource);
	}
	
	/**
	 * <pre>
	 * get AuthResource by id	 
	 * Usage : AuthResource.get(id);
	 * </pre>
	 * @param id
	 * @return AuthResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthResource get(String id) {
		AuthResource authResource = new AuthResource();
		authResource.setId(id);
		return get(authResource);
	}
	
	/**
	 * <pre>
	 * remove AuthResource by id
	 * Usage : AuthResource.remove(id)
	 * </pre>
	 * @param authResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(AuthResource authResource) {
		authResource.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthResource by id
	 * Usage : AuthResource.remove(id)
	 * </pre>
	 * @param authResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		AuthResource authResource = new AuthResource();
		authResource.setId(id);
		remove(authResource);
	}
	
	/**
	 * <pre>
	 * remove AuthResource by id
	 * Usage : AuthResource.remove(id)
	 * </pre>
	 * @param authResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthResource authResource = new AuthResource();
				authResource.setId(id);
				remove(authResource);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : AuthResource.hasName(AuthResource)
	 * </pre>
	 * @param authResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasName(AuthResource authResource) {
		return Dao.querySize("AuthResource.hasName", authResource) > 0;
	}
	
	/**
	 * <pre>
	 * has Code
	 * Usage : AuthResource.hasCode(AuthResource)
	 * </pre>
	 * @param authResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasCode(AuthResource authResource) {
		return Dao.querySize("AuthResource.hasCode", authResource) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthResource
	 * Usage : AuthResource.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthResource add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthResource.insertAuthResource", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthResource
	 * Usage : AuthResource.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthResource update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthResource.updateAuthResource", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthResource
	 * Usage : AuthResource.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthResource addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthResource by id
	 * Usage : AuthResource.remove()
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
		Dao.remove("AuthResource.deleteAuthResource", this);
	}
	
	/**
	 * <pre>
	 * get AuthResource by unique name
	 * Usage : AuthResource.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthResource getByName(String name) {
		AuthResource domain = new AuthResource();
		domain.setName(name);
		return Dao.queryOne("AuthResource.getAuthResourceByName", domain);
	}
	
	/**
	 * <pre>
	 * get AuthResource by unique code
	 * Usage : AuthResource.getByCode(code)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthResource getByCode(String code) {
		AuthResource domain = new AuthResource();
		domain.setCode(code);
		return Dao.queryOne("AuthResource.getAuthResourceByCode", domain);
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
				setUpdateTime(new java.util.Date());
				setCreateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthResource.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthResource.name", 
						Dao.querySize("AuthResource.countAuthResourceByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthResource.code", 
						Dao.querySize("AuthResource.countAuthResourceByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthResource.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthResource.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S", "MENU", "URL", "BUTTON", "OTHER"};
				 	validate.inArray("AuthResource.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
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
				validate.required("AuthResource.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthResource.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthResource.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthResource.name", 
						Dao.querySize("AuthResource.countAuthResourceByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthResource.code", 
						Dao.querySize("AuthResource.countAuthResourceByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthResource.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthResource.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S", "MENU", "URL", "BUTTON", "OTHER"};
				 	validate.inArray("AuthResource.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthResource.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
