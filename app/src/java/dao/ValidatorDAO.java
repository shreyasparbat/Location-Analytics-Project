/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import Util.DBConnection;
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

    public void validating() {
        List<String[]> validLocList;
        List<String[]> validDemoList;
        List<String[]> validllList;
        List<String[]> demoList = map.get("demographics.csv");
        if (demoList != null) {
            //missing validation
            for (String[] row : demoList) {
                try {
                    DBConnection.addDemo(row);
                } catch (SQLException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        List<String[]> llList = map.get("location-lookup.csv");
        if (llList != null) {
            //missing validation
            for (String[] row : llList) {
                try {
                    DBConnection.addLL(row);
                } catch (SQLException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        List<String[]> locList = map.get("location.csv");
        if (locList != null) {
            validLocList = LocationValidator.validateLocation(locList);
            for (String[] row : validLocList) {
                try {
                    DBConnection.addLoca(row);
                } catch (SQLException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ValidatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
