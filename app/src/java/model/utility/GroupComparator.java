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
 * @author Joel Tay
 */

public class GroupComparator implements Comparator<Group> {
    public int compare(Group g1, Group g2){
        Double difference = -(g1.getTotalDuration() - g2.getTotalDuration());
        return difference.intValue();
    }
}
