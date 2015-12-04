package org.iff.infra.util.validation;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 验证方法
 */
public class ValidationMethods {

  /**
   * 验证值对象是否存在
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean required(Object value) {
    return value != null && value.toString().length() > 0;
  }

  /**
   * 验证值对象是否为有效的Email地址
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean email(Object value) {
    return optional(value) ||
        value instanceof CharSequence && EmailValidator.getInstance().isValid(value.toString());
  }

  /**
   * 验证值对象是否为有效的URL
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean url(Object value) {
    return optional(value) ||
        value instanceof CharSequence && UrlValidator.getInstance().isValid(value.toString());
  }

  /**
   * 验证值对象是否为有效的银行卡
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean creditCard(Object value) {
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
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
      } catch (NumberFormatException e) {
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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
      return false;
    }

    CharSequence s = (CharSequence) value;
    return s.length() >= min;
  }

  /**
   * 验证值对象字符长度小于等于指定长度
   *
   * @param value 值对象
   * @param max   字符串长度最大值
   * @return 验证通过则返回true
   */
  public static boolean maxLength(Object value, int max) {
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
      return false;
    }

    CharSequence s = (CharSequence) value;
    return s.length() <= max;
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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence) && !(value instanceof Number)) {
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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence) && !(value instanceof Number)) {
      return false;
    }

    String s = value.toString();
    return NumberUtils.isNumber(s)
        && new BigDecimal(s).compareTo(new BigDecimal(max.toString())) <= 0;

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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence) && !(value instanceof Number)) {
      return false;
    }

    String s = value.toString();
    return NumberUtils.isNumber(s)
        && new BigDecimal(s).compareTo(new BigDecimal(min.toString())) >= 0
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
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
      return false;
    }

    String s = value.toString();
    return DateValidator.getInstance().isValid(s, pattern);
  }

  private static final Pattern ZIPCODE_PATTERN =
      Pattern.compile("^[1-9]\\d{5}$", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否为有效的邮政编码
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean zipcode(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && ZIPCODE_PATTERN.matcher(value.toString()).matches());
  }

  private static boolean optional(Object value) {
    return !required(value);
  }

  /**
   * 验证值对象是否为有效的身份证号
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean idcard(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && SidHelper.isValid(value.toString()));

  }

  /**
   * 验证值对象是否为IPV4地址
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean ipv4(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && InetAddressValidator.getInstance().isValidInet4Address(value.toString()));
  }

  /**
   * 验证值对象是否为IPV6地址
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean ipv6(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && InetAddressValidator.getInstance().isValidInet6Address(value.toString()));
  }

  /**
   * 验证值对象是否符合指定的正则表达式
   *
   * @param value 值对象
   * @param regex 正则表达式
   * @return 验证通过则返回true
   */
  public static boolean pattern(Object value, String regex) {
    return optional(value) ||
        (value instanceof CharSequence
            && value.toString().matches(regex));
  }

  /**
   * 验证值对象是否符合指定的后缀名
   *
   * @param value      值对象
   * @param extensions 后缀名列表
   * @return 验证通过则返回true
   */
  public static boolean extension(Object value, List<String> extensions) {
    if (optional(value)) {
      return true;
    }

    if (!(value instanceof CharSequence)) {
      return false;
    }

    for (String extension : extensions) {
      if (value.toString().endsWith(extension)) {
        return true;
      }
    }

    return false;
  }

  private static final Pattern CHINESE_PATTERN =
      Pattern.compile("\\p{IsHan}+", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否为全部中文
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean chinese(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && CHINESE_PATTERN.matcher(value.toString()).matches());
  }

  private static final Pattern MOBILE_PATTERN = Pattern.compile("^0?(13\\d|15[0-35-9]|18[0236-9]|14[57])(\\d{8})$");

  /**
   * 验证值对象是否国内手机号
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean mobile(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && MOBILE_PATTERN.matcher(value.toString().replaceAll("-", "")).matches());
  }

  private static final Pattern TEL_PATTERN = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$");

  /**
   * 验证值对象是否国内固话号码
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean tel(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && TEL_PATTERN.matcher(value.toString().replaceAll("-", "")).matches());
  }

  private static final Pattern LETTERS_ONLY_PATTERN =
      Pattern.compile("^[a-z]+$", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否只有字母
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean lettersOnly(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && LETTERS_ONLY_PATTERN.matcher(value.toString()).matches());
  }

  private static final Pattern ALPHA_NUMERIC_PATTERN =
      Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否只有字母和数字
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean alphaNumeric(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && ALPHA_NUMERIC_PATTERN.matcher(value.toString()).matches());
  }

  /**
   * 验证值对象是否符合指定的时间格式
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  private static final Pattern TIME_PATTERN =
      Pattern.compile("^([01]\\d|2[0-3]|[0-9])(:[0-5]\\d){1,2}$", Pattern.CASE_INSENSITIVE);

  public static boolean time(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && TIME_PATTERN.matcher(value.toString()).matches());
  }

  private static final Pattern NUMBER_PATTERN =
      Pattern.compile("^(?:-?\\d+|-?\\d{1,3}(?:,\\d{3})+)?(?:\\.\\d+)?$", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否为数值
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean number(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && NUMBER_PATTERN.matcher(value.toString()).matches());
  }

  private static final Pattern DIGITS_PATTERN =
      Pattern.compile("^\\d+$", Pattern.CASE_INSENSITIVE);

  /**
   * 验证值对象是否为数字
   *
   * @param value 值对象
   * @return 验证通过则返回true
   */
  public static boolean digits(Object value) {
    return optional(value) ||
        (value instanceof CharSequence
            && DIGITS_PATTERN.matcher(value.toString()).matches());
  }

  /**
   * 验证值对象与指定对象是否相等
   *
   * @param value 值对象
   * @param other 其他值对象
   * @return 验证通过则返回true
   */
  public static boolean equalTo(Object value, Object other) {
    return optional(value) ||
        (value instanceof CharSequence && value.toString().equals(other));
  }

}
