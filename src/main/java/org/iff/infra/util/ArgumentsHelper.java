/*******************************************************************************
 * Copyright (c) 2019-03-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ArgumentsHelper
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-03-28
 * auto generate by qdp.
 */
public class ArgumentsHelper {
    public static class Eager {
        protected List<Class> argumentTypes = new ArrayList<>();
        protected List<Object> arguments = new ArrayList<>();

        protected Eager() {
        }

        public static Eager create(Class... classes) {
            return new Eager().acceptTypes(classes);
        }

        public static Eager create(List<Class> classes) {
            return new Eager().acceptTypes(classes);
        }

        public static Eager create(String... classes) {
            return new Eager().acceptTypes(classes);
        }

        protected Eager acceptTypes(Class... classes) {
            if (classes == null || classes.length < 1) {
                return this;
            }
            for (Class cls : classes) {
                argumentTypes.add(cls);
            }
            return this;
        }

        protected Eager acceptTypes(List<Class> classes) {
            if (classes == null || classes.size() < 1) {
                return this;
            }
            argumentTypes.addAll(classes);
            return this;
        }

        protected Eager acceptTypes(String... classes) {
            if (classes == null || classes.length < 1) {
                return this;
            }
            for (String name : classes) {
                try {
                    Class cls = Class.forName(name);
                    argumentTypes.add(cls);
                } catch (Exception e) {
                    Exceptions.runtime("ArgumentsHelper.Eager class not found!");
                }
            }
            return this;
        }

        public Eager acceptArguments(Object... args) {
            if ((args == null || args.length < 1) && argumentTypes.isEmpty()) {
                return this;
            }
            if (args != null && args.length == argumentTypes.size()) {
                for (int i = 0; i < args.length; i++) {
                    String type = argumentTypes.get(i).getName();
                    TypeConvertHelper.TypeConvert convert = TypeConvertHelper.me().getNullable(type);
                    if (convert == null) {
                        Exceptions.runtime("ArgumentsHelper.Eager can not accept args!");
                    }
                    arguments.add(convert.convert(type, args[i], args[i].getClass(), null));
                }
            }
            Exceptions.runtime("ArgumentsHelper.Eager can not accept args!");
            return this;
        }

        public List<Class> argumentTypes() {
            return argumentTypes;
        }

        public List<Object> arguments() {
            return arguments;
        }
    }
}
