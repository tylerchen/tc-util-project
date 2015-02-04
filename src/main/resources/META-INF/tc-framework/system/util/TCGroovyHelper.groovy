package org.iff.groovy.util

class TCDsl{
	def static _me=new TCDsl()
	def methodMissing(String name, args) {
		def params=[]
		if(name.startsWith('substr ')){
			args.each{ arg ->
				if(!(arg in Closure)){
					params << arg
				}else{
					params << arg(params.clone())
				}
			}
		}
		return params.size()>0 ? params[params.size()-1] : null
	}
}
TCDsl.metaClass.'static'.invokeMethod = { String name, args ->
	return TCDsl.@_me.methodMissing(name, args)
}