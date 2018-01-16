/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * common exceptions.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-3-14
 */
public class Exceptions {

	public static void main(String[] args) {
		try {
			try {
				try {
					throw new Exception("null");
				} catch (Exception e) {
					Exceptions.runtime("entity", e);
				}
			} catch (Exception e) {
				Exceptions.exception("dao", e);
			}
		} catch (Exception e) {
			Exceptions.runtime("service", e);
		}
	}

	public static interface FossThrow {
		FossThrow combineException(Throwable e);

		FossThrow addMessage(List<CharSequence> messages);

		FossThrow addMessage(CharSequence... messages);

		List<CharSequence> getMessages();
	}

	@SuppressWarnings("serial")
	public static class FossException extends Exception implements FossThrow {
		private List<CharSequence> messages = new ArrayList<CharSequence>();

		public FossException() {
			super();
		}

		public FossException(String message, Throwable cause) {
			super(message, cause);
			addMessage(message);
			combineException(cause);
		}

		public FossException(String message) {
			super(message);
			addMessage(message);
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder().append('\n');
			for (int i = 0; i < messages.size(); i++) {
				sb.append(i < 9 ? "[0" : "[").append(i).append("] ").append(messages.get(i)).append("\n");
			}
			sb.append(messages.size() < 9 ? "[0" : "[").append(messages.size()).append("] ")
					.append(getCause().getMessage()).append("\n");
			return sb.toString();
		}

		public FossThrow combineException(Throwable e) {
			if (e instanceof FossThrow) {
				FossThrow ft = (FossThrow) e;
				addMessage(ft.getMessages());
			}
			return this;
		}

		public FossThrow addMessage(List<CharSequence> msg) {
			if (msg != null) {
				messages.addAll(msg);
			}
			return this;
		}

		public FossThrow addMessage(CharSequence... msg) {
			if (msg != null) {
				messages.addAll(Arrays.asList(msg));
			}
			return this;
		}

		public List<CharSequence> getMessages() {
			return messages;
		}
	}

	@SuppressWarnings("serial")
	public static class FossRuntimeException extends RuntimeException implements FossThrow {
		private List<CharSequence> messages = new ArrayList<CharSequence>();

		public FossRuntimeException() {
			super();
		}

		public FossRuntimeException(String message, Throwable cause) {
			super(message, cause);
			addMessage(message);
			combineException(cause);
		}

		public FossRuntimeException(String message) {
			super(message);
			addMessage(message);
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder().append('\n');
			for (int i = 0; i < messages.size(); i++) {
				sb.append(i < 9 ? "[0" : "[").append(i).append("] ").append(messages.get(i)).append("\n");
			}
			sb.append(messages.size() < 9 ? "[0" : "[").append(messages.size()).append("] ")
					.append(getCause().getMessage()).append("\n");
			return sb.toString();
		}

		public FossThrow combineException(Throwable e) {
			if (e instanceof FossThrow) {
				FossThrow ft = (FossThrow) e;
				addMessage(ft.getMessages());
			}
			return this;
		}

		public FossThrow addMessage(List<CharSequence> msg) {
			if (msg != null) {
				messages.addAll(msg);
			}
			return this;
		}

		public FossThrow addMessage(CharSequence... msg) {
			if (msg != null) {
				messages.addAll(Arrays.asList(msg));
			}
			return this;
		}

		public List<CharSequence> getMessages() {
			return messages;
		}
	}

	/**
	 * throw Exception with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param e
	 * @throws Exception
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void exception(CharSequence message, Throwable e) throws Exception {
		if (e instanceof FossThrow) {
			FossException exception = new FossException(message.toString(), e.getCause());
			exception.addMessage(((FossThrow) e).getMessages());
			throw exception;
		}
		throw new FossException("[FOSS-1001][" + e.getClass().getSimpleName() + "][" + message + "]", e);
	}

	/**
	 * throw Exception without Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @throws Exception
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void exception(CharSequence message) throws Exception {
		throw new FossException("[FOSS-1002][Exception][" + message + "]");
	}

	/**
	 * throw RuntimeException with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param e
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message, Throwable e) {
		if (e instanceof FossThrow) {
			FossRuntimeException exception = new FossRuntimeException(message.toString(), e.getCause());
			exception.addMessage(((FossThrow) e).getMessages());
			throw exception;
		}
		throw new FossRuntimeException("[FOSS-1003][" + e.getClass().getSimpleName() + "][" + message + "]", e);
	}

	/**
	 * throw RuntimeException without Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message) {
		throw new FossRuntimeException("[FOSS-1004][RuntimeException][" + message + "]");
	}
}
