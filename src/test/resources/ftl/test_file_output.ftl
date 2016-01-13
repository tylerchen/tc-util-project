<#assign content>
public class A{
//doto
/*
hello, 一些中文
*/
}

</#assign>

<#macro test>
/*
hello, 一些中文
*/
</#macro>

<@filewriter type="java" basedir="/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"
			name="org.iff.test.Hello.java">
${content}
</@filewriter>
<@filewriter type="xml" basedir="/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl/a/b/c"
			name="d/e/f/AA.xml">
<@test/>
</@filewriter>

${"testAbbA"?word_list?join(",")}