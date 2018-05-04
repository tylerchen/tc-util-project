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
 * AuthRoleResource
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthRoleResource implements Serializable {

	/** 主键 **/
	private String id;
	/** 角色 **/
	private String roleId;
	/** 资源 **/
	private String resourceId;
	/** 创建时间 **/
	private Date createTime;
	/** 修改时间 **/
	private Date updateTime;

	public AuthRoleResource() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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
	 * get AuthRoleResource by id	 
	 * Usage : AuthRoleResource.get(id);
	 * </pre>
	 * @param authRoleResource
	 * @return AuthRoleResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRoleResource get(AuthRoleResource authRoleResource) {
		return Dao.queryOne("AuthRoleResource.getAuthRoleResourceById", authRoleResource);
	}
	
	/**
	 * <pre>
	 * get AuthRoleResource by id	 
	 * Usage : AuthRoleResource.get(id);
	 * </pre>
	 * @param id
	 * @return AuthRoleResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRoleResource get(String id) {
		AuthRoleResource authRoleResource = new AuthRoleResource();
		authRoleResource.setId(id);
		return get(authRoleResource);
	}
	
	/**
	 * <pre>
	 * remove AuthRoleResource by id
	 * Usage : AuthRoleResource.remove(id)
	 * </pre>
	 * @param authRoleResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(AuthRoleResource authRoleResource) {
		authRoleResource.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthRoleResource by id
	 * Usage : AuthRoleResource.remove(id)
	 * </pre>
	 * @param authRoleResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		AuthRoleResource authRoleResource = new AuthRoleResource();
		authRoleResource.setId(id);
		remove(authRoleResource);
	}
	
	/**
	 * <pre>
	 * remove AuthRoleResource by id
	 * Usage : AuthRoleResource.remove(id)
	 * </pre>
	 * @param authRoleResource
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthRoleResource authRoleResource = new AuthRoleResource();
				authRoleResource.setId(id);
				remove(authRoleResource);
			}
		}
	}
	
	/**
	 * <pre>
	 * add AuthRoleResource
	 * Usage : AuthRoleResource.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRoleResource add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthRoleResource.insertAuthRoleResource", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthRoleResource
	 * Usage : AuthRoleResource.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRoleResource update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthRoleResource.updateAuthRoleResource", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthRoleResource
	 * Usage : AuthRoleResource.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRoleResource addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthRoleResource by id
	 * Usage : AuthRoleResource.remove()
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
		Dao.remove("AuthRoleResource.deleteAuthRoleResource", this);
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
				validate.required("AuthRoleResource.roleId", getRoleId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRoleResource.resourceId", getResourceId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthRole authRole = com.foreveross.qdp.domain.system.auth.AuthRole.get(getRoleId());
					validate.required("AuthRole", authRole, "iff.validate.required", "{0} is required!");
				}
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthResource authResource = com.foreveross.qdp.domain.system.auth.AuthResource.get(getResourceId());
					validate.required("AuthResource", authResource, "iff.validate.required", "{0} is required!");
				}
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
				validate.required("AuthRoleResource.roleId", getRoleId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRoleResource.resourceId", getResourceId(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthRoleResource.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthRole authRole = com.foreveross.qdp.domain.system.auth.AuthRole.get(getRoleId());
					validate.required("AuthRole", authRole, "iff.validate.required", "{0} is required!");
				}
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthResource authResource = com.foreveross.qdp.domain.system.auth.AuthResource.get(getResourceId());
					validate.required("AuthResource", authResource, "iff.validate.required", "{0} is required!");
				}
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthRoleResource.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
