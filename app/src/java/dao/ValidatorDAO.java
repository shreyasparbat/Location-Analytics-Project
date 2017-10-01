/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import Util.DBConnection;
import Util.DemographicsValidator;
import Util.LocationLookupValidator;
import java.util.*;
import Util.LocationValidator;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel Tay
 */
public class ValidatorDAO {

    HashMap<String, List<String[]>> map = new HashMap<>();

    public ValidatorDAO(HashMap<String, List<String[]>> map) {
        this.map = map;
    }

    public void validating() throws SQLException, ClassNotFoundException {
        List<String[]> validLocList;
        List<String[]> validDemoList;
        List<String[]> validllList;
        List<String[]> demoList = map.get("demographics.csv");
        if (demoList != null) {
            //missing validation
            validDemoList = DemographicsValidator.validateDemographic(demoList);

            DBConnection.addDemo(validDemoList);

        }

        List<String[]> llList = map.get("location-lookup.csv");
        if (llList != null) {
            //missing validation
            validllList = LocationLookupValidator.validateLocationLookup(llList);
            DBConnection.addLL(validllList);

        }

        List<String[]> locList = map.get("location.csv");
        if (locList != null) {
            validLocList = LocationValidator.validateLocation(locList);

            DBConnection.addLoca(validLocList);

        }
    }

}
