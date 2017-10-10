<%-- 
    Document   : Admin
    Created on : 23 Sep, 2017, 2:13:07 AM
    Author     : amanda
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="Protect.jsp" %>
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
            HashMap<Integer, List<String>> llErrors = (HashMap<Integer, List<String>>) session.getAttribute("ll_errors");
            HashMap<Integer, List<String>> demographErrors = (HashMap<Integer, List<String>>) session.getAttribute("demographics_errors");
        %>
         <p> 
            <%  if (demographErrors != null) {
                    String total = "";
                    Iterator<Integer> iter = demographErrors.keySet().iterator();
                    if(iter.hasNext()){
                        out.println("<h2> Errors found in Demographics.csv </h2> <br>");
                    }
                    while (iter.hasNext()) {
                        int index = iter.next();
                        List<String> demoErrorList = demographErrors.get(index);
                        Iterator<String> deIter = demoErrorList.iterator();
                        if (deIter.hasNext()) {
                            total = deIter.next();
                        }
                        while (deIter.hasNext()) {
                            total = total + "," + deIter.next();
                        }

                        out.println("Errors found in row " + index + " in demographics.csv: " + total);
                        out.println(" <br>");
                    }
                    demographErrors.clear();
                }
            %>
        </p>
         <p> 
            <%  if (llErrors != null) {
                    String total = "";
                    Iterator<Integer> iter = llErrors.keySet().iterator();
                    if(iter.hasNext()){
                        out.println("<h2> Errors found in LocationLookup.csv </h2> <br>");
                    }
                    while (iter.hasNext()) {
                        int index = iter.next();
                        List<String> locationLookupErrorList = llErrors.get(index);
                        Iterator<String> lleIter = locationLookupErrorList.iterator();
                        if (lleIter.hasNext()) {
                            total = lleIter.next();
                        }
                        while (lleIter.hasNext()) {
                            total = total + "," + lleIter.next();
                        }

                        out.println("Errors found in row " + index + " in LocationLookup.csv: " + total);
                        out.println(" <br>");
                    }
                    llErrors.clear();
                }
            %>
        </p>
        <p> 
            <%  if (locationErrors != null) {
                    String total = "";
                    Iterator<Integer> iter = locationErrors.keySet().iterator();
                    if(iter.hasNext()){
                        out.println("<h2> Errors found in Location.csv </h2> <br>");
                    }
                    while (iter.hasNext()) {                       
                        int index = iter.next();
                        List<String> locationErrorList = locationErrors.get(index);
                        Iterator<String> leIter = locationErrorList.iterator();
                        if (leIter.hasNext()) {
                            total = leIter.next();
                        }
                        while (leIter.hasNext()) {
                            total = total + "," + leIter.next();
                        }

                        out.println("Errors found in row " + index + " in location.csv: " + total);
                        out.println(" <br>");
                    }
                    locationErrors.clear();
                }
            %>
        </p>
        <br/>
        <br/>
        <p>
            Or, visit the <a href="Home.jsp">home page</a>.
        </p>
    </body>
</html>
