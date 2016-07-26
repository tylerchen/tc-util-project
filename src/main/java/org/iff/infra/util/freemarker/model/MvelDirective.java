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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("mvel")
public class MvelDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String var = getParameterName(params, "var");
		String type = getParameterName(params, "type");
		Object eval = null;
		{
			StringWriter stringWriter = new StringWriter(256);
			body.render(stringWriter);
			String script = stringWriter.toString();
			if (script.length() < 1) {
				return;
			}
			Map root = new HashMap();
			{
				for (Entry entry : (Set<Entry>) params.entrySet()) {
					Object model = entry.getValue();
					String key = entry.getKey().toString();
					if (model instanceof TemplateModel) {
						root.put(key, DeepUnwrap.unwrap((TemplateModel) model));
					}
				}
			}
			try {
				if ("template".equalsIgnoreCase(type)) {
					eval = TemplateRuntime.eval(script, root);
				} else {
					eval = MVEL.eval(script, root);
				}
				if (var != null && var.length() > 0) {
					env.setVariable(var, env.getConfiguration().getObjectWrapper().wrap(eval));
				} else if ("template".equalsIgnoreCase(type)) {
					if (eval != null) {
						env.getOut().write(eval.toString());
						body.render(env.getOut());
					}
				}
			} catch (Exception e) {
				Logger.debug(FCS.get("====MVEL Script====\r\n{0}\r\n===MVEL binding===\r\n{1}", script, root), e);
				Exceptions.runtime(FCS.get("====MVEL Script====\r\n{0}\r\n===MVEL binding===\r\n{1}", script, root), e);
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
