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
 * SysOpenreport
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-05-16
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysOpenreport implements Serializable {

	/** 主键 **/
	private String id;
	/** 名称 **/
	private String name;
	/** 配置类别 **/
	private String confType;
	/** XML内容 **/
	private String xmlContent;
	/** 分类1 **/
	private String type1;
	/** 分类2 **/
	private String type2;
	/** 描述 **/
	private String description;
	/** 修改时间 **/
	private Date updateTime;
	/** 创建时间 **/
	private Date createTime;

	public SysOpenreport() {
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
	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}
	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
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
	 * get SysOpenreport by id	 
	 * Usage : SysOpenreport.get(id);
	 * </pre>
	 * @param sysOpenreport
	 * @return SysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysOpenreport get(SysOpenreport sysOpenreport) {
		return Dao.queryOne("SysOpenreport.getSysOpenreportById", sysOpenreport);
	}
	
	/**
	 * <pre>
	 * get SysOpenreport by id	 
	 * Usage : SysOpenreport.get(id);
	 * </pre>
	 * @param id
	 * @return SysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysOpenreport get(String id) {
		SysOpenreport sysOpenreport = new SysOpenreport();
		sysOpenreport.setId(id);
		return get(sysOpenreport);
	}
	
	/**
	 * <pre>
	 * remove SysOpenreport by id
	 * Usage : SysOpenreport.remove(id)
	 * </pre>
	 * @param sysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(SysOpenreport sysOpenreport) {
		sysOpenreport.remove();
	}
	
	/**
	 * <pre>
	 * remove SysOpenreport by id
	 * Usage : SysOpenreport.remove(id)
	 * </pre>
	 * @param sysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String id) {
		SysOpenreport sysOpenreport = new SysOpenreport();
		sysOpenreport.setId(id);
		remove(sysOpenreport);
	}
	
	/**
	 * <pre>
	 * remove SysOpenreport by id
	 * Usage : SysOpenreport.remove(id)
	 * </pre>
	 * @param sysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysOpenreport sysOpenreport = new SysOpenreport();
				sysOpenreport.setId(id);
				remove(sysOpenreport);
			}
		}
	}
	
	/**
	 * <pre>
	 * has Name
	 * Usage : SysOpenreport.hasName(SysOpenreport)
	 * </pre>
	 * @param sysOpenreport
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static boolean hasName(SysOpenreport sysOpenreport) {
		return Dao.querySize("SysOpenreport.hasName", sysOpenreport) > 0;
	}
	
	/**
	 * <pre>
	 * add SysOpenreport
	 * Usage : SysOpenreport.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysOpenreport add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysOpenreport.insertSysOpenreport", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysOpenreport
	 * Usage : SysOpenreport.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysOpenreport update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysOpenreport.updateSysOpenreport", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysOpenreport
	 * Usage : SysOpenreport.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public SysOpenreport addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysOpenreport by id
	 * Usage : SysOpenreport.remove()
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
		Dao.remove("SysOpenreport.deleteSysOpenreport", this);
	}
	
	/**
	 * <pre>
	 * get SysOpenreport by unique name
	 * Usage : SysOpenreport.getByName(name)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-05-16
	 */
	public static SysOpenreport getByName(String name) {
		SysOpenreport domain = new SysOpenreport();
		domain.setName(name);
		return Dao.queryOne("SysOpenreport.getSysOpenreportByName", domain);
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
				validate.required("SysOpenreport.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.confType", getConfType(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysOpenreport.name", 
						Dao.querySize("SysOpenreport.countSysOpenreportByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysOpenreport.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysOpenreport.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
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
				validate.required("SysOpenreport.name", getName(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.confType", getConfType(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.type1", getType1(), "iff.validate.required", "{0} is required!");
				validate.required("SysOpenreport.type2", getType2(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysOpenreport.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysOpenreport.name", 
						Dao.querySize("SysOpenreport.countSysOpenreportByName", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysOpenreport.type1", getType1(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			 	{
				 	String[] keys = new String[]{"S"};
				 	validate.inArray("SysOpenreport.type2", getType2(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysOpenreport.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
