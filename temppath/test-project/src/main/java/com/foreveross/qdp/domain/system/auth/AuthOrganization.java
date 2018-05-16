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
 * AuthOrganization
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthOrganization implements Serializable {

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

	public AuthOrganization() {
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

	/**
	 * <pre>
	 * get AuthOrganization by id	 
	 * Usage : AuthOrganization.get(id);
	 * </pre>
	 * @param authOrganization
	 * @return AuthOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthOrganization get(AuthOrganization authOrganization) {
		return Dao.queryOne("AuthOrganization.getAuthOrganizationById", authOrganization);
	}
	
	/**
	 * <pre>
	 * get AuthOrganization by id	 
	 * Usage : AuthOrganization.get(id);
	 * </pre>
	 * @param id
	 * @return AuthOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthOrganization get(String id) {
		AuthOrganization authOrganization = new AuthOrganization();
		authOrganization.setId(id);
		return get(authOrganization);
	}
	
	/**
	 * <pre>
	 * remove AuthOrganization by id
	 * Usage : AuthOrganization.remove(id)
	 * </pre>
	 * @param authOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(AuthOrganization authOrganization) {
		authOrganization.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthOrganization by id
	 * Usage : AuthOrganization.remove(id)
	 * </pre>
	 * @param authOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		AuthOrganization authOrganization = new AuthOrganization();
		authOrganization.setId(id);
		remove(authOrganization);
	}
	
	/**
	 * <pre>
	 * remove AuthOrganization by id
	 * Usage : AuthOrganization.remove(id)
	 * </pre>
	 * @param authOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthOrganization authOrganization = new AuthOrganization();
				authOrganization.setId(id);
				remove(authOrganization);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : AuthOrganization.hasName(AuthOrganization)
	 * </pre>
	 * @param authOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasName(AuthOrganization authOrganization) {
		return Dao.querySize("AuthOrganization.hasName", authOrganization) > 0;
	}
	
	/**
	 * <pre>
	 * has Code
	 * Usage : AuthOrganization.hasCode(AuthOrganization)
	 * </pre>
	 * @param authOrganization
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasCode(AuthOrganization authOrganization) {
		return Dao.querySize("AuthOrganization.hasCode", authOrganization) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthOrganization
	 * Usage : AuthOrganization.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthOrganization add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthOrganization.insertAuthOrganization", this);
		if (StringUtils.isBlank(getRootId())) {// set root id for tree.
			setRootId(getId());
			update();
		}
		return this;
	}

	/**
	 * <pre>
	 * update AuthOrganization
	 * Usage : AuthOrganization.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthOrganization update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthOrganization.updateAuthOrganization", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthOrganization
	 * Usage : AuthOrganization.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthOrganization addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthOrganization by id
	 * Usage : AuthOrganization.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}

		// 删除AuthAutherMenu
		removeAuthAutherMenu();

		// 删除AuthOrganizationUser
		removeAuthOrganizationUser();

		// 删除AuthAutherRole
		removeAuthAutherRole();
		// 删除本对象
		Dao.remove("AuthOrganization.deleteAuthOrganization", this);
	}
	
	/**
	 * <pre>
	 * get AuthOrganization by unique name
	 * Usage : AuthOrganization.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthOrganization getByName(String name) {
		AuthOrganization domain = new AuthOrganization();
		domain.setName(name);
		return Dao.queryOne("AuthOrganization.getAuthOrganizationByName", domain);
	}
	
	/**
	 * <pre>
	 * get AuthOrganization by unique code
	 * Usage : AuthOrganization.getByCode(code)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthOrganization getByCode(String code) {
		AuthOrganization domain = new AuthOrganization();
		domain.setCode(code);
		return Dao.queryOne("AuthOrganization.getAuthOrganizationByCode", domain);
	}
	
	

			
	/**
	 * <pre>
	 * find AuthAutherMenu.
	 * Usage : AuthOrganization.findAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthMenu> findAuthMenu() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthMenuByAuthAutherMenuMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthMenu.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthAutherMenu.
	 * Usage : AuthOrganization.findAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthAutherMenu> findAuthAutherMenu() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthAutherMenuMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthAutherMenu.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthAutherMenu.
	 * Usage : AuthOrganization.removeAuthAutherMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
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
	 * Usage : AuthOrganization.assignAuthMenu()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void assignAuthMenuByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthOrganization.get(this.getId()));
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
	 * find AuthOrganizationUser.
	 * Usage : AuthOrganization.findAuthOrganizationUser()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthUser> findAuthUser() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthUserByAuthOrganizationUserMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthUser.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthOrganizationUser.
	 * Usage : AuthOrganization.findAuthOrganizationUser()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthOrganizationUser> findAuthOrganizationUser() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthOrganizationUserMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthOrganizationUser.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthOrganizationUser.
	 * Usage : AuthOrganization.removeAuthOrganizationUser()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void removeAuthOrganizationUser() {
		List<AuthOrganizationUser> list = findAuthOrganizationUser();
		for (AuthOrganizationUser item : list) {
			item.remove();
		}
	}
	
	/**
	 * <pre>
	 * assign AuthUser by id
	 * Usage : AuthOrganization.assignAuthUser()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void assignAuthUserByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthOrganization.get(this.getId()));
		Map<String, AuthOrganizationUser> map = new HashMap<String, AuthOrganizationUser>();
		if (ids != null) {
			int counter = 0;
			for (String id : ids) {
				AuthUser ref = AuthUser.get(id);
				Assert.notNull(ref);
				AuthOrganizationUser tmp = new AuthOrganizationUser();
				{
					tmp.setOrganizationId(this.getId());
					tmp.setUserId(id);
				}
				map.put(id, tmp);
			}
		}

		List<AuthOrganizationUser> list = findAuthOrganizationUser();
		for (AuthOrganizationUser item : list) {
			if (map.containsKey(item.getUserId())) {
				String id = item.getId();
				AuthOrganizationUser tmp = BeanHelper.copyProperties(item, map.remove(item.getUserId()));
				tmp.setId(id);
				
				tmp.addOrUpdate();
			} else {
				item.remove();
			}
		}

		for (AuthOrganizationUser item : map.values()) {
			
			item.add();
		}
	}

			
	/**
	 * <pre>
	 * find AuthAutherRole.
	 * Usage : AuthOrganization.findAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthRole> findAuthRole() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthRoleByAuthAutherRoleMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthRole.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthAutherRole.
	 * Usage : AuthOrganization.findAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public List<AuthAutherRole> findAuthAutherRole() {
		AuthOrganization condition = new AuthOrganization();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthOrganization.pageFindAuthAutherRoleMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthAutherRole.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthAutherRole.
	 * Usage : AuthOrganization.removeAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void removeAuthAutherRole() {
		List<AuthAutherRole> list = findAuthAutherRole();
		for (AuthAutherRole item : list) {
			item.remove();
		}
	}
	
	/**
	 * <pre>
	 * assign AuthRole by id
	 * Usage : AuthOrganization.assignAuthRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public void assignAuthRoleByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthOrganization.get(this.getId()));
		Map<String, AuthAutherRole> map = new HashMap<String, AuthAutherRole>();
		if (ids != null) {
			int counter = 0;
			for (String id : ids) {
				AuthRole ref = AuthRole.get(id);
				Assert.notNull(ref);
				AuthAutherRole tmp = new AuthAutherRole();
				{
					tmp.setAutherId(this.getId());
					tmp.setRoleId(id);
				}
				map.put(id, tmp);
			}
		}

		List<AuthAutherRole> list = findAuthAutherRole();
		for (AuthAutherRole item : list) {
			if (map.containsKey(item.getRoleId())) {
				String id = item.getId();
				AuthAutherRole tmp = BeanHelper.copyProperties(item, map.remove(item.getRoleId()));
				tmp.setId(id);
				tmp.setType(getClass().getSimpleName());
				tmp.addOrUpdate();
			} else {
				item.remove();
			}
		}

		for (AuthAutherRole item : map.values()) {
			item.setType(getClass().getSimpleName());
			item.add();
		}
	}
	
	/**
	 * <pre>
	 * find all menu by rootId.
	 * Usage : AuthOrganization.findByRootId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static List<AuthOrganization> findByRootId(String rootId) {
		if ( StringUtils.isBlank(String.valueOf(rootId)) ) {
			return new ArrayList<AuthOrganization>();
		}
		AuthOrganization condition = new AuthOrganization();
		condition.setRootId(rootId);
		Page page = Page.offsetPage(0, 100000, null);
		
		List<AuthOrganization> list = Dao.queryList("AuthOrganization.pageFindAuthOrganization", MapHelper.toMap("page", page, "vo", condition));
		return list;
	}

	/**
	 * <pre>
	 * find all menu by parentId.
	 * Usage : AuthOrganization.findByParentId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static List<AuthOrganization> findByParentId(String parentId) {
		if ( StringUtils.isBlank(String.valueOf(parentId)) ) {
			return new ArrayList<AuthOrganization>();
		}
		AuthOrganization condition = new AuthOrganization();
		condition.setParentId(parentId);
		Page page = Page.offsetPage(0, 100000, null);
		
		List<AuthOrganization> list = Dao.queryList("AuthOrganization.pageFindAuthOrganization", 
				MapHelper.toMap("page", page, "vo", condition));
		return list;
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
				setUpdateTime(new java.util.Date());
				setCreateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthOrganization.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.isUseRootMenu", getIsUseRootMenu(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthOrganization.name", 
						Dao.querySize("AuthOrganization.countAuthOrganizationByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthOrganization.code", 
						Dao.querySize("AuthOrganization.countAuthOrganizationByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthOrganization.isUseRootMenu", getIsUseRootMenu(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthOrganization.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthOrganization.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// clean the empty data.
					if ((getParentId() instanceof String) && getParentId().length() < 1) {
						setParentId(null);
					}
				}
				{// clean the empty data.
					if ((getRootId() instanceof String) && getRootId().length() < 1) {
						setRootId(null);
					}
				}
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
				validate.required("AuthOrganization.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.isUseRootMenu", getIsUseRootMenu(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthOrganization.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthOrganization.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthOrganization.name", 
						Dao.querySize("AuthOrganization.countAuthOrganizationByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthOrganization.code", 
						Dao.querySize("AuthOrganization.countAuthOrganizationByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthOrganization.isUseRootMenu", getIsUseRootMenu(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthOrganization.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthOrganization.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// clean the empty data.
					if ((getParentId() instanceof String) && getParentId().length() < 1) {
						setParentId(null);
					}
				}
				{// clean the empty data.
					if ((getRootId() instanceof String) && getRootId().length() < 1) {
						setRootId(null);
					}
				}
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthOrganization.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
