package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This Class Represent a Player. Every Player has a certain amount of Points
 * and a faction.
 * This Class is also Responsible for the RoadList and the Buildinglist.
 * The Player can add or remove a Road or a Building.
 *
 * @author Dario Zuellig
 * @version 05.12.2019
 *
 */

public class Player {
    
    private final Faction faction;
    private ArrayList<Road> roadList;
    private ArrayList<Settlement> buildingList;
    private int points;
    private ResourceStock resourcestock;
    
    /**
     *
     * In this Constructor every Player will set an Player faction .
     *
     * @param faction is the colour of every Player.
     */
    
    public Player(Faction faction) {
        points = 0;
        roadList = new ArrayList<>();
        buildingList = new ArrayList<>();
        this.faction = faction;
        resourcestock = new ResourceStock();
        
    }
    
    /**
     *
     * In this methode every Player can ask their faction
     *
     * @return It returns the faction from the Player.
     */
    
    public Faction getFaction() {

        return faction;
    }
    
    /**
     * In this method every Player can ask their Roads
     *
     * @return it returns an List with every Road from the Player.
     */
    public List<Road> getRoadList() {
        
        return roadList;
    }
    
    /**
     *
     * In this method the Player can add an Road.
     *
     * @param road is the road that the player can add to the game.
     *
     */
    public void addRoad(Road road) {
        if (road != null) {
            roadList.add(road);
        }
    }
    
    /**
     *
     * In this method the Player can ask their building
     *
     * @return
     * It returns an List with all the Buildings from the Player.
     */
    
    public ArrayList<Settlement> getBuildingList() {

        return buildingList;
    }
    
    /**
     * In this method you can add buildings to the player.
     *
     * @param settlement building to add
     */
    public void addBuilding(Settlement settlement) {
        if (settlement != null) {
            buildingList.add(settlement);
        }
    }
    
    public void removeBuilding(Settlement settlement){

        buildingList.remove(settlement);
    }
    
    /**
     * This Method returns the number of roads of the Player
     * @return the number of roads.
     */
    public int getRoadCount() {

        return roadList.size();
    }
    
    /**
     * This Method is to return the whole ResourceStock
     * @return the {@link ResourceStock} object
     */
    public ResourceStock getResourcestock() {
        return resourcestock;
    }
    
    /**
     * Method to make the player pay one ressource
     * @param resource the resource to pay
     * @return whether the transaction was successful
     */
    public boolean pay(Resource resource){
        
        return getResourcestock().remove(resource);
    }
    
    /**
     * Method to make the player pay
     * @param resource the resource to pay
     * @param amount the amount of ressources to pay
     * @return whether the transaction was successful
     */
    public boolean pay(Resource resource, int amount){
        
        return getResourcestock().remove(resource, amount);
    }
    
    /**
     * Method to make the player pay all the resources in the list
     * @param resourceList list with resources to pay
     * @return whether the transaction was successful
     */
    public boolean pay(List<Resource> resourceList){
        boolean transactionSuccessful = resourcestock.has(resourceList);
        if (transactionSuccessful) {
            for (Resource resource : resourceList) {
                transactionSuccessful = transactionSuccessful && pay(resource);
            }
        }
        return transactionSuccessful;
    }
    
    /**
     * Method to add one resource to the player
     * @param resource the resource to add
     */
    public void earn(Resource resource){
        getResourcestock().add(resource);
    }
    
    /**
     * Method to add resources to the player
     * @param resource the resources to add
     * @param amount the amount of the resource
     */
    public void earn(Resource resource, int amount){
        getResourcestock().add(resource, amount);
    }
    
    /**
     * This method get the Points of the Player
     *
     *
     * @return It return the Points of every Player.
     */
    public int getPoints() {
        return points;
    }
    
    /**
     * In this method its possible the add an amount of Points to the Points that
     * the Player currently holds
     *
     * @param amount of points that the player add.
     */
    public void addPoints(int amount) {
        if (amount > 0) {
            points = points + amount;
        }
    }

    /**
     * In this Method its possible the remove an amount of Points to the Points that
     * the Player currently holds
     * @param amount points that the player will get removed from the currently hold points.
     */
    public  void removePoints(int amount) {
        if (points >= amount){
            points = points - amount;
        }
    }
}

