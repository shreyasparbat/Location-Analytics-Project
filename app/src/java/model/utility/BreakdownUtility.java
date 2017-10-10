/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.HashMap;
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
    private static final String[] YEAR_LIST = {"2013", "2014", "2015", "2016", "2017"};
    private static final String[] GENDER_LIST = {"M", "F"};
    private static final String[] SCHOOL_LIST = {"sis", "law", "accountancy", "economics", "business", "socsc"};
    private static StudentDAO studentDAO = new StudentDAO();

    public static void percentageOneOption(String option, HashMap<String, Student> studentMap) {
        HashMap<String, Double> percentageList = null;
        
        if ("year".equals(option)) {
            
        }
    }

    public static void percentageTwoOptions(String option1, String option2, HashMap<String, Student> studentMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void percentageAllOptions(String option1, String option2, String option3, HashMap<String, Student> studentMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //returns a hashtable of percentage breakdown in a key value pair
    // key = year, value = percentage
    public HashMap<String, Double> byYear(HashMap<String, Student> studentMap) {
        HashMap<String, Double> yearPercentage = new HashMap<>();
        double totalStudentNo = studentMap.size();

        //getting percentage
        for (String year : YEAR_LIST) {
            HashMap<String, Student> yearStud = getStudentsByYear(year, studentMap);
            double noStudentsInYear = yearStud.size();
            double percentage = (noStudentsInYear / totalStudentNo) * 100;
            yearPercentage.put(year, percentage);
        }

        //returning
        return yearPercentage;
    }

    //returns a hashtable of percentage breakdown by gender in a key value pair
    // key = gender, value = percentage
    public HashMap<String, Double> byGender(HashMap<String, Student> studentMap) {
        HashMap<String, Double> yearPercentage = new HashMap<>();
        double totalStudentNo = studentMap.size();

        //getting percentage
        for (String gender : GENDER_LIST) {
            HashMap<String, Student> genderStud = getStudentsByGender(gender, studentMap);
            double noStudentsInYear = genderStud.size();
            double percentage = (noStudentsInYear / totalStudentNo) * 100;
            yearPercentage.put(gender, percentage);
        }

        //returning
        return yearPercentage;
    }

    //returns a hashtable of percentage breakdown by school in a key value pair
    // key = school, value = percentage
    public HashMap<String, Double> bySchool(HashMap<String, Student> studentMap) {
        HashMap<String, Double> yearPercentage = new HashMap<>();
        double totalStudentNo = studentMap.size();

        //getting percentage
        for (String sch : SCHOOL_LIST) {
            HashMap<String, Student> schoolStud = getStudentsBySchool(sch, studentMap);
            double noStudentsInSchool = schoolStud.size();
            double percentage = (noStudentsInSchool / totalStudentNo) * 100;
            yearPercentage.put(sch, percentage);
        }

        //returning
        return yearPercentage;
    }

    public HashMap<String, Student> getStudentsByYear(String year, HashMap<String, Student> studentMap) {
        //creates new hashtable
        HashMap<String, Student> studentsByYear = new HashMap<>();
        Iterator<String> iter = studentMap.keySet().iterator();
        String key = "";

        //getting valid students
        while (iter.hasNext()) {
            key = iter.next();
            Student student = studentMap.get(key);
            //if the student is in the desired year && is not in the new hashtable add student in the new hashtable
            String email = student.getEmail();
            if (!email.contains(year)) {
                if (!studentsByYear.containsKey(student.getMacAddress())) {
                    studentsByYear.put(key, student);
                }
            }
        }

        //returns new hashtable
        return studentsByYear;
    }
    
    //returns a hashtable of students based on the gender specified.
    public static HashMap<String, Student> getStudentsByGender(String g, HashMap<String, Student> studentMap) {
        char requiredGender = g.charAt(0);
        HashMap<String, Student> studentsByGender = new HashMap<>();
        Iterator<String> iter = studentMap.keySet().iterator();
        String key = "";

        //getting valid students
        while (iter.hasNext()) {
            key = iter.next();
            Student student = studentMap.get(key);
            //if the student has desired gender && is not in the new hashtable add student in the new hashtable
            char studentGender = student.getGender();
            if (requiredGender == studentGender) {
                if (!studentsByGender.containsKey(student.getMacAddress())) {
                    studentsByGender.put(key, student);
                }
            }
        }

        //returns new hashtable
        return studentsByGender;
    }

    //returns a hashtable of students based on the school specified.
    public static HashMap<String, Student> getStudentsBySchool(String school, HashMap<String, Student> studentMap) {
        //creates new hashtable
        HashMap<String, Student> studentsBySchool = new HashMap<>();
        Iterator<String> iter = studentMap.keySet().iterator();
        String key = "";

        //getting valid students
        while (iter.hasNext()) {
            key = iter.next();
            Student student = studentMap.get(key);
            //if the student is in the desired school && is not in the new hashtable add student in the new hashtable
            String email = student.getEmail();
            if (email.contains(school)) {
                if (!studentsBySchool.containsKey(student.getMacAddress())) {
                    studentsBySchool.put(key, student);
                }
            }
        }
        //returns new hashtable
        return studentsBySchool;

    }
}
