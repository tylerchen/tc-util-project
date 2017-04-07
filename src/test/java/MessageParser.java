
/*******************************************************************************
 * Copyright (c) Aug 26, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.iff.infra.util.Assert;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.StringHelper;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;
import groovy.xml.QName;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 26, 2016
 */
public class MessageParser {

	protected MessageParser.RowContent rows;
	protected Node xml = null;
	protected Map<String, Object> globalVariable = new HashMap<String, Object>();
	protected Map structure = new LinkedHashMap();

	public static void main(String[] args) {
		try {
			MessageParser parser = create(
					SocketHelper.getContent(
							new FileInputStream(
									"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/message/NEW.ASM"),
							false),
					SocketHelper.getContent(
							new FileInputStream(
									"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/message/asm-new-01.xml"),
							false));
			parser.parse();
			System.out.println(GsonHelper.toJsonString(parser.getStructure()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main_sim(String[] args) {
		try {
			MessageParser parser = create(
					SocketHelper.getContent(
							new FileInputStream(
									"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/message/W13.ssim"),
							false),
					SocketHelper.getContent(
							new FileInputStream(
									"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/message/sim-01.xml"),
							false));
			parser.parse();
			System.out.println(GsonHelper.toJsonString(parser.getStructure()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected MessageParser() {
	}

	public static MessageParser create(String message, String xml) {
		Assert.notBlank(message);
		Assert.notBlank(xml);
		try {
			MessageParser parse = new MessageParser();
			parse.xml = new XmlParser().parseText(xml);
			parse.rows = new RowContent().parse(message, true);
			/**
			 * msgtpl/parse/match/[col{range|in|not|pattern}|row{range|start|end|contain|pattern}|has{var}]
			 * msgtpl/parse/process/[loop{for|var|index}|split{sp|for}|join{sp|for|range}|add{for1|for2|sp}|replace{for|sp|value}|print]
			 */
			return parse;
		} catch (Exception e) {
			Exceptions.runtime(e.getMessage(), e);
		}
		return null;
	}

	public MessageParser parse() {
		List<Node> parse = xml.getAt(QName.valueOf("parse"));
		boolean isStop = true;
		int lineNumber = 1;
		int totalNumber = rows.getRows().size();
		for (; lineNumber <= totalNumber; lineNumber++) {
			globalVariable.put("_lineNumber", String.valueOf(lineNumber));
			System.out.println("Current line:" + lineNumber + ", Content:" + rows.getRow(lineNumber));
			for (Node parseNode : parse) {
				//System.out.println("Enter <parse>...");
				{/**/
					Object modelObj = parseNode.get("model");
					String model = modelObj == null ? "" : modelObj.toString();
					isStop = model.length() < 1 || "stop".equalsIgnoreCase(model);
					if (isStop) {
						lineNumber = Math.max(1, lineNumber - 1);
					}
				}
				List<Node> match = ((Node) parseNode.getAt(QName.valueOf("match")).get(0)).children();
				List<Node> process = ((Node) parseNode.getAt(QName.valueOf("process")).get(0)).children();
				boolean result = false;
				for (Node matchNode : (List<Node>) match) {
					//System.out.println("Enter <match>...");
					String key = matchNode.name().toString();
					result = Matcher.Factory.me().get(key).test(rows, lineNumber, globalVariable,
							MessageParser.MatchedValue.create(lineNumber, rows.getRow(lineNumber)),
							new MessageParser.MatchedValue[1], matchNode.attributes());
					if (!result) {
						break;
					}
				}
				if (!result) {
					continue;
				}
				for (Node processNode : (List<Node>) process) {
					//System.out.println("Enter <process>...");
					String key = processNode.name().toString();
					Map value = processNode.attributes();
					NodeList set = processNode.getAt(QName.valueOf("set"));
					if (!set.isEmpty()) {
						value.putAll(((Node) set.get(0)).attributes());
					}
					String propertyName = value.get("name") == null ? "" : value.get("name").toString();
					Processor.Factory.me().get(key).process(rows, lineNumber, globalVariable,
							MessageParser.MatchedValue.create(lineNumber, rows.getRow(lineNumber)), structure,
							getFormatName(propertyName, globalVariable), value);
				}
			}
		}
		return this;
	}

	public Map<String, Object> getGlobalVariable() {
		return globalVariable;
	}

	public Map getStructure() {
		return structure;
	}

	public static String getFormatName(String name, Map<String, Object> globalVariable) {
		if (name.indexOf('{') > -1) {
			return StringHelper.replaceBlock(name, globalVariable, null);
		}
		return name;
	}

	public static <T> T getVairable(String variableName, Map<String, Object> globalVariable, Map structure) {
		if (variableName == null) {
			return null;
		}
		if (variableName.startsWith("_")) {
			return (T) globalVariable.get(variableName);
		}
		return (T) MapHelper.getByPath(structure, variableName);
	}

	public static void setVariable(String variableName, Object value, Map<String, Object> globalVariable,
			Map structure) {
		if (variableName == null) {
			return;
		}
		if (variableName.startsWith("_")) {
			globalVariable.put(variableName, value);
		}
		MapHelper.setByPath(structure, variableName, (value instanceof String) ? ((String) value).trim() : value);
	}

	public static int[] getRange(String range) {
		String[] split = StringUtils.split(range, ',');
		int start = 0, end = 0;
		if (split.length == 1 && NumberUtils.isNumber(split[0])) {
			start = end = NumberUtils.toInt(split[0], 0);
		} else if (split.length == 2 && NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1])) {
			start = NumberUtils.toInt(split[0], 0);
			end = NumberUtils.toInt(split[1], 0);
		}
		return new int[] { start, end };
	}

	/**
	 * test the char if is visible.
	 * @param c
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Sep 1, 2016
	 */
	public static boolean isVisible(char c) {
		return c >= 32 && c <= 126;
	}

	/**
	 * remove start and end un-visible char, such as control char.
	 * @param str
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Sep 1, 2016
	 */
	public static String trimToVisible(String str) {
		int start = 0;
		for (; start < str.length(); start++) {
			if (isVisible(str.charAt(start))) {
				break;
			}
		}
		int end = str.length() - 1;
		for (; end > start; end--) {
			if (isVisible(str.charAt(end))) {
				break;
			}
		}
		return str.substring(start, end + 1);
	}

	/**
	 * return and pass the value to next process.
	 * @author zhaochen
	 */
	@SuppressWarnings("serial")
	public static class MatchedValue implements Serializable {
		private int lineNumber = 0;
		private String row = "";

		public static MatchedValue create(int lineNumber, String row) {
			MatchedValue value = new MatchedValue();
			{
				value.row = row;
				value.lineNumber = lineNumber;
			}
			return value;
		}

		public String getRow() {
			return row;
		}

		public int getLineNumber() {
			return lineNumber;
		}
	}

	/**
	 * message content to row content.
	 * @author zhaochen
	 */
	@SuppressWarnings("serial")
	public static class RowContent implements Serializable {
		private List<String> rows = new ArrayList<String>();

		/**
		 * parse content, separate line by 'new line' \n.
		 * @param content
		 * @param trimToVisible if true remove un-visible char of the row start and row end.
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Aug 29, 2016
		 */
		public RowContent parse(String content, boolean trimToVisible) {
			rows.clear();
			String row = "";
			int pos = 0;
			while (pos < content.length()) {
				int newLine = content.indexOf('\n', pos);
				if (newLine < 0) {
					break;
				}
				row = content.substring(pos, newLine);
				rows.add(trimToVisible ? trimToVisible(row) : row);
				pos = newLine + 1;
			}
			if (pos < content.length()) {
				row = content.substring(pos);
				rows.add(trimToVisible ? trimToVisible(row) : row);
			}
			return this;
		}

		/**
		 * start from 1, row number is the text line number.
		 * @param rowNumber start from 1.
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Aug 29, 2016
		 */
		public String getRow(int rowNumber) {
			if (rowNumber > 0 && rowNumber < rows.size() + 1) {
				return rows.get(rowNumber - 1);
			}
			return null;
		}

		/**
		 * start from 1, row number is the text line number.
		 * @param start start from 1.
		 * @param end start from 1.
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Aug 29, 2016
		 */
		public List<String> getRows(int start, int end) {
			List<String> rows = new ArrayList<String>();
			end = Math.min(rows.size(), end);
			if (start > 0 && start <= end) {
				while (start <= end) {
					rows.add(getRow(start));
					start = start + 1;
				}
			}
			return rows;
		}

		/**
		 * get column string.
		 * @param rowNumber start from 1
		 * @param colStart start from 1
		 * @param colEnd start from 1
		 * @return
		 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
		 * @since Aug 29, 2016
		 */
		public String getColumn(int rowNumber, int colStart, int colEnd) {
			String row = "";
			if (rowNumber > 0 && rowNumber < rows.size() + 1) {
				row = rows.get(rowNumber - 1);
				if (colStart >= 1 && colStart <= colEnd && colEnd <= row.length()) {
					colEnd = colEnd + 1;
					return StringUtils.substring(row, colStart - 1, colEnd - 1);
				}
			}
			return null;
		}

		public List<String> getRows() {
			return rows;
		}

	}
}
