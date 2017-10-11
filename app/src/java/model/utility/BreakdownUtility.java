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

    public static HashMap<String, Double> percentageOneOption(String option, HashMap<String, Student> studentMap) {
        HashMap<String, Double> percentageOneList = new HashMap<>();

        if ("year".equals(option)) {
            percentageOneList = byYear(studentMap);
        }
        if ("gender".equals(option)) {
            percentageOneList = byGender(studentMap);
        }
        if ("school".equals(option)) {
            percentageOneList = bySchool(studentMap);
        }

        return percentageOneList;
    }

    public static HashMap<String, HashMap<String, Double>> percentageTwoOptions(String option1, String option2, HashMap<String, Student> studentMap) {
        HashMap<String, HashMap<String, Double>> percentageTwoList = new HashMap<>();

        if ("year".equals(option1)) { 
            //Based on year, first get HashMap of students, then save it in another HashMap with year as key and the
            //previously obtained HashMap as value
            for (String year : YEAR_LIST) {
                percentageTwoList.put(year, percentageOneOption(option2, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            //Based on gender, first get HashMap of students (which fit option 2), then save it in another HashMap with gender as key and the
            //previously obtained HashMap as value
            for (String gender : GENDER_LIST) {
                percentageTwoList.put(gender, percentageOneOption(option2, getStudentsByYear(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students (which fit option 2), then save it in another HashMap with school as key and the
            //previously obtained HashMap as value
            for (String school : SCHOOL_LIST) {
                percentageTwoList.put(school, percentageOneOption(option2, getStudentsByYear(school, studentMap)));
            }
        }

        return percentageTwoList;
    }

    public static void percentageAllOptions(String option1, String option2, String option3, HashMap<String, Student> studentMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //returns a hashtable of percentage breakdown in a key value pair
    // key = year, value = percentage
    public static HashMap<String, Double> byYear(HashMap<String, Student> studentMap) {
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
    public static HashMap<String, Double> byGender(HashMap<String, Student> studentMap) {
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
    public static HashMap<String, Double> bySchool(HashMap<String, Student> studentMap) {
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

    public static HashMap<String, Student> getStudentsByYear(String year, HashMap<String, Student> studentMap) {
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
            if (email.contains(year)) {
                if (!studentsByYear.containsKey(student.getMacAddress())) { //why is this required?
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
