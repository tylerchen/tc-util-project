/*******************************************************************************
 * Copyright (c) Sep 27, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.iff.infra.util.Exceptions;
import org.iff.infra.util.PreCheckHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 27, 2017
 */
public interface ConfigAble {

	public static class ConfigAbleAbilities implements Abilities {
		private ConfigAble able;
		private String propName;
		private Object propValue;

		public ConfigAbleAbilities(ConfigAble able) {
			super();
			this.able = PreCheckHelper.checkNotNull(able);
		}

		public static ConfigAbleAbilities get(ConfigAble able) {
			return new ConfigAbleAbilities(able);
		}

		public <T> T able() {
			try {
				PropertyDescriptor prop = new PropertyDescriptor(propName, able.getClass());
				Method writeMethod = prop.getWriteMethod();
				writeMethod.invoke(able, new Object[] { propValue });
				if (able instanceof Readable) {
					ReloadAble.ReloadAbleAbilities.get((ReloadAble) able).able();
				}
			} catch (Exception e) {
				Exceptions.runtime("Invoke able error!", e, "TCU-ABL-100");
			}
			return null;
		}

		public ConfigAbleAbilities propName(String propName) {
			this.propName = PreCheckHelper.checkNotEmpty(propName);
			return this;
		}

		public ConfigAbleAbilities propValue(Object propValue) {
			this.propValue = propValue;
			return this;
		}
	}
}
