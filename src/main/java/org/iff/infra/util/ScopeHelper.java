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
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2015-2-15
 */
public class ScopeHelper extends LinkedHashMap {

	private String scopeName;

	private Map<String, Map> parentScope = new LinkedHashMap<String, Map>();

	public static ScopeHelper getInstance(String scopeName) {
		ScopeHelper scope = new ScopeHelper();
		scope.setScopeName(scopeName);
		return scope;
	}

	public ScopeHelper add(String scopeName, Map map) {
		Assert.notBlank(scopeName, "scope name is required!");
		Assert.notNull(map, "scope value is required!");
		parentScope.put(scopeName, map);
		return this;
	}

	public ScopeHelper set(ScopeHelper scopeHelper) {
		Assert.notNull(scopeHelper, "scope helper is required!");
		parentScope.putAll(scopeHelper.parentScope);
		add(scopeHelper.getScopeName(), scopeHelper);
		return this;
	}

	public ScopeHelper remove(String scopeName) {
		parentScope.remove(scopeName);
		return this;
	}

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

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		Assert.notBlank(scopeName, "scope name is required!");
		this.scopeName = scopeName;
	}

}
