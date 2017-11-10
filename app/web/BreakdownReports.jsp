<%-- 
    Document   : BreakdownReports
    Created on : 9 Oct, 2017, 5:20:44 PM
    Author     : shrey
--%>

<%@page import="java.util.TreeMap"%>
<%@page import="model.utility.BreakdownUtility"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>

<%    BreakdownUtility bu = new BreakdownUtility();
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

        <title>Breakdown Reports</title>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.min.css">

        <!-- Bootstrap core CSS -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Material Design Bootstrap -->
        <link href="assets/css/mdb.min.css" rel="stylesheet">

        <!-- Custom styles for this page -->

        <!-- Icon -->
        <link rel="icon" href="assets/logo.jpg">

        <!-- Importing Chart.js -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.0/Chart.bundle.min.js"></script>

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

                <div class="col-md-4"></div>

                <div class="jumbotron col-md-4 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 text-center mb-4">Get Breakdown Report</p>

                    <form action="BasicLocationReportsServlet">
                        <div class="form-group">
                            Option 1:
                            <select name="option1">
                                <option value="year">Year</option>
                                <option value="gender">Gender</option>
                                <option value="school">School</option>
                            </select>
                        </div>

                        <div class="form-group">
                            Option 2:
                            <select name="option2">
                                <option value="none2">None</option>
                                <option value="year">Year</option>
                                <option value="gender">Gender</option>
                                <option value="school">School</option>
                            </select>
                        </div>

                        <div class="form-group">
                            Option 3:
                            <select name="option3">
                                <option value="none3">None</option>
                                <option value="year">Year</option>
                                <option value="gender">Gender</option>
                                <option value="school">School</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>DateTime: </label>
                            <input class="form-control" type="datetime-local" name="datetime" min ="2010-01-01T00:00:00" max="2025-12-31T23:59:59" step="1" required>
                        </div>

                        <input type='hidden' name='function' value="breakdownByYearGenderSchool">

                        <div class="text-center">
                            <button type="submit" class="btn btn-amber">Go<i class="fa fa-paper-plane-o ml-1"></i></button>
                            <button type="reset" class="btn btn-blue-grey" name="Reset" value="Cancel">Cancel</button>  
                        </div>
                    </form>
                </div>

                <div class="col-md-4"></div>

            </div>

            <%  //printing error message
                if (!message.equals("")) {
                    out.print("<h4 class=\"text-center red-text\">" + message + "</h4>");
                }
            %>

            <!-- Instantiating required variables -->
            <%
                //maps
                TreeMap<String, Integer> percentageOneList = new TreeMap<>();
                TreeMap<String, TreeMap<String, Integer>> percentageTwoList = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList = new TreeMap<>();

                //List of json strings to be used for printing
                ArrayList<String> gsonStringList = new ArrayList<>();

                //populating gsonStringList with empty strings so as to avoid the IncesOutOfBoundsException
                for (int i = 0; i < 6; i++) {
                    gsonStringList.add("");
                }
            %>

            <!-- Printing chart according number of options requested -->
            <%  //printing percentageOneList if it exits
                if (request.getAttribute("percentageOneList") != null) {
                    percentageOneList = (TreeMap<String, Integer>) request.getAttribute("percentageOneList");
                    //getting output list
                    ArrayList<String> outputArrayList = new ArrayList<>();
                    //making bar chart
                    try {
                        gsonStringList = bu.printBarChart(percentageOneList);
                        outputArrayList = bu.printInner(percentageOneList);
                        out.print("<canvas id=\"inner\"></canvas>");

                        //error handling
                    } catch (IllegalArgumentException e) {
                        out.print("<h4 class=\"text-center red-text\">Records Not Found!</h4>");
                    }

                    //printing outputList
                    for (String output : outputArrayList) {
                        out.print(output);
                    }
                    
                }

                //printing percentageTwoList if it exits
                if (request.getAttribute("percentageTwoList") != null) {
                    percentageTwoList = (TreeMap<String, TreeMap<String, Integer>>) request.getAttribute("percentageTwoList");

                    //getting out put list
                    ArrayList<String> outputArrayList = new ArrayList<>();
                    try {
                        outputArrayList = bu.printMiddle(percentageTwoList);
                    } catch (IllegalArgumentException e) {
                        out.print("<h4 class=\"text-center red-text\">Records Not Found!</h4>");
                    }

                    //printing outputList
                    for (String output : outputArrayList) {
                        out.print(output);
                    }
                }

                //printing percentageAllList if it exits
                if (request.getAttribute("percentageAllList") != null) {
                    percentageAllList = (TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>) request.getAttribute("percentageAllList");

                    //getting out put list
                    ArrayList<String> outputArrayList = new ArrayList<>();
                    try {
                        outputArrayList = bu.printOuter(percentageAllList);
                    } catch (Exception e) {
                        out.print("<h4 class=\"text-center red-text\">Records Not Found!</h4>");
                    }

                    //printing outputList
                    for (String output : outputArrayList) {
                        out.print(output);
                    }
                }
            %>

            <!-- Bar-chart script for pretty interface-->
            <script>
                var ctx = document.getElementById("inner");
                var myChart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: <%=gsonStringList.get(0)%>,
                        datasets: [{
                                label: '# of Students',
                                data: <%=gsonStringList.get(1)%>,
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.2)',
                                    'rgba(54, 162, 235, 0.2)',
                                    'rgba(255, 206, 86, 0.2)',
                                    'rgba(75, 192, 192, 0.2)',
                                    'rgba(153, 102, 255, 0.2)',
                                    'rgba(255, 159, 64, 0.2)'
                                ],
                                borderColor: [
                                    'rgba(255,99,132,1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)',
                                    'rgba(75, 192, 192, 1)',
                                    'rgba(153, 102, 255, 1)',
                                    'rgba(255, 159, 64, 1)'
                                ],
                                borderWidth: 1
                            }]
                    },
                    options: {
                        scales: {
                            yAxes: [{
                                    ticks: {
                                        beginAtZero: true
                                    }
                                }]
                        }
                    }
                });
            </script>

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

