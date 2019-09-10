/*******************************************************************************
 * Copyright (c) 2019-03-13 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CollectInfo
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-13
 * auto generate by qdp.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface CollectInfo {

    /**
     * tag is first level of the info.
     *
     * @return
     */
    String tag() default "";

    /**
     * group is second level of the info.
     *
     * @return
     */
    String group() default "";

    /**
     * the info key.
     *
     * @return
     */
    String key() default "";

    /**
     * the info value.
     *
     * @return
     */
    String description() default "";

    /**
     * the info value by using ognl el to get value.
     *
     * @return
     */
    String ognlDescription() default "";

    /**
     * if this info will update after.
     *
     * @return
     */
    boolean updateAble() default true;
}
