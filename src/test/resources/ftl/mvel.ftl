test:${test}

<@mvel var="test">
str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

foreach (el : str) {
   System.out.print("[" + el + "]"); 
}

return str;
</@mvel>

test:${test}

test:${helper("MD5Helper").string2MD5("test")}
