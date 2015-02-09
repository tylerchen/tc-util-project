package org.iff.groovy.util

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
	def static debug(message,error,args){
		if(message){
			if(error && !(error in Throwable)){
				args=error
				error=null
			}
			if(args){
				if(!(args in Collection || args in Object[])){
					args=[args]
				}
			}
			if(error){
				org.iff.infra.util.Logger.debug(org.iff.infra.util.FCS.FCS(message.toString(), args), error)
			}else{
				org.iff.infra.util.Logger.debug(org.iff.infra.util.FCS.FCS(message.toString(), args))
			}
		}
	}
	def static warn(message,error,args){
		if(message){
			if(error && !(error in Throwable)){
				args=error
				error=null
			}
			if(args){
				if(!(args in Collection || args in Object[])){
					args=[args]
				}
			}
			if(error){
				org.iff.infra.util.Logger.warn(org.iff.infra.util.FCS.FCS(message.toString(), args), error)
			}else{
				org.iff.infra.util.Logger.warn(org.iff.infra.util.FCS.FCS(message.toString(), args))
			}
		}
	}
	def static info(message,error,args){
		if(message){
			if(error && !(error in Throwable)){
				args=error
				error=null
			}
			if(args){
				if(!(args in Collection || args in Object[])){
					args=[args]
				}
			}
			if(error){
				org.iff.infra.util.Logger.info(org.iff.infra.util.FCS.FCS(message.toString(), args), error)
			}else{
				org.iff.infra.util.Logger.info(org.iff.infra.util.FCS.FCS(message.toString(), args))
			}
		}
	}
	def static error(message,error,args){
		if(message){
			if(error && !(error in Throwable)){
				args=error
				error=null
			}
			if(args){
				if(!(args in Collection || args in Object[])){
					args=[args]
				}
			}
			if(error){
				org.iff.infra.util.Logger.error(org.iff.infra.util.FCS.FCS(message.toString(), args), error)
			}else{
				org.iff.infra.util.Logger.error(org.iff.infra.util.FCS.FCS(message.toString(), args))
			}
		}
	}
	def static trace(message,error,args){
		if(message){
			if(error && !(error in Throwable)){
				args=error
				error=null
			}
			if(args){
				if(!(args in Collection || args in Object[])){
					args=[args]
				}
			}
			if(error){
				org.iff.infra.util.Logger.trace(org.iff.infra.util.FCS.FCS(message.toString(), args), error)
			}else{
				org.iff.infra.util.Logger.trace(org.iff.infra.util.FCS.FCS(message.toString(), args))
			}
		}
	}
}