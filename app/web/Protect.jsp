<%-- 
    Document   : protect
    Created on : 28 Sep, 2017, 10:51:36 AM
    Author     : Joel Tay
--%>

<%

// check if user is authenticated
String _userName = (String) session.getAttribute("userName");
if (_userName == null) {
  // not authenticated, force user to authenticate
  response.sendRedirect("Login.jsp");
  return;
} 
%>