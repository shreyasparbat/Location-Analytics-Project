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
public class AGDStudentComparator implements Comparator<Student>{
    
    public int compare(Student s1, Student s2){
        int difference = s1.getEmail().compareToIgnoreCase(s2.getEmail());
        if(s1.getEmail().equals("")||s2.getEmail().equals("")){
            difference = 0-difference;
            if(difference==0){
                difference = s1.getMacAddress().compareTo(s2.getMacAddress());
            }
        }
        return difference;
    }
}
