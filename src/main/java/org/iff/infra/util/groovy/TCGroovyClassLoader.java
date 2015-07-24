/*******************************************************************************
 * Copyright (c) 2013-9-28 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2013-9-28
 */
public class TCGroovyClassLoader extends TCMainClassLoader {

	protected TCGroovyClassLoader() {
		super();
		this.classManagerName = "default";
	}

	protected TCGroovyClassLoader(String file, String classManagerName) {
		this();
		this.file = file;
		this.classManagerName = classManagerName;
	}

	public static TCGroovyClassLoader get(String file, String classManagerName) {
		return new TCGroovyClassLoader(file, classManagerName);
	}

	public static TCGroovyClassLoader get(String classManagerName) {
		return new TCGroovyClassLoader((String) null, classManagerName);
	}

	//new feature 15-07-15
	protected TCGroovyClassLoader(Map fileStruct, String classManagerName) {
		this();
		this.fileStruct = fileStruct;
		this.classManagerName = classManagerName;
	}

	public static TCGroovyClassLoader get(Map fileStruct, String classManagerName) {
		return new TCGroovyClassLoader(fileStruct, classManagerName);
	}
}
