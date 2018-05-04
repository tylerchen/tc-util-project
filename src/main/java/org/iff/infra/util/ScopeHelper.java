/*******************************************************************************
 * Copyright (c) 2015-2-15 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * scope-able value helper. such as seam scope or java variable scope, it will find the value from latest scope, 
 * if not found then search the parent scope.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-15
 */
public class ScopeHelper extends LinkedHashMap {

	private String scopeName;

	private Map<String, Map> parentScope = new LinkedHashMap<String, Map>();

	/**
	 * return the scope helper instance.
	 * @param scopeName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public static ScopeHelper getInstance(String scopeName) {
		ScopeHelper scope = new ScopeHelper();
		scope.setScopeName(scopeName);
		return scope;
	}

	/**
	 * add a scope as parent scope.
	 * @param scopeName
	 * @param map
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ScopeHelper add(String scopeName, Map map) {
		Assert.notBlank(scopeName, "scope name is required!");
		Assert.notNull(map, "scope value is required!");
		parentScope.put(scopeName, map);
		return this;
	}

	/**
	 * set parent scope.
	 * @param scopeHelper
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ScopeHelper set(ScopeHelper scopeHelper) {
		Assert.notNull(scopeHelper, "scope helper is required!");
		parentScope.putAll(scopeHelper.parentScope);
		add(scopeHelper.getScopeName(), scopeHelper);
		return this;
	}

	/**
	 * remove scope by name.
	 * @param scopeName
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public ScopeHelper remove(String scopeName) {
		parentScope.remove(scopeName);
		return this;
	}

	/**
	 * get scope by name.
	 * @param key
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public Object getScope(Object key) {
		if (super.containsKey(key)) {
			return super.get(key);
		}
		Object[] array = parentScope.entrySet().toArray();
		for (int i = array.length - 1; i > -1; i--) {
			java.util.Map.Entry<String, Map> entry = (java.util.Map.Entry<String, Map>) array[i];
			if (entry.getValue().containsKey(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * return the current scope name.
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public String getScopeName() {
		return scopeName;
	}

	/**
	 * set the scope name.
	 * @param scopeName
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 19, 2016
	 */
	public void setScopeName(String scopeName) {
		Assert.notBlank(scopeName, "scope name is required!");
		this.scopeName = scopeName;
	}

}
