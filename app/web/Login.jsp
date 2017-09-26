<html> 
    <body> 
        Login Page
         <%

            if (request.getAttribute("errMessage") != null) {
                out.println(request.getAttribute("errMessage"));
            }

        %> 
        <form method = "post" name ="login_form" action="LoginServlet">
            Username: <input type="text" name="username"><br/>
            Password: <input type="password" name="password"> <br/>
            <input type="submit" name="submit" value="Submit" />  

        </form>  
       

    </body>
</html>