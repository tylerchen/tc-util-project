import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.WebgorGrab;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class WebgorTest {

	public static void main0(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 61; i++) {
			list.add(i);
		}
		for (int i = 1; i < 121; i++) {
			if (i % 7 == 0) {
				list.add(i + 60);
			}
		}
		System.out.println(Arrays.toString(list.toArray()));
	}

	public static void main4(String[] args) {
		Map<String, String> header = MapHelper.toMap(/**/
				"userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0", /**/
				"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", /**/
				"Accept-Encoding", "gzip, deflate", /**/
				"Accept-Language", "en-US,en;q=0.5", /**/
				"Connection", "keep-alive"/**/
		);
		WebgorGrab.create().data(MapHelper.toMap("dep", "CAN", "arr", "BJS")).timeout(30000)/**/
				.header(header)
				.connection(
						"http://www.tianxun.com/intl-round-ccan-hkga.html?depdate=2016-03-19&rtndate=2016-03-23&cabin=Economy&adult=1&child=0&infant=0")/**/
				.cookie("SSSessionID=051u08456ec38603f6af0606720518334130; geoCountryCode=CN; D_SID=223.73.119.132:0WgormfvgmTWKyZbMb9EXmApdrN8p7MGThL6tl7oeus; D_PID=7E87B955-84EB-3578-A991-B8948732DC33; D_IID=9DA04AF9-F5D8-3E56-8799-3E5F93EFE1D7; D_UID=A22B48D8-4BE0-3CE5-82C3-48B87BAF7D90; D_HID=NJbZ32/d9jKeUUO6NWme+Gh5ZUTKbbzUdEQga4Xjy8A; fCabin=Economy; fDepDate=2016-03-19; fRtnDate=2016-03-23; fDepCityCode=CCAN; fDstCityCode=HKGA; fRoundType=2; fDirect=0; sessionid=1458321506471s11601; visitorid=1458321506471.10984; _ga=GA1.2.1460885560.1458321507; _gat_tianxun=1; _gat_skyscanner=1; _jzqa=1.4202611871395260000.1458321507.1458321507.1458321507.1; _jzqb=1.10.10.1458321507.1; _jzqc=1; _jzqx=1.1458321507.1458321507.1.jzqsr=tianxun%2Ecom|jzqct=/captchapage%2Ephp.-; _jzqckmp=1; Hm_lvt_3101d9f8f68af26aad2db3bf9992bd10=1458112897,1458305008,1458305046,1458319353; Hm_lpvt_3101d9f8f68af26aad2db3bf9992bd10=1458321793; __gads=ID=97736b80bb474a6c:T=1458321509:S=ALNI_Mb-oeF3B_o0PycGwgQZRu90VUqZew; ad_collapsed=1; tx_access_key=VlkFCVICUQ9JAwdTUQNRAVZQXVkCCQcEBw5QBVdQAldTBAoHVAVSCFY%3D; _qzja=1.726277769.1458321507212.1458321507212.1458321507212.1458321763977.1458321792788..0.0.6.1; _qzjb=1.1458321507212.6.0.0.0; _qzjc=1; _qzjto=6.1.0")
				.get().printError().printText().done()/**/
				.processData(new WebgorGrab.Process() {
					public WebgorGrab process(WebgorGrab test) {
						Document doc = (Document) test.getResult();
						Elements byTag = doc.getElementsByTag("script");
						ListIterator<Element> iterator = byTag.listIterator();
						while (iterator.hasNext()) {
							Element next = iterator.next();
							if (next.hasAttr("src")) {
								System.out.println(next.attr("src"));
							}
						}
						return test;
					}
				})/**/
				.connection(
						"http://www.tianxun.com/flight/ajax_intl_list.php?page=1&sort=price&order=asc&dep_flight_city_code=CAN&dst_flight_city_code=HKG&depart_date=2016-03-19&return_date=2016-03-23&cabin_type=Economy&adults=1&children=0&infants=0&token=VlkFCVIGUAZJVlYIAQFTV1cEVAUCV1NXUFMFA1QGAAUBCFJWVlZTCQA%3D&cache_key=754ded5a-c4a2-448c-8904-b131405601c9&depCity=%E5%B9%BF%E5%B7%9E&depCityId=10&dstCity=%E9%A6%99%E6%B8%AF&dstCityId=2015&cabin_type_name=%E7%BB%8F%E6%B5%8E%E8%88%B1&depCityCode4=CCAN&dstCityCode4=HKGA&status=UpdatesPending&_=1458322141648")
				.cookie("SSSessionID=051u08456ec38603f6af0606720518334130; geoCountryCode=CN; D_SID=223.73.119.132:0WgormfvgmTWKyZbMb9EXmApdrN8p7MGThL6tl7oeus; D_PID=7E87B955-84EB-3578-A991-B8948732DC33; D_IID=9DA04AF9-F5D8-3E56-8799-3E5F93EFE1D7; D_UID=A22B48D8-4BE0-3CE5-82C3-48B87BAF7D90; D_HID=NJbZ32/d9jKeUUO6NWme+Gh5ZUTKbbzUdEQga4Xjy8A; fCabin=Economy; fDepDate=2016-03-19; fRtnDate=2016-03-23; fDepCityCode=CCAN; fDstCityCode=HKGA; fRoundType=2; fDirect=0; sessionid=1458321506471s11601; visitorid=1458321506471.10984; _ga=GA1.2.1460885560.1458321507; _jzqa=1.4202611871395260000.1458321507.1458321507.1458321507.1; _jzqb=1.24.10.1458321507.1; _jzqc=1; _jzqx=1.1458321507.1458321507.1.jzqsr=tianxun%2Ecom|jzqct=/captchapage%2Ephp.-; _jzqckmp=1; Hm_lvt_3101d9f8f68af26aad2db3bf9992bd10=1458112897,1458305008,1458305046,1458319353; Hm_lpvt_3101d9f8f68af26aad2db3bf9992bd10=1458322142; __gads=ID=97736b80bb474a6c:T=1458321509:S=ALNI_Mb-oeF3B_o0PycGwgQZRu90VUqZew; ad_collapsed=1; _gat_tianxun=1; _gat_skyscanner=1; _qzja=1.726277769.1458321507212.1458321507212.1458321507212.1458322127054.1458322142260..0.0.14.1; _qzjb=1.1458321507212.14.0.0.0; _qzjc=1; _qzjto=14.1.0")
				.get().printError().printText().done()/**/
				;
	}

	public static void main(String[] args) {
		//		int[] dateInt = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
		//				24, 25, 26, 27, 28, 29, 30, 38, 45, 52, 59, 66, 73, 80, 87, 94, 101, 108, 115, 122, 129, 136, 143, 150,
		//				157, 164, 171, 178, 195, 209, 223, 237, 251, 265, 279, 293, 307, 321, 335, 349, 363 };
		int[] dateInt = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
				24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
				50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 67, 74, 81, 88, 95, 102, 109, 116, 123, 130, 137, 144, 151,
				158, 165, 172, 179 };
		Date date = new Date();
		for (int i = 0; i < dateInt.length; i++) {
			final boolean[] success = new boolean[] { false };
			while (!success[0]) {
				WebgorGrab.create()
						.data(MapHelper.toMap("dep", "SHA", "arr", "XNN", "date",
								new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(date, dateInt[i]))))
						.timeout(30000)/**/
						.connection(
								"http://flights.ctrip.com/booking/${requestData.dep}-${requestData.arr}-day-1.html?DDate1=${requestData.date}")/**/
						.get().printError().done().sleep(1000)/**/
						.processData(new WebgorGrab.Process() {
							public WebgorGrab process(WebgorGrab test) {
								String text = test.getData().getText(test.getName());
								int start = text.indexOf(
										"var url = \"http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights?");
								int end = text.indexOf("\";", start);
								String url = text.substring(start + "var url = \"".length(), end);
								test.connection(url).header(MapHelper.toMap("Referer", url));
								return test;
							}
						}).get().printError().done().sleep(1000)/**/
						.processData(new WebgorGrab.Process() {
							public WebgorGrab process(WebgorGrab test) {
								String text = test.getText();
								if (!(test.getResult() instanceof Map)) {
									return test;
								}
								Map result = (Map) test.getResult();
								if (result == null || result.get("Error") != null) {
									return test;
								}
								String fileName = test
										.evalString("${requestData.dep}_${requestData.arr}_${requestData.date}.json");
								File dir = new File(
										"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ctrip/");
								if (!dir.exists()) {
									dir.mkdirs();
								}
								File file = new File(dir, fileName);
								try {
									FileWriter writer = new FileWriter(file);
									writer.write(text);
									writer.flush();
									SocketHelper.closeWithoutError(writer);
									success[0] = true;
									System.err.println("SUCCESS:" + fileName);
								} catch (Exception e) {
									e.printStackTrace();
								}
								return test;
							}
						});
			}
		}
	}

	public static void main2(String[] args) {
		WebgorGrab.create()//.cookie(MapHelper.toMap("WT-FPT","id=220.206.244.6-3931944752.30501788:lv=1457580076266:ss=1457580076266:fs=1455944992170:pn=1:vn=3"))/**/
				//.connection("http://www.csair.com/cn/index.shtml?WT.mc_id=sem-baidu-BZ-title0229&WT.srch=1")/**/
				//.get().printError().printText().done()/**/
				.data(MapHelper.toMap("dep", "CAN", "arr", "SHA")).timeout(30000)/**/
				.connection(
						"http://flights.ctrip.com/booking/${requestData.dep}-${requestData.arr}-day-1.html?DDate1=2016-03-12")/**/
				//.header(MapHelper.toMap("Referer","http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=HAK&d1=2016-03-31&at=1&ct=0&it=0"))/**/
				//.cookie(MapHelper.toMap("WT-FPT","id=220.206.244.6-3931944752.30501788:lv=1457580439313:ss=1457580076266:fs=1455944992170:pn=3:vn=3"))/**/
				//.cookie(MapHelper.toMap("WT.al_flight","WT.al_hctype(S):WT.al_adultnum(1):WT.al_childnum(0):WT.al_infantnum(0):WT.al_orgcity1(CAN):WT.al_dstcity1(PEK):WT.al_orgdate1(2016-03-31)"))/**/
				.get().printError().printText().done()/**/
				.processData(new WebgorGrab.Process() {
					public WebgorGrab process(WebgorGrab test) {
						System.out.println("Cookie:" + test.getData().getCookie(test.getName()));
						return null;
					}
				}).data(MapHelper.toMap("dep", "CAN", "arr", "SHA")).timeout(30000)/**/
				.connection(
						"http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights?DCity1=${requestData.dep}&ACity1=${requestData.dep}&SearchType=S&DDate1=2016-03-12&IsNearAirportRecommond=0&rk=9.940797567929026092541&CK=DFB6CBE92F88940973601DF8C23A0D5B&r=0.23222865816863296624716")/**/
				.sleep(1000).get().printError().printText().done()/**/
				;
	}

	public static void main1(String[] args) {
		WebgorGrab.create().header(MapHelper.toMap("Referer",
				"http://bzclk.baidu.com/adrc.php?t=0fKL00c00fZmB9D0Fk9i0Am-Qs0UlrKy000000fWt1Y00000VSC4kW.THLD1eXq8ovOdUMu1x60UWdBmy-bIy9EUyNxTAT0T1Y4n1fsm16suH0knAm3PjfL0ZRqwWcvnW0krHDzPHD1PDD4wjPAnHTsfWD3wWRswbPKP1c0mHdL5iuVmv-b5HcvnWDvrj03n16hTZFEuA-b5HDvFhqzpHYkFMPdmhqzpHYhTZFG5Hc0uHdCIZwsrBtEILILQhP1my-zQhPEUiqWUBqGUhw-Xa41pZwVUjqgNa4VmdqGujd1uyYVmh7GuZRVf-CVIA-YUARsnWc4F-IRQMPzmv6qnfKWThnqPHDLn1R&tpl=tpl_10085_12986_1&l=1039344388&wd=%E5%8D%97%E6%96%B9%E8%88%AA%E7%A9%BA%E5%AE%98%E7%BD%91&issp=1&f=3&ie=utf-8&tn=baiduhome_pg&inputT=3901&prefixsug=%E5%8D%97%E6%96%B9&rsp=1"))/**/
				//.cookie(MapHelper.toMap("WT-FPT","id=220.206.244.6-3931944752.30501788:lv=1457580076266:ss=1457580076266:fs=1455944992170:pn=1:vn=3"))/**/
				//.connection("http://www.csair.com/cn/index.shtml?WT.mc_id=sem-baidu-BZ-title0229&WT.srch=1")/**/
				//.get().printError().printText().done()/**/
				.data(MapHelper.toMap("dep", "CAN", "arr", "CTU")).timeout(30000)/**/
				.connection(
						"http://b2c.csair.com/B2C40/query/jaxb/direct/query.ao?json=%7B%22depcity%22%3A%22${requestData.dep}%22%2C%20%22arrcity%22%3A%22${requestData.arr}%22%2C%20%22flightdate%22%3A%2220160331%22%2C%20%22adultnum%22%3A%221%22%2C%20%22childnum%22%3A%220%22%2C%20%22infantnum%22%3A%220%22%2C%20%22cabinorder%22%3A%220%22%2C%20%22airline%22%3A%221%22%2C%20%22flytype%22%3A%220%22%2C%20%22international%22%3A%220%22%2C%20%22action%22%3A%220%22%2C%20%22segtype%22%3A%221%22%2C%20%22cache%22%3A%220%22%2C%20%22preUrl%22%3A%22%22%2C%20%22isMember%22%3A%22%22%7D")/**/
				//.header(MapHelper.toMap("Referer","http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=HAK&d1=2016-03-31&at=1&ct=0&it=0"))/**/
				//.cookie(MapHelper.toMap("WT-FPT","id=220.206.244.6-3931944752.30501788:lv=1457580439313:ss=1457580076266:fs=1455944992170:pn=3:vn=3"))/**/
				//.cookie(MapHelper.toMap("WT.al_flight","WT.al_hctype(S):WT.al_adultnum(1):WT.al_childnum(0):WT.al_infantnum(0):WT.al_orgcity1(CAN):WT.al_dstcity1(PEK):WT.al_orgdate1(2016-03-31)"))/**/
				.get().printError().printText().done()/**/
				.data(MapHelper.toMap("dep", "CAN", "arr", "HAK")).timeout(30000)/**/
				.connection(
						"http://b2c.csair.com/B2C40/query/jaxb/direct/query.ao?json=%7B%22depcity%22%3A%22${requestData.dep}%22%2C%20%22arrcity%22%3A%22${requestData.arr}%22%2C%20%22flightdate%22%3A%2220160331%22%2C%20%22adultnum%22%3A%221%22%2C%20%22childnum%22%3A%220%22%2C%20%22infantnum%22%3A%220%22%2C%20%22cabinorder%22%3A%220%22%2C%20%22airline%22%3A%221%22%2C%20%22flytype%22%3A%220%22%2C%20%22international%22%3A%220%22%2C%20%22action%22%3A%220%22%2C%20%22segtype%22%3A%221%22%2C%20%22cache%22%3A%220%22%2C%20%22preUrl%22%3A%22%22%2C%20%22isMember%22%3A%22%22%7D")/**/
				.sleep(1000).get().printError().printText().done()/**/
				;
	}
}
