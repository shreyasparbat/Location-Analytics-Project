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

/**
 *
 * @author Joleen Mok
 */
public class LocationLookupValidator {

    /**
     * A map of error messages where key is the row and values are the error messages related to the row
     */
    public static HashMap<Integer, List<String>> llErrors = new HashMap<>();

    /**
     * A list of valid location IDs
     */
    public static ArrayList<String> locationIDList = new ArrayList<String>();
    
    /**
     * A list of location levels to check the id of location based on levels
     */
    private static ArrayList<String> locationLevels = new ArrayList<String>();

    static {
        locationLevels.add("10");
        locationLevels.add("20");
        locationLevels.add("30");
        locationLevels.add("40");
        locationLevels.add("50");
    }

    /**
     * Validates the contents of the locationlookup 
     * @param list the contents of the locationlookup.csv
     * @return a list of the processed location lookup data
     */
    public static List<String[]> validateLocationLookup(List<String[]> list) {
        List<String[]> correctList = new ArrayList<>();
        Iterator<String[]> iter = list.iterator();
        locationLevels.clear();
        int index = 1;
        if (iter.hasNext()) {
            iter.next(); //clears buffer   
        }
        while (iter.hasNext()) {
            String[] row = iter.next();
            ArrayList<String> errorMsgs = new ArrayList<>();
            boolean locationIdCheck = true;
            boolean semanticPlaceCheck = true;
            try {
                locationIdCheck = checkLocationID(row[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                locationIdCheck = false;
            }
            try {
                semanticPlaceCheck = checkSemanticPlace(row[1], row[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                semanticPlaceCheck = false;
            }
            if (!locationIdCheck) {
                errorMsgs.add("invalid location id");
            }
            if (!semanticPlaceCheck) {
                errorMsgs.add("invalid semantic place");
            }
            if (locationIdCheck && semanticPlaceCheck) {
                correctList.add(row);
                locationIDList.add(row[0]);
            } else {
                llErrors.put(index, errorMsgs);
            }
            index++;
        }

        return correctList;
    }

    /**
     * validates the location id based on the requirements
     * @param locatID input of locatID
     * @return return true if locatID is valid,else false
     */
    private static boolean checkLocationID(String locatID) {
        try {
            if (locatID.length() != 10) {
                return false;
            }
            int locID = Integer.parseInt(locatID);
            if (locID > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * validates the semantic place based on the requirements
     * @param semanticPlace input of semantic place
     * @param locationID input of location id
     * @return true if the semantic place is valid, else false
     */
    private static boolean checkSemanticPlace(String semanticPlace, String locationID) {
        // if contains other than numbers and alphabets

        if (semanticPlace.indexOf("SMUSISL") == 0 || semanticPlace.indexOf("SMUSISB") == 0) {
            //semantic location places are input as SMUSISL1NearSubway or SMUSISB1NearLiftLobby
            // either way need to verify the level within the string of semantic place B_ or L_ 
            // with locationID
            String level = semanticPlace.substring(7, 8);
            String locationIDlevelCheck = locationID.substring(4, 6);

            if (semanticPlace.indexOf("SMUSISL") == 0) {
                //1010100013 id is coded as 1010 (SIS), 10/20/30/40/50 (level), rest is id

                if (locationLevels.contains(locationIDlevelCheck) && level.equals(locationID.substring(4, 5))) {
                    return true;
                }
            } else {
                //1010110063 is a basement location. 1010(SIS), 11 indicates basement
                //but we care about the second 1

                if (locationIDlevelCheck.equals("11") && level.equals(locationID.substring(5, 6))) {
                    return true;
                }
            }
        }

        return false;
    }
}
