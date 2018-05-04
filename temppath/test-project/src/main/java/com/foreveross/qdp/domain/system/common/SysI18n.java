/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
 * SysI18n
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class SysI18n implements Serializable {

	/** 主键 **/
	private String id;
	/** 国际化Key **/
	private String messageKey;
	/** 国际化内容 **/
	private String messageContent;

	public SysI18n() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	/**
	 * <pre>
	 * get SysI18n by id	 
	 * Usage : SysI18n.get(id);
	 * </pre>
	 * @param sysI18n
	 * @return SysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysI18n get(SysI18n sysI18n) {
		return Dao.queryOne("SysI18n.getSysI18nById", sysI18n);
	}
	
	/**
	 * <pre>
	 * get SysI18n by id	 
	 * Usage : SysI18n.get(id);
	 * </pre>
	 * @param id
	 * @return SysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysI18n get(String id) {
		SysI18n sysI18n = new SysI18n();
		sysI18n.setId(id);
		return get(sysI18n);
	}
	
	/**
	 * <pre>
	 * remove SysI18n by id
	 * Usage : SysI18n.remove(id)
	 * </pre>
	 * @param sysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(SysI18n sysI18n) {
		sysI18n.remove();
	}
	
	/**
	 * <pre>
	 * remove SysI18n by id
	 * Usage : SysI18n.remove(id)
	 * </pre>
	 * @param sysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String id) {
		SysI18n sysI18n = new SysI18n();
		sysI18n.setId(id);
		remove(sysI18n);
	}
	
	/**
	 * <pre>
	 * remove SysI18n by id
	 * Usage : SysI18n.remove(id)
	 * </pre>
	 * @param sysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static void remove(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				SysI18n sysI18n = new SysI18n();
				sysI18n.setId(id);
				remove(sysI18n);
			}
		}
	}
	
	/**
	 * <pre>
	 * has MessageKey
	 * Usage : SysI18n.hasMessageKey(SysI18n)
	 * </pre>
	 * @param sysI18n
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static boolean hasMessageKey(SysI18n sysI18n) {
		return Dao.querySize("SysI18n.hasMessageKey", sysI18n) > 0;
	}
	
	/**
	 * <pre>
	 * add SysI18n
	 * Usage : SysI18n.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysI18n add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysI18n.insertSysI18n", this);
		return this;
	}

	/**
	 * <pre>
	 * update SysI18n
	 * Usage : SysI18n.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysI18n update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("SysI18n.updateSysI18n", this);
		return this;
	}
	
	/**
	 * <pre>
	 * add or update SysI18n
	 * Usage : SysI18n.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public SysI18n addOrUpdate() {
		if (StringUtils.isBlank(getId())) {
			return add();
		} else {
			return update();
		}
	}
	
	/**
	 * <pre>
	 * remove SysI18n by id
	 * Usage : SysI18n.remove()
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
		Dao.remove("SysI18n.deleteSysI18n", this);
	}
	
	/**
	 * <pre>
	 * get SysI18n by unique messageKey
	 * Usage : SysI18n.getByMessageKey(messageKey)
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2018-04-10
	 */
	public static SysI18n getByMessageKey(String messageKey) {
		SysI18n domain = new SysI18n();
		domain.setMessageKey(messageKey);
		return Dao.queryOne("SysI18n.getSysI18nByMessageKey", domain);
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
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("SysI18n.messageKey", getMessageKey(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysI18n.messageKey", 
						Dao.querySize("SysI18n.countSysI18nByMessageKey", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("edit".equals(type)) {
			{//初始化值
			}
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
				validate.required("SysI18n.messageKey", getMessageKey(), "iff.validate.required", "{0} is required!");
			}
			{// validate the primary key 
				validate.required("SysI18n.id", getId(), "iff.validate.required", "{0} is required!");
			}
			{// validate unique
				validate.isTrue("SysI18n.messageKey", 
						Dao.querySize("SysI18n.countSysI18nByMessageKey", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
			}
			{// validate value range
			}
			{// validate foreign key
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				validate.required("SysI18n.id", getId(), "iff.validate.required", "{0} is required!");
			}
		}
		return validate;
	}
}
