/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
 * AuthOrganizationUser
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthOrganizationUser implements Serializable {

	/** 主键 **/
	private String id;
	/** 用户 **/
	private String userId;
	/** 组织机构 **/
	private String organizationId;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public AuthOrganizationUser() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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
	 * get AuthOrganizationUser by id	 
	 * Usage : AuthOrganizationUser.get(id);
	 * </pre>
	 * @param authOrganizationUser
	 * @return AuthOrganizationUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthOrganizationUser get(AuthOrganizationUser authOrganizationUser) {
		return Dao.queryOne("AuthOrganizationUser.getAuthOrganizationUserById", authOrganizationUser);
	}
	
	/**
	 * <pre>
	 * get AuthOrganizationUser by id	 
	 * Usage : AuthOrganizationUser.get(id);
	 * </pre>
	 * @param id
	 * @return AuthOrganizationUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthOrganizationUser get(String id) {
		AuthOrganizationUser authOrganizationUser = new AuthOrganizationUser();
		authOrganizationUser.setId(id);
		return get(authOrganizationUser);
	}
	
	/**
	 * <pre>
	 * remove AuthOrganizationUser by id
	 * Usage : AuthOrganizationUser.remove(id)
	 * </pre>
	 * @param authOrganizationUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(AuthOrganizationUser authOrganizationUser) {
		authOrganizationUser.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthOrganizationUser by id
	 * Usage : AuthOrganizationUser.remove(id)
	 * </pre>
	 * @param authOrganizationUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		AuthOrganizationUser authOrganizationUser = new AuthOrganizationUser();
		authOrganizationUser.setId(id);
		remove(authOrganizationUser);
	}
	
	/**
	 * <pre>
	 * remove AuthOrganizationUser by id
	 * Usage : AuthOrganizationUser.remove(id)
	 * </pre>
	 * @param authOrganizationUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthOrganizationUser authOrganizationUser = new AuthOrganizationUser();
				authOrganizationUser.setId(id);
				remove(authOrganizationUser);
			}
		}
	}
	
	/**
	 * <pre>
	 * add AuthOrganizationUser
	 * Usage : AuthOrganizationUser.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthOrganizationUser add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthOrganizationUser.insertAuthOrganizationUser", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthOrganizationUser
	 * Usage : AuthOrganizationUser.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthOrganizationUser update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthOrganizationUser.updateAuthOrganizationUser", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthOrganizationUser
	 * Usage : AuthOrganizationUser.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthOrganizationUser addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthOrganizationUser by id
	 * Usage : AuthOrganizationUser.remove()
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
		Dao.remove("AuthOrganizationUser.deleteAuthOrganizationUser", this);
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
				validate.required("AuthOrganizationUser.userId", getUserId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganizationUser.organizationId", getOrganizationId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthUser authUser = com.foreveross.qdp.domain.system.auth.AuthUser.get(getUserId());
					validate.required("AuthUser", authUser, "iff.validate.required", "{0} is required!");
				}
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthOrganization authOrganization = com.foreveross.qdp.domain.system.auth.AuthOrganization.get(getOrganizationId());
					validate.required("AuthOrganization", authOrganization, "iff.validate.required", "{0} is required!");
				}
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
				validate.required("AuthOrganizationUser.userId", getUserId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganizationUser.organizationId", getOrganizationId(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthOrganizationUser.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthUser authUser = com.foreveross.qdp.domain.system.auth.AuthUser.get(getUserId());
					validate.required("AuthUser", authUser, "iff.validate.required", "{0} is required!");
				}
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthOrganization authOrganization = com.foreveross.qdp.domain.system.auth.AuthOrganization.get(getOrganizationId());
					validate.required("AuthOrganization", authOrganization, "iff.validate.required", "{0} is required!");
				}
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthOrganizationUser.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
