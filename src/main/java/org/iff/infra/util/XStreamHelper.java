/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

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

	/**
	 * convert xml to map.
	 * @param xml
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static Map fromConstomXmlToMap(String xml) {
		XStream custom = new XStream();
		custom.registerConverter(new PojoMapConverter());
		int start = xml.indexOf('<');
		if (start > -1) {
			if (xml.charAt(start + 1) == '?') {
				start = xml.indexOf('<', start + 1);
			}
		}
		if (start > -1) {
			int end = xml.indexOf('>', start + 1);
			String name = StringUtils.substring(xml, start + 1, Math.max(start + 1, end)).trim();
			if (name.indexOf(' ') > -1) {
				name = StringUtils.split(name, ' ')[0];
			}
			custom.alias(name, LinkedHashMap.class);
		}
		return (Map) custom.fromXML(xml, new LinkedHashMap());
	}

	public static class PojoMapConverter implements Converter {

		public PojoMapConverter() {
			super();
		}

		public boolean canConvert(Class clazz) {
			if (Map.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
				return true;
			} else {
				return false;
			}
		}

		public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
			map2xml(value, writer, context);
		}

		protected void map2xml(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
			Map<String, Object> map;
			List<Object> list;
			Object subvalue;
			String key;
			if (value instanceof Map) {
				map = (Map<String, Object>) value;
				for (Iterator<Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
					key = (String) entry.getKey();
					subvalue = entry.getValue();
					if (subvalue instanceof CharSequence) {
						writer.startNode(key);
						writer.setValue(String.valueOf(subvalue));
						writer.endNode();
					} else {
						if (subvalue instanceof List) {
							list = (List<Object>) subvalue;
							for (Object o : list) {
								writer.startNode(key);
								map2xml(o, writer, context);
								writer.endNode();
							}
						} else {
							map2xml(subvalue, writer, context);
						}
					}
				}

			} else if (value instanceof List) {
				list = (List<Object>) value;
				for (Object subval : list) {
					subvalue = subval;
					writer.startNode("Item");
					if (subvalue instanceof String) {
						writer.setValue(String.valueOf(subvalue));
					} else {
						map2xml(subvalue, writer, context);
					}
					writer.endNode();
				}
			} else {
				writer.setValue(String.valueOf(value));
			}
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			return populateMap(reader, context);
		}

		protected Object populateMap(HierarchicalStreamReader reader, UnmarshallingContext context) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			while (reader.hasMoreChildren()) {
				reader.moveDown();
				String key = reader.getNodeName();
				Object value = null;
				if (reader.hasMoreChildren()) {
					value = populateMap(reader, context);
				} else {
					value = reader.getValue();
				}
				if (map.containsKey(key)) {/*convert to list*/
					Object object = map.get(key);
					if (object instanceof List) {
						List<Object> list = (List<Object>) object;
						list.add(value);
					} else {
						List<Object> list = new ArrayList<Object>();
						list.add(object);
						list.add(value);
						map.put(key, list);
					}
				} else {
					/*insert into map*/
					map.put(key, value);
				}
				reader.moveUp();
			}
			if (map.size() == 1) {/*if only list then return list*/
				return map.values().iterator().next();
			} else {
				return map;
			}
		}

	}

	public static void main(String[] args) {
		String xml = "<Head><AA>1</AA><AA>2</AA><MessageSendDateTime>20150808102930</MessageSendDateTime><MessageSequence>125</MessageSequence><MessageType>OUT</MessageType><SourceSystemID>CA</SourceSystemID><DestinationSystemID>OMCCAAC</DestinationSystemID></Head>";
		Map<String, Object> map = (Map<String, Object>) fromConstomXmlToMap(xml);
		System.out.println(map);
		xml = xstream.toXML(map);
		System.out.println(xml);
		System.out.println((Map<String, Object>) xstream.fromXML(xml));
		xml = xstream.toXML(Arrays.asList(1, 2, 3, 4));
		System.out.println(xml);
	}
}
