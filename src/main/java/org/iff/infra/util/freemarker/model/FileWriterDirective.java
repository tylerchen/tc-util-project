/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Logger;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StreamHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("filewriter")
public class FileWriterDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String type = getParameterName(params, "type");
		String basedir = getParameterName(params, "basedir");
		String name = getParameterName(params, "name");
		String overwrite = getParameterName(params, "overwrite");
		if (type == null || type.length() < 1) {
			throw new TemplateException("Missing type parameter for filewirter directive", env);
		}
		if (basedir == null || basedir.length() < 1) {
			throw new TemplateException("Missing basedir parameter for filewirter directive", env);
		}
		if (name == null || name.length() < 1) {
			throw new TemplateException("Missing name parameter for filewirter directive", env);
		}
		try {
			String fileExt = "";
			String fileName = "";
			String filePath = "";
			int lastIndex1 = name.lastIndexOf('/');
			int lastIndex2 = name.lastIndexOf('.');
			if ((lastIndex1 > -1 && lastIndex1 < lastIndex2) || (lastIndex1 < 0 && lastIndex2 > -1)) {// ex: aa/bb.c
				fileExt = name.substring(lastIndex2);
				name = name.substring(0, lastIndex2);
			}
			if ("java".equalsIgnoreCase(type)) {
				name = name.replaceAll("\\.", "/");
			}
			{
				lastIndex1 = name.lastIndexOf('/');
				if (lastIndex1 > -1) {
					fileName = name.substring(lastIndex1 + 1) + fileExt;
					filePath = StringHelper.pathConcat(basedir, name.substring(0, lastIndex1));
				} else {
					fileName = name + fileExt;
					filePath = StringHelper.pathConcat(basedir, "/");
				}
			}

			FileOutputStream fos = null;
			try {
				File parentDir = new File(filePath);
				parentDir.mkdirs();
				File target = new File(parentDir, fileName);
				StringWriter stringWriter = new StringWriter(10240);
				body.render(stringWriter);
				String genContent = StringUtils.defaultString(stringWriter.toString());
				if (target.exists() && !Boolean.TRUE.toString().equalsIgnoreCase(overwrite)) {
					String fileContent = StringUtils
							.defaultString(StreamHelper.getContent(new FileInputStream(target), false));
					fileContent = fileContent.trim();
					if (StringUtils.equals(fileContent.trim(), genContent.trim())) {
						Logger.debug("File not change: " + fileName);
						return;
					} else {
						String targetName = fileName + ".qdpgen";
						target = new File(parentDir, targetName);
						Logger.debug("File has change: " + fileName);
					}
				}
				fos = new FileOutputStream(target);
				fos.write(genContent.getBytes("UTF-8"));
			} finally {
				SocketHelper.closeWithoutError(fos);
			}
		} catch (Exception e) {
			throw new TemplateException(e, env);
		}
	}

	private String getParameterName(Map params, String name) {
		SimpleScalar simple = (SimpleScalar) params.get(name);
		if (simple != null) {
			return simple.getAsString();
		}
		return null;
	}
}
