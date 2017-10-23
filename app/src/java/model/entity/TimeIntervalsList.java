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
    private double duration;

    /**
     *  Creates a new TimeIntervalList object with an empty arraylist of TimeIntervals and a duration of 0
     * 
     */
    public TimeIntervalsList() {
        intervalList = new ArrayList<>();
        duration = 0;
    }

    /**
     * Adds a new time intervals object into the list, adds the duration of the time interval object into the total duration 
     * @param ti a timeinterval object of a particular location
     */
    public void addTimeInterval(TimeIntervals ti) {
        intervalList.add(ti);
        duration += (ti.getEndTime().getTime() - ti.getStartTime().getTime()) / 1000.0; // (milliseconds to minutes)
    }

    //include method such as calculating the duration of time spent

    /**
     * Gets the list of time interval objects
     * @return an arraylist of time interval objects
     */
    public ArrayList<TimeIntervals> getList() {
        return intervalList;
    }

    /**
     * Gets the total duration of all the time intervals
     * @return time in seconds
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Process two time intervals lists and returns a time interval list of overlaps
     * @param anotherList another list of time intervals
     * @return a time interval list object which represents all the time overlaps
     */
    public TimeIntervalsList getOverlaps(TimeIntervalsList anotherList) {
        TimeIntervalsList toReturn = new TimeIntervalsList();
        if (anotherList != null) {
            for (TimeIntervals ti : intervalList) {
                for (TimeIntervals anotherTI : anotherList.intervalList) {
                    TimeIntervals overlaps = ti.overlap(anotherTI); // object or  null
                    if (overlaps != null) {
                        toReturn.addTimeInterval(overlaps);
                    }
                }
            }
        }

        return toReturn;
    }
}
