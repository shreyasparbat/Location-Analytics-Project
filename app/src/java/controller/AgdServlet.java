/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.AgdDAO;
import model.dao.StudentDAO;
import model.entity.Group;
import model.entity.Student;
import model.entity.TimeIntervals;
import model.entity.TimeIntervalsList;
import model.utility.AGDComparator;
import model.utility.TimeUtility;

/**
 *
 * @author amanda
 */
@WebServlet(name = "AgdServlet", urlPatterns = {"/AgdServlet"})
public class AgdServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Returns an arraylist of groups to the view page or returns an
     * error message if date time format is wrong
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //to take in user inputs
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String dateTime = date + " " + time + ":00";

        Timestamp startDateTime = null;
        Timestamp endDateTime = null;

        try {
            ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
            startDateTime = processingWindowArrayList.get(0);
            endDateTime = processingWindowArrayList.get(1);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errMessage", "Invalid date-time format");
            request.getRequestDispatcher("AutomaticGroupIdentification.jsp").forward(request, response);
        }

        //out.println(dateTime);
        AgdDAO agdDao = new AgdDAO();
        StudentDAO sDAO = new StudentDAO();
        TreeMap<String, Student> studentList = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);
        ArrayList<Group> list = agdDao.getStudentGroups(startDateTime, endDateTime, sDAO, studentList);
        
        
        //sort groups
        Collections.sort(list, new AGDComparator());
        request.setAttribute("studentCount", studentList.size());
        request.setAttribute("studentGroups", list);

        RequestDispatcher fwd = request.getRequestDispatcher("AutomaticGroupIdentification.jsp");
        fwd.forward(request, response);
        return;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
