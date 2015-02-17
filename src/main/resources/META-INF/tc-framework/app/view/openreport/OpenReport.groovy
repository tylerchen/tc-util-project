package org.iff.groovy.module.openreport

@TCAction(name="/open_report")
class OpenReportParser{
	def index(){
		def response=params.response
		response.writer << "<!DOCTYPE html>\n<html><head><meta charset='utf-8'></head><body><h1>Open Report</h1>"
		def reportFiles=org.iff.infra.util.ResourceHelper.loadResourcesInFileSystem('g:/bak/app_root/webapp/open-report', '.xml', 'open-report-*.xml', '')
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
		]
		reportFiles.each{
			parseXml(xml_struct, it)
		}
		return xml_struct
	}
	def parseXml(xml_struct, report_xml){
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
					'display-name'  : rp.driver.text(), 
					'page-size'     : rp.url.text(),
					'condition-size': rp.url.text(),
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
				]
			)
		}
		return xml_struct
	}
}

@TCAction(name="/report")
class OpenReportAction{
	def index(){
		// def response=params.response
		// response.writer << "<!DOCTYPE html>\n<html><head></head><body><h1>Open Report 1</h1></body></html>"
		// get report and config by report name
		// get action by operation and execute
		new ExcelAction().index()
		def action = new QueryAction()
		action.metaClass.params=params
		action.index()
		return
	}
}

class SqlQueryUtil{
	def query(driver,url,user,password){
		def sql = groovy.sql.Sql.newInstance(url, user, password, driver)
		def result = sql.rows('select brand_name,sku,srp,gross_weight,net_weight from product where product_class_id=:product_class_id',[product_class_id:30])
		//def result = sql.rows('select product_name,sku,srp,gross_weight,net_weight from product')
		def total=0
		result.collect{total+=it.srp}
		println total
		def tutorial='''
		Or insert a row using JDBC PreparedStatement inspired syntax:
		 def params = [10, 'Groovy', 'http://groovy.codehaus.org']
		 sql.execute 'insert into PROJECT (id, name, url) values (?, ?, ?)', params
		
		Or insert a row using GString syntax:
		 def map = [id:20, name:'Grails', url:'http://grails.codehaus.org']
		 sql.execute "insert into PROJECT (id, name, url) values ($map.id, $map.name, $map.url)"
		
		Or a row update:
		 def newUrl = 'http://grails.org'
		 def project = 'Grails'
		 sql.executeUpdate "update PROJECT set url=$newUrl where name=$project"
		
		Now try a query using eachRow:
		
		 println 'Some GR8 projects:'
		 sql.eachRow('select * from PROJECT') { row ->
			 println "${row.name.padRight(10)} ($row.url)"
		 }
		 
		 // using rows() with a named parameter with the parameter supplied in a map
		 println sql.rows('select * from PROJECT where name=:foo', [foo:'Gradle'])
		 // as above for eachRow()
		 sql.eachRow('select * from PROJECT where name=:foo', [foo:'Gradle']) {
		     // process row
		 }
		
		 // an example using both the ':' and '?.' variants of the notation
		 println sql.rows('select * from PROJECT where name=:foo and id=?.bar', [foo:'Gradle', bar:40])
		 // as above but using Groovy's named arguments instead of an explicit map
		 println sql.rows('select * from PROJECT where name=:foo and id=?.bar', foo:'Gradle', bar:40)
		
		 // an example showing rows() with a domain object instead of a map
		 class MyDomainClass { def baz = 'Griffon' }
		 println sql.rows('select * from PROJECT where name=?.baz', new MyDomainClass())
		 // as above for eachRow() with the domain object supplied in a list
		 sql.eachRow('select * from PROJECT where name=?.baz', [new MyDomainClass()]) {
		     // process row
		 }
		DataSource ds = new org.hsqldb.jdbc.jdbcDataSource()
        ds.database = "jdbc:hsqldb:mem:foo" + getMethodName()
        ds.user = 'sa'
        ds.password = ''
        return new Sql(ds)
		'''
		return result
	}
}

class RTString{
	def returnType(value){
		return value? value.toString() : ''
	}
}
class RTLike{
	def returnType(value){
		return value? "%${value}%" : ''
	}
}
class RTDate{
	def returnType(value){
		return org.apache.commons.lang3.time.DateUtils.parseDate(value, 'yyyy-MM-dd HH:mm:ss','yyyy-MM-dd','yyyy/MM/dd HH:mm:ss','yyyy/MM/dd')
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
		return { input(type:'hidden', name:paramMap.name, value:paramMap.value, id:"cp_${paramMap.name}") }
	}
}
class HWScript{
	def htmlWidget(paramMap){//name, value
		return { script(type:'text/javascript',paramMap.value) }
	}
}
class HWText{
	def htmlWidget(paramMap){//name, value
		return { input(type:'text', name:paramMap.name, value:paramMap.value, id:"cp_${paramMap.name}") }
	}
}
class HWCN2Select{
	def htmlWidget(paramMap){//name, value
		def area=paramMap.'html-widget'.data
		return {
			def onchange="""
				var input1=document.getElementById(\'cp_st1_${paramMap.name}\'); var option1=this.options[this.selectedIndex]; input1.value=option1.text;
				var data=option1.getAttribute('data'); var city=(data||\'\').trim().split(','); var select2=document.getElementById(\'cp_2_${paramMap.name}\');
				while(select2.firstChild) {select2.removeChild(select2.firstChild);}
				var html=\'<option></option>\'; for(var i=0;i<city.length;i++){
					var cityIdName=city[i], cIdName=[cityIdName, cityIdName];
					cIdName = cityIdName.indexOf(\':\')>0 ? cIdName=cityIdName.split(\':\') : cIdName;
					html+=(\'<option value=\\'\'+cIdName[0]+\'\\'>\'+cIdName[1]+\'</option>\');
				}
				select2.innerHTML=html; document.getElementById(\'cp_st2_${paramMap.name}\').value='';
			"""
			input(type:'hidden', name:"cp_st1_${paramMap.name}", id:"cp_st1_${paramMap.name}")
			select(name:"cp_1_${paramMap.name}", id:"cp_1_${paramMap.name}", class:'or-hw-select province', onchange: onchange){
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
			input(type:'hidden', name:"cp_st2_${paramMap.name}", id:"cp_st2_${paramMap.name}")
			select(name:"cp_2_${paramMap.name}", id:"cp_2_${paramMap.name}", class:'or-hw-select city', 
			onchange: "document.getElementById(\'cp_st2_${paramMap.name}\').value=this.options[this.selectedIndex].text"){
				option('')
			}
		}
	}
}
class HWSelect{
	def htmlWidget(paramMap){//name, value
		return {
			input(type:'hidden', name:"cp_sti_${paramMap.name}", id:"cp_sti_${paramMap.name}", paramMap.value)
			select(name:paramMap.name, id:"cp_${paramMap.name}", class:'or-hw-select', 
				onchange:"document.getElementById(\'cp_sti_${paramMap.name}\').value=this.options[this.selectedIndex].text"){
				mkp.yieldUnescaped(paramMap.'html-widget'.html)
			}
		}
	}
}


class ExcelAction{
	def index(){
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
					header(["Column1", "Column2", "Column3"])
					row(["a", "b", "c"])
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
		new File("G:/test.xls").withOutputStream{os ->
			workbook.write(os)
		}
	}
}

class QueryAction{
	def index(){
		println "URL PARAMS -> ${params.urlParams}"
		def response=params.response
		def contextPath=params.request.contextPath
		def reportConfig=new OpenReportParser().reportConfig()
		//'report-groups':[:],//name:{name,path,skin,display-name,data-source-ref,report-refs:{name,role,skin,display-name},role-refs:{name}}
		def reportGroup=reportConfig.'report-groups'?."${params.urlParams[0]}"
		def reportXml=reportConfig.'reports'?."${params.urlParams[1]}"
		println reportConfig
		println reportGroup
		println reportXml
		println reportXml?.conditions
		//
		response.setContentType("text/html; charset=UTF-8")
		response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		def mysql=reportConfig.'data-sources'.mysql
		def result=new SqlQueryUtil().query(mysql.driver,mysql.url,mysql.username,mysql.password)
		def reportParameter={
			table(class:'open-report-params'){
				tr(){
					td{
						form(class:'or-cdts-form', onsubmit:'return false'){
							div(class:'or-cdts'){
								reportXml?.conditions.each{cdt->
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
							div(class:'or-cdts-fbtn'){
								div(class:'or-cdts-fbtn search'){
									button('Search', type:'submit', onclick:'alert($(this).closest(\'form\').serialize())')
								}
								div(class:'or-cdts-fbtn export-xls'){
									button(type:'submit','Excel')
								}
							}
						}//END-form
					}
				}
			}
		}
		def reportData={
			table(class:'open-report-data'){
				thead(){
					tr(){
						result[0].each{k, v ->
							th{
								div(k)
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
		def reportPagination={
			def currentPage=0
			def totalPage=3
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
							a(href:'javascript:;', onclick:'return false', class:'page-btn page-total', "记录 100 条，共${totalPage}页")
							a(href:"${url}", title:'首页', class:'page-btn page-start', '<<')
							if(currentPage>4 && (totalPage-currentPage)<1){
								a(href:"${url}page_no=${currentPage-4}", class:'page-btn', "${currentPage-4}")
							}
							if(currentPage>3 && (totalPage-currentPage)<2){
								a(href:"${url}page_no=${currentPage-3}", class:'page-btn', "${currentPage-3}")
							}
							
							if(currentPage>2){
								a(href:"${url}page_no=${currentPage-2}", class:'page-btn', "${currentPage-2}")
							}
							if(currentPage>1){
								a(href:"${url}page_no=${currentPage-1}", class:'page-btn', "${currentPage-1}")
							}
							a(href:"${url}page_no=${currentPage}", class:'page-btn page-on', "${currentPage}")
							if(totalPage-currentPage>0){
								a(href:"${url}page_no=${currentPage+1}", class:'page-btn', "${currentPage+1}")
							}
							if(totalPage-currentPage>1){
								a(href:"${url}page_no=${currentPage+2}", class:'page-btn', "${currentPage+2}")
							}
							
							if(currentPage<3 && (totalPage-currentPage)>2){
								a(href:"${url}page_no=${currentPage+3}", class:'page-btn', "${currentPage+3}")
							}
							if(currentPage<2 && (totalPage-currentPage)>3){
								a(href:"${url}page_no=${currentPage+4}", class:'page-btn', "${currentPage+4}")
							}
							a(href:"${url}page_no=${totalPage}", title:'末页', class:'page-btn page-end', '>>')
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
				link(type:"text/css", href:"${contextPath}/css/browser/browser.css", rel:"stylesheet")
				script('',type:"text/javascript", src:"${contextPath}/js/jquery-1.8.2.min.js")
			}
			body(){
				h1("hello")
				div(class:'open-report-content'){
					reportParameter.delegate=delegate
					reportParameter()
					reportData.delegate=delegate
					reportData()
					reportPagination.delegate=delegate
					reportPagination()
					mkp.yieldUnescaped("<h1>yieldUnescaped</h1>"+Class.forName("java.util.Date").newInstance())
				}
			}
		}
	}
}



