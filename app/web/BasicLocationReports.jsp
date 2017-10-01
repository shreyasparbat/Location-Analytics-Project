<%-- 
    Document   : BasicLocationReports
    Created on : 1 Oct, 2017, 5:34:56 PM
    Author     : shrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%//@include file='Protect.jsp' %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Basic Location Reports</h1>
        <br/>
        <form action="basic-location-reports" method="post">
            Function: 
            <select name="function">
                <option value="1">Breakdown by Age and Gender</option>
                <option value="2">Top-k popular places</option>
                <option value="3">Top-k companions</option>
                <option value="4">Top-k next places</option>
            </select>
            
            <br/>
            
            K-value: 
            <select name="k">
                <option value="1">1</option>
                <option value="2">2</option>
                <option selected="selected" value="3">3</option> <% //project requirement: default value of k = 3) %>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
            </select>
            
            <br/>    
                
            Hours:
            <select name="hours">
                <% 
                    for (int i = 0; i <= 23; i++) {
                %>
                <option value="<%=i%>"><%=i%></option>
                <%
                    }
                %>
            </select>
            
            <br/>
            
            Minutes: 
            <select name="minutes">
                <% 
                    for (int i = 15; i <= 59; i++) {
                %>
                <option value="<%=i%>"><%=i%></option>
                <%
                    }
                %>
            </select>
            
            <br/>
            
            <input type="submit">
        </form>
    </body>
</html>

