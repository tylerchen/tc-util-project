package org.iff.infra.util;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.Logger;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ReflectHelper;
import org.iff.infra.util.StringHelper;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/*******************************************************************************
 * Copyright (c) Feb 17, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 17, 2016
 */
public class WebgorGrab {
	private static final String separator = "ʃ";
	private static Map<String, Method> methods = new LinkedHashMap<String, Method>();
	private String name = StringHelper.uuid();
	private WebgorData data = null;
	private WebgorGrab parent = null;
	private boolean onRunning = false;

	//

	public static void main(String[] args) {
		Map<String, String> header = MapHelper.toMap(/**/
				"userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0", /**/
				"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", /**/
				"Accept-Encoding", "gzip, deflate", /**/
				"Accept-Language", "en-US,en;q=0.5", /**/
				"Connection", "keep-alive"/**/
		);
		Map<String, String> cookie = MapHelper.toMap(/**/
				"__NRF", "E073B3434C0EDBE251A07B0DACCD7C51", /**/
				"JSESSIONID", "0A01D733C43D8E524D321D635D8EEABB022B627E1E", /**/
				"BIGipServerotn", "869728522.50210.0000"/**/
		);
		//查询车票
		//https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2016-03-11&leftTicketDTO.from_station=GZQ&leftTicketDTO.to_station=SHH&purpose_codes=ADULT
		//查询价格
		//https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=6c00000G8604&from_station_no=01&to_station_no=05&seat_types=O9OMP&train_date=2016-03-11
		//查询经过站
		//https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=6c00000G8604&from_station_telecode=IZQ&to_station_telecode=AOH&depart_date=2016-03-11

		try {
			Map<String, Object> data = MapHelper.toMap("init",
					MapHelper.toMap(/**/
							"date", "2016-03-11", /**/
							"from_station", "GZQ", /**/
							"to_station", "SHH", /**/
							"purpose_codes", "ADULT"));
			WebgorGrab.create().header(header).cookie(cookie).connection("https://kyfw.12306.cn/otn/lcxxcx/init")/**/
					.get().printError().printText().sleep(1000).done()/**/
					//查询车票
					.connection(
							"https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=${requestData.init.date}&leftTicketDTO.from_station=${requestData.init.from_station}&leftTicketDTO.to_station=${requestData.init.to_station}&purpose_codes=${requestData.init.purpose_codes}")/**/
					.data(data).get().printError().printText().sleep(1000).done()/**/
					//循环车票
					.processData(new Process() {
						public WebgorGrab process(WebgorGrab test) {
							if (test.isJson()) {
								test.getData().getEachData(test.name).clear();
								List<Map> list = (List<Map>) ((Map) test.getData().getResult(test.name)).get("data");
								if (list != null) {
									for (Map map : list) {
										Map tmp = (Map) map.get("queryLeftNewDTO");
										if (tmp != null && tmp.get("station_train_code").toString().toUpperCase()
												.startsWith("G")) {
											test.getData()
													.setResult(test.evalString(
															"ticket${separator}${dto.train_no}${separator}${dto.start_train_date}${separator}${dto.start_time}${separator}${dto.arrive_time}",
															MapHelper.toMap("dto", tmp)), tmp);
											test.getData().getEachData(test.name).add(tmp);
										}
									}
								}
							}
							return test;
						}
					})/**/
					//查询价格
					.eachStart()/**/
					.connection(
							"https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=${requestData.init.train_no}&from_station_no=${requestData.init.from_station_no}&to_station_no=${requestData.init.to_station_no}&seat_types=${requestData.init.seat_types}&train_date=2016-03-11")/**/
					.get().printError().printText().sleep(2000)/**/
					.eachEnd(
							"price${separator}${requestData.init.train_no}${separator}${requestData.init.from_station_no}${separator}${requestData.init.to_station_no}")/**/
					//查询经过站
					.eachStart()
					.connection(
							"https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=${requestData.init.train_no}&from_station_telecode=${requestData.init.from_station_telecode}&to_station_telecode=${requestData.init.to_station_telecode}&depart_date=2016-03-11")/**/
					.get().printError().printText().sleep(2000)/**/
					.eachEnd(
							"stations${separator}${requestData.init.train_no}${separator}${requestData.init.from_station_telecode}${separator}${requestData.init.to_station_telecode}")/**/
					.processData(new Process() {
						public WebgorGrab process(WebgorGrab test) {
							System.out.println(test.parent);
							Object result = test.getData().getResult(test.name);
							for (Entry<String, Object> entry : test.getData().result.entrySet()) {
								System.out.println(entry.getKey() + " ==> " + entry.getValue());
							}
							return test;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WebgorGrab create() {
		WebgorGrab test = new WebgorGrab();
		test.name = "root";
		return test;
	}

	public WebgorGrab connection(String url) {
		if (record(getMethod("connection", String.class), url)) {
			getData().setUrl(name, url);
		}
		return this;
	}

	public WebgorGrab header(Map<String, String> map) {
		MapHelper.combine(getData().getHeader(name), map);
		return this;
	}

	public WebgorGrab header(String header) {
		if (record(getMethod("header", String.class), header)) {
			if (header != null) {
				String trueHeader = evalString(header);
				String[] split = StringUtils.split(trueHeader, ";");
				Map<String, String> map = getData().getHeader(name);
				for (String kv : split) {
					String[] split2 = StringUtils.split(kv, "=");
					if (split2.length == 2) {
						map.put(split2[0].trim(), split2[1].trim());
					}
				}
			}
		}
		return this;
	}

	public WebgorGrab clearHeader() {
		if (record(getMethod("clearHeader", null), null)) {
			getData().getHeader(name).clear();
		}
		return this;
	}

	public WebgorGrab cookie(Map<String, String> map) {
		MapHelper.combine(getData().getCookie(name), map);
		return this;
	}

	public WebgorGrab cookie(String cookie) {
		if (record(getMethod("cookie", String.class), cookie)) {
			if (cookie != null) {
				String trueCookie = evalString(cookie);
				String[] split = StringUtils.split(trueCookie, ";");
				Map<String, String> map = getData().getCookie(name);
				for (String kv : split) {
					String[] split2 = StringUtils.split(kv, "=");
					if (split2.length == 2) {
						map.put(split2[0].trim(), split2[1].trim());
					}
				}
			}
		}
		return this;
	}

	public WebgorGrab clearCookie() {
		if (record(getMethod("clearCookie", null), null)) {
			getData().getCookie(name).clear();
		}
		return this;
	}

	public WebgorGrab data(Map<String, Object> dataUpdate) {
		getData().getRequestData(name).clear();
		if (dataUpdate != null) {
			getData().getRequestData(name).putAll(dataUpdate);
		}
		return this;
	}

	public WebgorGrab postData(Map<String, String> dataUpdate) {
		getData().getPostData(name).clear();
		if (dataUpdate != null) {
			getData().getPostData(name).putAll(dataUpdate);
		}
		return this;
	}

	public WebgorGrab timeout(int timeout) {
		getData().setTimeout(name, timeout);
		return this;
	}

	public WebgorGrab done() {
		try {
			onRunning = true;
			System.out.println("Data:" + GsonHelper.toJsonString(getData().getRequestData(name)));
			List<List<Object>> record = getData().getRecord(name);
			int getOrPostIndex = -1;
			for (List<Object> list : record) {
				Method m = (Method) list.get(0);
				if ("get".equals(m.getName()) || "post".equals(m.getName())) {
					getOrPostIndex = record.indexOf(list);
				}
			}
			if (getOrPostIndex < 0) {
				Exceptions.runtime("You must call get() or post() method to process the request!");
			}
			for (int i = 0; i <= getOrPostIndex; i++) {
				List<Object> list = record.get(i);
				Method m = (Method) list.get(0);
				System.out.println("Invoke method:" + m.getName());
				m.invoke(this, list.subList(1, list.size()).toArray());
			}
			String trueUrl = evalString(getData().getUrl(name));
			if (trueUrl.length() < 1) {
				return this;
			}
			Connection connection = Jsoup.connect(trueUrl);
			System.out.println("URL:" + trueUrl);
			settingConnection(connection);
			if (getData().getMethod(name) == org.jsoup.Connection.Method.POST) {
				connection.data(getData().getPostData(name));
			}
			Response response = connection.method(getData().getMethod(name)).timeout(getData().getTimeout(name))
					.execute();
			processResult(response);
			updateSetting(response);
			for (int i = getOrPostIndex + 1; i < record.size(); i++) {
				List<Object> list = record.get(i);
				Method m = (Method) list.get(0);
				System.out.println("Invoke method:" + m.getName());
				m.invoke(this, list.subList(1, list.size()).toArray());
			}
			getData().getRecord(name).clear();
		} catch (Exception e) {
			e.printStackTrace();
			Exceptions.runtime(getData().getMethod(name).name(), e);
		} finally {
			onRunning = false;
		}
		return this;
	}

	public WebgorGrab get() {
		if (record(getMethod("get", null), null)) {
			method("GET");
		}
		return this;
	}

	public WebgorGrab post() {
		if (record(getMethod("post", null), null)) {
			method("POST");
		}
		return this;
	}

	public WebgorGrab sleep(long ms) {
		if (record(getMethod("sleep", long.class), ms)) {
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
			} catch (Exception e) {
			}
		}
		return this;
	}

	public WebgorGrab fork() {
		getData().getMark(name).add("fork-join");
		return this.copy();
	}

	public WebgorGrab join(String dataKey) {
		closekMark("fork-join");
		done();
		if (dataKey == null || dataKey.length() < 1) {
			if (this.parent != null) {
				getData().clean(this.name);
				return this.parent;
			}
		} else if (this.parent != null) {
			String evalName = evalString(dataKey);
			getData().setResult(evalName, getData().getResult(this.name));
			getData().clean(this.name);
			return this.parent;
		}
		Exceptions.runtime("No parent found for join: " + dataKey);
		return null;
	}

	@SuppressWarnings("unchecked")
	public WebgorGrab method(String methodName) {
		if (record(getMethod("method", String.class), methodName)) {
			Map<String, org.jsoup.Connection.Method> map = MapHelper.toMap("GET", org.jsoup.Connection.Method.GET,
					"POST", org.jsoup.Connection.Method.POST, "PUT", org.jsoup.Connection.Method.PUT, "DELETE",
					org.jsoup.Connection.Method.DELETE);
			org.jsoup.Connection.Method method = map.get(StringUtils.upperCase(methodName));
			method = method == null ? org.jsoup.Connection.Method.GET : method;
			getData().setMethod(this.name, method);
		}
		return this;
	}

	public WebgorGrab processData(Process process) {
		process.process(this);
		return this;
	}

	public WebgorGrab copyRecord(String parentName) {
		getData().getRecord(name).addAll(getData().getRecord(parentName));
		return this;
	}

	public WebgorGrab eachStart() {
		getData().getMark(name).add("each");
		return this.copy();
	}

	public WebgorGrab eachEnd(String dataKey, Process process) {
		closekMark("each");
		WebgorGrab copy = this.copy();
		copy.done();
		WebgorGrab test = process.process(copy);
		String evalName = copy.evalString(dataKey);
		getData().setResult(evalName, getData().getResult(test.name));
		getData().clean(test.name);
		getData().clean(copy.name);
		getData().clean(this.name);
		return this.parent;
	}

	public WebgorGrab eachEnd(String dataKey) {
		closekMark("each");
		for (Object data : getData().getEachData(this.name).toArray()) {
			if (data instanceof WebgorGrab) {
				data = ((WebgorGrab) data).getResult();
			}
			WebgorGrab tmp = this.copy().copyRecord(this.name).data(MapHelper.toMap("init", data));
			tmp.done();
			String evalName = tmp.evalString(dataKey);
			getData().setResult(evalName, getData().getResult(tmp.name));
			getData().clean(tmp.name);
		}
		getData().clean(this.name);
		return this.parent;
	}

	private WebgorGrab processResult(Response response) {
		getData().setContentType(name, response.contentType());
		getData().setCharset(name, response.charset());
		getData().setText(name, response.body());
		if (response.statusCode() == 200) {
			if (isHtml()) {
				try {
					getData().setResult(name, response.parse());
				} catch (Exception e) {
					getData().getErrors(name).add(e);
				}
			} else if (isJson()) {
				try {
					getData().setResult(name, GsonHelper.toJson(response.body()));
				} catch (Exception e) {
					getData().getErrors(name).add(e);
				}
			} else if (isText()) {
				getData().setResult(name, response.body());
			}
		} else {
			getData().getErrors(name).add(response.statusCode());
		}
		return this;
	}

	private WebgorGrab updateSetting(Response response) {
		{// update header
			MapHelper.combine(getData().getHeader(name), MapHelper.toMap("Referer", response.url().toString()));
		}
		{// update cookie
			System.out.println("Recieve Cookies:" + response.cookies());
			MapHelper.combine(getData().getCookie(name), response.cookies());
		}
		return this;
	}

	private WebgorGrab settingConnection(Connection connection) {
		{// setting header
			for (Entry<String, String> entry : getData().getHeader(name).entrySet()) {
				if ("userAgent".equalsIgnoreCase(entry.getKey())) {
					connection.userAgent(entry.getValue());
				} else {
					connection.header(entry.getKey(), entry.getValue());
				}
			}
		}
		{// setting cookie
			for (Entry<String, String> entry : getData().getCookie(name).entrySet()) {
				connection.cookie(entry.getKey(), entry.getValue());
			}
		}
		{// setting properties
			connection.validateTLSCertificates(false)/**/
					.ignoreHttpErrors(true)/**/
					.ignoreContentType(true)/**/
					.followRedirects(true)/**/
					;
		}
		return this;
	}

	public boolean isHtml() {
		return getData().getContentType(name).indexOf("html") > -1;
	}

	public boolean isJson() {
		return getData().getContentType(name).indexOf("json") > -1;
	}

	public boolean isText() {
		return getData().getContentType(name).indexOf("plain") > -1;
	}

	public Object getResult() {
		return getData().getResult(name);
	}

	public Object getText() {
		return getData().getText(name);
	}

	private WebgorGrab closekMark(String name) {
		if (this.parent == null) {
			Exceptions.runtime("Parent is null!");
		}
		if (getData().getMark(this.parent.name).size() < 0) {
			Exceptions.runtime("Not found start mark for: " + name);
		} else
			if (!getData().getMark(this.parent.name).get(getData().getMark(this.parent.name).size() - 1).equals(name)) {
			Exceptions.runtime("The mark order is wrong for: " + name);
		} else {
			getData().getMark(this.parent.name).remove(getData().getMark(this.parent.name).size() - 1);
		}
		return this;
	}

	public WebgorGrab printError() {
		if (record(getMethod("printError", null), null)) {
			for (Object e : getData().getErrors(name)) {
				if (e instanceof Exception) {
					((Exception) e).printStackTrace();
				} else {
					System.out.println("ERROR: " + e);
				}
			}
		}
		return this;
	}

	public WebgorGrab printText() {
		if (record(getMethod("printText", null), null)) {
			System.out.println(getData().getText(name));
		}
		return this;
	}

	public String evalString(String template, Map<String, Object> data) {
		try {
			if (template == null || template.length() < 1 || template.indexOf('$') < 0) {
				return template;
			}
			Map<String, Object> binding = MapHelper.toMap(/**/
					"header", getData().getHeader(name), /**/
					"cookie", getData().getCookie(name), /**/
					"timeout", getData().getTimeout(name), /**/
					"url", getData().getUrl(name), /**/
					"method", getData().getMethod(name), /**/
					"requestData", getData().getRequestData(name), /**/
					"errors", getData().getErrors(name), /**/
					"parent", this.parent, /**/
					"data", this.data, /**/
					"result", getData().getResult(name), /**/
					"text", getData().getText(name), /**/
					"contentType", getData().getContentType(name), /**/
					"charset", getData().getCharset(name), /**/
					"eachData", getData().getEachData(name), /**/
					"separator", WebgorGrab.separator, /**/
					"WebgorGrab", this);
			if (data != null) {
				for (Entry<String, Object> entry : data.entrySet()) {
					binding.put(entry.getKey(), entry.getValue());
				}
			}
			return new groovy.text.GStringTemplateEngine().createTemplate(template).make(binding).toString();
		} catch (Exception e) {
			System.out.println("Eval: " + template);
			e.printStackTrace();
		}
		return "";
	}

	public String evalString(String template) {
		return evalString(template, null);
	}

	public WebgorGrab copy() {
		WebgorGrab test = new WebgorGrab();
		test.data = this.data;
		test.parent = this;
		test.data.copy(test.name, test.parent.name);
		return test;
	}

	public static interface Process {
		WebgorGrab process(WebgorGrab test);
	}

	public WebgorData getData() {
		if (data == null) {
			data = new WebgorData();
		}
		return data;
	}

	public String getName() {
		return name;
	}

	public WebgorGrab getParent() {
		return parent;
	}

	public boolean record(Method method, Object... params) {
		if (onRunning) {
			return true;
		}
		System.out.println("Record: " + method);
		List<Object> list = new ArrayList<Object>();
		{
			list.add(method);
			if (params != null) {
				for (Object param : params) {
					list.add(param);
				}
			}
		}
		getData().getRecord(name).add(list);
		return false;
	}

	public Method getMethod(String name, Class<?>... clazz) {
		List<String> parameterTypes = new ArrayList<String>();
		if (clazz != null) {
			for (Class<?> cls : clazz) {
				parameterTypes.add(cls.getName());
			}
		}
		String key = name + "-" + StringUtils.join(parameterTypes, "-");
		Method method = methods.get(key);
		if (method == null) {
			method = ReflectHelper.getMethod(getClass(), name,
					parameterTypes.toArray(new String[parameterTypes.size()]));
			methods.put(key, method);
		}
		return method;
	}

	public class WebgorData {
		private Map<String, Map<String, String>> header = new HashMap<String, Map<String, String>>();
		private Map<String, Map<String, String>> cookie = new HashMap<String, Map<String, String>>();
		private Map<String, Integer> timeout = new HashMap<String, Integer>();
		private Map<String, String> url = new HashMap<String, String>();
		private Map<String, org.jsoup.Connection.Method> method = new HashMap<String, org.jsoup.Connection.Method>();
		//
		private Map<String, Map<String, Object>> requestData = new HashMap<String, Map<String, Object>>();
		private Map<String, Map<String, String>> postData = new HashMap<String, Map<String, String>>();
		//
		private Map<String, List<Object>> errors = new HashMap<String, List<Object>>();
		private Map<String, Object> result = new HashMap<String, Object>();
		private Map<String, String> text = new HashMap<String, String>();
		private Map<String, String> contentType = new HashMap<String, String>();//"text/html";
		private Map<String, String> charset = new HashMap<String, String>();//"UTF-8";
		private Map<String, List<String>> mark = new HashMap<String, List<String>>();
		//
		private Map<String, List<Object>> eachData = new HashMap<String, List<Object>>();
		//
		private Map<String, List<List<Object>>> record = new HashMap<String, List<List<Object>>>();

		public Map<String, String> getHeader(String name) {
			if (!header.containsKey(name)) {
				header.put(name, new HashMap<String, String>());
			}
			return header.get(name);
		}

		public Map<String, String> getCookie(String name) {
			if (!cookie.containsKey(name)) {
				cookie.put(name, new HashMap<String, String>());
			}
			return cookie.get(name);
		}

		public int getTimeout(String name) {
			if (!timeout.containsKey(name)) {
				timeout.put(name, 5000);
			}
			return timeout.get(name);
		}

		public void setTimeout(String name, int newTimeout) {
			timeout.put(name, newTimeout);
		}

		public String getUrl(String name) {
			if (!url.containsKey(name)) {
				url.put(name, "");
			}
			return url.get(name);
		}

		public void setUrl(String name, String newUrl) {
			url.put(name, newUrl == null ? "" : newUrl);
		}

		public org.jsoup.Connection.Method getMethod(String name) {
			if (!method.containsKey(name)) {
				method.put(name, org.jsoup.Connection.Method.GET);
			}
			return method.get(name);
		}

		public void setMethod(String name, org.jsoup.Connection.Method newMethod) {
			method.put(name, newMethod);
		}

		public Map<String, Object> getRequestData(String name) {
			if (!requestData.containsKey(name)) {
				requestData.put(name, new HashMap<String, Object>());
			}
			return requestData.get(name);
		}

		public Map<String, String> getPostData(String name) {
			if (!postData.containsKey(name)) {
				postData.put(name, new HashMap<String, String>());
			}
			return postData.get(name);
		}

		public List<Object> getErrors(String name) {
			if (!errors.containsKey(name)) {
				errors.put(name, new ArrayList<Object>());
			}
			return errors.get(name);
		}

		public Object getResult(String name) {
			return result.get(name);
		}

		public void setResult(String name, Object newResult) {
			result.put(name, newResult);
		}

		public String getText(String name) {
			return text.get(name);
		}

		public void setText(String name, String newText) {
			text.put(name, newText);
		}

		public String getContentType(String name) {
			if (!contentType.containsKey(name)) {
				contentType.put(name, "text/html");
			}
			return contentType.get(name);
		}

		public void setContentType(String name, String newContentType) {
			contentType.put(name, newContentType);
		}

		public String getCharset(String name) {
			if (!charset.containsKey(name)) {
				charset.put(name, "UTF-8");
			}
			return charset.get(name);
		}

		public void setCharset(String name, String newCharset) {
			charset.put(name, newCharset);
		}

		public List<String> getMark(String name) {
			if (!mark.containsKey(name)) {
				mark.put(name, new ArrayList<String>());
			}
			return mark.get(name);
		}

		public List<Object> getEachData(String name) {
			if (!eachData.containsKey(name)) {
				eachData.put(name, new ArrayList<Object>());
			}
			return eachData.get(name);
		}

		public List<List<Object>> getRecord(String name) {
			if (!record.containsKey(name)) {
				record.put(name, new ArrayList<List<Object>>());
			}
			return record.get(name);
		}

		public void copy(String name, String parentName) {
			header.put(name, MapHelper.combine(new HashMap<String, String>(), header.get(parentName)));
			cookie.put(name, MapHelper.combine(new HashMap<String, String>(), cookie.get(parentName)));
			timeout.put(name, timeout.get(parentName));
			url.put(name, url.get(parentName));
			method.put(name, method.get(parentName));
			//private Map<String, Map<String, Object>> requestData = new HashMap<String, Map<String, Object>>();
			//private Map<String, List<Object>> errors = new HashMap<String, List<Object>>();
			//private Map<String, Object> result = new HashMap<String, Object>();
			//private Map<String, String> text = new HashMap<String, String>();
			contentType.put(name, contentType.get(parentName));
			charset.put(name, charset.get(parentName));
			//private Map<String, List<String>> mark = new HashMap<String, List<String>>();
			eachData.put(name, eachData.get(parentName));
			//private Map<String, List<List<Object>>> record = new HashMap<String, List<List<Object>>>();
		}

		public void clean(String name) {
			header.remove(name);
			cookie.remove(name);
			timeout.remove(name);
			url.remove(name);
			method.remove(name);
			requestData.remove(name);
			errors.remove(name);
			result.remove(name);
			text.remove(name);
			contentType.remove(name);
			charset.remove(name);
			mark.remove(name);
			eachData.remove(name);
			record.remove(name);
		}
	}
}
