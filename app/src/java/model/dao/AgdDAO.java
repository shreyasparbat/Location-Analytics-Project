/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static model.dao.LocationReportsDAO.startDateTime;
import model.entity.Student;
import model.entity.TimeIntervals;
import model.entity.TimeIntervalsList;
import model.utility.DBConnection;

/**
 *
 * @author amanda
 */
public class AgdDAO {

    public ArrayList<ArrayList<String>> getStudentGroups(Timestamp startDateTime, Timestamp endDateTime) {

        if (startDateTime == null || endDateTime == null) {
            return null;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StudentDAO sDAO = new StudentDAO();
        HashMap<String, Student> studentList = sDAO.getAllStudentsWithinProcessingWindow(startDateTime, endDateTime);

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select d.macaddress, time,locationid from demograph d, location l"
                    + " where d.macaddress = l.macaddress"
                    + " and  l.time between ? and ? and d.macaddress = \"01e4f117e521297e879a35999d990443e067b7b7\" "
                    + " order by d.macaddress, time"
                    + " limit 1000000;");
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);

            rs = stmt.executeQuery();
            Student previousStudent = null;
            int previousLocID = -1;
            Timestamp previousTimestamp = null;

            while (rs.next()) {
                Student student = studentList.get(rs.getString("macaddress")); //current student
                int locationid = rs.getInt("locationid");
                Timestamp timestamp = rs.getTimestamp("time");
                if (student.equals(previousStudent)) { //make sures pointing to the right student

                    if (locationid != previousLocID) { //identifies change in location and clear first line buffer
                        if (previousLocID == -1) {
                            previousTimestamp = timestamp; //records start time for the first entry
                        } else { //everything else that is based on the same user but with a different location id
                            //create time interval of previous location id
                            //[previoustimestamp is the start, current timestamp (where location id changes) is the end
                            TimeIntervals intervalDuration = new TimeIntervals(previousTimestamp, timestamp);
                            HashMap<Integer, TimeIntervalsList> locationTracker = student.getLocationRecords();
                            if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                                locationTracker.put(previousLocID, new TimeIntervalsList());
                            }
                            TimeIntervalsList intervalList = locationTracker.get(previousLocID);
                            intervalList.addTimeInterval(intervalDuration);
                            previousTimestamp = timestamp;
                            //after adding, the end time becomes the start time of the current location id unless a change is spotted
                        }
                    }
                } else { //enter the last location of the previous student
                    if (previousStudent != null) {
                        TimeIntervals intervalDuration = new TimeIntervals(previousTimestamp, endDateTime); //duration is the last startTime to end of query time
                        HashMap<Integer, TimeIntervalsList> locationTracker = previousStudent.getLocationRecords();
                        if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                            locationTracker.put(previousLocID, new TimeIntervalsList());
                        }
                        TimeIntervalsList intervalList = locationTracker.get(previousLocID);
                        intervalList.addTimeInterval(intervalDuration);
                        previousTimestamp = timestamp;
                        //after adding, the end time becomes the start time of the current location id unless a change is spotted
                    }

                }
                previousStudent = student;
                previousLocID = locationid;

            }
            TimeIntervals intervalDuration = new TimeIntervals(previousTimestamp, endDateTime); //duration is the last startTime to end of query time
            HashMap<Integer, TimeIntervalsList> locationTracker = previousStudent.getLocationRecords();
            if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                locationTracker.put(previousLocID, new TimeIntervalsList());
            }
            TimeIntervalsList intervalList = locationTracker.get(previousLocID);
            intervalList.addTimeInterval(intervalDuration);

            //testing
            Student s = studentList.get("01e4f117e521297e879a35999d990443e067b7b7");
            HashMap<Integer, TimeIntervalsList> locationTracker2 = s.getLocationRecords();
            Iterator<Integer> iter = locationTracker2.keySet().iterator();
            while (iter.hasNext()) {
                int locId = iter.next();
                TimeIntervalsList list = locationTracker2.get(locId);
                if (list != null) {
                    ArrayList<TimeIntervals> intList = list.getList();
                    for (TimeIntervals ti : intList) {
                        out.println("abc");
                        out.println(locId + " " + " " + ti.getStartTime().toString() + " " + ti.getEndTime().toString());
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return null;
    }
}