XML <-> Database
================

database structure: database -> table -> column.

xml format(1): xml element map to table, xml properties map to column.

example:

<server name>
	<containers>
		<container name processor>
			<html />
		</container>
	</containers>
</server>

will map to table: 
table prefix: xml_
xml_server{name}
xml_container{server_name, name, processor, html}
containers is a list of container, so don't set any property on the element

will transform to map:

{
	server@name:{
		containers:{
			container@name:{
				name, 
				processor, 
				html
			}//END-OF container@name
		}//END-OF containers
	}//END-OF server@name
}

so if the xml structure include a set/list of elements, use element+s element to wrap the set/list.

xml format(2): use json to storage the columns content, all xml element map to table: xml_table{id, parent_id, type, element_name, json}.


features:

1. parse xml from jar and file system

2. parse data from db

3. combine xml-jar + xml-filesystem + db-data

4. export xml from data









