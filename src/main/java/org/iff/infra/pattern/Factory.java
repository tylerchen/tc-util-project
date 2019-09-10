/*******************************************************************************
 * Copyright (c) 2018-12-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.pattern;

import java.util.List;

/**
 * <pre>
 * Factory pattern.
 * Sample Code:
 *    public static void main(String[] args) {
 *        PrintFactory.me().get("Date").print(new Date());
 *        PrintFactory.me().get("String").print("test");
 *        PrintFactory.me().register("Date", new DatePrint()).get("Date").print(new Date());
 *    }
 *
 *    public static interface PrintString {
 *        void print(Object o);
 *    }
 *
 *    public static class PrintFactory implements Factory<PrintFactory, String, PrintString> {
 *
 *        private static Map<String, PrintString> map = new HashMap<>();
 *        private static PrintFactory me = new PrintFactory().init().factory();
 *
 *        public static PrintFactory me() {
 *            return me;
 *        }
 *
 *        public PrintFactory factory() {
 *            return this;
 *        }
 *
 *        public PrintFactory init() {
 *            if (map.isEmpty()) {
 *                map.put("NotFoundPrint", new NotFoundPrint());
 *                map.put("String", new StringPrint());
 *            }
 *            return this;
 *        }
 *
 *        public PrintFactory register(String name, PrintString fact) {
 *            map.put(name, fact);
 *            return this;
 *        }
 *
 *        public PrintFactory remove(String name) {
 *            map.remove(name);
 *            return this;
 *        }
 *
 *        public boolean contains(String name) {
 *            return map.containsKey(name);
 *        }
 *
 *        public List<PrintString> getAll() {
 *            return new ArrayList<>(map.values());
 *        }
 *
 *        public PrintString get(String name) {
 *            PrintString ps = map.get(name);
 *            return ps != null ? ps : map.get("NotFoundPrint");
 *        }
 *    }
 *
 *    public static class NotFoundPrint implements PrintString {
 *        public void print(Object o) {
 *            System.out.println("NotFound for object:" + o.getClass().getName());
 *        }
 *    }
 *
 *    public static class StringPrint implements PrintString {
 *        public void print(Object o) {
 *            System.out.println(String.valueOf(o));
 *        }
 *    }
 *
 *    public static class DatePrint implements PrintString {
 *        public void print(Object o) {
 *            System.out.println(DateFormatUtils.format((Date) o, "yyyyMMddHHmmss"));
 *        }
 *    }
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-31
 * auto generate by qdp.
 */
public interface Factory<F extends Factory, K, V> {

    F factory();

    F init();

    F register(K name, V fact);

    F remove(K name);

    boolean contains(K name);

    List<V> getAll();

    V get(K name);
}
