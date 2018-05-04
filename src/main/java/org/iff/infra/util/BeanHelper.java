/*******************************************************************************
 * Copyright (c) 2013-1-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * bean helper.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2013-1-31
 */
@SuppressWarnings("unchecked")
public class BeanHelper {

	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static boolean usePOVOCopyHelper = false;

	private static final ObjectMapper mapper = new ObjectMapper()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
					DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disableDefaultTyping()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	static {
		mapper.setVisibility(mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		mapper.registerModule(new SimpleModule().addSerializer(java.util.Date.class, new DateSerializer())
				.addDeserializer(java.util.Date.class, new DateDeserializer()));
	}

	/**
	 * if set to true, then use POVOCopyHelper to copy beans.
	 * @param usePOVOCopyHelper
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	synchronized public static void setUsePOVOCopyHelper(boolean usePOVOCopyHelper) {
		BeanHelper.usePOVOCopyHelper = usePOVOCopyHelper;
	}

	/**
	 * date serializer.
	 * @author zhaochen
	 */
	public static class DateSerializer extends JsonSerializer<java.util.Date> {
		public void serialize(java.util.Date date, JsonGenerator jg, SerializerProvider paramSerializerProvider)
				throws IOException, JsonProcessingException {
			jg.writeString(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
		}
	}

	/**
	 * date de-serializer.
	 * @author zhaochen
	 */
	public static class DateDeserializer extends JsonDeserializer<java.util.Date> {
		public java.util.Date deserialize(JsonParser jp, DeserializationContext dc)
				throws IOException, JsonProcessingException {
			String date = null;
			try {
				date = jp.getText();
				return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMM/dd HH:mm:ss",
						"yyyyMM/dd");
			} catch (Exception e) {
				Logger.warn(FCS.get("Input value {0} can't not format to Date", date));
			}
			return null;
		}
	}

	private BeanHelper() {
	}

	/**
	 * if usePOVOCopyHelper=true, then use POVOCopyHelper to copy the object @see POVOCopyHelper.
	 * if usePOVOCopyHelper=false, then use Jackson serialize the origin object and de-serialize to destine object.
	 * @param dest
	 * @param orig
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	public static <T> T copyProperties(Object dest, Object orig) {
		return copyProperties(dest, orig, usePOVOCopyHelper);
	}

	/**
	 * if usePOVOCopyHelper=true, then use POVOCopyHelper to copy the object @see POVOCopyHelper.
	 * if usePOVOCopyHelper=false, then use Jackson serialize the origin object and de-serialize to destine object.
	 * @param dest
	 * @param orig
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	public static <T> T copyProperties(Object dest, Object orig, boolean usePOVOCopyHelper) {
		if (orig == null) {
			return (T) null;
		}
		try {
			if (usePOVOCopyHelper) {
				return POVOCopyHelper.copyTo(orig, dest);
			} else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
				mapper.writeValue(baos, orig);
				mapper.readerForUpdating(dest).readValue(baos.toByteArray());
			}
		} catch (Exception e) {
			Exceptions.runtime("org.iff.infra.util.BeanHelper.copyProperties(Object, Object)", e);
		}
		return (T) dest;
	}

	/**
	 * if usePOVOCopyHelper=true, then use POVOCopyHelper to copy the object @see POVOCopyHelper.
	 * if usePOVOCopyHelper=false, then use GSON serialize the origin object and de-serialize to destine class.
	 * @param clazz
	 * @param orig
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	public static <T> T copyProperties(Class<T> clazz, Object orig) {
		return copyProperties(clazz, orig, usePOVOCopyHelper);
	}

	/**
	 * if usePOVOCopyHelper=true, then use POVOCopyHelper to copy the object @see POVOCopyHelper.
	 * if usePOVOCopyHelper=false, then use GSON serialize the origin object and de-serialize to destine class.
	 * @param clazz
	 * @param orig
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Feb 14, 2016
	 */
	public static <T> T copyProperties(Class<T> clazz, Object orig, boolean usePOVOCopyHelper) {
		if (orig == null) {
			return (T) null;
		}
		if (usePOVOCopyHelper) {
			return POVOCopyHelper.copyTo(orig, clazz);
		} else {
			Object fromJson = GSON.fromJson(GSON.toJsonTree(orig), clazz);
			return (T) fromJson;
		}
	}

}
