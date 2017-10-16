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
import controller.BootstrapServlet;
import is203.JWTException;
import is203.JWTUtility;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
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
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;
import model.dao.ValidatorDAO;
import model.utility.DemographicsValidator;
import model.utility.LocationLookupValidator;
import model.utility.LocationValidator;

/**
 *
 * @author amanda
 */
@WebServlet(name = "JsonBootstrap", urlPatterns = {"/json/bootstrap"})
public class JsonBootstrap extends HttpServlet {

    private final HashMap<String, List<String[]>> map = new HashMap<>();

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonOutput = new JsonObject();

        try {

            String token = request.getParameter("token");
            if (token != null && token.length() > 0) {
                String verification = JWTUtility.verify(token, "secret");
                // throws JWT Utility if verify fails
                if (verification != null) {
                    if (MultipartFormDataRequest.isMultipartFormData(request)) {
                        try {
                            // Uses MultipartFormDataRequest to parse the HTTP request.
                            MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
                            String todo = null;
                            if (mrequest != null) {
                                todo = mrequest.getParameter("todo");
                            }
                            if ((todo != null) && (todo.equalsIgnoreCase("upload"))) {
                                // retrieving hashtable
                                Hashtable files = mrequest.getFiles();
                                if ((files != null) && (!files.isEmpty())) {
                                    UploadFile file = (UploadFile) files.get("uploadfile");
                                    if (file != null) {
                                        out.write("<li>Form field : uploadfile" + "<BR> Uploaded file : " + file.getFileName() + " (" + file.getFileSize() + " bytes)" + "<BR> Content Type : " + file.getContentType());
                                        out.println("");
                                        ZipInputStream zin = new ZipInputStream(file.getInpuStream());
                                        unzipThis(request, response, zin);
                                    }
                                }
                            }
                        } catch (UploadException ex) {
                            Logger.getLogger(BootstrapServlet.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException e) {
                            out.println(e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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
        } catch (JWTException ex) {
            jsonOutput.addProperty("status", "error");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("invalid token");
            jsonOutput.add("messages", jsonArray);

        } finally {
            String prettyPrint = gson.toJson(jsonOutput);
            out.println(prettyPrint);
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
    protected void unzipThis(HttpServletRequest request, HttpServletResponse response, ZipInputStream zin) throws IOException, ClassNotFoundException, SQLException {
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
        validDAO.validating();
        HashMap<Integer, List<String>> locationErrors = LocationValidator.locationErrors;
        session.setAttribute("location_errors", locationErrors);
        HashMap<Integer, List<String>> locationLookUpErrors = LocationLookupValidator.llErrors;
        session.setAttribute("ll_errors", locationLookUpErrors);
        HashMap<Integer, List<String>> demoErrors = DemographicsValidator.demographErrors;
        session.setAttribute("demographics_errors", demoErrors);
        response.sendRedirect("Admin.jsp");
    }

}
