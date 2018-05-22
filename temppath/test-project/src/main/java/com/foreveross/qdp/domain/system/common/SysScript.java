/*******************************************************************************
 * Copyright (c) 2018-05-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package com.foreveross.qdp.domain.system.common;

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
 * SysScript
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysScript implements Serializable {

	/** 主键 **/
	private String id;
	/** 名称 **/
	private String name;
	/** 代码 **/
	private String code;
	/** 内容 **/
	private String content;
	/** 参数 **/
	private String parameter;
	/** 分类1 **/
	private String type1;
	/** 分类2 **/
	private String type2;
	/** 描述 **/
	private String description;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;

	public SysScript() {
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
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
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
	 * get SysScript by id	 
	 * Usage : SysScript.get(id);
	 * </pre>
	 * @param sysScript
	 * @return SysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysScript get(SysScript sysScript) {
		return Dao.queryOne("SysScript.getSysScriptById", sysScript);
	}
	
	/**
	 * <pre>
	 * get SysScript by id	 
	 * Usage : SysScript.get(id);
	 * </pre>
	 * @param id
	 * @return SysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysScript get(String id) {
		SysScript sysScript = new SysScript();
		sysScript.setId(id);
		return get(sysScript);
	}
	
	/**
	 * <pre>
	 * remove SysScript by id
	 * Usage : SysScript.remove(id)
	 * </pre>
	 * @param sysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(SysScript sysScript) {
		sysScript.remove();
	}
	
	/**
	 * <pre>
	 * remove SysScript by id
	 * Usage : SysScript.remove(id)
	 * </pre>
	 * @param sysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		SysScript sysScript = new SysScript();
		sysScript.setId(id);
		remove(sysScript);
	}
	
	/**
	 * <pre>
	 * remove SysScript by id
	 * Usage : SysScript.remove(id)
	 * </pre>
	 * @param sysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysScript sysScript = new SysScript();
				sysScript.setId(id);
				remove(sysScript);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : SysScript.hasName(SysScript)
	 * </pre>
	 * @param sysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasName(SysScript sysScript) {
		return Dao.querySize("SysScript.hasName", sysScript) > 0;
	}
	
	/**
	 * <pre>
	 * has Code
	 * Usage : SysScript.hasCode(SysScript)
	 * </pre>
	 * @param sysScript
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasCode(SysScript sysScript) {
		return Dao.querySize("SysScript.hasCode", sysScript) > 0;
	}
	
	/**
	 * <pre>
	 * add SysScript
	 * Usage : SysScript.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysScript add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysScript.insertSysScript", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysScript
	 * Usage : SysScript.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysScript update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysScript.updateSysScript", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysScript
	 * Usage : SysScript.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysScript addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysScript by id
	 * Usage : SysScript.remove()
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
		Dao.remove("SysScript.deleteSysScript", this);
	}
	
	/**
	 * <pre>
	 * get SysScript by unique name
	 * Usage : SysScript.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysScript getByName(String name) {
		SysScript domain = new SysScript();
		domain.setName(name);
		return Dao.queryOne("SysScript.getSysScriptByName", domain);
	}
	
	/**
	 * <pre>
	 * get SysScript by unique code
	 * Usage : SysScript.getByCode(code)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysScript getByCode(String code) {
		SysScript domain = new SysScript();
		domain.setCode(code);
		return Dao.queryOne("SysScript.getSysScriptByCode", domain);
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
				validate.required("SysScript.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.content", getContent(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysScript.name", 
						Dao.querySize("SysScript.countSysScriptByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("SysScript.code", 
						Dao.querySize("SysScript.countSysScriptByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysScript.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysScript.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
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
				validate.required("SysScript.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.code", getCode(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.content", getContent(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysScript.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysScript.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysScript.name", 
						Dao.querySize("SysScript.countSysScriptByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				validate.isTrue("SysScript.code", 
						Dao.querySize("SysScript.countSysScriptByCode", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysScript.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysScript.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysScript.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
