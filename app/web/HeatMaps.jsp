<%-- 
    Document   : HeatMaps
    Created on : 11 Oct, 2017, 1 AM
    Author     : shrey
--%>

<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>

<%    //getting error message
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
        <link rel="stylesheet" href="assets/css/heatmapCSS.css">

        <!-- Icon -->
        <link rel="icon" href="assets/logo.jpg">

        <!-- JQuery -->
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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

                <div class="jumbotron col-md-10 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 mb-4">Get Heat Maps</p>

                    <form id="heat-map-form" action="HeatMapServlet" class="form-inline">
                        <div class="form-group">
                            Floor:
                            <select name="level">
                                <option value="B1">Basement 1</option>
                                <option value="L1">Level 1</option>
                                <option value="L2">Level 2</option>
                                <option value="L3">Level 3</option>
                                <option value="L4">Level 4</option>
                                <option value="L5">Level 5</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Date: </label>
                            <input class="form-control" type="date" name="date" max="2025-12-31" required>
                        </div>

                        <div class="form-group">
                            <label>Time: </label>
                            <input class="form-control" type="time" name='time' required>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-amber">Go<i class="fa fa-paper-plane-o ml-1"></i></button>
                            <button type="reset" class="btn btn-blue-grey" name="Reset" value="Cancel">Cancel</button>  
                        </div>
                    </form>
                </div>

                <div class="col-md-2">
                    <br/>
                    <%  //printing error message
                        if (!message.equals("")) {
                            out.print("<br/><br/><h4 class=\"text-center red-text\">" + message + "</h4>");
                        }
                    %>
                </div>

            </div>

            <!-- HeatMap row -->
            <div class ="row">
                <!-- Actual HeatMap -->
                <div class="col-md-9">

                    <%
                        //getting reply from servlet
                        String level = (String) request.getAttribute("level");
                        JsonObject result = (JsonObject) request.getAttribute("result");

                        //converting array to json representation
                        Gson gson = new GsonBuilder().create();
                        String heatMapData = gson.toJson(result);

                        //if map is about to be printed
                        if (level != null) {
                            //print map name
                            out.print("<h2>" + level + "</h2>");
                        }

                        //printing correct map
                        if ("B1".equals(level)) { %>

                    <%@include file='assets/maps/B1.svg'%>

                    <%  }

                        if ("L1".equals(level)) { %>

                    <%@include file='assets/maps/L1.svg'%>

                    <%  }
                        if ("L2".equals(level)) { %>

                    <%@include file='assets/maps/L2.svg'%>

                    <%  }
                        if ("L3".equals(level)) { %>

                    <%@include file='assets/maps/L3.svg'%>

                    <%  }
                        if ("L4".equals(level)) { %>

                    <%@include file='assets/maps/L4.svg'%>

                    <%  }
                        if ("L5".equals(level)) { %>

                    <%@include file='assets/maps/L5.svg'%>
                    <%  }
                    %>

                </div>

                <div class="col-md-1"></div>

                <%  //printing legends and coloring map (only if map is being printed)
                    if (level != null) {
                        //set css colour scheme according to heat values                
%>

                <script>
                    //for colouring
                    //var colors = ['#ffffd4', '#fee391', '#fec44f', '#fe9929', '#ec7014', '#cc4c02', '#8c2d04'];
                    var colors = ['#ffffb2', '#fed976', '#feb24c', '#fd8d3c', '#fc4e2a', '#e31a1c', '#b10026'];

                    //getting json obj
                    var result = JSON.parse('<%=heatMapData%>');

                    //looping through it
                    result.heatMapJsonArray.forEach(function (location) {
                        //retrieving objs
                        var id = location['semantic-place'];
                        var heatValue = location['heat-value'];

                        //colouring
                        $('#' + id).css('fill', colors[heatValue]);
                    });
                </script>

                <!-- Legends -->
                <div class="col-md-2">

                    <legend>
                        <h3>Heat Value</h3>
                    </legend>
                    <ul id="legend">
                        <li>
                            <span class="color-block" ></span>
                            0
                        </li>

                        <li>
                            <span class="color-block"></span>
                            1 to 2
                        </li>

                        <li>
                            <span class="color-block"></span>
                            3 to 5
                        </li>

                        <li>
                            <span class="color-block"></span>
                            6 to 10
                        </li>

                        <li>
                            <span class="color-block"></span>
                            11 to 20
                        </li>

                        <li>
                            <span class="color-block"></span>
                            21 to 30
                        </li>

                        <li>
                            <span class="color-block"></span>
                            31 and more
                        </li>
                    </ul>
                </div>

                <%  }
                %>
            </div>

            <hr>

            <footer>
                <p>&copy; SE G1T3</p>
            </footer>
        </div>

        <!-- SCRIPTS
        ================================================== -->       
        <!-- Bootstrap tooltips -->
        <script type="text/javascript" src="assets/js/popper.min.js"></script>
        <!-- Bootstrap core JavaScript -->
        <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
        <!-- MDB core JavaScript -->
        <script type="text/javascript" src="assets/js/mdb.min.js"></script>
    </body>
</html>
