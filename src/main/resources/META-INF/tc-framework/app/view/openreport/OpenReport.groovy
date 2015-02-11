package org.iff.groovy.module.openreport

@TCAction(name="/open_report")
class OpenReportParser{
	def index(){
		def response=params.response
		response.writer << "<!DOCTYPE html>\n<html><head></head><body><h1>Open Report</h1></body></html>"
	}
	def parseXml(){
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
		def writer=params.response.writer
		writer << "<!DOCTYPE html>\n<html><head></head><body><h1>Open Report</h1><br/>"
		def xml=new XmlParser().parse("${TCCache.me().cache().app_root}/webapp/open-report.xml")
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
			xml_struct.'data-sources'.put(
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
			xml_struct.'config'.'html-widgets'.put(it.'@name', it.text())
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
		writer << "${xml_struct}<br/>"
		writer << "</body></html>"
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
	def query(){
		def db = [url:'jdbc:mysql://localhost:3306/foodmart?useUnicode=true&characterEncoding=utf8', user:'iff', password:'iff', driver:'com.mysql.jdbc.Driver']
		def sql = groovy.sql.Sql.newInstance(db.url, db.user, db.password, db.driver)
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

class ReturnTypes{
	def static ReturnTypes me=new ReturnTypes()
	def ReturnTypes(){
		def openReport=TCCache.me().cache().open_report
		if(!openReport){
			TCCache.me().cache().put('open_report',[:])
		}
	}
	def static me(){
		return me
	}
	def registe(name, type){
		def returnType=TCCache.me().cache().open_report.return_type
		if(!returnType){
			TCCache.me().cache().open_report.put('return_type',[:])
		}
		returnType.put(name,type)
	}
	def unRegiste(name){
		def returnType=TCCache.me().cache().open_report.return_type
		if(returnType){
			returnType.remove(name)
		}
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

class HtmlWidgets{
	def static HtmlWidgets me=new HtmlWidgets()
	def HtmlWidgets(){
		def openReport=TCCache.me().cache().open_report
		if(!openReport){
			TCCache.me().cache().put('open_report',[:])
		}
	}
	def static me(){
		return me
	}
	def registe(name, type){
		def htmlWidget=TCCache.me().cache().open_report.html_widget
		if(!htmlWidget){
			TCCache.me().cache().open_report.put('html_widget',[:])
		}
		htmlWidget.put(name,type)
	}
	def unRegiste(name){
		def htmlWidget=TCCache.me().cache().open_report.html_widget
		if(htmlWidget){
			htmlWidget.remove(name)
		}
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
	def static area='''
		1100:北京,1101:大兴区,怀柔区,密云县,平谷区,顺义区,通州区,延庆县,房山区,昌平区,门头沟区,东城区,西城区,宣武区,崇文区,朝阳区,海淀区,丰台区,石景山区;
		上海,闸北区,金山区,普陀区,静安区,长宁区,徐汇区,卢湾区,南市区,黄浦区,虹口区,杨浦区,崇明县,南汇县,奉贤县,青浦县,浦东新区,松江区,嘉定区,闵行区,宝山区;
		天津,西青区,津南区,北辰区,武清区,宝坻区,蓟 县,宁河县,静海县,东丽区,大港区,和平区,河东区,河西区,河北区,南开区,红桥区,塘沽区,汉沽区;
		重庆,南岸区,北碚区,万盛区,双桥区,渝北区,巴南区,长寿区,九龙坡区,沙坪坝区,永川市,黔江区,涪陵区,万洲区,渝中区,大渡口区,江北区;
		黑龙江,大兴安岭地区,绥化,黑河,七台河,佳木斯,伊春,大庆,鸡西,双鸭山,鹤岗,牡丹江,齐齐哈尔,哈尔滨;
		吉林,通化,辽源,四平,吉林,长春,白山,松原,白城,延边朝鲜族自治州,高新,延吉,梅河口;
		辽宁,盘锦,阜新,辽阳,铁岭,朝阳,瓦房店,营口,葫芦岛,丹东,沈阳,大连,锦州,鞍山,抚顺,本溪;
		内蒙古,阿拉善盟,兴安盟,巴彦淖尔,呼伦贝尔,集宁,乌兰浩特,锡林浩特,巴彦淖尔盟,锡林郭勒盟,呼和浩特,包头,乌海,赤峰,通辽,鄂尔多斯,乌兰察布盟;
		宁夏,固原,吴忠,石嘴山,银川;
		新疆,阿克苏,昌吉,哈密,和田,喀什,克拉马依,库尔勒,石河子,吐鲁番,伊犁哈萨克自治州,博尔塔拉蒙古自治州,昌吉回族自治州,乌鲁木齐,克拉玛依,吐鲁番地区,哈密地区,和田地区,阿克苏地区,喀什地区,克孜勒苏柯尔克孜自治州,巴音郭楞蒙古自治州,乌市,奎屯,伊犁,伊宁;
		青海,格尔木,海西蒙古族藏族自治州,玉树藏族自治州,果洛藏族自治州,海南藏族自治州,黄南藏族自治州,海北藏族自治州,海东地区,西宁;
		甘肃,庆阳,定西地区,陇南地区,甘南藏族自治州,临夏回族自治州,嘉峪,武威,酒泉,平凉,兰州,天水,金昌,白银,嘉峪关,张掖;
		陕西,韩城,商洛,安康,榆林,汉中,渭南,咸阳,铜川,延安,宝鸡,西安;
		河北,廊坊,衡水,霸州,青县,任丘,涿州,沧州,承德,张家口,石家庄,保定,唐山,秦皇岛,邯郸,邢台;
		河南,安阳,新乡,鹤壁,焦作,平顶山,开封,洛阳,郑州,濮阳,许昌,驻马店,周口,信阳,商丘,南阳,三门峡,漯河;
		山东,林沂,莱州,胶南,即墨,淮坊,荷泽,临忻,龙口,蓬莱,青州,乳山,寿光,滕州,文登,招远,高密,菏泽,滨州,济南,青岛,烟台,淄博,枣庄,东营,潍坊,威海,济宁,聊城,临沂,德州,莱芜,日照,泰安;
		山西,运城,吕梁地区,河津,侯马,孝义,榆次,临汾,晋中,忻州,太原,大同,朔州,阳泉,长治,晋城;
		湖北,宜昌,荆州,十堰,襄樊,黄石,武汉,荆门,鄂州,株州,仙桃,潜江,汉阳,汉口,恩施,安陆,恩施土家族苗族自治州,随州,咸宁,黄冈,孝感;
		湖南,郴州,永州,怀化,娄底,湘西土家族苗族自治州,株州,邵东,益阳,张家界,长沙,株洲,湘潭,衡阳,邵阳,岳阳,常德;
		安徽,宿州,巢湖,六安,亳州,池州,宣城,蒙城,宁国,桐城,阜阳,滁州,合肥,芜湖,蚌埠,淮南,马鞍山,淮北,铜陵,安庆,黄山;
		江苏,南通,镇江,常州,无锡,苏州,泰州,扬州,盐城,宿迁,淮安,连云港,徐州,南京,常熟,丹阳,张家港,宜兴,吴县,吴江,泰州华,太仓,溧阳,昆山,靖江,江阴,江都,海门;
		浙江,上虞,瑞安,平湖,临海,临安,嵊州,温岭,义乌,永康,余姚,诸暨,新昌,乐清,奉化,东阳,杭州,宁波,温州,嘉兴,湖州,绍兴,金华,衢州,舟山,台州,丽水,慈溪;
		江西,吉安,宜春,抚州,上饶,高安,赣州,鹰潭,南昌,景德镇,萍乡,新余,九江;
		广东,东莞,汕尾,惠州,梅州,河源,韶关,汕头,珠海,深圳,广州,中山,江门,市梅,增城,台山,顺德,南海,开平,花都,云浮,揭阳,佛山,阳江,湛江,茂名,肇庆,清远,潮州;
		广西,玉林,百色,贺州,河池,来宾,崇左,贵港,钦州,南宁,柳州,桂林,梧州,北海,防城港;
		福建,福清,建瓯,晋江,南安,邵武,石狮,仙游,宁德,龙岩,福州,厦门,三明,莆田,泉州,漳州,南平;
		四川,乐山,南充,宜宾,广安,达州,巴中,内江,遂宁,广元,绵阳,德阳,泸州,攀枝花,自贡,成都,雅安,眉山,西昌,锦阳,广汉,凉山彝族自治州,甘孜藏族自治州,阿坝藏族羌族自治州,资阳;
		云南,西双版纳傣族自治州,楚雄彝族自治州,大理白族自治州,德宏傣族景颇族自治州,怒江傈傈族自治州,迪庆藏族自治州,大理,红河哈尼族彝族自治州,文山壮族苗族自治州,丽江,昆明,曲靖,玉溪,保山,昭通,思茅地区,临沧地区;
		贵州,铜仁,凯里,贵恙,都匀,黔南布依族苗族自治州,黔东南苗族侗族自治州,黔西南布依族苗族自治州,毕节地区,铜仁地区,安顺,遵义,六盘水,贵阳;
		西藏,林芝地区,阿里地区,日喀则地区,山南地区,昌都地区,那曲地区,拉萨;
		海南,海口,三亚;
		香港,香港;
		澳门,澳门;
		台湾,台北,高雄,台中,台南,基隆,新竹;
	'''
	def test(pname){
		def province=area.split(";").collect{it.trim()}.findAll{it.startsWith(pname)}
		if(province && province.size()==1){
			def pcity=province[0].split(",")
			println "Province:${pcity[0]}, City:${pcity[1..-1].join(',')}"
		}
	}
	def htmlWidget(paramMap){//name, value
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
				HWCN2Select.area.split(";").collect{it.trim().split(",")}.findAll{it.size()>1}.each{
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
			input(type:'hidden', name:"cp_selecttext_${paramMap.name}", id:"cp_selecttext_${paramMap.name}", paramMap.value)
			select(name:paramMap.name, id:"cp_${paramMap.name}", class:'or-hw-select', onchange:'''
				document.getElementById(${"'cp_selecttext_"+paramMap.name+"'"}).value=this.options[this.selectedIndex].text
			'''){
				mpk.yieldUnescaped(paramMap.html)
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
		response.setContentType("text/html; charset=UTF-8")
		response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		def result=new SqlQueryUtil().query()
		def reportParameter={
			table(class:'open-report-params'){
				tr(){
					td{
						def widget=new HWCN2Select().htmlWidget([name:'test', value:'test'])
						widget.delegate=delegate
						widget()
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
		new HWCN2Select().test("广东")
		//
		def rs=getClass().getClassLoader().getResources("META-INF/maven")
		while(rs.hasMoreElements()){
			def url=rs.nextElement()
			def path=url.getFile(), protocol=url.getProtocol()
			if (protocol in ['file','jar']) {
				println path
				File file = new File(path.substring(path.indexOf("file:") + 5, path.lastIndexOf("!/")))
				java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file)
				def entries=jarFile.entries()
				while(entries.hasMoreElements()){
					def entry=entries.nextElement()
					//println entry.name
				}
			}
		}
	}
}



