/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 *
 * @author shrey
 */
public class TimeUtility {

    /**
     * Returns an ArrayList of two Timestamp objects based on the dateTime
     * string passed into the function. These Timestamp objects signify the
     * start and end of the processing window. It takes in initial time, and
     * processes the end time 15 minutes later.
     *
     * @param dateTime a string containing dateTime from HTML form
     * @return an ArrayList of Timestamps where 
     * timestamp at index 0 is <code>startDateTime</code> 
     * and timestamp at index 1 is <code>endDateTime</code>
     */
    public static ArrayList<Timestamp> getNextProcessingWindow(String dateTime) throws DateTimeParseException {
        //getting the start date time for the processing window in consideration
        LocalDateTime ldt = LocalDateTime.parse(dateTime);
        Timestamp startDateTime = Timestamp.valueOf(ldt);
        //getting the end date time for the processing window in consideration
        LocalDateTime ldtTwo = ldt.plusMinutes(15);
        Timestamp endDateTime = Timestamp.valueOf(ldtTwo);
        //returning 
        ArrayList<Timestamp> processingWindowArrayList = new ArrayList<>();
        processingWindowArrayList.add(startDateTime);
        processingWindowArrayList.add(endDateTime);
        return processingWindowArrayList;
    }

    /**
     * Returns an ArrayList of two Timestamp objects based on the dateTime
     * string passed into the function. These Timestamp objects signify the
     * start and end of the processing window. It takes in initial time, and
     * processes the end time 15 minutes before.
     *
     * @param dateTime a string containing dateTime from HTML form
     * @return an ArrayList of Timestamps where 
     * timestamp at index 0 is <code>startDateTime</code> 
     * and timestamp at index 1 is <code>endDateTime</code>
     */
    public static ArrayList<Timestamp> getProcessingWindow(String dateTime) throws DateTimeParseException {

        //getting the start date time for the processing window in consideration
        LocalDateTime ldt = LocalDateTime.parse(dateTime);
        Timestamp endDateTime = Timestamp.valueOf(ldt);
        //getting the end date time for the processing window in consideration
        LocalDateTime ldtTwo = ldt.minusMinutes(15);
        Timestamp startDateTime = Timestamp.valueOf(ldtTwo);
        //returning 
        ArrayList<Timestamp> processingWindowArrayList = new ArrayList<>();
        processingWindowArrayList.add(startDateTime);
        processingWindowArrayList.add(endDateTime);
        return processingWindowArrayList;
    }


    public static ArrayList<Timestamp> getJsonProcessingWindow(String date) throws IllegalArgumentException {
        //checking whether patter is correct using regex
        if (date.matches("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d")) {
            //Make dateTime string of correct format
            String dateTime = date.substring(0, 10) + " " + date.substring(11, date.length());

            //get processing window and return it
            return getProcessingWindow(dateTime);
        } else {
            //incorrect pattern    
            throw new IllegalArgumentException();
        }
    }
    
    public static ArrayList<Timestamp> getJsonNextProcessingWindow(String date) throws IllegalArgumentException {
        //checking whether patter is correct using regex
        if (date.matches("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d")) {
            //Make dateTime string of correct format
            String dateTime = date.substring(0, 10) + " " + date.substring(11, date.length());

            //get processing window and return it
            return getNextProcessingWindow(dateTime);
        } else {
            //incorrect pattern    
            throw new IllegalArgumentException();
        }
    }
}
