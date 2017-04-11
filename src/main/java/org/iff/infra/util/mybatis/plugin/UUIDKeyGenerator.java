/*******************************************************************************
 * Copyright (c) Dec 20, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.plugin;

import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2013-2-2
 */
public class UUIDKeyGenerator implements KeyGenerator {
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		processGeneratedKeys(executor, ms, parameter);
	}

	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
	}

	private void processGeneratedKeys(Executor executor, MappedStatement ms, Object parameter) {
		if (ms.getSqlCommandType() != SqlCommandType.INSERT) {
			return;
		}
		String[] keyProperties = ms.getKeyProperties();
		if (parameter != null && keyProperties != null && keyProperties.length > 0) {
			try {
				Configuration configuration = ms.getConfiguration();
				MetaObject metaParam = configuration.newMetaObject(parameter);
				String keyProperty = keyProperties[0];
				if (metaParam.getGetterType(keyProperty) == String.class && metaParam.hasGetter(keyProperty)
						&& metaParam.hasSetter(keyProperty)
						&& StringUtils.isBlank((String) metaParam.getValue(keyProperty))) {
					metaParam.setValue(keyProperty, StringHelper.uuid());
				}
			} catch (Exception e) {
				throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e,
						e);
			}
		}
	}

}