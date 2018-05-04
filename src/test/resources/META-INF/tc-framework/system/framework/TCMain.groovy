package org.iff.groovy.framework


@TCFramework(name="TCMain")
class TCMain{
	def main(){
		def initer=TCCache.me().framework_instance.TCIniter
		initer.init()
		//
		def handlers=TCCache.me().framework.action_handlers
		handlers.add(new TCChain(){
			def process(params){
				TCHelper.debug('[BeforeHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		})
		handlers.add(TCCache.me().framework_instance.TCActionHandler)
		handlers.add(new TCChain(){
			def process(params){
				TCHelper.debug('[AfterHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		})
		//
		def starts=TCCache.me().framework_instance.TCStarts
		starts.start()
	}
}
@TCFramework(name="TCIniter")
class TCIniter{
	def init(){
		def list=TCCache.me().framework.inits// data structure=[instance,method]
		list.each{
			it.instance."$it.method"()
		}
	}
}
@TCFramework(name="TCStarts")
class TCStarts{
	def start(){
		def list=TCCache.me().framework.starts// data structure=[instance,method]
		list.each{
			def final start=it
			Thread.start{
				TCHelper.debug('[TCStarts]: {0}',start)
				start.instance."$start.method"()
			}//end thread
		}
	}
}
@TCFramework(name="TCStopper")
class TCStopper{
	def stop(){
		def list=TCCache.me().framework.stops// data structure=[instance,method]
		list.each{
			it.instance."$it.method"()
		}
	}
}
class TCFilter implements javax.servlet.Filter{
	def handler
	def target_prefix
	def void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException{
		TCHelper.debug('[TCFilter Init]')
		println "-----> ${TCCache.me()}"
		TCCache.me().servlet.put('filterConfig', filterConfig)
		TCCache.me().servlet.put('servletContext', filterConfig.servletContext)
		target_prefix = filterConfig.getInitParameter('target_prefix') ?: ''
		if(!handler){
			def tmp=new TCChain()
			def list=TCCache.me().framework.action_handlers
			list.reverse().each{
				it.nextChain=tmp
				tmp=it
			}
			handler=tmp
		}
	}
	def void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response,javax.servlet.FilterChain chain) throws IOException, javax.servlet.ServletException{
		request.setCharacterEncoding('UTF-8')
		response.setCharacterEncoding('UTF-8')
		def target=request.servletPath
		TCHelper.debug('[doFilter] handler:{0}, target:{1}, target_prefix:{2}', handler.class.name, target, target_prefix)
		if(target.endsWith('.gsp')||target.endsWith('.jsp')||target.startsWith('/css/')||target.startsWith('/js/')||target.startsWith('/images/')){
			chain.doFilter(request, response)
			return
		}
		if(target_prefix){
			if(target.size()>=target_prefix.size()){
				target=target.substring(target_prefix.size())
			}
			if(!target.startsWith('/')){
				target='/'+target
			}
		}
		if(target.endsWith(".html")||target.endsWith("/")||target.indexOf(".")<0){
			if(target.endsWith("/")){
				target=target.substring(0, target.length()-1)
			}
			if(target.endsWith(".html")){
				target=target.substring(0, target.length()-5)
			}
			def appContext=org.iff.infra.util.StringHelper.pathBuild("${request.contextPath}/${target_prefix}/", '/')
			if(appContext.endsWith('/')){
				appContext=appContext.substring(0,appContext.size()-1)
			}
			def result=handler.process(['request':request, 'response':response, 
				'context':request.contextPath,
				'resContext':request.contextPath,
				'appContext':appContext,
				'servletPath':request.servletPath, 'target':target, 'urlParams':[]])
			if(true==result){
				return
			}
		}
		chain.doFilter(request, response)
	}
	def void destroy(){
	}
}
@TCFramework(name="TCServer")
class TCServer{
	def server
	def classLoader=TCCLassManager.get()
	def TCServer(){
		def app_mode=TCCache.me().props.app_mode
		TCHelper.debug('[TCServer] app_mode:{0}', app_mode)
		if(!app_mode || 'embedded'!=app_mode){
			TCCache.me().framework.starts.add(['instance':this,'method':'start'])
			TCCache.me().framework.stops .add(['instance':this,'method':'stop' ])
		}
	}
	def start(){
		server = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.Server','int').newInstance(9090)
		def connector = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.ServerConnector','org.eclipse.jetty.server.Server').newInstance(server)
		def webApp = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.webapp.WebAppContext').newInstance()
		webApp.contextPath=TCCache.me().props.contextPath ?: '/'
		webApp.resourceBase=TCCache.me().props.resourceBase ?: "${TCCache.me().app_root}/webapp"
		webApp.classLoader=classLoader
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")	// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		server.handler=webApp
		TCHelper.debug('[TCServer] Starting web server on port: 9090')
		println "Starting web server on port: 9090"
		server.start()
		TCHelper.debug('[TCServer] Starting Complete. Welcome To The TC World :-)')
		println "Starting Complete. Welcome To The TC World :-)"
		server.join()
	}
	def stop() {
		server.stop()
	}
}
class TCChain{
	def nextChain
	def process(params){}
}
@TCFramework(name="TCActionHandler")
class TCActionHandler extends TCChain{
	def process(params){
		def actions=TCCache.me().framework?.actions, target=params?.target
		if(!params || !target || !actions){
			nextChain.process(params)
			return false
		}
		TCHelper.debug('[ActionHandler] nextChain:{0}, target:{1}', nextChain,target)
		def actionMap=actions[target]
		if(!actionMap){// target=/a/b/c split -> 1: /a/b, [c]; 2: /a, [b,c]; 3: /, [a,b,c]
			def targets=target.split('\\/')
			for(def i=targets.size()-2;i>-1;i--){
				def t=targets[0..i].join('/')
				actionMap=actions[t]
				if(actionMap){
					params.put('urlParams',targets[i+1..-1])
					break
				}
			}
		}
		if(actionMap){
			def actionClazz=TCCLassManager.me().loadClass(actionMap.action)
			//test the access privileage
			def config=TCCache.me().'config' ?: [:]
			def key1='access.'+actionClazz.name+'.'+actionMap.method, key2='access.'+actionClazz.name+'.*'
			if(!(config[key1]=='true'||config[key2]=='true')){
				TCHelper.debug('access not permited: {0}',actionClazz.name)
				nextChain.process(params)
				return false 
			}
			// test end
			def result
			def ins=actionClazz.newInstance()
			ins.metaClass.params=params
			ins.metaClass._request_params=[:]
			ins.metaClass._request_userAgent=[:]
			ins.metaClass._configs=[:]
			if(!ins.metaClass.hasMetaMethod('getConfigs', [Object[]] as Class[])){
				ins.metaClass.getConfigs={Object[] ps->
					if((ps && ps[0]==true) || _configs==null || _configs.size()==0){
						def prefix=actionClazz.name+'.'
						_configs=[:]
						TCCache.me().config.findAll{k,v->
							if(k.startsWith(prefix)){
								_configs.put(k.substring(prefix.size()),v)
							}
						}
					}
					return _configs
				}
			}
			if(!ins.metaClass.hasMetaMethod('addUrlParam', [Object,Object] as Class[])){
				ins.metaClass.addUrlParam={name, value->
					_request_params.put(name, urlEncode(value))
					return ins
				}
			}
			if(!ins.metaClass.hasMetaMethod('urlEncode', [Object] as Class[])){
				ins.metaClass.urlEncode={url->
					url ? java.net.URLEncoder.encode(url,'UTF-8') : ''
				}
			}
			if(!ins.metaClass.hasMetaMethod('urlDecode', [Object] as Class[])){
				ins.metaClass.urlDecode={url->
					url ? java.net.URLDecoder.decode(url,'UTF-8') : ''
				}
			}
			if(!ins.metaClass.hasMetaMethod('redirect', [Object] as Class[])){
				ins.metaClass.redirect={url->
					if(url && _request_params.size()>0){
						if(url.endsWith('?')||url.endsWith('&')){
							url=url+_request_params.collect{k,v-> k+'='+v}.join('&')
						}else if(url.indexOf('?')>-1){
							url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')
						}else{
							url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')
						}
					}
					ins.params.response.sendRedirect(url ? url.toString() : '')
				}
			}
			if(!ins.metaClass.hasMetaMethod('forward', [Object] as Class[])){
				ins.metaClass.forward={url->
					if(url && _request_params.size()>0){
						if(url.endsWith('?')||url.endsWith('&')){
							url=url+_request_params.collect{k,v-> k+'='+v}.join('&')
						}else if(url.indexOf('?')>-1){
							url=url+'&'+_request_params.collect{k,v-> k+'='+v}.join('&')
						}else{
							url=url+'?'+_request_params.collect{k,v-> k+'='+v}.join('&')
						}
					}
					ins.params.request.getRequestDispatcher(url ? url.toString() : '').forward(ins.params.request, ins.params.response)
				}
			}
			if(!ins.metaClass.hasMetaMethod('userAgent', [] as Class[])){
				ins.metaClass.userAgent={
					if(_request_userAgent.size()<1){
						_request_userAgent << org.iff.infra.util.HttpHelper.userAgent(params.request.getHeader('User-Agent'))
					}
					_request_userAgent
				}
			}
			if(actionClazz.getMethod(actionMap.method,null)){
				result=ins."$actionMap.method"()
			}else{
				result=ins."$actionMap.method"(params)
			}
			if(result && result.metaClass.hasMetaMethod('render', [] as Class[])){
				result.render()
			}
			return true
		}
		nextChain.process(params)
		return false
	}
	def registerAction(actionClass){
		if(!actionClass){
			return false
		}
		TCAction anno=actionClass.getAnnotation(TCAction.class)
		if(!anno){
			return false
		}
		def actions=TCCache.me().framework.actions
		def context=anno.name()
		actionClass.declaredMethods.each{
			def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
			def methodName=it.name, modifiers=it.modifiers
			if(methodName.indexOf('$')<0 && !(methodName in exclude) && !java.lang.reflect.Modifier.isStatic(modifiers) && java.lang.reflect.Modifier.isPublic(modifiers)){
				if('index'==methodName){
					actions[context]=['action':actionClass.name,'method':methodName,'context':context]
				}
				actions["${context}/${methodName}"]=['action':actionClass.name,'method':methodName,'context':context]
			}
		}
	}
	def unRegisterAction(actionClass){
		if(!actionClass){
			return false
		}
		def actions=TCCache.me().framework.actions
		TCAction anno=actionClass.getAnnotation(TCAction.class)
		if(!anno){
			return false
		}
		def context=anno.name()
		actions.remove(context)
		actionClass.declaredMethods.each{
			def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
			def methodName=it.name
			if(methodName.indexOf('$')<0 && !exclude.contains(methodName)){
				actions.remove("${context}/${methodName}")
			}
	   }
	}
}
