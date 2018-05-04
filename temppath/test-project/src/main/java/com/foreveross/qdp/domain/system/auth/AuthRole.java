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
 * AuthRole
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthRole implements Serializable {

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

	public AuthRole() {
		super();
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

	/**
	 * <pre>
	 * get AuthRole by id	 
	 * Usage : AuthRole.get(id);
	 * </pre>
	 * @param authRole
	 * @return AuthRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRole get(AuthRole authRole) {
		return Dao.queryOne("AuthRole.getAuthRoleById", authRole);
	}
	
	/**
	 * <pre>
	 * get AuthRole by id	 
	 * Usage : AuthRole.get(id);
	 * </pre>
	 * @param id
	 * @return AuthRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRole get(String id) {
		AuthRole authRole = new AuthRole();
		authRole.setId(id);
		return get(authRole);
	}
	
	/**
	 * <pre>
	 * remove AuthRole by id
	 * Usage : AuthRole.remove(id)
	 * </pre>
	 * @param authRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(AuthRole authRole) {
		authRole.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthRole by id
	 * Usage : AuthRole.remove(id)
	 * </pre>
	 * @param authRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		AuthRole authRole = new AuthRole();
		authRole.setId(id);
		remove(authRole);
	}
	
	/**
	 * <pre>
	 * remove AuthRole by id
	 * Usage : AuthRole.remove(id)
	 * </pre>
	 * @param authRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthRole authRole = new AuthRole();
				authRole.setId(id);
				remove(authRole);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : AuthRole.hasName(AuthRole)
	 * </pre>
	 * @param authRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasName(AuthRole authRole) {
		return Dao.querySize("AuthRole.hasName", authRole) > 0;
	}
	
	/**
	 * <pre>
	 * has Code
	 * Usage : AuthRole.hasCode(AuthRole)
	 * </pre>
	 * @param authRole
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasCode(AuthRole authRole) {
		return Dao.querySize("AuthRole.hasCode", authRole) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthRole
	 * Usage : AuthRole.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRole add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthRole.insertAuthRole", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthRole
	 * Usage : AuthRole.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRole update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthRole.updateAuthRole", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthRole
	 * Usage : AuthRole.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthRole addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthRole by id
	 * Usage : AuthRole.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}

		// 删除AuthRoleResource
		removeAuthRoleResource();

		// 删除AuthAutherMenu
		removeAuthAutherMenu();
		// 删除本对象
		Dao.remove("AuthRole.deleteAuthRole", this);
	}
	
	/**
	 * <pre>
	 * get AuthRole by unique name
	 * Usage : AuthRole.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRole getByName(String name) {
		AuthRole domain = new AuthRole();
		domain.setName(name);
		return Dao.queryOne("AuthRole.getAuthRoleByName", domain);
	}
	
	/**
	 * <pre>
	 * get AuthRole by unique code
	 * Usage : AuthRole.getByCode(code)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthRole getByCode(String code) {
		AuthRole domain = new AuthRole();
		domain.setCode(code);
		return Dao.queryOne("AuthRole.getAuthRoleByCode", domain);
	}
	
	

			
	/**
	 * <pre>
	 * find AuthRoleResource.
	 * Usage : AuthRole.findAuthRoleResource()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthResource> findAuthResource() {
		AuthRole condition = new AuthRole();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthRole.pageFindAuthResourceByAuthRoleResourceMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthResource.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthRoleResource.
	 * Usage : AuthRole.findAuthRoleResource()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthRoleResource> findAuthRoleResource() {
		AuthRole condition = new AuthRole();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthRole.pageFindAuthRoleResourceMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthRoleResource.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthRoleResource.
	 * Usage : AuthRole.removeAuthRoleResource()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void removeAuthRoleResource() {
		List<AuthRoleResource> list = findAuthRoleResource();
		for (AuthRoleResource item : list) {
			item.remove();
		}
	}
	
	/**
	 * <pre>
	 * assign AuthResource by id
	 * Usage : AuthRole.assignAuthResource()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void assignAuthResourceByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthRole.get(this.getId()));
		Map<String, AuthRoleResource> map = new HashMap<String, AuthRoleResource>();
		if (ids != null) {
			int counter = 0;
			for (String id : ids) {
				AuthResource ref = AuthResource.get(id);
				Assert.notNull(ref);
				AuthRoleResource tmp = new AuthRoleResource();
				{
					tmp.setRoleId(this.getId());
					tmp.setResourceId(id);
				}
				map.put(id, tmp);
			}
		}

		List<AuthRoleResource> list = findAuthRoleResource();
		for (AuthRoleResource item : list) {
			if (map.containsKey(item.getResourceId())) {
				String id = item.getId();
				AuthRoleResource tmp = BeanHelper.copyProperties(item, map.remove(item.getResourceId()));
				tmp.setId(id);
				
				tmp.addOrUpdate();
			} else {
				item.remove();
			}
		}

		for (AuthRoleResource item : map.values()) {
			
			item.add();
		}
	}

			
	/**
	 * <pre>
	 * find AuthAutherMenu.
	 * Usage : AuthRole.findAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthMenu> findAuthMenu() {
		AuthRole condition = new AuthRole();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthRole.pageFindAuthMenuByAuthAutherMenuMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthMenu.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthAutherMenu.
	 * Usage : AuthRole.findAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthAutherMenu> findAuthAutherMenu() {
		AuthRole condition = new AuthRole();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthRole.pageFindAuthAutherMenuMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthAutherMenu.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthAutherMenu.
	 * Usage : AuthRole.removeAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void removeAuthAutherMenu() {
		List<AuthAutherMenu> list = findAuthAutherMenu();
		for (AuthAutherMenu item : list) {
			item.remove();
		}
	}
	
	/**
	 * <pre>
	 * assign AuthMenu by id
	 * Usage : AuthRole.assignAuthMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void assignAuthMenuByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthRole.get(this.getId()));
		Map<String, AuthAutherMenu> map = new HashMap<String, AuthAutherMenu>();
		if (ids != null) {
			int counter = 0;
			for (String id : ids) {
				AuthMenu ref = AuthMenu.get(id);
				Assert.notNull(ref);
				{// 如果分配的是树型结构，那分配的是根节点
					id = ref.getRootId();
				} 
				AuthAutherMenu tmp = new AuthAutherMenu();
				{
					tmp.setAutherId(this.getId());
					tmp.setMenuId(id);
					// 如果需要排序
					tmp.setSort(++counter * 10);
				}
				map.put(id, tmp);
			}
		}

		List<AuthAutherMenu> list = findAuthAutherMenu();
		for (AuthAutherMenu item : list) {
			if (map.containsKey(item.getMenuId())) {
				String id = item.getId();
				AuthAutherMenu tmp = BeanHelper.copyProperties(item, map.remove(item.getMenuId()));
				tmp.setId(id);
				tmp.setType(getClass().getSimpleName());
				tmp.addOrUpdate();
			} else {
				item.remove();
			}
		}

		for (AuthAutherMenu item : map.values()) {
			item.setType(getClass().getSimpleName());
			item.add();
		}
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
				setUpdateTime(new java.util.Date());
				setCreateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthRole.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.status", getStatus(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthRole.name", 
						Dao.querySize("AuthRole.countAuthRoleByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthRole.code", 
						Dao.querySize("AuthRole.countAuthRoleByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthRole.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S", "ADMIN", "OPERATOR", "USER"};
				 	validate.inArray("AuthRole.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthRole.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("edit".equals(type)) {
			{//初始化值
				setUpdateTime(new java.util.Date());
				setCreateTime(null);
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthRole.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.type2", getType2(), "iff.validate.required", "{0} is required!");
				validate.required("AuthRole.status", getStatus(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthRole.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthRole.name", 
						Dao.querySize("AuthRole.countAuthRoleByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthRole.code", 
						Dao.querySize("AuthRole.countAuthRoleByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthRole.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S", "ADMIN", "OPERATOR", "USER"};
				 	validate.inArray("AuthRole.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthRole.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthRole.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
