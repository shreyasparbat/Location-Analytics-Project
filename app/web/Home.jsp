<%-- 
    Document   : Home
    Created on : 23 Sep, 2017, 1:13:59 AM
    Author     : amanda
--%>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html

    <html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Home Page</title>
</head>
<body>
    <h1>
        Hello 
        <%                String name = (String) session.getAttribute("userName");
            out.println(name);
        %>
        !
    </h1>
    <br>
    <br>
    <a href="BasicLocationReports.jsp">Basic Location Reports</a>
    <br/>
    <a href="Logout.jsp">Click here to logout</a>
</body>
</html>
