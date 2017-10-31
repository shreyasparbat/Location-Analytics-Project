/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.Comparator;
import model.entity.Location;

/**
 *
 * @author Ming Xuan
 */
public class LocationComparator implements Comparator<Location>{
    /**
     * Compares two students in terms of their mac-address values
     * @param s1 Student A
     * @param s2 Student B
     * @return an int to determine the value of the difference of two students mac addresses
     */
    public int compare(Location l1, Location l2){
        return l2.getNumberOfStudents().compareTo(l1.getNumberOfStudents());
    }
}
