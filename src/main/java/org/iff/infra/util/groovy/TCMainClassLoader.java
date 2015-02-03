/*******************************************************************************
 * Copyright (c) 2013-9-28 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.groovy;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.iff.infra.util.SocketHelper;

/**
 * @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> 
 * @since 2013-9-28
 */
public class TCMainClassLoader extends GroovyClassLoader {

	protected String file;
	protected long lastModify = 0;

	public TCMainClassLoader() {
		super(Thread.currentThread().getContextClassLoader(), getCompilerConfiguration());
	}

	public synchronized TCMainClassLoader recompile() {
		if (file != null) {
			if (file.startsWith("jar:file:")) {
				try {
					if (lastModify == 0) {
						lastModify = -1;
						super.recompile(new URL(file), null, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					File groovyFile = new File(file);
					if (lastModify < groovyFile.lastModified()) {
						lastModify = groovyFile.lastModified();
						System.out.println("------TCMainClassLoader.recompile-------->" + file);
						super.recompile(groovyFile.getCanonicalFile().toURI().toURL(), null, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	public String getFile() {
		return file;
	}

	public static CompilerConfiguration getCompilerConfiguration() {
		CompilerConfiguration config = new CompilerConfiguration();
		{
			config.setSourceEncoding("UTF-8");
			config.setRecompileGroovySource(true);
		}
		{
			ImportCustomizer customizer = new ImportCustomizer();
			customizer.addStarImports("org.iff.infra.util.groovy", "org.iff.infra.util", "org.iff.groovy.util",
					"org.iff.groovy.framework");
			config.addCompilationCustomizers(customizer);
		}
		return config;
	}

	public Class loadClass_0(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve)
			throws ClassNotFoundException, CompilationFailedException {
		return super.loadClass(name, lookupScriptFiles, preferClassOverScript, resolve);
	}

	public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve)
			throws ClassNotFoundException, CompilationFailedException {
		Class clazz = null;
		TCMainClassLoader[] loader = TCCLassManager.me().getClassMapLoader().get(name);
		if (loader != null) {
			return loader[0].loadClass_0(name, lookupScriptFiles, preferClassOverScript, resolve);
		}
		return super.loadClass(name, lookupScriptFiles, preferClassOverScript, resolve);
	}

	protected Class<?> findClass1(String name) throws ClassNotFoundException {
		//return super.findClass(name);
		Class clazz = null;
		TCMainClassLoader[] loader = TCCLassManager.me().getClassMapLoader().get(name);
		if (loader != null) {
			loader[0].loadClass(name);
		}
		return super.findClass(name);
	}
}
