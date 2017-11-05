<%-- 
    Document   : TopKCompanions
    Created on : Oct 25, 2017, 3:28:43 PM
    Author     : Ming Xuan
--%>

<%@page import="model.utility.StudentComparator"%>
<%@page import="java.util.Collections"%>
<%@page import="model.entity.Student"%>
<%@page import="model.entity.Group"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="model.utility.TopKUtility"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>

<!DOCTYPE html>
<html lang="en">

    <%  //getting companion list      
        List<String> macAddresses = TopKUtility.getStudentMacAddress();
        String macAddress = (String) request.getAttribute("student");
        Integer value = (Integer) request.getAttribute("k");
        HashMap<Integer, Group> companionList = (HashMap<Integer, Group>) request.getAttribute("companions");
        String email = (String) request.getAttribute("studentEmail");
        //getting error message
        String message = "";
        if (request.getAttribute("errMessage") != null) {
            message = (String) request.getAttribute("errMessage");
        }
    %>

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="icon.jpg">

        <title>Top-K Next Companions Report</title>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.min.css">

        <!-- Bootstrap core CSS -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Material Design Bootstrap -->
        <link href="assets/css/mdb.min.css" rel="stylesheet">

        <!-- Custom styles for this page -->

        <!-- Icon -->
        <link rel="icon" href="assets/logo.jpg">
    </head>

    <body>

        <!--Navbar-->
        <nav class="navbar fixed-top navbar-expand-lg navbar-dark blue-grey">

            <!-- Navbar brand (to be changed)-->
            <a class="navbar-brand" href="#">
                <img src="assets/logo.jpg" height="30" alt="">
            </a> 

            <!-- Collapse button -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>

            <!-- Collapsible content -->
            <div class="collapse navbar-collapse" id="navbarSupportedContent">

                <!-- Links -->
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="Home.jsp">Home<span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="HeatMaps.jsp">Heat Maps</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="AutomaticGroupIdentification.jsp">Group Identification</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Basic Location Reports</a>
                        <div class="dropdown-menu dropdown-primary" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" href="BreakdownReports.jsp">Breakdown by Year, Gender and School</a>
                            <a class="dropdown-item" href="TopkReports.jsp">Top-k Reports</a>
                        </div>
                    </li>

                </ul>
                <!-- Links -->

                <!-- logout -->
                <a class="nav-link nav-item btn btn-sm align-middle amber" href="Logout.jsp">Logout</a>
            </div>
            <!-- Collapsible content -->
        </nav>
        <!--/.Navbar-->

        <br/>
        <br/>
        <br/>
        <br/>

        <div class="container">

            <div class="row">

                <div class="col-md-3"></div>

                <div class="jumbotron col-md-6 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 text-center mb-4">Get Top K Next Companions</p>

                    <form action="BasicLocationReportsServlet">
                        <div class="form-group">
                            <br> Select Rank:
                            <select name="k">
                                <%  //printing k
                                    for (int i = 1; i <= 10; i++) {
                                        out.print("<option value = \"" + i + "\" ");
                                        if (i == 3) {
                                            out.print("selected");
                                        }
                                        out.print("> " + i + "</option>");
                                    }
                                %>
                            </select>
                        </div>
                        <div class="form-group">
                            <br> Select Student: 
                            <select name="user">
                                <%
                                    for (String student : macAddresses) {
                                        out.println("<option value = \"" + student + "\">" + student + "</option>");
                                    }
                                %>
                            </select>
                            <br>
                        </div>
                        <div class="form-group">
                            <label>Date: </label>
                            <input class="form-control" type="date" name="date" max="2025-12-31" required>
                        </div>

                        <div class="form-group">
                            <label>Time: </label>
                            <input class="form-control" type="time" name='time' required>
                        </div>

                        <input type="hidden" name="function" value="topKCompanions">

                        <div class="text-center">
                            <button type="submit" class="btn btn-amber">Go<i class="fa fa-paper-plane-o ml-1"></i></button>
                            <button type="reset" class="btn btn-blue-grey" name="Reset" value="Cancel">Cancel</button>  
                        </div>
                    </form>

                </div>


                <div class="col-md-3"></div>


            </div>

            <%  //printing error message
                if (!message.equals("")) {
                    out.print("<h4 class=\"text-center red-text\">" + message + "</h4>");
                }
            %>

            <br/>

            <div class="row">

                <div class="col-md-3"></div>

                <div class="col-md-6">

                    <%
                        if (value != null && companionList.size() > 0 && macAddress != null) {
                            out.println("<h5 class=\"text-center\">Companions to ");
                            if(email==null){
                                out.println("<u>" + macAddress + "</u><h5><br>");
                            }else{
                                out.println("<u>" + email + "</u><h5><br>");
                            }
                                     
                            out.println("<table border='1'>");
                            out.println("<tr><td> Rank </td> <td> Companion </td> <td> Mac-address</td> <td> Total co-location time </td>");
                            for (int i = 1; i <= value; i++) {
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(i);
                                out.println("</td>");
                                out.println("<td>");
                                List<Student> students = companionList.get(i).getOtherStudentsInGroup(macAddress);
                                //compare mac-addresses
                                Collections.sort(students, new StudentComparator());
                                for (Student s : students) {
                                    out.println(s.getEmail() + "<br>");
                                }
                                out.println("</td>");
                                out.println("<td>");
                                for (Student s : students) {
                                    out.println(s.getMacAddress() + "<br>");
                                }

                                //uncomment to see time comparison
                                //out.println(companionList.get(i).getTotalDuration());
                                out.println("</td>");
                                out.println("<td>");
                                out.println(companionList.get(i).getTotalDuration());
                                out.println(" seconds </td>");
                                out.println("</tr>");
                            }
                            out.println("</table>");
                        } else if (companionList.size() == 0) {
                            out.print("<h4 class=\"text-center red-text\">No companions found for given date time</h4>");
                        }
                    %>
                </div>

                <div class="col-md-3"></div>

            </div>
            <hr>

            <footer>
                <p>&copy; SE G1T3</p>
            </footer>
        </div> <!-- /container -->


        <!-- SCRIPTS
        ================================================== -->
        <!-- JQuery -->
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Bootstrap tooltips -->
        <script type="text/javascript" src="assets/js/popper.min.js"></script>
        <!-- Bootstrap core JavaScript -->
        <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
        <!-- MDB core JavaScript -->
        <script type="text/javascript" src="assets/js/mdb.min.js"></script>
    </body>
</html>
