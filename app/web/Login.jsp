<html>
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
        <link href="assets/signin.css" rel="stylesheet">

        <!-- Icon -->
        <link rel="icon" href="assets/logo.jpg">
    </head>
    <body> 
        <%
            String message = "";
            if (request.getAttribute("errMessage") != null) {
                message = (String) request.getAttribute("errMessage");
            }

        %> 
        <!-- container -->
        <div class="container">

            <!-- row -->
            <div class="row">

                <div class="col-md-4"></div>

                <!-- sign_in_col -->
                <div class="jumbotron col-md-4 centre-of-page">

                    <form action='LoginServlet' method='post' name ="login_form">

                        <div class="text-center animated fadeInDown page-header">
                            <h1>SLOCA</h1>
                        </div>

                        <div class="md-form animated fadeInLeft">

                            <i class="fa fa-user prefix grey-text"></i>
                            <input type="text" id="defaultForm-email" class="form-control" name='username' required>
                            <label for="defaultForm-email">Your username</label>
                        </div>

                        <div class="md-form animated fadeInRight">

                            <i class="fa fa-lock prefix grey-text"></i>
                            <input type="password" id="defaultForm-pass" class="form-control" name='password' required>
                            <label for="defaultForm-pass">Your password</label>
                        </div>

                        <div class="text-center animated fadeInUp">

                            <button type="submit" class="btn btn-amber">Login</button>
                        </div>

                        <%                            
                            if (!message.equals("")) {
                        %>
                        <h4 class="text-center red-text"><%=message%></h4>
                        <%
                            }
                        %>
                    </form>
                </div>
                <!-- /sign_in_col -->
                <div class="col-md-4"></div>
            </div> 
            <!-- /row --> 
        </div> 
        <!-- /container -->

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