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

import org.iff.infra.util.validation.ValidationMethods;

/**
 * A validate helper provides a set of utility methods to validate the data.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-10-10
 */
public class ValidateHelper {

	private List<String> errors = new ArrayList<String>();

	/**
	 * create a validate helper.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static ValidateHelper create() {
		return new ValidateHelper();
	}

	/**
	 * test if has no errors.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public boolean hasNoErrors() {
		return errors.isEmpty();
	}

	/**
	 * test if has errors.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * join errors as a string.
	 * @param seperator
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String joinErrors(String seperator) {
		if (!errors.isEmpty()) {
			return org.apache.commons.lang3.StringUtils.join(errors, seperator);
		}
		return "";
	}

	/**
	 * return errors.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * add error.
	 * @param errorCode
	 * @param defaultMessage
	 * @param args
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
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

	/**
	 * if the value not in array then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param array
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper inArray(String field, Object value, String errorCode, Object[] array) {
		inArray(field, value, errorCode, null, array);
		return this;
	}

	/**
	 * if the value not in array then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param array
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper inArray(String field, Object value, String errorCode, String defaultMessage, Object[] array) {
		if (!(ValidationMethods.inArray(value, array))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, array });
		}
		return this;
	}

	/**
	 * if the value is not true then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper isTrue(String field, Object value, String errorCode) {
		isTrue(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not true then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper isTrue(String field, Object value, String errorCode, String defaultMessage) {
		if (value == null || !(value instanceof Boolean) || !(Boolean) value) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	/**
	 * if the value is null then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper required(String field, Object value, String errorCode) {
		required(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is null then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper required(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.required(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	/**
	 * if the value is not the validate email then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper email(String field, Object value, String errorCode) {
		email(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate email then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper email(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.email(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	/**
	 * if the value is not the validate credit card then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper creditCard(String field, Object value, String errorCode) {
		creditCard(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate credit card then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper creditCard(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.creditCard(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	/**
	 * if the value is not the validate url then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper url(String field, Object value, String errorCode) {
		url(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate url then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper url(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.url(value))) {
			addError(errorCode, defaultMessage, new Object[] { field, value });
		}
		return this;
	}

	/**
	 * if the value length is low than min length then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param minLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper minLength(String field, Object value, String errorCode, int minLength) {
		minLength(field, value, errorCode, null, minLength);
		return this;
	}

	/**
	 * if the value length is low than min length then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param minLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper minLength(String field, Object value, String errorCode, String defaultMessage,
			int minLength) {
		if (!(ValidationMethods.minLength(value, minLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, minLength });
		}
		return this;
	}

	/**
	 * if the value length is greater than max length then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param maxLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper maxLength(String field, Object value, String errorCode, int maxLength) {
		maxLength(field, value, errorCode, null, maxLength);
		return this;
	}

	/**
	 * if the value length is greater than max length then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param maxLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper maxLength(String field, Object value, String errorCode, String defaultMessage,
			int maxLength) {
		if (!(ValidationMethods.maxLength(value, maxLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, maxLength });
		}
		return this;
	}

	/**
	 * if the value length is not in range then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param minLength
	 * @param maxLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper rangeLength(String field, Object value, String errorCode, int minLength, int maxLength) {
		rangeLength(field, value, errorCode, null, minLength, maxLength);
		return this;
	}

	/**
	 * if the value length is not in range then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param minLength
	 * @param maxLength
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper rangeLength(String field, Object value, String errorCode, String defaultMessage,
			int minLength, int maxLength) {
		if (!(ValidationMethods.rangeLength(value, minLength, maxLength))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, minLength, maxLength });
		}
		return this;
	}

	/**
	 * if the value lower than the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param min
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper min(String field, Object value, String errorCode, Number min) {
		min(field, value, errorCode, null, min);
		return this;
	}

	/**
	 * if the value lower than the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param min
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper min(String field, Object value, String errorCode, String defaultMessage, Number min) {
		if (!(ValidationMethods.min(value, min))) {
			addError(errorCode, defaultMessage, new Object[] { field, value, min });
		}
		return this;
	}

	/**
	 * if the value greater than the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param max
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper max(String field, Object value, String errorCode, Number max) {
		max(field, value, errorCode, null, max);
		return this;
	}

	/**
	 * if the value greater than the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param max
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper max(String field, Object value, String errorCode, String defaultMessage, Number max) {
		if (!(ValidationMethods.max(value, max))) {
			addError(errorCode, defaultMessage, new Object[] { field, max });
		}
		return this;
	}

	/**
	 * if the value not in the range the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param min
	 * @param max
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper range(String field, Object value, String errorCode, Number min, Number max) {
		range(field, value, errorCode, null, min, max);
		return this;
	}

	/**
	 * if the value not in the range the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param min
	 * @param max
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper range(String field, Object value, String errorCode, String defaultMessage, Number min,
			Number max) {
		if (!(ValidationMethods.range(value, min, max))) {
			addError(errorCode, defaultMessage, new Object[] { field, min, max });
		}
		return this;
	}

	/**
	 * if the value is not the validate date then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param pattern
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper date(String field, Object value, String errorCode, String pattern) {
		date(field, value, errorCode, null, pattern);
		return this;
	}

	/**
	 * if the value is not the validate date then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param pattern
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper date(String field, Object value, String errorCode, String defaultMessage, String pattern) {
		if (!(ValidationMethods.date(value, pattern))) {
			addError(errorCode, defaultMessage, new Object[] { field, pattern });
		}
		return this;
	}

	/**
	 * if the value is not the validate zip code then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper zipcode(String field, Object value, String errorCode) {
		zipcode(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate zip code then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper zipcode(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.zipcode(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate id card then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper idcard(String field, Object value, String errorCode) {
		idcard(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate id card then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper idcard(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.idcard(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not match the pattern then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param regex
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper pattern(String field, Object value, String errorCode, String regex) {
		pattern(field, value, errorCode, null, regex);
		return this;
	}

	/**
	 * if the value is not match the pattern then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param regex
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper pattern(String field, Object value, String errorCode, String defaultMessage, String regex) {
		if (!(ValidationMethods.pattern(value, regex))) {
			addError(errorCode, defaultMessage, new Object[] { field, regex });
		}
		return this;
	}

	/**
	 * if the value is not in the extensions list then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param extensions
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper extension(String field, Object value, String errorCode, List<String> extensions) {
		extension(field, value, errorCode, null, extensions);
		return this;
	}

	/**
	 * if the value is not in the extensions list then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param extensions
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper extension(String field, Object value, String errorCode, String defaultMessage,
			List<String> extensions) {
		if (!(ValidationMethods.extension(value, extensions))) {
			addError(errorCode, defaultMessage, new Object[] { field, extensions });
		}
		return this;
	}

	/**
	 * if the value is not the chinese then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper chinese(String field, Object value, String errorCode) {
		chinese(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the chinese then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper chinese(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.chinese(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate mobile number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper mobile(String field, Object value, String errorCode) {
		mobile(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate mobile number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper mobile(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.mobile(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate tel number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper tel(String field, Object value, String errorCode) {
		mobile(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate tel number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper tel(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.tel(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the letters then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper lettersOnly(String field, Object value, String errorCode) {
		lettersOnly(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the letters then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper lettersOnly(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.lettersOnly(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the alpha number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper alphaNumeric(String field, Object value, String errorCode) {
		alphaNumeric(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the alpha number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper alphaNumeric(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.alphaNumeric(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate time then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper time(String field, Object value, String errorCode) {
		time(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate time then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper time(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.time(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not equal to the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param otherField
	 * @param otherValue
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper equalTo(String field, Object value, String errorCode, String otherField, Object otherValue) {
		equalTo(field, value, errorCode, null, otherField, otherValue);
		return this;
	}

	/**
	 * if the value is not equal to the value then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @param otherField
	 * @param otherValue
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper equalTo(String field, Object value, String errorCode, String defaultMessage,
			String otherField, Object otherValue) {
		if (!(ValidationMethods.equalTo(value, otherValue))) {
			addError(errorCode, defaultMessage, new Object[] { field, otherField, otherValue });
		}
		return this;
	}

	/**
	 * if the value is not the validate digits then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper digits(String field, Object value, String errorCode) {
		digits(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate digits then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper digits(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.digits(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper number(String field, Object value, String errorCode) {
		number(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate number then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper number(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.number(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate java package then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaPackage(String field, Object value, String errorCode) {
		javaPackage(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate java package then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaPackage(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaPackage(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate java field then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaField(String field, Object value, String errorCode) {
		javaField(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate java field then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaField(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaField(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate java method then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaMethod(String field, Object value, String errorCode) {
		javaMethod(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate java method then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper javaMethod(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.javaMethod(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate maven artifactId then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper artifactId(String field, Object value, String errorCode) {
		artifactId(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate maven artifactId then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper artifactId(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.artifactId(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate db table name then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper tableName(String field, Object value, String errorCode) {
		tableName(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate db table name then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper tableName(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.tableName(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * if the value is not the validate db column name then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper columnName(String field, Object value, String errorCode) {
		columnName(field, value, errorCode, null);
		return this;
	}

	/**
	 * if the value is not the validate db column name then add error.
	 * @param field
	 * @param value
	 * @param errorCode
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ValidateHelper columnName(String field, Object value, String errorCode, String defaultMessage) {
		if (!(ValidationMethods.columnName(value))) {
			addError(errorCode, defaultMessage, new Object[] { field });
		}
		return this;
	}

	/**
	 * join the errors as string.
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String toString() {
		return FCS.get("ValidateHelper [{0}]", this.joinErrors("\n")).toString();
	}
}
