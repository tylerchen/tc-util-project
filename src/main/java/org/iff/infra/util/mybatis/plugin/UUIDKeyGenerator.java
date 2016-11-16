package org.iff.infra.util.mybatis.plugin;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.iff.infra.util.StringHelper;

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
						&& metaParam.hasSetter(keyProperty) && metaParam.getValue(keyProperty) == null) {
					metaParam.setValue(keyProperty, StringHelper.uuid());
				}
			} catch (Exception e) {
				throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e,
						e);
			}
		}
	}

}