/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
 * 角色资源 - AuthRoleResourceVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthRoleResource")
@SuppressWarnings("serial")
public class AuthRoleResourceVO implements Serializable {

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
	/** 名称 **/
	private String roleIdName;
	/** 名称 **/
	private String resourceIdName;
	/** resourceIdCode，用于权限中比较特殊的 **/
	private String resourceIdCode;

	public AuthRoleResourceVO() {
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

	public String getRoleIdName() {
		return roleIdName;
	}

	public void setRoleIdName(String roleIdName) {
		this.roleIdName = roleIdName;
	}
	public String getResourceIdName() {
		return resourceIdName;
	}

	public void setResourceIdName(String resourceIdName) {
		this.resourceIdName = resourceIdName;
	}
	
	public String getResourceIdCode() {
		return resourceIdCode;
	}

	public void setResourceIdCode(String code) {
		this.resourceIdCode = code;
	}

}
