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
     * A map of error messages where key is the row and values are the error
     * messages related to the row
     */
    public static HashMap<Integer, List<String>> llErrors = new HashMap<>();

    /**
     * A list of location ids that is validated during bootstrap
     */
    public static ArrayList<String> locationList = new ArrayList<String>();

    public static int numDLLRowsValidated;
    

    /**
     * Validates the contents of the locationlookup
     *
     * @param list the contents of the locationlookup.csv
     * @return a list of the processed location lookup data
     */
    public static List<String> validateLocationLookup(List<String[]> list) {
        List<String> correctList = new ArrayList<>();
        numDLLRowsValidated = 0;
        Iterator<String[]> iter = list.iterator();
        llErrors.clear();
        locationList.clear();
        if (iter.hasNext()) {
            iter.next(); //clears buffer   
        }
        int index = 2; // cos of buffer
        while (iter.hasNext()) {
            String[] row = iter.next();
            ArrayList<String> errorMsgs = new ArrayList<>();
            //check row for blanks
            errorMsgs = checkBlanks(row, errorMsgs);
            if (errorMsgs.isEmpty()) {
                boolean locationIdCheck = true;
                boolean semanticPlaceCheck = true;
                try {
                    locationIdCheck = checkLocationID(row[0].trim());
                } catch (ArrayIndexOutOfBoundsException e) {
                    locationIdCheck = false;
                }
                try {
                    semanticPlaceCheck = checkSemanticPlace(row[1].trim());
                } catch (ArrayIndexOutOfBoundsException e) {
                    semanticPlaceCheck = false;
                }
                if (!locationIdCheck) {
                    errorMsgs.add("invalid location id");

                }
                if (!semanticPlaceCheck) {
                    errorMsgs.add("invalid semantic place");
                }
            }

            if (errorMsgs.isEmpty()) {
                String rowData = row[0].trim() + "," + row[1].trim();
                numDLLRowsValidated++;
                correctList.add(rowData);
            } else {
                llErrors.put(index, errorMsgs);
            }
            index++;
        }

        return correctList;
    }

    /**
     * validates the location id based on the requirements, adds the valid
     * locationID into the locationList
     *
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
                locationList.add(locatID);
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
     *
     * @param semanticPlace input of semantic place
     * @return true if the semantic place is valid, else false
     */
    private static boolean checkSemanticPlace(String semanticPlace) {
        // if contains other than numbers and alphabets

        if (semanticPlace.indexOf("SMUSISL") == 0 || semanticPlace.indexOf("SMUSISB") == 0) {
            //semantic location places are input as SMUSISL1NearSubway or SMUSISB1NearLiftLobby
            // either way need to verify the level within the string of semantic place B_ or L_ 
            // with locationID
            String level = semanticPlace.substring(7, 8);
             if(level.matches("[1-5]")){
                return true; 
            }
        }

        return false;
    }

    /**
     * Validates the data row for blanks, will add the error msg of blank into
     * the errorMsgs
     *
     * @param row String[] representing a row of data
     * @param errorMsgs A list of error messages in string
     * @return the error list back
     */
    private static ArrayList<String> checkBlanks(String[] row, ArrayList<String> errorMsgs) {
        try {
            if (row[0].trim().equals("")) {
                errorMsgs.add("blank location-id");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            errorMsgs.add("blank location-id");
        }

        try {
            if (row[1].trim().equals("")) {
                errorMsgs.add("blank semantic place");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            errorMsgs.add("blank semantic place");
        }

        return errorMsgs;
    }
}
