package model.entity;

import java.util.ArrayList;

/**
 * A Location object that stores the information about the semanticPlace and
 * locationID
 *
 * @author Joel Tay
 */
public class Location {

    //attributes
    private String semanticPlace;
    private ArrayList<String> studentList;

    //constructors
    /**
     * Constructs a new Location object with the semantic place and initializes
     * an empty list of student.
     *
     * @param semanticPlace String of the semantic location description
     */
    public Location(String semanticPlace) {
        this.semanticPlace = semanticPlace;
        this.studentList = new ArrayList<>();
    }

    //getter
    /**
     * Returns the name of the semantic place
     *
     * @return a string that represents the semantic place of the location
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     * Returns an ArrayList of Student macaddresses located within the location
     *
     * @return an ArrayList that represents macaddresses of students in the location
     */
    public ArrayList<String> getStudents() {
        return studentList;
    }
    
    /**
     * Returns the number of Student macaddresses located within the location
     *
     * @return an Integer that represents the number of macaddresses of students in the location
     */
    public Integer getNumberOfStudents() {
        return studentList.size();
    }
    
    /**
     * Adds a student into the ArrayList of Student macaddresses located within the location
     * @param studentMac macAddress of a student
     * 
     */
    public void addStudent(String studentMac) {
        if(!studentList.contains(studentMac)){
            studentList.add(studentMac);
        }
    }
}
