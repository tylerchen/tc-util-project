/*******************************************************************************
 * Copyright (c) 2014-8-25 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

/**
 * A md5 helper.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-8-25
 */
public class MD5Helper {

	private static final String MD5_SALT = ":salt:tc";

	/**
	 * 对字符串进行MD5+salt加密：md5(md5(string)+":salt:tc")。
	 * 这样设计是用于登录验证的场景：用户只需要传输【account:firstSalt】，到后台，后台把firstSalt进行secondSalt就可以与数据密码匹配，整个过程都不出现原始密码。
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2017
	 */
	public static String firstSalt(String inStr) {
		String value = string2MD5(inStr);
		return string2MD5(value + MD5_SALT);
	}

	/**
	 * 对firstSalt字符串进行MD5+salt加密：md5(string +":salt:tc")，数据库存储的是这个密码。
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Nov 8, 2017
	 */
	public static String secondSalt(String inStr) {
		String value = string2MD5(inStr + MD5_SALT);
		return value;
	}

	/**
	 * String to md5-with-salt string
	 * @param inStr
	 * @param salt
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String string2SaltMD5(String inStr, String salt) {
		String value = string2MD5(inStr);
		return string2MD5(value + salt);
	}

	/**
	 * String to double-md5 string
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String string2DoubleMD5(String inStr) {
		String value = string2MD5(inStr);
		return string2MD5(value + StringUtils.reverse(value));
	}

	/**
	 * String to md5 string lowercase.
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuilder hexValue = new StringBuilder();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * encrypt md5 string
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * generate 32-bit md5
	 * @param inStr
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static byte[] md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return new byte[0];
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		return md5.digest(byteArray);
	}
}
