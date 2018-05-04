/*******************************************************************************
 * Copyright (c) 2015-2-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.jaxrs.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.iff.infra.util.jaxrs.gson.GsonProvider;
import org.iff.infra.util.jaxrs.xstream.XStreamProvider;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @param <T>
 * @since 2015-2-17
 */
public class GsonXmlStreamProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

	private GsonProvider gsonProvider = new GsonProvider();
	private XStreamProvider xStreamProvider = new XStreamProvider();

	public GsonXmlStreamProvider() {
	}

	public GsonProvider getGsonProvider() {
		return gsonProvider;
	}

	public void setGsonProvider(GsonProvider gsonProvider) {
		this.gsonProvider = gsonProvider;
	}

	public XStreamProvider getxStreamProvider() {
		return xStreamProvider;
	}

	public void setxStreamProvider(XStreamProvider xStreamProvider) {
		this.xStreamProvider = xStreamProvider;
	}
	//=====

	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		if (MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {
			return (T) xStreamProvider.readFrom((Class<Object>) type, genericType, annotations, mediaType, httpHeaders,
					entityStream);
		} else {
			return (T) gsonProvider.readFrom((Class<Object>) type, genericType, annotations, mediaType, httpHeaders,
					entityStream);
		}
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		if (MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {
			xStreamProvider.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
		} else {
			gsonProvider.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
		}
	}

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

}
