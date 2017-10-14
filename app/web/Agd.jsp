<%-- 
    Document   : Agd
    Created on : 10 Oct, 2017, 2:56:28 PM
    Author     : amanda
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> AGD </title>
    </head>
    <body>

        <h1>Input request parameters </h1>

        <form method = "get" name ="AgdRequest_form" action="AgdServlet">
            Date <input type="date" name="date"><br/>
            Time <Input type ="time" name ="time"> 
            Seconds<select name ="seconds">
                <%
                    for (int i =0; i<=59; i++){
                        String sec = ""+ i;
                        
                        if(i<10){
                            sec = "0" + i;
                        } 
                        out.println("<option value=" + sec + ">"+ i + "</option>"); 
                    }
                    %>
            </select> 
            
            <br/>
            <input type="submit" value="Send">

        </form>  
        <%
            /*not done 

            if (request.getAttribute("errMessage") != null) {
                out.println(request.getAttribute("errMessage"));
            }
            */
            
        %> 

    </body>
</html>


