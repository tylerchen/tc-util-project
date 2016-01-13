/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker;

import java.util.List;

import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;
import org.iff.infra.util.ResourceHelper;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.Version;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
public class FreeMarkerConfiguration extends Configuration {

	public static final BeansWrapper beansWrapper = new freemarker.ext.beans.BeansWrapperBuilder(
			freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
	public static final freemarker.template.TemplateModel helper = beansWrapper.getStaticModels();

	private String directivePath;

	public FreeMarkerConfiguration() {
		super(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		init();
	}

	public FreeMarkerConfiguration(Version incompatibleImprovements) {
		super(incompatibleImprovements);
		init();
	}

	protected void init() {
		setTemplateUpdateDelayMilliseconds(0);
		setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);

		setObjectWrapper(beansWrapper);
		setDefaultEncoding("UTF-8");
		setOutputEncoding("UTF-8");
		setLocale(java.util.Locale.CHINA);
		setLocalizedLookup(false);
		setNumberFormat("#0.#####");
		setDateFormat("yyyy-MM-dd");
		setTimeFormat("HH:mm:ss");
		setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
	}

	protected void scanDirective() {
		if (getDirectivePath() == null || getDirectivePath().length() < 1) {
			return;
		}
		String[] paths = getDirectivePath().trim().split(",");
		for (String path : paths) {
			path = path.trim().replaceAll("\\.", "/");
			List<String> resources = ResourceHelper.loadResourcesInClassPath(path, ".class", "*", null);
			for (String resource : resources) {
				int packageIndex = resource.lastIndexOf(path);
				if (packageIndex > -1) {
					resource = resource.substring(packageIndex, resource.length() - ".class".length());
					resource = resource.replaceAll("\\/", ".");
					Logger.debug("Find class:" + resource);
				} else {
					continue;
				}
				try {
					Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(resource);
					if (TemplateModel.class.isAssignableFrom(loadClass)) {
						FreeMarkerTemplateModel annotation = loadClass.getAnnotation(FreeMarkerTemplateModel.class);
						if (annotation == null || annotation.value() == null || annotation.value().length() < 1) {
							continue;
						}
						String modelName = annotation.value();
						setSharedVariable(modelName, loadClass.newInstance());
						Logger.debug(
								FCS.get("Register freemarker directive name {0}, class: {1}", modelName, resource));
					}
				} catch (Exception e) {
					Logger.warn(FCS.get("Can't register freemarker directive class: {1}", resource, e));
				}
			}
		}
	}

	public String getDirectivePath() {
		return directivePath;
	}

	public void setDirectivePath(String directivePath) {
		this.directivePath = directivePath;
		//
		scanDirective();
	}

}
