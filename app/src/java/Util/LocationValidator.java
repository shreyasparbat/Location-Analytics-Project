/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Joel Tay
 */
public class LocationValidator {

    public static HashMap<Integer, List<String>> locationErrors = new HashMap<>();

    public static List<String[]> validateLocation(List<String[]> list) {
        List<String[]> correctList = new ArrayList<>();
        HashMap<String, String[]> mapCheck = new HashMap<>();
        Iterator<String[]> iter = list.iterator();

        int index = 1;
        if (iter.hasNext()) {
            iter.next(); //clears buffer   
        }
        while (iter.hasNext()) {
            ArrayList<String> errorMsgs = new ArrayList<>();
            String[] row = iter.next();
            boolean timeCheck = true;
            boolean locationCheck = true;
            boolean macAddressCheck = true;
            try {
                timeCheck = checkTime(row[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                timeCheck = false;
            }
            try {
                locationCheck = checkLocation(row[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                locationCheck = false;
            }
            try {
                macAddressCheck = checkMacAddress(row[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                macAddressCheck = false;
            }
            boolean duplicateRow = false; //initially false
            if (timeCheck && locationCheck && macAddressCheck) {
                String key = row[1] + row[2];
                if (mapCheck.containsKey(key)) {
                    duplicateRow = true;
                }
                mapCheck.put(key, row);
            }
            if (!timeCheck) {
                errorMsgs.add("invalid timestamp");
            }
            if (!locationCheck) {
                errorMsgs.add("invalid location");
            }
            if (!macAddressCheck) {
                errorMsgs.add("invalid mac address");
            }
            if (duplicateRow) {
                errorMsgs.add("duplicate row");
            }
            if (timeCheck != true || locationCheck != true || macAddressCheck != true || duplicateRow == true) {
                locationErrors.put(index, errorMsgs);
            }
            index++;
        }

        //Once run finish, the mapCheck should contain all the updated and correct rows
        Iterator<String> correctRow = mapCheck.keySet().iterator();
        while (correctRow.hasNext()) {
            correctList.add(mapCheck.get(correctRow.next()));
        }

        return correctList;

    }

    public static boolean checkTime(String time) {
        try {
            Timestamp ts = Timestamp.valueOf(time);

        } catch (IllegalArgumentException e) {
            return false; //means wrong format
        }
        return true; //means correct format

    }

    public static boolean checkLocation(String location) {

        return LocationLookupValidator.locationIDList.contains(location);

    }

    public static boolean checkMacAddress(String mcAdd) {

        return mcAdd.matches("[a-fA-F0-9]{40}");

    }

}
