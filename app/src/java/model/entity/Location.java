package model.entity;

/**
 * A Location object that stores the information about the semanticPlace and
 * locationID
 *
 * @author Joel Tay
 */
public class Location {

    //attributes
    private String semanticPlace;
    private int locationId;
    private String level;

    //constructors
    /**
     * Constructs a new Location object with the unique location id and semantic
     * place description
     *
     * @param semanticPlace String of the semantic location description
     * @param locationId int of the location id of a semantic location
     */
    public Location(String semanticPlace, int locationId) {
        this.semanticPlace = semanticPlace;
        this.locationId = locationId;

        //getting level
        level = semanticPlace.substring(6, 8);
    }

    //getter
    /**
     * Returns the name of the semantic place
     *
     * @return a string that represents the semantic place of the location
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     * Returns the location id of a semantic place
     *
     * @return an int that represents the unique location id of a semantic place
     */
    public int getLocationId() {
        return locationId;
    }

    /**
     * Returns the level of a semantic place
     *
     * @return a string that represents the level of a semantic place
     */
    public String getLevel() {
        return level;
    }
}
