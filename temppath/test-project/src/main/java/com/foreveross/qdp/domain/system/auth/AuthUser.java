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
 * AuthUser
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthUser implements Serializable {

	/** 主键 **/
	private String id;
	/** 用户名称 **/
	private String name;
	/** 状态 **/
	private String status;
	/** 类型 **/
	private String type;
	/** 联系电话 **/
	private String tel;
	/** 联系EMAIL **/
	private String email;
	/** 性别 **/
	private String sex;
	/** 生日 **/
	private Date birthday;
	/** 家庭地址 **/
	private String familyAddr;
	/** 工作地址 **/
	private String workAddr;
	/** 工作电话 **/
	private String workTel;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public AuthUser() {
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
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getFamilyAddr() {
		return familyAddr;
	}

	public void setFamilyAddr(String familyAddr) {
		this.familyAddr = familyAddr;
	}
	public String getWorkAddr() {
		return workAddr;
	}

	public void setWorkAddr(String workAddr) {
		this.workAddr = workAddr;
	}
	public String getWorkTel() {
		return workTel;
	}

	public void setWorkTel(String workTel) {
		this.workTel = workTel;
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
	 * get AuthUser by id	 
	 * Usage : AuthUser.get(id);
	 * </pre>
	 * @param authUser
	 * @return AuthUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthUser get(AuthUser authUser) {
		return Dao.queryOne("AuthUser.getAuthUserById", authUser);
	}
	
	/**
	 * <pre>
	 * get AuthUser by id	 
	 * Usage : AuthUser.get(id);
	 * </pre>
	 * @param id
	 * @return AuthUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthUser get(String id) {
		AuthUser authUser = new AuthUser();
		authUser.setId(id);
		return get(authUser);
	}
	
	/**
	 * <pre>
	 * remove AuthUser by id
	 * Usage : AuthUser.remove(id)
	 * </pre>
	 * @param authUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(AuthUser authUser) {
		authUser.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthUser by id
	 * Usage : AuthUser.remove(id)
	 * </pre>
	 * @param authUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		AuthUser authUser = new AuthUser();
		authUser.setId(id);
		remove(authUser);
	}
	
	/**
	 * <pre>
	 * remove AuthUser by id
	 * Usage : AuthUser.remove(id)
	 * </pre>
	 * @param authUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthUser authUser = new AuthUser();
				authUser.setId(id);
				remove(authUser);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : AuthUser.hasName(AuthUser)
	 * </pre>
	 * @param authUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasName(AuthUser authUser) {
		return Dao.querySize("AuthUser.hasName", authUser) > 0;
	}
	
	/**
	 * <pre>
	 * has Email
	 * Usage : AuthUser.hasEmail(AuthUser)
	 * </pre>
	 * @param authUser
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasEmail(AuthUser authUser) {
		return Dao.querySize("AuthUser.hasEmail", authUser) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthUser
	 * Usage : AuthUser.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthUser add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthUser.insertAuthUser", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthUser
	 * Usage : AuthUser.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthUser update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthUser.updateAuthUser", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthUser
	 * Usage : AuthUser.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public AuthUser addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthUser by id
	 * Usage : AuthUser.remove()
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
		Dao.remove("AuthUser.deleteAuthUser", this);
	}
	
	/**
	 * <pre>
	 * get AuthUser by unique name
	 * Usage : AuthUser.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthUser getByName(String name) {
		AuthUser domain = new AuthUser();
		domain.setName(name);
		return Dao.queryOne("AuthUser.getAuthUserByName", domain);
	}
	
	/**
	 * <pre>
	 * get AuthUser by unique email
	 * Usage : AuthUser.getByEmail(email)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static AuthUser getByEmail(String email) {
		AuthUser domain = new AuthUser();
		domain.setEmail(email);
		return Dao.queryOne("AuthUser.getAuthUserByEmail", domain);
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
				validate.required("AuthUser.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.tel", getTel(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.email", getEmail(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.sex", getSex(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthUser.name", 
						Dao.querySize("AuthUser.countAuthUserByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthUser.email", 
						Dao.querySize("AuthUser.countAuthUserByEmail", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthUser.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthUser.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"F", "M", "N"};
				 	validate.inArray("AuthUser.sex", getSex(), "iff.validate.notInArray", "{0} is not in array!", keys);
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
				validate.required("AuthUser.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.type", getType(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.tel", getTel(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.email", getEmail(), "iff.validate.required", "{0} is required!");
				validate.required("AuthUser.sex", getSex(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthUser.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthUser.name", 
						Dao.querySize("AuthUser.countAuthUserByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("AuthUser.email", 
						Dao.querySize("AuthUser.countAuthUserByEmail", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthUser.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthUser.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"F", "M", "N"};
				 	validate.inArray("AuthUser.sex", getSex(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthUser.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
