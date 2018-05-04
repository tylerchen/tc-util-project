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
 * AuthAccount
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class AuthAccount implements Serializable {

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

	public AuthAccount() {
		super();
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

	/**
	 * <pre>
	 * get AuthAccount by id	 
	 * Usage : AuthAccount.get(id);
	 * </pre>
	 * @param authAccount
	 * @return AuthAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthAccount get(AuthAccount authAccount) {
		return Dao.queryOne("AuthAccount.getAuthAccountById", authAccount);
	}
	
	/**
	 * <pre>
	 * get AuthAccount by id	 
	 * Usage : AuthAccount.get(id);
	 * </pre>
	 * @param id
	 * @return AuthAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthAccount get(String id) {
		AuthAccount authAccount = new AuthAccount();
		authAccount.setId(id);
		return get(authAccount);
	}
	
	/**
	 * <pre>
	 * remove AuthAccount by id
	 * Usage : AuthAccount.remove(id)
	 * </pre>
	 * @param authAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(AuthAccount authAccount) {
		authAccount.remove();
	}
	
	/**
	 * <pre>
	 * remove AuthAccount by id
	 * Usage : AuthAccount.remove(id)
	 * </pre>
	 * @param authAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		AuthAccount authAccount = new AuthAccount();
		authAccount.setId(id);
		remove(authAccount);
	}
	
	/**
	 * <pre>
	 * remove AuthAccount by id
	 * Usage : AuthAccount.remove(id)
	 * </pre>
	 * @param authAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				AuthAccount authAccount = new AuthAccount();
				authAccount.setId(id);
				remove(authAccount);
			}
		}
	}
	
	/**
	 * <pre>
	 * has LoginEmail
	 * Usage : AuthAccount.hasLoginEmail(AuthAccount)
	 * </pre>
	 * @param authAccount
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasLoginEmail(AuthAccount authAccount) {
		return Dao.querySize("AuthAccount.hasLoginEmail", authAccount) > 0;
	}
	
	/**
	 * <pre>
	 * add AuthAccount
	 * Usage : AuthAccount.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthAccount add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAccount.insertAuthAccount", this);
		return this;
	}

	/**
	 * <pre>
	 * update AuthAccount
	 * Usage : AuthAccount.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthAccount update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("AuthAccount.updateAuthAccount", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update AuthAccount
	 * Usage : AuthAccount.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public AuthAccount addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove AuthAccount by id
	 * Usage : AuthAccount.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}

		// 删除AuthAutherRole
		removeAuthAutherRole();
		// 删除本对象
		Dao.remove("AuthAccount.deleteAuthAccount", this);
	}
	
	/**
	 * <pre>
	 * get AuthAccount by unique loginEmail
	 * Usage : AuthAccount.getByLoginEmail(loginEmail)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static AuthAccount getByLoginEmail(String loginEmail) {
		AuthAccount domain = new AuthAccount();
		domain.setLoginEmail(loginEmail);
		return Dao.queryOne("AuthAccount.getAuthAccountByLoginEmail", domain);
	}
	
	

			
	/**
	 * <pre>
	 * find AuthAutherRole.
	 * Usage : AuthAccount.findAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthRole> findAuthRole() {
		AuthAccount condition = new AuthAccount();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthAccount.pageFindAuthRoleByAuthAutherRoleMap", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthRole.class).getRows();
	}
	/**
	 * <pre>
	 * find AuthAutherRole.
	 * Usage : AuthAccount.findAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public List<AuthAutherRole> findAuthAutherRole() {
		AuthAccount condition = new AuthAccount();
		{
			condition.setId(this.getId());
		}
		Page page = Dao.queryPage("AuthAccount.pageFindAuthAutherRoleMap",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(AuthAutherRole.class).getRows();
	}
	/**
	 * <pre>
	 * remove AuthAutherRole.
	 * Usage : AuthAccount.removeAuthAutherRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
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
	 * Usage : AuthAccount.assignAuthRole()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public void assignAuthRoleByIds(String[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(AuthAccount.get(this.getId()));
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
				if( StringUtils.isNotBlank(getLoginPasswd()) ){
					setLoginPasswd(org.iff.infra.util.MD5Helper.secondSalt(org.iff.infra.util.MD5Helper.firstSalt(getLoginPasswd())));
				} else {
					setLoginPasswd( null );
				}
				setUpdateTime(new java.util.Date());
				setCreateTime(new java.util.Date());
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.email("AuthAccount.loginEmail", getLoginEmail(), "iff.validate.email", "{0} is not valid email!");
				validate.required("AuthAccount.loginPasswd", getLoginPasswd(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAccount.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAccount.type", getType(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthAccount.loginEmail", 
						Dao.querySize("AuthAccount.countAuthAccountByLoginEmail", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthAccount.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthAccount.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// clean the empty data.
					if ((getUserId() instanceof String) && getUserId().length() < 1) {
						setUserId(null);
					}
				}
			}
		} else if ("edit".equals(type)) {
			{//初始化值
				if( StringUtils.isNotBlank(getLoginPasswd()) ){
					setLoginPasswd(org.iff.infra.util.MD5Helper.secondSalt(org.iff.infra.util.MD5Helper.firstSalt(getLoginPasswd())));
				} else {
					setLoginPasswd( null );
				}
				setUpdateTime(new java.util.Date());
				setCreateTime(null);
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.email("AuthAccount.loginEmail", getLoginEmail(), "iff.validate.email", "{0} is not valid email!");
				validate.required("AuthAccount.status", getStatus(), "iff.validate.required", "{0} is required!");
				validate.required("AuthAccount.type", getType(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("AuthAccount.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("AuthAccount.loginEmail", 
						Dao.querySize("AuthAccount.countAuthAccountByLoginEmail", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"Y", "N"};
				 	validate.inArray("AuthAccount.status", getStatus(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("AuthAccount.type", getType(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
				{// clean the empty data.
					if ((getUserId() instanceof String) && getUserId().length() < 1) {
						setUserId(null);
					}
				}
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("AuthAccount.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
