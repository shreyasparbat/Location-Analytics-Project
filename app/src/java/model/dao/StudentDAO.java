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
        
        return studentMap;
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
