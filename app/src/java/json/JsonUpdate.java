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
import com.opencsv.CSVReader;
import exception.ValidatorException;
import is203.JWTException;
import is203.JWTUtility;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadFile;
import model.dao.ValidatorDAO;
import model.utility.DemographicsValidator;
import model.utility.LocationLookupValidator;
import model.utility.LocationValidator;

/**
 *
 * @author Joel Tay
 */
@WebServlet(name = "JsonUpdate", urlPatterns = {"/json/update"})
public class JsonUpdate extends HttpServlet {

    private final HashMap<String, List<String[]>> map = new HashMap<>();

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonOutput = new JsonObject();
        // Check for file upload request
        map.clear();
        if (MultipartFormDataRequest.isMultipartFormData(request)) {
            try {
                // Uses MultipartFormDataRequest to parse the HTTP request.
                MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
                if (mrequest != null) {
                    // retrieving hashtable
                    String token = mrequest.getParameter("token");
                    if (token != null && token.length() > 0) {
                        String verification = JWTUtility.verify(token, "depressurization");

                        Hashtable files = mrequest.getFiles();
                        if ((files != null) && (!files.isEmpty())) {
                            UploadFile file = (UploadFile) files.get("bootstrap-file");

                            if (file != null && file.getFileName().endsWith(".zip")) {
                                //unzips file
                                ZipInputStream zin = new ZipInputStream(file.getInpuStream());
                                unzipThis(request, response, zin, jsonOutput);
                            }

                        } else {
                            jsonOutput.addProperty("status", "error");
                            JsonArray jsonArray = new JsonArray();
                            jsonArray.add("missing file input");
                            jsonOutput.add("messages", jsonArray);
                        }

                    } else { // token parameter is missing or is a "" token
                        jsonOutput.addProperty("status", "error");
                        JsonArray jsonArray = new JsonArray();
                        if (token == null) {
                            jsonArray.add("missing token");
                        } else if (token.length() == 0) {
                            jsonArray.add("blank token");
                        }
                        jsonOutput.add("messages", jsonArray);

                    }

                }
            } catch (JWTException ex) {
                jsonOutput.addProperty("status", "error");
                JsonArray jsonArray = new JsonArray();
                jsonArray.add("invalid token");
                jsonOutput.add("messages", jsonArray);

            } catch (Exception e) {
                jsonOutput.addProperty("status", "error");
                JsonArray jsonArray = new JsonArray();
                jsonArray.add("invalid file");
                jsonOutput.add("messages", jsonArray);
            } finally {
                String prettyPrint = gson.toJson(jsonOutput);
                out.println(prettyPrint);
            }
        }

    }

    /**
     *
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param zin zip input stream object of the contents that needs to be read
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected void unzipThis(HttpServletRequest request, HttpServletResponse response, ZipInputStream zin, JsonObject jsonOutput) throws IOException, ClassNotFoundException, SQLException {
        HttpSession session = request.getSession();
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) { // for every item in the zip file
            //default constructor is CSVReader(new Reader (new InputStreamReader(InputStream())) and Reader is a superclass of InputStreamReader
            CSVReader csvr = new CSVReader(new InputStreamReader(zin));
            out.println("");
            out.println("");
            // name of the file in the zip folder
            String fileName = new File(entry.getName()).getName();
            if (fileName.endsWith(".csv")) {
                out.println();
                List<String[]> allList = csvr.readAll();
                map.put(fileName, allList);
            }
        }
        ValidatorDAO validDAO = new ValidatorDAO(map);
        try {
            validDAO.validating();

            HashMap<Integer, List<String>> locationErrors = LocationValidator.locationErrors;

            HashMap<Integer, List<String>> demoErrors = DemographicsValidator.demographErrors;

            //Inserting num rows successfully added into the request attribute
            // taken from static attribute from each validator class
            int demoInsert = DemographicsValidator.numDemoRowsValidated;
            int locaInsert = LocationValidator.numDLocaRowsValidated;

            //Json output for bootstrap
            if (locationErrors.isEmpty() && demoErrors.isEmpty()) {
                //if there is no errors = success
                jsonOutput.addProperty("status", "success");
                JsonArray jsonArray = new JsonArray();
                JsonObject demo = new JsonObject();
                demo.addProperty("demographics.csv", demoInsert);
                JsonObject location = new JsonObject();
                location.addProperty("location.csv", locaInsert);
                jsonArray.add(demo);
                jsonArray.add(location);
                jsonOutput.add("num-record-loaded", jsonArray);
            } else {
                jsonOutput.addProperty("status", "error");

                //first array - input number of successful rows entered
                JsonArray jsonArray = new JsonArray();
                JsonObject demo = new JsonObject();
                demo.addProperty("demographics.csv", demoInsert);
                JsonObject location = new JsonObject();
                location.addProperty("location.csv", locaInsert);
                jsonArray.add(demo);
                jsonArray.add(location);
                jsonOutput.add("num-record-loaded", jsonArray);

                //second array - output errors based on the file
                JsonArray jsonArrayError = new JsonArray();
                if (!demoErrors.isEmpty()) {

                    List<Integer> listOfErrorLines = new ArrayList<Integer>(demoErrors.size());
                    listOfErrorLines.addAll(demoErrors.keySet());
                    Collections.sort(listOfErrorLines);

                    Iterator<Integer> iter = listOfErrorLines.iterator();
                    while (iter.hasNext()) {
                        JsonObject demoError = new JsonObject();
                        demoError.addProperty("file", "demographics.csv");
                        int line = iter.next();
                        demoError = listErrors(demoError, demoErrors, line);
                        jsonArrayError.add(demoError);
                    }

                }
                if (!locationErrors.isEmpty()) {

                    List<Integer> listOfErrorLines = new ArrayList<Integer>(locationErrors.size());
                    listOfErrorLines.addAll(locationErrors.keySet());
                    Collections.sort(listOfErrorLines);
                    Iterator<Integer> iter = listOfErrorLines.iterator();
                    while (iter.hasNext()) {
                        int line = iter.next();
                        JsonObject locaError = new JsonObject();
                        locaError.addProperty("file", "location.csv");
                        locaError = listErrors(locaError, locationErrors, line);
                        jsonArrayError.add(locaError);
                    }

                }

                jsonOutput.add("error", jsonArrayError);
            }
        } catch (ValidatorException ex) {
            jsonOutput.addProperty("status", "error");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(ex.getMessage());
            jsonOutput.add("messages", jsonArray);
        }
    }

    /**
     *
     * @param obj JsonObject Object
     * @param errorList Hashmap of integer and list of string Object
     * @param line int of line representation
     * @return Json object
     */
    protected JsonObject listErrors(JsonObject obj, HashMap<Integer, List<String>> errorList, int line) {

        obj.addProperty("line", line);
        List<String> lineErrors = errorList.get(line);
        JsonArray lineArray = new JsonArray();
        for (String error : lineErrors) {
            lineArray.add(error);
        }
        obj.add("message", lineArray);

        return obj;

    }

}
