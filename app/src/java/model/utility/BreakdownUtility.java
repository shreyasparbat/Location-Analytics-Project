/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.Hashtable;
import java.util.Iterator;
import model.dao.StudentDAO;
import model.entity.Student;

/**
 *
 * @author Shreyas
 * @author Ming Xuan
 */
public class BreakdownUtility {

    //standard attributes
    private String[] yearList = {"2013", "2014", "2015", "2016", "2017"};
    private char[] genderList = {'M', 'F'};
    private String[] schoolList = {"sis", "law", "accountancy", "economics", "business", "socsc"};
    StudentDAO studentDAO = new StudentDAO();

    //returns a hashtable of percentage breakdown in a key value pair
    // key = year, value = percentage
    public Hashtable<String, Double> byYear() {
        Hashtable<String, Double> yearPercentile = new Hashtable<>();
        Hashtable<String, Student> studentList = studentDAO.getAllStudentsTable();
        double totalStudentNo = studentList.size();
        for (String year : yearList) {
            Hashtable<String, Student> yearStud = getStudentsByYear(year, studentList);
            double noStudentsInYear = yearStud.size();
            double percentile = noStudentsInYear / totalStudentNo;
            yearPercentile.put(year, percentile);
        }
        return yearPercentile;
    }

    public Hashtable<String, Student> getStudentsByYear(String year, Hashtable<String, Student> table) {
        //creates new hashtable
        Hashtable<String, Student> studentsByYear = new Hashtable<>();
        Iterator<String> iter = table.keySet().iterator();
        String key = "";
        while (iter.hasNext()) {
            key = iter.next();
        }
        Student student = table.get(key);
        //if the student is in the desired year && is not in the new hashtable add student in the new hashtable
        String email = student.getEmail();
        if (email.indexOf(year) == -1) {
            if (!studentsByYear.containsKey(student.getMacAddress())) {
                studentsByYear.put(key, student);
            }
        }

        //returns new hashtable
        return studentsByYear;
    }

}
