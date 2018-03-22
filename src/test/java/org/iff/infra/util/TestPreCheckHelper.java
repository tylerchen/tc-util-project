/*******************************************************************************
 * Copyright (c) Mar 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 14, 2016
 */
public class TestPreCheckHelper {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_nullToEmpty() {
		{
			Object empty = PreCheckHelper.nullToEmpty((String) null);
			Assert.assertTrue(empty instanceof String);
			Assert.assertTrue(((String) empty).length() == 0);
		}
		{
			Object empty = PreCheckHelper.nullToEmpty((List<?>) null);
			Assert.assertTrue(empty instanceof Collection);
			Assert.assertTrue(((Collection<?>) empty).size() == 0);
		}
		{
			Object empty = PreCheckHelper.nullToEmpty((Map<?, ?>) null);
			Assert.assertTrue(empty instanceof Map);
			Assert.assertTrue(((Map<?, ?>) empty).size() == 0);
		}
	}

}
