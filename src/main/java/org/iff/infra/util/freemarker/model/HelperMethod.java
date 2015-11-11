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

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("helper")
public class HelperMethod implements TemplateMethodModelEx {
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null) {
			if (arguments.size() > 0) {
				TemplateModel templateModel = null;
				String className = arguments.get(0).toString();
				if (className.indexOf('.') < 0) {
					try {
						templateModel = ((TemplateHashModel) FreeMarkerConfiguration.helper)
								.get("org.iff.infra.util." + className);
					} catch (Exception e) {
					}
				} else if (templateModel == null) {
					try {
						templateModel = ((TemplateHashModel) FreeMarkerConfiguration.helper).get(className);
					} catch (Exception e) {
					}
					if (templateModel == null) {
						try {
							templateModel = ((TemplateHashModel) FreeMarkerConfiguration.helper)
									.get("org.iff.infra.util." + className);
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
