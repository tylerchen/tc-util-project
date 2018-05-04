package org.iff.groovy.test.a

@TCAction(name="/test")
class TestAction{
	def index(){
		TCHelper.debug('test');
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('index') }
			body{ h1("So great SSOOOO!!! ${params}}") }
		}
	}
	def hello(){
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ title('hello') }
			body{ h1("hello aaaa") }
		}
	}
}

