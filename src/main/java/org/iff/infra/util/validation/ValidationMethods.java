package org.iff.infra.util.validation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.iff.infra.util.GsonHelper;

/**
 * 验证方法
 */
public class ValidationMethods {

	/**
	 * value whether in array
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean inArray(Object value, Object[] array) {
		return ArrayUtils.contains(array, value);
	}

	/**
	 * is json String
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean json(Object value) {
		if (value != null) {
			try {
				String json = value.toString();
				return GsonHelper.toJson(json) != null;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证值对象是否存在
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean required(Object value) {
		if (value != null) {
			if (value instanceof CharSequence) {
				return ((CharSequence) value).length() > 0;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证值对象是否为有效的Email地址
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean email(Object value) {
		return required(value) && value instanceof CharSequence
				&& EmailValidator.getInstance().isValid(value.toString());
	}

	/**
	 * 验证值对象是否为有效的URL
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean url(Object value) {
		return required(value) && value instanceof CharSequence && UrlValidator.getInstance().isValid(value.toString());
	}

	/**
	 * 验证值对象是否为有效的银行卡
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean creditCard(Object value) {
		if (!(required(value) && value instanceof CharSequence)) {
			return false;
		}
		String s = value.toString();
		return !(s.length() < 13 || s.length() > 19) && luhnCheck(s);
	}

	/**
	 * LUHN算法验证
	 *
	 * @param number 数字字符串
	 * @return 验证通过则返回true
	 */
	private static boolean luhnCheck(String number) {
		int digits = number.length();
		int oddOrEven = digits & 1;
		long sum = 0;

		for (int count = 0; count < digits; count++) {
			int digit;
			try {
				digit = Integer.parseInt(number.charAt(count) + "");
			} catch (Exception e) {
				return false;
			}

			if (((count & 1) ^ oddOrEven) == 0) { // not
				digit *= 2;
				if (digit > 9) {
					digit -= 9;
				}
			}
			sum += digit;
		}

		return (sum != 0) && (sum % 10 == 0);
	}

	/**
	 * 验证值对象字符长度大于等于指定长度
	 *
	 * @param value 值对象
	 * @param min   字符串长度最小值
	 * @return 验证通过则返回true
	 */
	public static boolean minLength(Object value, int min) {
		return required(value) && value instanceof CharSequence && ((CharSequence) value).length() >= min;
	}

	/**
	 * 验证值对象字符长度小于等于指定长度
	 *
	 * @param value 值对象
	 * @param max   字符串长度最大值
	 * @return 验证通过则返回true
	 */
	public static boolean maxLength(Object value, int max) {
		return required(value) && value instanceof CharSequence && ((CharSequence) value).length() <= max;
	}

	/**
	 * 验证值对象字符长度是否在指定长度范围内
	 *
	 * @param value 值对象
	 * @param min   字符串长度最小值
	 * @param max   字符串长度最大值
	 * @return 验证通过则返回true
	 */
	public static boolean rangeLength(Object value, int min, int max) {
		if (!(required(value) && value instanceof CharSequence)) {
			return false;
		}
		CharSequence s = (CharSequence) value;
		return s.length() >= min && s.length() <= max;
	}

	/**
	 * 验证值对象大于等于指定值
	 *
	 * @param value 值对象
	 * @param min   最小值
	 * @return 验证通过则返回true
	 */
	public static boolean min(Object value, Number min) {
		if (!required(value) || !required(min)) {
			return false;
		}
		String s = value.toString();
		return NumberUtils.isNumber(s) && new BigDecimal(s).compareTo(new BigDecimal(min.toString())) >= 0;
	}

	/**
	 * 验证值对象小于等于指定值
	 *
	 * @param value 值对象
	 * @param max   最大值
	 * @return 验证通过则返回true
	 */
	public static boolean max(Object value, Number max) {
		if (!required(value) || !required(max)) {
			return false;
		}
		String s = value.toString();
		return NumberUtils.isNumber(s) && new BigDecimal(s).compareTo(new BigDecimal(max.toString())) <= 0;

	}

	/**
	 * 验证值对象是否在指定数值范围内
	 *
	 * @param value 值对象
	 * @param min   最小值
	 * @param max   最大值
	 * @return 验证通过则返回true
	 */
	public static boolean range(Object value, Number min, Number max) {
		if (!required(value) || !required(min) || !required(max)) {
			return false;
		}
		String s = value.toString();
		return NumberUtils.isNumber(s) && new BigDecimal(s).compareTo(new BigDecimal(min.toString())) >= 0
				&& new BigDecimal(s).compareTo(new BigDecimal(max.toString())) <= 0;
	}

	/**
	 * 验证值对象是否为有效的日期格式
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean date(Object value) {
		return date(value, null);
	}

	/**
	 * 验证值对象是否为有效的日期格式
	 *
	 * @param value   值对象
	 * @param pattern 日期格式
	 * @return 验证通过则返回true
	 */
	public static boolean date(Object value, String pattern) {
		if (!required(value)) {
			return false;
		}
		if (value instanceof Date) {
			return true;
		}
		if (!(value instanceof CharSequence)) {
			return false;
		}
		String s = value.toString();
		return DateValidator.getInstance().isValid(s, pattern);
	}

	private static final Pattern ZIPCODE_PATTERN = Pattern.compile("^[1-9]\\d{5}$", Pattern.CASE_INSENSITIVE);

	/**
	 * 验证值对象是否为有效的邮政编码
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean zipcode(Object value) {
		return required(value) && value instanceof CharSequence && ZIPCODE_PATTERN.matcher(value.toString()).matches();
	}

	/**
	 * 验证值对象是否为有效的身份证号
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean idcard(Object value) {
		return required(value) && value instanceof CharSequence && SidHelper.isValid(value.toString());

	}

	/**
	 * 验证值对象是否为IPV4地址
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean ipv4(Object value) {
		return required(value) && InetAddressValidator.getInstance().isValidInet4Address(value.toString());
	}

	/**
	 * 验证值对象是否为IPV6地址
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean ipv6(Object value) {
		return required(value) && InetAddressValidator.getInstance().isValidInet6Address(value.toString());
	}

	/**
	 * 验证值对象是否符合指定的正则表达式
	 *
	 * @param value 值对象
	 * @param regex 正则表达式
	 * @return 验证通过则返回true
	 */
	public static boolean pattern(Object value, String regex) {
		return required(value) && value instanceof CharSequence && value.toString().matches(regex);
	}

	/**
	 * 验证值对象是否符合指定的后缀名
	 *
	 * @param value      值对象
	 * @param extensions 后缀名列表
	 * @return 验证通过则返回true
	 */
	public static boolean extension(Object value, List<String> extensions) {
		if (!required(value) || !(value instanceof CharSequence)) {
			return false;
		}
		for (String extension : extensions) {
			if (value.toString().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证值对象是否为全部中文
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean chinese(Object value) {
		return required(value) && value instanceof CharSequence && isChinese(value.toString());
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static final Pattern MOBILE_PATTERN = Pattern.compile("^0?(13\\d|15[0-35-9]|18[0236-9]|14[57])(\\d{8})$");

	/**
	 * 验证值对象是否国内手机号
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean mobile(Object value) {
		return required(value) && value instanceof CharSequence
				&& MOBILE_PATTERN.matcher(value.toString().replaceAll("-", "")).matches();
	}

	private static final Pattern TEL_PATTERN = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$");

	/**
	 * 验证值对象是否国内固话号码
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean tel(Object value) {
		return required(value) && value instanceof CharSequence
				&& TEL_PATTERN.matcher(value.toString().replaceAll("-", "")).matches();
	}

	private static final Pattern LETTERS_ONLY_PATTERN = Pattern.compile("^[a-z]+$", Pattern.CASE_INSENSITIVE);

	/**
	 * 验证值对象是否只有字母
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean lettersOnly(Object value) {
		return required(value) && value instanceof CharSequence
				&& LETTERS_ONLY_PATTERN.matcher(value.toString()).matches();
	}

	private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);

	/**
	 * 验证值对象是否只有字母和数字
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean alphaNumeric(Object value) {
		return required(value) && value instanceof CharSequence
				&& ALPHA_NUMERIC_PATTERN.matcher(value.toString()).matches();
	}

	/**
	 * 验证值对象是否符合指定的时间格式
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	private static final Pattern TIME_PATTERN = Pattern.compile("^([01]\\d|2[0-3]|[0-9])(:[0-5]\\d){1,2}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean time(Object value) {
		return required(value) && value instanceof CharSequence && TIME_PATTERN.matcher(value.toString()).matches();
	}

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^(?:-?\\d+|-?\\d{1,3}(?:,\\d{3})+)?(?:\\.\\d+)?$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * 验证值对象是否为数值
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean number(Object value) {
		return required(value) && (value instanceof Number
				|| (value instanceof CharSequence && NUMBER_PATTERN.matcher(value.toString()).matches()));
	}

	private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$", Pattern.CASE_INSENSITIVE);

	/**
	 * 验证值对象是否为数字
	 *
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean digits(Object value) {
		return required(value) && (value instanceof Number || value instanceof CharSequence)
				&& DIGITS_PATTERN.matcher(value.toString()).matches();
	}

	/**
	 * 验证值对象与指定对象是否相等
	 *
	 * @param value 值对象
	 * @param other 其他值对象
	 * @return 验证通过则返回true
	 */
	public static boolean equalTo(Object value, Object other) {
		return (value == null && other == null) || (value != null && value.equals(other));
	}

	private static final String JAVA_KEY_WORD = ",abstract,boolean,break,byte,case,catch,char,class,continue,default,do,double,else,extends,false,final,finally,float,for,if,implements,import,instanceof,int,interface,long,native,new,null,package,private,protected,public,return,short,static,super,switch,synchronized,this,throw,throws,transient,try,true,void,volatile,while,";

	/**
	 * 验证是否为有效的java package，不包含 "$" 字符
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean javaPackage(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence packageName = (CharSequence) value;
		if (StringUtils.isBlank(packageName)) {
			return false;
		}
		for (int i = 0; i < packageName.length(); i++) {
			char c = packageName.charAt(i);
			// test the char must be [a-zA-Z0-9.]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '.')) {
				return false;
			}
			// test the char starts with [a-zA-Z]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
				return false;
			}
			// test the char not end with [.]
			if (c == '.' && i == packageName.length() - 1) {
				return false;
			}
			// test package not start with number, event after dot.
			if (c == '.' && ('0' <= packageName.charAt(i + 1) && packageName.charAt(i + 1) <= '9')) {
				return false;
			}
		}
		{
			String[] split = packageName.toString().split("\\.");
			StringBuilder sb = new StringBuilder();
			for (String s : split) {
				sb.setLength(0);
				sb.append(',').append(s).append(',');
				if (JAVA_KEY_WORD.indexOf(sb.toString().toLowerCase()) > -1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 验证是否为有效的java field
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean javaField(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence fieldName = (CharSequence) value;
		if (StringUtils.isBlank(fieldName)) {
			return false;
		}
		for (int i = 0; i < fieldName.length(); i++) {
			char c = fieldName.charAt(i);
			// test the char must be [a-zA-Z0-9_$]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_' || c == '$')) {
				return false;
			}
			// test the char starts with [a-zA-Z_$]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_' || c == '$')) {
				return false;
			}
		}
		{
			StringBuilder sb = new StringBuilder().append(',').append(fieldName).append(',');
			if (JAVA_KEY_WORD.indexOf(sb.toString().toLowerCase()) > -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否为有效的java method
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean javaMethod(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence methodName = (CharSequence) value;
		if (StringUtils.isBlank(methodName)) {
			return false;
		}
		for (int i = 0; i < methodName.length(); i++) {
			char c = methodName.charAt(i);
			// test the char must be [a-zA-Z0-9_$]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_' || c == '$')) {
				return false;
			}
			// test the char starts with [a-zA-Z_$]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '_' || c == '$')) {
				return false;
			}
		}
		{
			StringBuilder sb = new StringBuilder().append(',').append(methodName).append(',');
			if (JAVA_KEY_WORD.indexOf(sb.toString().toLowerCase()) > -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否为有效的maven artifactId
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean artifactId(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence artifactid = (CharSequence) value;
		if (StringUtils.isBlank(artifactid)) {
			return false;
		}
		for (int i = 0; i < artifactid.length(); i++) {
			char c = artifactid.charAt(i);
			// test the char must be [a-zA-Z0-9-_]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '-' || c == '_')) {
				return false;
			}
			// test the char starts with [a-zA-Z]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
				return false;
			}
			// test the char not end with [-_]
			if ((c == '-' || c == '_') && i == artifactid.length() - 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否为有效的Table name
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean tableName(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence tableName = (CharSequence) value;
		if (StringUtils.isBlank(tableName)) {
			return false;
		}
		for (int i = 0; i < tableName.length(); i++) {
			char c = tableName.charAt(i);
			// test the char must be [a-zA-Z0-9_]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_')) {
				return false;
			}
			// test the char starts with [a-zA-Z]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
				return false;
			}
			// test the char not end with [_]
			if (c == '_' && i == tableName.length() - 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否为有效的Column name
	 * @param value 值对象
	 * @return 验证通过则返回true
	 */
	public static boolean columnName(Object value) {
		if (value == null || !(value instanceof CharSequence)) {
			return false;
		}
		CharSequence columnName = (CharSequence) value;
		if (StringUtils.isBlank(columnName)) {
			return false;
		}
		for (int i = 0; i < columnName.length(); i++) {
			char c = columnName.charAt(i);
			// test the char must be [a-zA-Z0-9_]
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_')) {
				return false;
			}
			// test the char starts with [a-zA-Z]
			if (i == 0 && !(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
				return false;
			}
			// test the char not end with [_]
			if (c == '_' && i == columnName.length() - 1) {
				return false;
			}
		}
		return true;
	}

}
