package org.iff.groovy.framework

// @see README-lifecycle.md
class TCLifeCycleBasic{
	def children=[]
	def map_path=''
	def class_manager=''
	def node_name=''
	def config=[:]
	def get_data(){
		TCCache.me().getByPath(map_path)
	}
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){
	}
	def load_groovy(){
		def mgr=TCCLassManager.getManager(class_manager, false)
		def data=get_data()
		def class_manager=TCCache.getByPath('framework/class_manager/'+class_manager)
		if(class_manager==null){
			TCCache.setByPath('framework/class_manager/'+class_manager, [instance: null, paths:[:]])
			class_manager=TCCache.getByPath('framework/class_manager/'+class_manager)
		}
		def class_manager_paths=class_manager.'paths'
		def groovy_path=data.'groovy_path'?.split(',')
		groovy_path.each{path->
			class_manager_paths[path]=true
		}
		if(groovy_path){
			TCGroovyLoader.load_compile_by_order(groovy_path, class_manager_name)
		}
	}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}

@TCFramework(name="TC_openapplication_server")
class TCLifeCycle_server{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_containers")
class TCLifeCycle_containers{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_container")
class TCLifeCycle_container{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_resources")
class TCLifeCycle_resources{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_resource")
class TCLifeCycle_resource{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_applications")
class TCLifeCycle_applications{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_application")
class TCLifeCycle_application{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_settings")
class TCLifeCycle_settings{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_setting")
class TCLifeCycle_setting{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_modules")
class TCLifeCycle_modules{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_module")
class TCLifeCycle_module{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_actions")
class TCLifeCycle_actions{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}
@TCFramework(name="TC_openapplication_action")
class TCLifeCycle_action{
	def children=[]
	def process(){
		load_cfg()
		load_groovy()
	}
	def load_cfg(){}
	def load_groovy(){}
	def start(){
		children.each{ it.process() }
	}
	def stop(){
		children.each{ it.stop() }
	}
}