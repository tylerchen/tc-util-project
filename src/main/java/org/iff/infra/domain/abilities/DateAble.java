/*******************************************************************************
 * Copyright (c) Sep 27, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import java.util.Date;

import org.iff.infra.util.Assert;
import org.iff.infra.util.ValidateHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 27, 2017
 */
public interface DateAble {

	Date getUpdateTime();

	void setUpdateTime(Date updateTime);

	Date getCreateTime();

	void setCreateTime(Date createTime);

	public class DateAbleAbilities implements Abilities {
		private DateAble able;

		public DateAbleAbilities(DateAble able) {
			super();
			Assert.notNull(able);
			this.able = able;
		}

		public static Abilities get(DateAble able) {
			return new DateAbleAbilities(able);
		}

		public void validateAdd(ValidateHelper validate) {
			able.setUpdateTime(new Date());
			able.setCreateTime(new Date());
		}

		public void validateUpdate(ValidateHelper validate) {
			able.setUpdateTime(new Date());
			able.setCreateTime(null);
		}

		public void validateDelete(ValidateHelper validate) {

		}

		public <T> T able() {
			return null;
		}

	}

}
