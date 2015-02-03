/*******************************************************************************
 * Copyright (c) 2012-11-21 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2012-11-21
 */
public final class StringHelper {

	private static final String ENCRYPTOR_PASSWD = "@@@ENCRYPTOR_PASSWD$$$";
	public static final int HIGHEST_SPECIAL = '>' + 1;
	public static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL][];
	static {
		specialCharactersRepresentation['&'] = "&amp;".toCharArray();
		specialCharactersRepresentation['<'] = "&lt;".toCharArray();
		specialCharactersRepresentation['>'] = "&gt;".toCharArray();
		specialCharactersRepresentation['"'] = "&#034;".toCharArray();
		specialCharactersRepresentation['\''] = "&#039;".toCharArray();
	}

	private StringHelper() {
	}

	public static String concat(String... strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			if (str == null) {
				str = "";
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace the xml chars:
	 * <code>&</code> to <code>&amp;amp;</code>
	 * <code><</code> to <code>&amp;lt;</code>
	 * <code>></code> to <code>&amp;gt;</code>
	 * <code>"</code> to <code>&amp;#034;</code>
	 * <code>'</code> to <code>&amp;#039;</code>
	 * </pre>
	 * @param str xml or html
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceXmlChar(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(str.length() * 2);
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			if (c < HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					sb.append(escaped);
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace string block to the specified value.
	 * the string block should wrap with {a-zA-z0-9}, such as: {name}, {helloWorld}
	 * this kind of the string block will not parse, such as: {name a}, {name,a}
	 * examples:
	 * hello {name} + "world" => hello world
	 * </pre>
	 * @param str
	 * @param replaces
	 * @param blank
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceBlock(String str, Map<String, Object> replaces, String blank) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int i = 0;
		replaces = replaces == null ? new HashMap<String, Object>() : replaces;
		StringBuffer sb = new StringBuffer(str);
		Matcher matcher = Pattern.compile("(\\{\\w*\\})").matcher(str);
		int offset = 0;
		for (; matcher.find(); i++) {
			String group = matcher.group();
			String substring = group.substring(1, group.length() - 1);
			String replace = replaces.containsKey(substring) ? String.valueOf(replaces.get(substring))
					: (blank == null ? group : blank);
			int start = matcher.start(), end = matcher.end();
			sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
			offset += replace.length() - (end - start);
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace string block to the specified value.
	 * the string block should wrap with {a-zA-z0-9}, such as: {name}, {helloWorld}
	 * this kind of the string block will not parse, such as: {name a}, {name,a}
	 * examples:
	 * hello {name} + "world" => hello world
	 * </pre>
	 * @param str
	 * @param replaces
	 * @param blank
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceBlock(String str, Object[] replaces, String blank) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int i = 0, len = replaces != null ? replaces.length : 0;
		StringBuffer sb = new StringBuffer(str);
		Matcher matcher = Pattern.compile("(\\{\\w*\\})").matcher(str);
		int offset = 0;
		for (; matcher.find(); i++) {
			String replace = i < len ? String.valueOf(replaces[i]) : (blank == null ? matcher.group() : blank);
			int start = matcher.start(), end = matcher.end();
			sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
			offset += replace.length() - (end - start);
		}
		return sb.toString();
	}

	public static String pathConcat(String... paths) {
		if (paths == null || paths.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String path : paths) {
			if (path == null) {
				path = "";
			}
			sb.append(path).append(getFileSeparator());
		}
		return pathBuild(sb.toString(), getFileSeparator());
	}

	/**
	 * <pre>
	 * build the beautiful path.
	 * example:
	 * c:\a\\\\b\\\c   =>  c:\a\b\c
	 * c:\a\/b\\\c   =>  c:\a\b\c
	 * /a\/b\\\c   =>  /a/b/c
	 * </pre>
	 * @param str
	 * @param fileSeparator
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String pathBuild(String str, String fileSeparator) {
		if (str == null || str.length() == 0) {
			return "";
		}
		String replace = fileSeparator;
		int i = 0;
		StringBuffer sb = new StringBuffer(str);
		{
			Matcher matcher = Pattern.compile("((\\\\+|/+)+)").matcher(str);
			int offset = 0;
			for (; matcher.find(); i++) {
				int start = matcher.start(), end = matcher.end();
				sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
				offset += replace.length() - (end - start);
			}
		}
		{
			Matcher matcher = Pattern.compile("((\\\\+|/+)+)").matcher(sb.toString());
			int offset = 0;
			for (; matcher.find(); i++) {
				int start = matcher.start(), end = matcher.end();
				sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
				offset += replace.length() - (end - start);
			}
		}
		return sb.toString();
	}

	public static String getFileSeparator() {
		return File.separator;
	}

	public static String getNotNullValue(Object value) {
		return value == null ? "" : value.toString();
	}

	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	public static String uuid() {
		return minUUID();
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return NumberHelper.toString(hi | (val & (hi - 1)), NumberHelper.MAX_RADIX).substring(1);
	}

	/** 
	 * 以62进制（字母加数字）生成19位UUID，最短的UUID 
	 *  
	 * @return 
	 */
	public static String minUUID() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		{
			long mostSignificantBits = uuid.getMostSignificantBits();
			sb.append(digits(mostSignificantBits >> 32, 8));
			sb.append(digits(mostSignificantBits >> 16, 4));
			sb.append(digits(mostSignificantBits, 4));
		}
		{
			long leastSignificantBits = uuid.getLeastSignificantBits();
			sb.append(digits(leastSignificantBits >> 48, 4));
			sb.append(digits(leastSignificantBits, 12));
		}
		return sb.toString();
	}

	/**
	 * 编写一个截取字符串的函数，输入为一个字符串和字节数，输出为按字节截取的字符串。
	 * 但是要保证汉字不被截半个，如"我ABC"4，应该截为"我AB"，输入"我ABC汉DEF"，6，
	 * 应该输出为"我ABC"而不是"我ABC+汉的半个"
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public static String subUniCodeString(String str, int length) {
		if (str == null) {
			return null;
		}
		int byteCount = 0;//字节计数
		int charCount = 0;//字符计数
		for (int i = 0; i < length; i++) {
			if (i >= str.length()) {
				break;
			}
			charCount++;
			int charIndex = (int) str.charAt(i);
			if (charIndex > 128) {
				byteCount += 2;
			} else {
				byteCount += 1;
			}
			if (byteCount > length) {
				charCount--;
				break;
			}
		}
		if (charCount < str.length()) {
			return str.substring(0, charCount);
		}
		return str;
	}

	/**
	 * safe substring, without exception
	 * @param str
	 * @param length
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2014-9-25
	 */
	public static String substr(String str, int length) {
		if (str == null) {
			return str;
		}
		if (length < str.length()) {
			return str.substring(0, length);
		}
		return str;
	}

	/**
	 * <pre>
	 * 1. value is String: return value
	 * 2. value is null: return null
	 * 3. value is Object: return value.toString()
	 * </pre>
	 * @param value
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2014-8-23
	 */
	public static String valueOf(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}

	static final String EX = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

	public static String encodeURIComponent(String input) {
		if (input == null) {
			return "";
		}
		int l = input.length();
		StringBuilder o = new StringBuilder(l * 3);
		for (int i = 0; i < l; i++) {
			String e = input.substring(i, i + 1);
			if (EX.indexOf(e) == -1) {
				byte[] b = new byte[0];
				try {
					b = e.getBytes("UTF-8");
				} catch (Exception e1) {
				}
				o.append(getHex(b));
				continue;
			}
			o.append(e);
		}
		return o.toString();
	}

	public static String getHex(byte buf[]) {
		StringBuilder o = new StringBuilder(buf.length * 3);
		for (int i = 0; i < buf.length; i++) {
			int n = (int) buf[i] & 0xff;
			o.append("%");
			if (n < 0x10) {
				o.append("0");
			}
			o.append(Long.toString(n, 16).toUpperCase());
		}
		return o.toString();
	}

	public static void main(String[] args) {
		System.out.println(subUniCodeString("我ABC汉DEF", 60));
		System.out.println(subUniCodeString("我ABC汉DEF", 4));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			minUUID();
		}
		System.out.println((System.currentTimeMillis() - start));
		System.out.println(uuid());
	}

}
