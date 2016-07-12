import java.util.HashMap;
import java.util.List;

import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.groovy2.TCGroovyLoader;
import org.iff.infra.util.groovy2.TCGroovyLoader.TCActionInvoker;

/*******************************************************************************
 * Copyright (c) May 11, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since May 11, 2016
 */
public class TCGroovyLoaderTest {

	public static void main(String[] args) {
		List<String> list = ResourceHelper.loadResourcesInClassPath("modules/newmodule", "tcmodule.xml", "tcmodule.xml", null);
		System.out.println(list);
		TCGroovyLoader loader = TCGroovyLoader.create("default", new String[] { "classpath://modules/newmodule" });
		loader.load();
		TCActionInvoker action = loader.getAction("/openreport", new HashMap());
		System.out.println(action.invoke(new Object[0]));
	}
}
