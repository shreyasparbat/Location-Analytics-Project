/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.dao.LocationReportsDAO;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.StudentDAO;
import model.entity.Student;
import model.utility.BreakdownUtility;
import model.utility.TimeUtility;

/**
 *
 * @author shrey
 */
@WebServlet(name = "BasicLocationReportsServlet", urlPatterns = {"/BasicLocationReportsServlet"})
public class BasicLocationReportsServlet extends HttpServlet {
    public BreakdownUtility bu = new BreakdownUtility();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Calls the appropriate function for each selection and returns a hashmap of the breakdown to the view page.
     *  
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        //retrieve request parameters
        String function = request.getParameter("function");
        String date = request.getParameter("date");
        String time = request.getParameter("time");

        //Make dateTime string of correct format
        String dateTime = date + " " + time + ":00";

        //Obtaining SQL Timestamp objects for start and end time of processing window
        Timestamp startDateTime = null;
        Timestamp endDateTime = null;

        try {
            ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
            startDateTime = processingWindowArrayList.get(0);
            endDateTime = processingWindowArrayList.get(1);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errMessage", "Invalid date-time format");
            request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
        }

        //Creating LocationReportsDAO obj
        //LocationReportsDAO locationReportsDAO = new LocationReportsDAO(startDateTime, endDateTime);
        switch (function) {

            //Breakdown by year and gender
            case "breakdownByYearGenderSchool": {

                //Get option parameters
                String option1 = request.getParameter("option1");
                String option2 = request.getParameter("option2");
                String option3 = request.getParameter("option3");

                //check for "overlapping options" error
                if (option1.equals(option2) || option1.equals(option3) || option2.equals(option3)) {
                    request.setAttribute("errMessage", "1 or more options overlapping");
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                }

                //Getting HashMap of all students in the SIS building during processing window
                StudentDAO sDAO = new StudentDAO();
                HashMap<String, Student> studentMap = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);

                //Check which function to call according to options selected
                if (option2.equals("none2") && option3.equals("none3")) {
                    //calls one value function if only the first option is filled
                    HashMap<String, Integer> percentageOneList = bu.percentageOneOption(option1, studentMap);

                    //send back to View page
                    request.setAttribute("percentageOneList", percentageOneList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);

                } else if (!option2.equals("none2") && option3.equals("none3")) {
                    //calls 2 option function
                    HashMap<String, HashMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option2, studentMap);

                    //send back to view  page
                    request.setAttribute("percentageTwoList", percentageTwoList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                } else if (option2.equals("none2") && !option3.equals("none3")) {
                    //calls 2 option function
                    HashMap<String, HashMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option3, studentMap);
                    
                    //send back to view  page
                    request.setAttribute("percentageTwoList", percentageTwoList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                } else {
                    //calls 3 function option
                    HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList = bu.percentageAllOptions(option1, option2, option3, studentMap);
                    
                    //send back to view  page
                    request.setAttribute("percentageAllList", percentageAllList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                }

                break;
            }

            //Top-k popular places
            case "topKPopularPlaces": {
                //int k = 0;
                //LocationReportsDAO.topkPopularPlaces(k, 10);
                request.getRequestDispatcher("/TopKPopularPlaces.jsp").forward(request, response);
            }
            break;

            //Top-k companions
            case "topKCompanions": {
                //int k = 0;
                //LocationReportsDAO.topkCompanions(k);
                request.getRequestDispatcher("/TopKCompanions.jsp").forward(request, response);
            }
            break;

            //Top-k next places
            case "topKNextPlaces": {
                //int k = 0;
                //LocationReportsDAO.topkNextPlaces(k);
                request.getRequestDispatcher("/TopKNextPlaces.jsp").forward(request, response);
            }
            break;
        }
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
