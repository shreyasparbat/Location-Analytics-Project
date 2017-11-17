/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static model.utility.DBConnection.createConnection;

/**
 *
 * @author Ming Xuan
 */
public class TopKUtility {
    
    /**
     * A method that calls the database and returns a list of string array where the first entry of the array is a student mac address and second entry is name
     * @return list of student's macaddress and email
     */
    public static List<String> getStudentMacAddress(){
        List<String> studentMA = new ArrayList<>();
        try {
            Connection conn = DBConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement("select distinct macaddress from location limit 10000;");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                studentMA.add(rs.getString(1));
            }
            DBConnection.close(conn, stmt, rs);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentMA;
    }
    
    /**
     * A method that connects Database and returns all semantic places found
     * @return a list of strings which contains the semantic places
     */
    public static List<String> getSemanticPlaces(){
        List<String> semanticPlaces = new ArrayList<>();
        try {
            Connection conn = DBConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement("select distinct semanticplace from locationlookup limit 10000;");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                semanticPlaces.add(rs.getString(1));
            }
            DBConnection.close(conn, stmt, rs);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return semanticPlaces;
    }
    /**
     * A method that calls the database and returns a email using a macaddress
     * @param macaddress mac address of a user
     * @return list of a student's email
     */
    public static String getStudentEmail(String macaddress){
        try {
            Connection conn = DBConnection.createConnection();
            PreparedStatement stmt = conn.prepareStatement("select distinct email from demograph where macaddress = ?;");
            stmt.setString(1, macaddress);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                return rs.getString("email");
            }
            DBConnection.close(conn, stmt, rs);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
