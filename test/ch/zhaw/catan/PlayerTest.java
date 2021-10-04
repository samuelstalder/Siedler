package ch.zhaw.catan;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.catan.Config.Faction;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class PlayerTest {
    
    private Player playerUnderTest;

    @BeforeEach
    public void setUp() {
        playerUnderTest = new Player(Faction.BLUE);
    }

    @Test
    public void testAddRoad() {
        // Setup
    	Road road1 = new Road(new Point(6,6),new Point(7,7), playerUnderTest);
    	Road road2 = new Road(new Point(4,6),new Point(5,7), playerUnderTest);

    	playerUnderTest.addRoad(road1);
    	playerUnderTest.addRoad(road2);
    	
    	List<Road>expected=Arrays.asList(road1,road2);


    	 assertEquals(expected, playerUnderTest.getRoadList());

     
    }

    @Test
    public void testAddBuilding() {
        // Setup
       Settlement settlement1 = new Settlement(new Point(6,4), playerUnderTest);
       Settlement settlement2 = new Settlement(new Point(7,4), playerUnderTest);
       playerUnderTest.addBuilding(settlement1);
       playerUnderTest.addBuilding(settlement2);
       List<Settlement> expected = Arrays.asList(settlement1,settlement2);

       assertEquals(expected, playerUnderTest.getBuildingList());

    }

    @Test
    public void testRemoveBuilding() {
        // Setup
        Settlement settlement1 =new Settlement(new Point(6,4), playerUnderTest);
        Settlement settlement2 = new Settlement(new Point(7,4), playerUnderTest);
        
        playerUnderTest.addBuilding(settlement1);
        playerUnderTest.addBuilding(settlement2);
        
        
        playerUnderTest.removeBuilding(settlement2);
        
        List<Settlement> expected = Arrays.asList(settlement1);
        
        assertEquals(expected, playerUnderTest.getBuildingList());
        
    }

    @Test
    public void testGetRoadCount() {
      
    	Road road1 = new Road(new Point(6,6),new Point(7,7), playerUnderTest);
    	Road road2 = new Road(new Point(4,6),new Point(5,7), playerUnderTest);
    	
    	playerUnderTest.addRoad(road1);
    	playerUnderTest.addRoad(road2);
    	
    	
    	int result = playerUnderTest.getRoadCount();


        assertEquals(2, result);
    }

    @Test
    public void testPay() {
        boolean result = playerUnderTest.pay(Config.Resource.WOOD);
        boolean expected=playerUnderTest.getResourcestock().has(Config.Resource.WOOD,9);

        assertEquals(expected, result );
    }

    @Test
    public void testPayWithAmount() {
        boolean result = playerUnderTest.pay(Config.Resource.WOOL,3);
        boolean expected=playerUnderTest.getResourcestock().has(Config.Resource.WOOL,5);
        assertEquals(expected,result);
    }

    @Test
    public void testPayEarn() {

        playerUnderTest.earn(Config.Resource.WOOD,4);
        // Setup
    	List<Config.Resource> resourceList = Arrays.asList(Config.Resource.WOOD, Config.Resource.WOOD, Config.Resource.WOOD);
        assertTrue(playerUnderTest.pay(resourceList));
    }
    
    @Test
    public void testPayFalse(){
        List<Config.Resource> resourceList = Arrays.asList(
                Config.Resource.WOOD,
                Config.Resource.STONE,
                Config.Resource.WOOL,
                Config.Resource.WOOL,
                Config.Resource.GRAIN,
                Config.Resource.GRAIN
        );
        for (Config.Resource resource : resourceList){
            playerUnderTest.earn(resource, 1);
        }
        assertFalse(playerUnderTest.pay(Config.Structure.SETTLEMENT.getCosts()));
        assertTrue(playerUnderTest.getResourcestock().has(resourceList));
    }

    @Test
    public void testEarn() {

        playerUnderTest.earn(Config.Resource.WOOD);
    	boolean expected = playerUnderTest.getResourcestock().has(Config.Resource.WOOD,1);
    	
    	assertTrue(expected);

    }

    @Test
    public void testEarnFalse() {
        // Setup
        playerUnderTest.earn(Config.Resource.WOOD, 2);
        boolean expected = playerUnderTest.getResourcestock().has(Config.Resource.WOOD, 3);
        
        assertFalse(expected);
    }

    @Test
    public void testAddPoints() {
    	SiedlerGame siedlergame=new SiedlerGame(3,4);
    	playerUnderTest.addPoints(3);
    	

       assertEquals(3,playerUnderTest.getPoints());

    }
}