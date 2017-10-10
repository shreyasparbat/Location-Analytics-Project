/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import com.sun.corba.se.impl.orb.ORBSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Location;
import model.entity.Student;
import model.utility.BreakdownUtility;
import model.utility.DBConnection;

/**
 *
 * @author shrey
 */
public class LocationReportsDAO {

    static Timestamp startDateTime;
    static Timestamp endDateTime;

    public LocationReportsDAO(Timestamp startDateTime, Timestamp endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Incomplete
     *
     * @param startTime
     * @param endTime
     */
    public static void breakdownByYearAndGender(String option1, String option2, String option3) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, Student> studentMap = new HashMap<>();

        //Getting Hashtable of all students in the SIS building during processing window
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
                Student student = new Student(rs.getString("macAddress"), rs.getString("name"), rs.getString("password"), rs.getString("email"), rs.getString("gender").charAt(0));
                studentMap.put(rs.getString("macAddress"), student);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        //Check which function to call
        if (option2.equals("none2") && option3.equals("none3")) {
            //calls one value function if only the first option is filled
            BreakdownUtility.percentageOneOption(option1, studentMap);
        } else if ((!option2.equals("none2") && option3.equals("none3"))) {
            //calls 2 option function
            BreakdownUtility.percentageTwoOptions(option1, option2, studentMap);
        } else if ((option2.equals("none2") && !option3.equals("none3"))) {
            BreakdownUtility.percentageTwoOptions(option1, option3, studentMap);
        } else {
            //calls 3 function option
            BreakdownUtility.percentageAllOptions(option1, option2, option3, studentMap);
        }
    }

    /**
     *
     * Returns the top-k most popular places within a processing window. The
     * popularity of a location is derived from the number of people located
     * there in the specified processing time window.
     *
     * @param k number of list entries to be returned
     * @param milliSeconds to get the sql Timestamp
     * @return topKPlacesList a list of the top-k popular places in the selected
     * processing window
     */
    public static ArrayList<Location> topkPopularPlaces(int k, long milliSeconds) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Location> topKPlacesList = new ArrayList<>();
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
        return topKPlacesList;
    }

    /**
     * Returns the top-k most companions within a processing window.The student
     * list is derived from the number of people co-located with a specific user
     * in the specified processing time window.
     *
     * @param k the number of companions
     * @return stList List of student objects based on the number specified
     */
    public static ArrayList<Student> topkCompanions(int k) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Student> stList = new ArrayList<>();
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
        return stList;
    }

    /**
     *
     * Returns the top-k next places within a processing window. The popularity
     * of a location is derived from the number of people likely to visit there
     * in the specified processing time window.
     *
     * @param k number of list entries to be returned
     * @param milliSeconds to get the sql Timestamp
     * @return topKPlacesList a list of the top-k popular places in the selected
     * processing window
     */
    public static ArrayList<Location> topkNextPlaces(int k) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Location> topKPlacesList = new ArrayList<>();
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
        return topKPlacesList;
    }
}
