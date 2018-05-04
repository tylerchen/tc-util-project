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
 * 帐户管理 - AuthAccountVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthAccount")
@SuppressWarnings("serial")
public class AuthAccountVO implements Serializable {

	/** 主键 **/
	private String id;
	/** 登录EMail **/
	private String loginEmail;
	/** 用户密码 **/
	private String loginPasswd;
	/** 状态 **/
	private String status;
	/** 类型 **/
	private String type;
	/** 用户 **/
	private String userId;
	/** 最后登录 **/
	private Date lastLogin;
	/** 尝试次数 **/
	private Integer loginTryTimes;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;
	/** 用户名称 **/
	private String userIdName;
	/** 角色 **/
	private String roleId;
	/** 名称 **/
	private String roleIdName;

	public AuthAccountVO() {
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getLoginPasswd() {
		return loginPasswd;
	}

	public void setLoginPasswd(String loginPasswd) {
		this.loginPasswd = loginPasswd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getLoginTryTimes() {
		return loginTryTimes;
	}

	public void setLoginTryTimes(Integer loginTryTimes) {
		this.loginTryTimes = loginTryTimes;
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

	public String getUserIdName() {
		return userIdName;
	}

	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
	}
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleIdName() {
		return roleIdName;
	}

	public void setRoleIdName(String roleIdName) {
		this.roleIdName = roleIdName;
	}

}
