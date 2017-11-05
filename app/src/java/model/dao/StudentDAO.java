package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.entity.Student;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Group;
import model.entity.TimeIntervals;
import model.entity.TimeIntervalsList;
import model.utility.AGDStudentComparator;
import model.utility.DBConnection;

/**
 * Allows the application to access the database and execute CRUD Functions
 * involved in the use of Student Objects
 *
 * @author Joel Tay
 */
public class StudentDAO {

    //attributes
    private TreeMap<String, Student> studentMap;

    //constructors
    /**
     * Default Constructor
     *
     *
     */
    public StudentDAO() {
        studentMap = new TreeMap<>();
    }
    
    /**
     * Returns a selected student using a macaddress
     * @param macAddress Specified student's macaddress
     * @return Student object if macaddress is found in the student list, else null.
     */
    public Student getStudent(String macAddress){
        if(macAddress!=null){
            return studentMap.get(macAddress);
        }
        return null;
    }
    //getters
    /**
     * to get students within processing window
     *
     * @param startDateTime timestamp object of start time from user
     * @param endDateTime timestamp object of end time from user
     * @return HashMap of students within processing window
     */
    public TreeMap<String, Student> getAllStudentsWithinProcessingWindow(Timestamp startDateTime, Timestamp endDateTime) {
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

        return studentMap;
    }

    /**
     * process student to return groups of students that spent a minimum of 12
     * mins together
     *
     * @return ArrayList of Groups
     */
    public ArrayList<Group> getStudentGroups(Student currentStudent) {
        ArrayList<Group> groupList = new ArrayList<>();

        //Collection<Student> tempList = studentMap.values();
        List<Student> students = new ArrayList<Student>(studentMap.values());

        //Iterating through student and pair students up with overlaps that last more than 12 minutes
        // location timings for student 
        TreeMap<Integer, TimeIntervalsList> currentStudentLocation = currentStudent.getLocationRecords();
        for (int j = 0; j < students.size(); j++) {
            Student nextStudent = students.get(j);
            if (!nextStudent.equals(currentStudent)) {
                Group groupCheck = new Group();
                groupCheck.addStudent(currentStudent);
                TreeMap<Integer, TimeIntervalsList> nextStudentLocation = nextStudent.getLocationRecords();
                Iterator<Integer> iter = currentStudentLocation.keySet().iterator();

                while (iter.hasNext()) {
                    Integer place = iter.next();
                    TimeIntervalsList currentTimeList;
                    if (nextStudentLocation.containsKey(place)) {
                        currentTimeList = currentStudentLocation.get(place);
                        TimeIntervalsList nextTimeList = nextStudentLocation.get(place);
                        TimeIntervalsList overlap = currentTimeList.getOverlaps(nextTimeList);
                        if (overlap.getList().size() > 0) { //Whens there an overlap
                            groupCheck.addLocation(place, overlap);
                            if (!groupCheck.getGroup().contains(nextStudent)) {
                                groupCheck.addStudent(nextStudent); // adds the student to the group if havnt
                            }
                        }
                    }
                }
                if (groupCheck.getGroup().size() > 1) { // group is 2 man group
                    groupList.add(groupCheck);
                }
            }

        }

        return groupList;
    }

    /**
     * process students to return groups of students that spent a minimum of 12
     * mins together
     *
     * @return ArrayList of Groups
     */
    public ArrayList<Group> getStudentGroups() {
        ArrayList<Group> groupList = new ArrayList<>();

        //Collection<Student> tempList = studentMap.values();
        List<Student> students = new ArrayList<Student>(studentMap.values());

        //Iterating through student and pair students up with overlaps that last more than 12 minutes
        for (int i = 0; i < students.size(); i++) {
            Student currentStudent = students.get(i);
            // location timings for student 
            TreeMap<Integer, TimeIntervalsList> currentStudentLocation = currentStudent.getLocationRecords();
            for (int j = i + 1; j < students.size(); j++) {
                //Group groupCheck = new Group(superGroup.getGroup(), superGroup.getRecord());
                Group groupCheck = new Group();
                groupCheck.addStudent(currentStudent);
                Student nextStudent = students.get(j);
                TreeMap<Integer, TimeIntervalsList> nextStudentLocation = nextStudent.getLocationRecords();
                Iterator<Integer> iter = currentStudentLocation.keySet().iterator();

                while (iter.hasNext()) {
                    Integer place = iter.next();
                    TimeIntervalsList currentTimeList;
                    if (nextStudentLocation.containsKey(place)) {
                        currentTimeList = currentStudentLocation.get(place);
                        TimeIntervalsList nextTimeList = nextStudentLocation.get(place);
                        TimeIntervalsList overlap = currentTimeList.getOverlaps(nextTimeList);
                        if (overlap.getList().size() >= 1) { //Whens there an overlap
                            groupCheck.addLocation(place, overlap);
                            if (!groupCheck.getGroup().contains(nextStudent)) {
                                groupCheck.addStudent(nextStudent); // adds the student to the group if havnt
                            }
                        }
                    }
                }
                Iterator<Integer> groupIter = groupCheck.getRecord().keySet().iterator();
                double duration = groupCheck.getTotalDuration();

                if (groupCheck.getGroup().size() > 1 && duration >= 720.0) { // group is 2 man group
                    groupList.add(groupCheck);
                }
            }
        }
        return groupList;
    }

    /**
     * to process groups to remove subsets, such hat larger groups of students
     * are formed
     *
     * @param groups groups of students
     * @return arraylist of merged groups of students
     */
    public ArrayList<Group> getSuperGroup(ArrayList<Group> groups) {
        ArrayList<Group> toReturn = new ArrayList<>();
        for (Group g : groups) {
            boolean toAdd = true;
            for (Group superGroup : toReturn) {
                if (g.contains(superGroup)) {
                    TreeMap<Integer, TimeIntervalsList> thisGroup = g.getRecord();
                    TreeMap<Integer, TimeIntervalsList> superGroupRecord = superGroup.getRecord();
                    Iterator<Integer> iter = superGroupRecord.keySet().iterator();
                    TreeMap<Integer, TimeIntervalsList> newRecord = new TreeMap<>();

                    double duration = 0;
                    while (iter.hasNext()) {
                        Integer place = iter.next();
                        TimeIntervalsList superGroupTimeList;
                        if (thisGroup.containsKey(place)) {
                            superGroupTimeList = superGroupRecord.get(place);
                            TimeIntervalsList smallGroupTimeList = thisGroup.get(place);
                            TimeIntervalsList overlap = superGroupTimeList.getOverlaps(smallGroupTimeList);
                            if (overlap.getList().size() > 0) { //Whens there an overlap
                                duration += overlap.getDuration();
                                newRecord.put(place, overlap); //places new overlap
                            }
                        }
                    }
                    if (duration >= 720.0) {
                        superGroup.addGroup(g);
                        toAdd = false;
                        superGroup.setLocaList(newRecord);
                    }
                }
            }
            if (toAdd) {
                toReturn.add(g);
            }

        }
        for (Group g : toReturn) {
            Collections.sort(g.getGroup(), new AGDStudentComparator());
        }
        return toReturn;
    }

    /**
     * Merge groups together based on their total duration in a given list of
     * group objects
     *
     * @param groups
     * @return list of merged groups
     */
    public ArrayList<Group> mergeGroups(ArrayList<Group> groups) {
        ArrayList<Group> toReturn = new ArrayList<>();
        for (Group g : groups) {
            boolean toAdd = true;
            for (Group mergeGroup : toReturn) {
                if (g.getTotalDuration() == mergeGroup.getTotalDuration()) {
                    mergeGroup.addGroup(g);
                    toAdd = false;
                }
            }
            if (toAdd) {
                toReturn.add(g);
            }
        }
        return toReturn;
    }

    /**
     * inserts time interval records into each student within the given time
     * frame
     *
     * @param studentList list of student records found within the input time
     * @param startDateTime timestamp object of start time from user
     * @param endDateTime timestamp object of end time from user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void importDataFromDatabase(TreeMap<String, Student> studentList, Timestamp startDateTime, Timestamp endDateTime) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        conn = DBConnection.createConnection();
        stmt = conn.prepareStatement("select macaddress, time, locationid from location"
                + " where time >= ? and time < ? "
                + " order by macaddress, time"
                + " limit 1000000;");
        stmt.setTimestamp(1, startDateTime);
        stmt.setTimestamp(2, endDateTime);

        rs = stmt.executeQuery();

        //Buffer for the first row
        Student previousStudent = null;
        int previousLocID = -1;
        Timestamp previousTimestamp = null;
        TimeIntervals interval = null;

        //Looping through all rows of the result sets to put in location datas of the student
        //Always compare current row with previous row, and see if there is a difference
        //If there is a difference in student object, means it reachs the end of the previous student records
        //If there is a difference in locationid object, means there is a change in location
        while (rs.next()) {

            Student student = studentList.get(rs.getString("macaddress")); //current student
            if (student == null) {
                Student sTemp = new Student(rs.getString("macaddress"));
                studentList.put(rs.getString("macaddress"), sTemp);
                student = studentList.get(rs.getString("macaddress"));
            }
            int locationid = rs.getInt("locationid"); //current
            Timestamp timestamp = rs.getTimestamp("time");
            if (student.equals(previousStudent)) { //make sures pointing to the right student
                if (locationid != previousLocID) { //identifies change in location and clear first line buffer
                    //everything else that is based on the same user but with a different location id
                    //create time interval of previous location id
                    //[previoustimestamp is the start, current timestamp (where location id changes) is the end
                    double timeDiff = (timestamp.getTime() - interval.getEndTime().getTime()) / 60000;
                    TimeIntervals intervalDuration;
                    if (timeDiff <= 5.0) {
                        intervalDuration = new TimeIntervals(interval.getStartTime(), timestamp); //duration is the last startTime to end of query time
                    } else {
                        Timestamp newEndTime = new Timestamp(interval.getEndTime().getTime() + 5 * 60 * 1000);
                        intervalDuration = new TimeIntervals(interval.getStartTime(), newEndTime);
                    }
                    TreeMap<Integer, TimeIntervalsList> locationTracker = student.getLocationRecords();
                    if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                        locationTracker.put(previousLocID, new TimeIntervalsList());
                    }
                    TimeIntervalsList intervalList = locationTracker.get(previousLocID);
                    intervalList.addTimeInterval(intervalDuration);
                    previousTimestamp = timestamp;
                    interval = new TimeIntervals(previousTimestamp, previousTimestamp);
                    //after adding, the end time becomes the start time of the current location id unless a change is spotted
                } else {
                    interval.setEndTime(timestamp);
                }

            } else { //enter the last location of the previous student
                if (previousStudent != null) {
                    double timeDiff = (endDateTime.getTime() - interval.getEndTime().getTime()) / 60000;
                    TimeIntervals intervalDuration;
                    if (timeDiff <= 5.0) {
                        intervalDuration = new TimeIntervals(interval.getStartTime(), endDateTime); //duration is the last startTime to end of query time
                    } else {
                        Timestamp newEndTime = new Timestamp(interval.getEndTime().getTime() + 5 * 60 * 1000);
                        intervalDuration = new TimeIntervals(interval.getStartTime(), newEndTime);
                    }
                    TreeMap<Integer, TimeIntervalsList> locationTracker = previousStudent.getLocationRecords();
                    if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                        locationTracker.put(previousLocID, new TimeIntervalsList());
                    }
                    TimeIntervalsList intervalList = locationTracker.get(previousLocID);
                    intervalList.addTimeInterval(intervalDuration);
                    previousTimestamp = timestamp;
                    //after adding, the end time becomes the start time of the current location id unless a change is spotted
                    interval = new TimeIntervals(previousTimestamp, previousTimestamp);
                } else {
                    previousTimestamp = timestamp;
                    interval = new TimeIntervals(previousTimestamp, previousTimestamp);
                }

            }
            //before moving to the next row, stores current student details as previous
            previousStudent = student;
            previousLocID = locationid;

        }
        if (interval != null) {
            //last row from the ResultSet, executes the last timeinterval
            double timeDiff = (endDateTime.getTime() - interval.getEndTime().getTime()) / 60000.0;
            TimeIntervals intervalDuration; //duration is the last startTime to end of query time
            if (timeDiff <= 5.0) {
                intervalDuration = new TimeIntervals(interval.getStartTime(), endDateTime); //duration is the last startTime to end of query time
            } else {
                Timestamp newEndTime = new Timestamp(interval.getEndTime().getTime() + 5 * 60 * 1000);
                intervalDuration = new TimeIntervals(interval.getStartTime(), newEndTime);
            }

            TreeMap<Integer, TimeIntervalsList> locationTracker = previousStudent.getLocationRecords();
            if (!locationTracker.containsKey(previousLocID)) { // if it is a new location id
                locationTracker.put(previousLocID, new TimeIntervalsList());
            }
            TimeIntervalsList intervalList = locationTracker.get(previousLocID);
            intervalList.addTimeInterval(intervalDuration);
        }

        DBConnection.close(conn, stmt, rs);
    }
}
