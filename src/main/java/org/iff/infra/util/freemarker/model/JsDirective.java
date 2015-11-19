/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("jstm")
public class JsDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String required = getParameterName(params, "required");
		{
			String jsCode = "";
			if (body != null) {
				StringWriter stringWriter = new StringWriter(512);
				body.render(stringWriter);
				jsCode = stringWriter.toString().trim();
			}
			if (jsCode.length() > 0) {
				TemplateModel model = env.getGlobalVariable("tmjs");
				if (model == null || !(model instanceof SimpleSequence)) {
					model = new SimpleSequence(new ArrayList(), env.getConfiguration().getObjectWrapper());
				}
				{
					((SimpleSequence) model).add(jsCode);
				}
				env.setGlobalVariable("tmjs", model);
			}
		}
		{
			TemplateModel model = env.getGlobalVariable("tmscript");
			if (model == null || !(model instanceof SimpleSequence)) {
				model = new SimpleSequence(new ArrayList(), env.getConfiguration().getObjectWrapper());
			}
			if (required != null && required.length() > 0) {
				String[] split = required.split(",");
				for (String s : split) {
					s = s.trim();
					int size = ((SimpleSequence) model).size();
					boolean contains = false;
					for (int i = 0; i < size; i++) {
						TemplateModel tm = ((SimpleSequence) model).get(i);
						if (s.equals(tm.toString())) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						((SimpleSequence) model).add(s);
					}
				}
			}
			env.setGlobalVariable("tmscript", model);
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
