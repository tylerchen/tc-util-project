/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.openreport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface SummaryMethod {

	BigDecimal summary(List<?> records, String colName);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("sum", new SummaryMethodSum());
				FACTORY.regist("avg", new SummaryMethodAvg());
				FACTORY.regist("max", new SummaryMethodMax());
				FACTORY.regist("min", new SummaryMethodMin());
				FACTORY.regist("count", new SummaryMethodCount());
			}
			return FACTORY;
		}

		public SummaryMethod get(String name) {
			if (registMap != null) {
				return (SummaryMethod) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, SummaryMethod returnType) {
			RegisterHelper.regist(SummaryMethod.class.getSimpleName(),
					MapHelper.toMap("name", name, "value", returnType));
			registMap = RegisterHelper.get(SummaryMethod.class.getSimpleName());
			return this;
		}

		public List<Map> listRegist() {
			List<Map> list = new ArrayList<Map>();
			for (Entry<String, Object> entry : registMap.entrySet()) {
				list.add(MapHelper.toMap("name", entry.getKey(), "value", entry.getValue()));
			}
			return list;
		}

	}

	public static class SummaryMethodSum implements SummaryMethod {
		public BigDecimal summary(List<?> records, String colName) {
			BigDecimal total = BigDecimal.valueOf(0);
			if (records == null || records.isEmpty()) {
				return total;
			}
			for (Object obj : records) {
				if (obj == null) {
					continue;
				} else if (!(obj instanceof Map)) {
					obj = new BeanMap(obj);
				}
				Object tmp = ((Map) obj).get(colName);
				if (tmp == null) {
					tmp = 0;
				}
				if (tmp instanceof String && NumberUtils.isNumber((String) tmp)) {
					tmp = NumberUtils.toDouble((String) tmp);
				}
				if (tmp instanceof Double || tmp instanceof Float) {
					total = total.add(BigDecimal.valueOf(((Number) tmp).doubleValue()));
				} else {
					total = total.add(BigDecimal.valueOf(((Number) tmp).longValue()));
				}
			}
			return total;
		}
	}

	public static class SummaryMethodAvg implements SummaryMethod {
		public BigDecimal summary(List<?> records, String colName) {
			BigDecimal total = BigDecimal.valueOf(0);
			if (records == null || records.isEmpty()) {
				return total;
			}
			for (Object obj : records) {
				if (obj == null) {
					continue;
				} else if (!(obj instanceof Map)) {
					obj = new BeanMap(obj);
				}
				Object tmp = ((Map) obj).get(colName);
				if (tmp == null) {
					tmp = 0;
				}
				if (tmp instanceof String && NumberUtils.isNumber((String) tmp)) {
					tmp = NumberUtils.toDouble((String) tmp);
				}
				if (tmp instanceof Double || tmp instanceof Float) {
					total = total.add(BigDecimal.valueOf(((Number) tmp).doubleValue()));
				} else {
					total = total.add(BigDecimal.valueOf(((Number) tmp).longValue()));
				}
			}
			return total.divide(BigDecimal.valueOf(records.size()), total.scale(), BigDecimal.ROUND_HALF_UP);
		}
	}

	public static class SummaryMethodMax implements SummaryMethod {
		public BigDecimal summary(List<?> records, String colName) {
			Number max = null;
			if (records == null || records.isEmpty()) {
				return null;
			}
			for (Object obj : records) {
				if (obj == null) {
					continue;
				} else if (!(obj instanceof Map)) {
					obj = new BeanMap(obj);
				}
				Object tmp = ((Map) obj).get(colName);
				if (tmp instanceof String && NumberUtils.isNumber((String) tmp)) {
					tmp = NumberUtils.toDouble((String) tmp);
				}
				if (max == null) {
					max = (Number) tmp;
					continue;
				}
				max = Math.max(max.doubleValue(), ((Number) tmp).doubleValue());
			}
			return max == null ? null : BigDecimal.valueOf(max.doubleValue());
		}
	}

	public static class SummaryMethodMin implements SummaryMethod {
		public BigDecimal summary(List<?> records, String colName) {
			Number min = null;
			if (records == null || records.isEmpty()) {
				return null;
			}
			for (Object obj : records) {
				if (obj == null) {
					continue;
				} else if (!(obj instanceof Map)) {
					obj = new BeanMap(obj);
				}
				Object tmp = ((Map) obj).get(colName);
				if (tmp instanceof String && NumberUtils.isNumber((String) tmp)) {
					tmp = NumberUtils.toDouble((String) tmp);
				}
				if (min == null) {
					min = (Number) tmp;
					continue;
				}
				min = Math.min(min.doubleValue(), ((Number) tmp).doubleValue());
			}
			return min == null ? null : BigDecimal.valueOf(min.doubleValue());
		}
	}

	public static class SummaryMethodCount implements SummaryMethod {
		public BigDecimal summary(List<?> records, String colName) {
			if (records == null || records.isEmpty()) {
				return BigDecimal.valueOf(0);
			}
			return BigDecimal.valueOf(records.size());
		}
	}
}
