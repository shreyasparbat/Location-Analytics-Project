package model.dao;

import model.entity.Student;
import java.util.*;
import model.*;

/**
 * Allows the application to access the database and execute CRUD Functions
 * involved in the use of Student Objects
 *
 * @author Joel Tay
 */
public class StudentDAO {

    //attributes
    private ArrayList<Student> sList;

    //constructors
    /**
     * Default Constructor
     *
     *
     */
    public StudentDAO() {
        sList = new ArrayList<>();
    }

    //methods
    //setters
    public void setStudent(Student student) {
        sList.add(student);
    }
    
    //getters
    /**
     * Returns a list of all students
     *
     * @return returns an array list of students
     */
    public ArrayList<Student> getAllStudents() {
        return sList;
    }

    //
    /**
     * Returns a student after searching his name in the database
     *
     * @param name - the string of the name
     * @return returns Student object if name is correct. Else returns a null
     * object.
     */
    public Student getStudent(String name) {
        for (Student s : sList) {
            String sName = s.getName();
            if (sName.equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns a list of all students in a Hashtable
     *
     * @return returns a Hashtable of students
     */
    public HashMap<String, Student> getAllStudentsMap() {
        HashMap<String, Student> studentTable = new HashMap<>();
        for (Student s : sList) {
            studentTable.put(s.getMacAddress(), s);
        }
        return studentTable;
    }
}
