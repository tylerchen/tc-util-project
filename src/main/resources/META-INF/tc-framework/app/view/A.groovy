package org.iff.groovy.test.a

@org.iff.infra.util.groovy.TCAction(name="/test")
class TestAction{
	def index(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('index') }
			body{ h1("So great!!!") }
		}
	}
	def hello(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('hello') }
			body{ h1("hello aaaa") }
		}
	}
	def ftl(){
		return new TCFreeMarkerRender(view:'/a.html',params:params)
	}
	def jsp(){
		return new TCJspRender(view:'/test.jsp',params:params)
	}
	def gsp(){
		return new TCGspRender(view:'/c.gsp',params:params)
	}
}
@org.iff.infra.util.groovy.TCAction(name="/hello")
class HelloAction{
	def index(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('index') }
			body{ h1("index") }
		}
	}
	def hello(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('hello') }
			body{ h1("hello eee") }
		}
	}
}

