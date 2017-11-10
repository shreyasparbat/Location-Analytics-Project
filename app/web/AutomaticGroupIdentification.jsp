<%-- 
    Document   : Agd
    Created on : 10 Oct, 2017, 2:56:28 PM
    Author     : amanda
--%>


<%@page import="java.util.TreeMap"%>
<%@page import="model.entity.TimeIntervals"%>
<%@page import="java.util.Iterator"%>
<%@page import="model.entity.TimeIntervalsList"%>
<%@page import="java.util.HashMap"%>
<%@page import="model.entity.Student"%>
<%@page import="model.entity.Group"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="icon.jpg">

        <title>Automatic Group Identification</title>

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

        <%  //Getting student groups
            Integer count = (Integer) request.getAttribute("studentCount");
            ArrayList<Group> studentGroups = (ArrayList<Group>) request.getAttribute("studentGroups");
            String errorMsg = (String) request.getAttribute("errMessage");
        %>

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

                <div class="col-md-4"></div>

                <div class="jumbotron col-md-4 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 text-center mb-4">Get Groups</p>

                    <form name="AgiRequest_form" action="AgiServlet">

                        <div class="form-group">
                            <label>DateTime: </label>
                            <input class="form-control" type="datetime-local" name="datetime" min ="2010-01-01T00:00:00" max="2025-12-31T23:59:59" step="1" required>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-amber">Go<i class="fa fa-paper-plane-o ml-1"></i></button>
                            <button type="reset" class="btn btn-blue-grey" name="Reset" value="Cancel">Cancel</button>  
                        </div>
                    </form>
                </div>

                <div class="col-md-4"></div>

            </div>

            <%  //print error message              
                if (errorMsg != null) {
                    out.println("<h4 class=\"text-center red-text\">" + errorMsg + "</h4>");
                }

                //printing table
                if (count != null && studentGroups != null) {
                    out.println("<table border='1' width='100%'>");
                    out.println("<tr> <th> Count of Students: " + count.toString() + " </th> </tr>");
                    out.println("<tr> <th> Count of groups : " + studentGroups.size() + " </th> </tr>");
                    out.println("</table>");
                    out.println("<table border='1'>");
                    for (Group g : studentGroups) {
                        out.println("<tr>");
                        ArrayList<Student> students = g.getGroup();
                        TreeMap<Integer, TimeIntervalsList> records = g.getRecord();
                        Iterator<Integer> iter = records.keySet().iterator();
                        double duration = 0;
                        out.println("<td> Group size: " + students.size() + " </td>");

                        out.println("<td> <table width='15%'>  ");
                        for (Student s : students) {
                            out.println("<tr> <td>" + s.getEmail() + "</td> <td>" + s.getMacAddress() + "</td> </tr>");
                        }
                        out.println(" </table> </td> ");
                        out.println("<td>");
                        int locationRank = 1;
                        while (iter.hasNext()) {
                            int place = iter.next();
                            double seconds = records.get(place).getDuration();
                            duration += seconds;
                            out.println("Location " + locationRank + ": " + place + "<br/>Time: " + seconds + " seconds<br>");
                            // for reference *testing
                            /*
                            ArrayList<TimeIntervals> timeList = records.get(place).getList();
                            for (TimeIntervals ti : timeList) {
                                out.println(ti.getStartTime().toString() + " - " + ti.getEndTime().toString());
                                out.println("<br>");  
                            }
                             */
                            locationRank++;
                        }
                        out.println("</td>");

                        out.println("<td> Total Time spent: " + duration + " seconds </td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
            %>

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