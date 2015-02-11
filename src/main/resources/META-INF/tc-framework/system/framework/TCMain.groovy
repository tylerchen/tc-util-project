package org.iff.groovy.framework

import org.iff.infra.util.groovy.TCCLassManager;

@org.iff.infra.util.groovy.TCFramework(name="TCMain")
class TCMain{
	def main(){
		def initer=TCCache.me().cache().framework_instance.TCIniter
		initer.init()
		//
		def handlers=TCCache.me().cache().framework.action_handlers
		handlers.add(new TCChain(){
			def process(params){
				TCHelper.debug('[BeforeHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		})
		handlers.add(TCCache.me().cache().framework_instance.TCActionHandler)
		handlers.add(new TCChain(){
			def process(params){
				TCHelper.debug('[AfterHandler]: {0}',params.target)
				nextChain?.process(params)
			}
		})
		//
		def starts=TCCache.me().cache().framework_instance.TCStarts
		starts.start()
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCIniter")
class TCIniter{
	def init(){
		def list=TCCache.me().cache().framework.inits// data structure=[instance,method]
		list.each{
			it.instance."$it.method"()
		}
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCStarts")
class TCStarts{
	def start(){
		def list=TCCache.me().cache().framework.starts// data structure=[instance,method]
		list.each(){
			def final start=it
			Thread.start{
				TCHelper.debug('[TCStarts]: {0}',start)
				start.instance."$start.method"()
			}//end thread
		}
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCStopper")
class TCStopper{
	def stop(){
		def list=TCCache.me().cache().framework.stops// data structure=[instance,method]
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
		TCCache.me().cache().servlet.put('servletContext', filterConfig.servletContext)
		target_prefix = filterConfig.getInitParameter('target_prefix') ?: ''
		if(!handler){
			def tmp=new TCChain()
			def list=TCCache.me().cache().framework.action_handlers
			list.reverse().each{
				it.nextChain=tmp
				tmp=it
			}
			handler=tmp
		}
	}
	def void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response,javax.servlet.FilterChain chain) throws IOException, javax.servlet.ServletException{
		request.setCharacterEncoding("UTF-8")
		response.setCharacterEncoding("UTF-8")
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
@org.iff.infra.util.groovy.TCFramework(name="TCServer")
class TCServer{
	def server
	def classLoader=TCCLassManager.get()
	def TCServer(){
		def app_mode=TCCache.me().cache().props.app_mode
		TCHelper.debug('[TCServer] app_mode:{0}', app_mode)
		if(!app_mode || 'embedded'!=app_mode){
			TCCache.me().cache().framework.starts.add(['instance':this,'method':'start'])
			TCCache.me().cache().framework.stops .add(['instance':this,'method':'stop' ])
		}
	}
	def start(){
		server = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.Server','int').newInstance(9090)
		def connector = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.server.ServerConnector','org.eclipse.jetty.server.Server').newInstance(server)
		def webApp = org.iff.infra.util.ReflectHelper.getConstructor('org.eclipse.jetty.webapp.WebAppContext').newInstance()
		webApp.contextPath=TCCache.me().cache().props.contextPath ?: '/'
		webApp.resourceBase=TCCache.me().cache().props.resourceBase ?: "${TCCache.me().cache().app_root}/webapp"
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
@org.iff.infra.util.groovy.TCFramework(name="TCActionHandler")
class TCActionHandler extends TCChain{
	def process(params){
		def actions=TCCache.me().cache().framework?.actions, target=params?.target
		if(!params || !target || !actions){
			nextChain.process(params)
			return false
		}
		TCHelper.debug('[ActionHandler] nextChain:{0}, target:{1}', nextChain,target)
		def actionMap=actions[target]
		if(!actionMap){// target=/a/b/c split -> 1: /a/b, [c]; 2: /a, [b,c]; 3: /, [a,b,c]
			def targets=target.split("\\/")
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
			def result
			if(actionClazz.getMethod(actionMap.method,null)){
				def ins=actionClazz.newInstance()
				ins.metaClass.params=params
				result=ins."$actionMap.method"()
			}else{
				def ins=actionClazz.newInstance()
				ins.metaClass.params=params
				result=ins."$actionMap.method"(params)
			}
			if(result instanceof TCRender){
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
		def actions=TCCache.me().cache().framework.actions
		def context=anno.name()
		actionClass.declaredMethods.each{
			def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
			def methodName=it.name
			if(methodName.indexOf('$')<0 && !(methodName in exclude)){
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
		def actions=TCCache.me().cache().framework.actions
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
