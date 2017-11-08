/*******************************************************************************
 * Copyright (c) Nov 3, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.log;

import org.iff.infra.util.StringHelper;
import org.iff.infra.util.ThreadLocalHelper;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 输出跟踪ID：线程ID/IP/SessionID/loginId.详细的TraceId组成参看：ShiroTraceIdFilter。
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 3, 2017
 */
public class TraceIdConvert extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return getTraceId();
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

}
