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
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.LocationReportsDAO;
import model.utility.JsonUtility;
import model.utility.TimeUtility;

/**
 *
 * @author amanda
 */
@WebServlet(name = "JsonTopKPopularPlaces", urlPatterns = {"/json/top-k-popular-places"})
public class JsonTopKPopularPlaces extends HttpServlet {

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
        String date = request.getParameter("date");
        String token = request.getParameter("token");
        boolean kValid = true;
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
            String verification = JWTUtility.verify(token, "depressurization");
        } catch (Exception ex) { //catch JWTException && null pointer
            tokenValid = false;
        }
        try {
            timeList = TimeUtility.getJsonProcessingWindow(date);
        } catch (Exception e) { // catch IllegalArgumentException && ull pointer
            dateValid = false;
        }

        JsonArray jArray = new JsonArray();
        if (!(kValid && dateValid && tokenValid)) { // if error
            jsonOutput.addProperty("status", "error");

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
            LocationReportsDAO locationReportDAO = new LocationReportsDAO(startDateTime, endDateTime);
            // key is semantic place
            // value is the count
            LinkedHashMap<String, Integer> popularPlacesList = locationReportDAO.topkPopularPlaces();
            jsonOutput.addProperty("status", "success");
            int i = 0;
            int temp = 0;
            Iterator<String> iter = popularPlacesList.keySet().iterator();
            while (i < rank && iter.hasNext()) {
                String key = iter.next();
                int count = popularPlacesList.get(key);
                if(temp != count){
                    temp = count;
                    i++;
                }
                JsonObject ranks = new JsonObject();
                ranks.addProperty("rank", i);
                ranks.addProperty("semanticplace",key);
                ranks.addProperty("count", count);
                jArray.add(ranks);
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
