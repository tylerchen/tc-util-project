
/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface Matcher {

	/**
	 * 行匹配，只能是一行一行的匹配
	 * @param rows 所有的报文
	 * @param lineNumber 当前行
	 * @param globalVariable 全局变量
	 * @param thisValue 当前值（行或列）
	 * @param matchedValue 匹配的值
	 * @param params XML带过来的参数
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Sep 1, 2016
	 */
	boolean test(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
			MessageParser.MatchedValue thisValue, MessageParser.MatchedValue[] matchedValue,
			Map<String, Object> params);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("col", FACTORY.new CacheProxyMatcher(new ColumnMatcher()));
				FACTORY.regist("row", FACTORY.new CacheProxyMatcher(new RowMatcher()));
				FACTORY.regist("has", FACTORY.new CacheProxyMatcher(new HasValueMatcher()));
			}
			return FACTORY;
		}

		public Matcher get(String name) {
			if (registMap != null) {
				return (Matcher) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, Matcher matcher) {
			RegisterHelper.regist(Matcher.class.getSimpleName(),
					MapHelper.toMap("name", name, "value", FACTORY.new CacheProxyMatcher(matcher)));
			registMap = RegisterHelper.get(Matcher.class.getSimpleName());
			return this;
		}

		public List<Map<String, Object>> listRegist() {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Entry<String, Object> entry : registMap.entrySet()) {
				list.add(MapHelper.toMap("name", entry.getKey(), "value", entry.getValue()));
			}
			return list;
		}

		class CacheProxyMatcher implements Matcher {
			private Matcher matcher;

			public CacheProxyMatcher(Matcher matcher) {
				this.matcher = matcher;
			}

			public boolean test(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
					MessageParser.MatchedValue thisValue, MessageParser.MatchedValue[] matchedValue,
					Map<String, Object> params) {
				boolean result = matcher.test(rows, lineNumber, globalVariable, thisValue, matchedValue, params);
				return result;
			}

		}
	}

	public static class ColumnMatcher implements Matcher {
		public boolean test(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, MessageParser.MatchedValue[] matchedValue,
				Map<String, Object> params) {
			try {
				String range = StringUtils.defaultString((String) params.get("range"));
				String in = StringUtils.defaultString((String) params.get("in"));
				String not = StringUtils.defaultString((String) params.get("not"));
				String pattern = StringUtils.defaultString((String) params.get("pattern"));
				String[] split = StringUtils.split(range, ',');
				int start = 0, end = 0;
				if (split.length == 1 && NumberUtils.isNumber(split[0])) {
					start = end = NumberUtils.toInt(split[0], 0);
				} else if (split.length == 2 && NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1])) {
					start = NumberUtils.toInt(split[0], 0);
					end = NumberUtils.toInt(split[1], 0);
				}
				String column = rows.getColumn(lineNumber, start, end);
				boolean result = true;
				if (range.length() > 0) {
					if (column == null || column.length() < 1) {
						result = false;
					}
				}
				if (result && in.length() > 0) {
					result = new StringBuffer(in.length() * 2).append(',').append(in).append(',').indexOf(column) > 0;
				}
				if (result && not.length() > 0) {
					result = new StringBuffer(in.length() * 2).append(',').append(not).append(',').indexOf(column) < 0;
				}
				if (result && pattern.length() > 0) {
					result = Pattern.compile(pattern).matcher(column).find();
				}
				if (result) {
					matchedValue[0] = MessageParser.MatchedValue.create(lineNumber, rows.getRow(lineNumber));
				}
				return result;
			} catch (Exception e) {
			}
			return false;
		}
	}

	public static class RowMatcher implements Matcher {
		public boolean test(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, MessageParser.MatchedValue[] matchedValue,
				Map<String, Object> params) {
			try {
				String range = StringUtils.defaultString((String) params.get("range"));
				String startStr = StringUtils.defaultString((String) params.get("start"));
				String endStr = StringUtils.defaultString((String) params.get("end"));
				String containStr = StringUtils.defaultString((String) params.get("contain"));
				String pattern = StringUtils.defaultString((String) params.get("pattern"));
				String[] split = StringUtils.split(range, ',');
				int start = 0, end = 0;
				if (split.length == 1 && NumberUtils.isNumber(split[0])) {
					start = end = NumberUtils.toInt(split[0], 0);
				} else if (split.length == 2 && NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1])) {
					start = NumberUtils.toInt(split[0], 0);
					end = NumberUtils.toInt(split[1], 0);
				}
				boolean result = true;
				String row = rows.getRow(lineNumber);
				if (result && range.length() > 0) {
					result = start >= 0 && start >= end && lineNumber >= start && lineNumber <= end;
				}
				if (result && startStr.length() > 0) {
					result = row.startsWith(startStr);
				}
				if (result && endStr.length() > 0) {
					result = row.endsWith(endStr);
				}
				if (result && containStr.length() > 0) {
					if (containStr.indexOf('|') > -1) {
						String[] split2 = StringUtils.split(containStr, '|');
						for (String tmp : split2) {
							result = row.indexOf(tmp) > -1;
							if (result) {
								break;
							}
						}
					} else if (containStr.indexOf('&') > -1) {
						String[] split2 = StringUtils.split(containStr, '&');
						for (String tmp : split2) {
							result = row.indexOf(tmp) > -1;
							if (!result) {
								break;
							}
						}
					} else {
						result = row.indexOf(containStr) > -1;
					}
				}
				if (result && pattern.length() > 0) {
					result = Pattern.compile(pattern).matcher(row).find();
				}
				if (result) {
					matchedValue[0] = MessageParser.MatchedValue.create(lineNumber, row);
				}
				return result;
			} catch (Exception e) {
			}
			return false;
		}
	}

	public static class HasValueMatcher implements Matcher {
		public boolean test(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, MessageParser.MatchedValue[] matchedValue,
				Map<String, Object> params) {
			try {
				String var = StringUtils.defaultString((String) params.get("var"));
				return globalVariable.get(var) != null;
			} catch (Exception e) {
			}
			return false;
		}
	}
}
