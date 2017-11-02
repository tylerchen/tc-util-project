/*******************************************************************************
 * Copyright (c) Mar 15, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker;

import java.util.ArrayList;
import java.util.List;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.springframework.beans.factory.InitializingBean;

import freemarker.template.TemplateModel;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 15, 2016
 */
public class ExFreeMarkerConfiguration extends FreeMarkerConfiguration implements InitializingBean {
	private List<String> models = new ArrayList<String>();

	public List<String> getModels() {
		return models;
	}

	public void setModels(List<String> models) {
		this.models = models;
	}

	public void addVariable() {
		if (getModels() == null || getModels().isEmpty()) {
			return;
		}
		for (String className : getModels()) {
			try {
				Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className.trim());
				if (TemplateModel.class.isAssignableFrom(loadClass)) {
					FreeMarkerTemplateModel annotation = loadClass.getAnnotation(FreeMarkerTemplateModel.class);
					if (annotation == null || annotation.value() == null || annotation.value().length() < 1) {
						continue;
					}
					String modelName = annotation.value();
					setSharedVariable(modelName, loadClass.newInstance());
					Logger.debug(FCS.get("Register freemarker directive name {0}, class: {1}", modelName, className));
				}
			} catch (Exception e) {
				Logger.warn(FCS.get("Can't register freemarker directive class: {1}", className, e));
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		addVariable();
	}
}
