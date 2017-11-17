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
import java.time.LocalTime;
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
    
    /**
     * A primary constructor for LocationReportsDAO 
     * @param startDateTime TimeStamp Object specifying the start date and time.
     * @param endDateTime TimeStamp Object specifying the end date and time.
     */
    public LocationReportsDAO(Timestamp startDateTime, Timestamp endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        bu = new BreakdownUtility();
    }

    /**
     * A secondary constructor for LocationReportsDAO 
     * @param startDateTime TimeStamp Object specifying the start date and time.
     * @param endDateTime TimeStamp Object specifying the end date and time.
     * @param startDateTimeTwo TimeStamp Object specifying another start date and time.
     * @param endDateTimeTwo TimeStamp Object specifying another end date and time.
     */
    public LocationReportsDAO(Timestamp startDateTime, Timestamp endDateTime, Timestamp startDateTimeTwo, Timestamp endDateTimeTwo) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startDateTimeTwo = startDateTimeTwo;
        this.endDateTimeTwo = endDateTimeTwo;
        bu = new BreakdownUtility();
    }

    /**
     * Breakdown by Year, Gender and School function taking in options specified
     * by the user.
     * @param option1 String first breakdown option
     * @param option2 String second breakdown option
     * @param option3 String third breakdown option
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
     * @return topKList which contains top-k popular places in the selected
     * processing window as a LinkedHashMap. The key denotes the sematic place
     * and the value represents the number of people in the place within the
     * query window.
     */
    public LinkedHashMap<String, Integer> topkPopularPlaces() {
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
                    + "order by noOfMacAdd DESC, semanticplace;");
            //setting parameters
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);
            stmt.setTimestamp(3, startDateTime);
            stmt.setTimestamp(4, endDateTime);
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

        try {
            sDAO.importDataFromDatabase(sMap, startDateTime, endDateTime);
            Student s = sMap.get(studentMac);
            if (s != null) {
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
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stList;
    }

    /**
     * Returns the top-k next places within a processing window. The popularity
     * of a location is derived from the number of people likely to visit there
     * in the specified processing time window.
     *
     * @param macAddList A list of MacAddresses who is previously tracked.
     * @return a HashMap of the top-k popular places in the selected
     * processing window with:
     * Key - String(Semantic Place)
     * Value - Location Object
     */
    public static HashMap<String, Location> topkNextPlaces(ArrayList<String> macAddList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //Hashmap<SemanticPlace, ArrayList<macAddresses>>
        HashMap<String, Location> map = new HashMap<>();
        LocalTime endTime = endDateTimeTwo.toLocalDateTime().toLocalTime();
        String tempLocation = null;
        LocalTime tempTime = null;
        String tempMacAddress = null;
        try {
            conn = DBConnection.createConnection();
            //selects macaddress from location within time interval and time window, and semantic place filtering by semanticplace
            stmt = conn.prepareStatement("select time, macaddress, semanticplace from\n"
                    + "(select time, macaddress,location.locationid, llu.semanticplace "
                    + "from location left outer join locationlookup llu "
                    + "on location.locationid = llu.locationid) as t\n"
                    + "where time >= ? and time < ?\n"
                    + "order by time desc, macaddress desc");
            //set params
            stmt.setTimestamp(1, startDateTimeTwo);
            stmt.setTimestamp(2, endDateTimeTwo);
            //execution of query
            rs = stmt.executeQuery();

            while (rs.next()) {
                //gets semanticplace and macaddress from result set
                String time = rs.getString("time");
                //checks if the time is within 5 min time window
                Timestamp rsTime = Timestamp.valueOf(time);
                LocalTime rsLocalTime = rsTime.toLocalDateTime().toLocalTime();
                LocalTime timeTest = rsLocalTime.plusMinutes(5);
                //if (timeTest.isAfter(endTime) || timeTest.equals(endTime)) {
                if (timeTest.isAfter(endTime)) {
                    continue;
                }
                String querySemanticPlace = rs.getString("semanticplace");
                String macAddress = rs.getString("macaddress");
                //checks if macAddress is within tracked users
                if (macAddList.indexOf(macAddress) == -1) {
                    continue;
                }
                //check if tempTime is not null and temp location is not null for recurring locations by semantic place
                if (tempTime != null && tempLocation != null && tempMacAddress != null) {
                    //check if macAddress is the same
                    if (tempMacAddress.equals(macAddress)) {
                        //check for latest time for macAddress given
                        if (rsLocalTime.isBefore(tempTime)) {
                            continue;
                        }
                    }

                }
                //if the location already exists in the map
                if (map.containsKey(querySemanticPlace)) {
                    Location l = map.get(querySemanticPlace);
                    l.addStudent(macAddress);
                } else {
                //location does not exist in the map
                    Location l = new Location(querySemanticPlace);
                    l.addStudent(macAddress);
                    map.put(querySemanticPlace, l);
                }
                //sets the temp variables
                tempTime = rsLocalTime;
                tempLocation = querySemanticPlace;
                tempMacAddress = macAddress;
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return map;
        //return peopleInNextTimeWindow;
    }

   /**
     * Returns the mac-addresses within the time window that are within the
     * specified semantic place. The mac-addresses are obtained from the data
     * within location.csv that matches with the specified processing time
     * window.
     *
     * @param semanticPlace A String value of the semantic place of choice
     * @return addressList as an ArrayList of mac-addresss that appear in the
     * location within the specified processing window.
     */
    public static ArrayList<String> peopleInSemanticPlace(String semanticPlace) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> addressList = new ArrayList<>();
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select l.macaddress \n"
                    + "from location l inner join locationlookup llu \n"
                    + "on l.locationid = llu.locationid inner join \n"
                    + "(select max(time) as lastestTime, macaddress \n"
                    + "from location l, locationlookup llu \n"
                    + "where l.locationid=llu.locationid\n"
                    + "and time >= ? and time < ?\n"
                    + "group by macaddress) t \n"
                    + "on l.time = t.lastestTime \n"
                    + "and l.macaddress = t.macaddress \n"
                    + "and semanticplace= ?;");
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);
            stmt.setString(3, semanticPlace);
            rs = stmt.executeQuery();
            while (rs.next()) {
                addressList.add(rs.getString("macaddress"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return addressList;
    }

}
