/*******************************************************************************
 * Copyright (c) 2019-03-06 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * ComparatorHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-06
 * auto generate by qdp.
 */
public class ComparatorHelper {
    /**
     * Use reflect to compare objects by string, WARN: low efficiency.
     *
     * @param clazz
     * @param publicMethodName
     * @param asc
     * @return
     */
    public static Comparator<Object> toStringComparator(Class<?> clazz, String publicMethodName, boolean asc) {
        clazz = PreRequiredHelper.requireNotNull(clazz);
        publicMethodName = PreRequiredHelper.requireNotBlank(publicMethodName);
        try {
            final Method method = clazz.getMethod(publicMethodName);
            if (method.getReturnType() != String.class) {
                Exceptions.runtime("ComparatorHelper method not return the String type for method: " + publicMethodName + ", class:" + clazz.getName() + "!");
            }
            final int sortDirection = asc ? 1 : -1;
            return new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    if (o1 == null && o2 == null || o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1 * sortDirection;
                    }
                    if (o2 == null) {
                        return 1 * sortDirection;
                    }
                    try {
                        String value1 = (String) method.invoke(o1);
                        String value2 = (String) method.invoke(o2);
                        if (StringUtils.equals(value1, value2)) {
                            return 0;
                        }
                        if (value1 == null) {
                            return -1 * sortDirection;
                        }
                        if (value2 == null) {
                            return 1 * sortDirection;
                        }
                        return value1.compareTo(value2) * sortDirection;
                    } catch (Exception e) {
                        Exceptions.runtime("ComparatorHelper invoke method: " + method.toGenericString() + " error!", e);
                    }
                    return 0;
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("ComparatorHelper public method not found: " + publicMethodName + ", class:" + clazz.getName() + "!", e);
        }
        return null;
    }

    /**
     * Use reflect to compare objects by number, WARN: low efficiency.
     *
     * @param clazz
     * @param publicMethodName
     * @param asc
     * @return
     */
    public static Comparator<Object> toNumberComparator(Class<?> clazz, String publicMethodName, boolean asc) {
        clazz = PreRequiredHelper.requireNotNull(clazz);
        publicMethodName = PreRequiredHelper.requireNotBlank(publicMethodName);
        try {
            final Method method = clazz.getMethod(publicMethodName);
            Class<?> type = method.getReturnType();
            Class<?>[] cls = new Class<?>[]{Double.class, Float.class, Long.class, Integer.class, Short.class, Byte.class, double.class, float.class, long.class, int.class, short.class, byte.class};
            if (!ArrayUtils.contains(cls, type)) {
                Exceptions.runtime("ComparatorHelper method not return the Number type for method: " + publicMethodName + ", class:" + clazz.getName() + "!");
            }
            final int sortDirection = asc ? 1 : -1;
            return new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    if (o1 == null && o2 == null || o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1 * sortDirection;
                    }
                    if (o2 == null) {
                        return 1 * sortDirection;
                    }
                    try {
                        Number value1 = (Number) method.invoke(o1);
                        Number value2 = (Number) method.invoke(o2);
                        if (value1 == null && value2 == null || value1 == value2) {
                            return 0;
                        }
                        if (value1 == null) {
                            return -1 * sortDirection;
                        }
                        if (value2 == null) {
                            return 1 * sortDirection;
                        }
                        if (value1 instanceof BigDecimal && value2 instanceof BigDecimal) {
                            return ((BigDecimal) value1).compareTo((BigDecimal) value2) * sortDirection;
                        }
                        if (value1 instanceof BigInteger && value2 instanceof BigInteger) {
                            return ((BigInteger) value1).compareTo((BigInteger) value2) * sortDirection;
                        }
                        Double d1 = value1.doubleValue();
                        Double d2 = value2.doubleValue();
                        return d1.compareTo(d2) * sortDirection;
                    } catch (Exception e) {
                        Exceptions.runtime("ComparatorHelper invoke method: " + method.toGenericString() + " error!", e);
                    }
                    return 0;
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("ComparatorHelper public method not found: " + publicMethodName + ", class:" + clazz.getName() + "!", e);
        }
        return null;
    }

    /**
     * Use reflect to compare objects by date, WARN: low efficiency.
     *
     * @param clazz
     * @param publicMethodName
     * @param asc
     * @return
     */
    public static Comparator<Object> toDateComparator(Class<?> clazz, String publicMethodName, boolean asc) {
        clazz = PreRequiredHelper.requireNotNull(clazz);
        publicMethodName = PreRequiredHelper.requireNotBlank(publicMethodName);
        try {
            final Method method = clazz.getMethod(publicMethodName);
            Class<?> type = method.getReturnType();
            Class<?>[] cls = new Class<?>[]{java.util.Date.class, java.sql.Date.class, Time.class, Timestamp.class};
            if (!ArrayUtils.contains(cls, type)) {
                Exceptions.runtime("ComparatorHelper method not return the Date type for method: " + publicMethodName + ", class:" + clazz.getName() + "!");
            }
            final int sortDirection = asc ? 1 : -1;
            return new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    if (o1 == null && o2 == null || o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1 * sortDirection;
                    }
                    if (o2 == null) {
                        return 1 * sortDirection;
                    }
                    try {
                        Date value1 = (Date) method.invoke(o1);
                        Date value2 = (Date) method.invoke(o2);
                        if (value1 == null && value2 == null || value1 == value2) {
                            return 0;
                        }
                        if (value1 == null) {
                            return -1 * sortDirection;
                        }
                        if (value2 == null) {
                            return 1 * sortDirection;
                        }
                        return value1.compareTo(value2) * sortDirection;
                    } catch (Exception e) {
                        Exceptions.runtime("ComparatorHelper invoke method: " + method.toGenericString() + " error!", e);
                    }
                    return 0;
                }
            };
        } catch (Exception e) {
            Exceptions.runtime("ComparatorHelper public method not found: " + publicMethodName + ", class:" + clazz.getName() + "!", e);
        }
        return null;
    }
}
