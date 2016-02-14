/*******************************************************************************
 * Copyright (c) Feb 2, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package test;

import org.iff.infra.util.ZipHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 2, 2016
 */
public class ZipHelperTest {

	public static void main(String[] args) {
		ZipHelper.zip("/Users/zhaochen/Desktop/untitled folder/tmp", "/Users/zhaochen/Desktop/test.zip");
		ZipHelper.unzip("/Users/zhaochen/Desktop/test.zip", "/Users/zhaochen/Desktop/tmp111");
	}
}
