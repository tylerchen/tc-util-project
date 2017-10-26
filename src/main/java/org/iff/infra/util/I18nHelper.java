/*******************************************************************************
 * Copyright (c) Dec 14, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * i18n helper.
 * <pre>
 * Usage: 
 * I18nHelper.loadDefualtMessages("classpath://META-INF/i18n");
 * 
 * properties file content using UTF-8 encoding:
 * Common.systemName=xxx
 * Common.systemName.en=xx
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 14, 2015
 */
public class I18nHelper {
	private static final I18nHelper me = new I18nHelper();

	private Map<String, String> messages = new HashMap<String, String>(1024);

	private String namespace = "";
	private Locale locale = null;

	/**
	 * return I18nHelper with default namespace.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper me() {
		return me;
	}

	/**
	 * get I18nHelper instance by namespace.
	 * @param namespace
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper get(String namespace) {
		I18nHelper helper = new I18nHelper();
		helper.messages = me.messages;
		helper.namespace = namespace == null ? "" : namespace;
		return helper;
	}

	/**
	 * get I18nHelper instance by namespace and locale.
	 * @param namespace
	 * @param locale
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper get(String namespace, Locale locale) {
		I18nHelper helper = new I18nHelper();
		helper.messages = me.messages;
		helper.namespace = namespace == null ? "" : namespace;
		helper.locale = locale;
		return helper;
	}

	/**
	 * set messages.
	 * @param messages
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper setMessages(Map<String, String> messages) {
		if (messages != null) {
			me.messages = messages;
		}
		return me;
	}

	/**
	 * add messages and over write exists message.
	 * @param messages
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper addMessages(Map<String, String> messages) {
		if (messages != null) {
			me.messages.putAll(messages);
		}
		return me;
	}

	/**
	 * load default messages.
	 * @param locationSplitByComma
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static I18nHelper loadDefualtMessages(String locationSplitByComma) {
		if (locationSplitByComma == null || locationSplitByComma.length() < 1) {
			locationSplitByComma = "classpath://META-INF/i18n";
		}
		String[] locations = locationSplitByComma.split(",");
		List<String> resList = new ArrayList<String>(64);
		for (String location : locations) {
			List<String> resources = ResourceHelper.loadResources(location, ".properties", "*", null);
			resList.addAll(resources);
		}
		for (String resource : resList) {
			try {
				String content = "";
				String fileName = StringUtils.substringAfterLast(resource, "/");
				// resource maybe jar or war or ear file.
				URL url = ResourceHelper.url(resource);
				if (!StringUtils.contains(resource, "!/")) {
					File file = new File(url.toURI());
					{
						if (!file.exists() || !file.isFile()) {
							continue;
						}
					}
					FileInputStream is = new FileInputStream(file);
					content = SocketHelper.getContent(is, false);
				} else {
					content = SocketHelper.getContent(url.openStream(), false);
				}
				Properties prop = new Properties();
				{
					prop.load(new StringReader(content));
					Logger.info("Loaded properties file: " + resource);
				}
				String keySubfix = "";
				{
					if (fileName.lastIndexOf('-') > -1) {
						keySubfix = fileName.substring(fileName.lastIndexOf('-') + 1, fileName.lastIndexOf('.'));
					}
				}
				String keyPrefix = "";
				{
					keyPrefix = prop.getProperty("namespace", "");
				}
				for (Entry<Object, Object> entry : prop.entrySet()) {
					String key = new StringBuilder(64).append(keyPrefix).append(keyPrefix.length() < 1 ? "" : ".")
							.append(entry.getKey().toString()).append(keySubfix.length() < 1 ? "" : ".")
							.append(keySubfix).toString();
					me.messages.put(key, entry.getValue().toString());
				}
			} catch (Exception e) {
				Logger.warn(
						FCS.get("[org.iff.infra.util.I18nHelper.loadDefualtMessages][{file}]: loading property file error! ",
								resource),
						e);
			}
		}
		return me;
	}

	/**
	 * return all messages.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public Map<String, String> getMessages() {
		return messages;
	}

	/**
	 * get namespace.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * set namespace.
	 * @param namespace
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * get locale
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * set locale.
	 * @param locale
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * return the keys by code.
	 * @param code
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public List<String> getKeys(String code) {
		List<String> list = new ArrayList<String>(8);
		if (code == null || code.length() < 1) {
			return list;
		}
		StringBuilder key = new StringBuilder(64);
		if (namespace.length() > 0) {
			key.append(namespace).append('.');
		}
		key.append(code);
		list.add(key.toString());
		if (locale != null) {
			key.append('.');
			if (locale.getLanguage() != null && locale.getLanguage().length() > 0) {
				key.append(locale.getLanguage());
				list.add(key.toString());
			}
			if (locale.getCountry() != null && locale.getCountry().length() > 0) {
				key.append('_').append(locale.getCountry());
				list.add(key.toString());
			}
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * get message by code, and format message by arguments, if can't find the message use default message.
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		List<String> keys = getKeys(code);
		String message = defaultMessage;
		for (String key : keys) {
			message = messages.get(key);
			if (message != null) {
				break;
			}
		}
		message = message == null ? (defaultMessage == null ? code : defaultMessage) : message;
		return StringHelper.replaceBlock(message, args, "");
	}

	/**
	 * get message by code, and format message by arguments, if can't find the message use default message.
	 * @param code
	 * @param argsMap
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getMessage(String code, Map<String, Object> argsMap, String defaultMessage) {
		List<String> keys = getKeys(code);
		String message = defaultMessage;
		for (String key : keys) {
			message = messages.get(key);
			if (message != null) {
				break;
			}
		}
		message = message == null ? (defaultMessage == null ? code : defaultMessage) : message;
		return StringHelper.replaceBlock(message, argsMap, "");
	}

	/**
	 * get message by code, and format the message by arguments.
	 * @param code
	 * @param args
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, null);
	}

	/**
	 * get message by code, if can't find the message by code the return default message.
	 * @param code
	 * @param defaultMessage
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getMessage(String code, String defaultMessage) {
		return getMessage(code, new Object[0], defaultMessage);
	}

	/**
	 * get message by code.
	 * @param code
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getMessage(String code) {
		return getMessage(code, new Object[0], null);
	}

}
