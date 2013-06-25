<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.aulas.business.questoes.javatester.*,java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String testClass = "RealizaTesteNaClasseSubmetida";
String path = "/usr/local/jboss-4.0.3SP1/server/default/deploy/aulas.war/WEB-INF/sandbox/";

File file = new File(path+testClass+".class");
if (file.exists()) out.println("Existe!"); else {
	out.println("Não existe!!<BR>");
	File dir = new File(path);
	String[] files = dir.list();
	for (int i = 0; i < files.length; i++) {
		out.println(files[i]+"<BR>");
	}
}


RuntimeExecutor r = new RuntimeExecutor(3000);
r.execute("java -cp "+path+" -Djava.security.manager "+testClass, null);
//r.execute("java -?", null);
if (r.getStdErr().equals("")) {
	out.println(r.getStdOut());
} else {
	out.println(r.getStdErr());
}

%>
</body>
</html>