/*******************************************************************************
 * Copyright (c) Jul 18, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 18, 2016
 */
public class GuessValidationQuery {
	public static String guessByUrl(String url) {
		/*jdbc:mysql://localhost:3306/new_qdp?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull*/
		if (StringUtils.isBlank(url)) {
			return "select 1";
		}
		if (url.startsWith("jdbc:mysql:")) {
			return "select 1";
		}
		if (url.startsWith("jdbc:oracle:")) {
			return "select 1 from dual";
		}
		if (url.startsWith("jdbc:sqlserver:")) {
			return "select 1";
		}
		if (url.startsWith("jdbc:h2:")) {
			return "select 1";
		}
		if (url.startsWith("jdbc:db2:")) {
			return "select 1 from sysibm.sysdummy1";
		}
		if (url.startsWith("jdbc:postgresql:")) {
			return "select 1";
		}
		return "select 1";
	}
}
