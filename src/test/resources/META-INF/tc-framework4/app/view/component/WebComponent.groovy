package org.iff.groovy.view.component

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
	def returnType(value){
		return value? value.split(',') : []
	}
}
@TCBean(name='TC_COM_HW_html')
class HWHtml{
	def htmlWidget(paramMap){
		return { mkp.yieldUnescaped(paramMap.'html-widget'.html) }
	}
}
@TCBean(name='TC_COM_HW_blank')
class HWBlank{
	def htmlWidget(paramMap){
		return { mkp.yieldUnescaped('&nbsp;') }
	}
}
@TCBean(name='TC_COM_HW_hidden')
class HWHidden{
	def htmlWidget(paramMap){//name, value
		return { input(type:'hidden', name:paramMap.name, value:paramMap.value, id:paramMap.name) }
	}
}
@TCBean(name='TC_COM_HW_script')
class HWScript{
	def htmlWidget(paramMap){//name, value
		return { script(type:'text/javascript',paramMap.value) }
	}
}
@TCBean(name='TC_COM_HW_text')
class HWText{
	def htmlWidget(paramMap){//name, value
		return { input(type:'text', name:paramMap.name, value:paramMap.value, id:paramMap.name) }
	}
}
@TCBean(name='TC_COM_HW_cn2select')
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
@TCBean(name='TC_COM_HW_select')
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
@TCBean(name='TC_COM_HW_mselect')
class HWMSelect{
	def htmlWidget(paramMap){//name, value
		return {
			select(name:paramMap.name, id:"${paramMap.name}", class:'or-hw-mselect', multiple:'multiple'){
				mkp.yieldUnescaped(paramMap.'html-widget'.html)
			}
		}
	}
}

