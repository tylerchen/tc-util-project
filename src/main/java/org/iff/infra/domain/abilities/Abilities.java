/*******************************************************************************
 * Copyright (c) Sep 27, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import org.iff.infra.util.ValidateHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 27, 2017
 */
public interface Abilities {

	void validateAdd(ValidateHelper validate);

	void validateUpdate(ValidateHelper validate);

	void validateDelete(ValidateHelper validate);

	<T> T able();
}
