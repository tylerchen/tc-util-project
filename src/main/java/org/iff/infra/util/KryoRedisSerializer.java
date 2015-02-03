package org.iff.infra.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;

@SuppressWarnings("unchecked")
public class KryoRedisSerializer {

	public static byte[] serialize(Object t) {
		Kryo kryo = new Kryo();
		ByteBufferOutput output = new ByteBufferOutput(2048, 1024 * 1024);
		kryo.writeClassAndObject(output, t);
		return output.toBytes();
	}

	public static <T> T deserialize(byte[] bytes) {
		Kryo kryo = new Kryo();
		Input input = new Input(bytes);
		T t = (T) kryo.readClassAndObject(input);
		return t;
	}
}