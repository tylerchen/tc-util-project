package org.iff.groovy.framework

class TCFilter implements javax.servlet.Filter{
	def handler
	def target_prefix
	def class_manager_name
	def void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException{
		TCHelper.debug('[TCFilter Init]')
		println "-----> ${TCCache.me()}"
		TCCache.me().setByPath('/framework/servlet/'+class_manager_name+'/filter_config', filterConfig)
		TCCache.me().setByPath('/framework/servlet/'+class_manager_name+'/servlet_context', filterConfig.servletContext)
		class_manager_name=filterConfig.getServletContext().getInitParameter('class_manager_name')
		println "filter: class_manager_name=${class_manager_name}"
		target_prefix = filterConfig.getInitParameter('target_prefix') ?: ''
		if(!handler){
			def tmp=new TCChain()
			def handlers=TCRegister.get('ActionHandler@'+class_manager_name)
			handlers.iterator().reverse().each{entry->
				entry.value.'handler'.nextChain=tmp
				tmp=entry.value.'handler'
			}
			handler=tmp
		}
	}
	def void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response,javax.servlet.FilterChain chain) throws IOException, javax.servlet.ServletException{
		request.setCharacterEncoding('UTF-8') || true && response.setCharacterEncoding('UTF-8')
		def target=request.servletPath
		TCHelper.debug('[doFilter] handler:{0}, target:{1}, target_prefix:{2}', handler, target, target_prefix)
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

@TCBean(name='TC_Server')
class TCServer{
	def server
	def class_manager_name
	def app_mode
	def port=8080
	def web_dir
	def context_path='/'
	def start(){
		TCHelper.debug('[TCServer] app_mode:{0}', app_mode)
		if(!app_mode || 'embedded'!=app_mode){
			Thread.start{
				this.start_server()
			}
		}
		//register action handler
		TCRegister.register('ActionHandler@'+class_manager_name, ['name':'before', 'handler':new TCChain(class_manager_name: class_manager_name){
			def process(params){
				TCHelper.debug('[BeforeHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		}])
		TCRegister.register('ActionHandler@'+class_manager_name, ['name':'on_action', 'handler':new TCActionHandler(class_manager_name: class_manager_name)])
		TCRegister.register('ActionHandler@'+class_manager_name, ['name':'after', 'handler':new TCChain(class_manager_name: class_manager_name){
			def process(params){
				TCHelper.debug('[AfterHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		}])
	}
	def start_server(){//http://stackoverflow.com/questions/9216650/jetty-setinitparameter-is-not-initializing-any-parameter
		server = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.Server','int').newInstance(port)
		def connector = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.ServerConnector','org.eclipse.jetty.server.Server').newInstance(server)
		def webApp = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.webapp.WebAppContext').newInstance()
		webApp.contextPath=context_path ?: '/'
		webApp.resourceBase=web_dir
		webApp.classLoader=TCCLassManager.getClassLoader(class_manager_name)
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")	// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		webApp.setInitParameter("class_manager_name", class_manager_name)
		server.handler=webApp
		TCHelper.debug('[TCServer] Starting web server on port: {0}', port)
		println "Starting web server on port: ${port}"
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
	def class_manager_name
	def nextChain
	TCChain(){}
	TCChain(class_manager_name){
		this.class_manager_name=class_manager_name
	}
	def process(params){}
}

class TCActionHandler extends TCChain{
	def process(params){
		def url_map=TCCache.me().getByPath('framework/urlmappings/'+class_manager_name,true)
		def target=params?.target
		if(!params || !target || !url_map){
			nextChain.process(params)
			return false
		}
		TCHelper.debug('[ActionHandler] nextChain:{0}, target:{1}', nextChain,target)
		def actionMap=url_map[target]
		if(!actionMap){// target=/a/b/c split -> 1: /a/b, [c]; 2: /a, [b,c]; 3: /, [a,b,c]
			def targets=target.split('\\/')
			for(def i=targets.size()-2;i>-1;i--){
				def t=targets[0..i].join('/')
				actionMap=url_map[t]
				if(actionMap){
					params.put('urlParams',targets[i+1..-1])
					break
				}
			}
		}
		if(actionMap){
			def actionClazz=TCCLassManager.getManager(class_manager_name, false).getClassLoader(class_manager_name).loadClass(actionMap.action)
			//test the access privileage
			/*def config=TCCache.me().'config' ?: [:]
			def key1='access.'+actionClazz.name+'.'+actionMap.method, key2='access.'+actionClazz.name+'.*'
			if(!(config[key1]=='true'||config[key2]=='true')){
				TCHelper.debug('access not permited: {0},{1}',actionClazz.name,config)
				nextChain.process(params)
				return false
			}*/
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
}


