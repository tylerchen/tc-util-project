/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;

import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;

/**
 * KryoRedisSerializer.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
@SuppressWarnings("unchecked")
public class KryoRedisSerializer {

	/**
	 * serialize an object.
	 * @param t
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static byte[] serialize(Object t) {
		Kryo kryo = new KryoReflectionFactorySupport();
		ByteBufferOutput output = new ByteBufferOutput(2048, 1024 * 1024);
		kryo.writeClassAndObject(output, t);
		return output.toBytes();
	}

	/**
	 * deserialize from bytes.
	 * @param bytes
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T deserialize(byte[] bytes) {
		Kryo kryo = new KryoReflectionFactorySupport();
		Input input = new Input(bytes);
		T t = (T) kryo.readClassAndObject(input);
		return t;
	}
}