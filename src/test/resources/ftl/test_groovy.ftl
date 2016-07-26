<@groovy var="test" fileName="pom.xml" groupId="tcom.est" artifactId="test-project" version="1.0.0">
<#noparse>
def projectRoot = new File("").getAbsolutePath();
//def file = new File("${projectRoot}/${fileName}")
def file = new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl/pom.xml")
def fileOut = new File("/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl/pom1.xml")
//def lines=file.readLines("UTF-8")
def map=[
'*<groupId>*</groupId>*':"<groupId>${groupId}</groupId>"
,'*<artifactId>*</artifactId>*':"<artifactId>${artifactId}</artifactId>"
,'*<version>*</version>*':"<artifactId>${version}</artifactId>"
]
file.filterLine("UTF-8"){str->
	def result=str
	if(!map.isEmpty()){
		def matchKey
		map.each{key, value->
			def match=org.iff.infra.util.ResourceHelper.wildCardMatch(str, key)
			if(match){
				matchKey=key
				result=value
			}else{
				result=str
			}
		}
		if(matchKey){
			map.remove(matchKey)
		}
	}
	result
}.writeTo(fileOut.newWriter("UTF-8"))  
</#noparse>
</@groovy>

<@groovy var="test1" ppp="pom.xml">
<#noparse>
def pkg=ppp.startsWith('com.foreveross.system')||ppp.startsWith('com.foreveross.extension') ? '' : ",${ppp}"
pkg
</#noparse>
</@groovy>

${test}
${test1}

<#macro formatCode align start='' end='' split='\n' leftSpace=' ' rightSpace=' '>
	<#local aligns=align?is_sequence?then(align, [align]) />
	<#local content><#nested/></#local><#local content=content?trim />
	<#list aligns as alignment>
		<#local maxBeforeLen=0 maxAfterLen=0 befores=[] afters=[] result=[]/>
		<#list content?split('\n') as line>
			<#list line?trim?split(split) as item>
				<#if item?index_of(alignment) gt -1>
					<#local before=item?substring(0, item?index_of(alignment))?trim after=item?substring(item?index_of(alignment)+alignment?length)?trim />
					<#local befores=befores+[before] afters=afters+[after] />
					<#local maxBeforeLen=(maxBeforeLen gt before?length)?then(maxBeforeLen, before?length)/>
					<#local maxAfterLen=(maxAfterLen gt before?length)?then(maxAfterLen, before?length)/>
				<#else>
					<#local befores=befores+[item] afters=afters+[''] />
				</#if>
			</#list>
		</#list>
		<#list befores as before>
		<#local tmpresult>
${start}${before?right_pad(maxBeforeLen)}${leftSpace}${alignment}${rightSpace}${afters[before?index]?right_pad(maxAfterLen)}${before?is_last?then('',end)}
		</#local>
		<#local result=result+[tmpresult]/>
		</#list>
		<#local content=result?join('')/>
	</#list>
${content}
</#macro>

<@formatCode start='    ' end='' split='\n' align=["\">","</if>"]>
<#noparse>
				<if test="name != null">NAME = #{name,jdbcType=VARCHAR},</if>
				DESCRIPTION = #{description,jdbcType=VARCHAR},
				<if test="updateTime != null">UPDATE_TIME = #{updateTime,jdbcType=DATE},</if>
				<if test="createTime != null">CREATE_TIME = #{createTime,jdbcType=DATE},</if>
</#noparse>
</@formatCode>
