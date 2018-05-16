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
 * 菜单管理 - AuthMenuVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "AuthMenu")
@SuppressWarnings("serial")
public class AuthMenuVO implements Serializable {

	/** 主键 **/
	private String id;
	/** 菜单名称 **/
	private String name;
	/** 资源 **/
	private String resourceId;
	/** 一级分类 **/
	private String type1;
	/** 二级分类 **/
	private String type2;
	/** 父菜单 **/
	private String parentId;
	/** 根菜单 **/
	private String rootId;
	/** 菜单排序 **/
	private Integer menuIndex;
	/** 菜单层次 **/
	private Integer maxLevel;
	/** 图标 **/
	private String ico;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;
	/** 名称 **/
	private String resourceIdName;
	/** resourceIdCode，用于权限中比较特殊的 **/
	private String resourceIdCode;
	/** 菜单名称 **/
	private String parentIdName;
	/** 菜单名称 **/
	private String rootIdName;

	public AuthMenuVO() {
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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
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

}
