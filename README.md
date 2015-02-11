tc-util-project
======

### Introduce project

tc-util-project is a utilities project make by TylerChen. 
There is a many useful tools to help the project.
Including:

1. org.iff.infra.com.dayatang: this was create by KevinChen, a convenience way to get the Spring Bean by using org.iff.infra.com.dayatang.domain.InstanceFactory
2. org.iff.infra.test: this was create by KevinChen, a test support for the project
3. org.iff.infra.util: this the tool set for java developers, try it
4. org.iff.infra.util.groovy: this is groovy support, see also META-INF\tc-framework, provide a standalone groovy web framework or you can embedded in a existing web project with a servlet path
5. org.iff.infra.util.mybatis: this is a mybatis extensions, provide UUID plugin and Page plugin
6. org.iff.infra.util.jdbc.dialet: this is a dialet to process sql to sum recourd count and paginate the records

### tc-groovy-framework introduce

#### TC groovy framework structure and loading sequence

1. app_root/system/util     : the utilities
2. app_root/system/framework: the framework
3. app_root/system/lib      : not using yet
4. app_root/app/model       : the model of the app
5. app_root/app/service     : the service of the app
6. app_root/app/view        : the view of the app

#### How to start the framework as standarlone mode

1. setting the app_root, where your groovy files place
2. setting the tc_jar_path, where the jar pack with the framework groovy files, if you don't know set this value to "META-INF/tc-framework"
3. setting the resourceBase, where the web root dir is
4. invoke the code org.iff.infra.util.groovy.TCCLassManager.main(null)
5. Sample Code
		public static void main(String[] args) throws Exception {
			System.setProperty("app_root", "/tc-util-project/src/main/resources/META-INF/myapp");
			System.setProperty("tc_jar_path", "META-INF/tc-framework");
			System.setProperty("resourceBase", "/tc-util-project/src/main/webapp");
			{
				org.iff.infra.util.groovy.TCCLassManager.main(args);
			}
		}

#### How to start the framework as embedded mode

1. place the config to web.xml, and change it
		<filter>
			<filter-name>tcfilter</filter-name>
			<filter-class>org.iff.infra.util.groovy.TCGroovyFilter</filter-class>
			<init-param>
				<param-name>tc_jar_path</param-name>
				<param-value>META-INF/tc-framework</param-value>
			</init-param>
			<init-param>
				<param-name>app_root</param-name>
				<param-value></param-value>
			</init-param>
			<init-param>
				<param-name>target_prefix</param-name>
				<param-value>/hello</param-value>
			</init-param>
		</filter>
		<filter-mapping>
			<filter-name>tcfilter</filter-name>
			<url-pattern>/hello/*</url-pattern>
		</filter-mapping>

#### How to write a view page

1. Create a groovy file and define groovy class
2. Add the annotaion @TCAction(name='/viewPath') to groovy class
3. Define a method and business logic
4. Access the url : http://host:port/context/filterContext/viewPath/methodName, if no specify methodName, will access index() method
5. There is many render types, you can use MarkupBuilder, TCFreeMarkerRender, TCJspRender, TCGspRender. for TCGspRender you need to register the groovy gsp servlet in web.xml
6. The internal parameter "params" include many properties: 
		request     : HttpServletRequest
		response    : HttpServletResponse
		context     : request.contextPath
		resContext  : request.contextPath
		appContext  : request.contextPath/filterContext, "filterContext" is the prefix path of filter when you embedded in a web 
		servletPath : request.servletPath
		target      : http://host:port/context/filterContext/viewPath/methodName, target="viewPath/methodName"
		urlParams   : http://host:port/context/filterContext/viewPath/methodName/urlParam1/urlParam2, urlParams=["urlParam1", "urlParam2"]
7. Sample Code
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

#### Parameters

1. app_root
		application root dir, where you place the groovy files, such as:
		System.setProperty("app_root", "/project/tc-framework");
2. resourceBase
		the web resource dir, where you place the web resource and web.xml, the dir structure as the same as war package, such as:
		System.setProperty("resourceBase", "/project/tc-framework/webapp");
3. tc_file_path
		the framework will scan the path and load the groovy files, if tc_file_path is not set will default to app_root
4. tc_jar_path
		the framework will scan the jar path and load the groovy files
5. tc_groovy_framework_start_class
		the class to start the tc groovy framework default is org.iff.groovy.framework.TCStarter
6. app_mode
		this parameter will set the "embedded" value, when you embedded the framework to an existing web as a filter
7. target_prefix
		this parameter should be set a value (filter prefix path) when you embedded the framework to an existing web as a filter



