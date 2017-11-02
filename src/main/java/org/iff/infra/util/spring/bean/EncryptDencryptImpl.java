/*******************************************************************************
 * Copyright (c) Jan 15, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.spring.bean;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;
import org.iff.infra.util.FCS;
import org.iff.infra.util.MD5Helper;
import org.iff.infra.util.RSAHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 15, 2016
 */
public class EncryptDencryptImpl implements EncryptDencrypt {

	public String encrypt(String method, String saltOrKey, String value) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		if ("MD5".equalsIgnoreCase(method)) {
			return MD5Helper.string2MD5(value);
		} else if ("MD5-2".equalsIgnoreCase(method)) {
			if (StringUtils.isNotEmpty(saltOrKey)) {
				return MD5Helper.string2MD5(saltOrKey + "-" + MD5Helper.string2MD5(value));
			}
			return MD5Helper.string2MD5(MD5Helper.string2MD5(value));
		} else if ("RSA".equalsIgnoreCase(method)) {
			if (StringUtils.isNotEmpty(saltOrKey)) {
				return RSAHelper.encrypt(value, RSAHelper.getPublicKeyFromBase64(saltOrKey));
			} else {
				return RSAHelper.encryptByDefaultKey(value);
			}
		}
		Assert.error(FCS.get("No encrypt method {0} found!", method));
		return value;
	}

	public String dencrypt(String method, String saltOrKey, String value) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		if ("MD5".equalsIgnoreCase(method)) {
			Assert.error(FCS.get("Encrypt method {0} is not supported for dencrypt!", method));
		} else if ("MD5-2".equalsIgnoreCase(method)) {
			Assert.error(FCS.get("Encrypt method {0} is not supported for dencrypt!", method));
		} else if ("RSA".equalsIgnoreCase(method)) {
			if (StringUtils.isNotEmpty(saltOrKey)) {
				return RSAHelper.decrypt(value, RSAHelper.getPrivateKeyFromBase64(saltOrKey));
			} else {
				return RSAHelper.decryptByDefaultKey(value);
			}
		}
		Assert.error(FCS.get("No dencrypt method {0} found!", method));
		return value;
	}

}
