/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.dao.LocationReportsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author shrey
 */
@WebServlet(name = "BasicLocationReportsServlet", urlPatterns = {"/basic-location-reports"})
public class BasicLocationReportsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * Calls the appropriate function for each selection.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        //retrieve request parameters
        String function = request.getParameter("function");
        int k = Integer.parseInt(request.getParameter("k"));
        int hours = Integer.parseInt(request.getParameter("hours"));
        int minutes = Integer.parseInt(request.getParameter("minutes"));
        
        //SQL Time objs for start and end time of processing window
        long milliSeconds = hours * 3600000 + minutes * 60000;
        Time startTime = new Time(milliSeconds - 900000);
        Time endTime = new Time(milliSeconds);
        
        switch (function) {
            
            //Breakdown by year and gender
            case "1": 
                LocationReportsDAO.breakdownByYearAndGender(startTime, endTime);
                break;
            
            //Top-k popular places
            case "2":
                LocationReportsDAO.topkPopularPlaces(k, 10);
                break;
             
            //Top-k companions
            case "3":
                LocationReportsDAO.topkCompanions(k);
                break;
              
            //Top-k next places
            case "4":
                LocationReportsDAO.topkNextPlaces(k);
                break;
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
