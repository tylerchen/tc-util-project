/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * event bus helper.
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2013-2-28
 */
public class EventBusHelper {
    private static EventBusHelper me = new EventBusHelper();
    private Map<String, Map<String, EventProcess>> defaultBus = Collections
            .synchronizedMap(new LinkedHashMap<String, Map<String, EventProcess>>());

    EventBusHelper() {
        init();
    }

    public static EventBusHelper me() {
        return me;
    }

    public static void main(String[] args) {
        ConsoleAppender console = new ConsoleAppender();
        LogManager.getRootLogger().addAppender(console);
        {
            //configure the appender
            String PATTERN = "%t %d [%p|%c|%C{1}] %m%n";
            console.setLayout(new PatternLayout(PATTERN));
            console.setThreshold(Level.DEBUG);
            console.activateOptions();
        }
        Logger.changeLevel("FOSS", "debug");
        EventBusHelper.me().asyncEvent("/test", 1);
        EventBusHelper.me().asyncEvent("/test", 1);
        EventBusHelper.me().asyncEvent("/test", 1);
        EventBusHelper.me().asyncEvent("/test", 1);
    }

    /**
     * init event bus and set deadEvent processor.
     *
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public void init() {
        if (!defaultBus.containsKey("deadEvent")) {
            regist("deadEvent", new DeadEventProcessor());
        }
    }

    /**
     * if bus is empty the can set a new bus.
     *
     * @param bus
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public boolean setDefaultBus(LinkedHashMap<String, Map<String, EventProcess>> bus) {
        if (defaultBus.isEmpty()) {
            defaultBus = bus;
            return true;
        }
        return false;
    }

    /**
     * regist event.
     *
     * @param eventPath
     * @param processor
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public boolean regist(String eventPath, EventProcess processor) {
        Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
        Assert.notNull(processor, "[EventBusHelper.regist]:processor is required!");
        Map<String, EventProcess> processors = defaultBus.get(eventPath);
        if (processors == null) {
            processors = new LinkedHashMap<String, EventProcess>();
            defaultBus.put(eventPath, processors);
        }
        processors.put(processor.getName(), processor);
        return true;
    }

    /**
     * unregist event.
     *
     * @param eventPath
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public boolean unregist(String eventPath) {
        Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
        Map<String, EventProcess> remove = defaultBus.remove(eventPath);
        return remove != null;
    }

    /**
     * unregist event.
     *
     * @param eventPath
     * @param processor
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public boolean unregist(String eventPath, EventProcess processor) {
        Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
        Assert.notNull(processor, "[EventBusHelper.regist]:processor is required!");
        Map<String, EventProcess> processors = defaultBus.get(eventPath);
        if (processors != null) {
            processors.remove(processor.getName());
            return true;
        }
        return false;
    }

    /**
     * unregist event.
     *
     * @param eventPath
     * @param processorName
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public boolean unregist(String eventPath, String processorName) {
        Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
        Assert.notBlank(processorName, "[EventBusHelper.regist]:processorName is required!");
        Map<String, EventProcess> processors = defaultBus.get(eventPath);
        if (processors != null) {
            processors.remove(processorName);
            return true;
        }
        return false;
    }

    /**
     * asynchronized event.
     *
     * @param eventPath
     * @param events
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public EventBusHelper asyncEvent(final String eventPath, final Object events) {
        final Map<String, EventProcess> processors = defaultBus.get(eventPath);
        if (processors != null && !processors.isEmpty()) {
            for (final Entry<String, EventProcess> entry : processors.entrySet()) {
                ThreadPoolHelper.executeFixed(new Runnable() {
                    public void run() {
                        try {
                            entry.getValue().listen(eventPath, events);
                        } catch (Exception e) {
                            Logger.debug(FCS.get("[EventBusHelper.async_event]:eventPath={0}", eventPath), e);
                        }
                    }
                });
            }
        } else if (!"deadEvent".endsWith(eventPath)) {
            asyncEvent("deadEvent", MapHelper.toMap("sourceEventPath", eventPath, "sourceEvent", events));
        }
        return this;
    }

    /**
     * Synchronized envent.
     *
     * @param eventPath
     * @param events
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Jul 19, 2016
     */
    public EventBusHelper syncEvent(final String eventPath, final Object events) {
        final Map<String, EventProcess> processors = defaultBus.get(eventPath);
        if (processors != null && !processors.isEmpty()) {
            for (final Entry<String, EventProcess> entry : processors.entrySet()) {
                try {
                    entry.getValue().listen(eventPath, events);
                } catch (Exception e) {
                    Logger.debug(FCS.get("[EventBusHelper.sync_event]:eventPath={0}", eventPath), e);
                }
            }
        } else if (!"deadEvent".endsWith(eventPath)) {
            syncEvent("deadEvent", MapHelper.toMap("sourceEventPath", eventPath, "sourceEvent", events));
        }
        return this;
    }

    /**
     * EventProcess interface.
     *
     * @author zhaochen
     */
    public static interface EventProcess {
        void listen(String eventPath, Object events);

        String getName();
    }

    /**
     * defult dead event processor.
     *
     * @author zhaochen
     */
    public static class DeadEventProcessor implements EventProcess {

        public void listen(String eventPath, Object events) {
            if (events instanceof Map) {
                Logger.warn(FCS.get("[deadEvent]:sourceEventPath={0}", ((Map<?, ?>) events).get("sourceEventPath")));
            }
            Logger.warn(FCS.get("[deadEvent]:eventPath={0}", eventPath));
        }

        public String getName() {
            return "deadEvent";
        }
    }
}
