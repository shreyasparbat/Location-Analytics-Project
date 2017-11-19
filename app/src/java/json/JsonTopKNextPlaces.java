/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.LocationReportsDAO;
import model.entity.Group;
import model.entity.Student;
import model.utility.JsonUtility;
import model.utility.StudentComparator;
import model.utility.TimeUtility;
import model.entity.Location;
import model.utility.LocationComparator;
import model.utility.TopKUtility;

/**
 *
 * @author amanda
 */
@WebServlet(name = "JsonTopKNextPlaces", urlPatterns = {"/json/top-k-next-places"})
public class JsonTopKNextPlaces extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        //creating gson object for output 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonOutput = new JsonObject();

        //retrieve request parameters
        String k = request.getParameter("k");
        String semanticPlace = request.getParameter("origin");
        String date = request.getParameter("date");
        String token = request.getParameter("token");

        List<String> semanticPlacesList = TopKUtility.getSemanticPlaces();

        boolean kValid = true;
        boolean semanticPlaceValid = true;
        boolean dateValid = true;
        boolean tokenValid = true;
        java.util.ArrayList<Timestamp> timeList = new java.util.ArrayList<>();
        java.util.ArrayList<Timestamp> timeListTwo = new java.util.ArrayList<>();
        int rank = 0;
        try {
            rank = JsonUtility.jsonToInt(k);
            if (rank == -1) {
                kValid = false;
            }
        } catch (Exception e) {
            kValid = false; //catch NumberFormatException && null pointer
        }
        try {
            String verification = JWTUtility.verify(token, "depressurization");
        } catch (Exception ex) { //catch JWTException && null pointer
            tokenValid = false;
        }
        try {
            timeList = TimeUtility.getJsonProcessingWindow(date);
            timeListTwo = TimeUtility.getJsonNextProcessingWindow(date);
        } catch (Exception e) { // catch IllegalArgumentException
            dateValid = false;
        }
        if (!(semanticPlacesList.contains(semanticPlace))) {
            semanticPlaceValid = false;
        }
        JsonArray jArray = new JsonArray();
        if (!(kValid && dateValid && tokenValid && semanticPlaceValid)) { // if error
            jsonOutput.addProperty("status", "error");
            if (!dateValid) {
                if (date == null) {
                    jArray.add("missing date");
                } else if (date.trim().equals("")) {
                    jArray.add("blank date");
                } else {
                    jArray.add("invalid date");
                }
            }
            if (!kValid) {
                if (k.trim().equals("")) { //cos can be unspecified but cannot be blank
                    jArray.add("blank k");
                } else {
                    jArray.add("invalid k");
                }
            }
            if (!semanticPlaceValid) {
                if (semanticPlace == null) {
                    jArray.add("missing origin");
                } else if (semanticPlace.trim().equals("")) {
                    jArray.add("blank origin");
                } else {
                    jArray.add("invalid origin");
                }
            }
            if (!tokenValid) {
                if (token == null) {
                    jArray.add("missing token");
                } else if (token.trim().equals("")) {
                    jArray.add("blank token");
                } else {
                    jArray.add("invalid token");
                }
            }
            jsonOutput.add("messages", jArray);
        } else {

            Timestamp startDateTime = timeList.get(0);
            Timestamp endDateTime = timeList.get(1);
            //getting datetime window2|
            Timestamp startDateTimeTwo = timeListTwo.get(0);
            Timestamp endDateTimeTwo = timeListTwo.get(1);

            //running logic for topKNextPlaces
            LocationReportsDAO locationReportsDAO = new LocationReportsDAO(startDateTime, endDateTime, startDateTimeTwo, endDateTimeTwo);
            ArrayList<String> studentsList = locationReportsDAO.peopleInSemanticPlace(semanticPlace);
            HashMap<String, Location> locationMap = locationReportsDAO.topkNextPlaces(studentsList);
            ArrayList<Location> locationList = new ArrayList<>();

            //converts hashmap to ArrayList of location for display
            Iterator iter = locationMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Location l = locationMap.get(key);
                locationList.add(l);
            }

            //sorting the locationlist
            Collections.sort(locationList, new LocationComparator());

            //placing output into json
            jsonOutput.addProperty("status", "success");
            int i = 0;
            int countTotal = 0;
            for (Location l : locationList) {
                countTotal += l.getNumberOfStudents();
            }
            //sorting the locationlist
            Collections.sort(locationList, new LocationComparator());
            jsonOutput.addProperty("total-users", studentsList.size());
            jsonOutput.addProperty("total-next-place-users", countTotal);
            Iterator itera = locationList.iterator();
            int temp = 0;
            while (i < rank && itera.hasNext()) {
                Location l = (Location) itera.next();
                int countStudents = l.getNumberOfStudents();
                //checks to increment rank counter or not. 
                //There is a need to increment rank counter only if the past count is different from the new count
                if (countStudents > 0) {
                    if (temp != countStudents) {
                        temp = countStudents;
                        i++;
                    }
                    JsonObject ranks = new JsonObject();
                    ranks.addProperty("rank", i);
                    ranks.addProperty("semantic-place", l.getSemanticPlace());
                    ranks.addProperty("count", countStudents);
                    jArray.add(ranks);
                } else {
                    i++;
                }

            }

            jsonOutput.add("results", jArray);
        }

        String prettyPrint = gson.toJson(jsonOutput);
        out.println(prettyPrint);

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
