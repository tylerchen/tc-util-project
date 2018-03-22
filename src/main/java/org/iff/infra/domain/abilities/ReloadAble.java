/*******************************************************************************
 * Copyright (c) Sep 27, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import org.iff.infra.util.PreCheckHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 27, 2017
 */
public interface ReloadAble {

	void reload();

	public static class ReloadAbleAbilities implements Abilities {
		private ReloadAble able;

		public ReloadAbleAbilities(ReloadAble able) {
			super();
			this.able = PreCheckHelper.checkNotNull(able);
		}

		public static ReloadAbleAbilities get(ReloadAble able) {
			return new ReloadAbleAbilities(able);
		}

		public <T> T able() {
			able.reload();
			return null;
		}
	}
}
