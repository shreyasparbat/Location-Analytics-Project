/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import Util.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author amanda
 */
public class LoginDAO {
        private AdminDAO adminDAO;
        
        
    public LoginDAO(){
        adminDAO = new AdminDAO();
    }

    public String authenticateUser(String userName, String password) {

        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String userNameDB = "";
        String passwordDB = "";
        String userNameSub = ""; // username from substring 
        int indexAtChar=0;
        
        // uses adminDAO to check for admin validality
        boolean isAdmin = adminDAO.isAdmin(userName,password);
        
        if(isAdmin){
            return "ADMIN";
        }
       
        try {
            con = DBConnection.createConnection(); //establishing connection
            statement = con.createStatement(); //Statement is used to write queries. Read more about it.
            resultSet = statement.executeQuery("select * from demograph "); //Here table name is users and userName,password are columns. fetching all the records and storing in a resultSet.
            while (resultSet.next()) { // Until next row is present otherwise it return false
                
                String email = resultSet.getString("email");
                indexAtChar = email.indexOf("@");
                userNameSub = email.substring(0,indexAtChar);

               // userNameDB = resultSet.getString("name"); //fetch the values present in database
                passwordDB = resultSet.getString("password");
                if (userName.equals(userNameSub) && password.equals(passwordDB)) {
                    return "SUCCESS"; ////If the user entered values are already present in database, which means user has already registered so I will return SUCCESS message.
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Invalid user credentials"; // Just returning appropriate message otherwise
        
       

    }
}
