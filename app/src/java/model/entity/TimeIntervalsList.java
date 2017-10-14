/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.util.ArrayList;

/**
 *
 * @author amanda
 */
public class TimeIntervalsList {

    private ArrayList<TimeIntervals> intervalList;

    public TimeIntervalsList() {
        intervalList = new ArrayList<>();
    }

    public void addTimeInterval(TimeIntervals t) {
        intervalList.add(t);
    }
    
    //include method such as calculating the duration of time spent
    
    public ArrayList<TimeIntervals> getList(){
        return intervalList;
    }
}
