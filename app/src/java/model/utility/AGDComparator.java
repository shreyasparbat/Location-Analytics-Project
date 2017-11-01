/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.util.Comparator;
import model.entity.Group;

/**
 *
 * @author Ming Xuan
 */
public class AGDComparator implements Comparator<Group> {
    
    public int compare(Group g1, Group g2){
        int difference = -(g1.getGroup().size() - g2.getGroup().size());
        if (difference == 0){
            Double timeDifference = -(g1.getTotalDuration() - g2.getTotalDuration());
            difference = timeDifference.intValue();
            if(difference ==0){
                difference = g1.compareTo(g2);
            }
        }
        return difference;
    }
}
