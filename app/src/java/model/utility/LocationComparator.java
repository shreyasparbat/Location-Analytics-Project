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
     * Compares two location in terms of their student count and semantic place
     * @param s1 location l1
     * @param s2 location l2
     * @return an int to determine the value of the difference of two location
     */
    public int compare(Location l1, Location l2){
        int difference= l2.getNumberOfStudents().compareTo(l1.getNumberOfStudents());
        if(difference == 0){
            difference = l1.getSemanticPlace().compareTo(l2.getSemanticPlace());
        }
        return difference;
    }
}
