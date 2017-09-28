<%-- 
    Document   : Home
    Created on : 23 Sep, 2017, 1:13:59 AM
    Author     : amanda
--%>
<%@include file="protect.jsp" %>>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html
    
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <h1>Hello 
            <%
                String name = (String) session.getAttribute("userName");
                out.println(name);
            %>
            !
        </h1>
    </body>
</html>
