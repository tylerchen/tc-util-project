package org.iff.groovy.view.openreport

import java.io.ByteArrayOutputStream;

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
		println xml_struct
	}
	def reportConfig(){
		def reportFiles=org.iff.infra.util.ResourceHelper.loadResourcesInFileSystem('g:/bak/app_root/webapp/open-report', '.xml', 'open-report-*.xml', '')
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
		return xml_struct
	}
	def private parseXml(xml_struct, report_xml){
		def xml=new XmlParser().parse(new File(report_xml))
		xml.'data-sources'.'data-source'.each{
			xml_struct.'data-sources'.put(
				it.'@name', [
					'name'    : it.'@name',
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
				ds.'@name', [
					'name'           : ds.'@name',
					'path'           : ds.'@path',
					'skin'           : ds.'@skin',
					'display-name'   : ds.'@display-name',
					'data-source-ref': ds.'@data-source-ref',
					'report-refs'    : [:].with{ m->
						ds.'report-ref'.each{
							m.put(
								it.'@name',[
									'name'        : it.'@name', 
									'role'        : it.'@role', 
									'skin'        : it.'@skin', 
									'display-name': it.'@display-name',
								]
							)
						}// end each
						m
					},//end with
					'role-refs'      : [:].with{ m->
						ds.'role-ref'.each{ m.put(it.'@name',[name: it.'@name']) }
						m
					}//end with
				]
			)
		}
		xml.'report-roles'.'role'.each{
			xml_struct.'roles'.put(
				it.'@name', [
					'name'           : it.'@name',
					'role-name'      : it.'@role-name', 
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
						m.put(it.'@name',[
							'name'    : it.'@name',
							'password': it.'@password',
							'role-ref': (it.'@role-ref' ?: '').split(","),
							'encrypt' : it.'@encrypt',
						])
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
			xml_struct.'config'.'html-widgets'.put(it.'@name', [
				'name'           : it.'@name',
				'class'          : it.'@class',
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
			xml_struct.'reports'.put(
				rp.'@name', [
					'name'          : rp.'@name',
					'display-name'  : rp.'@display-name', 
					'page-size'     : rp.'@page-size',
					'condition-size': rp.'@condition-size',
					'actions'       : [].with{ m ->
						rp.'actions'.'action'.each{
							m.add([
									'name'        : it.'@name',
									'display-name': it.'@display-name',
									'action'      : it.'@action',
									'index'       : it.'@index',
								]
							)// end m.put
						}// end each
						m
					},// end with
					'conditions'    : [].with{ m ->
						rp.'conditions'.'condition'.each{
							m.add([
									'name'           : it.'@name',
									'display-name'   : it.'@display-name',
									'type'           : it.'@type',
									'default-value'  : it.'@default-value',
									'return-type'    : it.'@return-type',
									'index'          : it.'@index',
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
						'js'         : rp.html.js.text()?.trim()?.split(','),
						'script'     : rp.html.script.text(),
						'data-header': rp.html.'data-header'.text(),
						'data-body'  : rp.html.'data-body'.text(),
					],
				]
			)
		}
		return xml_struct
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
			println "sql:${sql}\nqueryConditions:${queryConditions}"
			def columnNames=[]
			def sqlMetaClosure={metaData -> columnNames.addAll(metaData*.columnLabel) }
			if(pageSize>0){
				def countSql='select count(1) from ('+sql+') tmp_count'
				println "countSql:${countSql}"
				if(queryConditions.size()>0){
					ds.eachRow(countSql, queryConditions) { totalCount=it[0] }
				}else{
					ds.eachRow(countSql) { totalCount=it[0] }
				}
				def dialect=org.iff.infra.util.jdbc.dialet.Dialect.getInstanceByUrl(reportCfg.ds.url)
				def limitSql=dialect.getLimitString(sql, currentPage*pageSize, pageSize)
				println "limitSql:${limitSql}"
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
				export=p
			}
		}
		if(export){
			def excel=new ExcelAction()
			excel.metaClass.superAction=this
			excel.index()
		}else{
			def action = new QueryAction()
			action.metaClass.superAction=this
			action.index()
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
		println "--->${report.group.'data-source-ref'}-----${report.ds}"
		report.rttype=reportConfig.'config'.'return-types'
		report.conditions=[]
		report.report?.conditions.each{cdt->
			def htmlWidget=reportConfig.config.'html-widgets'."${cdt.type}"
			if(htmlWidget?.'class'){
				def instance=this.getClass().classLoader.loadClass(htmlWidget.'class',true,false)?.newInstance()
				def widget=instance?.htmlWidget(['name':cdt.name, 'html-widget':htmlWidget])
				def tmp=cdt.clone()
				tmp.put('instance', instance)
				tmp.put('widget', widget)
				report.conditions << tmp
			}//END-if
		}//END-each
		report
	}
	def protected reportGroup(reportConfig, groupName){
		
	}
}

class QueryAction{
	def index(){
		def params=superAction.params
		println "URL PARAMS -> ${params.urlParams}"
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def reportConfig=new OpenReportParser().reportConfig()
		def reportCfg=superAction.reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
		def pmap=superAction.urlParamMap(reportCfg)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		def sqlExecute=reportConfig.processor.query(reportCfg, reportConfig.processor.'data-sources'(reportCfg), pmap)
		def result=sqlExecute.result
		println "-->${sqlExecute.pageSize}-->${sqlExecute.totalCount}"
		def reportParameter={
			table(class:'open-report-params'){
				tr{
					td{
						div(class:'or-cdts'){
							reportCfg.conditions.each{cdt->
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
							}//END-each
						}//END-div
						div(class:'or-cdts-fbtns'){
							div(class:'or-cdts-fbtn search'){
								button('Search', type:'submit', class:'or_submitable')
							}
							div(class:'or-cdts-fbtn export-xls'){
								button('Excel', type:'submit', class:'or_submitable', target:'_blank', href:"export=excel")
							}
						}
					}
				}
			}
		}
		def reportData={
			table(class:'open-report-data'){
				thead(){
					tr(){
						sqlExecute.columnNames.each{name ->
							th{
								div(name)
							}
						}
					}
				}
				tbody(){
					result.each{row ->
						tr(){
							row.each{k, v ->
								td(class:"or-rc-${k}"){
									div(v)
								}
							}
						}
					}
				}
				tfoot(){
					tr(){
						result[0].each{k, v ->
							if(v instanceof Number){
								def total=0
								result.collect{total+=it."${k}"}
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
		def reportPagination={//[result:result, pageSize:pageSize, totalCount:totalCount, currentPage:currentPage]
			def currentPage=sqlExecute.currentPage
			def totalPage=(sqlExecute.pageSize>0?(sqlExecute.totalCount+sqlExecute.pageSize-1)/sqlExecute.pageSize:0) as int
			def url='report'
			url=url.indexOf("?")>0 ? "${url}&" :"${url}?"
			if(totalPage<=1){
				return table()
			}
			currentPage=currentPage<=1 ? 1 : currentPage
			currentPage=currentPage>totalPage ? totalPage : currentPage
			table(class:'open-report-pagination'){
				tr{
					td{
						div{
							a(href:'javascript:;', onclick:'return false', class:'page-btn page-total', "当前记录${sqlExecute.result.size()}条，共 ${sqlExecute.totalCount}条，共${totalPage}页")
							a(href:"p=0", title:'首页', class:'page-btn page-start or_submitable', '<<')
							if(currentPage>4 && (totalPage-currentPage)<1){
								a(href:"p=${currentPage-4}", class:'page-btn or_submitable', "${currentPage-4}")
							}
							if(currentPage>3 && (totalPage-currentPage)<2){
								a(href:"p=${currentPage-3}", class:'page-btn or_submitable', "${currentPage-3}")
							}
							
							if(currentPage>2){
								a(href:"p=${currentPage-2}", class:'page-btn or_submitable', "${currentPage-2}")
							}
							if(currentPage>1){
								a(href:"p=${currentPage-1}", class:'page-btn or_submitable', "${currentPage-1}")
							}
							a(href:"p=${currentPage}", class:'page-btn page-on or_submitable', "${currentPage}")
							if(totalPage-currentPage>0){
								a(href:"p=${currentPage+1}", class:'page-btn or_submitable', "${currentPage+1}")
							}
							if(totalPage-currentPage>1){
								a(href:"p=${currentPage+2}", class:'page-btn or_submitable', "${currentPage+2}")
							}
							
							if(currentPage<3 && (totalPage-currentPage)>2){
								a(href:"p=${currentPage+3}", class:'page-btn or_submitable', "${currentPage+3}")
							}
							if(currentPage<2 && (totalPage-currentPage)>3){
								a(href:"p=${currentPage+4}", class:'page-btn or_submitable', "${currentPage+4}")
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
			}
			body{
				h1("hello")
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
			def onchange="""
				var input1=document.getElementById(\'${paramMap.name}_i\'); var option1=this.options[this.selectedIndex]; input1.value=option1.text;
				var data=option1.getAttribute('data'); var city=(data||\'\').trim().split(','); var select2=document.getElementById(\'${paramMap.name}_2\');
				while(select2.firstChild) {select2.removeChild(select2.firstChild);}
				var html=\'<option></option>\'; for(var i=0;i<city.length;i++){
					var cityIdName=city[i], cIdName=[cityIdName, cityIdName];
					cIdName = cityIdName.indexOf(\':\')>0 ? cIdName=cityIdName.split(\':\') : cIdName;
					html+=(\'<option value=\\'\'+cIdName[0]+\'\\'>\'+cIdName[1]+\'</option>\');
				}
				select2.innerHTML=html; document.getElementById(\'${paramMap.name}_2\').value='';
			"""
			input(type:'hidden', name:"${paramMap.name}_i", id:"${paramMap.name}_i")
			select(name:"${paramMap.name}", id:"${paramMap.name}", class:'or-hw-select province', onchange: onchange){
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
			input(type:'hidden', name:"${paramMap.name}_i2", id:"${paramMap.name}_i2")
			select(name:"${paramMap.name}_2", id:"${paramMap.name}_2", class:'or-hw-select city',
			onchange: "document.getElementById(\'${paramMap.name}_i2\').value=this.options[this.selectedIndex].text"){
				option('')
			}
		}
	}
}
class HWSelect{
	def htmlWidget(paramMap){//name, value
		return {
			input(type:'hidden', name:"${paramMap.name}_i", id:"${paramMap.name}_i", paramMap.value)
			select(name:paramMap.name, id:"${paramMap.name}", class:'or-hw-select',
				onchange:"document.getElementById(\'${paramMap.name}_i\').value=this.options[this.selectedIndex].text"){
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


class ExcelAction{
	def index(){
		def params=superAction.params
		println "URL PARAMS -> ${params.urlParams}"
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def reportConfig=new OpenReportParser().reportConfig()
		def reportCfg=superAction.reportCfg(reportConfig, params.urlParams[0], params.urlParams[1])
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
			}
			commands {
				applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..3, sheetName:'Export2')
				//mergeCells(rows: 1, columns: 1..3)
			}
		}
		def baos=new ByteArrayOutputStream()
		workbook.write(baos)
		response.reset()
		response.addHeader('Content-Disposition', 'attachment;filename='+new String("${reportCfg.report.'display-name'}.xls".getBytes('UTF-8'),'ISO8859-1'))
		response.addHeader('Content-Length', String.valueOf(baos.size()))
		response.setContentType('application/octet-stream')
		def os=response.outputStream
		TCHelper.close(os){ baos.writeTo(os) }
	}
}

