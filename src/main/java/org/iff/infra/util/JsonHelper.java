/*******************************************************************************
 * Copyright (c) 2013-2-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * json helper default use gson.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-28
 */
@SuppressWarnings("unchecked")
public final class JsonHelper {

	/* gson use yyyy-MM-dd HH:mm:ss date format */
	public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
			.registerTypeAdapter(Date.class, new DateTypeAdapter()).create();;

	private JsonHelper() {
	}

	public static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
		public JsonElement serialize(Date ts, Type t, JsonSerializationContext jsc) {
			String dfString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ts.getTime()));
			return new JsonPrimitive(dfString);
		}

		public Date deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException {
			if (!(json instanceof JsonPrimitive)) {
				throw new JsonParseException("The date should be a string value");
			}

			String asString = json.getAsString();
			if (StringUtils.isBlank(asString)) {
				return null;
			}
			Date date = null;
			try {
				date = DateUtils.parseDate(asString, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss",
						"yyyy/MM/dd", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
			if (t == Date.class) {
				return date;
			} else if (t == Timestamp.class) {
				return new Timestamp(date.getTime());
			} else if (t == java.sql.Date.class) {
				return new java.sql.Date(date.getTime());
			} else {
				throw new IllegalArgumentException(getClass() + " cannot deserialize to " + t);
			}
		}
	}

	/**
	 * json string to object.
	 * @param clazz
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> T toObject(Class<T> clazz, CharSequence json) {
		return (T) GSON.fromJson(json.toString(), clazz);
	}

	/**
	 * json string to List Object.
	 * @param clazz
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> List<T> toObjectList(Class<T> clazz, CharSequence json) {
		return (List<T>) GSON.fromJson(json.toString(), new TypeToken<List<T>>() {
		}.getType());
	}

	/**
	 * json string to list object by class name.
	 * @param className
	 * @param json
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static <T> List<T> toObjectList(String className, CharSequence json) {
		try {
			return (List<T>) GSON.fromJson(json.toString(), new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	/**
	 * object to json string.
	 * @param obj
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}
}
