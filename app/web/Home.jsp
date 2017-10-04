<%-- 
    Document   : Home
    Created on : 23 Sep, 2017, 1:13:59 AM
    Author     : amanda
--%>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%    String username = (String) session.getAttribute("userName");
%>

<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="icon.jpg">

        <title>Login Page</title>

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
                <img src="https://images.vexels.com/media/users/3/139221/isolated/preview/687e1be6fe067f0b44800ac61b259816-circle-lion-logo-safari-by-vexels.png" height="30" alt="">
            </a> 

            <!-- Collapse button -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>

            <!-- Collapsible content -->
            <div class="collapse navbar-collapse" id="navbarSupportedContent">

                <!-- Links -->
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="homePage.jsp">Home<span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="heatMaps.jsp">Heat Maps</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="locationReports.jsp">Location Reports</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="groupIdentification.jsp">Group Identification</a>
                    </li>

                </ul>
                <!-- Links -->

                <!-- logout -->
                <a class="nav-link nav-item btn btn-sm align-middle amber" href="Logout.jsp">Logout</a>
            </div>
            <!-- Collapsible content -->
        </nav>
        <!--/.Navbar-->

        <!-- Main jumbotron -->
        <div class="jumbotron">

            <div class="container">

                <h1 class="display-3">Welcome <%=username%></h1>
            </div>
        </div>

        <div class="container">

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
                            <a href="#" class="btn btn-primary">Go</a>
                        </div>
                    </div>
                    <!--/.Card 1-->
                </div>

                <div class="col-md-4">
                    <!--Card 2-->
                    <div class="card">

                        <!--Card image-->
                        <img class="img-fluid" src="assets/locationreport.jpg" alt="Card image cap">

                        <!--Card content-->
                        <div class="card-body text-center">
                            <!--Title-->
                            <h4 class="card-title">Location Reports</h4>
                            <!--Text-->
                            <p class="card-text">Get various location based statistics (for the SIS building) for any given day and time.</p>
                            <a href="#" class="btn btn-primary">Go</a>
                        </div>
                    </div>
                    <!--/.Card 2-->
                </div>

                <div class="col-md-4">
                    <!--Card 3-->
                    <div class="card">

                        <!--Card image-->
                        <img class="img-fluid" src="assets/groupidentification.jpg" alt="Card image cap">

                        <!--Card content-->
                        <div class="card-body text-center">
                            <!--Title-->
                            <h4 class="card-title">Group Identification</h4>
                            <!--Text-->
                            <p class="card-text">Get the list of groups (location of group and composition) at a particular timing.</p>
                            <a href="#" class="btn btn-primary">Go</a>
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
        <script type="text/javascript" src="assets/js/jquery-3.1.1.min.js"></script>
        <!-- Bootstrap tooltips -->
        <script type="text/javascript" src="assets/js/popper.min.js"></script>
        <!-- Bootstrap core JavaScript -->
        <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
        <!-- MDB core JavaScript -->
        <script type="text/javascript" src="assets/js/mdb.min.js"></script>
    </body>
</html>
