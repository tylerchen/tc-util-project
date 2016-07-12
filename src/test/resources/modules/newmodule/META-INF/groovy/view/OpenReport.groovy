package org.iff.groovy.view.openreport

class TCBeanUtil{
	@groovy.transform.CompileStatic
	public static Object get(String beanName){
		return org.iff.infra.util.groovy2.TCGroovyLoader.getDefaultGroovyLoader().getBean(beanName)
	}
	@groovy.transform.CompileStatic
	public static Object getSpringBean(String beanName){
		return org.iff.infra.util.spring.SpringContextHelper.getBean(beanName)
	}
}

@TCBean(name='TC_COM_RT_string')
class RTString{
	def returnType(value, defVal){
		return value? value.toString() : (defVal ?: '')
	}
}
@TCBean(name='TC_COM_RT_like')
class RTLike{
	def returnType(value, defVal){
		return value? "%${value}%" : (defVal? "%${defVal}%" : '')
	}
}
@TCBean(name='TC_COM_RT_date')
class RTDate{
	def returnType(value, defVal){
		return org.apache.commons.lang3.time.DateUtils.parseDate(value ?: defVal, 'yyyy-MM-dd HH:mm:ss','yyyy-MM-dd','yyyy/MM/dd HH:mm:ss','yyyy/MM/dd')
	}
}
@TCBean(name='TC_COM_RT_split')
class RTSplit{
	def returnType(value, defVal){
		return value? value.split(',') : []
	}
}

@TCBean(name='TC_COM_HW_html')
class HWHtml{
	def String htmlWidget(paramMap){/* {name, value, html} */
		${paramMap.html}
	}
}
@TCBean(name='TC_COM_HW_blank')
class HWBlank{
	def String htmlWidget(paramMap){/* {name, value, html} */
		'&nbsp;'
	}
}
@TCBean(name='TC_COM_HW_hidden')
class HWHidden{
	def String htmlWidget(paramMap){/* {name, value, html} */
		"""
		<input type="hidden" name="${paramMap.name}" id="${paramMap.name}" value="${paramMap.value}" /> 
		"""
	}
}
@TCBean(name='TC_COM_HW_script')
class HWScript{
	def String htmlWidget(paramMap){/* {name, value, html} */
		"""
		<script type="text/javascript">
			${paramMap.value}
		</script>
		"""
	}
}
@TCBean(name='TC_COM_HW_text')
class HWText{
	def htmlWidget(paramMap){/* {name, value, html} */
		"""
		<input type="text" name="${paramMap.name}" id="${paramMap.name}" value="${paramMap.value}" /> 
		"""
	}
}
@TCBean(name='TC_COM_HW_cn2select')
class HWCN2Select{
	def String htmlWidget(paramMap){/* {name, value, html} */
		def content=[]
		/* the first select is the province select */
		content << """
			<select name="${paramMap.name}" id="${paramMap.name}" class="or-hw-select province">
				<option></option>
			"""
		(paramMap.data ?: '').split(';').collect{it.trim().split(',')}.findAll{it.size()>1}.each{area->
			def province=area[0]
			if(province.indexOf(':')>0){
				def pIdName=province.split(':')
				content << """<option value="${pIdName[0]}" data="${area[1..-1].join(',')}">${pIdName[1]}</option>"""
			}else{
				content << """<option value="${area[0]}" data="${area[1..-1].join(',')}">${area[1]}</option>"""
			}
		}
		content << """</select>"""
		/* the second select is the city select */
		content << """
			<select name="${paramMap.name}_2" id="${paramMap.name}_2" class="or-hw-select city">
				<option></option>
			</select>
			"""
		content
	}
}
@TCBean(name='TC_COM_HW_select')
class HWSelect{
	def String htmlWidget(paramMap){/* {name, value, html} */
		"""
		<!--<input type="hidden" name="${paramMap.name}_i" id="${paramMap.name}_i" value="${paramMap.value}" />-->
		<select name="${paramMap.name}" id="${paramMap.name}" class="or-hw-select">
			${paramMap.html}
		</select>
		"""
	}
}
@TCBean(name='TC_COM_HW_mselect')
class HWMSelect{
	def String htmlWidget(paramMap){/* {name, value, html} */
		"""
		<select name="${paramMap.name}" id="${paramMap.name}" class="or-hw-mselect" multiple="multiple">
			${paramMap.html}
		</select>
		"""
	}
}

@TCBean(name='TC_OR_SM_sum')
class ORSMsum{
	def summary(records, colName){
		def total=0 as BigDecimal
		records.each{rc->
			def bd=((rc in Map) ? rc[colName] : rc)
			if(bd instanceof Number){
				bd = bd as BigDecimal
				total=total+bd
			}else if(bd instanceof String && bd.isNumber()){
				bd=bd.toBigDecimal()
				total=total+bd
			}
		}
		total
	}
}
@TCBean(name='TC_OR_SM_avg')
class ORSMavg{
	def summary(records, colName){
		def total=0 as BigDecimal
		records.each{rc->
			def bd=((rc in Map) ? rc[colName] : rc)
			if(bd instanceof Number){
				bd = bd as BigDecimal
				total=total+bd
			}else if(bd instanceof String && bd.isNumber()){
				bd=bd.toBigDecimal()
				total=total+bd
			}
		}
		if(records && records.size()>0){
			return  total.divide(records.size() as BigDecimal, total.scale(), BigDecimal.ROUND_HALF_UP)
		}
		null
	}
}
@TCBean(name='TC_OR_SM_max')
class ORSMmax{
	def summary(records, colName){
		def max
		records.each{rc->
			def bd=((rc in Map) ? rc[colName] : rc)
			if(bd instanceof Number){
				bd = bd as BigDecimal
				max=(max ?: bd).max(bd)
			}else if(bd instanceof String && bd.isNumber()){
				bd=bd.toBigDecimal()
				max=(max ?: bd).max(bd)
			}
		}
		max
	}
}
@TCBean(name='TC_OR_SM_min')
class ORSMmin{
	def summary(records, colName){
		def min
		records.each{rc->
			def bd=((rc in Map) ? rc[colName] : rc)
			if(bd instanceof Number){
				bd = bd as BigDecimal
				min=(min ?: bd).min(bd)
			}else if(bd instanceof String && bd.isNumber()){
				bd=bd.toBigDecimal()
				min=(min ?: bd).min(bd)
			}
		}
		min
	}
}
@TCBean(name='TC_OR_SM_count')
class ORSMcount{
	def summary(records, colName){
		records ? records.size() : 0
	}
}

@TCBean(name='TC_OR_freemarker')
class ORFreemarker{
	private config
	def String process(templateSource, paramMap){
		def writer = new java.io.StringWriter()
		getConfig().getTemplate(templateSource, 'UTF-8').process(paramMap, writer)
		return writer.toString()
	}
	def private getConfig(){
		if(config == null){
			config = new org.iff.infra.util.freemarker.FreeMarkerConfiguration()
			config.setDirectivePath('org.iff.infra.util.freemarker.model')
			config.setTemplateLoader(new freemarker.cache.StringTemplateLoader())
			def builder = new freemarker.template.DefaultObjectWrapperBuilder(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS)
			builder.setUseAdaptersForContainers(true)
			config.setObjectWrapper(builder.build())
		}
		return config
	}
}

@TCBean(name='TC_OR_page')
class ORpage{
	def String gen(actionTemplateSource, paramMap){
		return TCBeanUtil.get('TC_OR_freemarker').process(actionTemplateSource, paramMap)
	}
}
@TCBean(name='TC_OR_conditions')
class ORconditions{
	def String gen(actionTemplateSource, paramMap){
		return TCBeanUtil.get('TC_OR_freemarker').process(actionTemplateSource, paramMap)
	}
}

@TCBean(name='TC_OR_actions')
class ORactions{
	def String gen(actionTemplateSource, paramMap){
		return TCBeanUtil.get('TC_OR_freemarker').process(actionTemplateSource, paramMap)
	}
}

@TCBean(name='TC_OR_data')
class ORdata{
	def String gen(actionTemplateSource, paramMap){
		return TCBeanUtil.get('TC_OR_freemarker').process(actionTemplateSource, paramMap)
	}
}
@TCBean(name='TC_OR_pagination')
class ORpagination{
	def String gen(actionTemplateSource, paramMap){
		return TCBeanUtil.get('TC_OR_freemarker').process(actionTemplateSource, paramMap)
	}
}

@TCBean(name='TC_OR_crosstable')
class ORcrosstable{
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
    def String gen(openreportConfig, reportConfig, conditionParams, queryResult){
        def seperator_str='&&&&@@@@&&&&'
        def getSummaryBean={Object summaryKey->
            def bean=TCBeanUtil.get("TC_OR_SM_${summary[summaryKey].'summary-method'}")
            if(bean==null){
                ['summary':{Object[] o->
                    '-noop-'
                }]
            }else{
                bean
            }
        }
        // need to specify 'row' names, 'col' names, 'val' names, 'valmethod' value name map with summary
        def row=[]/*dimension x*/, col=[]/*dimension y*/, summary=[:]/*value for calculate*/, summaryKeys=[]/* summary key names */
        /* init all data */
        reportConfig.crosstable.rows.each{key,value-> /* k=row@name */
            row << (key-'row@')
        }
        reportConfig.crosstable.columns.each{key,value-> /* k=column@name */
            col << (key-'column@')
        }
        reportConfig.crosstable.find{k,v->k.startsWith('summary@')}.value.findAll{k,v->k.contains('@')}.each{key,value-> /* k=value@name */
            summary.put(key-'value@', value)
            summaryKeys << (key-'value@')
        }
        /*
         * cross table like group function. group the same value by row and column.
         * this solution is using {group-row-value : group-column-value} map structure.
         */
        def rowmap=[:]/*{row1-row2-row*: {col1-col2col*: data}}*/, colmap=[:]/* {col1-col2col*: data}} */, tds=[]/*table contents*/, records=queryResult.result, tr, tc
        records.each{record->
            def rowValueArray=[]/* find the row value from the records, the row name specified from the report define. */
            row.each{rowName->
                rowValueArray << record[rowName]
            }
            def rowGroupKey=rowValueArray.join(seperator_str)/*group the row value. row key join as row key for group row-values and sort*/
            def columnGroupMap=rowmap[rowGroupKey]=rowmap[rowGroupKey] ?: [:]/*{joined-row-key : {joined-column-key:[record-row-datas]}}*/
            
            def columnValueArray=[]
            col.each{columnName->
                columnValueArray << record[columnName]
            }
            def columnGroupKey=columnValueArray.join(seperator_str)/*group the column value. column key join as column key for group column-values and sort*/
            colmap.put(columnGroupKey,'')
            
            (columnGroupMap[columnGroupKey]=columnGroupMap[columnGroupKey] ?: []) << record
        }/* when this loop is end, all the records has grouped. the rest code is how to print the cross table. */
        
        def columnGroupKeys=colmap.keySet().sort()/*sort columns, all grouped columns*/
        def headmap=[:]/*{0: [header-level0], 1: [header-level1], last: [value-header]}*/
        def rktmp/*temp for row key*/
        columnGroupKeys.each{columnGroupKey->
            def split=columnGroupKey.split(seperator_str)/* one grouped columns. */
            summaryKeys.each{summaryName-> /* add all summary to the grouped columns. */
                /*
                 * +--------------+------------------+
                 * |    gender    |       goods      |    => size = 2, level 0=[gender], in table the gender column-span = 2
                 * +--------------+------------------+
                 * | fmale | male | papers | printer |    => size = 2, level 1=[fmale,male]
                 * +--------------+------------------+
                 * | summaryName  |    summaryName   |    =>         , level 2=[summaryNames]
                 * +--------------+------------------+
                 */
                split.eachWithIndex{columnKey, columnIndex->
                    (headmap[columnIndex]=headmap[columnIndex] ?: []) << columnKey /*this array size for the column-span in table*/
                }
                (headmap[split.size()]=headmap[split.size()] ?: []) << summaryName
            }
        }
        
        tds << '<table class=\'open-report-data\'>'
        tds << '<thead>'
        headmap.eachWithIndex{level, heads, index-> /* print all the table header */
            /*
             * +--------------+------------------+-------------------+
             * |       |      |       goods      |                   |   ==> first loop row.collect... add the blank
             * +--------------+------------------+ columnSummaryName +
             * |       |      | papers | printer |                   |   ==> second loop row.collect... add the blank
             * +--------------+------------------+-------------------+
             * |       | male |   11   |    12   |        23         |
             * + gender|-------+-----------------+-------------------+
             * |       | fmale|   21   |    22   |        43         |
             * +--------------+------------------+-------------------+
             * |rowSummaryName|   32   |    34   |        66         |
             * +--------------+------------------+-------------------+
             */
            tds << "<tr><th class='ord-blank'>${row.collect{''}.join('</th><th class=\'ord-blank\'>')}</th>"
            def cspan, count=1
            heads.each{h->
                if(cspan==h){ /* not the first loop, like the sub-header. */
                    count=count+1
                }else{
                    if(cspan!=null){ /* the first loop, the top-header */
                        tds << "<th colspan='${count}'>${cspan}</th>"
                    }
                    cspan=h
                    count=1
                }
            }
            if(cspan!=null){
                tds << "<th colspan='${count}'>${cspan}</th>"
            }
            if(index==0){ /* append column summary */
                summary.eachWithIndex{summaryName, value, idx->
                    def displayname=value.'display-name' ?: summaryName
                    tds << "<th rowspan='${col.size()+1}' class='ord-col-summary-head ord-col-summary-head-${idx}'>${displayname}</th>"
                }
            }
            tds << "</tr>"
            cspan=null
        }
        tds << '</thead>'
        
        def hasmap=[:], rowsummary=(headmap[0]+summaryKeys).collect{[]}/*init row summary*/
        tds << "<tbody>"
        rowmap.eachWithIndex{rowKey, rowValue, rowIndex->/*{row1-row2-row*: {col1-col2col*: data}}*/
            tds << '<tr>'
            def colsummary=summaryKeys.collect{[]}/*init col summary*/
            columnGroupKeys.eachWithIndex{columnKey, columnIndex->
                if(rowKey!=rktmp){
                    def hmtmp=hasmap
                    rowKey.split(seperator_str).each{rowKeySplit->
                        if(hmtmp[rowKeySplit]==null){/* see above like 'gender', first set rowspan=1, then will replace this value */
                            tds << "<td rowspan='1' class='ord-row-head'>${rowKeySplit}</td>"
                            (hmtmp[rowKeySplit]=[:]).put(seperator_str,1)
                            hmtmp[rowKeySplit].put(seperator_str+'-td',tds.size()-1)
                        }else{
                            hmtmp[rowKeySplit][seperator_str]=hmtmp[rowKeySplit][seperator_str]+1
                            tds[hmtmp[rowKeySplit][seperator_str+'-td']]=tds[hmtmp[rowKeySplit][seperator_str+'-td']].replaceAll('rowspan=\'[0-9]*\'', "rowspan='${hmtmp[rowKeySplit][seperator_str]}'")
                        }
                        hmtmp=hmtmp[rowKeySplit]
                    }
                }
                summary.eachWithIndex{summaryName, summaryNameValue, summaryIndex-> /* summary the row value, summaryName is the column name to summary the value */
                    def summaryValue=getSummaryBean(summaryName)(rowValue[columnKey], summaryName)
                    /* -FOR TEST- def summaryValue=summaryIndex==0 ? avg(rowValue[columnKey], summaryName) : max(rowValue[columnKey], summaryName) */
                    if(summaryValue!=null){
                        rowsummary[columnIndex*summaryKeys.size()+summaryIndex] << summaryValue /* append value to row summary */
                        colsummary[summaryIndex] << summaryValue /* append value to col summary */
                    }
                    tds << "<td>${summaryValue ?: ''}</td>"
                }/* END-summary.eachWithIndex */
                rktmp=rowKey
            }/* END-columnGroupKeys.eachWithIndex */
            colsummary.eachWithIndex{cs, csIdx-> /* append col summary value */
                def summaryValue=getSummaryBean(summaryKeys[csIdx])(cs, summaryKeys[csIdx])
                /* -FOR TEST- def summaryValue=csIdx==0 ? avg(cs, summaryKeys[csIdx]) : max(cs, summaryKeys[csIdx]) */
                if(summaryValue!=null){
                    rowsummary[columnGroupKeys.size()*summaryKeys.size()+csIdx] << summaryValue /* append value to row summary */
                }
                tds << "<td class='ord-col-summary ord-col-summary-${csIdx}'>${summaryValue ?: ''}</td>"
            }/* END-colsummary.eachWithIndex */
            tds << '</tr>'
        }
        tds << '</tbody>'
        tds << '</tfoot>'
        [0].each{/* row summary */
            def displayname=reportConfig.crosstable.find{k,v->k.startsWith('summary@')}.value.'display-name'
            tds << "<tr class='ord-row-summary'><td colspan='${row.size()}' class='ord-row-summary-name'>${displayname}</td>"
            rowsummary.eachWithIndex{rs, rsIds->
                def summaryValue=getSummaryBean(summaryKeys[rsIds%summaryKeys.size()])(rs,summaryKeys[rsIds%summaryKeys.size()])
                /* -FOR TEST- def summaryValue=rsIds==0?avg(rs,summaryKeys[rsIds%summaryKeys.size()]):max(rs,summaryKeys[rsIds%summaryKeys.size()]) */
                tds << "<td>${summaryValue}</td>"
            }
            tds << '</tr>'
        }
        tds << '</tfoot>'
        tds.join('\n')
    }
	/* -FOR TEST- println gen(openreportConfig, reportConfig, queryResult) */
}

@TCBean(name='TC_OR_SQL_jdbcConnection')
class ORSQLjdbcConnection{
	def connection(openreportConfig, reportConfig){
		new groovy.sql.Sql(TCBeanUtil.getSpringBean(reportConfig.datasource)) /* TODO Only support spring datasource!!! */
	}
}

@TCBean(name='TC_OR_SQL_pageQuery')
class ORSQLpageQuery{
	def query(openreportConfig, reportConfig, conditionParams, jdbcConnection){
		def query=TCBeanUtil.get('TC_OR_SQL_fillCondition').query(openreportConfig, reportConfig, reportConfig.query, conditionParams)
		def sql=query.sql, queryConditions=query.conditions
		def pageSize=reportConfig.'page-size'?.trim() ?: '0', totalCount=0, currentPage=(conditionParams.p ?: '1').toInteger()-1, result
		pageSize=(pageSize.isNumber() ? pageSize.toInteger() : 0) as int
		currentPage=currentPage>0 ? currentPage : 0
		def columnNames=[]
		def sqlMetaClosure={metaData -> columnNames.addAll(metaData*.columnLabel) }
		if(pageSize>0){
			def countSql='select count(1) from ('+sql+') tmp_count'
			if(queryConditions.size()>0){
				jdbcConnection.eachRow(countSql, queryConditions) { totalCount=it[0] }
			}else{
				jdbcConnection.eachRow(countSql) { totalCount=it[0] }
			}
			/* guest the jdbc url */
			def url=''
			try{ url=url ?: jdbcConnection.dataSource.url }catch(err){}
			try{ url=url ?: jdbcConnection.dataSource.jdbcUrl }catch(err){}
			def dialect=org.iff.infra.util.jdbc.dialet.Dialect.getInstanceByUrl(url)/* TODO Only support C3PO, apache, dubbo datasource!!! */
			def limitSql=dialect.getLimitString(sql, currentPage*pageSize, pageSize)
			result = queryConditions.size()>0 ? jdbcConnection.rows(limitSql, queryConditions, sqlMetaClosure) : jdbcConnection.rows(limitSql, sqlMetaClosure)
		}else{
			result = queryConditions.size()>0 ? jdbcConnection.rows(sql, queryConditions, sqlMetaClosure) : jdbcConnection.rows(sql, sqlMetaClosure)
			totalCount=pageSize=result.size()
		}
		['result':result, 'pageSize':pageSize, 'totalCount':totalCount, 'currentPage':currentPage, 'columnNames':columnNames]
	}
}
@TCBean(name='TC_OR_SQL_queryAll')
class ORSQLqueryAll{
	def query(openreportConfig, reportConfig, conditionParams, jdbcConnection){
		def query=TCBeanUtil.get('TC_OR_SQL_fillCondition').query(openreportConfig, reportConfig, reportConfig.query, conditionParams)
		def sql=query.sql, queryConditions=query.conditions
		def columnNames=[]
		def sqlMetaClosure={metaData -> columnNames.addAll(metaData*.columnLabel) }
		def result = queryConditions.size()>0 ? jdbcConnection.rows(sql, queryConditions, sqlMetaClosure) : jdbcConnection.rows(sql, sqlMetaClosure)
		['result':result, 'pageSize':result.size(), 'totalCount':result.size(), 'currentPage':1, 'columnNames':columnNames]
	}
}
@TCBean(name='TC_OR_SQL_fillCondition')
class ORSQLfillCondition{
	def query(openreportConfig, reportConfig, sql, conditionParams){
		/* replace or remove the condition block (#[condition]) */
		def index=0, conditions=[:], conditionParamsConverted=TCBeanUtil.get('TC_OR_SQL_conditions').process(openreportConfig, reportConfig, conditionParams)
		while((index=sql.indexOf('#['))>0){
			def tmp=sql.substring(index+2, sql.indexOf(']',index))
			def name=tmp.substring(tmp.indexOf(':')+1).trim()
			def hasParam=conditionParamsConverted[name]!=null && conditionParamsConverted[name]!=''
			sql=sql.substring(0, index)+(hasParam ? tmp : '')+sql.substring(sql.indexOf(']',index)+1)
			if(hasParam){
				conditions.put(name, conditionParamsConverted[name])
			}
		}
		['sql':sql.trim(), 'conditions':conditions]
	}
}
@TCBean(name='TC_OR_SQL_conditions')
class ORSQLconditions{
	def process(openreportConfig, reportConfig, conditionParams){
		/* process the url param to the conditions by type */
		def conditions=[:]
		reportConfig.conditions.each{cdt->
			def type=cdt.'return-type', name=cdt.name, value=conditionParams[cdt.name]
			conditions.put(name, TCBeanUtil.get('TC_OR_returnType').returnType(openreportConfig, reportConfig, cdt, value))
		}
		conditions
	}
}
@TCBean(name='TC_OR_returnType')
class ORSQLreturnType{
	def returnType(openreportConfig, reportConfig, conditionConfig, value){
		/* process the url param to the specify type */
		def returnType=TCBeanUtil.get("TC_COM_RT_${conditionConfig.'return-type'}")
		returnType.returnType(value, returnType.returnType(conditionConfig.defVal, null))
	}
}

@TCBean(name='TC_OR_OP_query')
class OROPquery{
	def process(paramsMap){
		def openreportConfig=paramsMap.openreportConfig, reportConfig=paramsMap.reportConfig, conditionParams=paramsMap.conditionParams
		def pageTemplate=reportConfig.page.'page-template' ?: 'page_template', dataTemplate=reportConfig.page.'data-template' ?: 'data_template'
		def actionTemplate=reportConfig.page.'action-template' ?: 'action_template', conditionTemplate=reportConfig.page.'condition-template' ?: 'condition_template'
		def paginationTemplate=reportConfig.page.'pagination-template' ?: 'pagination_template'
		def isCrossTable=reportConfig.crosstable ? true : false
		def actionContent=TCBeanUtil.get('TC_OR_actions').gen(openreportConfig.templates."template@${actionTemplate}".'nodeContent', paramsMap)
		def conditionContent=TCBeanUtil.get('TC_OR_conditions').gen(openreportConfig.templates."template@${conditionTemplate}".'nodeContent', paramsMap)
		def dataContent, paginationContent
		if(isCrossTable){
			def jdbcConnection=TCBeanUtil.get('TC_OR_SQL_jdbcConnection').connection(openreportConfig, reportConfig)
			def queryResult=TCBeanUtil.get('TC_OR_SQL_queryAll').query(openreportConfig, reportConfig, conditionParams, jdbcConnection)
			dataContent=TCBeanUtil.get('TC_OR_crosstable').gen(openreportConfig, reportConfig, conditionParams, queryResult)
		}else{
			def jdbcConnection=TCBeanUtil.get('TC_OR_SQL_jdbcConnection').connection(openreportConfig, reportConfig)
			def queryResult=TCBeanUtil.get('TC_OR_SQL_pageQuery').query(openreportConfig, reportConfig, conditionParams, jdbcConnection)
			paramsMap.'queryResult'=queryResult
			dataContent=TCBeanUtil.get('TC_OR_data').gen(openreportConfig.templates."template@${dataTemplate}".'nodeContent', paramsMap)
			paginationContent=TCBeanUtil.get('TC_OR_pagination').gen(openreportConfig.templates."template@${paginationTemplate}".'nodeContent', paramsMap)
		}
		paramsMap.'actionContent'=actionContent
		paramsMap.'conditionContent'=conditionContent
		paramsMap.'dataContent'=dataContent
		paramsMap.'paginationContent'=paginationContent
		def pageContent=TCBeanUtil.get('TC_OR_page').gen(openreportConfig.templates."template@${pageTemplate}".'nodeContent', paramsMap)
		pageContent
	}
}
@TCBean(name='TC_OR_OP_excel')
class OROPexcel{
	def process(paramsMap){
		def openreportConfig=paramsMap.openreportConfig, reportConfig=paramsMap.reportConfig, conditionParams=paramsMap.conditionParams
		def jdbcConnection=TCBeanUtil.get('TC_OR_SQL_jdbcConnection').connection(openreportConfig, reportConfig)
		def queryResult=TCBeanUtil.get('TC_OR_SQL_queryAll').query(openreportConfig, reportConfig, conditionParams, jdbcConnection)
		
		def workbook=new ExcelBuilder().workbook {
			styles{
				font("bold"){ font ->
					font.setBoldweight(font.BOLDWEIGHT_BOLD)
				}
				cellStyle ("header"){ cellStyle ->
					cellStyle.setAlignment(cellStyle.ALIGN_CENTER)
				}
			}
			data {
				sheet (reportConfig.'display-name' ?: reportConfig.'name')  {
					header(queryResult.columnNames)
					queryResult.result.each{rowData ->
						def data=[]
						rowData.each{k, v -> data << v }
						row(data)
					}
				}
			}
			commands {
				/*applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..3, sheetName:'Export2')*/
				/*mergeCells(rows: 1, columns: 1..3)*/
			}
		}
		def baos=new ByteArrayOutputStream(1024*1024)
		workbook.write(baos)
		['fileName':"${reportConfig.'display-name' ?: reportConfig.'name'}.xls", 'data':baos.toByteArray()]
	}
}

@TCAction(name="/openreport")
class OpenReportAction{
	def index(paramsMap){
		def op=paramsMap.conditionParams.op ?: 'query'
		return TCBeanUtil.get("TC_OR_OP_${op}").process(paramsMap)
	}
}

