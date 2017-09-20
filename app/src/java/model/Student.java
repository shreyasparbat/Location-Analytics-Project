package model;

/**
 * A Student object that stores the information about the macAddress, name, password, email and gender
 * @author Joel Tay
 */
public class Student{
    //attributes of Student
    private String macAddress;
    private String name;
    private String password;
    private String email;
    private char gender;
    
    //constructor for Student

    /**
     * Constructs a new Student object with the unique macAddress, name, password, email and gender
     * @param macAddress String of mac address of user device
     * @param name String of user name
     * @param password String of intended password
     * @param email String of student email
     * @param gender char of student gender
     */
    public Student(String macAddress, String name, String password, String email, char gender){
        this.macAddress = macAddress;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
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
     *  Returns the password of a student account
     * @return a string that represents the password
     */
    public String getPassword(){
        return password;
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
    
    //methods

    
}