package org.iff.groovy.framework

@org.iff.infra.util.groovy.TCFramework(name="TCMain")
class TCMain{
	def main(){
		def initer=TCCache.me().cache().framework_instance.TCIniter
		initer.init()
		//
		def handlers=TCCache.me().cache().framework.action_handlers
		handlers.add(new TCChain(){
			def process(params){
				println "[BeforeHandler]: ${params.target}"
				nextChain?.process(params)
			}
		})
		handlers.add(TCCache.me().cache().framework_instance.TCActionHandler)
		handlers.add(new TCChain(){
			def process(params){
				println "[AfterHandler]: ${params.target}"
				nextChain?.process(params)
			}
		})
		//
		def starter=TCCache.me().cache().framework_instance.TCStarter
		starter.start()
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCIniter")
class TCIniter{
	def init(){
		def list=TCCache.me().cache().framework.inits// data structure=[instance,method]
		list.each(){
			it.instance."$it.method"()
		}
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCStarter")
class TCStarter{
	def start(){
		def list=TCCache.me().cache().framework.starts// data structure=[instance,method]
		list.each(){
			def final start=it
			Thread.start{
				println "=======TCStarter=======> ${start}"
				start.instance."$start.method"()
			}//end thread
		}
	}
}
@org.iff.infra.util.groovy.TCFramework(name="TCStopper")
class TCStopper{
	def stop(){
		def list=TCCache.me().cache().framework.stops// data structure=[instance,method]
		list.each(){
			it.instance."$it.method"()
		}
	}
}
class TCFilter implements javax.servlet.Filter{
	def handler
	def void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException{
		println "Groovy Filter Init"
		TCCache.me().cache().servlet.put("servletContext", filterConfig.servletContext)
		if(!handler){
			def tmp=new TCChain()
			def list=TCCache.me().cache().framework.action_handlers
			list.reverse().each(){
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
		def target_prefix=request.getAttribute('tc_groovy_target_prefix')
		println "doFilter: ${handler.class.name}, target:${target}, target_prefix:${target_prefix}"
		if(target.endsWith(".gsp")||target.endsWith(".jsp")||target.startsWith("/css/")||target.startsWith("/js/")||target.startsWith("/images/")){
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
			def result=handler.process(['request':request, 'response':response, 
				'context':request.contextPath,
				'resContext':request.contextPath,
				'appContext':org.iff.infra.util.StringHelper.pathBuild("${request.contextPath}/${target_prefix}/", '/')[0..-2],
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
		println "---------app_mode----------> ${app_mode}"
		if(!app_mode || 'embedded'!=app_mode){
			TCCache.me().cache().framework.starts.add(['instance':this,'method':'start'])
			TCCache.me().cache().framework.stops .add(['instance':this,'method':'stop' ])
		}
	}
	def start(){
		server = new org.eclipse.jetty.server.Server(9090)
		def connector = new org.eclipse.jetty.server.ServerConnector(server)
		def webApp = new org.eclipse.jetty.webapp.WebAppContext(contextPath:"/", resourceBase:"${TCCache.me().cache().app_root}/webapp", classLoader:classLoader)
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")	// webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));
		server.setHandler(webApp)
		println "Starting web server on port: 9090"
		server.start()
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
		println "[ActionHandler][${nextChain}][${actions}]: ${target}"
		def actionMap=actions[target]
		if(!actionMap){// target=/a/b/c split -> 1: /a/b, [c]; 2: /a, [b,c]; 3: /, [a,b,c]
			def targets=target.split("\\/")
			println "----> ${targets}"
			for(def i=targets.size()-2;i>-1;i--){
				def t=targets[0..i].join('/')
				println "join----> ${t}"
				actionMap=actions[t]
				if(actionMap){
					params.put('urlParams',targets[i+1..-1])
					break
				}
			}
		}
		if(actionMap){
			println "actionMap.action: ${actionMap.action}"
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
		println "----registerAction---:${actionClass}"
		if(!actionClass){
			return false
		}
		TCAction anno=actionClass.getAnnotation(TCAction.class)
		if(!anno){
			return false
		}
		def actions=TCCache.me().cache().framework.actions
		def context=anno.name()
		actionClass.declaredMethods.each(){
			def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
			def methodName=it.name
			if(methodName.indexOf('$')<0 && !(methodName in exclude)){
				println "$methodName"
				if('index'==methodName){
					actions[context]=['action':actionClass.name,'method':methodName,'context':context]
				}
				actions["${context}/${methodName}"]=['action':actionClass.name,'method':methodName,'context':context]
			}
		}
		println "actions: ${actions}"
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
		actionClass.declaredMethods.each(){
			def exclude=['invokeMethod','getMetaClass','setMetaClass','setProperty','getProperty']
			def methodName=it.name
			if(methodName.indexOf('$')<0 && !exclude.contains(methodName)){
				println "$methodName"
				actions.remove("${context}/${methodName}")
			}
	   }
	}
}
