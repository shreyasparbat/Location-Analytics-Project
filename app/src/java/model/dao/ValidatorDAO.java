/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import model.utility.DBConnection;
import model.utility.DemographicsValidator;
import model.utility.LocationLookupValidator;
import model.utility.LocationValidator;
import java.util.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel Tay
 */
public class ValidatorDAO {

    HashMap<String, List<String[]>> map = new HashMap<>();

    /**
     * Initiates a new ValidatorDAO object with a hashmap containing file names and values
     * @param map map where key is the document name and value is the contents of the document
     */
    public ValidatorDAO(HashMap<String, List<String[]>> map) {
        this.map = map;
    }

    /**
     * Validates the files in map of the validator 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void validating() throws SQLException, ClassNotFoundException {
        List<String[]> validLocList;
        List<String[]> validDemoList;
        List<String[]> validllList;
        boolean addFile = false; 
        
         List<String[]> llList = map.get("location-lookup.csv");
        if (llList != null) {
            //missing validation
            validllList = LocationLookupValidator.validateLocationLookup(llList);
            DBConnection.addLL(validllList);
            addFile = true;
        }
        List<String[]> demoList = map.get("demographics.csv");
        
        if (demoList != null) {
            //missing validation
            validDemoList = DemographicsValidator.validateDemographic(demoList);

            DBConnection.addDemo(validDemoList, addFile);

        }
        List<String[]> locList = map.get("location.csv");
        if (locList != null) {
            validLocList = LocationValidator.validateLocation(locList);
            DBConnection.addLoca(validLocList, addFile);

        }
    }

}
