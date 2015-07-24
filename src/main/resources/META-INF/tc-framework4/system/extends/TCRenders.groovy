package org.iff.groovy.framework

class TCRenderFactory{
	def getDefaultRender(){
		return new TCRender()
	}
	def getRender(view){
		
	}
}
class TCRender{
	def view
	def params//['request':request, 'response':response, 'context':request.contextPath, 'servletPath':request.servletPath, 'target':target]
	def render(){
	}
}
class TCGspRender extends TCRender{
	def render(){
		params.request.getRequestDispatcher(view.toString()).forward(params.request, params.response)
	}
}
class TCJspRender extends TCRender{
	def render(){
		params.request.getRequestDispatcher(view.toString()).forward(params.request, params.response)
	}
}
class TCFreeMarkerRender extends TCRender{
	def static String encoding='UTF-8'
	def static String contentType='text/html; charset=UTF-8'
	def static freemarker.template.Configuration config
	def TCFreeMarkerRender(){
		init()
	}
	def static init(){
		if(config){
			return
		}
		def servletContext=TCCache.me().servlet.servletContext
		config=new freemarker.template.Configuration()
		config.setServletContextForTemplateLoading(servletContext, "/")
		config.setTemplateUpdateDelay(0)
		config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER)
		config.setObjectWrapper(freemarker.template.ObjectWrapper.BEANS_WRAPPER)
		config.setDefaultEncoding(encoding)
		config.setOutputEncoding(encoding)
		config.setLocale(java.util.Locale.CHINA)
		config.setLocalizedLookup(false)
		config.setNumberFormat("#0.#####")
		config.setDateFormat("yyyy-MM-dd")
		config.setTimeFormat("HH:mm:ss")
		config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss")
	}
	def render(){
		def request=params.request
		def response=params.response
		response.setContentType(contentType)
		Enumeration<String> attrs = request.getAttributeNames()
		Map root = new HashMap()
		while (attrs.hasMoreElements()) {
			String attrName = attrs.nextElement()
			root.put(attrName, request.getAttribute(attrName))
		}
		root.put('request',request)
		PrintWriter writer = null
		try {
			freemarker.template.Template template = config.getTemplate(view)
			writer = response.getWriter()
			template.process(root, writer)		// Merge the data-model and the template
		} finally {
			if (writer != null){
				writer.close()
			}
		}
	}
}
