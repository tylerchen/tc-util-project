package org.iff.groovy.util

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
