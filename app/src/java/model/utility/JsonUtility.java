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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel Tay
 */
public class JsonUtility {

    /**
     * To give a rank after passing through a string k
     * @param k input 
     * @return rank, 3 if k is null
     * @throws NumberFormatException
     */
    public static int jsonToInt(String k) throws NumberFormatException {
        // if not specified, return 3
        if (k == null) {
            return 3;
        }
        int num = Integer.parseInt(k); //throws number format exeption if string does not contain parsable integer

        if (num < 1 || num > 10) {
            return -1; // rank not between 1 - 10
        }
        return num;

    }

    /**
     * Checks an input string variable to see whether is it a valid mac-address found in database
     * @param mac the string mac-address taken from the form param
     * @return true if input string is a valid mac-address found in the database, else false
     */
    public static boolean checkMacaddress(String mac) {
        if (mac == null) {
            return false;
        }
        boolean toReturn = mac.matches("[a-fA-F0-9]{40}");
        if (toReturn) {
            Connection conn = null;
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                conn = DBConnection.createConnection();
                stmt = conn.prepareStatement("select distinct macaddress from location where macaddress = ?;");
                stmt.setString(1, mac);
                rs = stmt.executeQuery();
                if (rs.next() == true) {
                    return true; //have 1 record
                }
            } catch (SQLException ex) {
                Logger.getLogger(JsonUtility.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JsonUtility.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DBConnection.close(conn, stmt, rs);
            }
        }
        return false;

    }
}
