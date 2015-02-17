/*******************************************************************************
 * Copyright (c) 2015-2-17 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.jaxrs.xstream;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2015-2-17
 */
public class XStreamProvider extends GenericXStreamProvider<Object> {

	public XStreamProvider() {
		this(new XStream());
	}

	public XStreamProvider(XStream xstream) {
		super(xstream);
	}

}
