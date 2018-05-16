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
 * 组织用户 - AuthOrganizationUserVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthOrganizationUser")
@SuppressWarnings("serial")
public class AuthOrganizationUserVO implements Serializable {

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
	/** 用户名称 **/
	private String userIdName;
	/** 组织机构名称 **/
	private String organizationIdName;

	public AuthOrganizationUserVO() {
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

	public String getUserIdName() {
		return userIdName;
	}

	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
	}
	public String getOrganizationIdName() {
		return organizationIdName;
	}

	public void setOrganizationIdName(String organizationIdName) {
		this.organizationIdName = organizationIdName;
	}

}
