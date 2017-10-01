/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.utility.DBConnection;

/**
 *
 * @author shrey
 */
public class LocationReportsDAO {

    public static void breakdownByYearAndGender(Time startTime, Time endTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        //

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("");
            rs = stmt.executeQuery("something");
            
            while (rs.next()) {
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
    }

    /**
     * Returns the top-k most popular places within a processing window. The
     * popularity of a location is derived from the number of people located
     * there in the specified processing time window.
     *
     * @param k number of list entries to be returned
     * @param milliSeconds to get the sql Timestamp
     * @return topkList a list of the top-k popular places in the selected
     * processing window
     */
    public static void topkPopularPlaces(int k, long milliSeconds) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("SELECT * from demograph d, location l, locationlookup llu "
                    + "where d.macaddress = l.macaddress "
                    + "and l.locationid = llu.locationid "
                    + "and d.time = ?;");
            //stmt.setTime(1, );
            rs = stmt.executeQuery("something");
            
            while (rs.next()) {
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
    }
    
    public static void topkCompanions(int k) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("");
            rs = stmt.executeQuery("something");
            
            while (rs.next()) {
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
    }
    
    public static void topkNextPlaces(int k) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("");
            rs = stmt.executeQuery("something");
            
            while (rs.next()) {
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
    }
}
