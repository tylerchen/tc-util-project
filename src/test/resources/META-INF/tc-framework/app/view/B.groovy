package org.iff.groovy.test.b

@org.iff.infra.util.groovy.TCAction(name="/b")
class TestAction{
	def index(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('bbbb') }
			body{ 
				h1("Hello TC!")
				div("${TCCache.me().framework_instance.TCServer.server.handler.sessionHandler.sessionManager.@_sessions}")
			}
		}
	}
	def aa(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('bbb') }
			body{ h1("hello ddddd") }
		}
	}
}

