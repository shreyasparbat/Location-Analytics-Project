/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
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
     * @param bootstrapProcess boolean to check if the process is bootstrap[True] or upload [False]
     * @param conn Connection object
     * @return the correct list of content from the file
     */
    public static List<String[]> validateLocation(List<String[]> list, boolean bootstrapProcess, Connection conn) throws ClassNotFoundException, SQLException {
        List<String[]> correctList = new ArrayList<>();
        HashMap<String, String[]> mapCheck = new HashMap<>();
        Iterator<String[]> iter = list.iterator();
        locationErrors.clear();
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
                locationCheck = checkLocation(row[2], conn, bootstrapProcess);
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
                String key = row[1] + row[0];
                if (mapCheck.containsKey(key)) {
                    duplicateRow = true;
                } else {
                    if (!bootstrapProcess) { //additional step for update
                        PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM location WHERE macaddress = ? and time = ? ");
                        stmt.setString(1, row[1]);
                        stmt.setTimestamp(2, Timestamp.valueOf(row[0]));
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next() == true) {
                            invalidRow = true;
                            duplicateRow = true;
                        }
                        stmt.close();
                    }
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
        /*Iterator<String> correctRow = mapCheck.keySet().iterator();
        while (correctRow.hasNext()) {
            correctList.add(mapCheck.get(correctRow.next()));
        }*/
         //Getting values
        Collection<String[]> listValues = mapCheck.values();
         
        //Creating an ArrayList of values
        correctList = new ArrayList<String[]>(listValues);
        
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
    private static boolean checkLocation(String location, Connection conn, boolean bootstrapProcess) throws SQLException {
        if (bootstrapProcess) {
            if (LocationLookupValidator.locationList.contains(location)) {
                return true;
            }
        } else {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM locationlookup WHERE locationid = " + Integer.parseInt(location));
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == true) {
                return true;
            }
            stmt.close();
        }

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
