/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.util.Arrays;
import java.util.List;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.freemarker.FreeMarkerConfiguration;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("fillObject")
public class FillObjectMethod implements TemplateMethodModelEx {
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null) {
			if (arguments.size() > 0) {
				TemplateModel templateModel = null;
				Object instance = null;
				String className = arguments.get(0).toString();
				{
					try {
						Class<?> forName = Class.forName(className);
						instance = forName.newInstance();
						//Environment.getCurrentEnvironment().getVariable("").
					} catch (Exception e) {
						throw new TemplateModelException(e.getMessage());
					}
					if (instance != null) {
						try {
							templateModel = FreeMarkerConfiguration.beansWrapper.wrap(instance);
						} catch (Exception e) {
						}
					}
				}
				return templateModel;
			}
		}
		Logger.warn(FCS.get("class not found, for arguments: {0}", Arrays.toString(arguments.toArray())));
		return null;
	}
}
