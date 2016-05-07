//package org.iff.infra.util;
//
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.WebResponse;
//
///*******************************************************************************
// * Copyright (c) Feb 17, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
// * All rights reserved.
// *
// * Contributors:
// *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
// ******************************************************************************/
//
///**
// * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
// * @since Feb 17, 2016
// */
//public class WebgorGrabHtmlUnit {
//	private static final String separator = "ʃ";
//	private static Map<String, Method> methods = new LinkedHashMap<String, Method>();
//	private String name = StringHelper.uuid();
//	private WebgorData data = null;
//	private WebgorGrabHtmlUnit parent = null;
//	private boolean onRunning = false;
//
//	//
//
//	public static void main(String[] args) {
//		Map<String, String> header = MapHelper.toMap(/**/
//				"userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0", /**/
//				"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", /**/
//				"Accept-Encoding", "gzip, deflate", /**/
//				"Accept-Language", "en-US,en;q=0.5", /**/
//				"Connection", "keep-alive"/**/
//		);
//		Map<String, String> cookie = MapHelper.toMap(/**/
//				"__NRF", "E073B3434C0EDBE251A07B0DACCD7C51", /**/
//				"JSESSIONID", "0A01D733C43D8E524D321D635D8EEABB022B627E1E", /**/
//				"BIGipServerotn", "869728522.50210.0000"/**/
//		);
//		//查询车票
//		//https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2016-03-11&leftTicketDTO.from_station=GZQ&leftTicketDTO.to_station=SHH&purpose_codes=ADULT
//		//查询价格
//		//https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=6c00000G8604&from_station_no=01&to_station_no=05&seat_types=O9OMP&train_date=2016-03-11
//		//查询经过站
//		//https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=6c00000G8604&from_station_telecode=IZQ&to_station_telecode=AOH&depart_date=2016-03-11
//
//		try {
//			Map<String, Object> data = MapHelper.toMap("init",
//					MapHelper.toMap(/**/
//							"date", "2016-03-11", /**/
//							"from_station", "GZQ", /**/
//							"to_station", "SHH", /**/
//							"purpose_codes", "ADULT"));
//			WebgorGrabHtmlUnit.create().header(header).cookie(cookie)
//					.connection("https://kyfw.12306.cn/otn/lcxxcx/init")/**/
//					.get().printError().printText().sleep(1000).done()/**/
//					//查询车票
//					.connection(
//							"https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=${requestData.init.date}&leftTicketDTO.from_station=${requestData.init.from_station}&leftTicketDTO.to_station=${requestData.init.to_station}&purpose_codes=${requestData.init.purpose_codes}")/**/
//					.data(data).get().printError().printText().sleep(1000).done()/**/
//					//循环车票
//					.processData(new Process() {
//						public WebgorGrabHtmlUnit process(WebgorGrabHtmlUnit test) {
//							if (test.isJson()) {
//								test.getData().getEachData(test.name).clear();
//								List<Map> list = (List<Map>) ((Map) test.getData().getResult(test.name)).get("data");
//								if (list != null) {
//									for (Map map : list) {
//										Map tmp = (Map) map.get("queryLeftNewDTO");
//										if (tmp != null && tmp.get("station_train_code").toString().toUpperCase()
//												.startsWith("G")) {
//											test.getData()
//													.setResult(test.evalString(
//															"ticket${separator}${dto.train_no}${separator}${dto.start_train_date}${separator}${dto.start_time}${separator}${dto.arrive_time}",
//															MapHelper.toMap("dto", tmp)), tmp);
//											test.getData().getEachData(test.name).add(tmp);
//										}
//									}
//								}
//							}
//							return test;
//						}
//					})/**/
//					//查询价格
//					.eachStart()/**/
//					.connection(
//							"https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=${requestData.init.train_no}&from_station_no=${requestData.init.from_station_no}&to_station_no=${requestData.init.to_station_no}&seat_types=${requestData.init.seat_types}&train_date=2016-03-11")/**/
//					.get().printError().printText().sleep(2000)/**/
//					.eachEnd(
//							"price${separator}${requestData.init.train_no}${separator}${requestData.init.from_station_no}${separator}${requestData.init.to_station_no}")/**/
//					//查询经过站
//					.eachStart()
//					.connection(
//							"https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=${requestData.init.train_no}&from_station_telecode=${requestData.init.from_station_telecode}&to_station_telecode=${requestData.init.to_station_telecode}&depart_date=2016-03-11")/**/
//					.get().printError().printText().sleep(2000)/**/
//					.eachEnd(
//							"stations${separator}${requestData.init.train_no}${separator}${requestData.init.from_station_telecode}${separator}${requestData.init.to_station_telecode}")/**/
//					.processData(new Process() {
//						public WebgorGrabHtmlUnit process(WebgorGrabHtmlUnit test) {
//							System.out.println(test.parent);
//							Object result = test.getData().getResult(test.name);
//							for (Entry<String, Object> entry : test.getData().result.entrySet()) {
//								System.out.println(entry.getKey() + " ==> " + entry.getValue());
//							}
//							return test;
//						}
//					});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static String[] split(String str) {
//		return StringUtils.split(str, separator);
//	}
//
//	public static WebgorGrabHtmlUnit create() {
//		WebgorGrabHtmlUnit test = new WebgorGrabHtmlUnit();
//		test.name = "root";
//		return test;
//	}
//
//	public WebgorGrabHtmlUnit templateStart(String name) {
//		if (record(getMethod("templateStart", String.class), name)) {
//			WebgorGrabHtmlUnit test = copy();
//			test.name = evalString(name);
//			getData().getMark(test.name).add("template-start");
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit templateEnd() {
//		if (record(getMethod("templateEnd", null), null)) {
//			if (closekMark("template-start")) {
//				List<List<Object>> record = getData().getRecord(name);
//				List<List<Object>> template = new ArrayList<List<Object>>();
//				for (List<Object> rec : record) {
//				}
//			} else {
//				Exceptions.runtime("No template-start mark found, use method templateStart before.");
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit fork() {
//		if (record(getMethod("fork", null), null)) {
//			getData().getMark(name).add("fork-join");
//			this.copy();
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit join(String dataKey) {
//		if (record(getMethod("join", String.class), dataKey)) {
//			closekMark("fork-join");
//			if (dataKey == null || dataKey.length() < 1) {
//				if (this.parent != null) {
//					getData().clean(this.name);
//				}
//			} else if (this.parent != null) {
//				String evalName = evalString(dataKey);
//				getData().setResult(evalName, getData().getResult(this.name));
//				getData().clean(this.name);
//			} else {
//				Exceptions.runtime("No parent found for join: " + dataKey);
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit each(String dataKey, String templateName) {
//		if (record(getMethod("each", String.class, String.class), dataKey, templateName)) {
//			getData().getMark(name).add("each");
//			getData().set(name, "eachDataKey", dataKey);
//			getData().set(name, "eachTemplate", templateName);
//		}
//		return this.copy();
//	}
//
//	public WebgorGrabHtmlUnit connection(String url) {
//		if (record(getMethod("connection", String.class), url)) {
//			getData().setUrl(name, url);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit header(Map<String, String> map) {
//		if (record(getMethod("header", Map.class), map)) {
//			MapHelper.combine(getData().getHeader(name), map);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit header(String header) {
//		if (record(getMethod("header", String.class), header)) {
//			if (header != null) {
//				String trueHeader = evalString(header);
//				String[] split = StringUtils.split(trueHeader, ";");
//				Map<String, String> map = getData().getHeader(name);
//				for (String kv : split) {
//					String[] split2 = StringUtils.split(kv, "=");
//					if (split2.length == 2) {
//						map.put(split2[0].trim(), split2[1].trim());
//					}
//				}
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit clearHeader() {
//		if (record(getMethod("clearHeader", null), null)) {
//			getData().getHeader(name).clear();
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit cookie(Map<String, String> map) {
//		if (record(getMethod("cookie", Map.class), map)) {
//			MapHelper.combine(getData().getCookie(name), map);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit cookie(String cookie) {
//		if (record(getMethod("cookie", String.class), cookie)) {
//			if (cookie != null) {
//				String trueCookie = evalString(cookie);
//				String[] split = StringUtils.split(trueCookie, ";");
//				Map<String, String> map = getData().getCookie(name);
//				for (String kv : split) {
//					String[] split2 = StringUtils.split(kv, "=");
//					if (split2.length == 2) {
//						map.put(split2[0].trim(), split2[1].trim());
//					}
//				}
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit clearCookie() {
//		if (record(getMethod("clearCookie", null), null)) {
//			getData().getCookie(name).clear();
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit data(Map<String, Object> dataUpdate) {
//		if (record(getMethod("data", Map.class), dataUpdate)) {
//			getData().getRequestData(name).clear();
//			if (dataUpdate != null) {
//				getData().getRequestData(name).putAll(dataUpdate);
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit postData(Map<String, String> dataUpdate) {
//		if (record(getMethod("postData", Map.class), dataUpdate)) {
//			getData().getPostData(name).clear();
//			if (dataUpdate != null) {
//				getData().getPostData(name).putAll(dataUpdate);
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit timeout(int timeout) {
//		if (record(getMethod("timeout", int.class), timeout)) {
//			getData().setTimeout(name, timeout);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit done() {
//		try {
//			WebgorGrabHtmlUnit webgor = null;
//			{
//				if (closekMark("each")) {
//
//				}
//			}
//			WebClient webClient = null;
//			{
//				webClient = getData().get("root", "WebClient", getData().OBJECT);
//				if (webClient == null) {
//					webClient = new WebClient();
//					webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理
//					webClient.getOptions().setJavaScriptEnabled(false);//开启js解析。对于变态网页，这个是必须的
//					webClient.getOptions().setCssEnabled(false);//开启css解析。对于变态网页，这个是必须的。
//					webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//					webClient.getOptions().setThrowExceptionOnScriptError(false);
//					webClient.getOptions().setTimeout(10000);
//				}
//				getData().set("root", "WebClient", webClient);
//			}
//			WebRequest request = new WebRequest(new URL(getData().getUrl(name)));
//			{
//				webgor.onRunning = true;
//				webgor.settingConnection(request);
//			}
//			Page page = webClient.getPage(request);
//			WebResponse response = page.getWebResponse();
//			System.out.println("Data:" + GsonHelper.toJsonString(getData().getRequestData(name)));
//			List<List<Object>> record = getData().getRecord(name);
//			int getOrPostIndex = -1;
//			for (List<Object> list : record) {
//				Method m = (Method) list.get(0);
//				if ("get".equals(m.getName()) || "post".equals(m.getName())) {
//					getOrPostIndex = record.indexOf(list);
//				}
//			}
//			if (getOrPostIndex < 0) {
//				Exceptions.runtime("You must call get() or post() method to process the request!");
//			}
//			for (int i = 0; i <= getOrPostIndex; i++) {
//				List<Object> list = record.get(i);
//				Method m = (Method) list.get(0);
//				System.out.println("Invoke method:" + m.getName());
//				m.invoke(this, list.subList(1, list.size()).toArray());
//			}
//			String trueUrl = evalString(getData().getUrl(name));
//			if (trueUrl.length() < 1) {
//				return this;
//			}
//			Connection connection = Jsoup.connect(trueUrl);
//			System.out.println("URL:" + trueUrl);
//			settingConnection(connection);
//			if (getData().getMethod(name) == org.jsoup.Connection.Method.POST) {
//				connection.data(getData().getPostData(name));
//			}
//			Response response = connection.method(getData().getMethod(name)).timeout(getData().getTimeout(name))
//					.execute();
//			processResult(response);
//			updateSetting(response);
//			for (int i = getOrPostIndex + 1; i < record.size(); i++) {
//				List<Object> list = record.get(i);
//				Method m = (Method) list.get(0);
//				System.out.println("Invoke method:" + m.getName());
//				m.invoke(this, list.subList(1, list.size()).toArray());
//			}
//			getData().getRecord(name).clear();
//		} catch (Exception e) {
//			e.printStackTrace();
//			Exceptions.runtime(getData().getMethod(name).name(), e);
//		} finally {
//			onRunning = false;
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit get() {
//		if (record(getMethod("get", null), null)) {
//			method("GET");
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit post() {
//		if (record(getMethod("post", null), null)) {
//			method("POST");
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit sleep(long ms) {
//		if (record(getMethod("sleep", long.class), ms)) {
//			try {
//				TimeUnit.MILLISECONDS.sleep(ms);
//			} catch (Exception e) {
//			}
//		}
//		return this;
//	}
//
//	@SuppressWarnings("unchecked")
//	public WebgorGrabHtmlUnit method(String methodName) {
//		if (record(getMethod("method", String.class), methodName)) {
//			Map<String, String> map = MapHelper.toMap("GET", "GET", "POST", "POST", "PUT", "PUT", "DELETE", "DELETE");
//			String method = map.get(StringUtils.upperCase(methodName));
//			method = method == null ? "GET" : method;
//			getData().setMethod(this.name, method);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit processData(Process process) {
//		if (record(getMethod("processData", Process.class), process)) {
//			process.process(this);
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit copyRecord(String parentName) {
//		if (record(getMethod("copyRecord", String.class), parentName)) {
//			getData().getRecord(name).addAll(getData().getRecord(parentName));
//		}
//		return this;
//	}
//
//	private WebgorGrabHtmlUnit processResult(WebResponse response) {
//		getData().setContentType(name, response.getContentType());
//		getData().setCharset(name, response.getContentCharset());
//		String contentAsString = response.getContentAsString();
//		getData().setText(name, contentAsString);
//		if (response.getStatusCode() == 200) {
//			if (isHtml()) {
//				try {
//					getData().setResult(name, contentAsString);
//				} catch (Exception e) {
//					getData().getErrors(name).add(e);
//				}
//			} else if (isJson()) {
//				try {
//					getData().setResult(name, GsonHelper.toJson(contentAsString));
//				} catch (Exception e) {
//					getData().getErrors(name).add(e);
//				}
//			} else if (isText()) {
//				getData().setResult(name, contentAsString);
//			}
//		} else {
//			getData().getErrors(name).add(response.getStatusCode());
//		}
//		return this;
//	}
//
//	private WebgorGrabHtmlUnit updateSetting(WebResponse response) {
//		//		{// update header
//		//			MapHelper.combine(getData().getHeader(name), MapHelper.toMap("Referer", response.url().toString()));
//		//		}
//		//		{// update cookie
//		//			System.out.println("Recieve Cookies:" + response.header("Set-Cookie"));
//		//			MapHelper.combine(getData().getCookie(name), response.cookies());
//		//			System.out.println("combine Cookies:" + getData().getCookie(name));
//		//		}
//		return this;
//	}
//
//	private WebgorGrabHtmlUnit settingConnection(WebRequest request) {
//		{// setting header
//			for (Entry<String, String> entry : getData().getHeader(name).entrySet()) {
//				System.out.println("Header, " + entry.getKey() + "=" + entry.getValue());
//				if ("userAgent".equalsIgnoreCase(entry.getKey())) {
//					connection.userAgent(entry.getValue());
//				} else {
//					connection.header(entry.getKey(), entry.getValue());
//				}
//			}
//		}
//		{// setting cookie
//			for (Entry<String, String> entry : getData().getCookie(name).entrySet()) {
//				connection.cookie(entry.getKey(), entry.getValue());
//			}
//		}
//		{// setting properties
//			connection.validateTLSCertificates(false)/**/
//					.ignoreHttpErrors(true)/**/
//					.ignoreContentType(true)/**/
//					.followRedirects(true)/**/
//					;
//		}
//		return this;
//	}
//
//	public boolean isHtml() {
//		return getData().getContentType(name).indexOf("html") > -1;
//	}
//
//	public boolean isJson() {
//		return getData().getContentType(name).indexOf("json") > -1;
//	}
//
//	public boolean isText() {
//		return getData().getContentType(name).indexOf("plain") > -1;
//	}
//
//	public Object getResult() {
//		return getData().getResult(name);
//	}
//
//	public String getText() {
//		return getData().getText(name);
//	}
//
//	private boolean closekMark(String name) {
//		if (this.parent == null) {
//			System.out.println("Parent is null!");
//			return false;
//		}
//		if (getData().getMark(this.parent.name).size() < 0) {
//			System.out.println("Not found start mark for: " + name);
//			return false;
//		} else
//			if (!getData().getMark(this.parent.name).get(getData().getMark(this.parent.name).size() - 1).equals(name)) {
//			System.out.println("The mark order is wrong for: " + name);
//			return false;
//		} else {
//			getData().getMark(this.parent.name).remove(getData().getMark(this.parent.name).size() - 1);
//			return true;
//		}
//	}
//
//	public WebgorGrabHtmlUnit printError() {
//		if (record(getMethod("printError", null), null)) {
//			for (Object e : getData().getErrors(name)) {
//				if (e instanceof Exception) {
//					((Exception) e).printStackTrace();
//				} else {
//					System.out.println("ERROR: " + e);
//				}
//			}
//		}
//		return this;
//	}
//
//	public WebgorGrabHtmlUnit printText() {
//		if (record(getMethod("printText", null), null)) {
//			System.out.println(getData().getText(name));
//		}
//		return this;
//	}
//
//	public String evalString(String template, Map<String, Object> data) {
//		try {
//			if (template == null || template.length() < 1 || template.indexOf('$') < 0) {
//				return template;
//			}
//			Map<String, Object> binding = MapHelper.toMap(/**/
//					"header", getData().getHeader(name), /**/
//					"cookie", getData().getCookie(name), /**/
//					"timeout", getData().getTimeout(name), /**/
//					"url", getData().getUrl(name), /**/
//					"method", getData().getMethod(name), /**/
//					"requestData", getData().getRequestData(name), /**/
//					"errors", getData().getErrors(name), /**/
//					"parent", this.parent, /**/
//					"data", this.data, /**/
//					"result", getData().getResult(name), /**/
//					"text", getData().getText(name), /**/
//					"contentType", getData().getContentType(name), /**/
//					"charset", getData().getCharset(name), /**/
//					"eachData", getData().getEachData(name), /**/
//					"separator", WebgorGrabHtmlUnit.separator, /**/
//					"WebgorGrab", this);
//			if (data != null) {
//				for (Entry<String, Object> entry : data.entrySet()) {
//					binding.put(entry.getKey(), entry.getValue());
//				}
//			}
//			return new groovy.text.GStringTemplateEngine().createTemplate(template).make(binding).toString();
//		} catch (Exception e) {
//			System.out.println("Eval: " + template);
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	public String evalString(String template) {
//		return evalString(template, null);
//	}
//
//	public WebgorGrabHtmlUnit copy() {
//		WebgorGrabHtmlUnit test = new WebgorGrabHtmlUnit();
//		{
//			test.data = this.data;
//			test.parent = this;
//			test.data.copy(test.name, test.parent.name);
//			addTarget(test);
//		}
//		return test;
//	}
//
//	public static interface Process {
//		WebgorGrabHtmlUnit process(WebgorGrabHtmlUnit test);
//	}
//
//	public WebgorData getData() {
//		if (data == null) {
//			data = new WebgorData();
//		}
//		return data;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public WebgorGrabHtmlUnit getParent() {
//		return parent;
//	}
//
//	public boolean record(Method method, Object... params) {
//		if (onRunning) {
//			return true;
//		}
//		System.out.println("Record: " + method);
//		List<Object> list = new ArrayList<Object>();
//		{
//			list.add(method);
//			if (params != null) {
//				for (Object param : params) {
//					list.add(param);
//				}
//			}
//		}
//		getData().getRecord(name).add(list);
//		return false;
//	}
//
//	public Method getMethod(String name, Class<?>... clazz) {
//		List<String> parameterTypes = new ArrayList<String>();
//		if (clazz != null) {
//			for (Class<?> cls : clazz) {
//				parameterTypes.add(cls.getName());
//			}
//		}
//		String key = name + "-" + StringUtils.join(parameterTypes, "-");
//		Method method = methods.get(key);
//		if (method == null) {
//			method = ReflectHelper.getMethod(getClass(), name,
//					parameterTypes.toArray(new String[parameterTypes.size()]));
//			methods.put(key, method);
//		}
//		return method;
//	}
//
//	public WebgorGrabHtmlUnit getTarget() {
//		return getData().getTarget();
//	}
//
//	public WebgorGrabHtmlUnit removeTarget() {
//		return getData().removeTarget();
//	}
//
//	public WebgorGrabHtmlUnit addTarget(WebgorGrabHtmlUnit target) {
//		return getData().addTarget(target);
//	}
//
//	public class WebgorData {
//		//{WebgorGrabName, {propertyName, propertyValue}}
//		private Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
//		private List<WebgorGrabHtmlUnit> targets = new ArrayList<WebgorGrabHtmlUnit>();
//		public final int OBJECT = 0;
//		public final int MAP = 1;
//		public final int LIST = 2;
//
//		public <T> T get(String webgor, String prop, int type) {
//			Map<String, Object> webgorMap = data.get(webgor);
//			if (webgorMap == null) {
//				webgorMap = new HashMap<String, Object>();
//				data.put(webgor, webgorMap);
//			}
//			Object value = webgorMap.get(prop);
//			if (MAP == type && value == null) {
//				value = new HashMap();
//				webgorMap.put(prop, value);
//			} else if (LIST == type && value == null) {
//				value = new ArrayList();
//				webgorMap.put(prop, value);
//			}
//			return (T) value;
//		}
//
//		public void set(String webgor, String prop, Object value) {
//			Map<String, Object> webgorMap = data.get(webgor);
//			if (webgorMap == null) {
//				webgorMap = new HashMap<String, Object>();
//				data.put(webgor, webgorMap);
//			}
//			webgorMap.put(prop, value);
//		}
//
//		public Map<String, String> getHeader(String name) {
//			return get(name, "header", MAP);
//		}
//
//		public Map<String, String> getCookie(String name) {
//			return get(name, "cookie", MAP);
//		}
//
//		public int getTimeout(String name) {
//			Integer timeout = get(name, "timeout", OBJECT);
//			return timeout == null ? 3000 : timeout;
//		}
//
//		public void setTimeout(String name, int newTimeout) {
//			set(name, "timeout", newTimeout);
//		}
//
//		public String getUrl(String name) {
//			String url = get(name, "url", OBJECT);
//			return url == null ? "" : url;
//		}
//
//		public void setUrl(String name, String newUrl) {
//			set(name, "url", newUrl);
//		}
//
//		public String getMethod(String name) {
//			String method = get(name, "method", OBJECT);
//			return method == null ? "GET" : method;
//		}
//
//		public void setMethod(String name, String newMethod) {
//			set(name, "method", newMethod);
//		}
//
//		public Map<String, Object> getRequestData(String name) {
//			return get(name, "requestData", MAP);
//		}
//
//		public Map<String, String> getPostData(String name) {
//			return get(name, "postData", MAP);
//		}
//
//		public List<Object> getErrors(String name) {
//			return get(name, "errors", LIST);
//		}
//
//		public Object getResult(String name) {
//			return get(name, "result", OBJECT);
//		}
//
//		public void setResult(String name, Object newResult) {
//			set(name, "result", newResult);
//		}
//
//		public String getText(String name) {
//			return get(name, "text", OBJECT);
//		}
//
//		public void setText(String name, String newText) {
//			set(name, "text", newText);
//		}
//
//		public String getContentType(String name) {
//			String ct = get(name, "contentType", OBJECT);
//			return ct == null ? "text/html" : ct;
//		}
//
//		public void setContentType(String name, String newContentType) {
//			set(name, "contentType", newContentType);
//		}
//
//		public String getCharset(String name) {
//			String charset = get(name, "charset", OBJECT);
//			return charset == null ? "UTF-8" : charset;
//		}
//
//		public void setCharset(String name, String newCharset) {
//			set(name, "charset", newCharset);
//		}
//
//		public List<String> getMark(String name) {
//			return get(name, "mark", LIST);
//		}
//
//		public List<Object> getEachData(String name) {
//			return get(name, "eachData", LIST);
//		}
//
//		public List<List<Object>> getRecord(String name) {
//			return get(name, "record", LIST);
//		}
//
//		public void copy(String name, String parentName) {
//			set(name, "header", MapHelper.combine(new HashMap<String, String>(), getHeader(parentName)));
//			set(name, "cookie", MapHelper.combine(new HashMap<String, String>(), getCookie(parentName)));
//			set(name, "timeout", getTimeout(parentName));
//			set(name, "url", getUrl(parentName));
//			set(name, "method", getMethod(parentName));
//			//private Map<String, Map<String, Object>> requestData = new HashMap<String, Map<String, Object>>();
//			//private Map<String, List<Object>> errors = new HashMap<String, List<Object>>();
//			//private Map<String, Object> result = new HashMap<String, Object>();
//			//private Map<String, String> text = new HashMap<String, String>();
//			set(name, "contentType", getContentType(parentName));
//			set(name, "charset", getCharset(parentName));
//			//private Map<String, List<String>> mark = new HashMap<String, List<String>>();
//			set(name, "eachData", Arrays.asList(getEachData(parentName).toArray()));
//			//private Map<String, List<List<Object>>> record = new HashMap<String, List<List<Object>>>();
//		}
//
//		public void clean(String name) {
//			data.remove(name);
//		}
//
//		public WebgorGrabHtmlUnit getTarget() {
//			return targets.isEmpty() ? null : targets.get(targets.size() - 1);
//		}
//
//		public WebgorGrabHtmlUnit removeTarget() {
//			return targets.isEmpty() ? null : targets.remove(targets.size() - 1);
//		}
//
//		public WebgorGrabHtmlUnit addTarget(WebgorGrabHtmlUnit target) {
//			targets.add(target);
//			return target;
//		}
//	}
//}
