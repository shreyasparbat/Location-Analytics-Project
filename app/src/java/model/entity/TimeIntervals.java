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

    public TimeIntervals(Timestamp st, Timestamp et) {
        startTime = st;
        endTime = et;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp et) {
        endTime = et;
    }

    /**
     * Returns a timeinterval which show the time overlap else return null
     *
     * @param anotherTI
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
