/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author amanda
 */
public class DBConnection {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String USERNAME = "root"; //MySQL username
    private static final String PASSWORD = ""; //MySQL password
    private static final String NEWURL = DB_URL + "softwareengineering";
    
     public static Connection createConnection() throws SQLException,ClassNotFoundException{
        Connection conn = null;
        Class.forName(JDBC_DRIVER); 
        conn = DriverManager.getConnection(NEWURL, USERNAME, PASSWORD);
        return conn;
    }
   

    public static void addDemo(String[] row) throws SQLException, ClassNotFoundException {
        Connection conn = createConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO DEMOGRAPH(`macaddress`, `name`, `password`, `email`, `gender`)  VALUES(?, ?, ?,?,?)");
        stmt.setString(1, row[0]);
        stmt.setString(2, row[1]);
        stmt.setString(3, row[2]);
        stmt.setString(4, row[3]);
        stmt.setString(5, row[4]);
        stmt.executeUpdate();
        //out.println("Number of records inserted" + numRecordUpdated);
        stmt.close();
        conn.close();
    }

    public static void addLL(String[] row) throws SQLException, ClassNotFoundException {
        Connection conn = createConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO locationlookup VALUES(?, ?)");
        stmt.setInt(1, Integer.parseInt(row[0]));
        stmt.setString(2, row[1]);
        stmt.executeUpdate();
        //out.println("Number of records inserted" + numRecordUpdated);
        stmt.close();
        conn.close();
    }
    
    
   
}
