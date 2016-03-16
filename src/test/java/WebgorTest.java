import java.io.InputStream;
import java.io.OutputStream;

import org.iff.infra.util.MapHelper;
import org.iff.infra.util.WebgorGrab;

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

	public static void main(String[] args) {
		WebgorGrab.create().data(MapHelper.toMap("dep", "CAN", "arr", "BJS")).timeout(30000)/**/
				.connection(
						"http://flights.ctrip.com/booking/${requestData.dep}-${requestData.arr}-day-1.html?DDate1=2016-03-16")/**/
				.get().printError().printText().done()/**/
				.processData(new WebgorGrab.Process() {
					public WebgorGrab process(WebgorGrab test) {
						String text = test.getData().getText(test.getName());
						int start = text.indexOf(
								"var url = \"http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights?");
						int end = text.indexOf("\";", start);
						String url = text.substring(start + "var url = \"".length(), end);
						test.connection(url);
						return test;
					}
				}).get().printError().printText().done()/**/
				;
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
