/*******************************************************************************
 * Copyright (c) Nov 10, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.freemarker.model;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.iff.infra.util.I18nHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.infra.util.freemarker.FreeMarkerTemplateModel;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 10, 2015
 */
@FreeMarkerTemplateModel("i18n")
@SuppressWarnings("rawtypes")
public class I18nMethod implements TemplateMethodModelEx {
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null) {
			if (arguments.size() > 0) {
				String key = arguments.size() > 0 ? arguments.get(0).toString() : null;
				String alias = arguments.size() > 1 ? arguments.get(1).toString() : "";
				String namespace = arguments.size() > 2 ? arguments.get(2).toString() : null;
				Locale locale = null;
				I18nHelper i18n = null;
				{
					Object o = ThreadLocalHelper.get("locale");
					if (o != null && o instanceof Locale) {
						locale = (Locale) o;
					} else {
						Subject subject = SecurityUtils.getSubject();
						if (subject.getPrincipal() != null && subject.isAuthenticated()) {
							locale = (Locale) subject.getSession().getAttribute("locale");
						}
					}
				}
				{
					Object o = ThreadLocalHelper.get("I18N");
					if (o != null && o instanceof I18nHelper) {
						i18n = (I18nHelper) o;
					}
				}
				if (StringUtils.isNotBlank(key)) {
					if (i18n != null) {
						return i18n.getMessage(key, alias);
					} else if (StringUtils.isNotBlank(namespace)) {
						if (locale != null) {
							return I18nHelper.get(namespace, locale).getMessage(key, alias);
						} else {
							return I18nHelper.get(namespace).getMessage(key, alias);
						}
					} else if (locale != null) {
						return I18nHelper.get(null, locale).getMessage(key, alias);
					} else {
						return I18nHelper.me().getMessage(key, alias);
					}
				} else {
					return alias;
				}
			}
		}
		return "";
	}
}
