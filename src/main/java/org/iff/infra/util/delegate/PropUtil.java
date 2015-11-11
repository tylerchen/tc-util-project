/*******************************************************************************
 * Copyright (c) Oct 12, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.delegate;

import java.util.Map;
import java.util.Map.Entry;

import org.iff.infra.common.Delegate;
import org.iff.infra.util.RegisterHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Oct 12, 2015
 */
public class PropUtil implements Delegate {

	private static PropUtil me = new PropUtil();

	public boolean support(Object o) {
		return true;
	}

	public static PropUtil me() {
		return me;
	}

	public String get(String key) {
		Map<String, Object> map = RegisterHelper.get(PropUtil.class.getName());
		for (Entry<String, Object> entry : map.entrySet()) {

		}
		return null;
	}
}
