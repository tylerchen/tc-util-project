/*******************************************************************************
 * Copyright (c) Dec 4, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.freemarker.FreeMarkerConfiguration;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Dec 4, 2015
 */
public class QdpCodeGenerator {

	private Map<String, String> resourceMap = new LinkedHashMap<String, String>();

	public static void main(String[] args) {
		QdpCodeGenerator cg = create();

		cg.loadResource(
				"file:///Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/main/resources/qdp-designer/template/1.0.0");
	}

	public static QdpCodeGenerator create() {
		return new QdpCodeGenerator();
	}

	public String process(Object params) {
		Configuration config = getConfig();
		StringWriter sw = new StringWriter();
		for (Entry<String, String> entry : resourceMap.entrySet()) {
			String fileName = entry.getKey();
			if (fileName.charAt(fileName.lastIndexOf('/') + 1) == '_') {
				continue;
			}
			try {
				{
					sw.write("\n====================\n");
					sw.write(fileName);
					sw.write("\n====================\n");
				}
				StringWriter tmp = new StringWriter();
				Template template = config.getTemplate(fileName, "UTF-8");
				template.process(MapHelper.toMap("root", params), tmp);
				sw.write(tmp.toString().trim());
			} catch (Exception e) {
				sw.write('\n');
				e.printStackTrace(new PrintWriter(sw));
			}
		}
		return sw.toString().trim();
	}

	public Configuration getConfig() {
		FreeMarkerConfiguration config = new FreeMarkerConfiguration();
		config.setDirectivePath("org.iff.infra.util.freemarker.model");
		config.setTemplateLoader(new TCFreeMarkerTemplateLoader());
		//SimpleObjectWrapper objectWrapper = new SimpleObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(
				Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		builder.setUseAdaptersForContainers(true);
		config.setObjectWrapper(builder.build());
		return config;
	}

	public void loadResource(String path) {
		// path=classpath://qdp-designer/template
		String fixPath = StringHelper.fixUrl(path);// fixPath=classpath:///qdp-designer/template
		Logger.debug("Path:" + path);
		List<String> resources = ResourceHelper.loadResources(fixPath, ".ftl", "*", null);
		String prefixPath = StringUtils.substringAfter(path, "://");// prefixPath=/qdp-designer/template
		Collections.sort(resources);
		for (String res : resources) {
			String tmp = StringHelper.fixUrl(res);
			String relativePath = StringUtils.substringAfter(res, prefixPath);
			{
				relativePath = relativePath.startsWith("/") ? relativePath : ("/" + relativePath);
			}
			Logger.debug("Resource:" + tmp);
			Logger.debug("RelativePath:" + relativePath);
			try {
				String content = SocketHelper.getContent(ResourceHelper.openUrlStream(tmp), false);
				resourceMap.put(relativePath, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, String> getResourceMap() {
		return resourceMap;
	}

	public void setResourceMap(Map<String, String> resourceMap) {
		this.resourceMap = resourceMap;
	}

	public class TCFreeMarkerTemplateLoader implements freemarker.cache.TemplateLoader {

		public TCFreeMarkerTemplateLoader() {
		}

		public Object findTemplateSource(String name) throws IOException {
			return resourceMap.get(name.startsWith("/") ? name : ("/" + name));
		}

		public long getLastModified(Object templateSource) {
			return 0;
		}

		public Reader getReader(Object templateSource, String encoding) throws IOException {
			return new StringReader((String) templateSource);
		}

		public void closeTemplateSource(Object templateSource) throws IOException {
			//do nothing
		}
	}
}
