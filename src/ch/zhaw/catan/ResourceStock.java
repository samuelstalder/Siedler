package ch.zhaw.catan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.zhaw.catan.Config.Resource;

/**
 *
 * The Class Resource Stock Represent a Stock full of Resources. It checks if an
 * amount of specific Resource is in the Stock. And in this Class its possible
 * to remove or add a Resource to the Stock
 *
 * @author Dario Zuellig
 * @version 05.12.2019
 *
 */

public class ResourceStock {
    
    private HashMap<Resource, Integer> resourceIntegerMap;
    Random random = new Random();

    /**
     *  This Constructor initialise a resourceIntegerMap.
     * To save all the resources in a Map
     *
     */

    public ResourceStock() {
        resourceIntegerMap = new HashMap<>();
    }

    /**
     *This Constructor creates an HashMap with the Resource as a Key and an Integer for the Value.
     *
     * @param initialResourceIntegerMap the resources to initialize the stock with.
     */
    
    public ResourceStock(Map<Config.Resource, Integer> initialResourceIntegerMap) {
        this();
        for (Map.Entry<Config.Resource, Integer> entry : initialResourceIntegerMap.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Creates a ResourceStock object with a list of resource to initialize the stock with
     * @param resourceList list of resources
     */
    public ResourceStock(List<Resource> resourceList){
        this();
        add(resourceList);
    }
    
    /**
     * This Class is Responsible to check if an amount of certain Resource is in the
     * Stock. The Parameter amount Represent the amount which you want to check if
     * the resource is in the Stock.
     *
     * @param resource to check if it is in the list
     * @param amount to check if a certain amount is in the List.
     * @return If the Certain amount of the specific Resource is in the Stock it
     *         return true if not it returns false.
     */
    public boolean has(Config.Resource resource, int amount) {
        
        boolean result = false;
        
        if (resourceIntegerMap.getOrDefault(resource, 0) >= amount) {
            result = true;
        }
        return result;
    }
    
    /**
     * Method to check if all the resources in a list are in stock
     * @param resourceList list to be checked
     * @return if the certain amount of the specific resources is in stock
     */
    public boolean has(List<Resource> resourceList){
        boolean isInStock = true;
        ResourceStock stockClone = new ResourceStock(resourceIntegerMap);
        for (Resource resource : resourceList) {
            if (!stockClone.remove(resource, 1)) {
                isInStock = false;
            }
        }
        return isInStock;
    }
    
    /**
     * With this method its posible to add an resource in the Stock.
     *
     * @param resource resource to be added
     */
    public void add(Config.Resource resource) {
        add(resource, 1);
    }
    
    /**
     * This method is to Add an certain amount of specific Resources in the Stock
     *
     *
     * @param resource To add in the Resourcestock
     * @param amount of Resources to add in the Resourcestock
     */
    
    public void add(Config.Resource resource, int amount) {
        
        if (amount > 0 && resource != null) {
            resourceIntegerMap.put(resource, amount + resourceIntegerMap.getOrDefault(resource, 0));
        }
    }
    
    /**
     * Method to add all resources in a list to the stock
     * @param resourceList resources to add
     */
    public void add(List<Resource> resourceList){
        for (Resource resource : resourceList){
            add(resource);
        }
    }
    
    /**
     * This method removes one element of a specific resource
     * @param resource resource that should be removed
     * @return true if the removing was successful
     */
    public boolean remove(Config.Resource resource){
        return remove(resource, 1);
    }
    
    /**
     * This method is to remove a certain amount of specific resources in the
     * resourceStock
     *
     * @param resource resource that should be removed
     * @param amount   how much should be removed
     * @return true if the removing was successful
     */
    public boolean remove(Config.Resource resource, int amount) {
        boolean result = has(resource, amount);
        if (result) {
            
            resourceIntegerMap.put(resource, resourceIntegerMap.get(resource) - amount);
        }
        return result;
    }
    
    /**
     * Get's the amount of a resource in the resourceStock
     *
     * @param resource resource to check
     * @return amount of specified resource
     */
    public int get(Config.Resource resource) {
        return resourceIntegerMap.getOrDefault(resource, 0);
    }
    
    /**
     * with this methode its possible to have a overview which Resources are in the Stock.
     * 
     * 
     * @return
     * It returns an Integer with the amount of Resources in the Stock.
     */
    
    public int total() {
        
        int sum = 0;
        
        for (Map.Entry<Config.Resource, Integer> entry : resourceIntegerMap.entrySet()) {
            
            sum = sum + entry.getValue();
        }
        
        return sum;
    }
    /**
     * 
     * This method removes a Random Resource from the ResourceList of the Player.
     * 
     * 
     * @param amount amount of resources to be removed
     * @return It returns a List with the random removed Resources.
     */
    public List<Resource> removeRandomResources(int amount){
        List<Resource> removedResources = new ArrayList<>();
        while (amount > 0 && amount <= total()){
            int index = random.nextInt(resourceIntegerMap.size());
            Resource resource = (Resource) resourceIntegerMap.keySet().toArray()[index];
            if (remove(resource)) {
                removedResources.add(resource);
                amount--;
            }
        }
        return removedResources;
    }
    
    /**
     * Method for testing purposes
     * removes all entries from the stock
     */
    public void clearStock() {
        resourceIntegerMap.clear();
    }
    
    /**
     * Prints the resourceStock in a formatted way
     * @return the formatted string
     */
    @Override
    public String toString(){
        String result = "";
        for (Map.Entry<Resource, Integer> entry : resourceIntegerMap.entrySet()){
            result += entry.getKey().name() + ": " + entry.getValue() + "\n";
        }
        return result;
    }
    
    /**
     * compares the stock of two ResourceStocks
     * @param obj the object to be compared
     * @return whether the the object is equal
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ResourceStock)) {
            return false;
        }
        return resourceIntegerMap.equals(((ResourceStock) obj).resourceIntegerMap);
    }
    
    @Override
    public int hashCode() {
        return 21 *  resourceIntegerMap.hashCode();
    }
}