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
import model.entity.TimeIntervalsList;
import model.utility.DBConnection;

/**
 * Allows the application to access the database and execute CRUD Functions
 * involved in the use of Student Objects
 *
 * @author Joel Tay
 */
public class StudentDAO {

    //attributes
    private HashMap<String, Student> studentMap;

    //constructors
    /**
     * Default Constructor
     *
     *
     */
    public StudentDAO() {
        studentMap = new HashMap<>();
    }

    //getters
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

    public ArrayList<Group> getStudentGroups() {
        ArrayList<Group> groupList = new ArrayList<>();

        //Collection<Student> tempList = studentMap.values();
        List<Student> students = new ArrayList<Student>(studentMap.values());

        //Iterating through student and pair students up with overlaps that last more than 12 minutes
        for (int i = 0; i < students.size(); i++) {
            Student currentStudent = students.get(i);
            // location timings for student 
            HashMap<Integer, TimeIntervalsList> currentStudentLocation = currentStudent.getLocationRecords();
            for (int j = i + 1; j < students.size(); j++) {
                //Group groupCheck = new Group(superGroup.getGroup(), superGroup.getRecord());
                Group groupCheck = new Group();
                groupCheck.addStudent(currentStudent);
                Student nextStudent = students.get(j);
                HashMap<Integer, TimeIntervalsList> nextStudentLocation = nextStudent.getLocationRecords();
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
                Iterator<Integer> groupIter = groupCheck.getRecord().keySet().iterator();
                double duration = 0;
                while (groupIter.hasNext()) {
                    duration += groupCheck.getRecord().get(groupIter.next()).getDuration();
                }
                if (groupCheck.getGroup().size() > 1 && duration >= 12.0) { // group is 2 man group
                    groupList.add(groupCheck);
                }
            }
        }
        groupList = getSuperGroup(groupList);
        return groupList;
    }

    public ArrayList<Group> getSuperGroup(ArrayList<Group> groups) {
        ArrayList<Group> toReturn = new ArrayList<>();
        for (Group g : groups) {
            boolean toAdd = true;
            for (Group superGroup : toReturn) {
                if (g.contains(superGroup)) {
                    HashMap<Integer, TimeIntervalsList> thisGroup = g.getRecord();
                    HashMap<Integer, TimeIntervalsList> superGroupRecord = superGroup.getRecord();
                    Iterator<Integer> iter = superGroupRecord.keySet().iterator();
                    HashMap<Integer, TimeIntervalsList> newRecord = new HashMap<>();

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
                    if (duration >= 12.0) {
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

        return toReturn;
    }
//
//    //
//    /**
//     * Returns a student after searching his name in the database
//     *
//     * @param name - the string of the name
//     * @return returns Student object if name is correct. Else returns a null
//     * object.
//     */
//    public Student getStudent(String name) {
//        for (Student s : sList) {
//            String sName = s.getName();
//            if (sName.equals(name)) {
//                return s;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Returns a list of all students in a Hashtable
//     *
//     * @return returns a Hashtable of students
//     */
//    public HashMap<String, Student> getAllStudentsMap() {
//        HashMap<String, Student> studentTable = new HashMap<>();
//        for (Student s : sList) {
//            studentTable.put(s.getMacAddress(), s);
//        }
//        return studentTable;
//    }
}
