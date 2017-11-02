/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("hasPermit")
public class HasPermitMethod implements TemplateMethodModelEx {
	public Object exec(List arguments) throws TemplateModelException {
		Subject subject = SecurityUtils.getSubject();
		if (subject.hasRole("ADMIN")) {
			return true;
		}
		if (arguments != null) {
			if (arguments.size() > 0) {
				for (Object o : arguments) {
					String permit = o.toString();
					if (subject.isPermitted(permit)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
