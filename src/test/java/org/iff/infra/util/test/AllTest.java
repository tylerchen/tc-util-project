/*******************************************************************************
 * Copyright (c) Mar 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 14, 2016
 */
public class AllTest {

	public static Test suite() {

		TestSuite suite = new TestSuite("Running all tests.");

		suite.addTestSuite(TestHttpHelper.class);

		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTest.suite());
	}
}
