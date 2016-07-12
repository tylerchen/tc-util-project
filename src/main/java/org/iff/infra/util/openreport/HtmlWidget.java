/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.FCS;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface HtmlWidget {

	String htmlWidget(String name, String value, String html, String data);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("html", new HtmlWidgetHtml());
				FACTORY.regist("blank", new HtmlWidgetBlank());
				FACTORY.regist("hidden", new HtmlWidgetHidden());
				FACTORY.regist("script", new HtmlWidgetScript());
				FACTORY.regist("text", new HtmlWidgetText());
				FACTORY.regist("select", new HtmlWidgetSelect());
				FACTORY.regist("mselect", new HtmlWidgetMSelect());
				FACTORY.regist("cn2select", new HtmlWidgetCN2Select());
			}
			return FACTORY;
		}

		public HtmlWidget get(String name) {
			if (registMap != null) {
				return (HtmlWidget) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, HtmlWidget returnType) {
			RegisterHelper.regist(HtmlWidget.class.getSimpleName(), MapHelper.toMap("name", name, "value", returnType));
			registMap = RegisterHelper.get(HtmlWidget.class.getSimpleName());
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

	public static class HtmlWidgetHtml implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return html;
		}
	}

	public static class HtmlWidgetBlank implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return "&nbsp;";
		}
	}

	public static class HtmlWidgetHidden implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return FCS
					.get("<input type=\"hidden\" name=\"{name}\" id=\"{name}\" value=\"{value}\" />", name, name, value)
					.toString();
		}
	}

	public static class HtmlWidgetScript implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return FCS.get("<script type=\"text/javascript\">{value}</script>", value).toString();
		}
	}

	public static class HtmlWidgetText implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return FCS.get("<input type=\"text\" name=\"${name}\" id=\"${name}\" value=\"${value}\" />", name, name,
					value).toString();
		}
	}

	public static class HtmlWidgetSelect implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return FCS.get("<select name=\"${name}\" id=\"${name}\" class=\"or-hw-select\">{html}</select>", html)
					.toString();
		}
	}

	public static class HtmlWidgetMSelect implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {
			return FCS
					.get("<select name=\"${name}\" id=\"${name}\" class=\"or-hw-mselect\" multiple=\"multiple\">{html}</select>",
							html)
					.toString();
		}
	}

	public static class HtmlWidgetCN2Select implements HtmlWidget {
		public String htmlWidget(String name, String value, String html, String data) {

			StringBuilder options = new StringBuilder(256);
			String[] proviceCitys = StringUtils.split(data, ';');
			for (String proviceCity : proviceCitys) {
				String[] proviceAndCity = StringUtils.split(proviceCity, ',');
				if (proviceAndCity != null && proviceAndCity.length > 1) {
					for (String item : proviceAndCity) {
						if (item.indexOf(':') > 0) {
							String[] idAndName = StringUtils.split(item, ':');
							options.append(
									FCS.get("<option value=\"${id}\" data=\"{data}\">{name}</option>\r\n", idAndName[0],
											StringUtils.join(
													Arrays.copyOfRange(proviceAndCity, 1, proviceAndCity.length), ','),
											idAndName[1]));
						} else {
							options.append(FCS.get("<option value=\"${id}\" data=\"{data}\">{name}</option>\r\n",
									proviceAndCity[0],
									StringUtils.join(Arrays.copyOfRange(proviceAndCity, 1, proviceAndCity.length), ','),
									proviceAndCity[0]));
						}
					}
				}
			}
			return FCS
					.get("<select name=\"{name}\" id=\"{name}\" class=\"or-hw-select province\"><option></option>{options}</select><select name=\"{name}_2\" id=\"{name}_2\" class=\"or-hw-select city\"><option></option></select>",
							name, name, options.toString(), name, name)
					.toString();
		}
	}

}
