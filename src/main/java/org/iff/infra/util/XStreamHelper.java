/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * a helper for xstream.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XStreamHelper {

	private static XStream xstream = new XStream();

	private XStreamHelper() {
	}

	/**
	 * return xstream instance.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static XStream getXstream() {
		return xstream;
	}

	/**
	 * convert object to xml content.
	 * @param obj
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String toXml(Object obj) {
		return xstream.toXML(obj);
	}

	/**
	 * convert xml to object.
	 * @param xml
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T fromXml(String xml) {
		return (T) xstream.fromXML(xml);
	}

	/**
	 * convert xml to an exists object root.
	 * @param xml
	 * @param root
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T fromXml(String xml, Object root) {
		return (T) xstream.fromXML(xml, root);
	}

	/**
	 * convert xml to map.
	 * @param xml
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Map fromXmlToMap(String xml) {
		return (Map) xstream.fromXML(xml, new LinkedHashMap());
	}
}
