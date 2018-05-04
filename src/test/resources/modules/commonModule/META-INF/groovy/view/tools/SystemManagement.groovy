package org.iff.groovy.view.tools

@TCAction(name="/tools/system_management")
class SystemManagementAction{
	def index(){
		def request=params.request, response=params.response
		def contextPath=params.appContext, actionContext=params.actionContext
		response.setContentType("text/html; charset=UTF-8")
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head{
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title("System Management")
			}
			body{
				h1("System Management")
				div(class:'system-management'){
					table(class:'module-list'){
						tr{
							['Module','Operation'].each{th(it)}
						}
						org.iff.infra.util.moduler.TCModuleManager.me().getModules().each{moduleName->
							tr{
								td(moduleName)
								td{
									a('Reload', href:"${contextPath}/${actionContext}/reload/${moduleName}")
								}
							}
						}
					}
				}// end of div
			}
		}
	}
	def reload(){
		def contextPath=params.appContext, actionContext=params.actionContext
		def urlParams=params.urlParams
		println urlParams
		if(urlParams&&urlParams[0]){
			org.iff.infra.util.moduler.TCModuleManager.me().reload(urlParams[0])
		}
		redirect("${contextPath}/${actionContext}")
	}
}
