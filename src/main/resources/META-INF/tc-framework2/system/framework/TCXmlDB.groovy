package org.iff.groovy.framework

class TCXmlJsonDB{
	def static parse_xml_to_map(map, xmlfiles){
		def xml_struct=map ?: [:]
		xmlfiles.each{file->
			println "file: ${it}"
			parse_xml(xml_struct, file)
		}
		xml_struct
	}
	
	def static parse_dbdata_to_map(map, xml_datas){//xml_table{id, parent_id, type, element_name, json}
		def xml_struct=map ?: [:]
		def pid_map=[:]
		xml_datas.each{row, pid=(row.parent_id ?: '-') ->
			(pid_map."${pid}"=(pid_map."${pid}" ?: [])).add(row)
		}
	
		pid_map.'-'.each{root->
		    parse_dbdata(xml_struct, pid_map, root)
		}
		xml_struct
	}
	
	def static combine_xmlmap_with_dbdatamap(maps){
		// not support
	}
	
	def static export_map_to_xmlfile(){
		// not support
	}
	
	def static parse_dbdata(xml_struct, pid_map, root){
	    def map=xml_struct
	    map.put(root.element_name, (map.get(root.element_name) ?: [:]) )
	    def level=[map.get(root.element_name)]// use for recursion
	    def childrenClosure={children, cls->
	        children.each{child, json=(child.json ?: '{}'), cid=child.id, element_name=child.element_name->
	            def value=new groovy.json.JsonSlurper().parseText(json.size()<4 ? '{}' : json)
	            def name=element_name
	            def child_children=pid_map.get(cid)
	            def attributes=value
	            def key=name+(attributes.name ? ('@'+attributes.name): '')
	            level << (level.last()."${key}"=(level.last()."${key}" ?: [:]))
	            level.last() << attributes
	            cls(child_children,cls)
	            level.remove(level.size()-1)
	        }
	    }
	    childrenClosure(pid_map.get(root.id),childrenClosure)
	    println groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(map))
	    map
	}
	
	def static parse_xml(xml_struct, file){
		def xml=new XmlParser().parse(new File(file))
		def map=xml_struct
		map.put(xml.name(), (map.get(xml.name()) ?: [:]) )
		def level=[map.get(xml.name())]// use for recursion
		def childrenClosure={children, cls->
		    children.each{child->
		        if(child instanceof String){
		        }else{
		            def name=child.name()
		            def child_children=child.children()
		            def attributes=child.attributes()
		            if(child_children.size()==1 && attributes.size()==0 && child_children[0] instanceof String){
		                // is the <![CDATA[]]> or text field
		                level.last().put(name, child_children[0])
		            }else{
		                def key=name+(attributes.name ? ('@'+attributes.name): '')
		                level << (level.last()."${key}"=(level.last()."${key}" ?: [:]))
		                level.last() << attributes
		                cls(child_children,cls)
		                level.remove(level.size()-1)
		            }
		        }
		    }
		}
		childrenClosure(xml.children(),childrenClosure)
		println groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(map))
		map
	}
}

