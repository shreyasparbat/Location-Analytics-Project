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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Location;
import model.utility.DBConnection;

/**
 *
 * @author Joleen Mok
 */
public class LocationLookupDAO {

    private ArrayList<Location> allLocation;

    public LocationLookupDAO() {
        allLocation = new ArrayList<>();
    }

    public ArrayList<Location> getAllLocations() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select locationid, semanticplace from locationlookup");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Location location = new Location(rs.getString("semanticplace"), rs.getInt("locationid"));
                allLocation.add(location);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return allLocation;
    }
}
