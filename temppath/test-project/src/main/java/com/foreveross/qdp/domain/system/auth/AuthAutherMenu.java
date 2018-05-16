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
 * AuthAutherMenu
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthAutherMenu implements Serializable {

	/** 主键 **/
	private String id;
	/** 授权实体 **/
	private String autherId;
	/** 菜单 **/
	private String menuId;
	/** 分类 **/
	private String type;
	/** 排序 **/
	private Integer sort;
	/** 创建时间 **/
	private Date createTime;
	/** 修改时间 **/
	private Date updateTime;

	public AuthAutherMenu() {
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
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
	 * get AuthAutherMenu by id	 
	 * Usage : AuthAutherMenu.get(id);
	 * </pre>
	 * @param authAutherMenu
	 * @return AuthAutherMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthAutherMenu get(AuthAutherMenu authAutherMenu) {
		return Dao.queryOne("AuthAutherMenu.getAuthAutherMenuById", authAutherMenu);
	}
	
	/**
	 * <pre>
	 * get AuthAutherMenu by id	 
	 * Usage : AuthAutherMenu.get(id);
	 * </pre>
	 * @param id
	 * @return AuthAutherMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthAutherMenu get(String id) {
		AuthAutherMenu authAutherMenu = new AuthAutherMenu();
		authAutherMenu.setId(id);
		return get(authAutherMenu);
	}
	
	/**
	 * <pre>
	 * remove AuthAutherMenu by id
	 * Usage : AuthAutherMenu.remove(id)
	 * </pre>
	 * @param authAutherMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(AuthAutherMenu authAutherMenu) {
		authAutherMenu.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthAutherMenu by id
	 * Usage : AuthAutherMenu.remove(id)
	 * </pre>
	 * @param authAutherMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		AuthAutherMenu authAutherMenu = new AuthAutherMenu();
		authAutherMenu.setId(id);
		remove(authAutherMenu);
	}
	
	/**
	 * <pre>
	 * remove AuthAutherMenu by id
	 * Usage : AuthAutherMenu.remove(id)
	 * </pre>
	 * @param authAutherMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthAutherMenu authAutherMenu = new AuthAutherMenu();
				authAutherMenu.setId(id);
				remove(authAutherMenu);
			}
		}
	}
	
	/**
	 * <pre>
	 * add AuthAutherMenu
	 * Usage : AuthAutherMenu.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherMenu add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAutherMenu.insertAuthAutherMenu", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthAutherMenu
	 * Usage : AuthAutherMenu.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherMenu update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAutherMenu.updateAuthAutherMenu", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthAutherMenu
	 * Usage : AuthAutherMenu.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthAutherMenu addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthAutherMenu by id
	 * Usage : AuthAutherMenu.remove()
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
		Dao.remove("AuthAutherMenu.deleteAuthAutherMenu", this);
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
				validate.required("AuthAutherMenu.autherId", getAutherId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.menuId", getMenuId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.sort", getSort(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.updateTime", getUpdateTime(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthMenu authMenu = com.foreveross.qdp.domain.system.auth.AuthMenu.get(getMenuId());
					validate.required("AuthMenu", authMenu, "iff.validate.required", "{0} is required!");
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
				validate.required("AuthAutherMenu.autherId", getAutherId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.menuId", getMenuId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.sort", getSort(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAutherMenu.updateTime", getUpdateTime(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthAutherMenu.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
			}
			{// validate value range
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthMenu authMenu = com.foreveross.qdp.domain.system.auth.AuthMenu.get(getMenuId());
					validate.required("AuthMenu", authMenu, "iff.validate.required", "{0} is required!");
				}
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthAutherMenu.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
