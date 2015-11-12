/*******************************************************************************
 * Copyright (c) Nov 11, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker;

import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 11, 2015
 */
public class SpringFreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

	private Configuration customConfiguration;

	protected Configuration newConfiguration() throws IOException, TemplateException {
		if (getCustomConfiguration() == null) {
			return super.newConfiguration();
		} else {
			return getCustomConfiguration();
		}
	}

	public Configuration getCustomConfiguration() {
		return customConfiguration;
	}

	public void setCustomConfiguration(Configuration customConfiguration) {
		this.customConfiguration = customConfiguration;
	}

}
