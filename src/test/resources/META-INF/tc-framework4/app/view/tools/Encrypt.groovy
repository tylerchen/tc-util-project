package org.iff.groovy.view.tools

@TCAction(name="/tools/encrypt")
class TemplateGeneratorAction{
	def index(){
		def response=params.response
		response.setContentType("text/html; charset=UTF-8")
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		build.html(xmlns:'http://www.w3.org/1999/xhtml'){
			head{
				meta(charset:'UTF-8')
				meta('http-equiv':'X-UA-Compatible','content':'IE=edge')
				title('Encrypt & Decrypt tools')
			}
			body{
				div(class:'main-content'){
					form(method:'POST', action:"${params.appContext}/${params.target}",enctype:'application/x-www-form-urlencoded'){
						table(class:'content-table'){
							thead{
								tr{
									th('明文' ,class:'content-lh')
									th(''     ,class:'content-mh')
									th('密文' ,class:'content-rh')
								}
							}
							tbody{
								tr{
									td(class:'content-lb'){
										textarea(id:'content-lb-ta', class:'content-lb-ta')
									}
									td(class:'content-mb'){
										divclass:'content-mb-ul'{
											ul{
												li(class:'content-mb-ul-header'){
													span('加密算法')
												}
												['aes', 'des', 'base64', 'md5'].each{name->
													li(class:'content-mb-ul-choice'){
														input(id:name, name:'encrypt_type', value:name, type:'radio')
														span(name.toUpperCase())
													}
												}
												li(class:'content-mb-ul-pwd'){
													span('密码')
													input(id:'pwd', name:'pwd', type:'text')
												}
												li(class:'content-mb-ul-btns'){
													button('加密 >>' , type:'submit', name:'op', value:'encrypt', class:'btn-encrypt-submit')
													button('<< 解密', type:'submit', name:'op', value:'decrypt', class:'btn-decrypt-submit')
												}
											}
										}
									}
									td(class:'content-rb'){
										textarea(id:'content-rb-ta', class:'content-rb-ta')
									}
								}
							}//END-tbody
						}//END-table
					}//END-form
				}//END-div
			}
		}
	}
	def default_public_key(){
		def pmap=urlParamMap()
		if(pmap?.get('encode')=='hex'){
			return org.iff.infra.util.RSAHelper.getDefaultPubKeyHex()
		} else {
			return org.iff.infra.util.RSAHelper.getDefaultPubKeyBase64()
		}
	}
	
	def protected urlParamMap(){
		def pmap=[:]
		params.urlParams.each{p->
			def index=p.indexOf('=')
			if(index>0){
				pmap.put(p.substring(0,index),p.substring(index+1))
			}
		}
		pmap
	}
}

