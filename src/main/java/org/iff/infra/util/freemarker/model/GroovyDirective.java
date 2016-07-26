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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("groovy")
public class GroovyDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String var = getParameterName(params, "var");
		Object eval = null;
		{
			StringWriter stringWriter = new StringWriter(256);
			body.render(stringWriter);
			String script = stringWriter.toString();
			if (script.length() < 1) {
				return;
			}
			Binding binding = new Binding();
			{
				for (Entry entry : (Set<Entry>) params.entrySet()) {
					Object model = entry.getValue();
					String key = entry.getKey().toString();
					if (model instanceof TemplateModel) {
						binding.setVariable(key, DeepUnwrap.unwrap((TemplateModel) model));
					}
				}
			}
			try {
				GroovyShell shell = new GroovyShell(binding);
				eval = shell.evaluate(script);
				if (var != null && var.length() > 0) {
					env.setVariable(var, env.getConfiguration().getObjectWrapper().wrap(eval));
				}
			} catch (Exception e) {
				Logger.debug(FCS.get("====Groovy Script====\r\n{0}\r\n===Groovy binding===\r\n{1}", script,
						binding.getVariables()), e);
				Exceptions.runtime(FCS.get("====Groovy Script====\r\n{0}\r\n===Groovy binding===\r\n{1}", script,
						binding.getVariables()), e);
			}
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
