<#assign content>
public class A{
//doto
/*
hello
*/
}
</#assign>

<@filewriter type="java" basedir="/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/ftl"
			name="org.iff.test.Hello.java">
${content}
</@filewriter>