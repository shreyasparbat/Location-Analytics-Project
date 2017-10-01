<%-- 
    Document   : Admin
    Created on : 23 Sep, 2017, 2:13:07 AM
    Author     : amanda
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="protect.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administrator Bootstrap Page</title>
    </head>
    <body>


        <form method="post" action="uploadFile" name="upform" enctype="multipart/form-data">
            <table width="60%" border="0" cellspacing="1" cellpadding="1" align="center" class="style1">
                <tr>
                    <td align="left">  <h1>Hello Admin!</h1>   </td>
                </tr>
                <tr>
                    <td align="left"><b>Select a file to upload for BootStrap:</b></td>
                </tr>
                <tr>
                    <td align="left">
                        <input type="file" name="uploadfile" size="50">
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        <input type="hidden" name="todo" value="upload">
                        <input type="submit" name="Submit" value="Upload">
                        <input type="reset" name="Reset" value="Cancel">
                    </td>
                </tr>
            </table>
        </form>
        <br>
        <br>
        <a href="logout.jsp">Click here to logout</a>
        
        
        <%
            HashMap<Integer, List<String>> locationErrors = (HashMap<Integer, List<String>>) session.getAttribute("location_errors");
            String total="";
            if (locationErrors != null) {
                Iterator<Integer> iter = locationErrors.keySet().iterator();
                while (iter.hasNext()) {
                    out.println("<h2>");
                    int index = iter.next();
                    List<String> locationErrorList = locationErrors.get(index);
                    Iterator<String> leIter = locationErrorList.iterator();
                    if (leIter.hasNext()) {
                         total= leIter.next();
                    }
                    while(leIter.hasNext()){
                        total = total+","+leIter.next();
                    }
                           
                    out.println("Errors found in row " + index + " in location.csv: " + total);
                    out.println("</h2> <br>");
                }
            }
            %>
    </body>
</html>
