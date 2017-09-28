/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.DBConnection;
import com.opencsv.CSVReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javazoom.upload.*;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javazoom.upload.MultipartFormDataRequest;

/**
 *
 * @author Joel Tay
 */
@WebServlet(name = "BootstrapUpload", urlPatterns = {"/uploadFile"})
public class BootstrapUpload extends HttpServlet {

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
        if (MultipartFormDataRequest.isMultipartFormData(request)) {
            PrintWriter out = response.getWriter();
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
                            unzipThis(zin);
                        }
                    }
                }
            } catch (UploadException ex) {
                Logger.getLogger(BootstrapUpload.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.getWriter().write("Upload Successful");
    }

    protected void unzipThis(ZipInputStream zin) throws IOException, ClassNotFoundException {

        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) { // for every item in the zip file
            //default constructor is CSVReader(new Reader (new InputStreamReader(InputStream())) and Reader is a superclass of InputStreamReader
            CSVReader csvr = new CSVReader(new InputStreamReader(zin));
            out.println("");
            out.println("");
            // name of the file in the zip folder
            String fileName = new File(entry.getName()).getName();
            if (fileName.endsWith(".csv")) {
                String[] header = csvr.readNext();
                //clear header buffer
                //validate header
                for (String s : header) {
                    out.print(s + "  ");
                }
                out.println();
                String[] arr;

                while ((arr = csvr.readNext()) != null) {
                    for (String s : arr) {
                        out.print(s);
                        out.print("             ");

                    }
                    out.println();
                    try {
                        switch (fileName) {
                            case "location.csv":
                                DBConnection.addLoca(arr);
                                break;
                            case "demographics.csv":
                                DBConnection.addDemo(arr);
                                break;
                            case "location-lookup.csv":
                                DBConnection.addLL(arr);
                                break;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(BootstrapUpload.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
