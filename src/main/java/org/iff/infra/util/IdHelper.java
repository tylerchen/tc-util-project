/*******************************************************************************
 * Copyright (c) Aug 22, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 22, 2017
 */
public class IdHelper {

	/**
	 * 获得扩展的SnowFlakeId，局域网内，有效IP最后一段需要不同。
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 22, 2017
	 */
	public static long longId() {
		return SnowflakeIdExHelper.id();
	}

	/**
	 * 获得19位的uuid，由UUID进行62位编码而成。
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 22, 2017
	 */
	public static String uuid() {
		return StringHelper.uuid();
	}

}
