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
import java.time.format.DateTimeParseException;
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
        String dateTime = request.getParameter("datetime");
        

        //Obtaining SQL Timestamp objects for start and end time of processing window
        Timestamp startDateTime = null;
        Timestamp endDateTime = null;

        //check for date-time format errors
        try {
            ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
            startDateTime = processingWindowArrayList.get(0);
            endDateTime = processingWindowArrayList.get(1);
        } catch (DateTimeParseException e) {
            request.setAttribute("errMessage", "Invalid date-time format");
            request.getRequestDispatcher("/HeatMaps.jsp").forward(request, response);
        }

        //get semanticPlaceHeat HashMap and its iterator
        HeatMapDAO heatMapDAO = new HeatMapDAO(startDateTime, endDateTime, level);
        HashMap<String, Integer> semanticPlaceHeat = heatMapDAO.getSemanticPlaceHeatFromSpecificFloor();
        Iterator<String> semanticPlaceHeatKeysIterator = semanticPlaceHeat.keySet().iterator();

        //create Json Array to be used for printing heat map
        JsonArray heatMapJsonArray = new JsonArray();

        while (semanticPlaceHeatKeysIterator.hasNext()) {
            //making json object to be added to array
            JsonObject jsonObject = new JsonObject();

            //get semanticPlace
            String semanticPlace = semanticPlaceHeatKeysIterator.next();

            //get number of mac addresses and correspinding heat value
            int noOfMacAdd = semanticPlaceHeat.get(semanticPlace);
            int heatValue = getHeatValue(noOfMacAdd);
            
            //semanticplaces corresponding to SRs have "." in them, which lead to problems in the svg file
            //because the "id" attribute of a path/shape cannot have a "."
            //thus here we replace the dot with "n"
            semanticPlace = semanticPlace.replace('.', 'n');

            //adding required "properties" to jsonObject
            jsonObject.addProperty("semantic-place", semanticPlace);
            jsonObject.addProperty("num-macAdd", noOfMacAdd);
            jsonObject.addProperty("heat-value", heatValue);

            //adding jsonObject to heatmap json array
            heatMapJsonArray.add(jsonObject);
        }

        //Store array in new "result" jsonObj
        JsonObject result = new JsonObject();
        result.addProperty("status", "success");
        result.add("heatMapJsonArray", heatMapJsonArray);

        //send result back to view page
        request.setAttribute("level", level);
        request.setAttribute("result", result);
        request.getRequestDispatcher("/HeatMaps.jsp").forward(request, response);

       
       out.close();
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
