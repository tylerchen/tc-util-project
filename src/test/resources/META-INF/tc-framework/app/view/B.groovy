package org.iff.groovy.test.b

@TCAction(name="/b")
class TestAction{
	def index(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('bbbb') }
			body{ h1("Hello TC!") }
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

