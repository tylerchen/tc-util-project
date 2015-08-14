/*******************************************************************************
 * Copyright (c) 2014-7-22 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-7-22
 */
public class HtmlHelper {

	private static Whitelist whitelist;

	private static Whitelist getWhitelist() {
		if (whitelist == null) {
			Whitelist user_content_filter = Whitelist.relaxed();
			user_content_filter.addTags("embed", "object", "param", "span",
					"div");
			user_content_filter.addAttributes(":all", "style", "class", "id",
					"name");
			user_content_filter.addAttributes("object", "width", "height",
					"classid", "codebase");
			user_content_filter.addAttributes("param", "name", "value");
			user_content_filter.addAttributes("embed", "src", "quality",
					"width", "height", "allowFullScreen", "allowScriptAccess",
					"flashvars", "name", "type", "pluginspage");
			whitelist = user_content_filter;
		}
		return whitelist;
	}

	/**
	 * 过滤HTML防止XSS攻击
	 * @param html
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-7-22
	 */
	public static String clean(String html) {
		if (html == null || html.trim().length() < 1) {
			return "";
		}
		return Jsoup.clean(html, getWhitelist());
	}

	public static String htmlToText(String html) {
		if (html == null || html.trim().length() < 1) {
			return "";
		}
		return Jsoup.parse(html).text();
	}
}
