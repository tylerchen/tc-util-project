package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EventBusHelper {
	private static Map<String, Map<String, EventProcess>> defaultBus = new LinkedHashMap<String, Map<String, EventProcess>>();

	public static boolean setDefaultBus(LinkedHashMap<String, Map<String, EventProcess>> bus) {
		if (defaultBus.isEmpty()) {
			defaultBus = bus;
			return true;
		}
		return false;
	}

	public static void addDeadEvent() {
		if (!defaultBus.containsKey("deadEvent")) {
			regist("deadEvent", new DeadEventProcessor());
		}
	}

	public static boolean regist(String eventPath, EventProcess processor) {
		Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
		Assert.notNull(processor, "[EventBusHelper.regist]:processor is required!");
		addDeadEvent();
		Map<String, EventProcess> processors = defaultBus.get(eventPath);
		if (processors == null) {
			processors = new LinkedHashMap<String, EventProcess>();
			defaultBus.put(eventPath, processors);
		}
		processors.put(processor.getName(), processor);
		return true;
	}

	public static boolean unregist(String eventPath) {
		Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
		Map<String, EventProcess> remove = defaultBus.remove(eventPath);
		return remove != null;
	}

	public static boolean unregist(String eventPath, EventProcess processor) {
		Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
		Assert.notNull(processor, "[EventBusHelper.regist]:processor is required!");
		Map<String, EventProcess> processors = defaultBus.get(eventPath);
		if (processors != null) {
			processors.remove(processor.getName());
			return true;
		}
		return false;
	}

	public static boolean unregist(String eventPath, String processorName) {
		Assert.notBlank(eventPath, "[EventBusHelper.regist]:eventPath is required!");
		Assert.notBlank(processorName, "[EventBusHelper.regist]:processorName is required!");
		Map<String, EventProcess> processors = defaultBus.get(eventPath);
		if (processors != null) {
			processors.remove(processorName);
			return true;
		}
		return false;
	}

	public static void asyncEvent(final String eventPath, final Object events) {
		final Map<String, EventProcess> processors = defaultBus.get(eventPath);
		if (processors != null && !processors.isEmpty()) {
			for (final Entry<String, EventProcess> entry : processors.entrySet()) {
				new Thread() {
					public void run() {
						try {
							entry.getValue().listen(eventPath, events);
						} catch (Exception e) {
							Logger.debug(FCS.get("[EventBusHelper.async_event]:eventPath={0}", eventPath), e);
						}
					}
				}.start();
			}
		} else {
			asyncEvent("deadEvent", MapHelper.toMap("sourceEventPath", eventPath, "sourceEvent", events));
		}
	}

	public static void syncEvent(final String eventPath, final Object events) {
		final Map<String, EventProcess> processors = defaultBus.get(eventPath);
		if (processors != null && !processors.isEmpty()) {
			for (final Entry<String, EventProcess> entry : processors.entrySet()) {
				try {
					entry.getValue().listen(eventPath, events);
				} catch (Exception e) {
					Logger.debug(FCS.get("[EventBusHelper.sync_event]:eventPath={0}", eventPath), e);
				}
			}
		} else {
			syncEvent("deadEvent", MapHelper.toMap("sourceEventPath", eventPath, "sourceEvent", events));
		}
	}

	public static interface EventProcess {
		void listen(String eventPath, Object events);

		String getName();
	}

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
