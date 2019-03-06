/*******************************************************************************
 * Copyright (c) 2019-03-06 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * InterfaceHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-06
 * auto generate by qdp.
 */
public class InterfaceHelper {

    /**
     * return a runnable instance to invoked public static method.
     *
     * @param clazz
     * @param publicStaticMethodName
     * @param throwExceptionWhenInvoke
     * @return
     */
    public static Runnable toRunnable(Class<?> clazz, String publicStaticMethodName, boolean throwExceptionWhenInvoke) {
        clazz = PreRequiredHelper.requireNotNull(clazz);
        publicStaticMethodName = PreRequiredHelper.requireNotBlank(publicStaticMethodName);
        try {
            Method method = clazz.getMethod(publicStaticMethodName);
            return new Runnable() {
                public void run() {
                    try {
                        method.invoke(null, null);
                    } catch (Exception e) {
                        if (throwExceptionWhenInvoke) {
                            Exceptions.runtime("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        } else {
                            Logger.error("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        }
                    }
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("InterfaceHelper public static method not found: " + publicStaticMethodName, e);
        }
        return null;
    }

    /**
     * return a runnable instance to invoked public method.
     *
     * @param instance
     * @param publicMethodName
     * @param throwExceptionWhenInvoke
     * @return
     */
    public static Runnable toRunnable(Object instance, String publicMethodName, boolean throwExceptionWhenInvoke) {
        instance = PreRequiredHelper.requireNotNull(instance);
        publicMethodName = PreRequiredHelper.requireNotBlank(publicMethodName);
        try {
            Method method = instance.getClass().getMethod(publicMethodName);
            return new Runnable() {
                public void run() {
                    try {
                        method.invoke(null, null);
                    } catch (Exception e) {
                        if (throwExceptionWhenInvoke) {
                            Exceptions.runtime("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        } else {
                            Logger.error("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        }
                    }
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("InterfaceHelper public method not found: " + publicMethodName + ", class:" + instance.getClass().getName() + "!", e);
        }
        return null;
    }

    /**
     * return a closeable instance to invoked public static method.
     *
     * @param clazz
     * @param publicStaticMethodName
     * @param throwExceptionWhenInvoke
     * @return
     */
    public static Closeable toCloseable(Class<?> clazz, String publicStaticMethodName, boolean throwExceptionWhenInvoke) {
        clazz = PreRequiredHelper.requireNotNull(clazz);
        publicStaticMethodName = PreRequiredHelper.requireNotBlank(publicStaticMethodName);
        try {
            Method method = clazz.getMethod(publicStaticMethodName);
            return new Closeable() {
                public void close() throws IOException {
                    try {
                        method.invoke(null, null);
                    } catch (Exception e) {
                        if (throwExceptionWhenInvoke) {
                            Exceptions.runtime("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        } else {
                            Logger.error("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        }
                    }
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("InterfaceHelper public static method not found: " + publicStaticMethodName, e);
        }
        return null;
    }

    /**
     * return a closeable instance to invoked public method.
     *
     * @param instance
     * @param publicMethodName
     * @param throwExceptionWhenInvoke
     * @return
     */
    public static Closeable toCloseable(Object instance, String publicMethodName, boolean throwExceptionWhenInvoke) {
        instance = PreRequiredHelper.requireNotNull(instance);
        publicMethodName = PreRequiredHelper.requireNotBlank(publicMethodName);
        try {
            Method method = instance.getClass().getMethod(publicMethodName);
            return new Closeable() {
                public void close() throws IOException {
                    try {
                        method.invoke(null, null);
                    } catch (Exception e) {
                        if (throwExceptionWhenInvoke) {
                            Exceptions.runtime("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        } else {
                            Logger.error("InterfaceHelper invoke method: " + method.toGenericString() + " error!", e);
                        }
                    }
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("InterfaceHelper public method not found: " + publicMethodName + ", class:" + instance.getClass().getName() + "!", e);
        }
        return null;
    }
}
