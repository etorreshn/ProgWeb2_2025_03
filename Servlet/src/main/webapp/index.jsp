<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Menú Servlet Grupo #4" %>
</h1>
<br/>
<!--
<a href="menu">Hello Servlet</a>-->
<%
    response.sendRedirect("menu");
%>
</body>
</html>