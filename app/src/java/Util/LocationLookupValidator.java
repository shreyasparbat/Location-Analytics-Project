/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Joel Tay
 */
public class LocationLookupValidator {

    public static HashMap<Integer, List<String>> llErrors = new HashMap<>();
    public static ArrayList<String> locationIDList = new ArrayList<String>();

    public static List<String[]> validateLocationLookup(List<String[]> list) {
        List<String[]> correctList = new ArrayList<>();
        Iterator<String[]> iter = list.iterator();
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
                semanticPlaceCheck = checkSemanticPlace(row[1]);
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

    private static boolean checkLocationID(String string) {
        try {
            int locID = Integer.parseInt(string);
            if (locID > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static boolean checkSemanticPlace(String string) {
        return true;
    }
}
