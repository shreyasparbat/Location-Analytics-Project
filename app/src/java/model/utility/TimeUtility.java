/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author shrey
 */
public class TimeUtility {
    
    /**
     * Returns an ArrayList of two Timestamp objects based on the dateTime string passed into the function.
     * These Timestamp objects signify the start and end of the processing window.
     * 
     * @param dateTime a string containing 
     * @return an ArrayList of Timestamps where timestamp at index 0 is <code>startDateTime</code> and timestamp at index 1 is <code>endDateTime</code>
     */
    public static ArrayList<Timestamp> getProcessingWindow(String dateTime) {

        //SQL Timestamp obj for endDateTime of processing window
        Timestamp endDateTime = Timestamp.valueOf(dateTime);

        //getting startDateTime
        int mins = Integer.parseInt(dateTime.substring(14, 16));
        int hrs = Integer.parseInt(dateTime.substring(11, 13));
        int days = Integer.parseInt(dateTime.substring(8, 10));
        int months = Integer.parseInt(dateTime.substring(5, 7));
        int years = Integer.parseInt(dateTime.substring(0, 4));

        //finding correct mins, hrs, days, months and years for startDateTime
        for (int i = 0; i < 15; i++) {
            mins--;
            if (mins == -1) {
                mins = 59;
                hrs--;

                if (hrs == -1) {
                    hrs = 23;
                    days--;

                    if (days == 0) {
                        months--;
                        if (months == 0) {
                            months = 12;
                            years--;
                        }
                        switch (months) {
                            case 1:
                            case 3:
                            case 5:
                            case 7:
                            case 8:
                            case 10:
                            case 12:
                                days = 31;
                                break;
                            
                            case 2:
                                //doesn't account for leap years
                                days = 28;
                                break;
                            
                            default:
                                days = 30;
                                break;
                        }
                    }
                }
            }
        }

        //forming startDateTime Timestamp object
        Timestamp startDateTime = Timestamp.valueOf(getDateTimeString(mins, hrs, days, months, years));

        //returning 
        ArrayList<Timestamp> processingWindowArrayList = new ArrayList<>();
        processingWindowArrayList.add(startDateTime);
        processingWindowArrayList.add(endDateTime);
        return processingWindowArrayList;
    }
    
    /**
     * Returns a string that can be used with <code>Timestamp.valueOf(String)</code> without errors.
     * 
     * @param mins
     * @param hrs
     * @param days
     * @param months
     * @param years
     * @return a string that can be used with <code>Timestamp.valueOf(String)</code> without errors.
     */
    public static String getDateTimeString (int mins, int hrs, int days, int months, int years) {
        //string to be combined later into dateTime
        String minsString = "";
        String hrsString = "";
        String monthsString = "";
        String daysString = "";
        
        //forming valid mins string
        if (mins < 10) {
            minsString += "0" + mins;
        } else {
            minsString += mins;
        }
        
        //forming valid hrs string
        if (hrs < 10) {
            hrsString += "0" + hrs;
        } else {
            hrsString += hrs;
        }

        //forming valid days string
        if (days < 10) {
            daysString += "0" + days;
        } else {
            daysString += days;
        }

        //forming valid months string
        if (months < 10) {
            monthsString += "0" + months;
        } else {
            monthsString += months;
        }
        
        //forming string to return
        String dateTime = years + "-" + monthsString + "-" + daysString + " " + hrsString + ":" + minsString + ":" + "00";
        
        //returning
        return dateTime;
    }
}
