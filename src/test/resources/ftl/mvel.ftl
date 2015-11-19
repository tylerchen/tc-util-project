test:${test}

<@mvel var="test">
str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ${test}";

foreach (el : str) {
   System.out.print("[" + el + "]"); 
}

return str;
</@mvel>

test:${test}

test:${helper("MD5Helper").string2MD5("test")}


test:${'helper("MD5Helper").string2MD5("test")'?eval}

<#assign time_start=.now?long />
<#list 1..10000 as j>
	<@mvel var="hello">
		return org.iff.infra.util.mybatis.plugin.Page.pageable(${j?c},1,111,null);
	</@mvel>
</#list>
test: ${hello}

test: ${(.now?long-time_start)?string}

<@jstm required="jquery.js">
var test='123';
</@jstm>

<@jstm required="jquery.js,test.js">
var test='aaa';
</@jstm>

<#list .globals.tmscript as script>
${script}
</#list>

<#list .globals.tmjs as js>
${js}
</#list>

<@csstm required="hello.css,test.css">
#aa{
	font-size: 11px;
}
</@csstm>

<@csstm required="tt.css">
#bb{
	font-size: 11px;
}
</@csstm>

<#list .globals.tmstyle as style>
${style}
</#list>

<#list .globals.tmcss as css>
${css}
</#list>