package org.iff.groovy.framework

// map path: framework/{type}/{class_loader}/{container}/{type}
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
	def static ex_call(Object[] p){
        if(p && p.size()>0 && p[-1] in Closure){
			def info=p.size()>1 ? p[0] : null, error=p.size()>2 ? p[1] : null
            try{
				if(info){
					def level = info.size()>0 ? info[0] : 'debug', msg = info.size()>1 ? info[1] : ''
					level = (level in ['debug', 'warn', 'info', 'error', 'trace']) ? level : 'debug'
					TCHelper."$level"(msg, info.size()>2 ? info[2..-1] : null)
				}
                p[-1](p)
            }catch(e){
				if(error){
					def level = error?.size()>0 ? error[0] : 'debug', msg = (error?.size()>1 ? error[1] : '{0}' )
					level = (level in ['debug', 'warn', 'info', 'error', 'trace']) ? level : 'debug'
					TCHelper."$level"(msg, error?.size()>2 ? (error[2..-1]+[e]) : [e])
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

class TCGetter{
	def static get_props(container_name=null){
		if(container_name==null){
			TCCache.me().getByPath('framework/global/props', true)
		}else{
			TCCache.me().getByPath("framework/prop/${container_name}", true)
		}
	}
	def static get_prop(name, container_name=null){
		if(container_name==null){
			get_props().get(name)
		}else{
			get_props(container_name).get(name) ?: get_props().get(name)
		}
	}
	def static get_configs(container_name=null){
		if(container_name==null){
			TCCache.me().getByPath('framework/global/configs', true)
		}else{
			TCCache.me().getByPath("framework/config/${container_name}", true)
		}
	}
	def static get_config(name, container_name=null){
		if(container_name==null){
			get_configs().get(name)
		}else{
			get_configs(container_name).get(name) ?: get_configs().get(name)
		}
	}
	def static get_class_manager(){
		TCCache.me().getByPath("framework/global/classmanager", true)
	}
	def static get_class_loader(){
		get_class_manager().get()
	}
	def static get_class_map(){
		TCCache.me().getByPath('framework/global/classmap', true)
	}
	def static get_bean_map(){
		TCCache.me().getByPath("framework/global/bean", true)
	}
	def static get_bean(bean_name, container_name='default', force_create=false){
		if(force_create){
			def bean_cls=get_bean_map().get(bean_name)
			if(!bean_cls){
				return null
			}else{
				return get_class_manager().loadClass(bean_cls.clazz.name).newInstance()
			}
		}
		
		def bean=TCCache.me().getByPath("framework/bean/${container_name}/${bean_name}")
		if(bean){
			return bean
		}else{
			def bean_cls=get_bean_map().get(bean_name)
			if(!bean_cls){
				return null
			}else{
				bean = get_class_manager().loadClass(bean_cls.clazz.name).newInstance()
				TCCache.me().setByPath("framework/bean/${container_name}/${bean_name}", bean)
			}
		}
		return bean
	}
	def static get_action_map(){
		TCCache.me().getByPath("framework/global/action", true)
	}
	def static get_action_url_map(container_name='default'){
		TCCache.me().getByPath("framework/action/${container_name}", true)
	}
	def static get_action(action_name, container_name='default'){
		def action=TCCache.me().getByPath("framework/action/${container_name}/${action_name}")
		if(action){
			return get_class_manager().loadClass(action.clazz.name).newInstance()
		}else if(container_name != 'default'){
			return get_action(action_name, 'default')
		}
	}
	def static get_url_map(){
		TCCache.me().getByPath("framework/global/url", true)
	}
	def static get_url_ins(container_name){
		TCCache.me().getByPath("framework/url/${container_name}", true)
	}
	def static get_register(){
		TCCache.me().getByPath('framework/global/register', true)
	}
	def static get_eventbus(){
		TCCache.me().getByPath('framework/global/eventbus', true)
	}
	def static get_event(event_path){
		TCCache.me().getByPath("framework/global/eventbus/${event_path}", true)
	}
	def static get_file_store(){
		TCCache.me().getByPath('framework/global/file_store', true)
	}
}

class TCRegister{
	def static regist(type, value){// value={name, path,...}
		TCHelper.debug('register():{0},{1}', type, value.name)
		def reg_val=TCGetter.get_register()
		(reg_val[type]=reg_val[type] ?: [:]).put(value.'name', value)
	}
	def static get(String type, name=null){
		def reg_val=TCGetter.get_register()
		name ? reg_val?.get(type)?.get(name) : reg_val?.get(type)
	}
}

class TCPathEventBus{
	def static regist(path, listener){
		TCHelper.debug('regist_listener():{0},{1}', path, listener)
		def map=TCGetter.get_event(path)
		(map.'listener'=map?.'listener' ?: []).add(listener)
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
		def map=TCGetter.get_event(path)
		TCHelper.ex_call(['debug','remove_listener():{0}', path]){
			map.'listener'?.remove(listener)
		}
	}
	def static async_event(path, events){
		def map=TCGetter.get_event(path)
		def listeners=map.'listener' ?: []
		if(listeners.size()<1){
			(path!='dead') && async_event('dead', ['source_path': path, 'source_events': events])
		}else{
			listeners.each{lsn->
				Thread.start{
					TCHelper.ex_call(['debug', 'async_event():{0}', path],['error', 'async_event():{0},{1}', path]){
						lsn.listen(path, events)
					}
				}
			}
		}
	}
	def static sync_event(path, events){
		TCHelper.debug('sync_event: {0}', path)
		def map=TCGetter.get_event(path)
		def listeners=map?.'listener' ?: []
		if(listeners.size()<1){
			(path!='dead') && sync_event('dead', ['source_path': path, 'source_events': events])
		}else{
			listeners.each{lsn->
				TCHelper.ex_call(['debug', 'sync_event():{0}', path],['error', 'sync_event():{0},{1}', path]){
					lsn.listen(path, events)
				}
			}
		}
	}
	def static dead_event=[
		listen:{path, events->
			TCHelper.warn('[dead_event][{0}]',events?.'source_path' ?: path)
		}
	]
}

class TCComplieByOrder{
	def static compile(file_structs){
		TCHelper.debug('compile():{0}', file_structs.collect{it.url})
		def order=["system/util/","system/framework/","system/extends/","app/model/","app/service/","app/view/"]
		def class_manager=TCGetter.get_class_manager()
		order.each{ opath ->
			TCHelper.debug('Compile Path: {0},{1}',opath,file_structs.collect{it.url})
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
			
			for(; errorTimes>0; errorTimes--){
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
			TCHelper.debug('compile(): classes {0}', classes.collect{it.name})
			if(classes.size()>0){
				TCPathEventBus.sync_event('groovy_classes', ['classes': classes, 'file_struct': file_structs])
			}
		}// END-each:order
	}
}
class TCDefaultClasses{
	def static parse_classes_event=[
		'name' : 'parse_classes',
		'listen': {path, events->// {classes, file_struct}
			TCHelper.debug('parse_classes.parse():events:{0},{1}', events?.classes.collect{it.name}, events?.file_struct?.url)
			def class_map=TCGetter.get_class_map()
			events.classes.each{classes->
				classes.each{cls, name=cls.name->
					if(class_map[name]){
						TCHelper.warn('class name exists: {1}',name)
					}
					class_map[name]=[clazz:cls, class_name: name, url:events.file_struct.url]
				}// END-classes.each
			}// END-events.classes.each
			TCHelper.debug('parse_classes.parse():class_map:{0}', class_map.collect{k,v->k})
			TCPathEventBus.sync_event('parsed_classes', events)
		}// END-parse
	]
	def static parse_beans_event=[
		'name' : 'parse_beans',
		'listen': {path, events->// {classes, file_struct}
			TCHelper.debug('get_parse_beans.parse():events:{0},{1}', events?.classes.collect{it.name}, events?.file_struct?.url)
			def bean_map=TCGetter.get_bean_map()
			events.classes.each{classes->
				classes.each{cls, name=cls.name->
					def anno=cls.getAnnotation(org.iff.infra.util.groovy.TCBean.class)
					if(anno){
						def anno_name=anno.name(), anno_order=anno.order()
						def bean=bean_map[anno_name] ?: [:]
						if(bean){
							TCHelper.warn('bean name exists: {1}',anno_name)
						}
						def bean_val=[clazz: cls, class_name: name, name: anno_name, order: anno_order, url:events.file_struct.url, instance: null]
						bean_map[anno_name] = (!bean || bean.order < bean_val.order) ? (bean << bean_val) : bean
					}//END-if(anno)
				}// END-classes.each
			}// END-events.classes.each
			TCHelper.debug('parse_beans.parse():bean_map:{0}', bean_map.collect{k,v->k})
			TCPathEventBus.sync_event('parsed_beans', events)
		}// END-parse
	]
	def static parse_actions_event=[
		'name' : 'parse_actions',
		'listen': {path, events->// {classes, file_struct}
			TCHelper.debug('get_parse_beans.parse():events:{0},{1}', events?.classes.collect{it.name}, events?.file_struct?.url)
			def action_map=TCGetter.get_action_map()
			def url_map=TCGetter.get_url_map()
			events.classes.each{classes->
				classes.each{cls, name=cls.name->
					def anno=cls.getAnnotation(org.iff.infra.util.groovy.TCAction.class)
					if(anno){
						def anno_name=anno.name(), anno_order=anno.order()
						def action=action_map[anno_name] ?: [:]
						if(action){
							TCHelper.warn('action name exists: {1}',anno_name)
						}
						TCHelper.debug('[parse_actions_event],process action:{0}', name)
						action_map[anno_name]=[clazz: cls, 'action':name,'context':anno_name]
						cls.declaredMethods.each{method->
							def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
							def method_name=method.name, modifiers=method.modifiers, context=anno_name
							if(method_name.indexOf('$')<0 && !(method_name in exclude) && !java.lang.reflect.Modifier.isStatic(modifiers) && java.lang.reflect.Modifier.isPublic(modifiers)){
								if('index'==method_name){
									url_map[context]=[clazz: cls, 'action':name,'method':method_name,'context':context]
								}
								url_map["${context}/${method_name}"]=[clazz: cls, 'action':name,'method':method_name,'context':context]
							}
						}
						[0].each{// add method to action
							cls.metaClass.params=[:]
							cls.metaClass._request_params=[:]
							cls.metaClass._request_userAgent=[:]
							cls.metaClass._configs=[:]
							if(!cls.metaClass.hasMetaMethod('getConfigs', [Object[]] as Class[])){
								cls.metaClass.getConfigs={Object[] ps->
									if((ps && ps[0]==true) || _configs==null || _configs.size()==0){
										def prefix=name+'.'
										TCGetter.get_configs().findAll{k,v->
											if(k.startsWith(prefix)){
												_configs.put(k.substring(prefix.size()),v)
											}
										}
									}
									_configs
								}
							}
							if(!cls.metaClass.hasMetaMethod('addUrlParam', [Object,Object] as Class[])){
								cls.metaClass.addUrlParam={key, value->
									_request_params.put(key, urlEncode(value))
									delegate
								}
							}
							if(!cls.metaClass.hasMetaMethod('urlEncode', [Object] as Class[])){
								cls.metaClass.urlEncode={url->
									url ? java.net.URLEncoder.encode(url,'UTF-8') : ''
								}
							}
							if(!cls.metaClass.hasMetaMethod('urlDecode', [Object] as Class[])){
								cls.metaClass.urlDecode={url->
									url ? java.net.URLDecoder.decode(url,'UTF-8') : ''
								}
							}
							if(!cls.metaClass.hasMetaMethod('redirect', [Object] as Class[])){
								cls.metaClass.redirect={url->
									if(url && _request_params.size()>0){
										if(url.endsWith('?')||url.endsWith('&')){
											url=url+_request_params.collect{k,v-> k+'='+v}.join('&')
										}else if(url.indexOf('?')>-1){
											url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')
										}else{
											url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')
										}
									}
									params.response.sendRedirect(url ? url.toString() : '')
								}
							}
							if(!cls.metaClass.hasMetaMethod('forward', [Object] as Class[])){
								cls.metaClass.forward={url->
									if(url && _request_params.size()>0){
										if(url.endsWith('?')||url.endsWith('&')){
											url=url+_request_params.collect{k,v-> k+'='+v}.join('&')
										}else if(url.indexOf('?')>-1){
											url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')
										}else{
											url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')
										}
									}
									params.request.getRequestDispatcher(url ? url.toString() : '').forward(params.request, params.response)
								}
							}
							if(!cls.metaClass.hasMetaMethod('userAgent', [] as Class[])){
								cls.metaClass.userAgent={
									if(_request_userAgent.size()<1){
										_request_userAgent << org.iff.infra.util.HttpHelper.userAgent(params.request.getHeader('User-Agent'))
									}
									_request_userAgent
								}
							}
						}//END-add method to action
					}//END-if(anno)
				}// END-classes.each
			}// END-events.classes.each
			TCHelper.debug('parse_actions.parse():action_map:{0}', action_map.collect{k,v->k})
			TCHelper.debug('parse_actions.parse():url_map:{0}', url_map.collect{k,v->k})
			TCPathEventBus.sync_event('parsed_actions', events)
		}// END-parse
	]
	def static get_parse_event(event_path){
		[
			'name'  : 'parse_event',
			'listen': {path, events->
				TCHelper.debug('get_parse_event.listen():{0},{1}', events?.classes.collect{it.name}, events?.file_struct?.url)
				TCHelper.warn('[get_parse_event][{0}]',path)
				TCRegister.get(event_path).each{k,v->
					v.parse(events)
				}
			}
		]
	}
	def static class_change_event=[
		'name'   : 'groovy_change',
		'listen' : {path, events->
			TCHelper.debug('class_change_event.listen():{0}', path)
			def class_manager=TCGetter.get_class_manager()
			events.'delete'?.each{file_struct->
				TCHelper.debug('[delete class][{0}]',file_struct.url)
				class_manager.removeClassNameScriptMapping(file_struct.url)
			}
			events.'modify' && TCComplieByOrder.compile(events.'modify')
		}
	]
}

class TCFileStore{
	def static set(file_struct){// file_struct={name, type, url, path, dir, relative_path, size, content, last_modify, last_scan}
		TCHelper.debug('TCFileStore.set():{0}', file_struct.url)
		def file_val=TCGetter.get_file_store()
		def event_file_struct=[], tmp_file_struct
		(TCHelper.isObjects(file_struct) ? file_struct : [file_struct]).each{fs->
			tmp_file_struct=file_val[fs.url]
			tmp_file_struct ? (tmp_file_struct << fs) : (tmp_file_struct=fs)
			file_val[fs.url] || (file_val[fs.url]=tmp_file_struct)
			event_file_struct.add(tmp_file_struct)
			load_content(tmp_file_struct)
		}
		TCHelper.debug('TCFileStore:{0}', file_val.collect{k,v->k})
		 if(event_file_struct.size()>0){
			 TCPathEventBus.sync_event('groovy_change', ['modify':event_file_struct])
		 }
	}
	def static del(file_struct){
		def fix_file_struct=TCHelper.isObjects(file_struct) ? file_struct : [file_struct]
		TCHelper.debug('TCFileStore.del():{0}', fix_file_struct.collect{it.url})
		if(fix_file_struct){
			def file_val=TCGetter.get_file_store()
			file_val && fix_file_struct.each{fs-> file_val.remove(fs.url) }
			if(fix_file_struct.size()>0){
				TCPathEventBus.sync_event('groovy_change', ['delete':fix_file_struct])
			}
		}
	}
	def static del_dir(url){
		TCHelper.debug('TCFileStore.del_dir():{0}', url)
		def file_val=TCGetter.get_file_store()
		def result=file_val.findAll{k,v->k.startsWith(url)}
		file_val && result.each{k,v-> file_val.remove(k)}
		if(result.size()>0){
			TCPathEventBus.sync_event('groovy_change', ['delete':result])
		}
	}
	//jar:file:///C:/Users/Tyler/Desktop/2015/groovy-2.4.0/bin/../lib/tc-util-project-1.0.jar!/META-INF/tc-framework/app/view/A.groovy
	//file:///G:/bak/app_root/app/view/a.groovy
	def static get(url){// url=file:///xx/ or jar:///xx/
		def file_val=TCGetter.get_file_store()
		def result=file_val?.get(url)
		!result && !url.endsWith('.groovy') && (result=file_val.findAll{k,v->k.startsWith(url)})
		result
	}
	def static get_del(url, last_scan){
		def file_val=TCGetter.get_file_store()
		def result=file_val.findAll{k,v->
			k.startsWith(url) && !k.endsWith('/system/framework/TC.groovy') && (v.last_scan!=last_scan)
		}.collect{k,v->v}
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
					file_struct.last_scan=last_scan
					if(file_struct.last_modify!=last_modify){
						file_struct['last_modify']=last_modify
						files << file_struct
						TCHelper.debug('[TCScanFile.scan]file last modify time change: {0}', file_url)
					}
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
					TCHelper.debug('[TCScanFile.scan]new groovy file: {0}', file_url)
				}
			}
		}
		del_files.addAll(TCFileStore.get_del(register_value.'path', last_scan))
		if(del_files.size()>0){
			TCFileStore.del(del_files)
		}
		if(files.size()>0){
			TCFileStore.set(files)
		}
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
	def static get(node_type){
		def bean=TCGetter.get_bean('TC_LifeCycle_'+node_type,'default', true)
		!bean && TCHelper.debug('=========bean:{0} not found=====', node_type)
		bean ?: new TCLifeCycleBasic()
	}
	def static start(){
		def bean=TCLifeCycleEngine.get('server')
		bean.create([map_path: 'server', node_type: 'server'])
		bean.process()
	}
}

// @see README-lifecycle.md
class TCLifeCycleBasic{
	def children=[]
	def map_path
	def node_type
	def node_name
	def container_name
	def config=[:]
	def get_data(){
		TCCache.me().getByPath('framework/server_cfg/'+map_path) ?: [:]
	}
	def create(cfg){
		map_path=cfg.map_path
		node_type=cfg.node_type
		node_name=cfg.node_name
		container_name=cfg.container_name
	}
	def process(){
		TCHelper.debug('TCLifeCycle.process(),{0},{1},{2}',map_path,node_name,node_type)
		load_cfg()
		load_groovy()
		create_children()
		start()
	}
	def load_cfg(){
		TCHelper.debug('TCLifeCycle.load_cfg(),{0},{1},{2}',map_path,node_name,node_type)
	}
	def load_groovy(){
		TCHelper.debug('TCLifeCycle.load_groovy(),{0},{1},{2}',map_path,node_name,node_type)
		def mgr=TCGetter.get_class_manager()
		def data=get_data()
		def groovy_path=data.'groovy_path'?.split(',')
		groovy_path.each{gp, path=org.iff.infra.util.StringHelper.fixUrl(gp)->
			TCScanFile.scan(['path': path])
			path.startsWith('file:') && TCRegister.regist('scan_dir', ['name': path, 'path': path])
		}
	}
	def create_children(){
		TCHelper.debug('TCLifeCycle.create_children(),{0},{1},{2}',map_path,node_name,node_type)
		def data=get_data()
		data.each{k,v->
			if(v instanceof Map){
				def node_type_child=k.indexOf('@')>-1 ? k.substring(0, k.indexOf('@')) : k
				def node_name_child=k.indexOf('@')>-1 ? k.substring(k.indexOf('@')+1) : ''
				def bean=TCLifeCycleEngine.get(node_type_child)
				bean.create(['node_type': node_type_child,'node_name':node_name_child, 'map_path':map_path+'/'+k, 'container_name':container_name])
				children << bean
			}
		}
	}
	def start(){
		TCHelper.debug('TCLifeCycle.start(),children:{0}',children.collect{it.map_path})
		children.each{child->
			child.process() 
		}
	}
	def stop(){
		TCHelper.debug('TCLifeCycle.stop(),{0},{1},{2}',map_path,class_manager_name,node_name)
		children.each{child-> child.stop() }
	}
}

@TCBean(name='TC_LifeCycle_container')
class TCLifeCycle_container extends TCLifeCycleBasic{
	def server
	def create(cfg){
		super.create(cfg)
		def data=get_data()
		container_name=data.name
	}
	def create_container(){
		def data=get_data()
		if(data.'type'=='web'){
			println "create_container=${data}"
			server=TCGetter.get_bean('TC_Server',node_name, true)
			server.container_name=node_name
			server.web_dir=data.'web_dir'
			server.port=data.'port'?.toInteger() ?: 8080
		}
	}
	def start(){
		println '================\n\n\n\n\n\n\n\n\n\n\n\n================'
		create_container()
		server?.start()
		TCHelper.debug('TCLifeCycle.start(),children:{0}',children.collect{it.map_path})
		Thread.start{
			children.each{child->
				child.process() 
			}
		}
	}
	def stop(){
		server?.stop()
		super.stop()
	}
}
@TCBean(name='TC_LifeCycle_action')
class TCLifeCycle_action extends TCLifeCycleBasic{
	def create(cfg){
		super.create(cfg)
		add_action_url_map()
	}
	def add_action_url_map(){
		def data=get_data()
		def action_url_map=TCGetter.get_action_url_map(container_name)
		action_url_map.put(data.url, data.ref_action)
		println '--------------================>>>'
	}
}

class TCStarter{
	def system_properties_to_map(){
		def map=TCHelper.prop2map(System.getProperties()){true}
		TCGetter.get_props().putAll(map)
		TCHelper.debug('system_properties_to_map(): {0}', TCGetter.get_props())
	}
	def properties_file_to_map(respath, version){
		TCGetter.get_configs().putAll(org.iff.infra.util.PropertiesHelper.loadProperties(respath, version))
		TCHelper.debug('properties_file_to_map(): {0}', TCGetter.get_configs())
	}
	
	/*00.0)initialize every thing*/
	def init(){
		
		TCPathEventBus.regist('dead', TCPathEventBus.dead_event)
		TCPathEventBus.regist('groovy_change', TCDefaultClasses.class_change_event)
		TCPathEventBus.regist('groovy_classes', TCDefaultClasses.parse_classes_event)
		TCPathEventBus.regist('parsed_classes', TCDefaultClasses.parse_beans_event)
		TCPathEventBus.regist('parsed_beans', TCDefaultClasses.parse_actions_event)
		TCRegister.regist('timer', ['name': 'groovy_scan_time',  'value': 10])
		TCRegister.regist('timer', ['name': 'groovy_scan_pause', 'value': false])
		TCScanFile.scaner()
		
		TCCache.me().setByPath('framework/global/classmanager', TCCLassManager.me())
		
		TCHelper.debug('init finished...')
	}
	
	/*00.)loading system groovy files TCFramework.groovy*/
	def load_framework_groovy(){
		// this lifecycle process in TCCLassManager
		TCHelper.debug('load_framework_groovy()')
	}
	/*01.)loading META-INF/tc-framework.properties*/
	def load_properties(){
		system_properties_to_map()
		properties_file_to_map(TCGetter.get_prop('tc_properties_filepath') ?: 'classpath://META-INF/tc-framework.properties', TCGetter.get_prop('tc_properties_version') ?: 'order.loading.configure')
		TCHelper.debug('load_properties()')
	}
	
	/*01.2)framework classes*/
	def process_framework_classes(){
		def class_manager=TCGetter.get_class_manager()
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
			TCPathEventBus.sync_event('groovy_classes', ['classes': class_loader.loadedClasses, 'file_struct': file_struct])
			TCHelper.debug('process_framework_classes()')
		}
	}

	/*02.)loading server configure from jar and filesystem*/
	def load_server_xml(){
		def server_xmls=TCGetter.get_prop('tc_server_xml').split(',')
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
		//java.util.concurrent.TimeUnit.SECONDS.sleep(10000)
	}
}
