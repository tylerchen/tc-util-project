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
 * AuthAutherRole
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthAutherRole implements Serializable {

	/** 主键 **/
	private String id;
	/** 授权实体 **/
	private String autherId;
	/** 角色 **/
	private String roleId;
	/** 分类 **/
	private String type;
	/** 创建时间 **/
	private Date createTime;
	/** 修改时间 **/
	private Date updateTime;

	public AuthAutherRole() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getAutherId() {
		return autherId;
	}

	public void setAutherId(String autherId) {
		this.autherId = autherId;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	 * get AuthAutherRole by id	 
	 * Usage : AuthAutherRole.get(id);
	 * </pre>
	 * @param authAutherRole
	 * @return AuthAutherRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthAutherRole get(AuthAutherRole authAutherRole) {
		return Dao.queryOne("AuthAutherRole.getAuthAutherRoleById", authAutherRole);
	}
	
	/**
	 * <pre>
	 * get AuthAutherRole by id	 
	 * Usage : AuthAutherRole.get(id);
	 * </pre>
	 * @param id
	 * @return AuthAutherRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthAutherRole get(String id) {
		AuthAutherRole authAutherRole = new AuthAutherRole();
		authAutherRole.setId(id);
		return get(authAutherRole);
	}
	
	/**
	 * <pre>
	 * remove AuthAutherRole by id
	 * Usage : AuthAutherRole.remove(id)
	 * </pre>
	 * @param authAutherRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(AuthAutherRole authAutherRole) {
		authAutherRole.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthAutherRole by id
	 * Usage : AuthAutherRole.remove(id)
	 * </pre>
	 * @param authAutherRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		AuthAutherRole authAutherRole = new AuthAutherRole();
		authAutherRole.setId(id);
		remove(authAutherRole);
	}
	
	/**
	 * <pre>
	 * remove AuthAutherRole by id
	 * Usage : AuthAutherRole.remove(id)
	 * </pre>
	 * @param authAutherRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthAutherRole authAutherRole = new AuthAutherRole();
				authAutherRole.setId(id);
				remove(authAutherRole);
			}
		}
	}
	
	/**
	 * <pre>
	 * add AuthAutherRole
	 * Usage : AuthAutherRole.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherRole add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAutherRole.insertAuthAutherRole", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthAutherRole
	 * Usage : AuthAutherRole.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherRole update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAutherRole.updateAuthAutherRole", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthAutherRole
	 * Usage : AuthAutherRole.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherRole addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthAutherRole by id
	 * Usage : AuthAutherRole.remove()
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
		Dao.remove("AuthAutherRole.deleteAuthAutherRole", this);
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
				validate.required("AuthAutherRole.autherId", getAutherId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherRole.roleId", getRoleId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherRole.type", getType(), "iff.validate.required", "{0} is required!");
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
				validate.required("AuthAutherRole.autherId", getAutherId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherRole.roleId", getRoleId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherRole.type", getType(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthAutherRole.id", getId(), "iff.validate.required", "{0} is required!");
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
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthAutherRole.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
