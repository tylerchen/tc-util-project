package org.iff.groovy.framework

import java.nio.file.*
import java.nio.file.attribute.*
import java.util.concurrent.*

class TCCache{
	private static final TCCache _me=new TCCache()
	private def Map _cache=init()
	private TCCache(){
	}
	def static init(){
		def cache=[:]
		cache.putAll([
			'framework_instance':[:],
			'action_instance':[:],
			'anno_class':['action':[:],'framework':[:]],
			'groovy_files':[:],
			'props':[:],
			'framework':[	'inits':[/*{instance,method}*/],
							'starts':[/*{instance,method}*/],
							'stops':[/*{instance,method}*/],
							'action_handlers':[],
							'actions':[:]/*{url:{action,context,method}}*/],
			'servlet':[:]
		])
		return cache
	}
	def static me(){
		return _me
	}
	def cache(){
		return _cache
	}
}
class TCStarter{
	def TCStarter(){
		TCCache.me().cache().framework_instance.TCStarter=this
	}
	def start(groovyFiles){
		//
		system_properties_to_map()
		//
		scan_groovy_file(groovyFiles)
		//
		compile_by_order()
		//
		start_folder_listener()
		//
		start_server()
		//
	}
	def pathClean(path){
		return path ? TCHelper.pathClean(path.toString()+'/').replaceAll('^/|/$','') : null
	}
	def system_properties_to_map(){
		def map=TCHelper.prop2map(System.getProperties()){true}
		TCCache.me().cache().with{
			get('props').putAll(map)
			put("app_root", pathClean(map.app_root))
		}
	}
	def scan_groovy_file(groovyFiles){
		def groovy_files=TCCache.me().cache().groovy_files
		groovyFiles.each{name, path ->
			if(name.endsWith('.groovy')){
				name=pathClean(name)
			}
			def files=groovy_files.get(name)
			def paths=(TCHelper.isObjects(path)?path:[path]) as List
			if(!files){
				groovy_files.put(name, paths)
			}else{
				files.addAll(paths)
			}
		}
		def app_root=TCCache.me().cache().app_root
		if(!app_root || app_root.size()<1){
			return
		}
		def rootPath=Paths.get(app_root)
		if(!rootPath?.toFile()?.exists()){
			return
		}
		def files=scan_groovy_files_from_filesys(app_root,['app','system'])
		groovy_files.putAll(files)
	}
	def compile_by_order(){
		def groovy_files=TCCache.me().cache().groovy_files.sort()
		def app_root=pathClean(TCCache.me().cache().app_root)
		TCHelper.debug('Compile By Order: {0}',groovy_files)
		def order=["system/util","system/framework","app/model","app/service","app/view"]
		order.each{ path ->
			TCHelper.debug('Compile Path: {0}/{1}',app_root,path)
			def errorCompile=[]
			def errorTimes=100
			groovy_files.each{pathContext, files ->
				pathContext=pathClean(pathContext)
				if(pathContext.startsWith(path)){
					files.each{ file ->
						TCHelper.debug('Compile File: {0}',file)
						if(!file.endsWith("/system/framework/TC.groovy")){
							def loader=TCCLassManager.me().compile(file)
							if(loader.isLastCompileSuccess()){
								parse_add_class(loader.loadedClasses)
							}else{
								TCHelper.warn('Compile File Error: {0}',file)
								errorCompile << file
							}
						}
					}
				}
			}// END-each:groovy_files
			for(;errorTimes>0;errorTimes--){
				if(errorCompile.isEmpty()){
					break
				}
				def errorFiles=errorCompile.clone()
				errorCompile.clear()
				errorFiles.each{file ->
					try{
						TCHelper.debug('Recompile File: {0}',file)
						def loader=TCCLassManager.me().compile(file)
						parse_add_class(loader.loadedClasses)
					}catch(err){
						TCHelper.warn('Recompile File Error: {0}',file)
						errorCompile << file
					}
				}
			}
		}// END-each:order
		parse_process()
	}
	def start_folder_listener(){
		def app_root=pathClean(TCCache.me().cache().app_root)
		if(!app_root || app_root.size()<1){
			return
		}
		def rootPath=Paths.get(app_root)
		if(!rootPath?.toFile()?.exists()){
			return
		}
		def changeListener=new Object(){
			def WatchEvent.Kind[] eventTypes=[StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY]//
			def events=[:]
			def getWatchEventKinds() {
				eventTypes
			}
			def addEvent(WatchEvent anEvent, String path){
				events.put(pathClean(path), anEvent)
			}
			def processEvent(){
				events.each{ k,v ->
					onEvent(v, k)
				}
				if(!events.isEmpty()){
					parse_framework_process()
				}
				events.clear()
			}
			def onEvent(WatchEvent anEvent, String path){
				def file=pathClean(path)
				TCHelper.debug('Reload Groovy: {0}, {1}',anEvent.kind().name(), file)
				def systemPath=pathClean(TCCache.me().cache().app_root+'/system')+'/'
				if(file.startsWith(systemPath)){
					TCHelper.warn('RESTART FRAMEWORK!!!..........')
				}
				if(anEvent.kind()==StandardWatchEventKinds.ENTRY_DELETE){
					TCCLassManager.me().removeClassNameScriptMapping(file)
				}else{
					def loader=TCCLassManager.me().compile(file)
					parse_add_class(loader.loadedClasses)
					TCHelper.debug('Recompile Classes: {0}',loader.loadedClasses)
				}
			}
		}
		folder_add_listener(rootPath, changeListener)
	}
	def start_server(){
		println "-------------------> start_server"
		println "-------------------> ${TCCache.me().cache().framework_instance}"
		println "-------------------> ${TCCache.me().cache().anno_class}"
		def instance=TCCache.me().cache().framework_instance
		if(instance.TCMain){
			instance.TCMain.main()
		}
	}
	def scan_groovy_files_from_jar(path){
		def files=[:]
		path=pathClean(path)
		def rs=getClass().getClassLoader().getResources(path)
		while(rs.hasMoreElements()){
			def url=rs.nextElement()
			def filePath=url.getFile(), protocol=url.getProtocol()
			if (protocol in ['file','jar']) {// path will starts with file:/
				TCHelper.debug('JAR: {0}',path)
				def file = new File(filePath.substring('file:'.size(), filePath.lastIndexOf("!/")))
				def jarFile = new java.util.jar.JarFile(file)
				def entries=jarFile.entries()
				while(entries.hasMoreElements()){
					def entry=entries.nextElement()
					def entryName=pathClean(entry.name)
					if (entryName.startsWith(path) && entryName.endsWith('.groovy')) {
						def fileName=entryName.substring(path.length())
						def fileSet=files[fileName]
						if(!fileSet){
							fileSet=[]
							files[fileName]=fileSet
						}
						fileSet << "${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}"
						TCHelper.debug('JAR: {0}, file: {1}',path,entryName)
					}
				}
			}
		}
		return files
	}
	def scan_groovy_files_from_filesys(app_root, subdirs){
		def files=[:]
		app_root=pathClean(app_root)
		def rootPath=Paths.get(app_root)
		Files.walkFileTree(rootPath, new FileVisitor<Path>() {
			def visitPathFiles=[:]// path:files
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
				def path1=pathClean(path)
				TCHelper.debug('PRE-DIR: {0}, subdirs: {1}',path1,subdirs)
				if(subdirs && app_root!=path1){
					def inSubdir=false
					for(def subdir in subdirs){
						subdir=pathClean(app_root+'/'+subdir)
						if(path1.startsWith(subdir)){
							inSubdir=true
							break
						}
					}//END-for
					if(!inSubdir){
						return FileVisitResult.TERMINATE
					}
				}
				visitPathFiles.put(path1,[])
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
				if(path.toString().endsWith('.groovy')){
					def fileSet=visitPathFiles.get(pathClean(path.parent))
					fileSet.add(pathClean(path))
					TCHelper.debug('VST-FILE: {0}',path)
				}
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
				def cleanPath=pathClean(path)
				def fileSet=visitPathFiles.get(cleanPath)
				if(fileSet && !fileSet.isEmpty()){
					def pos=app_root.size() as int
					if(cleanPath.size()>pos){
						files.put(cleanPath.substring(pos),fileSet)
					}else{
						files.put('',fileSet)
					}
				}
				//
				visitPathFiles.remove(cleanPath)
				//
				TCHelper.debug('POST-DIR: {0}, fileSet: ${1}',path,fileSet)
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
				TCHelper.debug('VST-ERROR: {0}',exc,path)
				return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE
			}
		})//walkFileTree done
		return files
	}
	//====== parse class start ======
	def parse_classes=[]
	def parse_add_class(Class[] clazz){
		if(clazz){
			parse_classes.addAll(clazz)
		}
	}
	def parse_process(){
		parse_mapping_class()
		parse_framework_process()
		parse_action_process()
		parse_classes.clear()
	}
	def parse_mapping_class(){
		def classMap=TCCache.me().cache().anno_class
		parse_classes.each{
			def anno=it.getAnnotation(TCAction.class)
			if(!anno){
				anno=it.getAnnotation(TCFramework.class)
			}
			if(anno){
				if(anno in TCAction){
					classMap.action[it.name]=[name:anno.name()]
				}else if(anno in TCFramework){
					classMap.framework[it.name]=[name:anno.name(), order:anno.order()]
				}
				classMap[it.name]=[clazz:it, instance:null]
			}
		}
		TCHelper.debug('parse_mapping_class, classes: {0}, anno_class:{1}',parse_classes,classMap)
	}
	def parse_framework_process(){
		def cache=TCCache.me().cache()
		def classMap=cache.anno_class
		def annoMap=[:]//framework annotation name value mapping class name and order
		parse_classes.each{
			def key=it.name
			def value=classMap.framework[key]
			if(value){
				if(!annoMap[value.name]){
					annoMap[value.name]=[name:key, order:value.order] // class name, class order
				}else if(annoMap[value.name].order<value.order){
					annoMap[value.name]=[name:key, order:value.order]
				}
			}
		}
		annoMap.each{key, value ->
			TCHelper.debug('parse_mapping_class, framework_instance:{0}={1}',key,value)
			cache.framework_instance[key]=classMap[value.name].clazz.newInstance()
			classMap[value.name].instance=cache.framework_instance[key]
		}
	}
	def parse_action_process(){
		def classMap=TCCache.me().cache().anno_class
		def actionHandler=TCCache.me().cache().framework_instance.TCActionHandler
		parse_classes.each{
			def action=classMap.action[it.name]
			if(action){
				actionHandler.registerAction(classMap[it.name].clazz)
			}
		}
	}
	//====== parse class end ======
	//====== folder watch start ======
	def folder_watch_service
	def folder_listener
	def folder_watch_keys=[:]
	def boolean folder_cancel=false
	def folder_get_watch_service(){
		if(!folder_watch_service){
			folder_watch_service=FileSystems.getDefault().newWatchService()
		}
		return folder_watch_service
	}
	def folder_add_listener(path, changeListener) {
		folder_listener=changeListener
		folder_register_all(path)
		folder_process_event()
	}
	def folder_register_all(path){
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)throws IOException{
				def key=dir.register(folder_get_watch_service(), folder_listener.getEventTypes())
				folder_watch_keys.put(key, dir)
				TCHelper.debug('folder_register_all, lisent dir:{0}',dir)
				return FileVisitResult.CONTINUE
			}
		})
	}
	def folder_process_event(){
		def t=Thread.start{
			while(!folder_cancel){
				def key = folder_get_watch_service().take()
				def dir = folder_watch_keys.get(key)
				if(!dir){
					continue
				}
				key.pollEvents().each{ event ->
					Path name = event.context()
					Path child = dir.resolve(name)
					TCHelper.debug('Event: {0}\nDir: {1}\nName: {2}\nFile: {3}',event.kind().name(),dir,name,child)
					if(event.kind()!=StandardWatchEventKinds.OVERFLOW){
						if (event.kind()==StandardWatchEventKinds.ENTRY_CREATE) {
							if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
								folder_register_all(child)
							}
						}
					}
					if (!Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
						def groovyPath=child.toString()
						if(groovyPath.endsWith('.groovy')){
							folder_listener.addEvent(event, groovyPath)
						}
					}
				}// end each
				// reset key and remove from set if directory no longer accessible
				if (!key.reset()) {
					folder_watch_keys.remove(key)
					// all directories are inaccessible
					if (folder_watch_keys.isEmpty()) {
						break
					}
				}
				folder_listener.processEvent()
			}// end while
		}//end thread
	}
	//====== folder watch end ======
}
class TCHelper{
	def static cache=[:]
	def static prop2map(props, filter){
		def map=[:]
		if(!props || !(props in Properties)){
			return map
		}
		props.entrySet().each{ entry->
			def ok=true
			if(filter && filter in Closure){
				ok=filter(entry)
			}
			if(ok==true){
				map.put(entry.key, entry.value)
			}
		}
		return map
	}
	def static pathPut(path, value){
		synchronized(cache){
			org.iff.infra.util.MapHelper.setByPath(cache, path, value)
		}
	}
	def static pathGet(path){
		org.iff.infra.util.MapHelper.getByPath(cache, path)
	}
	def static getKey(key){
		cache[key]
	}
	def static putAll(map){
		synchronized(cache){
			cache.putAll(map)
		}
	}
	def static remove(key){
		synchronized(cache){
			cache.remove(key)
		}
	}
	def static clear(){
		synchronized(cache){
			cache.clear()
		}
	}
	def static pathClean(path){
		org.iff.infra.util.StringHelper.pathBuild(path, '/')
	}
	def static isObjects(object){
		if(object==null){
			return false
		}
		return Collection.isAssignableFrom(object.getClass()) || object.getClass().isArray()
	}
	//=======Log Start
	def static debug(message,Object[] params){
		if(org.iff.infra.util.Logger.getLogger().isDebugEnabled() && message){
			def args=params ? (params as List) : []
			def error=(args[0] in Throwable) ? args.remove(0) : null
			if(error){
				org.iff.infra.util.Logger.debug(org.iff.infra.util.FCS.get(message.toString(), args as Object[]), error)
			}else{
				org.iff.infra.util.Logger.debug(org.iff.infra.util.FCS.get(message.toString(), args as Object[]))
			}
		}
	}
	def static warn(message,Object[] params){
		if(org.iff.infra.util.Logger.getLogger().isWarnEnabled() && message){
			def args=params ? (params as List) : []
			def error=(args[0] in Throwable) ? args.remove(0) : null
			if(error){
				org.iff.infra.util.Logger.warn(org.iff.infra.util.FCS.get(message.toString(), args as Object[]), error)
			}else{
				org.iff.infra.util.Logger.warn(org.iff.infra.util.FCS.get(message.toString(), args as Object[]))
			}
		}
	}
	def static info(message,Object[] params){
		if(org.iff.infra.util.Logger.getLogger().isInfoEnabled() && message){
			def args=params ? (params as List) : []
			def error=(args[0] in Throwable) ? args.remove(0) : null
			if(error){
				org.iff.infra.util.Logger.info(org.iff.infra.util.FCS.get(message.toString(), args as Object[]), error)
			}else{
				org.iff.infra.util.Logger.info(org.iff.infra.util.FCS.get(message.toString(), args as Object[]))
			}
		}
	}
	def static error(message,Object[] params){
		if(org.iff.infra.util.Logger.getLogger().isErrorEnabled() && message){
			def args=params ? (params as List) : []
			def error=(args[0] in Throwable) ? args.remove(0) : null
			if(error){
				org.iff.infra.util.Logger.error(org.iff.infra.util.FCS.get(message.toString(), args as Object[]), error)
			}else{
				org.iff.infra.util.Logger.error(org.iff.infra.util.FCS.get(message.toString(), args as Object[]))
			}
		}
	}
	def static trace(message,Object[] params){
		if(org.iff.infra.util.Logger.getLogger().isTraceEnabled() && message){
			def args=params ? (params as List) : []
			def error=(args[0] in Throwable) ? args.remove(0) : null
			if(error){
				org.iff.infra.util.Logger.trace(org.iff.infra.util.FCS.get(message.toString(), args as Object[]), error)
			}else{
				org.iff.infra.util.Logger.trace(org.iff.infra.util.FCS.get(message.toString(), args as Object[]))
			}
		}
	}
}