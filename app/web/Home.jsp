<%-- 
    Document   : Home
    Created on : 23 Sep, 2017, 1:13:59 AM
    Author     : amanda
--%>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%  //Get username for main jumbotron  
    String username = (String) session.getAttribute("userName");
%>

<!DOCTYPE html>

<html lang="en">
    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="icon.jpg">

        <title>Home</title>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.min.css">

        <!-- Bootstrap core CSS -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Material Design Bootstrap -->
        <link href="assets/css/mdb.min.css" rel="stylesheet">

        <!-- Custom styles for this page -->
        <link href="assets/jumbotron.css" rel="stylesheet">

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

        <div class="container">

            <!-- Main jumbotron -->
            <div class="jumbotron">

                <div class="container">
                    <h1 class="display-4 text-center">Welcome <i> <%=username%> </i></h1>
                </div>
            </div>

            <!-- Row of columns -->
            <div class="row">

                <div class="col-md-4">
                    <!--Card 1-->
                    <div class="card">

                        <!--Card image-->
                        <img class="img-fluid" src="assets/heatmap.jpg" alt="Card image cap">

                        <!--Card content-->
                        <div class="card-body text-center">
                            <!--Title-->
                            <h4 class="card-title">Heat Maps</h4>
                            <!--Text-->
                            <p class="card-text">Get the crowd density of any floor of the SIS building on any given day and time.</p>
                            <a href="HeatMaps.jsp" class="btn btn-primary">Go</a>
                        </div>
                    </div>
                    <!--/.Card 1-->
                </div>

                <div class="col-md-4">
                    <!--Card 2-->
                    <div class="card">

                        <!--Card image-->
                        <img class="img-fluid" src="assets/groupidentification.jpg" alt="Card image cap">

                        <!--Card content-->
                        <div class="card-body text-center">
                            <!--Title-->
                            <h4 class="card-title">Group Identification</h4>
                            <!--Text-->
                            <p class="card-text">Get the list of groups (location of group and composition) at a particular timing.</p>
                            <a href="AutomaticGroupIdentification.jsp" class="btn btn-primary">Go</a>
                        </div>
                    </div>
                    <!--/.Card 2-->
                </div>

                <div class="col-md-4">
                    <!--Card 3-->
                    <div class="card">
                        <!--Card image-->
                        <img class="img-fluid" src="assets/locationreport.jpg" alt="Card image cap">

                        <!--Card content-->
                        <div class="card-body text-center">
                            <!--Title-->
                            <h4 class="card-title">Location Reports</h4>
                            <!--Text-->
                            <p class="card-text">Get various location based statistics (for the SIS building) for any given day and time.</p>
                            <a href="BasicLocationReports.jsp" class="btn btn-primary">Go</a>
                        </div>
                    </div>
                    <!--/.Card 3-->
                </div>
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
