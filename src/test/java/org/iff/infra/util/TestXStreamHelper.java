/*******************************************************************************
 * Copyright (c) Mar 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.PreCheckHelper;
import org.iff.infra.util.XStreamHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 14, 2016
 */
public class TestXStreamHelper {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	public static class Hello {
		private int integer = 10;
		private Date date = new Date();
		private String str = "hello";
		private Object[] array = new Object[] { "arr1", 1, 2L, new Date() };

		public int getInteger() {
			return integer;
		}

		public void setInteger(int integer) {
			this.integer = integer;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public Object[] getArray() {
			return array;
		}

		public void setArray(Object[] array) {
			this.array = array;
		}
	}

	public static Object getObject() {
		return new Hello();
	}

	@Test
	public void test_toXml() {
		String xml = XStreamHelper.toXml(getObject());
		Assert.assertNotNull(xml);
	}

	@Test
	public void test_fromXml() {
		String xml = XStreamHelper.toXml(getObject());
		Assert.assertNotNull(xml);
		Object o = XStreamHelper.fromXml(xml);
		Assert.assertNotNull(o);
		Assert.assertEquals(o.getClass(), Hello.class);
	}

	@Test
	public void test_fromXmlToMap() {
		String xml = "<Head><AA>1</AA><AA>2</AA><MessageSendDateTime>20150808102930</MessageSendDateTime><MessageSequence>125</MessageSequence><MessageType>OUT</MessageType><SourceSystemID>CA</SourceSystemID><DestinationSystemID>OMCCAAC</DestinationSystemID></Head>";
		Assert.assertNotNull(xml);
		Object o = XStreamHelper.fromConstomXmlToMap(xml);
		System.out.println(GsonHelper.toJsonString(o));
		Assert.assertNotNull(o);
		Assert.assertTrue(Map.class.isInstance(o));
	}

	@Test
	public void test_toCustomXml() {
		String str = "<Head><AA>1</AA><AA>2</AA><MessageSendDateTime>20150808102930</MessageSendDateTime><MessageSequence>125</MessageSequence><MessageType>OUT</MessageType><SourceSystemID>CA</SourceSystemID><DestinationSystemID>OMCCAAC</DestinationSystemID></Head>";
		Object map = XStreamHelper.fromConstomXmlToMap(str);
		String xml = XStreamHelper.toCustomXml(map, "Head");
		Assert.assertNotNull(xml);
		String[] split = StringUtils.split(xml, "\n");
		String[] trims = PreCheckHelper.trimAndRemoveBlank(split);
		String join = StringUtils.join(trims);
		Assert.assertEquals(str, join);
		System.out.println(join);
	}

}
