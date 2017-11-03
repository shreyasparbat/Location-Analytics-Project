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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static model.dao.LocationReportsDAO.startDateTime;
import model.entity.Group;
import model.entity.Student;
import model.entity.TimeIntervals;
import model.entity.TimeIntervalsList;
import model.utility.DBConnection;

/**
 *
 * @author amanda
 */
public class AgdDAO {

    /**
     * returns a list of student group objects where size can be zero
     *
     * @param startDateTime timestamp object of start time from user
     * @param endDateTime timestamp object of end time from user
     * @param sDAO studentDAO object
     * @param studentList list of student records found within the input time
     * range
     * @return an arraylist of groups of students
     */
    public ArrayList<Group> getStudentGroups(Timestamp startDateTime, Timestamp endDateTime, StudentDAO sDAO, TreeMap<String, Student> studentList) {

        ArrayList<Group> studentGroups = new ArrayList<>();

        try {
            if (startDateTime != null && endDateTime != null ) {
                sDAO.importDataFromDatabase(studentList, startDateTime, endDateTime);
                
                
                studentGroups = sDAO.getStudentGroups();
                studentGroups = sDAO.getSuperGroup(studentGroups);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

        return studentGroups;
    } 

}
