package org.iff.groovy.framework

class TCStarter{
	def TCStarter(){
		TCCache.me().framework_instance.TCStarter=this
	}
	def start(groovyFiles){
		//
		system_properties_to_map()
		properties_file_to_map(TCCache.me().props.tc_properties_filepath ?: 'classpath://META-INF/tc-framework.properties', TCCache.me().props.tc_properties_version ?: 'order.loading.configure')
		//
		def app_root=TCCache.me().app_root
		def tc_jar_path=TCCache.me().props.tc_jar_path
		def tc_file_path=TCCache.me().props.tc_file_path ?: app_root
		TCHelper.debug('\napp_root:{0}\ntc_jar_path:{1}\ntc_file_path:{2}',app_root,tc_jar_path,tc_file_path)
		//
		def jar_files=scan_groovy_files_from_jar(app_root, tc_jar_path)
		def dir_files=scan_groovy_files_from_filesys(app_root, tc_file_path, ['app','system'])
		def exists_groovy_files=scan_exists_groovy_file(app_root, groovyFiles)
		def scan_groovy_files=scan_combine_groovy_files(jar_files, dir_files, exists_groovy_files)
		//
		compile_by_order(app_root, scan_groovy_files.sort())
		//
		scan_start_folder_listener(app_root, tc_file_path)
		//
		start_server()
		//
	}
	def pathClean(path){
		if(org.iff.infra.util.PlatformHelper.isWindows()){
			return path ? TCHelper.pathClean(path.toString()).replaceAll('^/|/$','') : null
		}else{
			return path ? TCHelper.pathClean(path.toString()).replaceAll('/$','') : null
		}
	}
	def system_properties_to_map(){
		def map=TCHelper.prop2map(System.getProperties()){true}
		TCCache.me().with{
			get('props').putAll(map)
			put("app_root", pathClean(map.app_root))
		}
	}
	def properties_file_to_map(respath, version){
		TCCache.me().config.putAll(org.iff.infra.util.PropertiesHelper.loadProperties(respath, version))
	}
	def compile_by_order(app_root, groovy_files){
		app_root=pathClean(app_root)
		TCHelper.debug('Compile By Order: {0}',groovy_files)
		def order=["system/util","system/framework","app/model","app/service","app/view"]
		order.each{ path ->
			TCHelper.debug('Compile Path: {0}/{1}',app_root,path)
			def errorCompile=[]
			def errorTimes=10
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
	def start_server(){
		TCHelper.debug('start_server ......')
		TCHelper.debug('framework_instance:{0}',TCCache.me().framework_instance)
		TCHelper.debug('anno_class:{0}',TCCache.me().anno_class)
		def instance=TCCache.me().framework_instance
		if(instance.TCMain){
			instance.TCMain.main()
		}
	}
	//====== scan groovy file and listen file change start ======
	def scan_dir_groovy_files=[:]// {file path:lastModify}
	def scan_cancel_listner=false
	def scan_sleep_second=5
	def scan_groovy_files_from_jar(app_root, path){
		def files=[:]
		if(!path || path.size()<1){
			return files
		}
		path=pathClean(path)
		def rs=getClass().getClassLoader().getResources(path)
		while(rs.hasMoreElements()){
			def url=rs.nextElement()
			def filePath=url.getFile(), protocol=url.getProtocol()
			if (protocol in ['file','jar'] && filePath.startsWith('file:')) {// path will starts with file:/
				TCHelper.debug('JAR: {0}',path)
				def file = new File(filePath.substring('file:'.size(), filePath.lastIndexOf("!/")))
				def jarFile = new java.util.jar.JarFile(file)
				def entries=jarFile.entries()
				while(entries.hasMoreElements()){
					def entry=entries.nextElement()
					def entryName=pathClean(entry.name)
					if (entryName.startsWith(path) && entryName.endsWith('.groovy')) {
						def fileName=pathClean(entryName.substring(0,entryName.lastIndexOf('/'))-path)
						if(fileName in files){
							files[fileName].add(org.iff.infra.util.ResourceHelper.fixUrl("${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}"))
						}else{
							files[fileName]=[org.iff.infra.util.ResourceHelper.fixUrl("${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}")]
						}
						TCHelper.debug('JAR: {0}, file: {1}',path,entryName)
					}
				}
			}
		}
		return files
	}
	def scan_groovy_files_from_filesys(app_root, dir_root, subdirs){
		def files=[:]//{parentPath:[fileSet]}
		if(!dir_root || dir_root.size()<1 || !(new File(dir_root).exists())){
			return files
		}
		dir_root=pathClean(dir_root)
		(subdirs ?: ['']).each{subdir->
			def dirFile=new File(dir_root+'/'+subdir)
			dirFile.exists() && dirFile.eachFileRecurse(groovy.io.FileType.FILES){ file ->
				if(file.path.endsWith('.groovy')){
					def parent=pathClean(pathClean(file.parent)-dir_root), path=pathClean(file.path)
					scan_dir_groovy_files.put(file.path, file.lastModified())// add for listener
					TCHelper.debug('Groovy File: {0}, file: {1}',parent,path)
					if(files[parent]){
						files[parent].add('file:///'+path)
					}else{
						files[parent]=['file:///'+path]
					}
				}
			}
		}
		return files
	}
	def scan_exists_groovy_file(app_root, groovyFiles){
		def groovy_files=[:]
		app_root=pathClean(app_root) ?: ''
		groovyFiles.each{name, path ->
			if(name.endsWith('.groovy') && name.lastIndexOf('/')>0){
				name=name.substring(0,name.lastIndexOf('/'))
			}
			name=pathClean(pathClean(name)-app_root)
			def paths=(TCHelper.isObjects(path)?path:[path]) as List
			if(name in groovy_files){
				groovy_files[name].addAll(paths)
			}else{
				groovy_files[name]=paths.clone()
			}
		}
		return groovy_files
	}
	def scan_combine_groovy_files(Object[] groovyFiles){
		def groovy_files=[:]
		groovyFiles.each{gfs->
			gfs.each{key, value->
				key=key.replaceAll('^/|/$','')
				value=(TCHelper.isObjects(value)?value:[value]) as List
				if(key in groovy_files){
					groovy_files[key].addAll(value)
				}else{
					groovy_files[key]=value.clone()
				}
			}
		}
		return groovy_files
	}
	def scan_start_folder_listener(app_root, root_dir){
		Thread.start{
			TCHelper.debug('scan_start_folder_listener, root_dir:{0}, app_root:{1}',root_dir,app_root)
			while(!scan_cancel_listner && root_dir){
				def dirFile=[:], modified=[:], folder=new File(root_dir)
				if(!folder.exists()){
					break
				}
				folder.exists()&& folder.traverse(type:groovy.io.FileType.FILES, nameFilter: ~/.*\.groovy$/){file->
					def path=file.path, lastModify=file.lastModified()
					if(path in scan_dir_groovy_files){
						if(lastModify != scan_dir_groovy_files[path]){
							modified.put(path, 'modify')
						}
					}else{
						modified.put(path, 'add')
					}
					dirFile.put(path, lastModify)
				}
				scan_dir_groovy_files.each{k,v->
					if(!(k in dirFile)){
						modified.put(k, 'delete')
					}
				}
				scan_dir_groovy_files.clear()
				scan_dir_groovy_files.putAll(dirFile)
				if(!modified.isEmpty()){
					modified.findAll{k,v-> v=='delete'}.each{k,v->
						TCCLassManager.me().removeClassNameScriptMapping(k)
					}
					def updated_groovy_files=[:]
					root_dir=pathClean(root_dir)
					modified.findAll{k,v-> v!='delete'}.each{k,v->
						def name=pathClean(k)
						name=pathClean(pathClean(name.substring(0,name.lastIndexOf('/')))-root_dir)
						if(name in updated_groovy_files){
							updated_groovy_files[name].add(k)
						}else{
							updated_groovy_files[name]=[k]
						}
					}
					def scan_groovy_files=scan_combine_groovy_files(updated_groovy_files)
					compile_by_order(app_root, scan_groovy_files.sort())
				}
				java.util.concurrent.TimeUnit.SECONDS.sleep(scan_sleep_second)
			}
		}
	}
	//====== scan groovy file and listen file change end ======
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
		def classMap=TCCache.me().anno_class
		parse_classes.each{
			def anno=it.getAnnotation(org.iff.infra.util.groovy.TCAction.class)
			if(!anno){
				anno=it.getAnnotation(org.iff.infra.util.groovy.TCFramework.class)
			}
			if(anno){
				if(anno in TCAction){
					if(classMap.action[it.name]){
						TCHelper.warn('class name exists: {1}',it.name)
					}
					classMap.action[it.name]=[name:anno.name()]
				}else if(anno in TCFramework){
					if(classMap.action[it.name]){
						TCHelper.warn('class name exists: {1}',it.name)
					}
					classMap.framework[it.name]=[name:anno.name(), order:anno.order()]
				}
				classMap[it.name]=[clazz:it, instance:null]
			}
		}
		TCHelper.debug('parse_mapping_class, classes: {0}, anno_class:{1}',parse_classes,classMap)
	}
	def parse_framework_process(){
		def cache=TCCache.me()
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
		def classMap=TCCache.me().anno_class
		def actionHandler=TCCache.me().framework_instance.TCActionHandler
		parse_classes.each{
			def action=classMap.action[it.name]
			if(action){
				actionHandler.registerAction(classMap[it.name].clazz)
			}
		}
	}
	//====== parse class end ======
}
class TCHelper{
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
	def static pathClean(path){
		org.iff.infra.util.StringHelper.pathBuild(path, '/')
	}
	def static isObjects(object){
		if(object==null){
			return false
		}
		return Collection.isAssignableFrom(object.getClass()) || object.getClass().isArray()
	}
    def static close(Object[] p){
        if(p && p.size()>0 && p[-1] in Closure){
            try{
                p[-1](p)
            }finally{
                for(i in p.length-1){
                    try{p[i].close()}catch(err){}
                }
            }
        }
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