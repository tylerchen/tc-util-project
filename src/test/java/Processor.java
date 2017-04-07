
/*******************************************************************************
 * Copyright (c) Jun 28, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RegisterHelper;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public interface Processor {

	void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
			MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params);

	public static class Factory {
		public static final Factory FACTORY = new Factory();
		private Map<String, Object> registMap = null;

		public static Factory me() {
			if (FACTORY.registMap == null) {
				FACTORY.regist("split", FACTORY.new CacheProxyProcessor(new SplitProcessor()));
				FACTORY.regist("join", FACTORY.new CacheProxyProcessor(new JoinProcessor()));
				FACTORY.regist("add", FACTORY.new CacheProxyProcessor(new AddProcessor()));
				FACTORY.regist("replace", FACTORY.new CacheProxyProcessor(new ReplaceProcessor()));
				FACTORY.regist("str", FACTORY.new CacheProxyProcessor(new StringProcessor()));
				FACTORY.regist("print", FACTORY.new CacheProxyProcessor(new PrintProcessor()));
			}
			return FACTORY;
		}

		public Processor get(String name) {
			if (registMap != null) {
				return (Processor) registMap.get(name);
			}
			return null;
		}

		public Factory regist(String name, Processor returnType) {
			RegisterHelper.regist(Processor.class.getSimpleName(),
					MapHelper.toMap("name", name, "value", new CacheProxyProcessor(returnType)));
			registMap = RegisterHelper.get(Processor.class.getSimpleName());
			return this;
		}

		public List<Map<String, Object>> listRegist() {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Entry<String, Object> entry : registMap.entrySet()) {
				list.add(MapHelper.toMap("name", entry.getKey(), "value", entry.getValue()));
			}
			return list;
		}

		class CacheProxyProcessor implements Processor {
			private Processor processor;

			public CacheProxyProcessor(Processor processor) {
				this.processor = processor;
			}

			public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
					MessageParser.MatchedValue thisValue, Map structure, String propertyName,
					Map<String, Object> params) {
				processor.process(rows, lineNumber, globalVariable, thisValue, structure, propertyName, params);
			}
		}
	}

	public static class SplitProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				String sp = StringUtils.defaultString((String) params.get("sp"));
				String var = StringUtils.defaultString((String) params.get("for"));
				String vairable = thisValue.getRow();
				if (var.length() > 0) {
					vairable = (String) globalVariable.get(var);
				}
				if (StringUtils.isNotBlank(vairable)) {
					String[] split = StringUtils.split(vairable, sp);
					MessageParser.setVariable(propertyName, split, globalVariable, structure);
				}
			} catch (Exception e) {
			}
		}
	}

	public static class JoinProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				String sp = StringUtils.defaultString((String) params.get("sp"));
				String var = StringUtils.defaultString((String) params.get("for"));
				String range = StringUtils.defaultString((String) params.get("range"));
				int[] startEnd = MessageParser.getRange(range);
				int start = startEnd[0], end = startEnd[1];
				Object object = MessageParser.getVairable(var, globalVariable, structure);
				if (var.length() > 0 && object != null) {
					String value = "";
					if (object instanceof List) {
						object = ((List) object).toArray();
					}
					if (object.getClass().isArray()) {
						if (start < 1) {
							value = StringUtils.join((Object[]) object, sp);
						} else {
							Object[] arr = (Object[]) object;
							value = StringUtils.join(
									Arrays.copyOfRange(arr, start - 1, Math.max(start, Math.min(end, arr.length))), sp);
						}
					}
					MessageParser.setVariable(propertyName, value, globalVariable, structure);
				}
			} catch (Exception e) {
			}
		}
	}

	public static class AddProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				String var1 = StringUtils.defaultString((String) params.get("for1"));
				String var2 = StringUtils.defaultString((String) params.get("for2"));
				String sp = StringUtils.defaultString((String) params.get("sp"));
				String vairable1 = MessageParser.getVairable(var1, globalVariable, structure);
				String vairable2 = MessageParser.getVairable(var2, globalVariable, structure);
				MessageParser.setVariable(propertyName, StringHelper.concat(vairable1, sp, vairable2), globalVariable,
						structure);
			} catch (Exception e) {
			}
		}
	}

	public static class ReplaceProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				String var = StringUtils.defaultString((String) params.get("for"));
				String sp = StringUtils.defaultString((String) params.get("sp"));
				String value = StringUtils.defaultString((String) params.get("value"));
				String vairable = thisValue.getRow();
				if (var.length() > 0) {
					vairable = MessageParser.getVairable(var, globalVariable, structure);
				}
				if (sp.length() > 0 && vairable != null) {
					MessageParser.setVariable(propertyName, StringUtils.replace(vairable, sp, value), globalVariable,
							structure);
				}
			} catch (Exception e) {
			}
		}
	}

	public static class StringProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				String var = StringUtils.defaultString((String) params.get("for"));
				String range = StringUtils.defaultString((String) params.get("range"));
				int[] startEnd = MessageParser.getRange(range);
				int start = startEnd[0], end = startEnd[1];
				String vairable = thisValue.getRow();
				if (var.length() > 0) {
					vairable = MessageParser.getVairable(var, globalVariable, structure);
				}
				if (vairable != null) {
					if (start > 0 && end >= start) {
						MessageParser.setVariable(propertyName, rows.getColumn(lineNumber, start, end), globalVariable,
								structure);
					} else {
						MessageParser.setVariable(propertyName, rows.getRow(lineNumber), globalVariable, structure);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static class PrintProcessor implements Processor {
		public void process(MessageParser.RowContent rows, int lineNumber, Map<String, Object> globalVariable,
				MessageParser.MatchedValue thisValue, Map structure, String propertyName, Map<String, Object> params) {
			try {
				System.out.println("LineNumber:" + lineNumber);
				System.out.println("ThisValue:" + GsonHelper.toJsonString(thisValue));
				System.out.println("Structure:" + GsonHelper.toJsonString(structure));
			} catch (Exception e) {
			}
		}
	}
}
