/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel Tay
 */
public class LocationValidator {

    /**
     * A map of error messages where key is the row and values are the error
     * messages related to the row
     */
    public static HashMap<Integer, List<String>> locationErrors = new HashMap<>();

    /**
     * Validates the contents of the location.csv file
     *
     * @param list the list of content from the file
     * @return the correct list of content from the file
     */
    public static List<String[]> validateLocation(List<String[]> list) throws ClassNotFoundException, SQLException {
        List<String[]> correctList = new ArrayList<>();
        HashMap<String, String[]> mapCheck = new HashMap<>();
        Iterator<String[]> iter = list.iterator();
        locationErrors.clear();
        int index = 1;
        Connection conn = DBConnection.createConnection();
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
                locationCheck = checkLocation(row[2],conn);
            } catch (ArrayIndexOutOfBoundsException e) {
                locationCheck = false;
            }
            try {
                macAddressCheck = checkMacAddress(row[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                macAddressCheck = false;
            }
            boolean duplicateRow = false; //initially false
            boolean invalidRow = false;
            if (timeCheck && locationCheck && macAddressCheck) {
                String key = row[1] + row[2];
                if (mapCheck.containsKey(key)) {
                    duplicateRow = true;
                } else {

                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM location WHERE macaddress = ? and locationid = ? ");
                    stmt.setString(1, row[1]);
                    stmt.setInt(2, Integer.parseInt(row[2]));
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() == true) {
                        invalidRow = true;
                        duplicateRow = true;
                    }
                    stmt.close();

                }
                if (invalidRow == false) {
                    mapCheck.put(key, row);
                }
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
        conn.close();
        return correctList;

    }

    /**
     * Validates the time format is in TimeStamp format
     *
     * @param time time input in string
     * @return true if the time is valid, else false
     */
    private static boolean checkTime(String time) {
        try {
            Timestamp ts = Timestamp.valueOf(time);

        } catch (IllegalArgumentException e) {
            return false; //means wrong format
        }
        return true; //means correct format

    }

    /**
     * Validates the location according to the format
     *
     * @param location location input
     * @return true if location is valid, false if not
     */
    private static boolean checkLocation(String location, Connection conn) throws SQLException {
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM locationlookup WHERE locationid = " + Integer.parseInt(location));
        ResultSet rs = stmt.executeQuery();
        if (rs.next() == true) {
            return true;
        }
        stmt.close();

        return false;
    }

    /**
     * Validates the MacAddress according to the format
     *
     * @param mcAddress macAddress input
     * @return true if mac address is valid, false if not
     */
    private static boolean checkMacAddress(String mcAdd) {

        return mcAdd.matches("[a-fA-F0-9]{40}");

    }

}
