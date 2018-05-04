package org.iff.groovy.view.component

@TCBean(name='TC_COM_PAGE_FORM')
class TCFormPage{
	/** include: title, field, buttons **/
	def simple(options/*{super_action}*/){
		def server_xmls='file:///media/新加卷/workspace/JeeGalileo/tc-util-project2/src/test/resources/META-INF/form'
		def xmls=[]
		server_xmls.each{file->
			xmls.addAll(org.iff.infra.util.ResourceHelper.loadResources(file, '.xml', '*', null))
		}
		def map=TCXmlJsonDB.parse_xml_to_map(null, xmls)
		def params=options.super_action.params
		def response=params.response, contextPath=params.request.contextPath, resContext=params.resContext
		def url_params=params.urlParams
		def build = new groovy.xml.MarkupBuilder(response.writer)
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head{
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title(options.title)
				map?.pages?."page@${url_params[0]}"?.html?.css.each{
					link(type:"text/css", href:"${resContext}/${it}", rel:"stylesheet")
				}
				map?.pages?."page@${url_params[0]}"?.html?.js.each{
					script('',type:"text/javascript", src:"${resContext}/${it}")
				}
				style(type:'text/css', map?.pages?."page@${url_params[0]}"?.html?.style ?: '')
			}
			body{
				div(class:'tc-page-header'){
					h1(map?.pages?."page@${url_params[0]}"?.'display-name')
				}
				div(class:'tc-page-content'){
					form(class:'tc-page-form', onsubmit:'return false', id: map?.pages?."page@${url_params[0]}"?.name){
					}//END-form
				}
			}
			script(type:"text/javascript", map?.pages?."page@${url_params[0]}"?.html?.script ?: '')
		}
	}
}



