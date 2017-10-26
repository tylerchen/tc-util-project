/*******************************************************************************
 * Copyright (c) Sep 27, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;
import org.iff.infra.util.ValidateHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 27, 2017
 */
public interface StatusAble {

	String getStatus();

	void setStatus(String status);

	public class StatusAbleAbilities implements Abilities {
		private StatusAble able;

		public StatusAbleAbilities(StatusAble able) {
			super();
			Assert.notNull(able);
			this.able = able;
		}

		public static Abilities get(StatusAble able) {
			return new StatusAbleAbilities(able);
		}

		public void validateAdd(ValidateHelper validate) {
			String[] keys = new String[] { "Y", "N" };
			validate.inArray(able.getClass().getSimpleName() + ".status", able.getStatus(), "iff.validate.notInArray",
					"{0} is not in array!", keys);
		}

		public void validateUpdate(ValidateHelper validate) {
			String[] keys = new String[] { "Y", "N" };
			if (StringUtils.isNotBlank(able.getStatus())) {
				validate.inArray(able.getClass().getSimpleName() + ".status", able.getStatus(),
						"iff.validate.notInArray", "{0} is not in array!", keys);
			} else {
				able.setStatus(null);
			}
		}

		public void validateDelete(ValidateHelper validate) {

		}

		public <T> T able() {
			return null;
		}

	}

}
