package org.iff.groovy.view.openreport

@TCAction(name="/report/open_report")
class OpenReportParser{
	def index(){
		def response=params.response
		response.writer << "<!DOCTYPE html>\n<html><head><meta charset='utf-8'></head><body><h1>Open Report</h1>"
		def reportFiles=org.iff.infra.util.ResourceHelper.loadResourcesInFileSystem('E:/workspace/JeeGalileo/tc-util-project/src/test/resources/webapp/open-report', '.xml', 'open-report-*.xml', '')
		response.writer << reportFiles
		def xml_struct=[
			'data-sources':[:],//name:{name,driver,url,username,password,encrypt}
			'report-groups':[:],//name:{name,path,skin,display-name,data-source-ref,report-refs:{name,role,skin,display-name},role-refs:{name}}
			'roles':[:],//name:{name,role-name,data-source-ref,sql,code,language}
			'report-users':[:],//{users:name:{name,password,role-ref,encrypt},sql,code,language,data-source-ref}
			'config':[
					'actions':[:],//{name:value}
					'html-widgets':[:],//{name:value}
					'return-types':[:],//{name:value}
				],
			'reports':[:],//{name,display-name,page-size,condition-size,actions:[{name,display-name,action,index}],conditions:[{name,display-name,type,default-value,return-type,index,html,code,language,data-source-ref}]}
		]
		reportFiles.each{
			parseXml(xml_struct, it)
		}
		response.writer << xml_struct
		response.writer << "</body></html>"
	}
	def reportConfig(){
		def reportFiles=org.iff.infra.util.ResourceHelper.loadResourcesInFileSystem('E:/workspace/JeeGalileo/tc-util-project/src/test/resources/webapp/open-report', '.xml', 'open-report-*.xml', '')
		def xml_struct=[
			'data-sources':[:],//name:{name,driver,url,username,password,encrypt}
			'report-groups':[:],//name:{name,path,skin,display-name,data-source-ref,report-refs:{name,role,skin,display-name},role-refs:{name}}
			'roles':[:],//name:{name,role-name,data-source-ref,sql,code,language}
			'report-users':[:],//{users:name:{name,password,role-ref,encrypt},sql,code,language,data-source-ref}
			'config':[
					'actions':[:],//{name:value}
					'html-widgets':[:],//{name:value}
					'return-types':[:],//{name:value}
				],
			'reports':[:],//{name,display-name,page-size,condition-size,actions:[{name,display-name,action,index}],conditions:[{name,display-name,type,default-value,return-type,index,html,code,language,data-source-ref}]}
			'processor':[:],
		]
		reportFiles.each{
			parseXml(xml_struct, it)
		}
		registXmlProcessor(xml_struct)
		xml_struct
	}
	def private parseXml(xml_struct, report_xml){
		def xml=new XmlParser().parse(new File(report_xml))
		xml.'data-sources'.'data-source'.each{
			xml_struct.'data-sources'.put(
				it.'@name', it.attributes()+[
					'driver'  : it.driver.text(), 
					'url'     : it.url.text(),
					'username': it.username.text(),
					'password': it.password.text(),
					'encrypt' : it.password.'@encrypt'[0] ?: 'BASE64',
				]
			)
		}
		xml.'report-groups'.'report-group'.each{ds -> //name="" path="" skin="" display-name="" data-source-ref=""
			xml_struct.'report-groups'.put(
				ds.'@name', ds.attributes()+[
					'report-refs'    : [:].with{ m->
						ds.'report-ref'.each{
							m.put( it.'@name', it.attributes() )
						}// end each
						m
					},//end with
					'role-refs'      : [:].with{ m->
						ds.'role-ref'.each{ m.put(it.'@name',it.attributes()) }
						m
					}//end with
				]
			)
		}
		xml.'report-roles'.'role'.each{
			xml_struct.'roles'.put(
				it.'@name', it.attributes()+[
					'sql'            : it.sql.text(),
					'code'           : it.code.text(),
					'language'       : it.code.'@language'[0] ?: 'groovy',
					'data-source-ref': it.code.'@data-source-ref'[0] ?: it.sql.'@data-source-ref'[0]
				]
			)
		}
		[0].each{
			def ru=xml.'report-users'
			xml_struct.'report-users'.put(
				'users',[:].with{ m->
					ru.'user'.each{
						m.put(it.'@name',it.attributes()+['role-ref': (it.'@role-ref' ?: '').split(","),])
					}// end each
					m
				}// end with
			)
			xml_struct.'report-users'.put('sql', 			 ru.sql.text())
			xml_struct.'report-users'.put('code', 			 ru.code.text())
			xml_struct.'report-users'.put('language', 		 ru.code.'@language'[0] ?: 'groovy')
			xml_struct.'report-users'.put('data-source-ref', ru.code.'@data-source-ref'[0] ?: ru.sql.'@data-source-ref'[0])
		}
		xml.'config'.'actions'.'action'.each{
			xml_struct.'config'.'actions'.put(it.'@name', it.text())
		}
		xml.'config'.'html-widgets'.'html-widget'.each{
			xml_struct.'config'.'html-widgets'.put(it.'@name', it.attributes()+[
				'sql'            : it.sql.text(),
				'code'           : it.code.text(),
				'language'       : it.code.'@language'[0] ?: 'groovy',
				'data-source-ref': it.code.'@data-source-ref'[0] ?: it.sql.'@data-source-ref'[0],
				'javascript'     : it.javascript.text(),
				'data'           : it.data.text(),
				'html'           : it.html.text(),
			])
		}
		xml.'config'.'return-types'.'return-type'.each{
			xml_struct.'config'.'return-types'.put(it.'@name', it.text())
		}
		xml.'reports'.'report'.each{rp ->
			def isCross=false
			xml_struct.'reports'.put(
				rp.'@name', rp.attributes()+[
					'actions'       : [].with{ m ->
						rp.'actions'.'action'.each{
							m.add(it.attributes())// end m.put
						}// end each
						m
					},// end with
					'conditions'    : [].with{ m ->
						rp.'conditions'.'condition'.each{
							m.add(it.attributes()+[
									'html'           : it.html.text(),
									'code'           : it.code.text(),
									'language'       : it.code.'@language'[0],
									'data-source-ref': it.code.'@data-source-ref'[0],
								]
							)// end m.put
						}// end each
						m
					},// end with
					'query'         :rp.query.text(),
					'html'          :[
						'css'        : rp.html.css.text()?.trim()?.split(','),
						'style'      : rp.html.style.text(),
						'js'         : rp.html.js.text()?.trim()?.split(','),
						'script'     : rp.html.script.text(),
						'data-header': rp.html.'data-header'.text(),
						'data-body'  : rp.html.'data-body'.text(),
					],
					'cross'         :(rp.'cross'[0]?.attributes() ?: [:])+[
						'rows':rp.cross?.rows?.row?.'@name' ?: [],
						'columns':rp.cross?.columns?.column?.'@name' ?: [],
						'values':[:].with{map->
							rp.'cross'?.'values'?.'value'?.each{v->
								map.put(v.'@name', v.attributes())
								isCross=true
							}
							map
						},
					],
				]
			)
			if(isCross){
				xml_struct.'reports'[rp.'@name'].put('isCross',true)
			}
		}
		xml_struct
	}
	def private registXmlProcessor(xml_struct){
		xml_struct.processor.'data-sources'={reportCfg-> // return the sql instance
			def jdbc=reportCfg.ds
			groovy.sql.Sql.newInstance(jdbc.url, jdbc.username, jdbc.password, jdbc.driver)
		}
		xml_struct.processor.'query'={reportCfg, ds, conditions-> // execute the sql from database
			def query=xml_struct.processor.sql(reportCfg, conditions)
			def sql=query.sql.trim(), queryConditions=query.conditions
			def pageSize=reportCfg.report.'page-size'?.trim() ?: '0', totalCount=0, currentPage=(conditions.p ?: '1').toInteger()-1, result
			pageSize=(pageSize.isNumber() ? pageSize.toInteger() : 0) as int
			currentPage=currentPage>0 ? currentPage : 0
			def columnNames=[]
			def sqlMetaClosure={metaData -> columnNames.addAll(metaData*.columnLabel) }
			if(pageSize>0){
				def countSql='select count(1) from ('+sql+') tmp_count'
				if(queryConditions.size()>0){
					ds.eachRow(countSql, queryConditions) { totalCount=it[0] }
				}else{
					ds.eachRow(countSql) { totalCount=it[0] }
				}
				def dialect=org.iff.infra.util.jdbc.dialet.Dialect.getInstanceByUrl(reportCfg.ds.url)
				def limitSql=dialect.getLimitString(sql, currentPage*pageSize, pageSize)
				result = queryConditions.size()>0 ? ds.rows(limitSql, queryConditions, sqlMetaClosure) : ds.rows(limitSql, sqlMetaClosure)
				//result.collect{total+=it.srp}
			}else{
				result = queryConditions.size()>0 ? ds.rows(sql, queryConditions, sqlMetaClosure) : ds.rows(sql, sqlMetaClosure)
				totalCount=pageSize=result.size()
			}
			[result:result, pageSize:pageSize, totalCount:totalCount, currentPage:currentPage, columnNames:columnNames]
		}
		xml_struct.processor.'queryAll'={reportCfg, ds, conditions-> // execute the sql from database
			def query=xml_struct.processor.sql(reportCfg, conditions)
			def sql=query.sql.trim(), queryConditions=query.conditions
			def columnNames=[]
			def sqlMetaClosure={metaData -> columnNames.addAll(metaData*.columnLabel) }
			def result = queryConditions.size()>0 ? ds.rows(sql, queryConditions, sqlMetaClosure) : ds.rows(sql, sqlMetaClosure)
			[result:result, pageSize:result.size(), totalCount:result.size(), currentPage:1, columnNames:columnNames]
		}
		xml_struct.processor.'sql'={reportCfg, param->// replace or remove the condition block (#[condition])
			def sql=reportCfg.report.query, index=0, conditions=[:]
			while((index=sql.indexOf('#['))>0){
			    def tmp=sql.substring(index+2, sql.indexOf(']',index))
			    def name=tmp.substring(tmp.indexOf(':')+1).trim()
				def hasParam=param[name]!=null && param[name]!=''
			    sql=sql.substring(0, index)+(hasParam ? tmp : '')+sql.substring(sql.indexOf(']',index)+1)
				if(hasParam){
					conditions.put(name, param[name])
				}
			}
			[sql:sql, conditions:conditions]
		}
		xml_struct.processor.'condition'={reportCfg, urlParamMap->// process the url param to the conditions by type
			def conditions=[:]
			reportCfg.report.conditions.each{cdt->
				def type=cdt.'return-type', name=cdt.name, value=urlParamMap[cdt.name], defVal=cdt.'default-value'
				conditions.put(name, xml_struct.processor.'return-type'(reportCfg,type,value,defVal))
			}
			conditions
		}
		xml_struct.processor.'return-type'={reportCfg, type, value, defVal->// process the url param to the specify type
			def typeName=reportCfg.global.config?.'return-types'?."$type"
			def ins=this.getClass().classLoader.loadClass(typeName,true,false)?.newInstance()
			return (!typeName || !ins) ? (value ?: defVal) : ins.returnType(value, defVal)
		}
		xml_struct.processor.encrypt={map->//{name, value}
//			if(map.name=='BASE64'){
//				return new String(org.iff.infra.util.BaseCryptHelper.decodeBase64(map.value?.toCharArray()))
//			}else if(map.name=='AES'){
//				if(map.algorithm){
//					return org.iff.infra.util.AESCoderHelper.decrypt(
//						org.iff.infra.util.BaseCryptHelper.decodeBase64(map.value?.toCharArray())
//						, map.key.getBytes(), map.algorithm)
//				}else{
//					return org.iff.infra.util.AESCoderHelper.decrypt(
//						org.iff.infra.util.BaseCryptHelper.decodeBase64(map.value?.toCharArray())
//						, map.key.getBytes())
//				}
//			}else{
//				
//			}
			map.value
		}
		xml_struct.processor.roles={map->
			map.'role-name'?.split(',').collect{it.trim()}.findAll{it.size()>0}
		}
		xml_struct.processor.'html-widgets'={map->
			//smap.'role-name'?.split(',')
		}
	}
}

@TCAction(name="/report/report")
class OpenReportAction{
	def index(){
		def export
		params.urlParams.each{p->
			if(p.startsWith('export=')){
				export=p-'export='
			}
		}
		if(export){
			def excel=new ExcelAction()
			excel.metaClass.superAction=this
			excel.index()
		}else{
			def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
			def reportConfig=new OpenReportParser().reportConfig()
			def reportCfg=reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
			if(!reportCfg.isValid){
				TCHelper.close(response.writer){oss-> oss[0].append('report not found') }
				return
			}
			if(reportCfg.report.isCross){
				def action = new CrossTableAction()
				action.metaClass.superAction=this
				action.index()
			}else{
				def action = new QueryAction()
				action.metaClass.superAction=this
				action.index()
			}
		}
		return
	}
	def protected urlParamMap(reportCfg){
		def pmap=[:]
		params.urlParams.each{p->
			def index=p.indexOf('=')
			if(index>0){
				pmap.put(p.substring(0,index),p.substring(index+1))
			}
		}
		pmap
	}
	def protected reportCfg(reportConfig, groupName, reportName){// return {global,group,report,ds,rttype,conditions}
		def report=[:]
		report.global=reportConfig
		report.group=reportConfig.'report-groups'?."$groupName"
		report.report=reportConfig.'reports'?."$reportName"
		report.ds=reportConfig.'data-sources'?."${report.group.'data-source-ref'}"
		report.rttype=reportConfig.'config'.'return-types'
		report.conditions=[]
		report.report?.conditions.each{cdt->
			if(cdt.html && cdt.html.size()){
				report.conditions << cdt.clone()
			}else{
				def htmlWidget=reportConfig.config.'html-widgets'."${cdt.type}"
				if(htmlWidget?.'class'){
					def instance=this.getClass().classLoader.loadClass(htmlWidget.'class',true,false)?.newInstance()
					def widget=instance?.htmlWidget(['name':cdt.name, 'html-widget':htmlWidget])
					def tmp=cdt.clone()
					tmp.put('instance', instance)
					tmp.put('widget', widget)
					report.conditions << tmp
				}//END-if
			}//END-else
		}//END-each
		report.isValid=report.group && report.report
		report
	}
	def protected reportGroup(reportConfig, groupName){
		
	}
}

class QueryAction{
	def index(){
		def params=superAction.params
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def reportConfig=new OpenReportParser().reportConfig()
		def reportCfg=superAction.reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
		if(!reportCfg.isValid){
			TCHelper.close(response.writer){oss-> oss[0].append('report not found') }
			return
		}
		def pmap=superAction.urlParamMap(reportCfg)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		def sqlExecute=reportConfig.processor.query(reportCfg, reportConfig.processor.'data-sources'(reportCfg), pmap)
		def result=sqlExecute.result
		def reportParameter={
			table(class:'open-report-params'){
				tr{
					td{
						div(class:'or-cdts'){
							reportCfg.conditions.each{cdt->
								if(cdt.html && cdt.html.size()>0){
									div(class:"or-cdt-name ${cdt.name}", cdt.'display-name')
									div(class:"or-cdt-value ${cdt.name}"){
										mkp.yieldUnescaped(cdt.html)
									}
								}else{
									def htmlWidget=reportConfig.config.'html-widgets'."${cdt.type}"
									if(htmlWidget?.'class'){
										def instance=this.getClass().classLoader.loadClass(htmlWidget.'class',true,false)?.newInstance()
										def widget=instance?.htmlWidget(['name':cdt.name, 'html-widget':htmlWidget])
										if(widget in Closure){
											widget.delegate=delegate
											div(class:"or-cdt ${cdt.name}"){
												div(class:"or-cdt-name ${cdt.name}", cdt.'display-name')
												div(class:"or-cdt-value ${cdt.name}"){
													widget()
												}
											}
										}//END-if
									}//END-if
								}//END-else
							}//END-each
						}//END-div
						div(class:'or-cdts-fbtns'){
							reportCfg.report.actions.each{act->
								div(class:"or-cdts-fbtn ${act.name}"){
									button("${act.'display-name'}", act+['class':'or_submitable', type:'submit'])
								}
							}
						}
					}
				}
			}
		}
		def reportData={
			table(class:'open-report-data'){
				thead{
					tr{
						sqlExecute.columnNames.each{name ->
							th{
								div(name)
							}
						}
					}
				}
				tbody{
					result.each{row ->
						tr{
							row.each{k, v ->
								td(class:"or-rc-${k}"){
									div(v)
								}
							}//END-row.each
						}//END-tr
					}//END-result.each
				}
				if(reportCfg.report.'row-summary' in ['true','1']){
					tfoot{
						tr{
							result[0].each{k, v ->
								if(v instanceof Number){
									def total=0 as BigDecimal
									result.collect{total=total+(it."${k}" as BigDecimal)}
									td{
										div(total)
									}
								}else{
									td('')
								}
							}
						}
					}
				}
			}
		}
		def reportPagination={//[result:result, pageSize:pageSize, totalCount:totalCount, currentPage:currentPage]
			def currentPage=sqlExecute.currentPage+1
			def totalPage=(sqlExecute.pageSize>0?(sqlExecute.totalCount+sqlExecute.pageSize-1)/sqlExecute.pageSize:0) as int
			currentPage=currentPage<=1 ? 1 : currentPage
			currentPage=currentPage>totalPage ? totalPage : currentPage
			table(class:'open-report-pagination'){
				tr{
					td{
						div{
							a(href:'javascript:;', onclick:'return false', class:'page-btn page-total', "当前记录${sqlExecute.result.size()}条，共 ${sqlExecute.totalCount}条，共${totalPage}页")
							a(href:"p=0", title:'首页', class:'page-btn page-start or_submitable', '<<')
							def pageLink=[], start=Math.max(1, currentPage-5), end=Math.min(currentPage+5, totalPage),pos=0
							for(;start<=end;start++){
								pageLink << start
								pos=start==currentPage ? pageLink.size()-1 : pos
							}
							def startOffset=0, endOffset=0, len=pageLink.size()
							for(def i in 1..4){
								startOffset=pos-i>=0 ? i : startOffset
								endOffset=pos+i<=len-1 ? i : endOffset
								if(startOffset+endOffset==4){
									break
								}
							}
							pageLink=pageLink.subList(pos-startOffset,pos+endOffset+1)
							pageLink.each{pageNo->
								a(href:"p=${pageNo}", class:'page-btn or_submitable'+(pageNo==currentPage?' page-on':''), "${pageNo}")
							}
							a(href:"p=${totalPage}", title:'末页', class:'page-btn page-end or_submitable', '>>')
						}
					}
				}
			}
		}
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head{
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title("Report")
				reportCfg.report.html.css.each{
					link(type:"text/css", href:"${resContext}/${it}", rel:"stylesheet")
				}
				reportCfg.report.html.js.each{
					script('',type:"text/javascript", src:"${resContext}/${it}")
				}
				style(type:'text/css', reportCfg.report.html.style)
			}
			body{
				div(class:'open-report-header'){
					h1(reportCfg.report.'display-name')
				}
				div(class:'open-report-content'){
					form(class:'or-cdts-form', onsubmit:'return false', id: reportCfg.report.name){
						reportParameter.delegate=delegate
						reportParameter()
						reportData.delegate=delegate
						reportData()
						reportPagination.delegate=delegate
						reportPagination()
					}//END-form
				}
			}
			script(type:"text/javascript",reportCfg.report.html.script)
		}
	}
}

class RTString{
	def returnType(value, defVal){
		return value? value.toString() : (defVal ?: '')
	}
}
class RTLike{
	def returnType(value, defVal){
		return value? "%${value}%" : (defVal? "%${defVal}%" : '')
	}
}
class RTDate{
	def returnType(value, defVal){
		return org.apache.commons.lang3.time.DateUtils.parseDate(value ?: defVal, 'yyyy-MM-dd HH:mm:ss','yyyy-MM-dd','yyyy/MM/dd HH:mm:ss','yyyy/MM/dd')
	}
}
class RTSplit{
	def returnType(value){
		return value? value.split(',') : []
	}
}

class HWHtml{
	def htmlWidget(paramMap){
		return { mkp.yieldUnescaped(paramMap.'html-widget'.html) }
	}
}
class HWBlank{
	def htmlWidget(paramMap){
		return { mkp.yieldUnescaped('&nbsp;') }
	}
}
class HWHidden{
	def htmlWidget(paramMap){//name, value
		return { input(type:'hidden', name:paramMap.name, value:paramMap.value, id:paramMap.name) }
	}
}
class HWScript{
	def htmlWidget(paramMap){//name, value
		return { script(type:'text/javascript',paramMap.value) }
	}
}
class HWText{
	def htmlWidget(paramMap){//name, value
		return { input(type:'text', name:paramMap.name, value:paramMap.value, id:paramMap.name) }
	}
}
class HWCN2Select{
	def htmlWidget(paramMap){//name, value
		def area=paramMap.'html-widget'.data
		return {
			//input(type:'hidden', name:"${paramMap.name}_i", id:"${paramMap.name}_i")
			select(name:"${paramMap.name}", id:"${paramMap.name}", class:'or-hw-select province'){
				option('')
				area.split(";").collect{it.trim().split(",")}.findAll{it.size()>1}.each{
					def province=it[0]
					if(province.indexOf(':')>0){
						def pIdName=province.split(":")
						option(value:pIdName[0], data:it[1..-1].join(','), pIdName[1])
					}else{
						option(value:it[0], data:it[1..-1].join(','), it[0])
					}
				}
			}
			//input(type:'hidden', name:"${paramMap.name}_2_i", id:"${paramMap.name}_i2")
			select(name:"${paramMap.name}_2", id:"${paramMap.name}_2", class:'or-hw-select city'){
				option('')
			}
		}
	}
}
class HWSelect{
	def htmlWidget(paramMap){//name, value
		return {
			//input(type:'hidden', name:"${paramMap.name}_i", id:"${paramMap.name}_i", paramMap.value)
			select(name:paramMap.name, id:"${paramMap.name}", class:'or-hw-select'){
				mkp.yieldUnescaped(paramMap.'html-widget'.html)
			}
		}
	}
}
class HWMSelect{
	def htmlWidget(paramMap){//name, value
		return {
			select(name:paramMap.name, id:"${paramMap.name}", class:'or-hw-mselect', multiple:'multiple'){
				mkp.yieldUnescaped(paramMap.'html-widget'.html)
			}
		}
	}
}

class CrossTableAction{
	def seperator_str='&&&&@@@@&&&&'
	def noop={Object[] p->}
	def valmethodmap=[// value methods, provides: sum, avg
		'sum':{records, colName->
			def total=0 as BigDecimal
			records.each{rc->
				def bd=((rc in Map) ? rc[colName] : rc) as BigDecimal
				total=total+bd
			}
			total
		},'avg':{records, colName->
			def total=0 as BigDecimal
			records.each{rc->
				def bd=((rc in Map) ? rc[colName] : rc) as BigDecimal
				total=total+bd
			}
			if(records && records.size()>0){
				return  total.divide(records.size() as BigDecimal, total.scale(), BigDecimal.ROUND_HALF_UP)
			}
			return null
		}
	]
	def index(){
		def params=superAction.params
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def reportConfig=new OpenReportParser().reportConfig()
		def reportCfg=superAction.reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
		if(!reportCfg.isValid){
			TCHelper.close(response.writer){oss-> oss[0].append('report not found') }
			return
		}
		def pmap=superAction.urlParamMap(reportCfg)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		def sqlExecute=reportConfig.processor.queryAll(reportCfg, reportConfig.processor.'data-sources'(reportCfg), pmap)
		def result=sqlExecute.result
		def reportParameter={
			table(class:'open-report-params'){
				tr{
					td{
						div(class:'or-cdts'){
							reportCfg.conditions.each{cdt->
								if(cdt.html && cdt.html.size()>0){
									div(class:"or-cdt-name ${cdt.name}", cdt.'display-name')
									div(class:"or-cdt-value ${cdt.name}"){
										mkp.yieldUnescaped(cdt.html)
									}
								}else{
									def htmlWidget=reportConfig.config.'html-widgets'."${cdt.type}"
									if(htmlWidget?.'class'){
										def instance=this.getClass().classLoader.loadClass(htmlWidget.'class',true,false)?.newInstance()
										def widget=instance?.htmlWidget(['name':cdt.name, 'html-widget':htmlWidget])
										if(widget in Closure){
											widget.delegate=delegate
											div(class:"or-cdt ${cdt.name}"){
												div(class:"or-cdt-name ${cdt.name}", cdt.'display-name')
												div(class:"or-cdt-value ${cdt.name}"){
													widget()
												}
											}
										}//END-if
									}//END-if
								}//END-else
							}//END-each
						}//END-div
						div(class:'or-cdts-fbtns'){
							reportCfg.report.actions.each{act->
								div(class:"or-cdts-fbtn ${act.name}"){
									button("${act.'display-name'}", act+['class':'or_submitable', type:'submit'])
								}
							}
						}
					}
				}
			}
		}
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head{
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title("Report")
				reportCfg.report.html.css.each{
					link(type:"text/css", href:"${resContext}/${it}", rel:"stylesheet")
				}
				reportCfg.report.html.js.each{
					script('',type:"text/javascript", src:"${resContext}/${it}")
				}
				style(type:'text/css', reportCfg.report.html.style)
			}
			body{
				div(class:'open-report-header'){
					h1(reportCfg.report.'display-name')
				}
				div(class:'open-report-content'){
					form(class:'or-cdts-form', onsubmit:'return false', id: reportCfg.report.name){
						reportParameter.delegate=delegate
						reportParameter()
						def crossCfg=reportCfg.report.cross
						def sep_str=seperator_str, paramMap=pmap, cross_table_html
						try{
							cross_table_html=cross_table([
								row       : paramMap.cross_rows ? paramMap.cross_rows.split(',') : crossCfg.rows.clone(), 
								col       : paramMap.cross_cols ? paramMap.cross_cols.split(',') : crossCfg.columns.clone(), 
								val       : paramMap.cross_vals ? paramMap.cross_vals.split(',') : crossCfg.values.collect{k,v->k},
								valmethod : paramMap.cross_vlms ? org.iff.infra.util.JsonHelper.toObject(java.util.LinkedHashMap, paramMap.cross_vlms) : [:].with{m->
									crossCfg.values.each{k,v-> m.put(k,v.'summary-method')}
									m
								},
								valdisplay: paramMap.cross_vnms ? org.iff.infra.util.JsonHelper.toObject(java.util.LinkedHashMap, paramMap.cross_vnms) : [:].with{m->
									crossCfg.values.each{k,v-> m.put(k,v.'display-name')}
									m.put((sep_str+'-row-summary'),crossCfg.'display-name')
									m
								},
							],result)
						}catch(err){
							cross_table_html=err.toString()
						}
						mkp.yieldUnescaped(cross_table_html)
					}//END-form
				}
			}
			script(type:"text/javascript",reportCfg.report.html.script)
		}
	}
	def private cross_table(crossCfg, records){// need to specify 'row' names, 'col' names, 'val' names, 'valmethod' value name map with summary, 'valdisplay' value display name
		def row=crossCfg.row, col=crossCfg.col, val=crossCfg.val, valmethod=crossCfg.valmethod, valdisplay=crossCfg.valdisplay
		//if(row.any{r-> (r in col)}){
		//	return 'ERROR: rows and columns contains the same field.'
		//}
		def rowmap=[:]/*{row1-row2-row*: {col1-col2col*: data}}*/, colmap=[:]/*col1-col2col**/, tds=[]/*table contents*/, tr, tc
		records.each{rc->
			def rtmp=[]
			row.each{r->
				rtmp << rc[r]
			}
			def rkey=rtmp.join(seperator_str)
			def tmap=rowmap[rkey]=rowmap[rkey] ?: [:]
			
			def ctmp=[]
			col.each{c->
				ctmp << rc[c]
			}
			def ckey=ctmp.join(seperator_str)
			colmap.put(ckey,'')
			
			(tmap[ckey]=tmap[ckey] ?: []) << rc
		}
		
		def ckeys=colmap.keySet().sort()/*sort col*/, headmap=[:]/*the table header*/, rktmp
		ckeys.each{ck->
			def split=ck.split(seperator_str)
			for(def v=0;v<val.size();v++){
				for(def i=0;i<split.size();i++){
					(headmap[i]=headmap[i] ?: []).push(split[i])
				}
				(headmap[split.size()]=headmap[split.size()] ?: []).push(val[v])
			}
		}
		
		tds << '<table class=\'open-report-data\'>'
		tds << '<thead>'
		headmap.eachWithIndex{lvl, heads, index->
			tds << "<tr><th class='ord-blank'>${row.collect{''}.join('</th><th class=\'ord-blank\'>')}</th>"
			def cspan, count=1
			heads.each{h->
				if(cspan==h){
					count=count+1
				}else{
					if(cspan!=null){
						tds << "<th colspan='${count}'>${cspan}</th>"
					}
					cspan=h
					count=1
				}
			}
			if(cspan!=null){
				tds << "<th colspan='${count}'>${cspan}</th>"
			}
			if(index==0){// append column summary
		        val.eachWithIndex{v,idx->
					def displayname=valdisplay[v] ?: (((valmethod[v] ?: '')+':'+v))
		            tds << "<th rowspan='${col.size()+1}' class='ord-col-summary-head ord-col-summary-head-${idx}'>${displayname}</th>"
		        }
			}
			tds << "</tr>"
			cspan=null
		}
		tds << '</thead>'
		def hasmap=[:], rowsummary=(headmap[0]+val).collect{[]}/*init row summary*/
		tds << "<tbody>"
		(rowmap as TreeMap).eachWithIndex{rk,rv,ix->
			tds << '<tr>'
			def colsummary=val.collect{[]}/*init col summary*/
			ckeys.eachWithIndex{ck, ckeysIdx->
				if(rk!=rktmp){
					def hmtmp=hasmap
					rk.split(seperator_str).each{sp->
						if(hmtmp[sp]==null){
							tds << "<td rowspan='1' class='ord-row-head'>${sp}</td>"
							(hmtmp[sp]=[:]).put(seperator_str,1)
							hmtmp[sp].put(seperator_str+'-td',tds.size()-1)
						}else{
							hmtmp[sp][seperator_str]=hmtmp[sp][seperator_str]+1
							tds[hmtmp[sp][seperator_str+'-td']]=tds[hmtmp[sp][seperator_str+'-td']].replaceAll('rowspan=\'[0-9]*\'', "rowspan='${hmtmp[sp][seperator_str]}'")
						}
						hmtmp=hmtmp[sp]
					}
				}
				val.eachWithIndex{v, valIdx->
					def summary=valmethodmap[valmethod[v]](rv[ck],v)
					if(summary!=null){
						rowsummary[ckeysIdx*val.size()+valIdx] << summary // append value to row summary
						colsummary[valIdx] << summary // append value to col summary
					}
					tds << "<td>${summary ?: ''}</td>"
				}// END-val.eachWithIndex
				rktmp=rk
			}// END-ckeys.eachWithIndex
			colsummary.eachWithIndex{cs, csIdx-> // append col summary value
				def summary=valmethodmap[valmethod[val[csIdx]]](cs,val[csIdx])
				if(summary!=null){
					rowsummary[ckeys.size()*val.size()+csIdx] << summary // append value to row summary
				}
				tds << "<td class='ord-col-summary ord-col-summary-${csIdx}'>${summary ?: ''}</td>"
			}//END-colsummary.eachWithIndex
			tds << '</tr>'
		}
		tds << "</tbody>"
		tds << "</tfoot>"
		[0].each{// row summary
			def displayname=valdisplay[seperator_str+'-row-summary'] ?: 'Summary'
			tds << "<tr class='ord-row-summary'><td colspan='${row.size()}' class='ord-row-summary-name'>${displayname}</td>"
			rowsummary.eachWithIndex{rs, rsIds->
				tds << "<td>${valmethodmap[valmethod[val[rsIds%val.size()]]](rs,val[rsIds%val.size()]) ?: ''}</td>"
			}
			tds << '</tr>'
		}
		tds << "</tfoot>"
		tds.join('\n')
	}
}


class ExcelAction{
	def index(){
		def params=superAction.params
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def reportConfig=new OpenReportParser().reportConfig()
		def reportCfg=superAction.reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
		if(!reportCfg.isValid){
			TCHelper.close(response.writer){oss-> oss[0].append('report not found') }
			return
		}
		def pmap=superAction.urlParamMap(reportCfg)
		def sqlExecute=reportConfig.processor.queryAll(reportCfg, reportConfig.processor.'data-sources'(reportCfg), pmap)
		def result=sqlExecute.result
		
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
				sheet ("Export1")  {
					header(sqlExecute.columnNames)
					result.each{rowData ->
						def data=[]
						rowData.each{k, v -> data << v }
						row(data)
					}
				}
				sheet ("Export2")  {
					header(["Column1", "Column2", "Column3"])
					row(["a", "b", "c"])
				}
				sheet ("Export3")  {
					header(["Column1", "Column2", "Column3"])
					row(["a", "b", "c"])
				}
			}
			commands {
				applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..3, sheetName:'Export2')
				//mergeCells(rows: 1, columns: 1..3)
			}
		}
		def baos=new ByteArrayOutputStream(1024*1024)
		workbook.write(baos)
		response.reset()
		def fileName=superAction.userAgent().isIE ? superAction.urlEncode("${reportCfg.report.'display-name'}.xls") : new String("${reportCfg.report.'display-name'}.xls".getBytes('UTF-8'),'ISO8859-1')
		response.addHeader('Content-Disposition', 'attachment;filename='+fileName)
		response.addHeader('Content-Length', String.valueOf(baos.size()))
		response.setContentType('application/octet-stream')
		TCHelper.close(response.outputStream){oss-> baos.writeTo(oss[0]) }
	}
}

