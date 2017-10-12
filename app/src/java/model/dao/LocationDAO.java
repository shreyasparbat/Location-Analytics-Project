package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.entity.Location;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.utility.DBConnection;

/**
 *  Allows the application to access the database and execute CRUD Functions
 * involved in the use of Location Objects
 * @author Joel Tay
 */
public class LocationDAO{
    //attributes
    private ArrayList<Location> locationList;
    
    //constructors

    /**
     * Initializes a newly created LocationDAO object
     */
    public LocationDAO(){
        locationList = new ArrayList<Location>();  
    }
    
    //methods
    //returns a specific semanticLocation

    /**
     * Returns the name of the semantic location based on the location I.D.
     * @param id int of the location id 
     * @return String of the name of the semantic location if found. Else return null
     */
    public String getSpecificSemanticLocation(int id){
        
        for(Location l: locationList){
            if(l.getLocationId() == id){
                return l.getSemanticPlace();
            }
        }

        return null;
    }
    
    /**
     * Returns the entire list of available locations
     * @return ArrayList of Location
     */
    public ArrayList<Location> getAllLocation(){
        return locationList;
    }
    
    //returns 

    /**
     * Returns all location objects of a semanticLocation
     * @param semanticLocation the string of the semantic location 
     * @return ArrayList of Location 
     */
    public ArrayList<Location> getLocation(String semanticLocation){
        ArrayList<Location> semanticList = new ArrayList<>();
        for(Location l : locationList){
            if(l.getSemanticPlace().equals(semanticLocation)){
                semanticList.add(l);
            }
        }
        return semanticList;
    }
    
    public HashMap<String, Student> getAllStudentsWithinProcessingWindow(Timestamp startDateTime, Timestamp endDateTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Getting HashMap of all students in the SIS building during processing window
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select DISTINCT d.macaddress, name, password, email, gender "
                    + "from demograph d, location l, locationlookup llu "
                    + "where d.macaddress = l.macaddress and time >= ? and time < ? "
                    + "and l.locationid = llu.locationid");
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);

            rs = stmt.executeQuery();

            while (rs.next()) {
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        
        return studentMap;
    }
    
}