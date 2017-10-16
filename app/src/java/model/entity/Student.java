package model.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Student object that stores the information about the macAddress, name, password, email and gender
 * @author Joel Tay
 */
public class Student{
    //attributes of Student
    private String macAddress;
    private String name;
    private String email;
    private char gender;
    private HashMap<Integer,TimeIntervalsList> locationTracker;
    //constructor for Student

    /**
     * Constructs a new Student object with the unique macAddress, name, password, email and gender
     * @param macAddress String of mac address of user device
     * @param name String of user name
     * @param password String of intended password
     * @param email String of student email
     * @param gender char of student gender
     */
    public Student(String macAddress, String name, String email, char gender){
        this.macAddress = macAddress;
        this.name = name;
        this.email = email;
        this.gender = gender;
        locationTracker = new HashMap<>();
    }
    
    //getters

    /**
     * Returns the mac address of a student
     * @return a string that represents the macAddress
     */
    public String getMacAddress(){
        return macAddress;
    }

    /**
     * Returns the email of a student account
     * @return a string that represents student email
     */
    public String getEmail(){
        return email;
    }
    
    /**
     * Returns the name of a student account
     * @return a string that represents the name of a student
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the gender of a student account
     * @return a char 'F' for female or 'M' for male
     */
    public char getGender(){
        return gender;
    }
    
    public HashMap getLocationRecords(){
        return locationTracker;
    }
    
    public boolean equals(Student s){
        if(s!=null && s.macAddress==macAddress){
            return true;
        }
        return false;
    }
    //methods

    
}