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
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;
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
 * @author shrey
 */
@WebServlet(name = "JsonHeatMap", urlPatterns = {"/json/heatmap"})
public class JsonHeatMap extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //creating gson object for output 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonOutput = new JsonObject();

        //json array for indicating errors
        JsonArray jsonErrorArray = new JsonArray();

        try {
            //verify token 
            String token = request.getParameter("token");
            if (token != null && token.length() > 0) {
                String verification = JWTUtility.verify(token, "depressurization");

                if (verification != null) {
                    //retrieve request parameters (date)
                    String date = request.getParameter("date");

                    //expanding reach of Timestamp objects
                    Timestamp startDateTime = null;
                    Timestamp endDateTime = null;

                    //check for missing or blank date
                    if (date == null) {
                        jsonErrorArray.add("missing date");
                    } else if (date.equals("")) {
                        jsonErrorArray.add("blank date");
                    } else {
                        //check for invalid date
                        try {
                            //get processing window
                            ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getJsonProcessingWindow(date);
                            startDateTime = processingWindowArrayList.get(0);
                            endDateTime = processingWindowArrayList.get(1);

                        } catch (Exception e) {
                            //invalid date  
                            jsonErrorArray.add("invalid date");
                        }
                    }

                    //expanding the reach of level string
                    String level = "";

                    //retrieve request parameters
                    String floorString = request.getParameter("floor");

                    //check for missing or blank floor
                    if (floorString == null) {
                        jsonErrorArray.add("missing floor");
                    } else if (floorString.equals("")) {
                        jsonErrorArray.add("blank floor");
                    } else {
                        //not missing or blank
                        int floor = Integer.parseInt(request.getParameter("floor"));

                        //retrive floor name (ie level) from floor number
                        switch (floor) {
                            case 0:
                                level += "B1";
                                break;

                            case 1:
                                level += "L1";
                                break;

                            case 2:
                                level += "L2";
                                break;

                            case 3:
                                level += "L3";
                                break;

                            case 4:
                                level += "L4";
                                break;

                            case 5:
                                level += "L5";
                                break;

                            default:
                                //incorrect floor
                                jsonErrorArray.add("invalid floor");
                                break;
                        }
                    }

                    //if no errors
                    if (jsonErrorArray.size() == 0) {

                        //get semanticPlaceHeat TreeMap and its iterator
                        HeatMapDAO heatMapDAO = new HeatMapDAO(startDateTime, endDateTime, level);
                        TreeMap<String, int[]> semanticPlaceHeat = heatMapDAO.getSemanticPlaceHeatFromSpecificFloor();
                        Iterator<String> semanticPlaceHeatKeysIterator = semanticPlaceHeat.keySet().iterator();

                        //create Json Array to be used for printing heat map
                        JsonArray heatMapJsonArray = new JsonArray();

                        while (semanticPlaceHeatKeysIterator.hasNext()) {
                            //making json object to be added to array
                            JsonObject jsonObject = new JsonObject();

                            //get semanticPlace
                            String semanticPlace = semanticPlaceHeatKeysIterator.next();

                            //get number of mac addresses and correspinding heat value
                            int[] informationArray = semanticPlaceHeat.get(semanticPlace);
                            int noOfMacAdd = informationArray[0];
                            int heatValue = getHeatValue(noOfMacAdd);

                            //adding required "properties" to jsonObject
                            jsonObject.addProperty("semantic-place", semanticPlace);
                            jsonObject.addProperty("num-people", noOfMacAdd);
                            jsonObject.addProperty("crowd-density", heatValue);

                            //adding jsonObject to heatmap json array
                            heatMapJsonArray.add(jsonObject);
                        }

                        //Store array into out put json
                        jsonOutput.addProperty("status", "success");
                        jsonOutput.add("heatmap", heatMapJsonArray);

                    } else {
                        //add error array to output json object
                        jsonOutput.addProperty("status", "error");
                        jsonOutput.add("messages", jsonErrorArray);
                    }
                }
            } else {
                // token parameter is missing or is a "" token
                jsonOutput.addProperty("status", "error");
                if (token == null) {
                    jsonErrorArray.add("missing token");
                } else if (token.length() == 0) {
                    jsonErrorArray.add("blank token");
                }
                jsonOutput.add("messages", jsonErrorArray);
            }

        } catch (JWTException ex) {
            //invalid token 
            jsonOutput.addProperty("status", "error");
            jsonErrorArray.add("invalid token");
            jsonOutput.add("messages", jsonErrorArray);
        } catch (NumberFormatException e) {
            //invalid floor 
            jsonOutput.addProperty("status", "error");
            jsonErrorArray.add("invalid floor");
            jsonOutput.add("messages", jsonErrorArray);
        } finally {
            String prettyPrint = gson.toJson(jsonOutput);
            out.println(prettyPrint);
            out.close();
        }
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
