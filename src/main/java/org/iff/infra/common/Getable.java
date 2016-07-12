/*******************************************************************************
 * Copyright (c) Nov 5, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.common;

/**
 * A interface for getter.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 5, 2015
 */
public interface Getable<T> {

	/**
	 * return value by key.
	 * @param key
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	T get(Object key);

	/**
	 * return value by key and default value.
	 * @param key
	 * @param defaultValue
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	T get(Object key, T defaultValue);
}
