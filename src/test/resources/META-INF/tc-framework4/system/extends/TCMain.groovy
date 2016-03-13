package org.iff.groovy.framework

class TCFilter implements javax.servlet.Filter{
	def handler
	def target_prefix
	def container_name
	def void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException{
		container_name=filterConfig.getServletContext().getInitParameter('container_name') ?: 'default'
		TCHelper.debug('[TCFilter Init][container_name={0}]', container_name)
		TCCache.me().setByPath("/framework/servlet/${container_name}/filter_config", filterConfig)
		TCCache.me().setByPath("/framework/servlet/${container_name}/servlet_context", filterConfig.servletContext)
		println "filter: container_name=${container_name}"
		target_prefix = filterConfig.getInitParameter('target_prefix') ?: ''
		if(!handler){
			def tmp=new TCChain()
			def handlers=TCRegister.get('ActionHandler@'+container_name)
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
				'servletPath':request.servletPath, 'target':target, 'urlParams':[], 'container_name': container_name])
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
	def container_name
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
		TCRegister.regist('ActionHandler@'+container_name, ['name':'before', 'handler':new TCChain(container_name: container_name){
			def process(params){
				TCHelper.debug('[BeforeHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		}])
		TCRegister.regist('ActionHandler@'+container_name, ['name':'on_action', 'handler':new TCActionHandler(container_name: container_name)])
		TCRegister.regist('ActionHandler@'+container_name, ['name':'after', 'handler':new TCChain(container_name: container_name){
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
		webApp.classLoader=TCGetter.get_class_loader()
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")	// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		webApp.setInitParameter("container_name", container_name)
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
	def container_name
	def nextChain
	TCChain(){}
	TCChain(container_name){
		this.container_name=container_name
	}
	def process(params){}
}

class TCActionHandler extends TCChain{
	def process(params){
		def url_map=TCGetter.get_url_map()
		def action_url_map=TCGetter.get_action_url_map(container_name)
		def target=params?.target
		if(!params || !target || !url_map || !action_url_map){
			println "params=${params}"
			println "target=${target}"
			println "url_map=${url_map}"
			println "action_url_map=${action_url_map}"
			nextChain.process(params)
			return false
		}
		TCHelper.debug('[ActionHandler] nextChain:{0}, target:{1}', nextChain,target)
		def action_url=action_url_map[target], action_url_params=[], actionMap
		if(!action_url){// target=/a/b/c split -> 1: /a/b, [c]; 2: /a, [b,c]; 3: /, [a,b,c]
			def targets=target.split('\\/')
			for(def i=targets.size()-2;i>-1;i--){
				def t=targets[0..i].join('/')
				action_url=action_url_map[t]
				if(action_url){
					action_url_params=targets[i+1..-1]
					break
				}
			}
		}
		if(action_url_params.isEmpty()){
			actionMap = url_map[action_url]
		}else if(action_url){
			def targets = action_url.split('\\/')+action_url_params
			for(def i=targets.size()-1;i>-1;i--){
				def t=targets[0..i].join('/')
				actionMap=url_map[t]
				if(actionMap){
					params.put('urlParams',targets[i..-1])
					break
				}
			}
		}
		if(actionMap){
			def actionClazz=TCGetter.get_class_loader().loadClass(actionMap.action)
			//test the access privileage
			def key1='access.'+actionClazz.name+'.'+actionMap.method, key2='access.'+actionClazz.name+'.*'
			println "------------>${TCGetter.get_config(key1, container_name)}"
			println "------------>${TCGetter.get_config(key2, container_name)}"
			if(!(TCGetter.get_config(key1, container_name)=='true'||TCGetter.get_config(key2, container_name)=='true')){
				TCHelper.debug('access not permited: {0},\n{1},\n{2}',actionClazz.name, [key1, key2], TCGetter.get_configs())
				nextChain.process(params)
				return false
			}
			// test end
			def result
			def ins=actionClazz.newInstance()
			ins.params=params
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


