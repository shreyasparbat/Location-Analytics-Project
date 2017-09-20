package model;

/**
 * A Location object that stores the information about the semanticPlace and locationID
 * @author Joel Tay
 */
public class Location{
    //attributes
    private String semanticPlace;
    private int locationId;
    
    //constructors

    /**
     *  Constructs a new Location object with the unique location id and semantic place description
     * @param semanticPlace String of the semantic location description
     * @param locationId int of the location id of a semantic location
     */
    public Location(String semanticPlace, int locationId){
        this.semanticPlace = semanticPlace;
        this.locationId = locationId;
    }
    
    //getter

    /**
     * Returns the name of the semantic place 
     * @return a string that represents the semantic place of the location
     */
    public String getSemanticPlace(){
        return semanticPlace;
    }
    
    /**
     * Returns the location id of a semantic place
     * @return an int that represents the unique location id of a semantic place
     */
    public int getLocationId(){
        return locationId;
    }
}