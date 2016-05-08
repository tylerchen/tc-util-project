/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.LoggerFactory;

/**
 * A Logger util to provide log message
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-3-14
 */
public class Logger {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger("FOSS");

	private static final Log LOGGER = new Log(log);

	public static Log get(String loggerName) {
		return new Log(StringUtils.isEmpty(loggerName) ? log : LoggerFactory.getLogger(loggerName.trim()));
	}

	public static org.slf4j.Logger getLogger() {
		return log;
	}

	public static String getName() {
		return "FOSS";
	}

	public static void debug(CharSequence message) {
		LOGGER.debug(message);
	}

	public static void debug(CharSequence message, Throwable t) {
		LOGGER.debug(message, t);
	}

	public static void error(CharSequence message) {
		LOGGER.error(message);
	}

	public static void error(CharSequence message, Throwable t) {
		LOGGER.error(message, t);
	}

	public static void info(CharSequence message) {
		LOGGER.info(message);
	}

	public static void info(CharSequence message, Throwable t) {
		LOGGER.info(message, t);
	}

	public static void trace(CharSequence message) {
		LOGGER.trace(message);
	}

	public static void trace(CharSequence message, Throwable t) {
		LOGGER.trace(message, t);
	}

	public static void warn(CharSequence message) {
		LOGGER.warn(message);
	}

	public static void warn(CharSequence message, Throwable t) {
		LOGGER.warn(message, t);
	}

	public static void changeLevel(String loggerName, String level) {
		if (loggerName == null || loggerName.length() < 1) {
			Level lv = Level.toLevel(level);
			LogManager.getRootLogger().setLevel(lv);
		} else {
			Level lv = Level.toLevel(level);
			org.apache.log4j.Logger logger = LogManager.getLogger(loggerName);
			logger.setLevel(lv);
		}
	}

	public static class Log {
		private org.slf4j.Logger logger = Logger.log;

		protected Log(org.slf4j.Logger logger) {
			this.logger = logger == null ? Logger.log : logger;
		}

		public void debug(CharSequence message) {
			if (logger.isDebugEnabled()) {
				logger.debug(message.toString());
			}
		}

		public void debug(CharSequence message, Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(message.toString(), t);
			}
		}

		public void error(CharSequence message) {
			if (logger.isErrorEnabled()) {
				logger.error(message.toString());
			}
		}

		public void error(CharSequence message, Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error(message.toString(), t);
			}
		}

		public void info(CharSequence message) {
			if (logger.isInfoEnabled()) {
				logger.info(message.toString());
			}
		}

		public void info(CharSequence message, Throwable t) {
			if (logger.isInfoEnabled()) {
				logger.info(message.toString(), t);
			}
		}

		public void trace(CharSequence message) {
			if (logger.isTraceEnabled()) {
				logger.trace(message.toString());
			}
		}

		public void trace(CharSequence message, Throwable t) {
			if (logger.isTraceEnabled()) {
				logger.trace(message.toString(), t);
			}
		}

		public void warn(CharSequence message) {
			if (logger.isWarnEnabled()) {
				logger.warn(message.toString());
			}
		}

		public void warn(CharSequence message, Throwable t) {
			if (logger.isWarnEnabled()) {
				logger.warn(message.toString(), t);
			}
		}
	}

	/**
	 * get trace id for method access trace, if trace id is empty the create a new one.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jan 28, 2016
	 */
	public static String getTraceId() {
		Object id = ThreadLocalHelper.get("TRACE_ID");
		if (id != null && id instanceof String) {
		} else {
			id = StringHelper.uuid();
			ThreadLocalHelper.set("TRACE_ID", id);
		}
		return (String) id;
	}

	/**
	 * update trace id for method access trace, if trace id is empty the create a new one.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jan 28, 2016
	 */
	public static void updateTraceId(String traceId) {
		String id = traceId == null ? getTraceId() : traceId;
		{
			ThreadLocalHelper.set("TRACE_ID", id);
		}
	}
}
