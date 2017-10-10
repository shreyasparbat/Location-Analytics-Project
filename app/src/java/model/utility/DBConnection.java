/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

//import java.util.ArrayList;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
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
    private static String osName = System.getProperty("os.name");
    //private static final String COMMA_DELIMITER = ",";
    // private static String newLineSeparator = "\r\n";
    //private static String pathName = "C:/Windows/Temp";

    static {
        readDatabaseProperties();
        //if (osName.equals("Linux")){
        //newLineSeparator = "\n";
        //pathName = "/tmp";
        //  }
    }

    /**
     * creates connection to the database
     *
     * @return Connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

    /**
     * Reads the database properties from the properties file
     */
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

    /**
     * close the given connection, statement and resultset
     *
     * @param conn the connection object to be closed
     * @param stmt the statement object to be closed
     * @param rs the resultset object to be closed
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.WARNING,
                    "Unable to close ResultSet", ex);
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.WARNING,
                    "Unable to close Statement", ex);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.WARNING,
                    "Unable to close Connection", ex);
        }
    }

    /**
     * Bootstraps demographics data into the database under the table
     * demographics
     *
     * @param contents the contents of the demographic
     * @param bootstrap whether process is a bootstrap or not
     * @param conn Connection object
     * @throws SQLException SQL exception to database
     * @throws ClassNotFoundException
     */
    public static void addDemo(List<String[]> contents, boolean bootstrap, Connection conn) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt;
        if (bootstrap == true) {
            stmt = conn.prepareStatement("TRUNCATE TABLE demograph;");
            stmt.executeUpdate();
        }
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("INSERT INTO DEMOGRAPH(`macaddress`, `name`, `password`, `email`, `gender`)  VALUES(?, ?, ?,?,?)");
        int index = 1;
        for (String[] row : contents) {
            stmt.setString(1, row[0]);
            stmt.setString(2, row[1]);
            stmt.setString(3, row[2]);
            stmt.setString(4, row[3]);
            stmt.setString(5, row[4]);
            stmt.addBatch();
            if (index == 500) {
                stmt.executeBatch();
                stmt.clearBatch();
                index = 1;
            }
        }

        stmt.executeBatch();
        stmt.clearBatch();
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 1;");
        stmt.execute();
        stmt.close();
    }

    /**
     * Bootstraps Location data into the database under the table locationlookup
     *
     * @param contents the contents of the locationlookup.csv
     * @param conn Connection object
     * @throws SQLException SQL exception to database
     * @throws ClassNotFoundException
     */
    public static void addLL(List<String[]> contents, Connection conn) throws SQLException, ClassNotFoundException {        
        PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE locationlookup;");
        stmt.executeUpdate();
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("INSERT INTO locationlookup VALUES(?, ?)");
        int index = 1;
        for (String[] row : contents) {
            stmt.setInt(1, Integer.parseInt(row[0]));
            stmt.setString(2, row[1]);
            stmt.addBatch();
            index++;
            if (index == 500) {
                stmt.executeBatch();
                stmt.clearBatch();
                index = 1;
            }
        }

        stmt.executeBatch();
        stmt.clearBatch();
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 1;");
        stmt.execute();
        stmt.close();
    }

    /**
     * Bootstraps location data into the database under the table location
     *
     * @param contents the contents of the location
     * @param conn Connection object
     * @throws SQLException SQL exception to database
     * @throws ClassNotFoundException
     */
    public static void addLoca(List<String[]> contents, boolean bootstrap, Connection conn) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt;
        if (bootstrap == true) {
            stmt = conn.prepareStatement("TRUNCATE TABLE location;");
            stmt.executeUpdate();
        }
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 0;");
        stmt.execute();
        stmt = conn.prepareStatement("INSERT INTO location VALUES(?, ?,?)");
        int index = 1;
        for (String[] arr : contents) {
            stmt.setTimestamp(1, Timestamp.valueOf(arr[0]));
            stmt.setString(2, arr[1]);
            stmt.setInt(3, Integer.parseInt(arr[2]));
            stmt.addBatch();
            index++;
            if (index == 500) {
                stmt.executeBatch();
                stmt.clearBatch();
                index = 1;
            }
        }

        stmt.executeBatch();
        stmt.clearBatch();
        stmt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
        stmt.execute();
        stmt = conn.prepareStatement("SET UNIQUE_CHECKS = 1;");
        stmt.execute();
        stmt.close();
    }

}
