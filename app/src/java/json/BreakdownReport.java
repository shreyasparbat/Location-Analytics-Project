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
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author Joleen Mok
 */
@WebServlet(name = "BreakdownReport", urlPatterns = {"/json/basic-loc-report"})
public class BreakdownReport extends HttpServlet {

    public BreakdownUtility bu = new BreakdownUtility();

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

        try {
            //verify token 
            String token = request.getParameter("token");
            if (token != null && token.length() > 0) {
                String verification = JWTUtility.verify(token, "secret");

                if (verification != null) {
                    //retrieve request parameters (date)
                    String date = request.getParameter("date");

                    //Make dateTime string of correct format
                    String dateTime = date.substring(0, 10) + " " + date.substring(11, date.length());

                    //get processing window
                    ArrayList<Timestamp> processingWindowArrayList = TimeUtility.getProcessingWindow(dateTime);
                    Timestamp startDateTime = processingWindowArrayList.get(0);
                    Timestamp endDateTime = processingWindowArrayList.get(1);

                    //retrieve request parameters (order)
                    String order = request.getParameter("order");
                    String[] orderList = order.split(",");

                    //get HashMap of all students in the SIS building during processing window
                    StudentDAO sDAO = new StudentDAO();
                    HashMap<String, Student> studentMap = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);

                    switch (orderList.length) {
                        case 1:
                            String option = orderList[0];
                            //no errors
                            if (isCorrectOption(option)) {
                                //get breakdown map
                                HashMap<String, Integer> percentageOneList = bu.percentageOneOption(option, studentMap);
                                jsonOutput.addProperty("status", "success");
                                jsonOutput.add("breakdown", printInnerMap(percentageOneList));

                            } else {
                                //invalid options
                                jsonOutput.addProperty("status", "error");
                                JsonArray jsonArray = new JsonArray();
                                jsonArray.add("invalid order");
                                jsonOutput.add("messages", jsonArray);
                            }
                            break;

                        case 2:
                            String option1 = orderList[0];
                            String option2 = orderList[1];

                            //check for correct option 
                            if (isCorrectOption(option1) && isCorrectOption(option2)) {
                                //check for overlap
                                if (option1.equals(option2)) {
                                    //invalid options
                                    jsonOutput.addProperty("status", "error");
                                    JsonArray jsonArray = new JsonArray();
                                    jsonArray.add("invalid order");
                                    jsonOutput.add("messages", jsonArray);
                                } else {
                                    //get breakdown map
                                    HashMap<String, HashMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option2, studentMap);
                                    jsonOutput.addProperty("status", "success");
                                    jsonOutput.add("breakdown", printMiddleMap(percentageTwoList));
                                }

                            } else {
                                //invalid options
                                jsonOutput.addProperty("status", "error");
                                JsonArray jsonArray = new JsonArray();
                                jsonArray.add("invalid order");
                                jsonOutput.add("messages", jsonArray);
                            }
                            break;

                        case 3:
                            String optionA = orderList[0];
                            String optionB = orderList[1];
                            String optionC = orderList[2];

                            //check for correct option 
                            if (isCorrectOption(optionA) && isCorrectOption(optionB) && isCorrectOption(optionC)) {
                                //check for overlap
                                if (optionA.equals(optionB) || optionA.equals(optionC) || optionB.equals(optionC)) {
                                    //invalid options
                                    jsonOutput.addProperty("status", "error");
                                    JsonArray jsonArray = new JsonArray();
                                    jsonArray.add("invalid order");
                                    jsonOutput.add("messages", jsonArray);
                                } else {
                                    //get breakdown map
                                    HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList = bu.percentageAllOptions(optionA, optionB, optionC, studentMap);
                                    jsonOutput.addProperty("status", "success");
                                    jsonOutput.add("breakdown", printOuterMap(percentageAllList));
                                }
                            } else {
                                //invalid options
                                jsonOutput.addProperty("status", "error");
                                JsonArray jsonArray = new JsonArray();
                                jsonArray.add("invalid order");
                                jsonOutput.add("messages", jsonArray);
                            }
                            break;
                    }
                }
            } else {
                // token parameter is missing or is a "" token
                jsonOutput.addProperty("status", "error");
                JsonArray jsonArray = new JsonArray();
                if (token == null) {
                    jsonArray.add("missing token");
                } else if (token.length() == 0) {
                    jsonArray.add("blank token");
                }
                jsonOutput.add("messages", jsonArray);
            }

        } catch (JWTException ex) {
            //invalid token 
            jsonOutput.addProperty("status", "error");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("invalid token");
            jsonOutput.add("messages", jsonArray);
        } catch (IllegalArgumentException e) {
            //invalid date  
            jsonOutput.addProperty("status", "error");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("invalid date");
            jsonOutput.add("messages", jsonArray);
        } finally {
            String prettyPrint = gson.toJson(jsonOutput);
            out.println(prettyPrint);
        }
    }

    public JsonArray printInnerMap(HashMap<String, Integer> innerMap) {
        //jsonArray to be outputed
        JsonArray outputJsonArray = new JsonArray();

        //getting iterator for inner most map keys
        Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
        while (innerMapKeysIter.hasNext()) {
            //get innerKey 
            String innerKey = innerMapKeysIter.next();

            //getting innerMapValue
            int innerMapValue = innerMap.get(innerKey);

            //adding to jsonArray
            JsonObject innerMapJson = new JsonObject();
            innerMapJson.addProperty(innerKey, innerMapValue);
            outputJsonArray.add(innerMapJson);
        }

        return outputJsonArray;
    }

    public JsonArray printMiddleMap(HashMap<String, HashMap<String, Integer>> middleMap) {
        //jsonArray to be outputed
        JsonArray outputJsonArray = new JsonArray();

        //getting inner maps, which can only be accessed within this while loop
        Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();

        while (middleMapKeysIter.hasNext()) {
            //store the key into output list
            String middlekey = middleMapKeysIter.next();

            //get inner map
            HashMap<String, Integer> innerMap = middleMap.get(middlekey);

            //adding inner map breakdown to jsonArray
            JsonObject innerMapJson = new JsonObject();
            innerMapJson.add("breakdown", printInnerMap(innerMap));
            outputJsonArray.add(innerMapJson);
        }

        //returning
        return outputJsonArray;
    }

    public JsonArray printOuterMap(HashMap<String, HashMap<String, HashMap<String, Integer>>> outerMap) {
        //jsonArray to be outputed
        JsonArray outputJsonArray = new JsonArray();

        //getting middle maps, which can only be accessed within this while loop
        Iterator<String> outerMapKeysIter = outerMap.keySet().iterator();

        while (outerMapKeysIter.hasNext()) {
            //store the key into output list
            String middlekey = outerMapKeysIter.next();

            //get middle map
            HashMap<String, HashMap<String, Integer>> middleMap = outerMap.get(middlekey);

            //adding middle map breakdown to jsonArray
            JsonObject innerMapJson = new JsonObject();
            innerMapJson.add("breakdown", printMiddleMap(middleMap));
            outputJsonArray.add(innerMapJson);
        }

        //returning
        return outputJsonArray;
    }

    public boolean isCorrectOption(String option) {
        return option.equals("year") || option.equals("gender") || option.equals("school");
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
