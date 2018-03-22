/*******************************************************************************
 * Copyright (c) Mar 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.XmlHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 14, 2016
 */
public class TestXmlHelper {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test_parseXmlTextToMap() {
		{
			String str = "<A prop='world'>hello</A>";
			Map map = XmlHelper.parseXmlTextToMap(null, str);
			Assert.assertNotNull(map);
			Assert.assertNotNull(map.get("A"));
			System.out.println(GsonHelper.toJsonString(map));
		}
		{
			String str = "<A name='world'>hello</A>";
			Map map = XmlHelper.parseXmlTextToMap(null, str);
			Assert.assertNotNull(map);
			Assert.assertNotNull(map.get("A@world"));
			System.out.println(GsonHelper.toJsonString(map));
		}
	}

	@Test
	public void test_parseXmlToMap() {
		try {
			File temp = File.createTempFile("test_parseXmlToMap", ".xml");
			FileWriter fileoutput = new FileWriter(temp);
			BufferedWriter buffout = new BufferedWriter(fileoutput);
			String str = "<A prop='world'>hello</A>";
			buffout.write(str);
			buffout.close();
			fileoutput.close();
			Map map = XmlHelper.parseXmlToMap(null, temp.getAbsolutePath());
			Assert.assertNotNull(map);
			Assert.assertNotNull(map.get("A"));
			System.out.println(GsonHelper.toJsonString(map));
			temp.delete();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}
