/*******************************************************************************
 * Copyright (c) 2018-06-06 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.infra.util.log;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;
import org.iff.infra.util.EventBusHelper;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.PreCheckHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LogHelper，未启用。
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-06-06
 * auto generate by qdp.
 */
@Deprecated
public class LogHelper {

    private static final String valuesSeparator = "`@`";
    private static final String kvSeparator = "`#`";

    private static LogAdapter log = null;

    public static void init(LogAdapter logAdapter) {
        Assert.notNull(logAdapter, "LOG LogAdapter is required!");
        log = logAdapter;
    }

    public static KeyValueConcator concator() {
        return new KeyValueConcator();
    }

    public interface LogAdapter {
        void log(String message);

        void log(Map<String, Object> data);

        void logJson(String json);

        void log(Object data);
    }

    /**
     * 用于拼接 K-V 型日志。
     */
    public static class KeyValueConcator {
        private StringBuilder sb = new StringBuilder(128);

        public KeyValueConcator concat(String key, String value) {
            if (StringUtils.isBlank(key)) {
                return this;
            }
            if (sb.length() > 0) {
                sb.append(valuesSeparator);
            }
            sb.append(key).append(kvSeparator).append(value);
            return this;
        }

        public String toString() {
            return sb.toString();
        }
    }

    public static class SplitOutLogAdapter implements LogAdapter {
        private static final org.iff.infra.util.Logger.Log Logger = org.iff.infra.util.Logger.get("FOSS.LOG.SPLITOUT");


        public void log(String message) {
            Logger.info(concator().concat("message", message).toString());
        }

        public void log(Map<String, Object> data) {
            if (data == null || data.isEmpty()) {
                return;
            }
            KeyValueConcator concator = concator();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                concator.concat(entry.getKey(), entry.getValue() == null ? null : GsonHelper.toJsonString(entry.getValue()));
            }
            Logger.info(concator.toString());
        }

        public void logJson(String json) {
            Logger.info(concator().concat("json", json).toString());
        }

        public void log(Object data) {
            Logger.info(concator().concat("json", GsonHelper.toJsonString(data)).toString());
        }
    }

    public static class ArrayLogAdapter implements LogAdapter {
        public static final int MAX_COUNT = 1024;
        public static final String eventBusName = "LOG_SAVE_LogMessage";
        private static final org.iff.infra.util.Logger.Log Logger = org.iff.infra.util.Logger.get("FOSS.LOG.ARRAY");
        private static final ReentrantLock lock = new ReentrantLock();
        private final List<LogMessage> logs = new ArrayList<LogMessage>(MAX_COUNT);
        private long lastSaveTime = System.currentTimeMillis();

        public void log(String message) {
            logs.add(LogMessage.create("String", message));
        }

        public void log(Map<String, Object> data) {
            logs.add(LogMessage.create("Map", data));
        }

        public void logJson(String json) {
            logs.add(LogMessage.create("Json", json));
        }

        public void log(Object data) {
            logs.add(LogMessage.create("Object", data));
        }

        void save() {
            if (!(logs.size() > MAX_COUNT - 50 || (logs.size() > 0 && System.currentTimeMillis() - lastSaveTime > 3000))) {
                return;
            }
            try {
                lock.lock();
                if (!(logs.size() > MAX_COUNT - 50 || (logs.size() > 0 && System.currentTimeMillis() - lastSaveTime > 3000))) {
                    return;
                }
                LogMessage[] logMessages = logs.toArray(new LogMessage[logs.size()]);
                logs.clear();
                lastSaveTime = System.currentTimeMillis();
                EventBusHelper.me().asyncEvent(eventBusName, logMessages);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class LogMessage implements Serializable {
        private String type;
        private Object data;

        public static LogMessage create(String type, Object data) {
            LogMessage lm = new LogMessage();
            lm.type = PreCheckHelper.checkNotBlank(type, "LOG type is required!");
            lm.data = data;
            return lm;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
