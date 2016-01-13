/*******************************************************************************
 * Copyright (c) Jan 2, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 2, 2016
 */
public class MapWrapHelper {

	public static <T> T wrapTo(Map map, Class<T> clazz) {
		Assert.notNull(map);
		Assert.notNull(clazz);
		if (Modifier.isFinal(clazz.getModifiers())) {
			Assert.error(FCS.get("The class {0} CAN NOT be final!", clazz.getName()));
		}
		return null;
	}
}
