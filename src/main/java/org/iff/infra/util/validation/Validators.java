/*******************************************************************************
 * Copyright (c) 2019-03-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 ******************************************************************************/
package org.iff.infra.util.validation;

import org.iff.infra.util.ArgumentsHelper;

/**
 * Validators
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-28
 * auto generate by qdp.
 */
public class Validators {
    static interface Validator {
        boolean validate(Object... args);
    }

    public static class InArray implements Validator {
        private static final ArgumentsHelper.Eager eager = ArgumentsHelper.Eager.create(Object.class, Object[].class);

        public boolean validate(Object... args) {
            eager.acceptArguments(args);
            return ValidationMethods.inArray(eager.arguments().get(0), (Object[]) eager.arguments().get(1));
        }
    }
}
