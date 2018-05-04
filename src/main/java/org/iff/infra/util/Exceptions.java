/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

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
					Exceptions.runtime("entity", e, "ENT-001");
				}
			} catch (Exception e) {
				Exceptions.exception("dao", e, "DAO-001");
			}
		} catch (Exception e) {
			Exceptions.runtime("service", e, "SVC-001");
		}
	}

	public static interface FossThrow {
		String getErrorCode();
	}

	@SuppressWarnings("serial")
	public static class FossException extends Exception implements FossThrow {
		/**
		 * exception code.
		 */
		private String errorCode = "FOSS-000";

		public FossException() {
			super();
		}

		public FossException(String message, Throwable cause) {
			super(message, cause, true, false);
		}

		public FossException(String message, String errorCode) {
			super(message);
			setErrorCode(errorCode == null ? "FOSS-000" : errorCode);
		}

		public FossException(String message, Throwable cause, String errorCode) {
			super(message, cause, true, false);
			setErrorCode(errorCode == null ? "FOSS-000" : errorCode);
		}

		public FossException(String message) {
			super(message);
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			sb.append('[').append(getErrorCode()).append("] ").append(super.getMessage());
			return sb.toString();
		}

		public String getErrorCode() {
			return this.errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String toString() {
			String s = "FossThrow";
			String message = getLocalizedMessage();
			return (message != null) ? (s + ": " + message) : s;
		}

	}

	@SuppressWarnings("serial")
	public static class FossRuntimeException extends RuntimeException implements FossThrow {
		/**
		 * exception code.
		 */
		private String errorCode = "FOSS-001";

		public FossRuntimeException() {
			super();
		}

		public FossRuntimeException(String message, Throwable cause) {
			super(message, cause, true, false);
		}

		public FossRuntimeException(String message, String errorCode) {
			super(message);
			setErrorCode(errorCode == null ? "FOSS-001" : errorCode);
		}

		public FossRuntimeException(String message, Throwable cause, String errorCode) {
			super(message, cause, true, false);
			setErrorCode(errorCode == null ? "FOSS-001" : errorCode);
		}

		public FossRuntimeException(String message) {
			super(message);
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			sb.append('[').append(getErrorCode()).append("] ").append(super.getMessage());
			return sb.toString();
		}

		public String getErrorCode() {
			return this.errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String toString() {
			String s = "FossThrow";
			String message = getLocalizedMessage();
			return (message != null) ? (s + ": " + message) : s;
		}
	}

	/**
	 * throw Exception with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param e
	 * @param errorCode
	 * @throws Exception
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void exception(CharSequence message, Throwable e, String errorCode) throws Exception {
		String newMessage = message == null ? null : message.toString();
		throw new FossException(newMessage, e, errorCode);
	}

	/**
	 * throw Exception with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param errorCode
	 * @throws Exception
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void exception(CharSequence message, String errorCode) throws Exception {
		String newMessage = message == null ? null : message.toString();
		throw new FossException(newMessage, errorCode);
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
		String newMessage = message == null ? null : message.toString();
		throw new FossException(newMessage, e);
	}

	/**
	 * throw Exception without Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @throws Exception
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void exception(CharSequence message) throws Exception {
		String newMessage = message == null ? null : message.toString();
		throw new FossException(newMessage);
	}

	/**
	 * throw RuntimeException with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param e
	 * @param errorCode
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message, Throwable e, String errorCode) {
		String newMessage = message == null ? null : message.toString();
		throw new FossRuntimeException(newMessage, e, errorCode);
	}

	/**
	 * throw RuntimeException with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param errorCode
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message, String errorCode) {
		String newMessage = message == null ? null : message.toString();
		throw new FossRuntimeException(newMessage, errorCode);
	}

	/**
	 * throw RuntimeException with Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @param e
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message, Throwable e) {
		String newMessage = message == null ? null : message.toString();
		throw new FossRuntimeException(newMessage, e);
	}

	/**
	 * throw RuntimeException without Throwable error
	 * @param message you can use com.foreveross.infra.util.FormatableCharSequence.get
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-3-14
	 */
	public static void runtime(CharSequence message) {
		String newMessage = message == null ? null : message.toString();
		throw new FossRuntimeException(newMessage);
	}
}
