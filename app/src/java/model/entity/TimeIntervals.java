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

    /**
     * Constructor of a time intervals object
     * @param st a timestamp object that refers to the start time
     * @param et a timestamp object that refers to the end time
     */
    public TimeIntervals(Timestamp st, Timestamp et) {
        startTime = st;
        endTime = et;
    }

    /**
     * Returns the start time
     * @return a timestamp object that refers to the start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time
     * @return a timestamp object that refers to the end time
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets a new end time stamp object
     * @param et a timestamp object
     */
    public void setEndTime(Timestamp et) {
        endTime = et;
    }

    /**
     * Returns a timeinterval which show the time overlap else return null
     *
     * @param anotherTI Another timeinterval object of the same location
     * @return TimeInterval object if an overlap exists else null
     */
    public TimeIntervals overlap(TimeIntervals anotherTI) {
        if (anotherTI == null) {
            return null;
        }
        // if b.starttime starts after a.endtime or a.starttime starts after b.endtime [no overlaps]
        if (!(endTime.getTime() <= anotherTI.getStartTime().getTime() || startTime.getTime() >= anotherTI.getEndTime().getTime())) {
            long start = Math.max(startTime.getTime(), anotherTI.startTime.getTime());
            long end = Math.min(endTime.getTime(), anotherTI.endTime.getTime());
            TimeIntervals overlap = new TimeIntervals(new Timestamp(start), new Timestamp(end));
            return overlap;
        }
        return null;
    }

}
