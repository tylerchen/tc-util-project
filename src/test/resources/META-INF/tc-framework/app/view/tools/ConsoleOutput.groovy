package org.iff.groovy.view.tools

@TCAction(name="/tools/output")
class ConsoleOuputAction{
	def static baos
	def index(){
		if(!baos){
			baos=new org.iff.infra.util.ScrollableByteArrayOutputStream()
			System.out=new PrintStream(baos)
			System.err=new PrintStream(baos)
			def appender=new org.apache.log4j.WriterAppender(new org.apache.log4j.PatternLayout('[%-d{yyyy-MM-dd HH:mm:ss}][%p]-[Thread: %t]-[%M()]: %m%n'), baos)
			org.apache.log4j.LogManager.getCurrentLoggers().toList().each{ logger->
				logger.addAppender(appender)
				//org.apache.log4j.Logger.getRootLogger().addAppender(appender)
			}
		}
		System.out.println('aaaaaaaa')
		def build = new groovy.xml.MarkupBuilder(params.response.writer)
		build.html{
			head{ 
				title('bbbb')
				meta(charset:"UTF-8")
			}
			body{ 
				h1("Hello TC!")
				pre(baos.toString())
				baos.reset()
			}
		}
	}
	def reset(){
		baos=null
		params.response.writer << 'OK'
	}
}

