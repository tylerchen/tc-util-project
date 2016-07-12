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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.StringHelper;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 28, 2016
 */
public class CrossTable {

	public static void main(String[] args) {
		Map openreportConfig = new HashMap();
		Map conditionParams = new HashMap();
		Map reportConfig = MapHelper.toMap(/**/
				"crosstable", MapHelper.toMap(/**/
						"rows", MapHelper.toMap("row@gender", new HashMap(), "row@age", new HashMap())/**/
						, "columns", MapHelper.toMap("column@month", new HashMap(), "column@goods", new HashMap())/**/
						, "summary@test",
						MapHelper.toMap("display-name", "汇总"/**/
								, "name", "Summary"/**/
								, "value@price",
								MapHelper.toMap("name", "price", "display-name", "平均价格", "summary-method", "avg")/**/
								, "value@amount",
								MapHelper.toMap("name", "amount", "display-name", "总数", "summary-method", "sum")/**/
		)/*summary@test*/
		)/*crosstable*/
		);
		Map queryResult = MapHelper.toMap("result",
				Arrays.asList(/**/
						MapHelper.toMap("gender", "fmale", "age", 18, "month", "JAN", "goods", "papers", "amount", 10,
								"price", 10), /**/
				MapHelper.toMap("gender", "fmale", "age", 18, "month", "JAN", "goods", "printer", "amount", 11, "price",
						11), /**/
				MapHelper.toMap("gender", "fmale", "age", 18, "month", "FEB", "goods", "papers", "amount", 12, "price",
						12), /**/
				MapHelper.toMap("gender", "fmale", "age", 18, "month", "FEB", "goods", "printer", "amount", 13, "price",
						13), /**/
				MapHelper.toMap("gender", "fmale", "age", 28, "month", "JAN", "goods", "papers", "amount", 14, "price",
						14), /**/
				MapHelper.toMap("gender", "fmale", "age", 28, "month", "JAN", "goods", "printer", "amount", 15, "price",
						15), /**/
				MapHelper.toMap("gender", "fmale", "age", 28, "month", "FEB", "goods", "papers", "amount", 16, "price",
						16), /**/
				MapHelper.toMap("gender", "fmale", "age", 28, "month", "FEB", "goods", "printer", "amount", 17, "price",
						17), /**/
				MapHelper.toMap("gender", "male", "age", 18, "month", "JAN", "goods", "papers", "amount", 18, "price",
						18), /**/
				MapHelper.toMap("gender", "male", "age", 18, "month", "JAN", "goods", "printer", "amount", 19, "price",
						19), /**/
				MapHelper.toMap("gender", "male", "age", 18, "month", "FEB", "goods", "papers", "amount", 20, "price",
						20), /**/
				MapHelper.toMap("gender", "male", "age", 18, "month", "FEB", "goods", "printer", "amount", 30, "price",
						30), /**/
				MapHelper.toMap("gender", "male", "age", 28, "month", "JAN", "goods", "papers", "amount", 40, "price",
						40), /**/
				MapHelper.toMap("gender", "male", "age", 28, "month", "JAN", "goods", "printer", "amount", 50, "price",
						50), /**/
				MapHelper.toMap("gender", "male", "age", 28, "month", "FEB", "goods", "papers", "amount", 60, "price",
						60), /**/
				MapHelper.toMap("gender", "male", "age", 28, "month", "FEB", "goods", "printer", "amount", 70, "price",
						70)/**/
		));
		System.out.println(new CrossTable().generate(openreportConfig, reportConfig, conditionParams, queryResult));
	}

	/** -FOR TEST- 
	 * def openreportConfig=[:]
	 * def reportConfig=[:]
	 * def conditionParams=[:]
	 * def queryResult=['result':[]]
	 * reportConfig.'crosstable'=[:]
	 * reportConfig.'crosstable'.'rows'=['row@gender':[:],'row@age':[:]]
	 * reportConfig.'crosstable'.'columns'=['column@month':[:]]
	 * reportConfig.'crosstable'.'summary@test'=['display-name':'Summary','name':'Summary','value@price':['name':'price','display-name':'priceAvg','summary-method':'avg'],
	 *     'value@amount':['name':'amount','display-name':'amountSum','summary-method':'sum']]
	 * queryResult.'result' << ['gender':'fmale', 'age':18, 'month': 'JAN', 'goods':'papers' , 'amount': 10, 'price': 10]
	 * queryResult.'result' << ['gender':'fmale', 'age':18, 'month': 'JAN', 'goods':'printer', 'amount': 11, 'price': 11]
	 * queryResult.'result' << ['gender':'fmale', 'age':18, 'month': 'FEB', 'goods':'papers' , 'amount': 12, 'price': 12]
	 * queryResult.'result' << ['gender':'fmale', 'age':18, 'month': 'FEB', 'goods':'printer', 'amount': 13, 'price': 13]
	 * queryResult.'result' << ['gender':'fmale', 'age':28, 'month': 'JAN', 'goods':'papers' , 'amount': 14, 'price': 14]
	 * queryResult.'result' << ['gender':'fmale', 'age':28, 'month': 'JAN', 'goods':'printer', 'amount': 15, 'price': 15]
	 * queryResult.'result' << ['gender':'fmale', 'age':28, 'month': 'FEB', 'goods':'papers' , 'amount': 16, 'price': 16]
	 * queryResult.'result' << ['gender':'fmale', 'age':28, 'month': 'FEB', 'goods':'printer', 'amount': 17, 'price': 17]
	 * queryResult.'result' << ['gender':'male' , 'age':18, 'month': 'JAN', 'goods':'papers' , 'amount': 18, 'price': 18]
	 * queryResult.'result' << ['gender':'male' , 'age':18, 'month': 'JAN', 'goods':'printer', 'amount': 19, 'price': 19]
	 * queryResult.'result' << ['gender':'male' , 'age':18, 'month': 'FEB', 'goods':'papers' , 'amount': 20, 'price': 20]
	 * queryResult.'result' << ['gender':'male' , 'age':18, 'month': 'FEB', 'goods':'printer', 'amount': 30, 'price': 30]
	 * queryResult.'result' << ['gender':'male' , 'age':28, 'month': 'JAN', 'goods':'papers' , 'amount': 40, 'price': 40]
	 * queryResult.'result' << ['gender':'male' , 'age':28, 'month': 'JAN', 'goods':'printer', 'amount': 50, 'price': 50]
	 * queryResult.'result' << ['gender':'male' , 'age':28, 'month': 'FEB', 'goods':'papers' , 'amount': 60, 'price': 60]
	 * queryResult.'result' << ['gender':'male' , 'age':28, 'month': 'FEB', 'goods':'printer', 'amount': 70, 'price': 70]
	 * def avg(records, colName){
	 *     def total=0 as BigDecimal
	 *     records.each{rc->
	 *         def bd=((rc in Map) ? rc[colName] : rc) as BigDecimal
	 *         total=total+bd
	 *     }
	 *     if(records && records.size()>0){
	 *         return  total.divide(records.size() as BigDecimal, total.scale(), BigDecimal.ROUND_HALF_UP)
	 *     }
	 *     null
	 * }
	 * def max(records, colName){
	 *     def max
	 *     records.each{rc->
	 *         def bd=((rc in Map) ? rc[colName] : rc) as BigDecimal
	 *         max=(max ?: bd).max(bd)
	 *     }
	 *     max
	 * }
	 **/
	/**
	 * 打印的时候从上到下，从左到右打印
	 * +--------------+------------------+-------------------+
	 * |  空白  | 空白  |       列头        |                   |
	 * +--------------+------------------+      列汇总        +
	 * |  空白  | 空白  | 列头   |   列头   |                   |
	 * +--------------+------------------+-------------------+
	 * |       | 行头  |   11   |    12   |        23         |
	 * +  行头  |-------+-----------------+-------------------+
	 * |       | 行头  |   21   |    22   |        43         |
	 * +--------------+------------------+-------------------+
	 * | 行汇总 |   32 |   32   |    34   |        66         |
	 * +--------------+------------------+-------------------+
	 * 
	 * +--------------+------------------+-------------------+
	 * |       |      |       goods      |                   |
	 * +--------------+------------------+ columnSummaryName +
	 * |       |      | papers | printer |                   |
	 * +--------------+------------------+-------------------+
	 * |       | male |   11   |    12   |        23         |
	 * + gender|-------+-----------------+-------------------+
	 * |       | fmale|   21   |    22   |        43         |
	 * +--------------+------------------+-------------------+
	 * |rowSummaryName|   32   |    34   |        66         |
	 * +--------------+------------------+-------------------+
	 **/

	/**
	 * rowmap['gender&&&&@@@@&&&&male']=colmap={'goods&&&&@@@@&&&&papers':11}
	 * rowmap['gender&&&&@@@@&&&&male']=colmap={'goods&&&&@@@@&&&&printer':12}
	 * rowmap['gender&&&&@@@@&&&&fmale']=colmap={'goods&&&&@@@@&&&&papers':21}
	 * rowmap['gender&&&&@@@@&&&&fmale']=colmap={'goods&&&&@@@@&&&&printer'22}
	 * rowsummary=loop colmap keys, summary rowmap[rowkey][columnkey]=colmap.key
	 * columnsummary=loop rowmap keys, summary colmap values
	 * 就是按行和列进行分组，相同分组的数据就按指定的统计方式（函数）进行统计指定的值，有多少个统计方式就有多少个列。
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String generate(Map openreportConfig, Map reportConfig, Map conditionParams, Map queryResult) {
		String separator_str = "∫";
		List<String> tds = new ArrayList<String>()/*table contents*/;
		/*dimension x，其实就是行，从summary描述中找出【行】分组。*/
		List<String> row = new ArrayList<String>();
		/*dimension y，其实就是列，从summary描述中找出【列】分组。*/
		List<String> col = new ArrayList<String>()/*dimension y*/;
		/*value for calculate，这个描述了对哪个列的数据进行统计（统计函数）*/
		Map<String, Map> summary = new LinkedHashMap<String, Map>();
		/* summary key names，就是需要统计的列的名称，0=列的名称，1=统计方法 */
		List<String[]> summaryKeys = new ArrayList<String[]>();
		/* init all data */
		initData(reportConfig, row, col, summary, summaryKeys);
		/*
		 * cross table like group function. group the same value by row and column.
		 * this solution is using {group-row-value : group-column-value} map structure.
		 */
		/*{row1-row2-row*: {col1-col2col*: data}}*/
		Map<String, Map<String, List>> rowmap = new LinkedHashMap<String, Map<String, List>>();
		/* {col1-col2col*: data}} */
		Map<String, String> colmap = new LinkedHashMap<String, String>();
		List<Map> records = (List<Map>) queryResult.get("result");
		{
			if (records == null || records.isEmpty()) {
				tds.add("<table class='or-data'>");
				tds.add("<tr><td class='or_norecord'><div class='or_norecord'>no record found!</div></td></tr>");
				tds.add("</table>");
				return StringUtils.join(tds, '\n');
			}
		}
		{/*prepare row and column datas*/
			prepareRowAndColumnDatas(separator_str, row, col, rowmap, colmap, records);
		}
		ArrayList<String> columnGroupKeys = new ArrayList<String>(colmap.keySet());
		Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
		{
			prepareHeaders(separator_str, summaryKeys, columnGroupKeys, headerMap);
		}
		{
			tds.add("<table class='or-data'>");
		}
		{
			/*
			* +--------------+------------------+-------------------+
			* |  空白  | 空白  |       列头        |                   |    --->打印表头
			* +--------------+------------------+      列汇总        +
			* |  空白  | 空白  | 列头   |   列头   |                   |     --->打印表头
			* +--------------+------------------+-------------------+
			* |       | 行头  |   11   |    12   |        23         |
			* +  行头  |-------+-----------------+-------------------+
			* |       | 行头  |   21   |    22   |        43         |
			* +--------------+------------------+-------------------+
			* | 行汇总 |   32 |   32   |    34   |        66         |
			* +--------------+------------------+-------------------+
			*/
			printTableHeader(tds, row, col, summary, headerMap);
		}
		List<List<Map<String, BigDecimal>>> rowSummary = new ArrayList<List<Map<String, BigDecimal>>>();
		{/*print content and row summary*/
			Map hasMap = new LinkedHashMap();
			{/*init row summary*/
				int count = headerMap.get("0").size() + summaryKeys.size();
				for (int i = 0; i < count; i++) {
					rowSummary.add(new ArrayList());
				}
			}
			{
				tds.add("<tbody>");
			}
			/*{row1-row2-row*: {col1-col2col*: data}}*/
			int rowIndex = -1;
			/*用来区别是否为新行*/
			String rowKeyTemp = null;
			/*按行来打印，每个单元格就是对应summary中需要统计的列名，以及统计的方法*/
			for (Entry<String, Map<String, List>> entry : rowmap.entrySet()) {
				rowIndex = rowIndex + 1;
				String rowKey = entry.getKey();
				Map<String, List> rowValue = entry.getValue();
				{
					tds.add("<tr>");
				}
				List<List<Map<String, BigDecimal>>> columnOfRowSummary = new ArrayList<List<Map<String, BigDecimal>>>();
				{/*init col summary*/
					int count = summaryKeys.size();
					for (int i = 0; i < count; i++) {
						columnOfRowSummary.add(new ArrayList());
					}
				}
				{/*打印一行（不包括行统计）*/
					/*
					* +--------------+------------------+-------------------+
					* |  空白  | 空白  |       列头        |                   |
					* +--------------+------------------+      列汇总        +
					* |  空白  | 空白  | 列头   |   列头   |                   | 
					* +--------------+------------------+-------------------+
					* |       | 行头  |   11   |    12   |        23         |    --->打印一行，先打印行头，再打印数据（不包括行统计“列汇总”）
					* +  行头  |-------+-----------------+-------------------+
					* |       | 行头  |   21   |    22   |        43         |    --->打印一行，先打印行头，再打印数据（不包括行统计“列汇总”）
					* +--------------+------------------+-------------------+
					* | 行汇总 |   32 |   32   |    34   |        66         |
					* +--------------+------------------+-------------------+
					*/
					rowKeyTemp = printRow(separator_str, tds, summary, summaryKeys, columnGroupKeys, rowSummary, hasMap,
							rowKeyTemp, rowKey, rowValue, columnOfRowSummary);
				}
				GsonHelper.toJsonString(columnOfRowSummary);
				{/*打印行统计*/
					/*
					* +--------------+------------------+-------------------+
					* |  空白  | 空白  |       列头        |                   |
					* +--------------+------------------+      列汇总        +
					* |  空白  | 空白  | 列头   |   列头   |                   | 
					* +--------------+------------------+-------------------+
					* |       | 行头  |   11   |    12   |        23         |    --->打印“列汇总”
					* +  行头  |-------+-----------------+-------------------+
					* |       | 行头  |   21   |    22   |        43         |    --->打印“列汇总”
					* +--------------+------------------+-------------------+
					* | 行汇总 |   32 |   32   |    34   |        66         |
					* +--------------+------------------+-------------------+
					*/
					printRowSummary(tds, summaryKeys, columnGroupKeys, rowSummary, columnOfRowSummary);
				}
				tds.add("</tr>");
			} /*END-rowmap*/
			{
				tds.add("</tbody>");
			}
		}
		{
			tds.add("<tfoot>");
		}
		{
			/*
			* +--------------+------------------+-------------------+
			* |  空白  | 空白  |       列头        |                   |
			* +--------------+------------------+      列汇总        +
			* |  空白  | 空白  | 列头   |   列头   |                   | 
			* +--------------+------------------+-------------------+
			* |       | 行头  |   11   |    12   |        23         |
			* +  行头  |-------+-----------------+-------------------+
			* |       | 行头  |   21   |    22   |        43         |
			* +--------------+------------------+-------------------+
			* | 行汇总 |   32 |   32   |    34   |        66         |    --->打印“行汇总”
			* +--------------+------------------+-------------------+
			*/
			printColumnSummary(reportConfig, tds, row, summaryKeys, rowSummary);
		}
		{
			tds.add("</tfoot>");
		}
		return StringUtils.join(tds, '\n');
	}

	private void printColumnSummary(Map reportConfig, List<String> tds, List<String> row, List<String[]> summaryKeys,
			List<List<Map<String, BigDecimal>>> rowSummary) {
		{/* column summary */
			String displayName = "";
			{
				Map<String, Object> map = (Map) reportConfig.get("crosstable");
				for (Entry<String, Object> entry : map.entrySet()) {
					if (entry.getKey().startsWith("summary@")) {
						displayName = (String) ((Map) entry.getValue()).get("display-name");
					}
				}
			}
			tds.add(StringHelper.concat("<tr class='ord-row-summary'><td colspan='", String.valueOf(row.size()),
					"' class='ord-row-summary-name'>", displayName, "</td>"));
			for (int rowSummaryIndex = 0; rowSummaryIndex < rowSummary.size(); rowSummaryIndex++) {
				List<Map<String, BigDecimal>> rowRecord = rowSummary.get(rowSummaryIndex);
				BigDecimal summaryData = SummaryMethod.Factory.me()
						.get(summaryKeys.get(rowSummaryIndex % summaryKeys.size())[1]).summary(rowRecord, "name");
				tds.add(StringHelper.concat("<td>", summaryData.toString(), "</td>"));
			}
		}
	}

	private void printRowSummary(List<String> tds, List<String[]> summaryKeys, ArrayList<String> columnGroupKeys,
			List<List<Map<String, BigDecimal>>> rowSummary, List<List<Map<String, BigDecimal>>> columnOfRowSummary) {
		/* append col summary value */
		for (int colSummaryIndex = 0; colSummaryIndex < columnOfRowSummary.size(); colSummaryIndex++) {
			BigDecimal summaryData = SummaryMethod.Factory.me().get((String) summaryKeys.get(colSummaryIndex)[1])
					.summary(columnOfRowSummary.get(colSummaryIndex), "name");
			if (summaryData != null) {
				/* append value to row summary */
				Map map = MapHelper.toMap("name", summaryData);
				rowSummary.get(columnGroupKeys.size() * summaryKeys.size() + colSummaryIndex).add(map);
			}
			tds.add(StringHelper.concat("<td class='ord-col-summary ord-col-summary-", String.valueOf(colSummaryIndex),
					"'>", summaryData == null ? "" : summaryData.toString(), "</td>"));
		} /* END-colSummary */
	}

	private String printRow(String separator_str, List<String> tds, Map<String, Map> summary,
			List<String[]> summaryKeys, ArrayList<String> columnGroupKeys,
			List<List<Map<String, BigDecimal>>> rowSummary, Map hasMap, String rowKeyTemp, String rowKey,
			Map<String, List> rowValue, List<List<Map<String, BigDecimal>>> columnOfRowSummary) {
		/*把列打印完毕*/
		for (int columnIndex = 0; columnIndex < columnGroupKeys.size(); columnIndex++) {
			if (rowKey != rowKeyTemp) {/*rowKey != rowKeyTemp，表示行不同了，换行了就得打印行头*/
				/*
				* +--------------+------------------+-------------------+
				* |  空白  | 空白  |       列头        |                   |
				* +--------------+------------------+      列汇总        +
				* |  空白  | 空白  | 列头   |   列头   |                   |  
				* +--------------+------------------+-------------------+
				* |       | 行头  |   11   |    12   |        23         |    --->打印行头
				* +  行头  |-------+-----------------+-------------------+
				* |       | 行头  |   21   |    22   |        43         |    --->打印行头
				* +--------------+------------------+-------------------+
				* | 行汇总 |   32 |   32   |    34   |        66         |
				* +--------------+------------------+-------------------+
				 */
				printRowHeader(separator_str, tds, hasMap, rowKey);
			}
			/* summary the row value, summaryName is the column name to summary the value */
			int summaryIndex = -1;
			for (Entry<String, Map> summaryEntry : summary.entrySet()) {
				summaryIndex = summaryIndex + 1;
				String summaryName = summaryEntry.getKey();
				Map summaryValue = summaryEntry.getValue();
				BigDecimal summaryData = SummaryMethod.Factory.me().get((String) summaryValue.get("summary-method"))
						.summary(rowValue.get(columnGroupKeys.get(columnIndex)), summaryName);
				if (summaryData != null) {
					Map map = MapHelper.toMap("name", summaryData);
					/* append value to row summary */
					rowSummary.get(columnIndex * summaryKeys.size() + summaryIndex).add(map);
					/* append value to col summary */
					columnOfRowSummary.get(summaryIndex).add(map);
				}
				tds.add(StringHelper.concat("<td>", StringUtils.defaultIfBlank(summaryData.toString(), ""), "</td>"));
			}
			rowKeyTemp = rowKey;
		} /*END-columnGroupKeys*/
		return rowKeyTemp;
	}

	private void prepareRowAndColumnDatas(String separator_str, List<String> row, List<String> col,
			Map<String, Map<String, List>> rowmap, Map<String, String> colmap, List<Map> records) {
		{/*prepare row and column datas*/
			for (Map record : records) {
				/* find the row value from the records, the row name specified from the report define. */
				List<String> rowValueArray = new ArrayList<String>(256);
				for (String rowName : row) {
					rowValueArray.add(record.get(rowName).toString());
				}
				/*group the row value. row key join as row key for group row-values and sort*/
				String rowGroupKey = StringUtils.join(rowValueArray, separator_str);
				/*{joined-row-key : {joined-column-key:[record-row-datas]}}*/
				if (rowmap.get(rowGroupKey) == null) {
					rowmap.put(rowGroupKey, new LinkedHashMap<String, List>());
				}
				Map<String, List> columnGroupMap = rowmap.get(rowGroupKey);

				List<String> columnValueArray = new ArrayList<String>(256);
				for (String colName : col) {
					columnValueArray.add(record.get(colName).toString());
				}
				/*group the column value. column key join as column key for group column-values and sort*/
				String columnGroupKey = StringUtils.join(columnValueArray, separator_str);
				colmap.put(columnGroupKey, "");

				if (columnGroupMap.get(columnGroupKey) == null) {
					columnGroupMap.put(columnGroupKey, new ArrayList());
				}
				columnGroupMap.get(columnGroupKey).add(record);
			} /* when this loop is end, all the records has grouped. the rest code is how to print the cross table. */
		}
	}

	private void printRowHeader(String separator_str, List<String> tds, Map hasMap, String rowKey) {
		Map hmtmp = hasMap;
		String[] split = StringUtils.split(rowKey, separator_str);
		for (String rowKeySplit : split) {/* 还没开始打开数值，这里是打印行头 */
			Map map = (Map) hmtmp.get(rowKeySplit);
			if (map == null) {
				/* see above like 'gender', first set rowspan=1, then will replace this value */
				tds.add(StringHelper.concat("<td rowspan='1' class='ord-row-head'>", rowKeySplit, "</td>"));
				{
					map = new LinkedHashMap<String, Integer>();
					hmtmp.put(rowKeySplit, map);
				}
				map.put(separator_str, 1);
				map.put(separator_str + "-td", tds.size() - 1);
			} else {
				map.put(separator_str, (Integer) map.get(separator_str) + 1);
				tds.set((Integer) map.get(separator_str + "-td"), tds.get((Integer) map.get(separator_str + "-td"))
						.replaceAll("rowspan=\'[0-9]*\'", "rowspan='" + map.get(separator_str) + "'"));
			}
			hmtmp = (Map) hmtmp.get(rowKeySplit);
		}
	}

	private void prepareHeaders(String separator_str, List<String[]> summaryKeys, ArrayList<String> columnGroupKeys,
			Map<String, List<String>> headmap) {
		{/*prepare headers*/
			/*sort columns, all grouped columns*/
			Collections.sort(columnGroupKeys);
			/*{0: [header-level0], 1: [header-level1], last: [value-header]}*/
			for (String columnGroupKey : columnGroupKeys) {
				String[] split = columnGroupKey.split(separator_str)/* one grouped columns. */;
				/* add all summary to the grouped columns. */
				/*
				 * +--------------+------------------+
				 * |    gender    |       goods      |    => size = 2, level 0=[gender], in table the gender column-span = 2
				 * +--------------+------------------+
				 * | fmale | male | papers | printer |    => size = 2, level 1=[fmale,male]
				 * +--------------+------------------+
				 * | summaryName  |    summaryName   |    =>         , level 2=[summaryNames]
				 * +--------------+------------------+
				 */
				for (String[] summaryName : summaryKeys) {
					for (int i = 0; i < split.length; i++) {
						/*this array size for the column-span in table*/
						if (headmap.get(String.valueOf(i)) == null) {
							headmap.put(String.valueOf(i), new ArrayList<String>());
						}
						headmap.get(String.valueOf(i)).add(split[i]);
					}
					if (headmap.get(String.valueOf(split.length)) == null) {
						headmap.put(String.valueOf(split.length), new ArrayList<String>());
					}
					headmap.get(String.valueOf(split.length)).add(summaryName[0]);
				}
			}
		}
	}

	private void printTableHeader(List<String> tds, List<String> row, List<String> col, Map<String, Map> summary,
			Map<String, List<String>> headmap) {
		{/* print all the table header */
			/*
			 * +--------------+------------------+-------------------+
			 * |       |      |       goods      |                   |   ==> first loop row.collect... and the blank
			 * +--------------+------------------+ columnSummaryName +
			 * |       |      | papers | printer |                   |   ==> second loop row.collect... and the blank
			 * +--------------+------------------+-------------------+
			 * |       | male |   11   |    12   |        23         |
			 * + gender|-------+-----------------+-------------------+
			 * |       | fmale|   21   |    22   |        43         |
			 * +--------------+------------------+-------------------+
			 * |rowSummaryName|   32   |    34   |        66         |
			 * +--------------+------------------+-------------------+
			 */
			tds.add("<thead>");
			int index = -1;
			for (Entry<String, List<String>> entry : headmap.entrySet()) {
				index = index + 1;
				{/*打行与列左上角交叉的空白部分打印出来*/
					tds.add(StringHelper.concat("<tr>", StringUtils.repeat("<th class='ord-blank'></th>", row.size())));
				}
				List<String> levelHeader = entry.getValue();
				for (int i = 0; i < levelHeader.size(); i++) {
					String head = levelHeader.get(i);
					int count = i + 1;
					/*看看下一个列头是不是与上一个列头相同，如果相同那就合并单元格*/
					for (; count < levelHeader.size(); count++) {
						String headNext = levelHeader.get(count);
						if (!StringUtils.equals(head, headNext)) {
							break;
						}
					}
					tds.add(StringHelper.concat("<th colspan='", String.valueOf(count - i), "'>", head, "</th>"));
					i = count - 1;
				}
				/* append column summary */
				if (index == 0) {
					int summaryIndex = -1;
					for (Entry<String, Map> summaryEntry : summary.entrySet()) {
						summaryIndex = summaryIndex + 1;
						String displayname = StringUtils.defaultIfBlank(
								(String) summaryEntry.getValue().get("display-name"), summaryEntry.getKey());
						tds.add(StringHelper.concat("<th rowspan='", String.valueOf(col.size() + 1),
								"' class='ord-col-summary-head ord-col-summary-head-", String.valueOf(summaryIndex),
								"'>", displayname, "</th>"));
					}
				}
				tds.add("</tr>");
			}
			tds.add("</thead>");
		}
	}

	private void initData(Map reportConfig, List<String> row, List<String> col, Map<String, Map> summary,
			List<String[]> summaryKeys) {
		{
			Map map = (Map) MapHelper.getByPath(reportConfig, "crosstable/rows");
			for (Entry entry : (Set<Entry>) map.entrySet()) {/* k=row@name */
				row.add(StringUtils.substringAfter((String) entry.getKey(), "row@"));
			}
		}
		{
			Map map = (Map) MapHelper.getByPath(reportConfig, "crosstable/columns");
			for (Entry entry : (Set<Entry>) map.entrySet()) {/* k=column@name */
				col.add(StringUtils.substringAfter((String) entry.getKey(), "column@"));
			}
		}
		{
			Map map = (Map) MapHelper.getByPath(reportConfig, "crosstable");
			for (Entry entry : (Set<Entry>) map.entrySet()) {
				if (entry.getKey().toString().startsWith("summary@")) {
					for (Entry ventry : (Set<Entry>) ((Map) entry.getValue()).entrySet()) {
						String key = ventry.getKey().toString();
						if (key.indexOf('@') > -1) {/* k=value@name */
							summary.put(StringUtils.substringAfter(key, "value@"), (Map) ventry.getValue());
							summaryKeys.add(new String[] { StringUtils.substringAfter(key, "value@"),
									(String) ((Map) ventry.getValue()).get("summary-method") });
						}
					}
				}
			}
		}
	}
}
