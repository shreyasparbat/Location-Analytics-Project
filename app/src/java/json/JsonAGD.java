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
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.AgdDAO;
import model.dao.StudentDAO;
import model.entity.Group;
import model.entity.Student;
import model.utility.AGDComparator;
import model.utility.TimeUtility;

/**
 *
 * @author Joel Tay
 */
@WebServlet(name = "JsonAGD", urlPatterns = {"/json/group_detect"})
public class JsonAGD extends HttpServlet {

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
        String token = request.getParameter("token");
        String dateTime = request.getParameter("date");
        ArrayList<Timestamp> processingWindowArrayList = null;
        JsonArray jArray = new JsonArray();
        boolean dateValid = true;
        boolean tokenValid = true;
        try {
            String verification = JWTUtility.verify(token, "depressurization");
        } catch (Exception ex) { //catch JWTException && null pointer
            tokenValid = false;
        }
        try {
            processingWindowArrayList = TimeUtility.getJsonProcessingWindow(dateTime);
        } catch (IllegalArgumentException e) { // catch IllegalArgumentException
            dateValid = false;
        }
        if (!(dateValid && tokenValid)) { // if error
            jsonOutput.addProperty("status", "error");

            if (!dateValid) {
                if (dateTime == null) {
                    jArray.add("missing date");
                } else if (dateTime.trim().equals("")) {
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
            Timestamp startDateTime = processingWindowArrayList.get(0);
            Timestamp endDateTime = processingWindowArrayList.get(1);
            AgdDAO agdDao = new AgdDAO();
            StudentDAO sDAO = new StudentDAO();
            TreeMap<String, Student> studentList = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);
            ArrayList<Group> list = agdDao.getStudentGroups(startDateTime, endDateTime, sDAO, studentList);

            //sort groups
            Collections.sort(list, new AGDComparator());
            
            jsonOutput.addProperty("status", "success");
            jsonOutput.addProperty("total-users", studentList.size());
            jsonOutput.addProperty("total-groups", list.size());
            for(Group g : list){
                JsonObject group = new JsonObject();
                group.addProperty("size", g.getGroup().size());
                Double time = g.getTotalDuration();
                group.addProperty("total-time-spent", time.intValue());
                JsonArray members = new JsonArray();
                for(Student s : g.getGroup()){
                    JsonObject studentInfo = new JsonObject();
                    studentInfo.addProperty("email",s.getEmail());
                    studentInfo.addProperty("mac-address",s.getMacAddress());
                    members.add(studentInfo);
                }
                group.add("members", members);
                JsonArray location = new JsonArray();
                for(int id: g.getRecord().keySet()){
                    JsonObject locationInfo = new JsonObject();
                    locationInfo.addProperty("location", id);
                    Double locationTime = g.getRecord().get(id).getDuration();
                    locationInfo.addProperty("time-spent", locationTime.intValue());
                    location.add(locationInfo);
                }
                group.add("locations", location);
                jArray.add(group);
            }
            jsonOutput.add("groups", jArray);
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

}
