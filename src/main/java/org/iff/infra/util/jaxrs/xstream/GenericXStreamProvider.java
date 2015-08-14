/*******************************************************************************
 * Copyright (c) 2015-2-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.jaxrs.xstream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.google.common.base.Charsets;
import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-17
 */
public abstract class GenericXStreamProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

	private XStream xstream;

	public GenericXStreamProvider() {
	}

	public GenericXStreamProvider(XStream xstream) {
		this.xstream = xstream;
	}

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException,
			WebApplicationException {
		return (T) getXstream().fromXML(new InputStreamReader(entityStream, Charsets.UTF_8));
	}

	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
			WebApplicationException {
		final Writer w = new OutputStreamWriter(entityStream, Charsets.UTF_8);
		getXstream().toXML(t, w);
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

}
