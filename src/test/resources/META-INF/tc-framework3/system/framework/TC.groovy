package org.iff.groovy.framework


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
			return path ? org.iff.infra.util.StringHelper.pathBuild(path.toString(),'/').replaceAll('^/|/$','') : null
		}else{
			return path ? org.iff.infra.util.StringHelper.pathBuild(path.toString(),'/').replaceAll('/$','') : null
		}
	}
	def static url_clean(url){
		org.iff.infra.util.StringHelper.fixUrl(url)
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
        def map=[ins:str, lins:'']
        def val={ map.ins }
        def left={ 
            def tmp=map.lins
            map.lins=map.ins
            map.ins=tmp
            map 
        }
        def scut={cutto, offset=0 ->
            def tmp=map.ins
            if(cutto instanceof String && map.ins.startsWith(cutto)){
                def index=cutto.size()+offset 
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
                map.lins=tmp.substring(0, tmp.size()-map.ins.size())
            }else if(cutto instanceof Number){
                map.ins=map.ins.substring(cutto)
                map.lins=tmp.substring(0, tmp.size()-map.ins.size())
            }
            map
        }
        def icut={cutto, offset=0 ->
            def tmp=map.ins
            if(cutto instanceof String && map.ins.indexOf(cutto)>-1){
                def index=map.ins.indexOf(cutto)+cutto.size()+offset
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
                map.lins=tmp.substring(0, tmp.size()-map.ins.size())
            }else if(cutto instanceof Number){
                def index=cutto+offset
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : (map.ins.size()-1))
                map.lins=tmp.substring(0, tmp.size()-map.ins.size())
            }
            map
        }
        def lcut={cutoff, offset=0 ->
            def tmp=map.ins
            if(cutoff instanceof String && map.ins.lastIndexOf(cutoff)>-1){
                def index=map.ins.lastIndexOf(cutoff)+offset
                map.ins=map.ins.substring(0, map.ins.size()>index ? (index>-1 ? index : 0) : map.ins.size())
                map.lins=tmp.substring(map.ins.size())
            }else if(cutoff instanceof Number){
                def index=cutoff+offset
                map.ins=map.ins.substring(map.ins.size()>index ? (index>-1 ? index : 0) : map.ins.size())
                map.lins=tmp.substring(map.ins.size())
            }
            map
        }
        def ecut={cutoff, offset=0 ->
            def tmp=map.ins
            if(cutoff instanceof String && map.ins.endsWith(cutoff)){
                def index=map.ins.size()-cutoff.size()+offset
                map.ins=map.ins.substring(0, map.ins.size()>index ? (index>-1 ? index : 0) : map.ins.size())
                map.lins=tmp.substring(map.ins.size())
            }else if(cutoff instanceof Number){
                map.ins=map.ins.substring(0, last)
                map.lins=tmp.substring(map.ins.size())
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
        return map << ['scut':scut, 'ecut':ecut, 'icut': icut, 'lcut': lcut, 'left': left, 'clean': clean, 'val': val, 'str': new_str]
    }
	//
	def static get_bean(class_manager_name, bean_name, newInstance=false){
		def beans_map=TCCache.me().getByPath('framework/beans/'+class_manager_name)
		def bean=beans_map?.get(bean_name)
		if(bean){
			if(newInstance){
				return TCCLassManager.getManager(class_manager_name, false).loadClass(bean.clazz.name).newInstance()
			}else if(bean.instance==null){
				return (bean.instance=TCCLassManager.getManager(class_manager_name, false).loadClass(bean.clazz.name).newInstance())
			}
		}else{
			bean=TCCache.me().getByPath('framework/beans/default')?.get(bean_name)
			if(bean && newInstance){
				return TCCLassManager.getManager('default', false).loadClass(bean.clazz.name).newInstance()
			}else if(bean && bean.instance==null){
				return (bean.instance=TCCLassManager.getManager('default', false).loadClass(bean.clazz.name).newInstance())
			}
		}
		//bean=[clazz:cls, instance:null, name:anno.name(), order:anno.order(), url:events.file_struct.url]
	}
	def static get_action(class_manager_name, action_name){
		def actions_map=TCCache.me().getByPath('framework/actions/'+class_manager_name)
		def action=actions_map?.get(action_name)
		//bean=[clazz:cls, instance:null, name:anno.name(), order:anno.order(), url:events.file_struct.url]
		if(action){
			return TCCLassManager.getManager(class_manager_name, false).loadClass(action.clazz.name)?.newInstance()
		}else{
			action=TCCache.me().getByPath('framework/beans/default')?.get(action_name)
			if(action){
				return TCCLassManager.getManager('default', false).loadClass(action.clazz.name)?.newInstance()
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

class TCProp{}
TCProp.metaClass.static.propertyMissing   = { String name -> TCCache.me().props.get(name)  }
class TCConfig{}
TCConfig.metaClass.static.propertyMissing = { String name -> TCCache.me().config.get(name) }

class TCRegister{
	def static register_path='framework/register'
	def static register(type, value){// value={name, path,...}
		def reg_val=TCCache.me().getByPath(register_path, true)
		(reg_val[type]=reg_val[type] ?: [:]).put(value.'name', value)
		TCHelper.debug('register():{0},{1}', type, value.name)
	}
	def static get(String type, name=null){
		def reg_val=TCCache.me().getByPath(register_path, true)
		name ? reg_val?.get(type)?.get(name) : reg_val?.get(type)
	}
}

class TCPathEventBus{
	def static event_bus_path='framework/path_event_bus'
	def static regist_listener(path, listener){
		def event_path=event_bus_path+'/'+path
		def map=TCCache.me().getByPath(event_path, true)
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
		TCHelper.debug('regist_listener():{0},{1}', path, listener)
	}
	def static remove_listener(path, listener){
		def event_path=event_bus_path+'/'+path
		def map=TCCache.me().getByPath(event_path)
		map && (map.'listener'=map.'listener' ?: []).remove(listener)
		TCHelper.debug('remove_listener():{0},{1}', path, listener)
	}
	def static async_event(path, events){
		def event_path=event_bus_path+'/'+path
		def map=TCCache.me().getByPath(event_path)
		def listeners=map?.'listener' ?: []
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
		def event_path=event_bus_path+'/'+path
		def map=TCCache.me().getByPath(event_path)
		def listeners=map?.'listener' ?: []
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

class TCComplieByOrder{
	def static compile(file_structs, class_manager_name){
		TCHelper.debug('compile():{0},{1}', file_structs.collect{it.url}, class_manager_name)
		def order=["system/framework/","system/util/","app/model/","app/service/","app/view/"]
		def class_manager=TCCLassManager.getManager(class_manager_name, false)
		order.each{ opath ->
			TCHelper.debug('Compile Path: {0},{1},{2}',opath,class_manager_name,file_structs.collect{it.url})
			def errorCompile=[], classes=[]
			def files=file_structs.findAll{fs->fs.relative_path.startsWith(opath)}
			def errorTimes=files.size()
			files.each{fs, url=fs.url->
				TCHelper.debug('Compile File: {0}', url)
				if(!url.endsWith("/system/framework/TCFramework.groovy")){
					def loader=class_manager.compile(fs)
					if(loader.isLastCompileSuccess()){
						classes.add(loader.loadedClasses)
					}else{
						TCHelper.warn('Compile File Error: {0}',url)
						errorCompile << fs
					}// END-else
				}// END-if
			}// END-files.each
			
			for(;errorTimes>0;errorTimes--){
				if(errorCompile.isEmpty()){
					break
				}
				def errorFiles=errorCompile.clone()
				errorCompile.clear()
				errorFiles.each{fs, url=fs.url->
					TCHelper.debug('Compile Error File: {0}', url)
					def loader=class_manager.compile(fs)
					if(loader.isLastCompileSuccess()){
						classes.add(loader.loadedClasses)
					}else{
						TCHelper.warn('Compile File Error: {0}',url)
						errorCompile << fs
					}// END-else
				}// END-errorFiles.each
			}// END-for
			TCHelper.debug('compile(): classes {0}', classes)
			TCPathEventBus.sync_event('groovy_classes', ['classes': classes, 'class_manager_name': class_manager_name, 'file_struct': file_structs])
		}// END-each:order
	}
}
class TCDefaultClasses{
	def static parse_classes=[
		'name' : 'parse_classes',
		'parse': {events->// {classes, class_manager_name, file_struct}
			TCHelper.debug('parse_classes.parse():events:{0},{1},{2}', events.class_manager_name, events.classes, events.file_struct?.url)
			def class_map=TCCache.me().getByPath('framework/classes/'+events.class_manager_name,true)
			def beans_map=TCCache.me().getByPath('framework/beans/'+events.class_manager_name,true)
			def actions_map=TCCache.me().getByPath('framework/actions/'+events.class_manager_name,true)
			events.classes.each{classes->
				classes.each{cls, name=cls.name->
					def anno=cls.getAnnotation(org.iff.infra.util.groovy.TCAction.class)
					anno = anno ?: cls.getAnnotation(org.iff.infra.util.groovy.TCBean.class)
					if(anno){
						if(class_map[name]){
							TCHelper.warn('class name exists: {1}',name)
						}
						def cm=class_map[name]=[clazz:cls, instance:null, name:anno.name(), order:anno.order(), url:events.file_struct.url]
						def bean=beans_map[cm.name] ?: [:]
						beans_map[cm.name] = (!bean || bean.order < cm.order) ? (bean << cm) : bean
						(anno in TCAction) && (actions_map[cm.name]=beans_map[cm.name])
					}//END-if(anno)
				}// END-classes.each
			}// END-events.classes.each
			TCHelper.debug('parse_classes.parse():class_map:{0}', class_map)
			TCHelper.debug('parse_classes.parse():beans_map:{0}', beans_map)
			TCHelper.debug('parse_classes.parse():actions_map:{0}', actions_map)
		}// END-parse
	]
	def static parse_action=[
		'name' : 'parse_action',
		'parse': {events->// {classes, class_manager_name, file_struct}
			TCHelper.debug('parse_action.parse():events:{0},{1},{2}', events.class_manager_name, events.classes, events.file_struct?.url)
			def class_map=TCCache.me().getByPath('framework/classes/'+events.class_manager_name,true)
			def actions_map=TCCache.me().getByPath('framework/actions/'+events.class_manager_name,true)
			def url_map=TCCache.me().getByPath('framework/urlmappings/'+events.class_manager_name,true)
			events.classes.each{classes->
				classes.each{cls, name=cls.name->
					def action_name=class_map[name]?.name
					def action=action_name ? actions_map[action_name] : null
					if(action){
						def context=action.name
						action.clazz.declaredMethods.each{method->
							def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
							def method_name=method.name, modifiers=method.modifiers
							if(method_name.indexOf('$')<0 && !(method_name in exclude) && !java.lang.reflect.Modifier.isStatic(modifiers) && java.lang.reflect.Modifier.isPublic(modifiers)){
								if('index'==method_name){
									url_map[context]=['action':action.clazz.name,'method':method_name,'context':context]
								}
								url_map["${context}/${method_name}"]=['action':action.clazz.name,'method':method_name,'context':context]
							}
						}
					}
				}// END-classes.each
			}// END-events.classes.each
			TCHelper.debug('parse_classes.parse():url_map:{0}', url_map)
		}// END-parse
	]
	def static groovy_classes_parse_event=[
		'listen': {path, events->
			TCHelper.debug('groovy_classes_parse_event.listen():{0},{1},{2}', events.class_manager_name, events.classes, events.file_struct?.url)
			TCHelper.warn('[groovy_classes_parse_event][{0}]',path)
			TCRegister.get('parse_classes').each{k,v->
				v.parse(events)
			}
		}
	]
	def static get_class_manager_event(url_dir, class_manager_name){
		[
			'url_dir': url_dir,
			'class_manager_name': class_manager_name,
			'listen': {path, events->
				TCHelper.debug('get_class_manager_event.listen():{0},{1},{2}', url_dir, class_manager_name, path)
				TCHelper.warn('[class_manager_event][{0}]',path)
				def class_manager=TCCLassManager.getManager(class_manager_name, false)
				events.'delete'.each{file_struct->
					class_manager.removeClassNameScriptMapping(file_struct.url)
				}
				events.'modify' && TCComplieByOrder.compile(events.'modify', class_manager_name)
			}
		]
	}
}

class TCFileStore{
	def static file_path='framework/file_store'
	def static set(file_struct){// file_struct={name, type, url, path, dir, relative_path, size, content, last_modify, last_scan}
		TCHelper.debug('TCFileStore.set():{0}', file_struct.url)
		def file_val=TCCache.me().getByPath(file_path)
		!file_val && (file_val=[:]) || TCCache.me().setByPath(file_path, file_val)
		def event_file_struct=[], tmp_file_struct
		(TCHelper.isObjects(file_struct) ? file_struct : [file_struct]).each{fs->
			tmp_file_struct=file_val[fs.url]
			tmp_file_struct ? (tmp_file_struct << fs) : (tmp_file_struct=fs)
			file_val[fs.url] || (file_val[fs.url]=tmp_file_struct)
			event_file_struct << tmp_file_struct
			load_content(tmp_file_struct)
		}
		TCHelper.debug('TCFileStore:{0}', file_val.collect{k,v->k})
		TCPathEventBus.sync_event('groovy_change', ['modify':event_file_struct])
	}
	def static del(file_struct){
		TCHelper.debug('TCFileStore.del():{0}', file_struct.url)
		if(file_struct){
			def file_val=TCCache.me().getByPath(file_path), fix_file_struct=TCHelper.isObjects(file_struct) ? file_struct : [file_struct]
			file_val && fix_file_struct.each{fs-> file_val.remove(fs.url) }
			TCPathEventBus.sync_event('groovy_change', ['delete':fix_file_struct])
		}
	}
	def static del_dir(url){
		TCHelper.debug('TCFileStore.del_dir():{0}', url)
		def file_val=TCCache.me().getByPath(file_path)
		def result=file_val.findAll{k,v->k.startsWith(url)}
		file_val && result.each{k,v-> file_val.remove(k)}
		TCPathEventBus.sync_event('groovy_change', ['delete':result])
	}
	//jar:file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
	//file:///G:/bak/app_root/app/view/a.groovy
	def static get(url){// url=file:///xx/ or jar:///xx/
		def file_val=TCCache.me().getByPath(file_path)
		def result=file_val?.get(url)
		!result && !url.endsWith('.groovy') && (result=file_val.findAll{k,v->k.startsWith(url)})
		result
	}
	def static get_del(url, last_scan){
		def file_val=TCCache.me().getByPath(file_path)
		def result=file_val.findAll{k,v->
			k.startsWith(url) && !k.endsWith('/system/framework/TC.groovy') && (v.last_scan!=last_scan)}
		result
	}
	def static load_content(file_struct){
		def url=new URL(file_struct.url)
		file_struct.content=url.getText()
		file_struct.size=file_struct.content.size()
		file_struct.last_modify=file_struct.url.startsWith('file:') ? new File(url.toURI()).lastModified() : 0
	}
}

class TCScanFile{
	def static scan(register_value){
		TCHelper.debug('TCScanFile.scan():{0}', register_value)
		def files_url=org.iff.infra.util.ResourceHelper.loadResources(register_value.'path', '.groovy', '*', null)
		if(!files_url){
			TCFileStore.del_dir(register_value.'path')
			return
		}
		def files=[], del_files=[], last_scan=System.currentTimeMillis(), str_help=TCHelper.str('')
		files_url.each{file_url->
			if(!file_url.endsWith('/system/framework/TC.groovy')){
				def file_struct=TCFileStore.get(file_url)
				if(file_struct){
					def last_modify=file_url.startsWith('file:') ? new File(new URL(file_url).toURI()).lastModified() : 0
					(file_struct.last_scan=last_scan) && (file_struct.last_modify!=last_modify) && (file_struct.last_modify=last_modify) && (files << file_struct)
				}else{
					def file_name=file_url.substring(file_url.lastIndexOf('/')+1)
					files << [
						'name': file_name, 
						'type': file_name.substring(file_name.lastIndexOf('.')+1),
						'url' : file_url,
						'path': register_value.path,
						'dir' : str_help.str(org.iff.infra.util.StringHelper.fixUrl(register_value.path)).icut(':///').icut('!/').scut('/').val(),
						'relative_path': str_help.str(file_url-org.iff.infra.util.StringHelper.fixUrl(register_value.path)-file_name).scut('/').val(),
						'last_scan': last_scan/*, size, content, last_modify*/]
				}
			}
		}
		del_files.addAll(TCFileStore.get_del(register_value.'path', last_scan))
		TCFileStore.del(del_files)
		TCFileStore.set(files)
	}
	def static scaner(){
		Thread.start{
			def scan_dirs, pause
			while(true){
				TCHelper.debug('TCScanFile.scaner():start:{0}', TCRegister.get('scan_dir'))
				while((scan_dirs=TCRegister.get('scan_dir'))){
					TCHelper.debug('TCScanFile.scaner():{0}', scan_dirs)
					while((pause=TCRegister.get('timer','groovy_scan_pause')?.value)){
						TCHelper.debug('TCScanFile.scaner():paused')
						java.util.concurrent.TimeUnit.SECONDS.sleep(TCRegister.get('timer','groovy_scan_time')?.value ?: 10)
					}
					scan_dirs.each{name, scan_dir->
						TCHelper.debug('TCScanFile.scaner():{0}', scan_dir)
						TCScanFile.scan(scan_dir)
					}
					java.util.concurrent.TimeUnit.SECONDS.sleep(TCRegister.get('timer','groovy_scan_time')?.value ?: 10)
				}
				java.util.concurrent.TimeUnit.SECONDS.sleep(TCRegister.get('timer','groovy_scan_time')?.value ?: 10)
			}
		}
	}
}

class TCXmlJsonDB{
	def static parse_xml_to_map(map, xmlfiles){
		def xml_struct=(map==null ? [:] : map)
		xmlfiles.each{file->
			println "file: ${file}"
			parse_xml(xml_struct, file)
		}
		xml_struct
	}
	
	def static parse_dbdata_to_map(map, xml_datas){//xml_table{id, parent_id, type, element_name, json}
		def xml_struct=(map==null ? [:] : map)
		def pid_map=[:]
		xml_datas.each{row, pid=(row.parent_id ?: '-') ->
			(pid_map."${pid}"=(pid_map."${pid}" ?: [])).add(row)
		}
	
		pid_map.'-'.each{root->
			parse_dbdata(xml_struct, pid_map, root)
		}
		xml_struct
	}
	
	def static combine_xmlmap_with_dbdatamap(maps){
		// not support
	}
	
	def static export_map_to_xmlfile(){
		// not support
	}
	
	def static parse_dbdata(xml_struct, pid_map, root){
		def map=xml_struct
		map.put(root.element_name, (map.get(root.element_name) ?: [:]) )
		def level=[map.get(root.element_name)]// use for recursion
		def childrenClosure={children, cls->
			children.each{child, json=(child.json ?: '{}'), cid=child.id, element_name=child.element_name->
				def value=new groovy.json.JsonSlurper().parseText(json.size()<4 ? '{}' : json)
				def name=element_name
				def child_children=pid_map.get(cid)
				def attributes=value
				def key=name+(attributes.name ? ('@'+attributes.name): '')
				level << (level.last()."${key}"=(level.last()."${key}" ?: [:]))
				level.last() << attributes
				cls(child_children,cls)
				level.remove(level.size()-1)
			}
		}
		childrenClosure(pid_map.get(root.id),childrenClosure)
		println groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(map))
		map
	}
	
	def static parse_xml(xml_struct, file){
		def xml=new XmlParser().parse(file.startsWith('file:') ? new File(new URL(file).toURI()) : new File(file))
		def map=xml_struct
		map.put(xml.name(), (map.get(xml.name()) ?: [:]) )
		def level=[map.get(xml.name())]// use for recursion
		def childrenClosure={children, cls->
			children.each{child->
				if(child instanceof String){
				}else{
					def name=child.name()
					def child_children=child.children()
					def attributes=child.attributes()
					if(child_children.size()==1 && attributes.size()==0 && child_children[0] instanceof String){
						// is the <![CDATA[]]> or text field
						level.last().put(name, child_children[0])
					}else{
						def key=name+(attributes.name ? ('@'+attributes.name): '')
						level << (level.last()."${key}"=(level.last()."${key}" ?: [:]))
						level.last() << attributes
						cls(child_children,cls)
						level.remove(level.size()-1)
					}
				}
			}
		}
		childrenClosure(xml.children(),childrenClosure)
		println groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(map))
		map
	}
}

class TCLifeCycleEngine{
	def static start(){
		def bean=TCHelper.get_bean('default','TC_LifeCycle_server')
		bean.create()
		bean.process()
	}
}

// @see README-lifecycle.md
class TCLifeCycleBasic{
	def children=[]
	def map_path=''
	def class_manager_name=''
	def node_name=''
	def config=[:]
	def get_data(){
		TCCache.me().getByPath('framework/server_cfg/'+map_path) ?: [:]
	}
	def create(cfg){
	}
	def process(){
		TCHelper.debug('TCLifeCycle.process(),{0},{1},{2}',map_path,class_manager_name,node_name)
		load_cfg()
		load_groovy()
		create_children()
		start()
	}
	def load_cfg(){
		TCHelper.debug('TCLifeCycle.load_cfg(),{0},{1},{2}',map_path,class_manager_name,node_name)
	}
	def load_groovy(){
		TCHelper.debug('TCLifeCycle.load_groovy(),{0},{1},{2}',map_path,class_manager_name,node_name)
		def mgr=TCCLassManager.getManager(class_manager_name, false)
		def data=get_data()
		def groovy_path=data.'groovy_path'?.split(',')
		groovy_path.each{gp, path=org.iff.infra.util.StringHelper.fixUrl(gp)->
			TCPathEventBus.regist_listener('/groovy_change', TCDefaultClasses.get_class_manager_event(path, class_manager_name))
			TCScanFile.scan(['path': path])
			path.startsWith('file:') && TCRegister.register('scan_dir', ['name': path, 'path': path, 'class_manager_name': class_manager_name])
		}
	}
	def create_children(){
		TCHelper.debug('TCLifeCycle.create_children(),{0},{1},{2}',map_path,class_manager_name,node_name)
		def data=get_data(), str_help=TCHelper.str('')
		data.each{k,v->
			if(v instanceof Map){
				def node=k.indexOf('@')>-1 ? k.substring(0, k.indexOf('@')) : k
				def node_name=k.indexOf('@')>-1 ? k.substring(k.indexOf('@')+1) : ''
				def bean=TCHelper.get_bean(class_manager_name,'TC_LifeCycle_'+node)
				println TCCache.me().getByPath('framework/beans/'+class_manager_name)
				println "${k},${node},${node_name}"
				println bean
				bean.create(['node_name':node_name, 'parent_class_manager_name':class_manager_name, 'map_path':map_path+'/'+k])
				children << bean
			}
		}
	}
	def start(){
		TCHelper.debug('TCLifeCycle.start(),children:{0}',children)
		children.each{child->
			Thread.start{
				child.process() 
			}
		}
	}
	def stop(){
		TCHelper.debug('TCLifeCycle.stop(),{0},{1},{2}',map_path,class_manager_name,node_name)
		children.each{child-> child.stop() }
	}
}

@TCBean(name='TC_LifeCycle_server')
class TCLifeCycle_server extends TCLifeCycleBasic{
	def create(cfg){
		map_path='server'
		class_manager_name='default'
		node_name=''
	}
}
@TCBean(name='TC_LifeCycle_containers')
class TCLifeCycle_containers extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_container')
class TCLifeCycle_container extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		node_name=cfg.'node_name'
		def mgr=TCCLassManager.getManager(node_name, true)
		class_manager_name=node_name
	}
}
@TCBean(name='TC_LifeCycle_resources')
class TCLifeCycle_resources extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_resource')
class TCLifeCycle_resource extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_applications')
class TCLifeCycle_applications extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_application')
class TCLifeCycle_application extends TCLifeCycleBasic{
	def server
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
		create_application(cfg)
	}
	def create_application(cfg){
		def data=get_data()
		if(data.'type'=='web'){
			println "create_application=${data}"
			server=TCHelper.get_bean(class_manager_name, 'TC_Server', true)
			server.class_manager_name=class_manager_name
			server.web_dir=data.'web_dir'
		}
	}
	def start(){
		server?.start()
		super.start()
	}
	def stop(){
		server?.stop()
		super.stop()
	}
}
@TCBean(name='TC_LifeCycle_settings')
class TCLifeCycle_settings extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_setting')
class TCLifeCycle_setting extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_modules')
class TCLifeCycle_modules extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_module')
class TCLifeCycle_module extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_actions')
class TCLifeCycle_actions extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}
@TCBean(name='TC_LifeCycle_action')
class TCLifeCycle_action extends TCLifeCycleBasic{
	def create(cfg){
		map_path=cfg.'map_path'
		class_manager_name=cfg.'parent_class_manager_name'
		node_name=cfg.'node_name'
	}
}

class TCStarter{
	def system_properties_to_map(){
		def map=TCHelper.prop2map(System.getProperties()){true}
		TCCache.me().with{
			get('props').putAll(map)
			put("app_root", TCHelper.path_clean(map.app_root))
		}
		TCHelper.debug('system_properties_to_map(): {0}', TCCache.me().'props')
	}
	def properties_file_to_map(respath, version){
		TCCache.me().'config'.putAll(org.iff.infra.util.PropertiesHelper.loadProperties(respath, version))
		TCHelper.debug('properties_file_to_map(): {0}', TCCache.me().'config')
	}
	
	/*00.)loading system groovy files TCFramework.groovy*/
	def load_framework_groovy(){
		// this lifecycle process in TCCLassManager
		TCHelper.debug('load_framework_groovy()')
	}
	/*01.)loading META-INF/tc-framework.properties*/
	def load_properties(){
		system_properties_to_map()
		properties_file_to_map(TCProp.tc_properties_filepath ?: 'classpath://META-INF/tc-framework.properties', TCProp.tc_properties_version ?: 'order.loading.configure')
		TCHelper.debug('load_properties()')
	}
	
	/*01.1)initialize every thing*/
	def init(){
		TCCache.me().setByPath('framework/register',[:])
		TCCache.me().setByPath('framework/path_event_bus',[:])
		TCCache.me().setByPath('framework/classes',[:])
		TCCache.me().setByPath('framework/beans',[:])
		TCCache.me().setByPath('framework/actions',[:])
		TCCache.me().setByPath('framework/urlmappings',[:])
		TCCache.me().setByPath('framework/file_store',[:])
		TCCache.me().setByPath('framework/server_cfg',[:])
		TCCache.me().setByPath('framework/server_xml',[:])
		
		TCProp.metaClass.static.propertyMissing   = { String name -> TCCache.me().props.get(name)  }
		TCConfig.metaClass.static.propertyMissing = { String name -> TCCache.me().config.get(name) }
		TCPathEventBus.regist_listener('/dead', TCPathEventBus.dead_event)
		TCPathEventBus.regist_listener('/groovy_classes', TCDefaultClasses.groovy_classes_parse_event)
		TCRegister.register('parse_classes', TCDefaultClasses.parse_classes)
		TCRegister.register('parse_classes', TCDefaultClasses.parse_action)
		TCRegister.register('timer', ['name': 'groovy_scan_time',  'value': 10])
		TCRegister.register('timer', ['name': 'groovy_scan_pause', 'value': false])
		TCScanFile.scaner()
		TCHelper.debug('init()')
	}
	/*01.2)framework classes*/
	def process_framework_classes(){
		def class_manager=TCCLassManager.getManager('default', false)
		def script_loader_map=class_manager.getScriptMapClassLoader()
		script_loader_map.each{script_name, class_loader->
			def path=script_name.substring(0, script_name.lastIndexOf('/system/'))+'/'
			def file_struct=[
				'name': script_name.substring(script_name.lastIndexOf('/')+1),
				'type': script_name.substring(script_name.lastIndexOf('.')+1),
				'url' : script_name,
				'path': path,
				'dir' : path.startsWith('jar:') ? path.substring(path.indexOf('!/')+2) : path.substring(path.indexOf(':///')+4),
				'relative_path': script_name.substring(script_name.lastIndexOf('/system/')+1, script_name.lastIndexOf('/')+1),
				'last_scan': 0,
				'last_modify': 0/*, size, contents*/]
			TCPathEventBus.sync_event('groovy_classes', ['classes': class_loader.loadedClasses, 'class_manager_name': 'default', 'file_struct': file_struct])
			TCHelper.debug('process_framework_classes()')
		}
	}

	/*02.)loading server configure from jar and filesystem*/
	def load_server_xml(){
		def server_xmls=TCProp.'tc_server_xml'.split(',')
		def xmls=[]
		server_xmls.each{file->
			xmls.addAll(org.iff.infra.util.ResourceHelper.loadResources(file, 'tc-framework-server.xml', '*', null))
		}
		TCCache.me().setByPath('framework/server_xml', xmls)
		TCHelper.debug('load_server_xml():{0}', xmls)
	}

	/*03.)loading server configure from database*/
	def load_server_data(){
		TCHelper.debug('load_server_data()')
	}

	/*04.)combine server configure*/
	def combine_server_cfg(){
		def server_cfg=[:]
		TCXmlJsonDB.parse_xml_to_map(server_cfg,TCCache.me().getByPath('framework/server_xml')) 
		TCCache.me().setByPath('framework/server_cfg', server_cfg)
		TCHelper.debug('combine_server_cfg():{0}', server_cfg)
	}

	/*05.)start server*/
	def start_server(){
		TCLifeCycleEngine.start()
		TCHelper.debug('start_server()')
	}

	def start(){
		init()
		load_framework_groovy()
		load_properties()
		process_framework_classes()
		load_server_xml()
		load_server_data()
		combine_server_cfg()
		start_server()
		TCHelper.debug('start()')
		java.util.concurrent.TimeUnit.SECONDS.sleep(10000)
	}
}
