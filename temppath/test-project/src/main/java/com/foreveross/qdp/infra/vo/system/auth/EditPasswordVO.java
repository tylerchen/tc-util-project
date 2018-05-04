/*******************************************************************************
 * Copyright (c) 2018-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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
 * 帐户管理 - EditPasswordVO
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2018-04-10
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "EditPassword")
@SuppressWarnings("serial")
public class EditPasswordVO implements Serializable {


	/** 主键 **/
	private String id;
	/** 原密码 **/
	private String loginPasswdOld;
	/** 新密码 **/
	private String loginPasswd;
	/** 重复新密码 **/
	private String loginPasswdNew;

	public EditPasswordVO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginPasswdOld() {
		return loginPasswdOld;
	}

	public void setLoginPasswdOld(String loginPasswdOld) {
		this.loginPasswdOld = loginPasswdOld;
	}
	public String getLoginPasswd() {
		return loginPasswd;
	}

	public void setLoginPasswd(String loginPasswd) {
		this.loginPasswd = loginPasswd;
	}
	public String getLoginPasswdNew() {
		return loginPasswdNew;
	}

	public void setLoginPasswdNew(String loginPasswdNew) {
		this.loginPasswdNew = loginPasswdNew;
	}

}
