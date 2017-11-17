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
import java.util.TreeMap;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.utility.DBConnection;

/**
 *
 * @author shrey
 */
public class HeatMapDAO {

    private TreeMap<String, int[]> semanticPlaceHeat;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private String level;

    /**
     * Initiates a new HeatMapDAO object 
     * @param startDateTime timestamp of a start time
     * @param endDateTime time stamp of a end time
     * @param level level in the sis building
     */
    public HeatMapDAO(Timestamp startDateTime, Timestamp endDateTime, String level) {
        semanticPlaceHeat = new TreeMap<>();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.level = level;
    }

    /**
     * Gets semantic place heat values from specific floor in sis building
     * @return a Tree map where semantic place is the key and an int array where number of mac addresses is the first entry and locaiton id is the second entry
     */
    public TreeMap<String, int[]> getSemanticPlaceHeatFromSpecificFloor() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Getting TreeMap of all students in the SIS building during processing window
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement(
                    "select t3.sp as 'semanticplace', count(t1.macadd) as 'noOfMacAdd', t3.lid2 as 'locationid' "
                    + "from "
                    + "( "
                    + "select macaddress as 'macadd', time as 'ts', locationid as 'lid' "
                    + "from location "
                    + "where time >= ? and time < ? "
                    + ") as t1 "
                    + "inner join "
                    + "( "
                    + "select macaddress  as 'macadd2', max(time) as 'maxts' from location "
                    + "where time >= ? and time < ? "
                    + "group by macadd2"
                    + ") as t2 "
                    + "on t1.macAdd = t2.macadd2 and t1.ts = t2.maxts "
                    + "right outer join "
                    + "( "
                    + "select locationid as 'lid2', semanticplace as sp "
                    + "from locationlookup "
                    + "where semanticplace like ? "
                    + ") as t3 "
                    + "on t1.lid = t3.lid2 "
                    + "group by t3.sp"
            );

            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);
            stmt.setTimestamp(3, startDateTime);
            stmt.setTimestamp(4, endDateTime);
            stmt.setString(5, "%" + level + "%");

            rs = stmt.executeQuery();

            while (rs.next()) {
                //getting query results
                String semanticPlace = rs.getString("semanticplace");
                int[] informationArray = new int[2];
                informationArray[0] = rs.getInt("noOfMacAdd"); //NOTE: no need to check for null cause getInt() returns 0 if value in table is NULL
                informationArray[1] = rs.getInt("locationid");

                //put into toReturnMap
                semanticPlaceHeat.put(semanticPlace, informationArray);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, "Unable to perform request", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationReportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        //returning required treemap
        return semanticPlaceHeat;
    }
}
