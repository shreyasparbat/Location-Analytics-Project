package model;
/**
 * An Admin object that stores the information about the admin id and password

 * @author Joel Tay
 */
public class Admin{
    //attributes of Student
    private String login;
    private String password;
    
    
    //constructor for Admin

    /**
     *   Constructs a new Admin object 
     */
    public Admin(){
        password = "shreyasthebest";
        login = "admin";
    }
    
   
    
    //methods

    /**
     * Returns the login name of the admin
     * @return a string that represents admin login
     */
    public String getUser(){
        return login;
    }
    
    /**
     * Returns the password of an admin account
     * @return a string that represents admin password
     */
    public String getPassword(){
        return password;
    }
}