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

                <div class="jumbotron col-md-9 centre-of-page">

                    <!-- Form get table -->

                    <p class="h5 mb-4">Get Breakdown Report</p>

                    <form action="basic-location-reports" class="form-inline">
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

                    </form>
                </div>

                <div class="col-md-4"></div>

            </div>

            <%  //printing error message
                if (!message.equals("")) {
                    out.print("<h4 class=\"text-center red-text\">" + message + "</h4>");
                }
            %>

            <!-- HeatMap row -->
            <div class ="row">
                <!-- Actual HeatMap -->
                <div class="col-md-9">
                    <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
                         width="800px" height="600px" viewBox="0 0 800 600" enable-background="new 0 0 800 600" xml:space="preserve">
                    <path fill="#494949" stroke="#000000" stroke-miterlimit="10" d="M99.604,143.5l0.407-109h602.253l-1.781,92.782l38.327,0.788
                          l-78.055,85.43H625.5v41h-244v87h1.145l-0.134-11.612c0,0,63.39,5.511,86.991,33.995c23.602,28.486,0.814,54.617,0.814,54.617
                          h-8.139l-1.629,9.023l-17.904,7.361l-5.696,19.55l-15.464,7.334l-3.484,19.539l-18.489,7.327l-8.953,17.091l-18.72,5.783
                          l-7.324,17.991h-19.533l-8.952,17.818l-17.905,5.802l-12.208,16.38h-9.766l1.628,7.586c0,0-0.814,4.607-21.16,3.793
                          c-20.346-0.812-69.178-27.347-93.594-55.017c-24.416-27.672-34.996-75.362-34.996-75.362h6.511l0.814-6.162l-48.019-2.673
                          c0,0,11.395-62.783,18.719-64.412c7.325-1.627-7.325,1.571-7.325,1.571l-4.069-128.618l12.208-1.267L93.5,143.5H99.604z"/>
                    <polygon fill="#D8D8D8" stroke="#000000" stroke-miterlimit="10" points="625.5,213.553 625.5,254.5 382.5,254.5 382.5,389.347 
                             333.5,390.159 333.5,340.514 230.5,341.327 230.5,408.879 208.662,409.104 208.254,338.5 182.5,338.5 182.5,271.337 180.582,151.5 
                             625.5,151.5 "/>
                    <rect x="230.5" y="341.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="103" height="48"/>
                    <rect x="233.5" y="282.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="100" height="28"/>
                    <rect x="224.5" y="200.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="51" height="51"/>
                    <rect x="275.5" y="200.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="31" height="31"/>
                    <rect x="306.5" y="200.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="44" height="51"/>
                    <rect x="382.5" y="268.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="37" height="51"/>
                    <rect x="402.5" y="196.5" fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" width="99" height="58"/>
                    <polygon fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" points="230.5,389.5 382.5,389.5 382.5,426.5 314.055,426.5 
                             295.336,538.139 202.557,522.672 199.708,442.5 187.5,442.5 187.5,409.691 230.5,408.879 "/>
                    <polygon fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" points="203.371,169.5 180.87,169.5 179.5,143.5 94.314,143.5 
                             115.474,211.895 205.812,212.671 "/>
                    <path fill="#AAA9A9" stroke="#000000" stroke-miterlimit="10" d="M115.338,212.672l15.803,51.712c0,0,4.646,9.852,9.801,13.107
                          c5.153,3.255,10.333,3.162,10.333,3.162l31.225-9.045v-24.687l25.491-0.248l-1.903-33.934"/>

                    <g>
                    <rect id="SMUSISL2SR2-1" class="heatmap-area" x="97.5" y="34.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="160" height="117"/>
                    <rect x="97.568" y="80.895" fill="none" width="153.006" height="38.522"/>
                    <text transform="matrix(1 0 0 1 155.3979 89.4868)" font-family="'Arial-BoldMT'" font-size="12">SR 2-1</text>
                    </g>

                    <g>
                    <rect id="SMUSISL2SR2-2" class="heatmap-area" x="290.5" y="34.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="118" height="117"/>
                    <rect x="290.726" y="80.895" fill="none" width="116.106" height="38.522"/>
                    <text transform="matrix(1 0 0 1 330.1055 89.4868)" font-family="'Arial-BoldMT'" font-size="12">SR 2-2</text>
                    </g>

                    <g>
                    <rect id="SMUSISL2SR2-3" class="heatmap-area" x="408.5" y="34.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="115" height="117"/>
                    <rect x="408.191" y="80.895" fill="none" width="116.107" height="38.522"/>
                    <text transform="matrix(1 0 0 1 447.5723 89.4868)" font-family="'Arial-BoldMT'" font-size="12">SR 2-3</text>
                    </g>

                    <g>
                    <rect id="SMUSISL2SR2-4" class="heatmap-area" x="558.5" y="34.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="144" height="117"/>
                    <rect x="559.025" y="80.895" fill="none" width="142.422" height="38.522"/>
                    <text transform="matrix(1 0 0 1 611.5625 89.4868)" font-family="'Arial-BoldMT'" font-size="12">SR 2-4</text>
                    </g>

                    <g>
                    <rect id="SMUSISL2STUDYAREA1" class="heatmap-area" x="501.5" y="196.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="124" height="58"/>
                    <rect x="500.215" y="222.994" fill="none" width="125.547" height="32.506"/>
                    <text transform="matrix(1 0 0 1 526.3086 231.5859)" font-family="'Arial-BoldMT'" font-size="12">Study Area 1</text>
                    </g>

                    <g>
                    <rect id="SMUSISL2STUDYAREA2" class="heatmap-area" x="149.5" y="245.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="59" height="93"/>
                    <rect x="150.47" y="268.489" fill="none" width="56.969" height="57.78"/>
                    <text transform="matrix(1 0 0 1 162.2881 277.0815)"><tspan x="0" y="0" font-family="'Arial-BoldMT'" font-size="12">Study </tspan><tspan x="3.325" y="14.4" font-family="'Arial-BoldMT'" font-size="12">Area </tspan><tspan x="13.33" y="28.8" font-family="'Arial-BoldMT'" font-size="12">2</tspan></text>
                    </g>

                    <g>
                    <rect id="SMUSISL2LOBBY" class="heatmap-area" x="333.5" y="251.5" fill="#FFFFFF" stroke="#000000" stroke-miterlimit="10" width="49" height="138"/>
                    <rect x="333.5" y="302.609" fill="none" width="49" height="57.781"/>
                    <text transform="matrix(1 0 0 1 340.0029 311.2012)" font-family="'Arial-BoldMT'" font-size="12">Lobby</text>
                    </g>

                    </svg>
                </div>

                <!-- Legends -->
                <div class="col-md-3">
                    <legend>
                        <h3>Data</h3>
                    </legend>

                    <strong>Semantic place: </strong> <br/>
                    <span id="semantic-place">-</span> <br/>

                    <br/>

                    <strong>Number of people: </strong> <br/>
                    <span id="num-people">-</span> <br/>

                    <legend>
                        <h3>Legend</h3>
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
