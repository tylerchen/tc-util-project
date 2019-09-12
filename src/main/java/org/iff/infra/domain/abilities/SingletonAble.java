/*******************************************************************************
 * Copyright (c) 2019-04-10 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.domain.abilities;

import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.PreRequiredHelper;

/**
 * SingletonAble, implements this interface MUST have a public static field with name "INSTANCE".
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-04-10
 * auto generate by qdp.
 */
public interface SingletonAble {
    public static SingletonAble singletonInstance(Class<?> singletonAble) {
        PreRequiredHelper.requireNotNull(singletonAble);
        try {
            return (SingletonAble) singletonAble.getField("INSTANCE").get(null);
        } catch (Exception e) {
            Exceptions.runtime(FCS.get("SingletonAble there is no public static INSTANCE field declare in class {name}, implements first!", singletonAble.getName()), e);
        }
        return null;
    }
}
