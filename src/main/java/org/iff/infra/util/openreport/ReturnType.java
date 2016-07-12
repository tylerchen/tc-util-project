/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface ReturnType {

	Object returnType(String value, String defaultValue);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("string", new ReturnTypeString());
				FACTORY.regist("like", new ReturnTypeLike());
				FACTORY.regist("date", new ReturnTypeDate());
				FACTORY.regist("split", new ReturnTypeSplit());
			}
			return FACTORY;
		}

		public ReturnType get(String name) {
			if (registMap != null) {
				return (ReturnType) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, ReturnType returnType) {
			RegisterHelper.regist(ReturnType.class.getSimpleName(), MapHelper.toMap("name", name, "value", returnType));
			registMap = RegisterHelper.get(ReturnType.class.getSimpleName());
			return this;
		}

		public List<Map> listRegist() {
			List<Map> list = new ArrayList<Map>();
			for (Entry<String, Object> entry : registMap.entrySet()) {
				list.add(MapHelper.toMap("name", entry.getKey(), "value", entry.getValue()));
			}
			return list;
		}

	}

	public static class ReturnTypeString implements ReturnType {
		public Object returnType(String value, String defaultValue) {
			return StringUtils.defaultIfBlank(value, defaultValue);
		}
	}

	public static class ReturnTypeLike implements ReturnType {
		public Object returnType(String value, String defaultValue) {
			return StringUtils.isBlank(value)
					? (StringUtils.isBlank(defaultValue) ? "" : StringHelper.concat("%", defaultValue, "%"))
					: StringHelper.concat("%", value, "%");
		}
	}

	public static class ReturnTypeDate implements ReturnType {
		public Object returnType(String value, String defaultValue) {
			try {
				return DateUtils.parseDate(StringUtils.defaultIfBlank(value, defaultValue), "yyyy-MM-dd HH:mm:ss",
						"yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd");
			} catch (Exception e) {
				return null;
			}
		}
	}

	public static class ReturnTypeSplit implements ReturnType {
		public Object returnType(String value, String defaultValue) {
			return StringUtils.split(StringUtils.defaultIfBlank(value, defaultValue), ',');
		}
	}

}
