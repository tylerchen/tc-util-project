package org.iff.groovy.framework

import java.nio.file.*
import java.nio.file.attribute.*
import java.util.concurrent.*

class FolderWatchers {
	def static FolderWatchers watchers
	def static WatchService watchService
	def static Map<WatchKey,Path> keys=[:]
	def static ChangeListener changeListener
	def boolean cancel
	private FolderWatchers(){
		watchService = FileSystems.getDefault().newWatchService()
	}
	def static FolderWatchers me() {
		if (!watchers){
			watchers = new FolderWatchers()
		}
		return watchers
	}
	def addFolderListener(Path path, ChangeListener changeListener) {
		FolderWatchers.changeListener=changeListener
		registerAll(path)
		processEvent()
	}
	def cancelFolderWatching() {
	}
	def registerAll(Path path){
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)throws IOException{
				def key=dir.register(watchService, changeListener.getEventTypes())
				keys.put(key, dir)
				println "lisent: $dir"
				return FileVisitResult.CONTINUE
			}
		})
	}
	def processEvent(){
		def t=Thread.start{
			while(!cancel){
				def key = watchService.take()
				def dir = keys.get(key)
				if(dir==null){
					continue
				}
				key.pollEvents().each{ event ->
					Path name = event.context()
					Path child = dir.resolve(name)
					println "${event.kind().name()}:${dir}:${name}: ${child}\n"
					if(event.kind()!=StandardWatchEventKinds.OVERFLOW){
						if (event.kind()==StandardWatchEventKinds.ENTRY_CREATE) {
							if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
								registerAll(child)
							}
						}
					}
					if (!Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
						def groovyPath=child.toString()
						if(groovyPath.endsWith(".groovy")){
							changeListener.addEvent(event, groovyPath)
						}
					}
				}// end each
				// reset key and remove from set if directory no longer accessible
				if (!key.reset()) {
					keys.remove(key)
					// all directories are inaccessible
					if (keys.isEmpty()) {
						break
					}
				}
				changeListener.processEvent()
			}// end while
		}//end thread
	}
}
class ChangeListener {
	def WatchEvent.Kind[] eventTypes
	def events=[:]
	public ChangeListener(WatchEvent.Kind[] eventTypes) {
		this.eventTypes = eventTypes
	}
	def WatchEvent.Kind[] getEventTypes() {
		return eventTypes
	}
	def addEvent(WatchEvent anEvent, String path){
		events.put(path, anEvent)
	}
	def processEvent(){
		events.each(){ k,v ->
			onEvent(v, k)
		}
		if(!events.isEmpty()){
			TCClassParse.me().process()
		}
		events.clear()
	}
	def onEvent(WatchEvent anEvent, String path){
	}
}
class TCClassParse{
	def static TCClassParse _me=new TCClassParse()
	def classes=[]
	def static me(){
		return _me
	}
	def addClasses(Class[] clazz){
		if(clazz){
			classes.addAll(clazz)
		}
	}
	def process(){
		mappingClass()
		println "action=====>${TCCache.me().cache().anno_class.action}"
		println "framework=====>${TCCache.me().cache().anno_class.framework}"
		frameworkProcess()
		actionProcess()
		classes.clear()
	}
	def mappingClass(){
		def classMap=TCCache.me().cache().anno_class
		classes.each{
			def anno=it.getAnnotation(TCAction.class)
			if(!anno){
				anno=it.getAnnotation(TCFramework.class)
			}
			if(anno){
				if(anno instanceof TCAction){
					classMap.action[it.name]=[name:anno.name()]
				}else if(anno instanceof TCFramework){
					classMap.framework[it.name]=[name:anno.name(), order:anno.order()]
				}
				classMap[it.name]=[clazz:it, instance:null]
			}
		}
		println "classes = $classes"
		println "classMap = $classMap"
	}
	def frameworkProcess(){
		def cache=TCCache.me().cache()
		def classMap=cache.anno_class
		def frameworkAnnoNameMappingClassOrder=[:]//framework annotation name value mapping class name and order
		classes.each{
			def key=it.name
			def value=classMap.framework[key]
			if(value){
				if(!frameworkAnnoNameMappingClassOrder[value.name]){
					frameworkAnnoNameMappingClassOrder[value.name]=[name:key, order:value.order]
				}else if(frameworkAnnoNameMappingClassOrder[value.name].order<value.order){
					frameworkAnnoNameMappingClassOrder[value.name]=[name:key, order:value.order]
				}
			}
		}
		frameworkAnnoNameMappingClassOrder.each{key, value ->
			println "framework_instance: ${key}, ${value}"
			cache.framework_instance[key]=classMap[value.name].clazz.newInstance()
			classMap[value.name].instance=cache.framework_instance[key]
		}
	}
	def actionProcess(){
		def classMap=TCCache.me().cache().anno_class
		def actionHandler=TCCache.me().cache().framework_instance.TCActionHandler
		classes.each{
			def action=classMap.action[it.name]
			if(action){
				actionHandler.registerAction(classMap[it.name].clazz)
			}
		}
	}
}
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
class Starter{
	def start(groovyFiles){
		//
		systemPropertiesToMap()
		//
		scanGroovyFile(groovyFiles)
		//
		compileByOrder()
		//
		startFolderListener()
		//
		startServer()
		//
	}
	def scanGroovyFile(groovyFiles){
		groovyFiles.each{name, path ->
			if(name.endsWith('.groovy')){
				if(name.lastIndexOf('/')>-1){
					name=name.substring(0,name.lastIndexOf('/')).replaceAll('^/|/$','')
				}
			}
			def files=TCCache.me().cache().groovy_files.get(name)
			def isCollectionOrArray={object ->
					[Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
			}
			def paths=(isCollectionOrArray(path)?path:[path]) as List
			if(!files){
				TCCache.me().cache().groovy_files.put(name, paths)
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
		// Walk thru mainDir directory
		Files.walkFileTree(rootPath, new FileVisitor<Path>() {
			def visitPathFiles=[:]// path:files
			// First (minor) speed up. Compile regular expression pattern only one time.
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
				//return (matches)? FileVisitResult.CONTINUE:FileVisitResult.SKIP_SUBTREE;
				visitPathFiles.put(path.toString().replaceAll("\\\\","/"),[])
				println "PRE-DIR: $path"
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
				if(path.toString().endsWith(".groovy")){
					def files=visitPathFiles.get(path.parent.toString().replaceAll("\\\\","/"))
					files.add(path.toString().replaceAll("\\\\","/"))
					println "FILE: $path"
				}
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
				def cleanPath=path.toString().replaceAll("\\\\","/")
				def files=visitPathFiles.get(cleanPath)
				println "postVisitDirectory: ${cleanPath}=${files}"
				if(files && !files.isEmpty()){
					def pos=TCCache.me().cache().app_root.size() as int
					if(cleanPath.size()>pos){
						TCCache.me().cache().groovy_files.put(cleanPath.substring(pos),files)
					}else{
						TCCache.me().cache().groovy_files.put('',files)
					}
				}
				//
				visitPathFiles.remove(cleanPath)
				//
				println "POST-DIR: $path, ${files}"
				return FileVisitResult.CONTINUE
			}
			public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
				exc.printStackTrace()
				return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE
			}
		})//walkFileTree done
		println "=============groovy_files============>>>>>>>>>>>>> ${TCCache.me().cache().groovy_files}"
	}
	def startFolderListener(){
		def app_root=TCCache.me().cache().app_root
		if(!app_root || app_root.size()<1){
			return
		}
		def rootPath=Paths.get(app_root)
		if(!rootPath?.toFile()?.exists()){
			return
		}
		def changeListener = new ChangeListener([StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY]){
			def onEvent(WatchEvent anEvent, String path){
				println "LISTENER 1 ${anEvent.kind().name()}, ${path}"
				def file=path.replaceAll("\\\\","/")
				println "Reload Groovy: ${anEvent.kind().name()}, ${path}"
				if(file.startsWith("${TCCache.me().cache().app_root}/system/")){
					println "RESTART FRAMEWORK!!!.........."
				}
				if(anEvent.kind()==StandardWatchEventKinds.ENTRY_DELETE){
					org.iff.infra.util.groovy.TCCLassManager.me().removeClassNameScriptMapping(file)
				}else{
					def loader=TCCLassManager.me().compile(file)
					TCClassParse.me().addClasses(loader.loadedClasses)
					println "Recompile Classes: ${loader.loadedClasses}"
				}
			}
		}
		FolderWatchers.me().addFolderListener(rootPath, changeListener)
	}
	def startServer(){
		def cache=TCCache.me().cache()
		println "CACHE: ${cache}"
		if(cache.framework_instance.TCMain){
			def instance=cache.framework_instance.TCMain
			instance.main()
		}
	}
	def compileByOrder(){
		println "compileByOrder============>>>>>>> ${TCCache.me().cache().groovy_files}"
		def order=["system/util","system/framework","app/model","app/service","app/view"]
		def groovy_files=TCCache.me().cache().groovy_files.sort()
		order.each{ path ->
			println "compile path: ${TCCache.me().cache().app_root}/${path}"
			def errorCompile=[]
			def errorTimes=100
			groovy_files.each{pathContext, files ->
				pathContext=pathContext.replaceAll('^/|/$','')
				if(pathContext.startsWith(path)){
					files.each{ file ->
						println "compile file: $file"
						if(!file.endsWith("/system/framework/TC.groovy")){
							def loader=TCCLassManager.me().compile(file)
							if(loader.isLastCompileSuccess()){
								TCClassParse.me().addClasses(loader.loadedClasses)
							}else{
								println "comile file error: ${file}"
								errorCompile.add(file)
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
						println "recompile error file: $file"
						def loader=TCCLassManager.me().compile(file)
						TCClassParse.me().addClasses(loader.loadedClasses)
					}catch(err){
						errorCompile << file
					}
				}
			}
		}// END-each:order
		TCClassParse.me().process()
	}
	def systemPropertiesToMap(){
		Properties ps = System.getProperties()
		ps.entrySet().each{ entry->
			if(entry.key instanceof String){
				TCCache.me().cache().props.put(entry.key, entry.value)
				println "property: ${entry}"
			}
		}
		TCCache.me().cache().put("app_root", TCCache.me().cache().props.app_root.toString().replaceAll("\\\\","/"))
	}
}