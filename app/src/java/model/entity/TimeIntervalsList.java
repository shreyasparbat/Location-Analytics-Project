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

    public TimeIntervalsList() {
        intervalList = new ArrayList<>();
        duration = 0;
    }

    public void addTimeInterval(TimeIntervals ti) {
        intervalList.add(ti);
        duration += (ti.getEndTime().getTime() - ti.getStartTime().getTime()) / 60000.0; // (milliseconds to minutes)
    }

    //include method such as calculating the duration of time spent
    public ArrayList<TimeIntervals> getList() {
        return intervalList;
    }

    public double getDuration() {
        return duration;
    }

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
