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
     * It takes in initial time, and processes the end time 15 minutes later.
     * 
     * @param dateTime a string containing 
     * @return an ArrayList of Timestamps where timestamp at index 0 is <code>startDateTime</code> and timestamp at index 1 is <code>endDateTime</code>
     */
    public static ArrayList<Timestamp> getNextProcessingWindow(String dateTime) throws IllegalArgumentException {

        //SQL Timestamp obj for startDateTime of processing window
        Timestamp startDateTime = Timestamp.valueOf(dateTime);

        //getting startDateTime
        int mins = Integer.parseInt(dateTime.substring(14, 16));
        int hrs = Integer.parseInt(dateTime.substring(11, 13));
        int days = Integer.parseInt(dateTime.substring(8, 10));
        int months = Integer.parseInt(dateTime.substring(5, 7));
        int years = Integer.parseInt(dateTime.substring(0, 4));
        
        //finding correct mins, hrs, days, months and years for endDateTime
        for (int i = 0; i < 15; i++) {
            mins++;
            if (mins == 60) {
                mins = 0;
                hrs++;

                if (hrs == 24) {
                    hrs = 0;
                    days++;

                    if (days == 29 && months == 2) {
                        days =1;
                        months++;
                    }else if(days == 31 && (months == 4 ||months == 6 || months == 9 ||months == 11)){
                        days = 1;
                        months++;
                    }else if(days == 32 && months == 13){
                        days = 1;
                        months = 1;
                        years++;
                    }else if (days ==32){
                        days = 1;
                        months++;
                    }
                }
            }
        }

        //forming endDateTime Timestamp object
        Timestamp endDateTime = Timestamp.valueOf(getDateTimeString(mins, hrs, days, months, years));

        //returning 
        ArrayList<Timestamp> processingWindowArrayList = new ArrayList<>();
        processingWindowArrayList.add(startDateTime);
        processingWindowArrayList.add(endDateTime);
        return processingWindowArrayList;
    }
    
    /**
     * Returns an ArrayList of two Timestamp objects based on the dateTime string passed into the function.
     * These Timestamp objects signify the start and end of the processing window.
     * It takes in initial time, and processes the end time 15 minutes before.
     * 
     * @param dateTime a string containing 
     * @return an ArrayList of Timestamps where timestamp at index 0 is <code>startDateTime</code> and timestamp at index 1 is <code>endDateTime</code>
     */
    public static ArrayList<Timestamp> getProcessingWindow(String dateTime) throws IllegalArgumentException {

        //getting startDateTime
        int mins = Integer.parseInt(dateTime.substring(14, 16));
        int hrs = Integer.parseInt(dateTime.substring(11, 13));
        int days = Integer.parseInt(dateTime.substring(8, 10));
        int months = Integer.parseInt(dateTime.substring(5, 7));
        int years = Integer.parseInt(dateTime.substring(0, 4));
        int sec = Integer.parseInt(dateTime.substring(17));
        
        //check time format
        if(hrs<0||hrs>23 ||mins<0||mins>59||sec<0||sec>59){
            throw new IllegalArgumentException();
        }
        
        //valid date month
        switch (months){
            case 2:
                if(years%4==0){ //leap year
                    if(days>29){  // more than 29 days
                        throw new IllegalArgumentException();
                    }
                }else{
                    if(days>28){
                        throw new IllegalArgumentException();
                    }
                }
                break;
                
            case 4: case 6: case 9: case 11:
                if(days>30){
                    throw new IllegalArgumentException();
                }
            break;
        }

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
        
        //SQL Timestamp obj for endDateTime of processing window
        Timestamp endDateTime = Timestamp.valueOf(dateTime);
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
     * @param mins the minutes in int
     * @param hrs the hours in int 
     * @param days the number of days in int
     * @param months the month in int 1-12
     * @param years the year in int
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
