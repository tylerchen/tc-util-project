/*******************************************************************************
 * Copyright (c) Jan 15, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring.bean;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 15, 2016
 */
public interface EncryptDencrypt {

	String encrypt(String method, String salt, String value);

	String dencrypt(String method, String salt, String value);
}
