/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.dao.LocationReportsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.StudentDAO;
import model.entity.Group;
import model.entity.Location;
import model.entity.Student;
import model.utility.BreakdownUtility;
import model.utility.LocationComparator;
import model.utility.TimeUtility;
import model.utility.TopKUtility;

/**
 *
 * @author shrey
 */
@WebServlet(name = "BasicLocationReportsServlet", urlPatterns = {"/BasicLocationReportsServlet"})
public class BasicLocationReportsServlet extends HttpServlet {

    public BreakdownUtility bu = new BreakdownUtility();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Calls the appropriate function for each selection and returns a
     * hashmap of the breakdown to the view page.
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

        //Creating LocationReportsDAO obj
        //LocationReportsDAO locationReportsDAO = new LocationReportsDAO(startDateTime, endDateTime);
        switch (function) {

            //Breakdown by year and gender
            case "breakdownByYearGenderSchool": {
                //error message generation
                try {
                    ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
                    startDateTime = processingWindowArrayList.get(0);
                    endDateTime = processingWindowArrayList.get(1);
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errMessage", "Invalid date-time format");
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                }

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
                TreeMap<String, Student> studentMap = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);

                //Check which function to call according to options selected
                if (option2.equals("none2") && option3.equals("none3")) {
                    //calls one value function if only the first option is filled
                    TreeMap<String, Integer> percentageOneList = bu.percentageOneOption(option1, studentMap);

                    //send back to View page
                    request.setAttribute("percentageOneList", percentageOneList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);

                } else if (!option2.equals("none2") && option3.equals("none3")) {
                    //calls 2 option function
                    TreeMap<String, TreeMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option2, studentMap);

                    //send back to view  page
                    request.setAttribute("percentageTwoList", percentageTwoList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                } else if (option2.equals("none2") && !option3.equals("none3")) {
                    //calls 2 option function
                    TreeMap<String, TreeMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option3, studentMap);

                    //send back to view  page
                    request.setAttribute("percentageTwoList", percentageTwoList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                } else {
                    //calls 3 function option
                    TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList = bu.percentageAllOptions(option1, option2, option3, studentMap);

                    //send back to view  page
                    request.setAttribute("percentageAllList", percentageAllList);
                    request.getRequestDispatcher("/BreakdownReports.jsp").forward(request, response);
                }

                break;
            }

            //Top-k popular places
            case "topKPopularPlaces": {
                //Error message generation
                try {
                    ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
                    startDateTime = processingWindowArrayList.get(0);
                    endDateTime = processingWindowArrayList.get(1);
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errMessage", "Invalid date-time format");
                    request.getRequestDispatcher("/TopKPopularPlaces.jsp").forward(request, response);
                }

                //Getting Parametres to process Top K popular places request
                int k = Integer.parseInt(request.getParameter("k"));
                LocationReportsDAO locationReportsDAO = new LocationReportsDAO(startDateTime, endDateTime);
                // key is the semantic place
                // value is the number of people at that semantic place in the given time window
                LinkedHashMap<String, Integer> popularPlaceList = locationReportsDAO.topkPopularPlaces();
                //setting attributes for display page
                request.setAttribute("k", k);
                request.setAttribute("startDateTime", startDateTime);
                request.setAttribute("endDateTime", endDateTime);
                request.setAttribute("popularPlaces", popularPlaceList);
                request.getRequestDispatcher("/TopKPopularPlaces.jsp").forward(request, response);
                break;
            }

            //Top-k companions
            case "topKCompanions": {
                //Error message generation
                try {
                    ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
                    startDateTime = processingWindowArrayList.get(0);
                    endDateTime = processingWindowArrayList.get(1);
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errMessage", "Invalid date-time format");
                    request.getRequestDispatcher("/TopKCompanions.jsp").forward(request, response);
                }

                int k = Integer.parseInt(request.getParameter("k"));
                String studentMacAddress = request.getParameter("user");
                LocationReportsDAO locationReportDAO = new LocationReportsDAO(startDateTime, endDateTime);
                // key is the rank from 1 - n
                // value is the merged groups
                HashMap<Integer, Group> companionList = locationReportDAO.topkCompanions(k, studentMacAddress);
                String stuEmail = TopKUtility.getStudentEmail(studentMacAddress);
                if (stuEmail != null) {
                    request.setAttribute("studentEmail", stuEmail);
                }
                request.setAttribute("student", studentMacAddress);

                request.setAttribute("k", k);
                request.setAttribute("companions", companionList);
                request.getRequestDispatcher("/TopKCompanions.jsp").forward(request, response);
                break;
            }

            //Top-k next places
            case "topKNextPlaces": {
                //Error message generation
                Timestamp startDateTimeTwo = null;
                Timestamp endDateTimeTwo = null;
                try {
                    //getting first processing window
                    ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
                    startDateTime = processingWindowArrayList.get(0);
                    endDateTime = processingWindowArrayList.get(1);
                    //getting second processing window
                    ArrayList<Timestamp> processingWindowTwoArrayList = TimeUtility.getNextProcessingWindow(dateTime);
                    startDateTimeTwo = processingWindowTwoArrayList.get(0);
                    endDateTimeTwo = processingWindowTwoArrayList.get(1);
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errMessage", "Invalid date-time format");
                    request.getRequestDispatcher("/TopKNextPlaces.jsp").forward(request, response);
                }

                //getting K value 
                int k = Integer.parseInt(request.getParameter("k"));
                String semanticPlace = request.getParameter("place");
                LocationReportsDAO locationReportsDAO = new LocationReportsDAO(startDateTime, endDateTime, startDateTimeTwo, endDateTimeTwo);
                ArrayList<String> studentsList = locationReportsDAO.peopleInSemanticPlace(semanticPlace);
                ArrayList<ArrayList<String>> comparisonWindow = locationReportsDAO.topkNextPlaces();
                HashMap<String, Location> locationMap = new HashMap<>();
                ArrayList<Location> locationList = new ArrayList<>();

                //for each set of comparisonWindow
                for (ArrayList<String> s : comparisonWindow) {
                    //get semantic place
                    String sp = s.get(0);
                    //check if semantic place is mapped. If it isn't add it to mapping
                    if (locationMap.get(sp) == null) {
                        locationMap.put(sp, new Location(sp));
                    }
                    //Location l is the semantic place
                    Location l = locationMap.get(sp);
                    //get list of people in semantic place
                    ArrayList<String> peopleList = l.getStudents();
                    //if the macaddress in question is in the studentList and not in the place, add the student into the place to track his next location
                    if ((!peopleList.contains(s.get(1))) && studentsList.contains(s.get(1))) {
                        peopleList.add(s.get(1));
                    }
                }
                //converts hashmap to ArrayList of location for display
                Iterator iter = locationMap.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    Location l = locationMap.get(key);
                    locationList.add(l);
                }

                //sorting the locationlist
                Collections.sort(locationList, new LocationComparator());
                request.setAttribute("time1", startDateTimeTwo);
                request.setAttribute("time2", endDateTimeTwo);
                request.setAttribute("list", comparisonWindow);
                //request.setAttribute("list", test);
                request.setAttribute("k", k);
                request.setAttribute("studentList", studentsList);
                request.setAttribute("place", semanticPlace);
                request.setAttribute("locationList", locationList);
                request.getRequestDispatcher("/TopKNextPlaces.jsp").forward(request, response);
                break;
            }
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
