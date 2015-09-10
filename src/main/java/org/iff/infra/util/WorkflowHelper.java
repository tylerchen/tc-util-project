/*******************************************************************************
 * Copyright (c) Sep 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Sep 9, 2015
 */
public class WorkflowHelper {

	private List<Object> steps = new ArrayList<Object>();
	private Map<String, Object> result = new HashMap<String, Object>();
	private Map<String, Object> mark = new LinkedHashMap<String, Object>();

	public static interface Process {
		Object process(WorkflowHelper wf, Object params);
	}

	public static WorkflowHelper get(Object params) {
		WorkflowHelper wfh = new WorkflowHelper();
		wfh.steps.add(params);
		return wfh;
	}

	public WorkflowHelper process(Process process) {
		if (process == null) {
			return this;
		}
		try {
			Object result = process.process(this, steps.get(steps.size() - 1));
			result = MapHelper.toMap("result", result, "params", steps.get(steps.size() - 1), "error", null);
			steps.add(result);
		} catch (Throwable t) {
			result = MapHelper.toMap("result", null, "params", steps.get(steps.size() - 1), "error", t);
			steps.add(result);
		}
		return this;
	}

	public WorkflowHelper on(String name, Object value, Process process) {
		process(process);
		mark.put(name, value);
		return this;
	}

	public WorkflowHelper off(String name, Object value, Process process) {
		process(process);
		mark.put(name, value);
		return this;
	}

	public WorkflowHelper fork(Process... processes) {
		if (processes != null) {
			for (final Process process : processes) {
				new Thread() {
					public void run() {
						process(process);
					}
				}.start();
			}
		}
		return this;
	}

	public WorkflowHelper log(CharSequence message, Throwable t, int debugLevel) {
		if (message == null) {
			return this;
		}
		if (debugLevel == 0) {
			if (t == null) {
				Logger.debug(message);
			} else {
				Logger.debug(message, t);
			}
		} else if (debugLevel == 1) {
			if (t == null) {
				Logger.warn(message);
			} else {
				Logger.warn(message, t);
			}
		} else if (debugLevel == 2) {
			if (t == null) {
				Logger.info(message);
			} else {
				Logger.info(message, t);
			}
		} else if (debugLevel == 3) {
			if (t == null) {
				Logger.error(message);
			} else {
				Logger.error(message, t);
			}
		} else if (debugLevel == 4) {
			if (t == null) {
				Logger.trace(message);
			} else {
				Logger.trace(message, t);
			}
		} else {
			if (t == null) {
				Logger.debug(message);
			} else {
				Logger.debug(message, t);
			}
		}
		return this;
	}
}
