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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Group;
import model.entity.Location;
import model.entity.Student;
import model.utility.BreakdownUtility;
import model.utility.DBConnection;
import model.utility.GroupComparator;
import model.utility.LocationComparator;

/**
 *
 * @author shrey
 */
public class LocationReportsDAO {

    static Timestamp startDateTime;
    static Timestamp endDateTime;
    static Timestamp startDateTimeTwo;
    static Timestamp endDateTimeTwo;
    private BreakdownUtility bu;

    public LocationReportsDAO(Timestamp startDateTime, Timestamp endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        bu = new BreakdownUtility();
    }

    public LocationReportsDAO(Timestamp startDateTime, Timestamp endDateTime, Timestamp startDateTimeTwo, Timestamp endDateTimeTwo) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startDateTimeTwo = startDateTimeTwo;
        this.endDateTimeTwo = endDateTimeTwo;
        bu = new BreakdownUtility();
    }

    /**
     * Incomplete
     *
     * @param startTime
     * @param endTime
     */
    public void breakdownByYearAndGender(String option1, String option2, String option3) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TreeMap<String, Student> studentMap = new TreeMap<>();

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
                Student student = new Student(rs.getString("macAddress"), rs.getString("name"), rs.getString("email"), rs.getString("gender").charAt(0));
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
            bu.percentageOneOption(option1, studentMap);
        } else if ((!option2.equals("none2") && option3.equals("none3"))) {
            //calls 2 option function
            bu.percentageTwoOptions(option1, option2, studentMap);
        } else if ((option2.equals("none2") && !option3.equals("none3"))) {
            bu.percentageTwoOptions(option1, option3, studentMap);
        } else {
            //calls 3 function option
            bu.percentageAllOptions(option1, option2, option3, studentMap);
        }
    }

    /**
     *
     * Returns the top-k most popular places within a processing window. The
     * popularity of a location is derived from the number of people located
     * there in the specified processing time window.
     *
     * @param k number of list entries to be returned
     * @return topKPlacesList a list of the top-k popular places in the selected
     * processing window as a LinkedHashMap
     */
    public LinkedHashMap<String, Integer> topkPopularPlaces(int k) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        LinkedHashMap<String, Integer> topKList = new LinkedHashMap<>();
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select llu.semanticplace as 'semanticplace', count(t1.macadd) as 'noOfMacAdd' from \n"
                    + "(select macaddress as 'macadd', time as 'ts', locationid as 'locationid' from location \n"
                    + "where time >=? and time < ?) as t1\n"
                    + "inner join \n"
                    + "(select macaddress  as 'macadd2', max(time) as 'maxts' from location \n"
                    + "where time >= ? and time < ? \n"
                    + "group by macadd2) as t2\n"
                    + "on t1.macAdd = t2.macadd2 and t1.ts = t2.maxts right outer join locationlookup llu\n"
                    + "on t1.locationid = llu.locationid group by llu.semanticplace\n"
                    + "order by noOfMacAdd DESC limit ?;");
            //setting parameters
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);
            stmt.setTimestamp(3, startDateTime);
            stmt.setTimestamp(4, endDateTime);
            stmt.setInt(5, k);
            rs = stmt.executeQuery();
            //generating result set
            while (rs.next()) {
                topKList.put(rs.getString("semanticplace"), rs.getInt("noOfMacAdd"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        //returns LinkedHashMap
        return topKList;
    }

    /**
     * Returns the top-k most companions within a processing window.The student
     * list is derived from the number of people co-located with a specific user
     * in the specified processing time window.
     *
     * @param k the number of companions
     * @param studentMac student's unique macaddress
     * @return stList List of student objects based on the number specified
     */
    public HashMap<Integer, Group> topkCompanions(int k, String studentMac) {
        HashMap<Integer, Group> stList = new HashMap<>();
        StudentDAO sDAO = new StudentDAO();
        TreeMap<String, Student> sMap = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);
        Student s = sMap.get(studentMac);
        if (s != null) {
            try {
                sDAO.importDataFromDatabase(sMap, startDateTime, endDateTime);
                //gets groups of two
                ArrayList<Group> studentGroup = sDAO.getStudentGroups(s);
                //sort groups by their duration (Highest to lowest)
                Collections.sort(studentGroup, new GroupComparator());

                //merge the groups if they have common total duration (regardless of location)
                studentGroup = sDAO.mergeGroups(studentGroup);
                int i = 1;
                for (Group g : studentGroup) {
                    if (i <= k) {
                        stList.put(i, g);
                        i++;
                    } else {
                        continue;
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public static ArrayList<Location> topkNextPlaces(int k, String semanticPlace) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Location> topKNextPlacesList = new ArrayList<>();
        HashMap<String, Location> locationMap = new HashMap<>();
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select macaddress, semanticplace\n"
                    + "from location l \n"
                    + "inner join locationlookup ll\n"
                    + "on l.locationid = ll.locationid \n"
                    + "where macaddress in\n"
                    + "	(select macaddress from location\n"
                    + "	where macaddress in \n"
                    + "		(select distinct macaddress from location, locationlookup \n"
                    + "		 where location.locationid = locationlookup.locationid \n"
                    + "          and locationlookup.semanticplace = ?)\n"
                    + "	and time >= ? and time < ?\n"
                    + "	group by macaddress)\n"
                    + "and time >= ? and time < ?\n"
                    + "group by macaddress, semanticplace\n"
                    + "having min(time) < date_sub(max(time), interval 5 minute)\n"
                    + "order by macaddress;");
            //set params
            stmt.setString(1, semanticPlace);
            stmt.setTimestamp(2, startDateTime);
            stmt.setTimestamp(3, endDateTime);
            stmt.setTimestamp(4, startDateTimeTwo);
            stmt.setTimestamp(5, endDateTimeTwo);
            //execution of query
            rs = stmt.executeQuery();

            while (rs.next()) {
                //gets semanticplace and macaddress from result set
                String querySemanticPlace = rs.getString("semanticplace");
                String macAddress = rs.getString("macaddress");

                //Iterates though list of topKNextPlaces. If location does not exist, create new locaiton and add to list
                //if location exists, retrieve macAddress List from the location
                if (locationMap.containsKey(querySemanticPlace)) {
                    Location location = locationMap.get(querySemanticPlace);
                    location.addStudent(macAddress);
                } else {
                    Location location = new Location(querySemanticPlace);
                    location.addStudent(macAddress);
                    locationMap.put(querySemanticPlace, location);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        //transferring from HashMap to ArrayList
        Iterator iter = locationMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Location loc = locationMap.get(key);
            topKNextPlacesList.add(loc);
        }
        //sorting of topKNextPlacesList
        Collections.sort(topKNextPlacesList, new LocationComparator());
        //returning only require K values

        return topKNextPlacesList;
    }

    public static ArrayList<String> peopleInSemanticPlace(String semanticPlace) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> studentList = new ArrayList<>();
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement( ""
                    + "select macaddress from location\n"
                    + "	where macaddress in \n"
                    + "		(select distinct macaddress from location, locationlookup \n"
                    + "		 where location.locationid = locationlookup.locationid \n"
                    + "          and locationlookup.semanticplace = ?)\n"
                    + "	and time >= ? and time < ?\n"
                    + "	group by macaddress");
            stmt.setString(1, semanticPlace);
            stmt.setTimestamp(2, startDateTime);
            stmt.setTimestamp(3, endDateTime);
            rs = stmt.executeQuery();
            while (rs.next()) {
                studentList.add(rs.getString("macaddress"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return studentList;
    }
}
