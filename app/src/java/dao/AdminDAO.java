package dao;
import model.*;

/**
 *  Allows the application to access the database and execute CRUD Functions
 * involved in the use of Admin Objects
 * @author Joel Tay
 */
public class AdminDAO{
    private Admin admin;
    
    /**
     *  Default deconstructor
     */
    public AdminDAO(){
        admin = new Admin();
    }
    
    //checks for admin credentials

    /**
     * Returns the value of Administrator check 
     * @param id  the string of the id
     * @return true if admin id matches with input id, else false
     */
    public boolean isAdmin(String id){
        if( id != null){
            if(id.equals(admin.getUser())){
                return true;
            }
        }
        return false;
    
    }
    
    //authenticateAdminPass

    /**
     * Returns the value of the authentication process
     * @param pwd the string of the password
     * @return true if the admin password matches with the input pw, else false
     */
    public boolean authenticateAdminPassword(String pwd){
        if(pwd != null){
            if(admin.getPassword().equals(pwd)){
                return true;
            }
        }
        return false;
    }
}