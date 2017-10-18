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
    private final String[] YEAR_LIST = {"2013", "2014", "2015", "2016", "2017"};
    private final String[] GENDER_LIST = {"M", "F"};
    private final String[] SCHOOL_LIST = {"sis", "law", "accountancy", "economics", "business", "socsc"};

    public HashMap<String, Integer> percentageOneOption(String option, HashMap<String, Student> studentMap) {
        HashMap<String, Integer> percentageOneList = new HashMap<>();

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

    public HashMap<String, HashMap<String, Integer>> percentageTwoOptions(String option1, String option2, HashMap<String, Student> studentMap) {
        HashMap<String, HashMap<String, Integer>> percentageTwoList = new HashMap<>();

        //get hash map based on first option
        HashMap<String, Integer> percentageList = percentageOneOption(option1, studentMap);
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

    public HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllOptions(String option1, String option2, String option3, HashMap<String, Student> studentMap) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList = new HashMap<>();

        //get hash map based on first option
        HashMap<String, Integer> numberList = percentageOneOption(option1, studentMap);
        Iterator<String> percentageListKeyIterator = numberList.keySet().iterator();

        if ("year".equals(option1)) {
            //Based on year, first get HashMap of students, then save it in another HashMap with year and that year's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String year = percentageListKeyIterator.next();
                int number = numberList.get(year);
                //half rounding
                //int rounded = (int) (percentageList.get(year) + 0.5);

                percentageAllList.put(year + " : " + number, percentageTwoOptions(option2, option3, getStudentsByYear(year, studentMap)));
            }
        }
        if ("gender".equals(option1)) {
            //Based on gender, first get HashMap of students, then save it in another HashMap with gender and that gender's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String gender = percentageListKeyIterator.next();
                int number = numberList.get(gender);

                //half rounding
                //int rounded = (int) (percentageList.get(gender) + 0.5);
                percentageAllList.put(gender + " : " + number, percentageTwoOptions(option2, option3, getStudentsByGender(gender, studentMap)));
            }
        }
        if ("school".equals(option1)) {
            //Based on school, first get HashMap of students, then save it in another HashMap with school and that school's percentage as key and the
            //previously obtained HashMap as value
            while (percentageListKeyIterator.hasNext()) {
                String school = percentageListKeyIterator.next();
                int number = numberList.get(school);
                //half rounding
                //int rounded = (int) (percentageList.get(school) + 0.5);

                percentageAllList.put(school + " : " + number, percentageTwoOptions(option2, option3, getStudentsBySchool(school, studentMap)));
            }
        }

        return percentageAllList;
    }

    //returns a hashmap of number of students breakdown by year in a key value pair
    // key = year, value = number of students by year
    public HashMap<String, Integer> byYear(HashMap<String, Student> studentMap) {
        HashMap<String, Integer> yearNumber = new HashMap<>();

        //getting number
        for (String year : YEAR_LIST) {
            HashMap<String, Student> yearStud = getStudentsByYear(year, studentMap);
            int noStudentsInYear = yearStud.size();
            yearNumber.put(year, noStudentsInYear);
        }

        //returning
        return yearNumber;
    }

    //returns a hashtable of number of students by gender in a key value pair
    // key = gender, value = number of students
    public HashMap<String, Integer> byGender(HashMap<String, Student> studentMap) {
        //initializing hashmap for conversion into raw numbers
        HashMap<String, Integer> yearNumber = new HashMap<>();
        //getting number of students by gender
        for (String gender : GENDER_LIST) {
            HashMap<String, Student> genderStud = getStudentsByGender(gender, studentMap);
            int noStudentsInYear = genderStud.size();
            yearNumber.put(gender, noStudentsInYear);
        }

        //returning
        return yearNumber;
    }

    //returns a hashtable of number of students breakdown by school in a key value pair
    // key = school, value = number of students by school
    public HashMap<String, Integer> bySchool(HashMap<String, Student> studentMap) {
        //initializing hashmap for conversion into raw numbers
        HashMap<String, Integer> schoolNumber = new HashMap<>();
        double totalStudentNo = studentMap.size();

        //getting number of students in school
        for (String sch : SCHOOL_LIST) {
            HashMap<String, Student> schoolStud = getStudentsBySchool(sch, studentMap);
            int noStudentsInSchool = schoolStud.size();
            schoolNumber.put(sch, noStudentsInSchool);
        }
        //returning
        return schoolNumber;
    }

    public HashMap<String, Student> getStudentsByYear(String year, HashMap<String, Student> studentMap) {
        //creates new hashmap
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
    public HashMap<String, Student> getStudentsByGender(String g, HashMap<String, Student> studentMap) {
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
    public HashMap<String, Student> getStudentsBySchool(String school, HashMap<String, Student> studentMap) {
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

    public ArrayList<String> printBarChart(HashMap<String, Integer> percentageOneList) throws IllegalArgumentException {

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

    public ArrayList<String> printMiddle(HashMap<String, HashMap<String, Integer>> percentageTwoList) throws IllegalArgumentException {
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
            HashMap<String, Integer> innerMap = percentageTwoList.get(middlekey);
            //getting percentage for outer map
            double numer = getNumerator(innerMap);

            int percentRounded = (int) ((numer / denom) * 100 + 0.5);
            outputArrayList.add("<li type=\"a\">" + middlekey + " , " + percentRounded + "%</li>");

            //for inner ordered list
            outputArrayList.add("<ol>");

            //getting iterator for inner most map keys
            Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
            while (innerMapKeysIter.hasNext()) {
                //get innerKey 
                String innerKey = innerMapKeysIter.next();
                //outputArrayList.add(innerKey);w

                //checking if the value is 'NaN'
                int innerMapValue = innerMap.get(innerKey);
                if (Double.isNaN(innerMapValue)) {
                    //throw new IllegalArgumentException();
                }

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

        //returning
        return outputArrayList;
    }

    public ArrayList<String> printOuter(HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList) throws IllegalArgumentException {
        //ArrayList to be outputed
        ArrayList<String> outputArrayList = new ArrayList<>();
        outputArrayList.add("<ol>");

        //getting middle maps, which can only be accessed within this while loop
        Iterator<String> outerMapKeysIter = percentageAllList.keySet().iterator();
        while (outerMapKeysIter.hasNext()) {
            //store the key into output listHashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList
            String outerKey = outerMapKeysIter.next();
            HashMap<String, HashMap<String, Integer>> middleMap = percentageAllList.get(outerKey);
            //getting percentage value
            double denom = getThreeOptionDenominator(percentageAllList);
            double numer = getTwoOptionDenominator(middleMap);
            int percentage = (int) ((numer / denom) * 100 + 0.5);

            outputArrayList.add("<li>" + outerKey + " , " + percentage + "%</li>");
            //getting outputlist of middle and inner maps
            //

            //getting inner maps, which can only be accessed within this while loop
            Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();

            while (middleMapKeysIter.hasNext()) {
                //store the key into output list
                String middlekey = middleMapKeysIter.next();
                //get inner map
                HashMap<String, Integer> innerMap = middleMap.get(middlekey);
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
                    //outputArrayList.add(innerKey);w

                    //checking if the value is 'NaN'
                    int innerMapValue = innerMap.get(innerKey);
                    if (Double.isNaN(innerMapValue)) {
                        //throw new IllegalArgumentException();
                    }

                    //half rounding values and storing them in a new list
                    int innerMapPercentageRounded = (int) ((innerMapValue / denom) * 100 + 0.5);

                    //store into out put list
                    outputArrayList.add("<li type=\"i\">" + innerKey + " : " + innerMapValue + " , " + innerMapPercentageRounded + "% </li>");
                }

                //adding final tag (innerlist)
                outputArrayList.add("</ol>");
            }
        }
        //adding final tag (middle list)
        outputArrayList.add("</ol>");

        //returning
        return outputArrayList;
    }

    public double getThreeOptionDenominator(HashMap<String, HashMap<String, HashMap<String, Integer>>> percentageAllList) {
        double denominator = 0.0;
        Iterator<String> outerMapKeysIter = percentageAllList.keySet().iterator();
        while (outerMapKeysIter.hasNext()) {
            String outerKey = outerMapKeysIter.next();
            HashMap<String, HashMap<String, Integer>> middleMap = percentageAllList.get(outerKey);
            Iterator<String> middleMapKeysIter = middleMap.keySet().iterator();
            while (middleMapKeysIter.hasNext()) {
                String middlekey = middleMapKeysIter.next();
                HashMap<String, Integer> innerMap = middleMap.get(middlekey);
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

    public double getTwoOptionDenominator(HashMap<String, HashMap<String, Integer>> percentageTwoList) {
        double denominator = 0;
        Iterator<String> middleMapKeysIter = percentageTwoList.keySet().iterator();
        while (middleMapKeysIter.hasNext()) {
            String middlekey = middleMapKeysIter.next();
            HashMap<String, Integer> innerMap = percentageTwoList.get(middlekey);
            Iterator<String> innerMapKeysIter = innerMap.keySet().iterator();
            while (innerMapKeysIter.hasNext()) {
                String innerKey = innerMapKeysIter.next();
                int value = innerMap.get(innerKey);
                denominator += value;
            }
        }
        return denominator;
    }

    public double getNumerator(HashMap<String, Integer> input) {
        double numerator = 0;
        Iterator<String> inputIter = input.keySet().iterator();
        while (inputIter.hasNext()) {
            String inputKey = inputIter.next();
            numerator += input.get(inputKey);
        }
        return numerator;
    }
}
