/*******************************************************************************
 * Copyright (c) 2018-05-30 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util;

import java.io.Closeable;
import java.util.*;

/**
 * 提供 ShutdownHook 工具类，所有需要在进程终止前关闭的资源都需要实现 Closeable 接口。
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-05-30
 * auto generate by qdp.
 */
public class ShutdownHookHelper {

    private static final Map<String, ShutdownHookResource> resources = new LinkedHashMap<String, ShutdownHookResource>();

    /**
     * 注册 ShutdownHook 资源。
     *
     * @param id
     * @param resource
     */
    synchronized public static void register(String id, Closeable resource) {
        ShutdownHookResource closeable = resources.get(id);
        Assert.isNull(closeable, "ShutdownHook resource " + id + " exists, un-register first!");
        resources.put(id, new ShutdownHookResource(id, resource));
        Logger.info("ShutdownHook resource " + id + " register, total resource number: " + resources.size());
    }

    /**
     * 删除 ShutdownHook 资源。
     *
     * @param id
     * @return
     */
    synchronized public static ShutdownHookResource unregister(String id) {
        Assert.notBlank(id, "ShutdownHook id is required!");
        return resources.remove(id);
    }

    /**
     * 资源信息
     */
    public static class ShutdownHookResource implements Runnable {

        private String id;
        private Closeable resource;

        public ShutdownHookResource(String id, Closeable resource) {
            Assert.notBlank(id, "ShutdownHook id is required!");
            Assert.notNull(resource, "ShutdownHook resource is required!");
            this.id = id;
            this.resource = resource;
        }

        public void run() {
            StreamHelper.closeWithoutError(resource);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Closeable getResource() {
            return resource;
        }

        public void setResource(Closeable resource) {
            this.resource = resource;
        }
    }

    /**
     * 用于关闭资源
     */
    public static class ShutdownHookThread implements Runnable {
        public void run() {
            List<String> keys = new ArrayList<String>(resources.keySet());
            Collections.reverse(keys);
            for (String key : keys) {
                ShutdownHookResource runnable = resources.get(key);
                if (runnable != null) {
                    try {
                        ThreadPoolHelper.executeFixed(runnable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
