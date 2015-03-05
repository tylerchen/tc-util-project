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
		//System.setProperty("tc_jar_path", "META-INF/tc-framework-test");
		System.setProperty("resourceBase", "E:/workspace/JeeGalileo/tc-util-project/src/test/resources/webapp");
		System.setProperty("tc_file_browser_path", "G:/bak/app_root/webapp");
		{
			org.iff.infra.util.groovy.TCCLassManager.main(args);
		}
	}

}
