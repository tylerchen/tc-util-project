/*******************************************************************************
 * Copyright (c) Sep 7, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.iff.infra.util.JsonHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 7, 2016
 */
public class GsonParamConverterAndProvider implements ParamConverterProvider {

	/*ResteasyProviderFactory.getInstance().getProviderInstances();*/
	private Set<Object> providerInstancesForResteasy;

	public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType,
			final Annotation[] annotations) {
		try {
			if (providerInstancesForResteasy != null && providerInstancesForResteasy.size() > 0) {
				for (Object obj : providerInstancesForResteasy) {
					if (obj instanceof ParamConverterProvider && !(obj instanceof GsonParamConverterAndProvider)) {
						try {
							ParamConverter<T> converter = ((ParamConverterProvider) obj).getConverter(rawType,
									genericType, annotations);
							if (converter != null) {
								return converter;
							}
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return new ParamConverter<T>() {
			public T fromString(String value) {
				if (value == null) {
					return null;
				}
				return (T) JsonHelper.toObject(rawType, value);
			}

			public String toString(T value) {
				if (value == null) {
					return null;
				}
				return JsonHelper.toJson(value);
			}
		};
	}

	public Set<Object> getProviderInstancesForResteasy() {
		return providerInstancesForResteasy;
	}

	public void setProviderInstancesForResteasy(Set<Object> providerInstancesForResteasy) {
		this.providerInstancesForResteasy = providerInstancesForResteasy;
	}

}
