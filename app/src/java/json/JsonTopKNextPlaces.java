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
        int kValue = Integer.parseInt(k);
        String semanticPlace = request.getParameter("origin");
        out.println("place" + semanticPlace);
        String date = request.getParameter("date");
        String token = request.getParameter("token");

        List<String> semanticPlacesList = TopKUtility.getSemanticPlaces();

        boolean kValid = true;
        boolean semanticPlaceValid = true;
        boolean dateValid = true;
        boolean tokenValid = true;
        java.util.ArrayList<Timestamp> timeList = new java.util.ArrayList<>();
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
            String verification = JWTUtility.verify(token, "secret");
        } catch (Exception ex) { //catch JWTException && null pointer
            tokenValid = false;
        }
        try {
            date = date.replace("T", " ");
            timeList = TimeUtility.getProcessingWindow(date);
        } catch (Exception e) { // catch IllegalArgumentException && null pointer
            dateValid = false;
        }

        JsonArray jArray = new JsonArray();
        if (!(kValid && dateValid && tokenValid)) { // if error
            jsonOutput.addProperty("status", "error");

            if (!(semanticPlacesList.contains(semanticPlace))) {
                semanticPlaceValid = false;
                if (!semanticPlaceValid) {
                    if (semanticPlace == null) {
                        jArray.add("missing origin");
                    } else if (semanticPlace.trim().equals("")) {
                        jArray.add("blank origin");
                    } else {
                        jArray.add("invalid origin");
                    }
                }
            }

            if (!kValid) {
                if (k.trim().equals("")) { //cos can be unspecified but cannot be blank
                    jArray.add("blank k");
                } else {
                    jArray.add("invalid k");
                }
            }
            if (!dateValid) {
                if (date == null) {
                    jArray.add("missing date");
                } else if (date.trim().equals("")) {
                    jArray.add("blank date");
                } else {
                    jArray.add("invalid date");
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
            out.println("date " + startDateTime + " " + endDateTime);
            LocationReportsDAO locationReportDAO = new LocationReportsDAO(startDateTime, endDateTime);
            //why
            ArrayList<Location> nextPlaceList = locationReportDAO.topkNextPlaces(kValue, semanticPlace);
            ArrayList<String> totalUsers = locationReportDAO.peopleInSemanticPlace(semanticPlace);
            out.println("users" + totalUsers.size());
            
            out.println("nextplacelist" + nextPlaceList.size());
            
            Collections.sort(nextPlaceList, new LocationComparator());
            jsonOutput.addProperty("status", "success");
            int i = 1;
            int countTotal = 0;
            for (Location l : nextPlaceList) {
                countTotal += l.getNumberOfStudents();

            }

            jsonOutput.addProperty("total-users", totalUsers.size());
            jsonOutput.addProperty("total-next-place-users", countTotal);
            while (i <= rank) {

                for (Location l : nextPlaceList) {
                    int countStudents = l.getNumberOfStudents();
                    JsonObject ranks = new JsonObject();
                    ranks.addProperty("rank", i);
                    ranks.addProperty("semantic-place", l.getSemanticPlace());
                    ranks.addProperty("count", countStudents);
                    jArray.add(ranks);
                }
                i++;

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
