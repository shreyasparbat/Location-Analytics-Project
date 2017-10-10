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
     * Initiates a new ValidatorDAO object with a hashmap containing file names
     * and values
     *
     * @param map map where key is the document name and value is the contents
     * of the document
     */
    public ValidatorDAO(HashMap<String, List<String[]>> map) {
        this.map = map;
    }

    /**
     * Validates the files in map of the validator
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void validating() throws SQLException, ClassNotFoundException {
        List<String[]> validLocList;
        List<String[]> validDemoList;
        List<String[]> validllList;
        // checks if process is a bootstrap or update; dependent on the location-lookup file
        boolean bootstrapProcess = false;

        if (map.containsKey("location-lookup.csv")) {
            //missing validation
            List<String[]> llList = map.get("location-lookup.csv");

            validllList = LocationLookupValidator.validateLocationLookup(llList);
            DBConnection.addLL(validllList);
            bootstrapProcess = true;
        }
        if (map.containsKey("demographics.csv")) {
            //missing validation
            List<String[]> demoList = map.get("demographics.csv");
            validDemoList = DemographicsValidator.validateDemographic(demoList);
            DBConnection.addDemo(validDemoList, bootstrapProcess);

        }

        if (map.containsKey("location.csv")) {
            List<String[]> locList = map.get("location.csv");
            validLocList = LocationValidator.validateLocation(locList,bootstrapProcess);
            DBConnection.addLoca(validLocList, bootstrapProcess);

        }
    }

}
