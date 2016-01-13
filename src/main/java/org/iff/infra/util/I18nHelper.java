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

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 14, 2015
 */
public class I18nHelper {
	private static final I18nHelper me = new I18nHelper();

	private Map<String, String> messages = new HashMap<String, String>(1024);

	private String namespace = "";
	private Locale locale = null;

	public static I18nHelper me() {
		return me;
	}

	public static I18nHelper get(String namespace) {
		I18nHelper helper = new I18nHelper();
		helper.messages = me.messages;
		helper.namespace = namespace == null ? "" : namespace;
		return helper;
	}

	public static I18nHelper get(String namespace, Locale locale) {
		I18nHelper helper = new I18nHelper();
		helper.messages = me.messages;
		helper.namespace = namespace == null ? "" : namespace;
		helper.locale = locale;
		return helper;
	}

	public static I18nHelper setMessages(Map<String, String> messages) {
		if (messages != null) {
			me.messages = messages;
		}
		return me;
	}

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
				File file = new File(new URL(resource).toURI());
				{
					if (!file.exists() || !file.isFile()) {
						continue;
					}
				}
				Properties prop = new Properties();
				{
					FileInputStream is = new FileInputStream(file);
					prop.load(new StringReader(SocketHelper.getContent(is, false)));
				}
				String keySubfix = "";
				{
					String fileName = file.getName();
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
								resource));
			}
		}
		return me;
	}

	public Map<String, String> getMessages() {
		return messages;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

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

	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, null);
	}

	public String getMessage(String code, String defaultMessage) {
		return getMessage(code, new Object[0], defaultMessage);
	}

	public String getMessage(String code) {
		return getMessage(code, new Object[0], null);
	}
}
