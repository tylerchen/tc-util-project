/*******************************************************************************
 * Copyright (c) 2014-10-10 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.regex.Pattern;

/**
 * A validate helper provides a set of utility methods to validate the data.
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-10-10
 */
public class ValidateHelper {
	/** Email regex **/
	private static final String regex_email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/** Email pattern **/
	private static final Pattern pattern_email = Pattern.compile(regex_email);

	/**
	 * Verify whether the input is Email
	 * @param email
	 * @return
	 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static boolean email(CharSequence email) {
		return pattern_email.matcher(email).matches();
	}
}
