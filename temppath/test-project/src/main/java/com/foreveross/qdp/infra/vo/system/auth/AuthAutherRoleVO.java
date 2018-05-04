/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.infra.vo.system.auth;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 授权角色 - AuthAutherRoleVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthAutherRole")
@SuppressWarnings("serial")
public class AuthAutherRoleVO implements Serializable {

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
	/** 名称 **/
	private String roleIdName;

	public AuthAutherRoleVO() {
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

	public String getRoleIdName() {
		return roleIdName;
	}

	public void setRoleIdName(String roleIdName) {
		this.roleIdName = roleIdName;
	}

}
