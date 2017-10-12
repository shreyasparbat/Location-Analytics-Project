<%-- 
    Document   : HeatMaps
    Created on : 11 Oct, 2017, 1 AM
    Author     : shrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    //getting error message
    String message = "";
    if (request.getAttribute("errMessage") != null) {
        message = (String) request.getAttribute("errMessage");
    }

%>

<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="icon.jpg">

        <title>Heat Maps</title>

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
        <div class="container">

            <div class="row">

                <div class="col-md-4"></div>

                <div class="jumbotron col-md-4 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 text-center mb-4">Get Breakdown Report</p>

                    <form action="basic-location-reports">
                        <div class="form-group">
                            Floor:
                            <select name="level">
                                <option value="b1">Basement 1</option>
                                <option value="l1">Level 1</option>
                                <option value="l2">Level 2</option>
                                <option value="l3">Level 3</option>
                                <option value="l4">Level 4</option>
                                <option value="l5">Level 5</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Date: </label>
                            <input class="form-control" type="date" name="date">
                        </div>

                        <div class="form-group">
                            <label>Time: </label>
                            <input class="form-control" type="time" name='time'>
                        </div>

                        <input type='hidden' name='function' value="breakdownByYearGenderSchool">

                        <div class="text-center">
                            <button type="submit" class="btn btn-amber">Go <i class="fa fa-paper-plane-o ml-1"></i></button>
                        </div>

                        <%  //printing error message
                            if (!message.equals("")) {
                        %>
                        <h4 class="text-center red-text"><%=message%></h4>
                        <%
                            }
                        %>

                    </form>
                </div>

                <div class="col-md-4"></div>

            </div>

            <hr>

            <footer>
                <p>&copy; SE G1T3</p>
            </footer>
        </div>

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
