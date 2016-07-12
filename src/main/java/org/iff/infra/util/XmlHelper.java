/*******************************************************************************
 * Copyright (c) Aug 11, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import groovy.util.Node;
import groovy.util.XmlParser;
import groovy.xml.QName;

/**
 * <pre>
 * parse xml file to map structure, only LinkedHashMap without list.
 * usage:
 * parseXmlToMap(new LinkedHashMap(), "file:///path/to/xml.xml")
 * OR
 * parseXmlToMap(new LinkedHashMap(), "/path/to/xml.xml")
 * example:
 * 1. xml file
 *   <root><nodes><node name="a"><html>htmlcontent</html></node><node name="b" type="c"></node></nodes></root>
 * 2. convert to map:
 *   {root : {nodes : {node@a: {name:a, html: htmlcontent, node@b: {name: b, type: c}}}}}
 * SO:
 * a) the same element MUST contains a "name" property, and "name" property value can not contain "@" character
 * b) if a element contains a no property element, such as "html", will treat as a property of parent element. 
 * c) if you pass the same map to the parameter map, and invoke many times, the map value will be overwrite.
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 11, 2015
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XmlHelper {

	/**
	 * parse the xml structure to map.
	 * @param xml you can pass the exists map or null value.
	 * @param file file path, can start with "file://" or just the file system path.
	 * @return xml file map structure.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 18, 2015
	 */
	public static Map parseXmlToMap(Map xml, String file) {
		try {
			xml = xml == null ? new LinkedHashMap() : xml;
			Node node = null;
			if (file.startsWith("jar:")) {
				node = new XmlParser().parse(file);
			} else if (file.startsWith("file:")) {
				node = new XmlParser().parse(file);
			} else {
				node = new XmlParser().parse(new File(file));
			}
			xml.put(getNodeName(node), xml.get(node.name()) == null ? new LinkedHashMap() : xml.get(getNodeName(node)));
			List<Node> list = new ArrayList<Node>(128);
			List<Map> level = new ArrayList<Map>(128);
			list.add(node);
			level.add((Map) xml.get(getNodeName(node)));
			while (list.size() > 0) {
				Node nd = list.remove(list.size() - 1);
				Map last = level.remove(level.size() - 1);
				String name = getNodeName(nd);
				List children = nd.children();
				Map attributes = nd.attributes();
				Map attrMap = new HashMap();
				for (Entry entry : (java.util.Set<Entry>) attributes.entrySet()) {
					attrMap.put(getAttribueName(entry.getKey()), entry.getValue());
				}
				// process current node attributes.
				last.putAll(attrMap);
				if (nd.localText() != null && nd.localText().size() > 0) {
					StringBuilder sb = new StringBuilder(256);
					for (String s : nd.localText()) {
						sb.append(s.trim());
					}
					last.put("nodeContent", sb.toString());
				}
				if (children.size() == 1 && attributes.size() == 0 && children.get(0) instanceof String) {
					// process node like: <html><![CDATA[]]></html>, to parent attribute html.
					last.put(name, children.get(0));
				}
				for (Object child : nd.children()) {
					if (child instanceof String) {
					} else {
						Node nn = (Node) child;
						String nname = getNodeName(nn);
						String childKey = nname + (nn.attributes().get("name") != null
								? ("@" + getAttribueName(nn.attributes().get("name"))) : "");
						if (!last.containsKey(childKey)) {
							last.put(childKey, new LinkedHashMap());
						}
						level.add((Map) last.get(childKey));
						list.add((Node) child);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}

	/**
	 * get the node name and remove namespace.
	 * @param node
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
	private static String getNodeName(Node node) {
		if (node.name() instanceof QName) {
			return ((QName) node.name()).getLocalPart();
		} else {
			return node.name().toString();
		}
	}

	/**
	 * get the node attribute name and remove namespace.
	 * @param attributeName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jun 7, 2016
	 */
	private static String getAttribueName(Object attributeName) {
		if (attributeName instanceof QName) {
			return ((QName) attributeName).getLocalPart();
		} else {
			return attributeName.toString();
		}
	}

	public static void main(String[] args) {
		Map m = parseXmlToMap(null,
				"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ctrip/openreport-test.xml");
		System.out.println(m);
		System.out.println("------");
		System.out.println(MapHelper.getByPath(m, "openreport/reports/report@test/query/nodeContent"));
	}
}
