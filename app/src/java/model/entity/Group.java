/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Amanda
 */
public class Group {
    private ArrayList<Student> sGroup;
    private HashMap<Integer,TimeIntervalsList> locList;
    
    public Group(){
        sGroup = new ArrayList<>();
        locList = new HashMap<>();
    }
    
    public Group(ArrayList<Student> studentList, HashMap<Integer,TimeIntervalsList> locaList ){
        sGroup = studentList;
        locList = locaList;
    }
    
    /**
     * Adds a new student into the group
     * @param s
     */
    public void addStudent(Student s){
        if (!sGroup.contains(s)){
            sGroup.add(s);
        }
    }
    
    /**
     * Sets a list of students in the group
     * @param sList
     */
    public void setStudents(ArrayList<Student> sList){
        sGroup = sList;
    }
    
    /**
     *  Sets a new record list to the group details
     * @param locaList
     */
    public void setLocaList(HashMap<Integer,TimeIntervalsList> locaList){
        locList = locaList;
    }
    
    /**
     * Adds a new location with time interval lists into the group details
     * @param locationID
     * @param list
     */
    public void addLocation(int locationID, TimeIntervalsList list){
        locList.put(locationID, list);
    }
    
    /**
     * Returns the list of students in the group
     * @return
     */
    public ArrayList<Student> getGroup(){
        return sGroup;
    }
    
    /**
     * Returns the location records of the group
     * @return
     */
    public HashMap<Integer,TimeIntervalsList> getRecord(){
        return locList;
    }
    
    /**
     * Checks whether group contains minimally one member from another group
     * @param g Another group list
     * @return true if at least one member is presented in both groups, false if otherwise
     */
    public boolean contains(Group g){
        for(Student s : sGroup){
            for(Student s1 : g.sGroup){
                if(s.equals(s1)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Adds students of another group into the current group. Group size doesnt increase if group is a subset of current group
     * @param g
     */
    public void addGroup(Group g){
        for(Student s : g.sGroup){
            addStudent(s);
        }
    }
}
