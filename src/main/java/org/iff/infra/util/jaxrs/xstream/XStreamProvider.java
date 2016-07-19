/*******************************************************************************
 * Copyright (c) 2015-2-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.jaxrs.xstream;

import org.iff.infra.util.XStreamHelper;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-17
 */
public class XStreamProvider extends GenericXStreamProvider<Object> {

	public XStreamProvider() {
		this(XStreamHelper.getXstream());
	}

	public XStreamProvider(XStream xstream) {
		super(xstream);
	}

}
