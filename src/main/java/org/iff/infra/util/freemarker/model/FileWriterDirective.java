/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.iff.infra.util.SocketHelper;
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
			if ("java".equalsIgnoreCase(type)) {
				if (name.toLowerCase().endsWith(".java")) {
					name = name.substring(0, name.length() - ".java".length()).replaceAll("\\.", "/") + ".java";
				}
			}
			name = StringHelper.pathBuild(name, "/");
			FileOutputStream fos = null;
			try {
				File parentDir = new File(StringHelper.pathConcat(basedir,
						name.lastIndexOf('/') > -1 ? name.substring(0, name.lastIndexOf('/')) : name));
				parentDir.mkdirs();
				fos = new FileOutputStream(new File(parentDir,
						name.lastIndexOf('/') > -1 ? name.substring(name.lastIndexOf('/') + 1) : name));
				StringWriter stringWriter = new StringWriter(10240);
				body.render(stringWriter);
				fos.write(stringWriter.toString().getBytes());
			} finally {
				SocketHelper.closeWithoutError(fos);
			}
		} catch (Exception e) {
			throw new TemplateException(e, env);
		}
		//		{
		//			String cssCode = "";
		//			if (body != null) {
		//				StringWriter stringWriter = new StringWriter(512);
		//				body.render(stringWriter);
		//				cssCode = stringWriter.toString().trim();
		//			}
		//			if (cssCode.length() > 0) {
		//				TemplateModel model = env.getGlobalVariable("tmcss");
		//				if (model == null || !(model instanceof SimpleSequence)) {
		//					model = new SimpleSequence(new ArrayList(), env.getConfiguration().getObjectWrapper());
		//				}
		//				{
		//					((SimpleSequence) model).add(cssCode);
		//				}
		//				env.setGlobalVariable("tmcss", model);
		//			}
		//		}
		//		{
		//			TemplateModel model = env.getGlobalVariable("tmstyle");
		//			if (model == null || !(model instanceof SimpleSequence)) {
		//				model = new SimpleSequence(new ArrayList(), env.getConfiguration().getObjectWrapper());
		//			}
		//			if (required != null && required.length() > 0) {
		//				String[] split = required.split(",");
		//				for (String s : split) {
		//					s = s.trim();
		//					int size = ((SimpleSequence) model).size();
		//					boolean contains = false;
		//					for (int i = 0; i < size; i++) {
		//						TemplateModel tm = ((SimpleSequence) model).get(i);
		//						if (s.equals(tm.toString())) {
		//							contains = true;
		//							break;
		//						}
		//					}
		//					if (!contains) {
		//						((SimpleSequence) model).add(s);
		//					}
		//				}
		//			}
		//			env.setGlobalVariable("tmstyle", model);
		//		}
	}

	private String getParameterName(Map params, String name) {
		SimpleScalar simple = (SimpleScalar) params.get(name);
		if (simple != null) {
			return simple.getAsString();
		}
		return null;
	}
}
