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
 * AuthMenu
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthMenu implements Serializable {

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

	public AuthMenu() {
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

	/**
	 * <pre>
	 * get AuthMenu by id	 
	 * Usage : AuthMenu.get(id);
	 * </pre>
	 * @param authMenu
	 * @return AuthMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthMenu get(AuthMenu authMenu) {
		return Dao.queryOne("AuthMenu.getAuthMenuById", authMenu);
	}
	
	/**
	 * <pre>
	 * get AuthMenu by id	 
	 * Usage : AuthMenu.get(id);
	 * </pre>
	 * @param id
	 * @return AuthMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthMenu get(String id) {
		AuthMenu authMenu = new AuthMenu();
		authMenu.setId(id);
		return get(authMenu);
	}
	
	/**
	 * <pre>
	 * remove AuthMenu by id
	 * Usage : AuthMenu.remove(id)
	 * </pre>
	 * @param authMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(AuthMenu authMenu) {
		authMenu.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthMenu by id
	 * Usage : AuthMenu.remove(id)
	 * </pre>
	 * @param authMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		AuthMenu authMenu = new AuthMenu();
		authMenu.setId(id);
		remove(authMenu);
	}
	
	/**
	 * <pre>
	 * remove AuthMenu by id
	 * Usage : AuthMenu.remove(id)
	 * </pre>
	 * @param authMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthMenu authMenu = new AuthMenu();
				authMenu.setId(id);
				remove(authMenu);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : AuthMenu.hasName(AuthMenu)
	 * </pre>
	 * @param authMenu
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasName(AuthMenu authMenu) {
		return Dao.querySize("AuthMenu.hasName", authMenu) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthMenu
	 * Usage : AuthMenu.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthMenu add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthMenu.insertAuthMenu", this);
		if (StringUtils.isBlank(getRootId())) {// set root id for tree.
			setRootId(getId());
			update();
		}
		reOrder( getParentId() );
		return this;
	}

	/**
	 * <pre>
	 * update AuthMenu
	 * Usage : AuthMenu.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthMenu update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthMenu.updateAuthMenu", this);
		reOrder( getParentId() );
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthMenu
	 * Usage : AuthMenu.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthMenu addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthMenu by id
	 * Usage : AuthMenu.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		// 删除本对象
		Dao.remove("AuthMenu.deleteAuthMenu", this);
	}
	
	/**
	 * <pre>
	 * get AuthMenu by unique name
	 * Usage : AuthMenu.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthMenu getByName(String name) {
		AuthMenu domain = new AuthMenu();
		domain.setName(name);
		return Dao.queryOne("AuthMenu.getAuthMenuByName", domain);
	}
	
	
	
	/**
	 * <pre>
	 * find all menu by rootId.
	 * Usage : AuthMenu.findByRootId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static List<AuthMenu> findByRootId(String rootId) {
		if ( StringUtils.isBlank(String.valueOf(rootId)) ) {
			return new ArrayList<AuthMenu>();
		}
		AuthMenu condition = new AuthMenu();
		condition.setRootId(rootId);
		Page page = Page.offsetPage(0, 100000, null);
		page.addAscOrderBy("menuIndex");
		List<AuthMenu> list = Dao.queryList("AuthMenu.pageFindAuthMenu", MapHelper.toMap("page", page, "vo", condition));
		return list;
	}

	/**
	 * <pre>
	 * find all menu by parentId.
	 * Usage : AuthMenu.findByParentId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static List<AuthMenu> findByParentId(String parentId) {
		if ( StringUtils.isBlank(String.valueOf(parentId)) ) {
			return new ArrayList<AuthMenu>();
		}
		AuthMenu condition = new AuthMenu();
		condition.setParentId(parentId);
		Page page = Page.offsetPage(0, 100000, null);
		page.addAscOrderBy("menuIndex");
		List<AuthMenu> list = Dao.queryList("AuthMenu.pageFindAuthMenu", 
				MapHelper.toMap("page", page, "vo", condition));
		return list;
	}

	/**
	 * <pre>
	 * check menu level.
	 * Usage : AuthMenu.checkMenuLevel()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean checkMenuLevel(String parentId) {
		if ( parentId == null || StringUtils.isBlank(String.valueOf(parentId)) ) {
			return true;
		}
		AuthMenu domain = get(parentId);
		int count = 0;
		while ( domain != null ) {
			count = count + 1;
			if ( StringUtils.equals(domain.getId(), domain.getRootId()) || 
					StringUtils.equals(domain.getId(), domain.getParentId()) || count > 100) {// 如果当前节点就是根节点
				break;
			}
			domain = get( domain.getParentId() );
		}
		if ( domain != null && (domain.getMaxLevel() - 1) >= count ) {
			return true;
		}
		return false;
	}
	
	/**
	 * <pre>
	 * Re-order by parentId.
	 * Usage : AuthMenu.reOrder()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static List<AuthMenu> reOrder(String parentId) {
		List<AuthMenu> items = findByParentId(parentId);
		int i = 1;
		for( AuthMenu item : items ) {
			item.setMenuIndex( i++ * 10 );
			item.updateByMenuIdex();
		}
		return items;
	}
	
	/**
	 * <pre>
	 * update AuthMenu by menu index.
	 * Usage : AuthMenu.updateByMenuIdex()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthMenu updateByMenuIdex() {
		Dao.save("AuthMenu.updateAuthMenu", this);
		return this;
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
			{// 设置默认值
				AuthMenu parent = get(getParentId());
				if( parent != null ) {
					setType1(parent.getType1());
					setType2(parent.getType2());
					setRootId(parent.getRootId());
					setMaxLevel(parent.getMaxLevel());
				}
			}
			{// 检查最大层级
				validate.isTrue("AuthMenu.maxLevel", checkMenuLevel(getParentId()), "AuthMenu.maxLevel",
						"{0} level is deep than root menu setting.");
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthMenu.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.resourceId", getResourceId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthMenu.name", 
						Dao.querySize("AuthMenu.countAuthMenuByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthMenu.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthMenu.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthResource authResource = com.foreveross.qdp.domain.system.auth.AuthResource.get(getResourceId());
					validate.required("AuthResource", authResource, "iff.validate.required", "{0} is required!");
				}
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
			{// 设置默认值
				AuthMenu parent = get(getParentId());
				if( parent != null ) {
					setType1(parent.getType1());
					setType2(parent.getType2());
					setRootId(parent.getRootId());
					setMaxLevel(parent.getMaxLevel());
				}
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("AuthMenu.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.resourceId", getResourceId(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("AuthMenu.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthMenu.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthMenu.name", 
						Dao.querySize("AuthMenu.countAuthMenuByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthMenu.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthMenu.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// check the foreign key.
					com.foreveross.qdp.domain.system.auth.AuthResource authResource = com.foreveross.qdp.domain.system.auth.AuthResource.get(getResourceId());
					validate.required("AuthResource", authResource, "iff.validate.required", "{0} is required!");
				}
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
				validate.required("AuthMenu.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
