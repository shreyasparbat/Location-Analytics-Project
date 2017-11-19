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
import java.util.Iterator;
import java.util.TreeMap;
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
public class JsonBreakdownReport extends HttpServlet {

    /**
     * Creates a new breakdown utility object for location reports usage
     */
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
        String token = request.getParameter("token");
        String order = request.getParameter("order");
        String date = request.getParameter("date");
        java.util.ArrayList<Timestamp> processingWindowArrayList = new java.util.ArrayList<>();
        boolean orderValid = true;
        boolean dateValid = true;
        boolean tokenValid = true;

        try {
            String verification = JWTUtility.verify(token, "depressurization");
        } catch (Exception ex) { //catch JWTException && null pointer
            tokenValid = false;
        }
        try {
            processingWindowArrayList = TimeUtility.getJsonProcessingWindow(date);
        } catch (Exception e) { // catch IllegalArgumentException
            dateValid = false;
        }
        orderValid = isValidOption(order);

        JsonArray jArray = new JsonArray();
        if (!(orderValid && dateValid && tokenValid)) {
            //invalid 
            jsonOutput.addProperty("status", "error");
            if (!tokenValid) {
                if (token == null) {
                    jArray.add("missing token");
                } else if (token.trim().equals("")) {
                    jArray.add("blank token");
                } else {
                    jArray.add("invalid token");
                }
            } else {
                ArrayList<String> j2Array = new ArrayList<>();
                if (!dateValid) {
                    if (date == null) {
                        j2Array.add("missing date");
                    } else if (date.trim().equals("")) {
                        j2Array.add("blank date");
                    } else {
                        j2Array.add("invalid date");
                    }
                }
                if (!orderValid) {
                    if (order == null) {
                        j2Array.add("missing order");
                    } else if (order.trim().equals("")) {
                        j2Array.add("blank order");
                    } else {
                        j2Array.add("invalid order");
                    }
                }
                if(!j2Array.isEmpty()){
                    Collections.sort(j2Array);
                    for(String errorMsg : j2Array){
                        jArray.add(errorMsg);
                    }
                }
            }

            jsonOutput.add("messages", jArray);
        } else {
            Timestamp startDateTime = processingWindowArrayList.get(0);
            Timestamp endDateTime = processingWindowArrayList.get(1);

            String[] orderList = order.split(",");

            //get HashMap of all students in the SIS building during processing window
            StudentDAO sDAO = new StudentDAO();
            TreeMap<String, Student> stMap = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);
            TreeMap<String, Student> studentMap = new TreeMap<String, Student>(stMap);
            String option1;
            String option2;
            String option3;
            switch (orderList.length) {
                case 1:
                    String option = orderList[0];
                    //no errors
                    if (isCorrectOption(option)) {
                        //get breakdown map
                        TreeMap<String, Integer> percentageOneList = bu.percentageOneOption(option, studentMap);
                        jsonOutput.addProperty("status", "success");
                        jsonOutput.add("breakdown", printInnerMap(percentageOneList, option));

                    }
                    break;

                case 2:
                    option1 = orderList[0];
                    option2 = orderList[1];

                    //check for correct option 
                    if (isCorrectOption(option1) && isCorrectOption(option2)) {

                        //get breakdown map
                        TreeMap<String, TreeMap<String, Integer>> percentageTwoList = bu.percentageTwoOptions(option1, option2, studentMap);
                        jsonOutput.addProperty("status", "success");
                        jsonOutput.add("breakdown", printMiddleMap(percentageTwoList, option2, option1));

                    }
                    break;

                case 3:
                    option1 = orderList[0];
                    option2 = orderList[1];
                    option3 = orderList[2];

                    //check for correct option 
                    if (isCorrectOption(option1) && isCorrectOption(option2) && isCorrectOption(option3)) {
                        //get breakdown map
                        TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList = bu.percentageAllOptions(option1, option2, option3, studentMap);
                        jsonOutput.addProperty("status", "success");
                        jsonOutput.add("breakdown", printOuterMap(percentageAllList, option1, option2, option3));

                    }
                    break;

            }
        }

        String prettyPrint = gson.toJson(jsonOutput);
        out.println(prettyPrint);

    }

    /**
     *
     * @param innerMap stores the TreeMap of a String and Integer variable
     * @param option1 string option
     * @return
     */
    public JsonArray printInnerMap(TreeMap<String, Integer> innerMap, String option1) {
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
            if (option1.equals("year")) {
                innerMapJson.addProperty(option1, Integer.parseInt(innerKey));
            } else {
                innerMapJson.addProperty(option1, innerKey);
            }
            innerMapJson.addProperty("count", innerMapValue);
            outputJsonArray.add(innerMapJson);
        }

        return outputJsonArray;
    }

    /**
     *
     * @param middleMap stores the TreeMap of the variable and its count
     * together with the inner values
     * @param option2 first option
     * @param option1 last option
     * @return
     */
    public JsonArray printMiddleMap(TreeMap<String, TreeMap<String, Integer>> middleMap, String option2, String option1) {
        //jsonArray to be outputed
        JsonArray outputJsonArray = new JsonArray();

        //getting inner maps, which can only be accessed within this while loop
        Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();

        while (middleMapKeysIter.hasNext()) {
            //store the key into output list
            String middlekey = middleMapKeysIter.next();

            //getting name and count from key
            String[] middleKeyList = middlekey.split(":");
            String name = middleKeyList[0].trim();
            String count = middleKeyList[1].trim();
            //get inner map
            TreeMap<String, Integer> innerMap = middleMap.get(middlekey);

            //adding inner map breakdown to jsonArray
            JsonObject innerMapJson = new JsonObject();
            if (option1.equals("year")) {
                innerMapJson.addProperty(option2, Integer.parseInt(name));
            } else {
                innerMapJson.addProperty(option2, name);
            }
            innerMapJson.addProperty("count", Integer.parseInt(count));
            innerMapJson.add("breakdown", printInnerMap(innerMap, option1));

            outputJsonArray.add(innerMapJson);
        }

        //returning
        return outputJsonArray;
    }

    /**
     *
     * @param outerMap stores the TreeMap of the variable and its count together
     * with the inner values
     * @param option3 most outer option
     * @param option2 middle option
     * @param option1 last option
     * @return
     */
    public JsonArray printOuterMap(TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> outerMap, String option3, String option2, String option1) {
        //jsonArray to be outputed
        JsonArray outputJsonArray = new JsonArray();

        //getting middle maps, which can only be accessed within this while loop
        Iterator<String> outerMapKeysIter = outerMap.keySet().iterator();

        while (outerMapKeysIter.hasNext()) {
            //store the key into output list
            String outerkey = outerMapKeysIter.next();

            //getting name and count from key
            String[] middleKeyList = outerkey.split(":");
            String name = middleKeyList[0].trim();
            String count = middleKeyList[1].trim();

            //get middle map
            TreeMap<String, TreeMap<String, Integer>> middleMap = outerMap.get(outerkey);

            //adding middle map breakdown to jsonArray
            JsonObject innerMapJson = new JsonObject();
            if (option3.equals("year")) {
                innerMapJson.addProperty(option3, Integer.parseInt(name));
            } else {
                innerMapJson.addProperty(option3, name); //most outer
            }

            innerMapJson.addProperty("count", Integer.parseInt(count));
            innerMapJson.add("breakdown", printMiddleMap(middleMap, option2, option1));
            outputJsonArray.add(innerMapJson);
        }
        //returning
        return outputJsonArray;
    }

    /**
     * Returns a "true" boolean value if the option input by user is correct
     *
     * @param option takes in a String of the option input by user
     * @return boolean "true" is returned if input is correct, "false" if it is
     * wrong
     */
    public boolean isCorrectOption(String option) {
        return option.equals("year") || option.equals("gender") || option.equals("school");
    }

    /**
     * Returns a "true" boolean value if the option input by user is Valid
     *
     * @param option takes in a String of the option input by user
     * @return boolean "true" is returned if input is valid, "false" if it is
     * not valid
     */
    public boolean isValidOption(String option) {
        if (option == null || option.endsWith(",")) {
            return false;
        }
        ArrayList<String> options = new ArrayList<>();
        options.add("year");
        options.add("gender");
        options.add("school");

        String[] array = option.split(",");
        for (String words : array) { //loops the entire list
            if (!options.contains(words)) {  //if options doesnt contain the word
                return false;
            } else {
                options.remove(words); //remove from list
            }
        }

        return true;
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
