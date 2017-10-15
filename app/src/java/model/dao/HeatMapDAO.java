/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.utility.DBConnection;

/**
 *
 * @author shrey
 */
public class HeatMapDAO {

    private HashMap<String, Integer> semanticPlaceHeat;
    private Timestamp startDateTime;
    private Timestamp endDateTime;

    public HeatMapDAO(Timestamp startDateTime, Timestamp endDateTime) {
        semanticPlaceHeat = new HashMap<>();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public HashMap<String, Integer> getAllSemanticPlaceHeat() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Getting HashMap of all students in the SIS building during processing window
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select ll.locationid, ll.semanticplace, noOfMacAdd from locationlookup ll "
                    + "left join "
                    + "( "
                    + "    select uniqueMacAddWithMaxTime.latest_time, count(uniqueMacAddWithMaxTime.macaddress) as noOfMacAdd, l.locationid as locid from location l "
                    + "    inner join "
                    + "    ( "
                    + "        select macaddress, max(time) as latest_time "
                    + "        from location "
                    + "        group by macaddress "
                    + ""
                    + "    ) uniqueMacAddWithMaxTime "
                    + "    on l.macaddress = uniqueMacAddWithMaxTime.macaddress "
                    + "    where latest_time >= ? and latest_time < ? "
                    + "    group by l.locationid "
                    + "    order by l.locationid "
                    + ") locIdsOfUniqueMacAddWithMaxTime "
                    + "on ll.locationid = locIdsOfUniqueMacAddWithMaxTime.locid "
                    + "order by ll.locationid");

            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);

            rs = stmt.executeQuery();

            while (rs.next()) {
                //getting query results
                String semanticPlace = rs.getString("semanticplace");
                int noOfMacAdd = rs.getInt("noOfMacAdd"); //NOTE: no need to check for null cause getInt() returns 0 if value in table is NULL

                //check if semanticPlace already exists in the map
                if (semanticPlaceHeat.containsKey(semanticPlace)) {
                    //get the old value, add new value to it then replace in the map
                    int oldValue = semanticPlaceHeat.get(semanticPlace);
                    semanticPlaceHeat.replace(semanticPlace, oldValue + noOfMacAdd);
                } else {
                    //add to map the place and its initial no of MacAddressess
                    semanticPlaceHeat.put(semanticPlace, noOfMacAdd);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return semanticPlaceHeat;
    }
}
