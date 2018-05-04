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
 * 角色管理 - AuthRoleVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthRole")
@SuppressWarnings("serial")
public class AuthRoleVO implements Serializable {

	/** 主键 **/
	private String id;
	/** 名称 **/
	private String name;
	/** 编码 **/
	private String code;
	/** 一级分类 **/
	private String type1;
	/** 二级分类 **/
	private String type2;
	/** 状态 **/
	private String status;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;
	/** 资源 **/
	private String resourceId;
	/** 名称 **/
	private String resourceIdName;
	/** 菜单 **/
	private String menuId;
	/** 菜单名称 **/
	private String menuIdName;

	public AuthRoleVO() {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceIdName() {
		return resourceIdName;
	}

	public void setResourceIdName(String resourceIdName) {
		this.resourceIdName = resourceIdName;
	}
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuIdName() {
		return menuIdName;
	}

	public void setMenuIdName(String menuIdName) {
		this.menuIdName = menuIdName;
	}

}
