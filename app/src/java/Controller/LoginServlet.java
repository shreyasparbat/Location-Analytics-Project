/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import model.dao.LoginDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author amanda
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        LoginDAO loginDao = new LoginDAO(); //creating object for LoginDao. This class contains main logic of the application.
        String userValidate = loginDao.authenticateUser(userName, password); //Calling authenticateUser function
        if (userValidate.equals("SUCCESS")) //If function returns success string then user will be rooted to Home page
        {
            session.setAttribute("userName", userName); //with setAttribute() you can define a "key" and value pair so that you can get it in future using getAttribute("key")
            request.getRequestDispatcher("/Home.jsp").forward(request, response);//RequestDispatcher is used to send the control to the invoked page.
        } else if (userValidate.equals("ADMIN")) {
            session.setAttribute("userName","Administrator");
            request.getRequestDispatcher("/Admin.jsp").forward(request, response);

        } else {
            request.setAttribute("errMessage", userValidate); //If authenticateUser() function returnsother than SUCCESS string it will be sent to Login page again. Here the error message returned from function has been stored in a errMessage key.
            request.getRequestDispatcher("/Login.jsp").forward(request, response);//forwarding the request

        }
    }

}
