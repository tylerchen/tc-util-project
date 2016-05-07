import java.net.URLEncoder;

import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.HttpHelper;
import org.iff.infra.util.MapHelper;

/*******************************************************************************
 * Copyright (c) Mar 27, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Mar 27, 2016
 */
public class PostToWebservice {

	public static void main(String[] args) throws Exception {
		HttpHelper.post("http://localhost:8080/webservice/serviceSlaApplication/addServiceSla",
				GsonHelper.toJsonString(MapHelper.toMap("serviceUrl", "test", "accessIp", "localhost",
						"inByte", 111, "outByte", 222, "userInfo", "test")));
	}
}
