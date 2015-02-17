package org.iff.groovy.view.system

import java.util.*
import java.net.*
import java.text.*
import java.util.zip.*
import java.io.*

@TCAction(name="/file_browser")
class FileBrowserAction{
	def config=[restrict_path:TCCache.me().props.tc_file_browser_path ?: '$$']
	def parseRequest(){
		def req
		println "params: ${params}"
		println "params.request.contentType:${params.request.contentType}"
		println "query string:${params.request.queryString}"
		def queryString=params.request.queryString
		def op, parent, path
		queryString?.split("&").each{
			if(it.startsWith('op=')){
				op=it.substring('op='.length())
				op=op?op.replaceAll('\\.\\.',''):''
			}else if(it.startsWith('parent=')){
				parent=it.substring('parent='.length())
				parent=parent?parent.replaceAll('\\.\\.',''):''
			}else if(it.startsWith('path=')){
				path=it.substring('path='.length())
				path=path?path.replaceAll('\\.\\.',''):''
			}
		}
		println "op=${op}, parent=${parent}, path=${path}"
		def currentPath=config.restrict_path+(parent?"/${parent}":'')+(path?"/${path}":'')
		currentPath=org.iff.infra.util.StringHelper.pathBuild(currentPath, '/')
		def currentParent=(parent?"/${parent}":'')+(path?"/${path}":'')
		currentParent=org.iff.infra.util.StringHelper.pathBuild(currentParent, '/')
		
		if(params.request.contentType && params.request.contentType.toLowerCase().contains('multipart/form-data')){
			def policy=org.iff.infra.util.ReflectHelper.getConstructor('com.oreilly.servlet.multipart.DefaultFileRenamePolicy').newInstance()
			req=org.iff.infra.util.ReflectHelper.getConstructor('com.oreilly.servlet.MultipartRequest'
				,'javax.servlet.http.HttpServletRequest'
				,'java.lang.String'
				,'int'
				,'java.lang.String'
				,'com.oreilly.servlet.multipart.FileRenamePolicy').newInstance(params.request, currentPath, 1024*1024, "UTF-8", policy)
		}else{
			req=params.request
		}
		def content=req.getParameter('content')
		params.request.parameterMap.each(){key, value ->
			params.request.setAttribute(key, req.getParameter(key))
		}
		return [request:req, req:req, response:params.response, params:params, currentPath:currentPath, currentParent:currentParent
			, contextPath: params.appContext
			, resContext : params.resContext
			, op:op, parent:parent, path:path, content:content]
	}
	def index(){
		def parse=parseRequest()
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		if(!op){
			list(parse)
		}else{
			if("View"==op){
				view(parse)
			}else if("Edit"==op){
				edit(parse)
			}else if("Delete"==op){
				delete(parse)
			}else if("Save"==op){
				save(parse)
			}else if("Upload"==op){
				upload(parse)
			}else if("Rename"==op){
				rename(parse)
			}else if("Test"==op){
				test(parse)
			}
		}
	}
	def list(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		response.setContentType("text/html; charset=UTF-8")
		response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK)
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head(){
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title("File browser")
				link(type:"text/css", href:"${resContext}/css/browser/browser.css", rel:"stylesheet")
				script('',type:"text/javascript", src:"${resContext}/js/jquery-1.8.2.min.js")
				script(type:"text/javascript",'''
				 	function getSelected(){
				 		var value=[];
						$('.file-list input:checked').each(function(){
							value.push($(this).closest('tr').attr('data'));
						});
						return value;
				 	}
				 	function deleteFile(){
				 		if(confirm('Delete Files: '+(getSelected()||[]).join(','))){
				 			$('#delete-form input[name="content"]').val((getSelected()||[]).join(','));
				 			return true;
				 		}else{
				 			return false;
				 		}
				 	}
				 	function renameFile(){
				 		var select=getSelected()
				 		if(select.length!=1){
				 			alert('Please select one file!');
				 			return false;
				 		}else{
				 			$('#rename-form input[name="currentName"]').val(select[0]);
				 		}
				 		var name=prompt(\'New Name:\', \'\');
				 		if(name){
				 			$('#rename-form input[name="newName"]').val(name);
				 			return true;
				 		}else{
				 			return false;
				 		}
				 	}
				 ''')
			}
			body(){
				h1("File Browser")
				div(class:'file-browser'){
					table(class:'file-list'){
						tr{
							['','Name','Size','Type','Date','',''].each(){th(it)}
						}
						['[..]'].each(){dir ->
							def name=dir
							def parentDir=currentParent?(currentParent.substring(0,currentParent.lastIndexOf('/'))):''
							tr(data:name){
								td{ input(type:'checkbox') }
								td{ a(name,href:"?parent=${parentDir}") }
								['','DIR','','',''].each{ td(it) }
							}
						}
						def folder=getFoler(currentPath)
						folder.dir.each(){dir ->
							def name=dir.name
							tr(data:name){
								td{ input(type:'checkbox') }
								td{ a(name,href:"?parent=${currentParent}&path=${name}") }
								['','DIR',String.format('%tF %<tT',new Date(dir.lastModified())),'',''].each{ td(it) }
							}
						}
						DecimalFormat df = new DecimalFormat("###,##0")
						folder.file.each(){file ->
							def name=file.name
							tr(data:name){
								td{ input(type:'checkbox') }
								td{ a(name,href:"?parent=${currentParent}&path=${name}&op=View", target:'_blank') }
								td(df.format(file.length()))
								td('DIR')
								td(String.format('%tF %<tT',new Date(file.lastModified())))
								td{ a('Download',href:"?parent=${currentParent}&path=${name}&op=Download", target:'_blank') }
								td{ a('Edit',href:"?parent=${currentParent}&path=${name}&op=Edit", target:'_blank') }
							}
						}
					}// end of table
					div(class:'file-op'){
						table(class:'file-optable'){
							tr{
								td{
									button('New Folder', onclick:'var name=prompt(\'Folder Name:\', \'\'); if(name){alert(name)}')
								}
								td{
									button('Move', onclick:'var name=prompt(\'Folder Name:\', \'\'); if(name){alert(name)}')
								}
								td{
									form(method:'POST', action:"?parent=${currentParent}&op=Rename", id:'rename-form',enctype:'application/x-www-form-urlencoded'){
										input(type:'hidden', name:'currentName')
										input(type:'hidden', name:'newName')
										input(type:'submit', value:'Rename', onclick:'return renameFile();')
									}
								}
								td{
									form(method:'POST', action:"?parent=${currentParent}&op=Delete", id:'delete-form',enctype:'application/x-www-form-urlencoded'){
										input(type:'hidden', name:'content')
										input(type:'submit', value:'Delete', onclick:'return deleteFile();')
									}
								}
								td{
									form(method:'POST', action:"?parent=${currentParent}&op=Upload",enctype:'multipart/form-data'){
										input(type:'file', name:'file')
										input(type:'submit', value:'Upload')
									}
								}
							}
						}
					}
				}// end of div
			}
		}
	}
	def getFoler(path){
		File file=new File(path)
		def dirs=[], files=[]
		file.eachDir(){ dirs.add(it) }
		file.eachFile(){
			if(!it.isDirectory()){ 
				files.add(it) 
			}
		}
		println "DIR:${dirs}"
		println "FILE:${files}"
		return ['dir':dirs.sort(),'file':files.sort()]
	}
	def view(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		def contentType='text/plain'
		def fileExt=currentPath.substring(currentPath.toString().lastIndexOf("."))
		if(['.jpg','.jpeg','.png','.gif'].contains(fileExt)){
			contentType=org.iff.infra.util.ContentType.getContentType(currentPath,'text/plain')
		}else if(['.html','.html'].contains(fileExt)){
			contentType=org.iff.infra.util.ContentType.getContentType(currentPath,'text/plain')
		}
		response.setContentType("${contentType}; charset=UTF-8")
		response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK)
		println "setContentType:${contentType}"
		response.outputStream.withWriter('UTF-8'){writer ->
			new File(currentPath).withReader('UTF-8'){ reader ->
				writer << reader
			}
		}
		response.writer.flush()
		response.writer.close()
	}
	def edit(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		//
		def sw = new StringWriter()
		new File(currentPath).withReader('UTF-8'){ reader ->
			sw << reader
		}
		//
		def build = new groovy.xml.MarkupBuilder(response.writer)
		response.writer << "<!DOCTYPE html>"
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head(){
				 meta(charset:"UTF-8")
				 meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				 title("File browser")
				 ['lib/codemirror.css','addon/hint/show-hint.css','addon/display/fullscreen.css','theme/night.css'].each{
					 link(type:"text/css", href:"${resContext}/js/codemirror/${it}", rel:"stylesheet")
				 }
				 ['lib/codemirror.js','mode/css/css.js','addon/hint/show-hint.js','addon/hint/css-hint.js','addon/display/fullscreen.js'].each{
					 script('',type:"text/javascript", src:"${resContext}/js/codemirror/${it}")
				 }
			}
			body(){
				h1("File Browser")
				div(class:'file-browser'){
					form(method:'POST', action:"?parent=${parent}&path=${path}&op=Save",enctype:'application/x-www-form-urlencoded'){
						textarea(sw.toString(), name:'content', id:'content', cols:140, rows:35)
						div{
							input(type:'submit', value:'Save', id:'save')
							input(type:'button', value:'Cancel', onclick:'confirm(\'Discard Save?\')&&window.close()')
						}
					}
				}
				script('''
					var myCodeMirror = CodeMirror.fromTextArea(document.getElementById(\'content\'),
						{lineNumbers: true
							,mode: "text/css"
							,matchBrackets:true
							,fullScreen: true
							,theme: "night"
							,extraKeys: {
								 "Alt-/": "autocomplete"
								,"Ctrl-S": function(cm){document.getElementById('save').click();}
								,"F11": function(cm) {cm.setOption("fullScreen", !cm.getOption("fullScreen"));}
								,"Esc": function(cm) {
									if (cm.getOption("fullScreen")){ cm.setOption("fullScreen", false);}
								}
							}
						});
					''',type:"text/javascript")
			}
		}
	}
	def save(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		println "content:${content}"
		new File(currentPath).withWriter('UTF-8'){ writer ->
			writer << content
		}
		response.sendRedirect("${contextPath}/file_browser?parent=${parent}&path=${path}&op=Edit")
	}
	def upload(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		Enumeration files = request.getFileNames()
		while (files.hasMoreElements()) {
			String name = files.nextElement()
			String filesystemName = request.getFilesystemName(name)
			// 文件没有上传则不生成 UploadFile, 这与 cos的解决方案不一样
			if (filesystemName) {
				String originalFileName = request.getOriginalFileName(name)
				String contentType = request.getContentType(name)
				println "name:${name}, saveDirectory:${currentPath}, filesystemName:${filesystemName}, originalFileName:${originalFileName}, contentType:${contentType}"
			}
		}
		response.sendRedirect("${contextPath}/file_browser?parent=${parent}")
	}
	def delete(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		content?.split(",").each{
			def fileName=config.restrict_path+(parent?"/${parent}":'')+(it?"/${it}":'')
			fileName=org.iff.infra.util.StringHelper.pathBuild(fileName, '/')
			println "fileName---->${fileName}"
			def file=new File(fileName)
			if(file.exists() && !file.isDirectory()){
				file.delete()
			}
		}
		response.sendRedirect("${contextPath}/file_browser?parent=${parent}")
	}
	def rename(parse){
		def request=parse.request, req=parse.req, response=parse.response, currentPath=parse.currentPath, currentParent=parse.currentParent
		def contextPath=parse.contextPath, resContext=parse.resContext, op=parse.op, parent=parse.parent, path=parse.path, content=parse.content
		def currentName=request.getParameter('currentName'), newName=request.getParameter('newName')
		currentName=config.restrict_path+(parent?"/${parent}":'')+(currentName?"/${currentName}":'')
		currentName=org.iff.infra.util.StringHelper.pathBuild(currentName, '/')
		println "currentName---->${currentName}"
		def file=new File(currentName)
		if(newName && file.exists() && !file.isDirectory()){
			def fileName=config.restrict_path+(parent?"/${parent}":'')+(newName?"/${newName}":'')
			fileName=org.iff.infra.util.StringHelper.pathBuild(fileName, '/')
			println "newName---->${newName}"
			def renameTo=new File(fileName)
			if(!renameTo.exists()){
				file.renameTo(fileName)
			}
		}
		response.sendRedirect("${contextPath}/file_browser?parent=${parent}")
	}
}
