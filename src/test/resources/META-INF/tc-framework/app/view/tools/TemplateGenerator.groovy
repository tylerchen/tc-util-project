package org.iff.groovy.view.tools

@TCAction(name="/tools/template_generator")
class TemplateGeneratorAction{
	def index(){
		def response=params.response
		response.writer << "<!DOCTYPE html>\n<html><head><meta charset='utf-8'></head><body><h1>TemplateGenerator</h1>"
		response.writer << "</body></html>"
//		TemplateGenerator
//		1、Generate File
//		2、Generate String
//		3、loop
//		4、condition
//		5、Template
	}
}

