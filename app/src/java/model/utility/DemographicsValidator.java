/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.*;

/**
 *
 * @author Joel Tay
 */
public class DemographicsValidator {

    /**
     * A map of error messages where key is the row and values are the error
     * messages related to the row
     */
    public static HashMap<Integer, List<String>> demographErrors = new HashMap<>();
    /**
     * A list of school email to verify
     */
    private static ArrayList<String> schEmailList = new ArrayList<String>();

    static {
        schEmailList.add("@sis.smu.edu.sg");
        schEmailList.add("@socsc.smu.edu.sg");
        schEmailList.add("@business.smu.edu.sg");
        schEmailList.add("@accountancy.smu.edu.sg");
        schEmailList.add("@economics.smu.edu.sg");
        schEmailList.add("@law.smu.edu.sg");

    }

    /**
     * Validates the contents of the demographics
     *
     * @param list initial data of the demographics data
     * @return correctList the correct form of data after validation
     */
    public static List<String[]> validateDemographic(List<String[]> list) {
        List<String[]> correctList = new ArrayList<>();
        Iterator<String[]> iter = list.iterator();
        demographErrors.clear();
        int index = 1;
        if (iter.hasNext()) {
            iter.next(); //clears buffer   
        }
        while (iter.hasNext()) {
            ArrayList<String> errorMsgs = new ArrayList<>();

            String[] row = iter.next();
            boolean macAddressCheck = true;
            boolean passwordCheck = true;
            boolean emailCheck = true;
            boolean genderCheck = true;

            try {
                macAddressCheck = checkMacAddress(row[0].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                macAddressCheck = false;
            }
            try {
                passwordCheck = checkPassword(row[2].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                passwordCheck = false;
            }
            try {
                emailCheck = checkEmail(row[3].trim(), row[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                emailCheck = false;
            }
            try {
                genderCheck = checkGender(row[4].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                genderCheck = false;
            }

            if (macAddressCheck && passwordCheck && emailCheck && genderCheck) {
                correctList.add(row);
            }
            if (!macAddressCheck) {
                errorMsgs.add("invalid mac address");
            }
            if (!passwordCheck) {
                errorMsgs.add("invalid password");
            }
            if (!emailCheck) {
                errorMsgs.add("invalid email");
            }
            if (!genderCheck) {
                errorMsgs.add("invalid gender");
            }
            if (macAddressCheck != true || passwordCheck != true || emailCheck != true || genderCheck != true) {
                demographErrors.put(index, errorMsgs);
            }
            index++;

        }

        return correctList;
    }

    /**
     * Validates the checkMacAddress according to the format
     *
     * @param mcAddress macAddress input
     * @return true if mac address is valid, false if not
     */
    private static boolean checkMacAddress(String mcAddress) {
        return mcAddress.matches("[a-fA-F0-9]{40}");
    }

    /**
     * Validates the checkPassword according to the requirements
     *
     * @param password password input
     * @return true if password fulfills requirements, false if it doesnt
     */
    private static boolean checkPassword(String password) {
        return (!(password.length() < 8) && !(password.contains(" ")));
    }

    /**
     * Validates the email to see if it's valid
     *
     * @param email Email input
     * @param name name of student
     * @return true if email is valid, false if email is invalid
     */
    private static boolean checkEmail(String email, String name) {
        boolean isStudentValid = true;
        boolean isSchoolValid = true;
        boolean isYearValid = true;
        try {

            int atColumn = email.indexOf("@");
            if (atColumn == -1) {
                return false;
            }
            String schEmail = email.substring(atColumn);
            isSchoolValid = schEmailList.contains(schEmail);
            String schEmailName = email.substring(0, atColumn);
            String schEmailYear = schEmailName.substring(schEmailName.lastIndexOf(".") + 1);
            int year = Integer.parseInt(schEmailYear);
            isYearValid = (year >= 2013 && year <= 2017);
            String emailName = schEmailName.substring(0, schEmailName.lastIndexOf("."));
            name = name.replaceAll(" ", ".").toLowerCase();
            isStudentValid = emailName.equals(name);

        } catch (NullPointerException e) {
            return false;
        }
        if (!isStudentValid || !isSchoolValid || !isYearValid) {
            return false;
        }
        return true;
    }

    /**
     * Validates the gender if it's 'M' or 'F'
     *
     * @param gender gender input
     * @return true if gender fulfills the requirements, false if not
     */
    private static boolean checkGender(String gender) {
        gender = gender.toUpperCase();
        return (gender.equals("M") || gender.equals("F"));
    }
}
