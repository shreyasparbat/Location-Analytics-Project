<%-- 
    Document   : logout
    Created on : 28 Sep, 2017, 10:55:12 AM
    Author     : Joel Tay
--%>
<%
     session.invalidate();
    response.sendRedirect("Login.jsp");
%>
