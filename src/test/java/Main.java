import groovy.lang.GroovyObject;

import org.iff.infra.util.groovy.TCCLassManager;

/*******************************************************************************
 * Copyright (c) 2014-7-3 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2014-7-3
 */

public class Main {

	public static void main(String[] args) throws Exception {
		//for test
		System.setProperty("app_root", "E:/workspace/JeeGalileo/tc-util-project/src/main/resources/META-INF/tc-framework");
		System.setProperty("resourceBase", "G:/bak/app_root/webapp");
		{
			TCCLassManager.me().compile(System.getProperty("app_root") + "/system/framework/TC.groovy");
			Class clazz = TCCLassManager.me().get().loadClass("org.iff.groovy.framework.Starter");
			GroovyObject groovyObject = (GroovyObject) clazz.newInstance();
			groovyObject.invokeMethod("start", new Object[0]);
		}
	}

}
