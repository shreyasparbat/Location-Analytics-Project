package model.dao;
import model.entity.Student;
import java.util.*;
import model.*;

/**
 * Allows the application to access the database and execute CRUD Functions
 * involved in the use of Student Objects
 * @author Joel Tay
 */
public class StudentDAO {
    //attributes
    private final ArrayList<Student> sList;
    
    //constructors

    /**
     *  Default Constructor
     * 
     * 
     */
    public StudentDAO(){
        sList = new ArrayList<>();
        // hard copy user input
        sList.add(new Student("mac1", "name1", "pass1" , "email1@smu.edu.sg", 'G'));
        sList.add(new Student("mac2", "name2", "pass2" , "email2@smu.edu.sg", 'G'));
        sList.add(new Student("mac3", "name3", "pass3" , "email3@smu.edu.sg", 'G'));
    }
    
    //methods
    //getters

    /**
     * Returns a list of all students
     * 
     * @return returns an array list of students
     */
    public ArrayList<Student> getAllStudents(){
        return sList;
    }
   
    
    //

    /**
     * Returns a student after searching his name in the database
     * @param name - the string of the name
     * @return returns Student object if name is correct. Else returns a null object.
     */
    public Student getStudent(String name){
        for(Student s: sList){
            String sName = s.getName();
            if(sName.equals(name)){
                return s;
            }
        }
        return null;
    }

    public Hashtable<String, Student> getAllStudentsTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}