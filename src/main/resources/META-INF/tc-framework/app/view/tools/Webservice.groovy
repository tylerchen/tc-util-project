package org.iff.groovy.view.tools

@TCAction(name="/tools/webservice")
class WebserviceAction{
	def index(){
		def writer=params.response.writer
		writer << "<!DOCTYPE html>"
		def build = new groovy.xml.MarkupBuilder(writer)
		build.html(xmlns:"http://www.w3.org/1999/xhtml"){
			head{
				meta(charset:"UTF-8")
				meta('http-equiv':"X-UA-Compatible",'content':"IE=edge")
				title("Webservice")
			}
			body(){
				h1("hello")
			}
		}
	}
	def json(){
		def writer=params.response.writer
		params.response.setContentType("text/plain; charset=UTF-8")
		params.response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK)
		def o=[a:1,b:2,c:'aaaa',e:new Date()]
		def xstream = new com.thoughtworks.xstream.XStream()
		def xml=xstream.toXML([1,'a',new Date(),[],[:]])
		def gson=groovy.json.JsonOutput.toJson(o)
		writer << gson
	}
	def rest(){
		def cxf_jwsrs=new org.iff.infra.util.groovy.TCCXFServlet()
		cxf_jwsrs.setClassNames(TestWebService.class.name)
		def filterConfig=TCCache.me().servlet.filterConfig
		def servletConfig=[
				getServletName:{
					filterConfig.filterName
				},
				getServletContext:{
					filterConfig.servletContext
				},
				getInitParameter:{String name->
					if ('base-address'==name) {
						'/tools/webservice/rest'
					}else if('jaxrs.address'==name){
						'/hello'
					}else{
						filterConfig.getInitParameter(name)
					}
				},
				getInitParameterNames:{
					filterConfig.initParameterNames
				}
			] as javax.servlet.ServletConfig
		cxf_jwsrs.init(servletConfig)
		def requestProxy = new groovy.util.Proxy().wrap( params.request )
		requestProxy.metaClass.getPathInfo={
			def uri=params.request.requestURI
			return uri.substring(uri.indexOf('/webservice/rest')+'/webservice/rest'.size())
		}
		cxf_jwsrs.service(requestProxy as javax.servlet.http.HttpServletRequest,params.response)
	}
	def testrs(){
		def content=org.iff.infra.util.RequestHelper.get('http://localhost:9090/webservice/rest/hello/test/b/tylerchen', ['_type':'xml'],[:])
		println content
		params.response.writer << content
		def xstream = new com.thoughtworks.xstream.XStream()
		params.response.writer << org.iff.infra.util.JsonHelper.toJson(xstream.fromXML(content.body))
	}
}

@javax.ws.rs.Path('/test')
class TestWebService{
	def aaa='bbb'
	
	@javax.ws.rs.GET
	@javax.ws.rs.Path('/b/{username}')
	@javax.ws.rs.Produces(['application/json', 'application/xml'])
	def test(@javax.ws.rs.PathParam("username") String username){
		new TestBean(username:username)
	}
}
//@javax.xml.bind.annotation.XmlRootElement(name = "TestBean")
//@javax.xml.bind.annotation.XmlAccessorType( javax.xml.bind.annotation.XmlAccessType.FIELD )
class TestBean{
	def a='aaaa'
	def b=new Date()
	def c=1L
	def username
	def map=[a:1,b:2]
	def list=[1,2,3,4,5]
}
