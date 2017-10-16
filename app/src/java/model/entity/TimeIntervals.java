/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.sql.Timestamp;

/**
 *
 * @author amanda
 */
public class TimeIntervals {
    private Timestamp startTime;
    private Timestamp endTime;
    
    public TimeIntervals(Timestamp st, Timestamp et){
        startTime = st;
        endTime = et;
    }
    
    public Timestamp getStartTime(){
        return startTime;
    }
    
    public Timestamp getEndTime(){
        return endTime;
    }
    
    public void setEndTime(Timestamp et){
        endTime = et;
    }
    
    
}
