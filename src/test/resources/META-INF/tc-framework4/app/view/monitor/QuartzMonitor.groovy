package org.iff.groovy.view.monitor

@TCAction(name="/monitor/quartz")
class QuartzMonitorAction{
	def protected urlParamMap(urlParams){
		def pmap=[:]
		urlParams.each{p->
			def index=p.indexOf('=')
			if(index>0){
				pmap.put(p.substring(0,index),p.substring(index+1))
			}
		}
		pmap
	}
	def index(){
	}
	def private process_configurations(){//{configId:{datasource:{db config},quartz:{properties}}}
		
	}
}

