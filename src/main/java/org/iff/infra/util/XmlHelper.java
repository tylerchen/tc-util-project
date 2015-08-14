/*******************************************************************************
 * Copyright (c) Aug 11, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import groovy.util.Node;
import groovy.util.XmlParser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 11, 2015
 */
public class XmlHelper {

	public static Map parseXmlToMap(Map xml, String file) {
		try {
			xml = xml == null ? new LinkedHashMap() : xml;
			Node node = new XmlParser().parse(file.startsWith("file:") ? new File(new URL(file).toURI()) : new File(
					file));
			xml.put(node.name(), xml.get(node.name()) == null ? new LinkedHashMap() : xml.get(node.name()));
			List<Node> list = new ArrayList<Node>(128);
			List<Map> level = new ArrayList<Map>(128);
			list.add(node);
			level.add((Map) xml.get(node.name()));
			while (list.size() > 0) {
				Node nd = list.remove(list.size() - 1);
				Map last = level.remove(level.size() - 1);
				String name = (String) nd.name();
				List children = nd.children();
				Map attributes = nd.attributes();
				// process current node attributes.
				last.putAll(attributes);
				if (children.size() == 1 && attributes.size() == 0 && children.get(0) instanceof String) {
					// process node like: <html><![CDATA[]]></html>, to parent attribute html.
					last.put(name, children.get(0));
				}
				for (Object child : nd.children()) {
					if (child instanceof String) {
					} else {
						Node nn = (Node) child;
						String childKey = nn.name()
								+ (nn.attributes().get("name") != null ? ("@" + nn.attributes().get("name")) : "");
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

	public static void main(String[] args) {
		System.out
				.println(parseXmlToMap(null,
						"file:///media/新加卷/workspace/JeeGalileo/tc-util-project2/src/test/resources/webapp/open-report/open-report-reports.xml"));
	}
}
