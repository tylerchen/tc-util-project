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
 * 组织管理 - AuthOrganizationVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthOrganization")
@SuppressWarnings("serial")
public class AuthOrganizationVO implements Serializable {

	/** 主键 **/
	private String id;
	/** 组织机构名称 **/
	private String name;
	/** 上级机构 **/
	private String parentId;
	/** 根机构 **/
	private String rootId;
	/** 组织机构编码 **/
	private String code;
	/** 使用根菜单 **/
	private String isUseRootMenu;
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
	/** 组织机构名称 **/
	private String parentIdName;
	/** 组织机构名称 **/
	private String rootIdName;
	/** 菜单 **/
	private String menuId;
	/** 菜单名称 **/
	private String menuIdName;
	/** 用户 **/
	private String userId;
	/** 用户名称 **/
	private String userIdName;
	/** 角色 **/
	private String roleId;
	/** 名称 **/
	private String roleIdName;

	public AuthOrganizationVO() {
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsUseRootMenu() {
		return isUseRootMenu;
	}

	public void setIsUseRootMenu(String isUseRootMenu) {
		this.isUseRootMenu = isUseRootMenu;
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

	public String getParentIdName() {
		return parentIdName;
	}

	public void setParentIdName(String parentIdName) {
		this.parentIdName = parentIdName;
	}
	public String getRootIdName() {
		return rootIdName;
	}

	public void setRootIdName(String rootIdName) {
		this.rootIdName = rootIdName;
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
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
