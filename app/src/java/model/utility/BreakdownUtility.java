/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

        //get hash map based on first option
        HashMap<String, Double> percentageList = percentageOneOption(option1, studentMap);
        Iterator<String> percentageListKeyIterator = percentageList.keySet().iterator();

        if ("year".equals(option1)) {
            //Based on year, first get HashMap of students, then save it in another HashMap with year and that year's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String year = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(year) + 0.5);

                percentageTwoList.put(year + " : " + rounded + "%", percentageOneOption(option2, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            //Based on gender, first get HashMap of students, then save it in another HashMap with gender and that gender's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String gender = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(gender) + 0.5);

                percentageTwoList.put(gender + " : " + rounded + "%", percentageOneOption(option2, getStudentsByGender(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students, then save it in another HashMap with school and that school's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String school = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(school) + 0.5);

                percentageTwoList.put(school + " : " + rounded + "%", percentageOneOption(option2, getStudentsBySchool(school, studentMap)));
            }
        }

        return percentageTwoList;
    }

    public static HashMap<String, HashMap<String, HashMap<String, Double>>> percentageAllOptions(String option1, String option2, String option3, HashMap<String, Student> studentMap) {
        HashMap<String, HashMap<String, HashMap<String, Double>>> percentageAllList = new HashMap<>();

        //get hash map based on first option
        HashMap<String, Double> percentageList = percentageOneOption(option1, studentMap);
        Iterator<String> percentageListKeyIterator = percentageList.keySet().iterator();

        if ("year".equals(option1)) {
            //Based on year, first get HashMap of students, then save it in another HashMap with year and that year's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String year = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(year) + 0.5);
                
                percentageAllList.put(year + " : " + rounded + "%", percentageTwoOptions(option2, option3, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            //Based on gender, first get HashMap of students, then save it in another HashMap with gender and that gender's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String gender = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(gender) + 0.5);

                percentageAllList.put(gender + " : " + rounded + "%", percentageTwoOptions(option2, option3, getStudentsByGender(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students, then save it in another HashMap with school and that school's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String school = percentageListKeyIterator.next();

                //half rounding
                int rounded = (int) (percentageList.get(school) + 0.5);

                percentageAllList.put(school + " : " + rounded + "%", percentageTwoOptions(option2, option3, getStudentsBySchool(school, studentMap)));
            }
        }

        return percentageAllList;
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

    public static ArrayList<String> printBarChart(HashMap<String, Double> percentageOneList) throws IllegalArgumentException {

        //strings for charts (innermost layer) to be outputed
        String gsonInnerLabel = "";
        String gsonInnerData = "";

        //getting iterator for inner most map
        Iterator<Double> innerMapValuesIter = percentageOneList.values().iterator();
        ArrayList<Integer> innerMapPercentage = new ArrayList<>();
        while (innerMapValuesIter.hasNext()) {

            //checking if the value is 'NaN'
            double innerMapValue = innerMapValuesIter.next();
            if (Double.isNaN(innerMapValue)) {
                throw new IllegalArgumentException();
            }

            //half rounding values and storing them in a new list
            innerMapPercentage.add((int) (innerMapValue + 0.5));
        }

        //Converting to JSON strings
        gsonInnerLabel = new Gson().toJson(percentageOneList.keySet());
        gsonInnerData = new Gson().toJson(innerMapPercentage);

        //returning
        ArrayList<String> toReturnInner = new ArrayList<>();
        toReturnInner.add(gsonInnerLabel);
        toReturnInner.add(gsonInnerData);
        return toReturnInner;
    }

    public static ArrayList<String> printMiddle(HashMap<String, HashMap<String, Double>> percentageTwoList) throws IllegalArgumentException {
        //ArrayList to be outputed
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<ol>");

        //getting inner maps, which can only be accessed within this while loop
        Iterator<String> middleMapKeysIter = percentageTwoList.keySet().iterator();
        while (middleMapKeysIter.hasNext()) {
            //store the key into output list
            String middlekey = middleMapKeysIter.next();
            outputArrayList.add("<li type=\"a\">" + middlekey + "</li>");

            //for inner ordered list
            outputArrayList.add("<ol>");

            //get inner map
            HashMap<String, Double> innerMap = percentageTwoList.get(middlekey);

            //getting iterator for inner most map keys
            Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
            while (innerMapKeysIter.hasNext()) {
                //get innerKey 
                String innerKey = innerMapKeysIter.next();
                //outputArrayList.add(innerKey);

                //checking if the value is 'NaN'
                double innerMapValue = innerMap.get(innerKey);
                if (Double.isNaN(innerMapValue)) {
                    //throw new IllegalArgumentException();
                }

                //half rounding values and storing them in a new list
                int innerMapPercentageRounded = (int) (innerMapValue + 0.5);

                //store into out put list
                outputArrayList.add("<li type=\"i\">" + innerKey + " : " + innerMapPercentageRounded + "%</li>");
            }

            //adding final tag (innerlist)
            outputArrayList.add("</ol>");
        }

        //adding final tag (middle list)
        outputArrayList.add("</ol>");
        
        //returning
        return outputArrayList;
    }
    
    public static ArrayList<String> printOuter(HashMap<String, HashMap<String, HashMap<String, Double>>> percentageAllList) throws IllegalArgumentException {
        //ArrayList to be outputed
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<ol>");
        
        //getting middle maps, which can only be accessed within this while loop
        Iterator<String> outerMapKeysIter = percentageAllList.keySet().iterator();
        while (outerMapKeysIter.hasNext()) {
            //store the key into output list
            String outerkey = outerMapKeysIter.next();
            outputArrayList.add("<li>" + outerkey + "</li>");
            
            //getting outputlist of middle and inner maps
            outputArrayList.addAll(printMiddle(percentageAllList.get(outerkey)));
        }
        
        //adding final tag (middle list)
        outputArrayList.add("</ol>");
        
        //returning
        return outputArrayList;
    }
}