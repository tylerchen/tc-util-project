package org.iff.groovy.framework

class TCProp{}
TCProp.metaClass.static.propertyMissing   = { String name -> TCCache.me().props.get(name)  }
class TCConfig{}
TCConfig.metaClass.static.propertyMissing = { String name -> TCCache.me().config.get(name) }

/*
 * support: 
 * file://[file-path]
 * classpath://[class-path]
 * jar://[jar-name]/[file-path]
 * database://[user-name]:[password]@[ip]:[port]/database/sql
 * datasource://[datasource-name]/sql
 * resource://[resource-name]
 * 
 * return:
 * files://, classpath://, jar://   : only return the file path
 * database://, datasource          : return the result of the sql
 * resource://                      : return the system resource
 */
class TCProtocol{
	def static get(protocol){
		
	}
	def static file=[
		protocol:{protocol->
			def files=[], ps=(protocol ?: '').split(',')
			ps.each{path->
				def rs=org.iff.infra.util.ResourceHelper.loadResourcesInFileSystem(path, '*', '', '')
				files.addAll(rs)
			}
			files
		}
	]
	def static classpath=[
		protocol:{protocol->
			def files=[], ps=(protocol ?: '').split(',')
			ps.each{path->
				def rs=org.iff.infra.util.ResourceHelper.loadResourcesInClassPath(path, '*', '', '')
				files.addAll(rs)
			}
			files
		}
	]
	def static jar=[
		protocol:{protocol->
			def files=[], ps=(protocol ?: '').split(',')
			ps.each{path->
				def rs=org.iff.infra.util.ResourceHelper.loadResourcesInJar(path, '*', '', '')
				files.addAll(rs)
			}
			files
		}
	]
	def static database=[
		protocol:{protocol->
			def files=[], ps=(protocol ?: '').split(',')
			ps.each{path->
				def rs=org.iff.infra.util.ResourceHelper.loadResourcesInJar(path, '*', '', '')
				files.addAll(rs)
			}
			files
		}
	]
}
TCProtocol.metaClass.static.propertyMissing = { String name -> TCCache.getByPath('framework/protocol').get(name)  }

class TCPathEventBus{
	def static regist_listener(path, listener){
		def event_path=org.iff.infra.util.StringHelper.pathBuild('framework/path_event_bus/'+path, '/')
		def map=TCCache.getByPath(event_path)
		(map.'listener'=map.'listener' ?: []).add(listener)
		if(listener instanceof Map){
			listener.'event_path'=path
			listener.'unregist'={
				TCPathEventBus.remove_listener(listener.'event_path', listener)
			}
		} else if(!listener.metaClass.hasMetaMethod('unregist', [] as Object[])){
			listener.metaClass.'event_path'=path
			listener.metaClass.unregist={
				TCPathEventBus.remove_listener(listener.'event_path', listener)
			}
		}
	}
	def static remove_listener(path, listener){
		def event_path=org.iff.infra.util.StringHelper.pathBuild('framework/path_event_bus/'+path, '/')
		def map=TCCache.getByPath(event_path)
		(map.'listener'=map.'listener' ?: []).remove(listener)
	}
	def static async_event(path, events){
		def event_path=org.iff.infra.util.StringHelper.pathBuild('framework/path_event_bus/'+path, '/')
		def map=TCCache.getByPath(event_path)
		def listeners=map.'listener' ?: []
		if(listeners.size()<1){
			(path!='/dead') && async_event('/dead', events)
		}else{
			listeners.each{lsn->
				Thread.start{
					lsn.listen(path, events)	
				}
			}
		}
	}
	def static sync_event(path, events){
		def event_path=org.iff.infra.util.StringHelper.pathBuild('framework/path_event_bus/'+path, '/')
		def map=TCCache.getByPath(event_path)
		def listeners=map.'listener' ?: []
		if(listeners.size()<1){
			(path!='/dead') && sync_event('/dead', events)
		}else{
			listeners.each{lsn->
				lsn.listen(path, events)	
			}
		}
	}
	def static dead_event=[
		listen:{path, events->
			TCHelper.warn('[dead_event][{0}][{1}]',path,events)
		}
	]
}
TCPathEventBus.regist_listener('/dead', TCPathEventBus.dead_event)

class TCFileStore{// you can access file from file store
	// file struct: file_url:{file_name, file_type, file_url, file_size, root_path, load_content, last_modify, content}
	// framework/file_store/jar|file/root_path/groovy_paths/{file_name:file_struct}
	// framework/file_store/file_url/file_struct
	// root_path: MUST end with '/', example: 'g:/a/b/c/' or '/opt/test/a/b/c/'
	// groovy_path
	def static get_file(file_url){
		TCCache.getByPath('framework/file_store/file_url')[file_url]
	}
	def static get_file_content(file_url){
		TCCache.getByPath('framework/file_store/file_url')[file_url]?.'content'
	}
	def static set_file(file_struct){
		TCCache.getByPath('framework/file_store/file_url').put(file_struct.file_url, file_struct)
		if(file_struct.file_url.startsWith('file:')){
			def file_map=TCCache.getByPath('framework/file_store/file')
			def root_map=(file_map[file_struct.root_path]=file_map[file_struct.root_path] ?: [:])/*get root path file map*/
			def groovy_paths=TCHelper.str(file_struct.file_url).scut('file://'+file_struct.root_path).ecut(file_struct.file_name).clean().val()
			def file_name_map=org.iff.infra.util.MapHelper.getByPath(root_map, groovy_paths)/*get the file name map*/
			if(file_name_map==null){
				file_name_map=[/*file_name*/:/*file_stuct*/]
				org.iff.infra.util.MapHelper.setByPath(root_map, groovy_paths, file_name_map)/*init the file name map*/
			}
			def fs_old=file_name_map[file_struct.file_name]/*get old file struct*/
			if(fs_old){
				file_struct.each{k,v-> fs_old[k]=v } /*update to the new file struct*/
			}else{
				file_array[file_struct.file_name]=file_struct/*setting the new file struct if file is new*/
			}
		}else if(file_struct.file_url.startsWith('jar:')){
			def file_map=TCCache.getByPath('framework/file_store/jar')
			def root_map=(file_map[file_struct.root_path]=file_map[file_struct.root_path] ?: [:])/*get root path file map*/
			def groovy_paths=TCHelper.str(file_struct.file_url).scut('jar:file://'+file_struct.root_path).ecut(file_struct.file_name).clean().val()
			def file_name_map=org.iff.infra.util.MapHelper.getByPath(root_map, groovy_paths)/*get the file name map*/
			if(file_name_map==null){
				file_name_map=[/*file_name*/:/*file_stuct*/]
				org.iff.infra.util.MapHelper.setByPath(root_map, groovy_paths, file_name_map)/*init the file name map*/
			}
			def fs_old=file_name_map[file_struct.file_name]/*get old file struct*/
			if(fs_old){
				file_struct.each{k,v-> fs_old[k]=v } /*update to the new file struct*/
			}else{
				file_array[file_struct.file_name]=file_struct/*setting the new file struct if file is new*/
			}
		}
	}
	def static del_file(file_url){
		def file_struct=get_file(file_url)
		TCCache.getByPath('framework/file_store/file_url').remove(file_struct.file_url)
		if(file_struct.file_url.startsWith('file:')){
			def file_map=TCCache.getByPath('framework/file_store/file')
			def root_map=(file_map[file_struct.root_path]=file_map[file_struct.root_path] ?: [:])/*get root path file map*/
			def groovy_paths=TCHelper.str(file_struct.file_url).scut('file://'+file_struct.root_path).ecut(file_struct.file_name).clean().val()
			def file_name_map=org.iff.infra.util.MapHelper.getByPath(root_map, groovy_paths)/*get the file name map*/
			file_name_map?.remove(file_struct.file_name)
		}else if(file_struct.file_url.startsWith('jar:')){
			def file_map=TCCache.getByPath('framework/file_store/jar')
			def root_map=(file_map[file_struct.root_path]=file_map[file_struct.root_path] ?: [:])/*get root path file map*/
			def groovy_paths=TCHelper.str(file_struct.file_url).scut('jar:file://'+file_struct.root_path).ecut(file_struct.file_name).clean().val()
			def file_name_map=org.iff.infra.util.MapHelper.getByPath(root_map, groovy_paths)/*get the file name map*/
			file_name_map?.remove(file_struct.file_name)
		}
	}
	def static del_dir(file_url_dir){
		def files=TCCache.getByPath('framework/file_store/file_url')
		file_url_dir=TCHelper.path_clean(file_url_dir+'/')
		files.findAll{file_url, file_struct->
			file_url.startsWith(file_url_dir)
		}.each{file_url, file_struct->
			files.remove(file_url)
		}
		if(file_url_dir.startsWith('file:')){
			def file_map=TCCache.getByPath('framework/file_store/file')
			def root_map=file_map[file_url_dir-'file://']/*get root path file map*/
			root_map?.empty()
		}
	}
	def static get_root_path_file(file_url_dir){
		file_url_dir=file_url_dir.startsWith('jar://') ? ('jar:file://'+(file_url_dir-'jar://')) : file_url_dir
		TCCache.getByPath('framework/file_store/file_url').findAll{k,v-> k.startsWith(file_url_dir)}
	}
	def static load_groovy(url_dir){//starts with jar:// or file://
		def files = org.iff.infra.util.ResourceHelper.loadResources(url_dir, '.groovy', '*', null)
		def str_help=TCHelper.str('.')
		files?.each{file_url->
			def file_name=file_url.substring(file_url.lastIndexOf('/')+1)
			def file_struct=[
				file_name   : file_name,
				file_type   : str_help.str(file_name).icut('.').val(),
				file_url    : file_url,
				file_size   : -1,
				root_path   : url_dir-'jar://'-'file://',
				load_content: null,
				last_modify : -1,
				content     : null]
			TCFileStore.set_file(file_struct)
		}
	}
}

class TCFolderListener{
	def static folder_path='framework/file_listener/folder/dir' // {root_path:{dir:hash}}
	def static pause_path='framework/file_listener/folder/pause' // true or false
	def static scan_seconds='framework/file_listener/folder/scan_seconds' // integer
	def static regist(root_path){
		def dirs=TCCache.getByPath(folder_path)
		def regist_path=dirs[root_path]
		if(regist_path==null){
			dirs.put(root_path,[:])
			//dirs.findAll{true}.each{dir, hash->
			//	if(dir.startsWith(root_path)){//dir is child dir of the root_path, so we should remove child dir and listen parent dir
			//		dirs.remove(dir)
			//		dirs[root_path]=[:]
			//	}
			//}
		}
	}
	def static listen(){
		TCCache.setByPath(folders_path, [:])
		Thread.start{
			def dirs, pause
			while((dirs=TCCache.getByPath(folders_path)) && dirs.size()>0){
				while((pause=TCCache.getByPath(pause_path))){
					java.util.concurrent.TimeUnit.SECONDS.sleep(TCCache.getByPath(scan_seconds) ?: 5)
				}
				def all_modified=[:], all_deleted=[:], str_help=TCHelper.str('.')
				dirs.each{dir_path, dir_hashmap->
					def scan_file=[:], modified=[:], deleted=[:], folder=new File(dir_path), root_path=TCHelper.path_clean('file://'+dir_path+'/')
					def files_map=TCFileStore.get_root_path_file(root_path)
					if(!folder.exists() || !folder.isDirectory()){// may be folder was removed
						deleted << files_map
						TCFileStore.del_dir(root_path)
					}else if(folder.exists() && folder.isDirectory()){
						def by_name = { it.name }
						folder.traverse(type:groovy.io.FileType.FILES, nameFilter: ~/.*\.groovy$/, sort:by_name){file->
							def file_path=TCHelper.path_clean(file.path), last_modify=file.lastModified(), file_name=file.name
							def file_url=TCHelper.path_clean('file://'+file.path)
							scan_file[file_url]=last_modify
							if(file_url in files_map){
								if(last_modify != files_map[file_url].last_modify){
									modified.put(file_url, [
										file_name   : file_name, 
										file_type   : str_help.str(file_name).scut('.').val(), 
										file_url    : file_url, 
										file_size   : file.size(), 
										root_path   : str_help.str(file_url).scut(dir_path).ecut(file_name).val(), 
										load_content: null, 
										last_modify : last_modify, 
										content     : null])
								}
							}else{
								modified.put(file_url, [
										file_name   : file_name, 
										file_type   : str_help.str(file_name).scut('.').val(), 
										file_url    : file_url, 
										file_size   : file.size(), 
										root_path   : str_help.str(file_url).scut(dir_path).ecut(file_name).val(), 
										load_content: null, 
										last_modify : last_modify, 
										content     : null])
							}
						}//end-folder.traverse
						deleted=files_map.findAll{k,v-> !(k in scan_file)}
					}//end-if
					all_modified[dir_path]=modified
					all_deleted[dir_path]=deleted
				}
				if(all_modified.size()!=0 || all_deleted.size()!=0){
					TCPathEventBus.async_event('/folder_listener', ['modified':all_modified, 'deleted':all_deleted])
				}
				java.util.concurrent.TimeUnit.SECONDS.sleep(TCCache.getByPath(scan_seconds) ?: 5)
			}
		}
	}
}

class TCGroovyLoader{
	
	def static root_url_manager='framework/groovy_listener/root_url_manager' // {root_path_url:[class_manager_name]}
	def static pause_path='framework/groovy_listener/pause' // true or false
	def static scan_seconds='framework/groovy_listener/scan_seconds' // integer
	def static regist(root_path_url, class_manager_name){
		def dirs=TCCache.getByPath(root_url_manager)
		def regist_path=dirs[root_path_url]
		if(regist_path && !regist_path.contains(class_manager_name)){
			regist_path << class_manager_name
		}else{
			( dirs[root_path_url] = regist_path ?: [] ).add(class_manager_name)
		}
	}

	def static load_compile_by_order(groovy_path, class_manager_name){
		def cm=TCCache.getByPath('framework/class_manager/'+class_manager_name)
		def order=["system/util","system/framework","app/model","app/service","app/view"]
	}
	
	def static groovy_reload_event=[
	]
}


class TCFramework{
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
	
	/*00.)loading system groovy files TCFramework.groovy*/
	def load_framework_groovy(){
		// this lifecycle process in TCCLassManager
	}
	/*01.)loading META-INF/tc-framework.properties*/
	def load_properties(){
		system_properties_to_map()
		properties_file_to_map(TCProp.tc_properties_filepath ?: 'classpath://META-INF/tc-framework.properties', TCProp.tc_properties_version ?: 'order.loading.configure')
	}

	/*02.)loading server configure from jar and filesystem*/
	def load_server_xml(){}

	/*03.)loading server configure from database*/
	def load_server_data(){}

	/*04.)combine server configure*/
	def combine_server_cfg(){}

	/*05.)start server*/
	def start_server(){}

	/*06.)loading container configure*/
	def load_container_cfg(){}

	/*07.)loading container groovy files*/
	def load_container_groovy(){}

	/*08.)loading resource*/
	def load_container_resource(){}
	
	/*09.)start container*/
	def start_container(){}

	/*10.)loading application configure*/
	def load_application_cfg(){}

	/*11.)loading application groovy files*/
	def load_application_groovy(){}
	
	
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
							files[fileName].add("${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}")
						}else{
							files[fileName]=["${protocol}:${filePath.substring(0, filePath.lastIndexOf('!/') + 2)}${entryName}"]
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
						files[parent].add(path)
					}else{
						files[parent]=[path]
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
	
	def static path_clean(path){
		if(org.iff.infra.util.PlatformHelper.isWindows()){
			return path ? TCHelper.pathClean(path.toString()).replaceAll('^/|/$','') : null
		}else{
			return path ? TCHelper.pathClean(path.toString()).replaceAll('/$','') : null
		}
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
	def static flatten(Map m, String separator = '/'){
		m.collectEntries {k, v ->
			v instanceof Map ? flatten(v, separator).collectEntries { q, r ->  [(k + separator + q): r] } : [(k):v] 
		}
	}
    def static str(str){
        def map=[ins:str]
        def val={
            map.ins
        }
        def scut={start, offset=0 ->
            if(start instanceof String){
                map.ins=map.ins.startsWith(start) ? map.ins.substring(start.size()+offset) : map.ins
            }else if(start instanceof Number){
                map.ins=map.ins.substring(start)
            }
            map
        }
		def icut={cutto, offset=0 ->
            if(cutto instanceof String){
                def index=map.ins.indexOf(cutto)+cutto.size()+offset
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
            }else if(cutto instanceof Number){
                def index=cutto+offset
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
            }
            map
        }
		def lcut={last, offset=0 ->
            if(last instanceof String){
                map.ins=map.ins.endsWith(last) ? map.ins.substring(0, map.ins.size()-last.size()+offset) : map.ins
            }else if(last instanceof Number){
                map.ins=map.ins.substring(0, last)
            }
            map
		}
        def ecut={cutfrom, offset=0 ->
            if(cutfrom instanceof String){
                def index=map.ins.lastIndexOf(cutfrom)+offset
                map.ins=map.ins.substring(0, map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
            }
            map
        }
		def clean={
			map.ins=TCHelper.path_clean(map.ins)
			map
		}
		def new_str={String new_str->
			map.ins=new_str
			map
		}
        return map << ['scut':scut, 'ecut':ecut, 'icut': strim, 'lcut': etrim, 'clean': clean, 'val': val, 'str': new_str]
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