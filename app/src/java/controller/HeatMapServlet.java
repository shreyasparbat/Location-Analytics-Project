/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.HeatMapDAO;
import model.utility.TimeUtility;

/**
 *
 * @author Joleen Mok
 */
@WebServlet(name = "HeatMapServlet", urlPatterns = {"/HeatMapServlet"})
public class HeatMapServlet extends HttpServlet {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

        //retrieve request parameters
        String level = request.getParameter("level");
        String date = request.getParameter("date");
        String time = request.getParameter("time");

        //Make dateTime string of correct format
        String dateTime = date + " " + time + ":00";

        //Obtaining SQL Timestamp objects for start and end time of processing window
        Timestamp startDateTime = null;
        Timestamp endDateTime = null;

        //check for date-time format errors
        try {
            ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
            startDateTime = processingWindowArrayList.get(0);
            endDateTime = processingWindowArrayList.get(1);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errMessage", "Invalid date-time format");
            request.getRequestDispatcher("/HeatMaps.jsp").forward(request, response);
        }

        //get semanticPlaceHeat HashMap and its iterator
        HeatMapDAO heatMapDAO = new HeatMapDAO(startDateTime, endDateTime);
        HashMap<String, Integer> semanticPlaceHeat = heatMapDAO.getAllSemanticPlaceHeat();
        Iterator<String> semanticPlaceHeatKeysIterator = semanticPlaceHeat.keySet().iterator();

        //store only the required level's semantic places in one ArrayList 
        //and the corresponding heatValue in another ArrayList 
        ArrayList<String> semanticPlaceFromSpecifiedFloor = new ArrayList<>();
        ArrayList<Integer> correspondingHeatValue = new ArrayList<>();

        //NOTE: same index means they correspond with each other
        while (semanticPlaceHeatKeysIterator.hasNext()) {
            String semanticPlace = semanticPlaceHeatKeysIterator.next();

            //check if level is same
            if (semanticPlace.contains(level)) {
                //add to arraylist
                semanticPlaceFromSpecifiedFloor.add(semanticPlace);

                //get heat value and add to corresponding arraylist
                int heatValue = getHeatValue(semanticPlaceHeat.get(semanticPlace));
                correspondingHeatValue.add(heatValue);
            }
        }

        //create Json Array to be used for printing heat map
        JsonArray heatMapJsonArray = new JsonArray();

        //read the above created lists and store values in JsonArray
        for (int i = 0; i < semanticPlaceFromSpecifiedFloor.size(); i++) {
            //making json object to be added to array
            JsonObject jsonObject = new JsonObject();

            //adding required "properties" to jsonObject
            jsonObject.addProperty("semantic-place", semanticPlaceFromSpecifiedFloor.get(i));
            jsonObject.addProperty("heat-value", correspondingHeatValue.get(i));

            //adding jsonObject to heatmap json array
            heatMapJsonArray.add(jsonObject);
        }

//        //Store array in new "result" jsonObj
//        JsonObject result = new JsonObject();
//        result.addProperty("status", "success");
//        result.add("heatMapJsonArray", heatMapJsonArray);

        //send reply
        request.setAttribute("level", level);
        request.setAttribute("heatMapJsonArray", heatMapJsonArray);
        request.getRequestDispatcher("/HeatMaps.jsp").forward(request, response);

//        //send reply and close PrintWriter
//        out.println(gson.toJson(heatMapJsonArray));
//        out.close();
    }

    public int getHeatValue(int numOfMacAdd) {
        if (numOfMacAdd == 0) {
            return 0;
        } else if (numOfMacAdd >= 1 && numOfMacAdd <= 2) {
            return 1;
        } else if (numOfMacAdd >= 3 && numOfMacAdd <= 5) {
            return 2;
        } else if (numOfMacAdd >= 6 && numOfMacAdd <= 10) {
            return 3;
        } else if (numOfMacAdd >= 11 && numOfMacAdd <= 20) {
            return 4;
        } else if (numOfMacAdd >= 21 && numOfMacAdd <= 30) {
            return 5;
        } else {
            return 6;
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
