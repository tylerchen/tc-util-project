/*******************************************************************************
 * Copyright (c) 2013-9-28 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;


/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2013-9-28
 */
public class TCGroovyClassLoader extends TCMainClassLoader {

	protected TCGroovyClassLoader() {
		super();
	}

	protected TCGroovyClassLoader(String file) {
		this();
		this.file = file;
	}

	public static TCGroovyClassLoader get(String file) {
		return new TCGroovyClassLoader(file);
	}

	public static TCGroovyClassLoader get() {
		return new TCGroovyClassLoader();
	}
}
