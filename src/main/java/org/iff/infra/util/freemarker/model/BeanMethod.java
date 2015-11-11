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

import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("bean")
public class BeanMethod implements TemplateMethodModelEx {
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null) {
			if (arguments.size() > 0) {
				String beanName = arguments.get(0).toString();
				return InstanceFactory.getInstance(beanName);
			}
		}
		Logger.warn(FCS.get("bean name not found, for arguments: {0}", Arrays.toString(arguments.toArray())));
		return null;
	}
}
