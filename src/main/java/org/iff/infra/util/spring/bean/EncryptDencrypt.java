/*******************************************************************************
 * Copyright (c) Jan 15, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring.bean;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 15, 2016
 */
public interface EncryptDencrypt {

	public static final String METHOD_MD5 = "MD5";
	public static final String METHOD_MD5_2 = "MD5-2";
	public static final String METHOD_MD5_FIRST = "MD5-first";
	public static final String METHOD_MD5_SECOND = "MD5-second";
	public static final String METHOD_MD5_BOTH = "MD5-both";
	public static final String METHOD_RSA = "RSA";

	String encrypt(String method, String salt, String value);

	String dencrypt(String method, String salt, String value);
}
