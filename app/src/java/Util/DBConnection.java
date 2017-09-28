/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

//import java.util.ArrayList;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amanda
 */
public class DBConnection {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String dbURL;
    private static String dbUser; //MySQL username
    private static String dbPassword; //MySQL password
    private static final String PROPS_FILENAME = "/connection.properties";

    static {
        readDatabaseProperties();
    }

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Class.forName(JDBC_DRIVER);
        String message = "dbURL: " + dbURL
                + "  , dbUser: " + dbUser
                + "  , dbPassword: " + dbPassword;
        Logger.getLogger(DBConnection.class.getName()).log(Level.INFO, message);

        conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        return conn;
    }

    private static void readDatabaseProperties() {
        InputStream is = null;
        try {
            // Retrieve properties from connection.properties via the CLASSPATH
            // WEB-INF/classes is on the CLASSPATH
            is = DBConnection.class.getResourceAsStream(PROPS_FILENAME);
            Properties props = new Properties();
            props.load(is);

            // load database connection details
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String dbName = props.getProperty("db.name");
            dbUser = props.getProperty("db.user");
            // grab environment variable to check if we are on production environment
            String osName = System.getProperty("os.name");
            if (osName.equals("Linux")) {
                // in production environment, use aws.db.password
                dbPassword = props.getProperty("aws.db.password");
            } else {
                // in local environment, use db.password
                dbPassword = props.getProperty("db.password");
            }

            dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to load '" + PROPS_FILENAME + "'.";

            System.out.println(message);
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, message, ex);
            throw new RuntimeException(message, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.WARNING, "Unable to close connection.properties", ex);
                }
            }
        }
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

    public static void addLoca(String[] arr) throws SQLException, ClassNotFoundException {
        Connection conn = createConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO location VALUES(?, ?,?)");
        stmt.setTimestamp(1, Timestamp.valueOf(arr[0]));
        stmt.setString(2, arr[1]);
        stmt.setInt(3, Integer.parseInt(arr[2]));

        stmt.executeUpdate();
        //out.println("Number of records inserted" + numRecordUpdated);
        stmt.close();
        conn.close();
    }

}
