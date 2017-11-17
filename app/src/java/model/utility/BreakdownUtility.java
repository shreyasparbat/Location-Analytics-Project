/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import model.entity.Student;

/**
 *
 * @author Shreyas
 * @author Ming Xuan
 */
public class BreakdownUtility {

    //standard attributes
    private final String[] YEAR_LIST = {"2013", "2014", "2015", "2016", "2017"};
    private final String[] GENDER_LIST = {"M", "F"};
    private final String[] SCHOOL_LIST = {"sis", "law", "accountancy", "economics", "business", "socsc"};
    
    /**
     * Returns a sorted TreeMap based on the option input by the user
     * @param option Choice of breakdown report 
     * @param studentMap The list of students to be broken down
     * @return Treemap containing the inner details
     */
    public TreeMap<String, Integer> percentageOneOption(String option, TreeMap<String, Student> studentMap) {
        TreeMap<String, Integer> percentageOneList = new TreeMap<>();

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
    
    /**
     * Returns a sorted TreeMap based on the two option inputs by the user
     * @param option1 Choice of breakdown report 
     * @param option2 Choice of breakdown report 
     * @param studentMap The list of students to be broken down
     * @return Treemap containing the two options
     */
    public TreeMap<String, TreeMap<String, Integer>> percentageTwoOptions(String option1, String option2, TreeMap<String, Student> studentMap) {
        TreeMap<String, TreeMap<String, Integer>> percentageTwoList = new TreeMap<>();
        
        //get hash map based on first option
        TreeMap<String, Integer> percentageList = percentageOneOption(option1, studentMap);
        Iterator<String> percentageListKeyIterator = percentageList.keySet().iterator();

        if ("year".equals(option1)) {
            //Based on year, first get HashMap of students, then save it in another HashMap with year and that year's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String year = percentageListKeyIterator.next();
                int number = percentageList.get(year);
                //half rounding
                //int rounded = (int) (percentageList.get(year) + 0.5);

                percentageTwoList.put(year + " : " + number, percentageOneOption(option2, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            percentageTwoList = new TreeMap<>(Collections.reverseOrder());
            //Based on gender, first get HashMap of students, then save it in another HashMap with gender and that gender's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String gender = percentageListKeyIterator.next();
                int number = percentageList.get(gender);
                //half rounding
                //int rounded = (int) (percentageList.get(gender) + 0.5);

                percentageTwoList.put(gender + " : " + number, percentageOneOption(option2, getStudentsByGender(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students, then save it in another HashMap with school and that school's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String school = percentageListKeyIterator.next();
                int number = percentageList.get(school);
                //half rounding
                //int rounded = (int) (percentageList.get(school) + 0.5);

                percentageTwoList.put(school + " : " + number, percentageOneOption(option2, getStudentsBySchool(school, studentMap)));
            }
        }

        return percentageTwoList;
    }
    /**
     * Returns a sorted TreeMap based on the three option inputs by the user
     * @param option1 Choice of breakdown report 
     * @param option2 Choice of breakdown report 
     * @param option3 Choice of breakdown report 
     * @param studentMap The list of students to be broken down
     * @return Treemap of all options breakdown
     */
    public TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllOptions(String option1, String option2, String option3, TreeMap<String, Student> studentMap) {
        TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList = new TreeMap<>();

        //get hash map based on first option
        TreeMap<String, Integer> numberList = percentageOneOption(option1, studentMap);
        Iterator<String> percentageListKeyIterator = numberList.keySet().iterator();

        if ("year".equals(option1)) {
            //Based on year, first get HashMap of students, then save it in another HashMap with year and that year's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String year = percentageListKeyIterator.next();
                int number = numberList.get(year);
                percentageAllList.put(year + " : " + number, percentageTwoOptions(option2, option3, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            percentageAllList = new TreeMap<>(Collections.reverseOrder());
            //Based on gender, first get HashMap of students, then save it in another HashMap with gender and that gender's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String gender = percentageListKeyIterator.next();
                int number = numberList.get(gender);
                percentageAllList.put(gender + " : " + number, percentageTwoOptions(option2, option3, getStudentsByGender(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students, then save it in another HashMap with school and that school's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String school = percentageListKeyIterator.next();
                int number = numberList.get(school);
                percentageAllList.put(school + " : " + number, percentageTwoOptions(option2, option3, getStudentsBySchool(school, studentMap)));
            }
        }

        return percentageAllList;
    }
    
    /**
     * Returns a sorted TreeMap based on the year option input, where the year
     * is the Key and the number of Students takes an Integer Value 
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by year
    */
    public TreeMap<String, Integer> byYear(TreeMap<String, Student> studentMap) {
        TreeMap<String, Integer> yearNumber = new TreeMap<>();

        //getting number
        for (String year : YEAR_LIST) {
            TreeMap<String, Student> yearStud = getStudentsByYear(year, studentMap);
            int noStudentsInYear = yearStud.size();
            yearNumber.put(year, noStudentsInYear);
        }

        //returning
        return yearNumber;
    }

    /**
     * Returns a sorted TreeMap based on the gender option input, where the gender
     * is the Key and the number of Students takes an Integer Value 
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by gender
    */
    public TreeMap<String, Integer> byGender(TreeMap<String, Student> studentMap) {
        //initializing hashmap for conversion into raw numbers
        TreeMap<String, Integer> yearNumber = new TreeMap<>(Collections.reverseOrder());
        //getting number of students by gender
        for (String gender : GENDER_LIST) {
            TreeMap<String, Student> genderStud = getStudentsByGender(gender, studentMap);
            int noStudentsInYear = genderStud.size();
            yearNumber.put(gender, noStudentsInYear);
        }

        //returning
        return yearNumber;
    }

    /**
     * Returns a sorted TreeMap based on the school option input, where the school
     * is the Key and the number of Students takes an Integer Value 
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by school
    */
    public TreeMap<String, Integer> bySchool(TreeMap<String, Student> studentMap) {
        //initializing hashmap for conversion into raw numbers
        TreeMap<String, Integer> schoolNumber = new TreeMap<>();
        double totalStudentNo = studentMap.size();

        //getting number of students in school
        for (String sch : SCHOOL_LIST) {
            TreeMap<String, Student> schoolStud = getStudentsBySchool(sch, studentMap);
            int noStudentsInSchool = schoolStud.size();
            schoolNumber.put(sch, noStudentsInSchool);
        }
        //returning
        return schoolNumber;
    }
    
    /**
     * Returns a sorted TreeMap based on the year option input, where the mac-address
     * is the Key and the Student object takes the value
     * @param year a particular admission year
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by year
    */
    public TreeMap<String, Student> getStudentsByYear(String year, TreeMap<String, Student> studentMap) {
        //creates new hashmap
        TreeMap<String, Student> studentsByYear = new TreeMap<>();
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

        //returns new TreeMap
        return studentsByYear;
    }

    /**
     * Returns a sorted TreeMap based on the gender option input, where the mac-address
     * is the Key and the Student object takes the value
     * @param g gender of a student
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by year
    */
    public TreeMap<String, Student> getStudentsByGender(String g, TreeMap<String, Student> studentMap) {
        char requiredGender = g.charAt(0);
        TreeMap<String, Student> studentsByGender = new TreeMap<>();
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

        //returns new TreeMap
        return studentsByGender;
    }

    /**
     * Returns a sorted TreeMap based on the school option input, where the mac-address
     * is the Key and the Student object takes the value
     * @param school a particular school in smu 
     * @param studentMap The list of students to be broken down
     * @return Treemap of students broken down by year
    */
    public TreeMap<String, Student> getStudentsBySchool(String school, TreeMap<String, Student> studentMap) {
        //creates new hashtable
        TreeMap<String, Student> studentsBySchool = new TreeMap<>();
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
    
    /**
     * Enables pretty printing of data for one Option
     * @param percentageOneList The list of students that is already broken down
     * @return Breakdown results to be printed out.
    */
    public ArrayList<String> printBarChart(TreeMap<String, Integer> percentageOneList) throws IllegalArgumentException {

        //strings for charts (innermost layer) to be outputed
        String gsonInnerLabel = "";
        String gsonInnerData = "";

        //getting iterator for inner most map
        Iterator<Integer> innerMapValuesIter = percentageOneList.values().iterator();
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
    
    /**
     * Enables pretty printing of data for one Option
     * @param percentageOneList The list of students that is already broken down
     * @return Breakdown results to be printed out.
    */
    public ArrayList<String> printInner(TreeMap<String, Integer> percentageOneList) throws IllegalArgumentException{
        //ArrayList for output
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<br>");
        
        //getting innerMap keys
        Iterator<String> innerMapValuesIter = percentageOneList.keySet().iterator();
        double denom = getOneOptionDenominator(percentageOneList);
        
        while(innerMapValuesIter.hasNext()){
            String key = innerMapValuesIter.next();
            double value = percentageOneList.get(key);
            //perform half rounding and add to print list
            int percentRounded = (int)((value/denom)*100 + 0.5);
            outputArrayList.add("<li>" + key + " , " + (int)value + " , "+ percentRounded + "%</li>");
        }
        return outputArrayList;
    }
    
    /**
     * Enables pretty printing of data for two Option
     * @param percentageTwoList The list of students that is already broken down
     * @return Breakdown results to be printed out.
    */
    public ArrayList<String> printMiddle(TreeMap<String, TreeMap<String, Integer>> percentageTwoList) throws IllegalArgumentException {
        //ArrayList to be outputed
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<ol>");

        //getting inner maps, which can only be accessed within this while loop
        Iterator<String> middleMapKeysIter = percentageTwoList.keySet().iterator();
        double denom = getTwoOptionDenominator(percentageTwoList);

        while (middleMapKeysIter.hasNext()) {
            //store the key into output list
            String middlekey = middleMapKeysIter.next();
            //get inner map
            TreeMap<String, Integer> innerMap = percentageTwoList.get(middlekey);
            //getting percentage for outer map
            double numer = getNumerator(innerMap);
            int percentRounded = (int) ((numer / denom) * 100 + 0.5);
            outputArrayList.add("<li>" + middlekey + " , " + percentRounded + "%</li>");

            //for inner ordered list
            outputArrayList.add("<ol>");

            //getting iterator for inner most map keys
            Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
            while (innerMapKeysIter.hasNext()) {
                //get innerKey 
                String innerKey = innerMapKeysIter.next();

                //getting innerMapValue
                int innerMapValue = innerMap.get(innerKey);

                //half rounding values and storing them in a new list
                int innerMapPercentageRounded = (int) ((innerMapValue / denom) * 100 + 0.5);

                //store into out put list
                outputArrayList.add("<li type=\"a\">" + innerKey + " : " + innerMapValue + " , " + innerMapPercentageRounded + "% </li>");
            }

            //adding final tag (innerlist)
            outputArrayList.add("</ol>");
        }

        //adding final tag (middle list)
        outputArrayList.add("</ol>");

        //returning
        return outputArrayList;
    }
    
    /**
     * Enables pretty printing of data for three Option
     * @param percentageAllList The list of students that is already broken down
     * @return Breakdown results to be printed out.
    */
    public ArrayList<String> printOuter(TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList) throws IllegalArgumentException {
        //ArrayList to be outputed
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<ol>");

        //getting middle maps, which can only be accessed within this while loop
        Iterator<String> outerMapKeysIter = percentageAllList.keySet().iterator();
        while (outerMapKeysIter.hasNext()) {
            //store the key into output listHashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList
            String outerKey = outerMapKeysIter.next();
            TreeMap<String, TreeMap<String, Integer>> middleMap = percentageAllList.get(outerKey);
            
            //getting percentage value
            double denom = getThreeOptionDenominator(percentageAllList);
            double numer = getTwoOptionDenominator(middleMap);
            int percentage = (int) ((numer / denom) * 100 + 0.5);

            //adding outer tag
            outputArrayList.add("<li>" + outerKey + " , " + percentage + "%</li>");
            
            //adding starting ordered list tag (middle list)
            outputArrayList.add("<ol>");

            //getting inner maps, which can only be accessed within this while loop
            Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();

            while (middleMapKeysIter.hasNext()) {
                //store the key into output list
                String middlekey = middleMapKeysIter.next();
                //get inner map
                TreeMap<String, Integer> innerMap = middleMap.get(middlekey);
                //getting percentage for outer map
                numer = getNumerator(innerMap);

                int percentRounded = (int) ((numer / denom) * 100 + 0.5);
                outputArrayList.add("<li type=\"a\">" + middlekey + " , " + percentRounded + "%</li>");

                //for inner ordered list
                outputArrayList.add("<ol>");

                //getting iterator for inner most map keys
                Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
                while (innerMapKeysIter.hasNext()) {
                    //get innerKey 
                    String innerKey = innerMapKeysIter.next();

                    //get inner map value
                    int innerMapValue = innerMap.get(innerKey);
                    
                    //half rounding values and storing them in a new list
                    int innerMapPercentageRounded = (int) ((innerMapValue / denom) * 100 + 0.5);

                    //store into out put list
                    outputArrayList.add("<li type=\"i\">" + innerKey + " : " + innerMapValue + " , " + innerMapPercentageRounded + "% </li>");
                }

                //adding final tag (innerlist)
                outputArrayList.add("</ol>");
            }
            //adding final tag (middle list)
            outputArrayList.add("</ol>");
        }
        //adding final tag (outer list)
        outputArrayList.add("</ol>");

        //returning
        return outputArrayList;
    }
    
    /**
     * Gets the denominator value to calculate for percentages of all options.
     * @param percentageAllList The list of students that is already broken down
     * @return <code>Double</code> number to calculate percentage value.
    */
    public double getThreeOptionDenominator(TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> percentageAllList) {
        double denominator = 0.0;
        Iterator<String> outerMapKeysIter = percentageAllList.keySet().iterator();
        while (outerMapKeysIter.hasNext()) {
            String outerKey = outerMapKeysIter.next();
            TreeMap<String, TreeMap<String, Integer>> middleMap = percentageAllList.get(outerKey);
            Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();
            while (middleMapKeysIter.hasNext()) {
                String middlekey = middleMapKeysIter.next();
                TreeMap<String, Integer> innerMap = middleMap.get(middlekey);
                Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
                while (innerMapKeysIter.hasNext()) {
                    String innerKey = innerMapKeysIter.next();
                    int value = innerMap.get(innerKey);
                    denominator += value;
                }
            }
        }
        return denominator;
    }

    /**
     * Gets the denominator value to calculate for percentages of two options.
     * @param percentageTwoList The list of students that is already broken down
     * @return <code>Double</code> number to calculate percentage value.
    */
    public double getTwoOptionDenominator(TreeMap<String, TreeMap<String, Integer>> percentageTwoList) {
        double denominator = 0;
        Iterator<String> middleMapKeysIter = percentageTwoList.keySet().iterator();
        while (middleMapKeysIter.hasNext()) {
            String middlekey = middleMapKeysIter.next();
            TreeMap<String, Integer> innerMap = percentageTwoList.get(middlekey);
            Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
            while (innerMapKeysIter.hasNext()) {
                String innerKey = innerMapKeysIter.next();
                int value = innerMap.get(innerKey);
                denominator += value;
            }
        }
        return denominator;
    }
    
    /**
     * Gets the denominator value to calculate for percentages of one option.
     * @param percentageOneList The list of students that is already broken down
     * @return <code>Double</code> number to calculate percentage value.
    */
    public double getOneOptionDenominator(TreeMap<String, Integer> percentageOneList){
        double denominator = 0;
        Iterator<String> innerMapKeysIter = percentageOneList.keySet().iterator();
        while(innerMapKeysIter.hasNext()){
            String key = innerMapKeysIter.next();
            int value = percentageOneList.get(key);
            denominator += value;
        }
        return denominator;
    }
    
    /**
     * Gets the numerator value to calculate for percentages.
     * @param input The list of students that is already broken down
     * @return <code>Double</code> number to calculate percentage value.
    */
    public double getNumerator(TreeMap<String, Integer> input) {
        double numerator = 0;
        Iterator<String> inputIter = input.keySet().iterator();
        while (inputIter.hasNext()) {
            String inputKey = inputIter.next();
            numerator += input.get(inputKey);
        }
        return numerator;
    }
}
