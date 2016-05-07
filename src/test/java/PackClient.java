
/*******************************************************************************
 * Copyright (c) Jan 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.iff.infra.util.StringHelper;
import org.iff.infra.util.ZipHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 14, 2016
 */
public class PackClient {

	public static void main(String[] args) {
		zipClient();
	}

	public static void zipClient() {
		List<String> zipPaths = new ArrayList<String>();
		{
			for (File f : new File(
					"/Users/zhaochen/dev/workspace/cocoa/test-ocr/firebug_extension/helloworld")
							.listFiles()) {
				zipPaths.add(f.getAbsolutePath());
			}
		}
		ZipHelper.zip(zipPaths.toArray(new String[zipPaths.size()]),
				"/Users/zhaochen/dev/workspace/cocoa/test-ocr/firebug_extension/helloworld.xpi", null);
	}
}
