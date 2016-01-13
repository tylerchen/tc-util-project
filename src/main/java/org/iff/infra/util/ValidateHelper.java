/*******************************************************************************
 * Copyright (c) 2014-10-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.iff.infra.util.mybatis.service.Dao;
import org.iff.infra.util.validation.ValidationMethods;

/**
 * A validate helper provides a set of utility methods to validate the data.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-10-10
 */
public class ValidateHelper {
	//	/** Email regex **/
	//	private static final String regex_email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	//	/** Email pattern **/
	//	private static final Pattern pattern_email = Pattern.compile(regex_email);
	//
	//	/**
	//	 * Verify whether the input is Email
	//	 * @param email
	//	 * @return
	//	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	//	 * @since 2015-2-6
	//	 */
	//	public static boolean email(CharSequence email) {
	//		return pattern_email.matcher(email).matches();
	//	}
	private List<String> errors = new ArrayList<String>();

	public static ValidateHelper create() {
		return new ValidateHelper();
	}

	public boolean hasNoErrors() {
		return errors.isEmpty();
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public String joinErrors(String seperator) {
		if (!errors.isEmpty()) {
			return org.apache.commons.lang3.StringUtils.join(errors, seperator);
		}
		return "";
	}

	public List<String> getErrors() {
		return errors;
	}

	public ValidateHelper addError(String errorCode, String defaultMessage, Object[] args) {
		Object namespaceObj = ThreadLocalHelper.get("namespace");
		Object localeObj = ThreadLocalHelper.get("locale");
		Locale locale = null;
		String namespace = "";
		if (localeObj != null && localeObj instanceof Locale) {
			locale = (Locale) localeObj;
		}
		if (namespaceObj != null && namespace instanceof String) {
			namespace = (String) namespaceObj;
		}
		I18nHelper i18n = I18nHelper.me();
		if (locale != null) {
			i18n = I18nHelper.get(namespace, locale);
		} else if (namespace != null && namespace.length() > 0) {
			i18n = I18nHelper.get(namespace);
		}
		Map<String, Object> argsMap = new HashMap<String, Object>();
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (arg == null) {// if null set to empty
					argsMap.put(String.valueOf(i), "");
				} else if (i == 0 && arg instanceof String) {// if first is string, need to get the name
					argsMap.put(String.valueOf(i), i18n.getMessage((String) arg));
				} else {// put to map
					argsMap.put(String.valueOf(i), arg);
				}
			}
		}
		errors.add(i18n.getMessage(errorCode, argsMap, defaultMessage));
		return this;
	}

	public ValidateHelper inArray(String field, Object value, String errorCode, Object[] array) {
		inArray(field, value, errorCode, null, array);
		return this;
	}

	public ValidateHelper inArray(String field, Object value, String errorCode, String defaultMessage, Object[] array) {
		if (!(ValidationMethods.inArray(value, array))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, array });
		}
		return this;
	}

	public ValidateHelper isTrue(String field, Object value, String errorCode) {
		isTrue(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper isTrue(String field, Object value, String errorCode, String defaultMessage) {
		if (value == null || !(value instanceof Boolean) || !(Boolean) value) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	public ValidateHelper required(String field, Object value, String errorCode) {
		required(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper required(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.required(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	public ValidateHelper email(String field, Object value, String errorCode) {
		email(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper email(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.email(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	public ValidateHelper creditCard(String field, Object value, String errorCode) {
		creditCard(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper creditCard(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.creditCard(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	public ValidateHelper url(String field, Object value, String errorCode) {
		url(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper url(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.url(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	public ValidateHelper minLength(String field, Object value, String errorCode, int minLength) {
		minLength(field, value, errorCode, null, minLength);
		return this;
	}

	public ValidateHelper minLength(String field, Object value, String errorCode, String defaultMessage,
			int minLength) {
		if (!(ValidationMethods.minLength(value, minLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, minLength });
		}
		return this;
	}

	public ValidateHelper maxLength(String field, Object value, String errorCode, int maxLength) {
		maxLength(field, value, errorCode, null, maxLength);
		return this;
	}

	public ValidateHelper maxLength(String field, Object value, String errorCode, String defaultMessage,
			int maxLength) {
		if (!(ValidationMethods.maxLength(value, maxLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, maxLength });
		}
		return this;
	}

	public ValidateHelper rangeLength(String field, Object value, String errorCode, int minLength, int maxLength) {
		rangeLength(field, value, errorCode, null, minLength, maxLength);
		return this;
	}

	public ValidateHelper rangeLength(String field, Object value, String errorCode, String defaultMessage,
			int minLength, int maxLength) {
		if (!(ValidationMethods.rangeLength(value, minLength, maxLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, minLength, maxLength });
		}
		return this;
	}

	public ValidateHelper min(String field, Object value, String errorCode, Number min) {
		min(field, value, errorCode, null, min);
		return this;
	}

	public ValidateHelper min(String field, Object value, String errorCode, String defaultMessage, Number min) {
		if (!(ValidationMethods.min(value, min))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, min });
		}
		return this;
	}

	public ValidateHelper max(String field, Object value, String errorCode, Number max) {
		max(field, value, errorCode, null, max);
		return this;
	}

	public ValidateHelper max(String field, Object value, String errorCode, String defaultMessage, Number max) {
		if (!(ValidationMethods.max(value, max))) {
			addError(errorCode, defaultMessage, new Object[] { field, max });
		}
		return this;
	}

	public ValidateHelper range(String field, Object value, String errorCode, Number min, Number max) {
		range(field, value, errorCode, null, min, max);
		return this;
	}

	public ValidateHelper range(String field, Object value, String errorCode, String defaultMessage, Number min,
			Number max) {
		if (!(ValidationMethods.range(value, min, max))) {
			addError(errorCode, defaultMessage, new Object[] { field, min, max });
		}
		return this;
	}

	public ValidateHelper date(String field, Object value, String errorCode, String pattern) {
		date(field, value, errorCode, null, pattern);
		return this;
	}

	public ValidateHelper date(String field, Object value, String errorCode, String defaultMessage, String pattern) {
		if (!(ValidationMethods.date(value, pattern))) {
			addError(errorCode, defaultMessage, new Object[] { field, pattern });
		}
		return this;
	}

	public ValidateHelper zipcode(String field, Object value, String errorCode) {
		zipcode(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper zipcode(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.zipcode(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper idcard(String field, Object value, String errorCode) {
		idcard(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper idcard(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.idcard(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper pattern(String field, Object value, String errorCode, String regex) {
		pattern(field, value, errorCode, null, regex);
		return this;
	}

	public ValidateHelper pattern(String field, Object value, String errorCode, String defaultMessage, String regex) {
		if (!(ValidationMethods.pattern(value, regex))) {
			addError(errorCode, defaultMessage, new Object[] { field, regex });
		}
		return this;
	}

	public ValidateHelper extension(String field, Object value, String errorCode, List<String> extensions) {
		extension(field, value, errorCode, null, extensions);
		return this;
	}

	public ValidateHelper extension(String field, Object value, String errorCode, String defaultMessage,
			List<String> extensions) {
		if (!(ValidationMethods.extension(value, extensions))) {
			addError(errorCode, defaultMessage, new Object[] { field, extensions });
		}
		return this;
	}

	public ValidateHelper chinese(String field, Object value, String errorCode) {
		chinese(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper chinese(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.chinese(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper mobile(String field, Object value, String errorCode) {
		mobile(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper mobile(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.mobile(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper tel(String field, Object value, String errorCode) {
		mobile(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper tel(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.tel(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper lettersOnly(String field, Object value, String errorCode) {
		lettersOnly(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper lettersOnly(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.lettersOnly(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper alphaNumeric(String field, Object value, String errorCode) {
		alphaNumeric(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper alphaNumeric(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.alphaNumeric(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper time(String field, Object value, String errorCode) {
		time(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper time(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.time(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper equalTo(String field, Object value, String errorCode, String otherField, Object otherValue) {
		equalTo(field, value, errorCode, null, otherField, otherValue);
		return this;
	}

	public ValidateHelper equalTo(String field, Object value, String errorCode, String defaultMessage,
			String otherField, Object otherValue) {
		if (!(ValidationMethods.equalTo(value, otherValue))) {
			addError(errorCode, defaultMessage, new Object[] { field, otherField, otherValue });
		}
		return this;
	}

	public ValidateHelper digits(String field, Object value, String errorCode) {
		digits(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper digits(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.digits(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper number(String field, Object value, String errorCode) {
		number(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper number(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.number(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper javaPackage(String field, Object value, String errorCode) {
		javaPackage(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper javaPackage(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaPackage(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper javaField(String field, Object value, String errorCode) {
		javaField(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper javaField(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaField(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper javaMethod(String field, Object value, String errorCode) {
		javaMethod(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper javaMethod(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaMethod(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper artifactId(String field, Object value, String errorCode) {
		artifactId(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper artifactId(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.artifactId(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper tableName(String field, Object value, String errorCode) {
		tableName(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper tableName(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.tableName(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	public ValidateHelper columnName(String field, Object value, String errorCode) {
		columnName(field, value, errorCode, null);
		return this;
	}

	public ValidateHelper columnName(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.columnName(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}
}
