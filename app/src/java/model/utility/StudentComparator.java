/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.Comparator;
import model.entity.Student;

/**
 *
 * @author Joel Tay
 */
public class StudentComparator implements Comparator<Student> {

     /**
     * Compares two students in terms of their mac-address values
     * @param s1 Student A
     * @param s2 Student B
     * @return an int to determine the value of the difference of two students mac addresses
     */
    public int compare(Student s1, Student s2) {
        return s1.getMacAddress().compareTo(s2.getMacAddress());
    }
}
