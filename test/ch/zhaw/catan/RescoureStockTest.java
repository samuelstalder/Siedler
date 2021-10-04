package ch.zhaw.catan;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RescoureStockTest {
	
private ResourceStock resourceStockUnderTest;
    
    @BeforeEach
    public void setUp() {
        resourceStockUnderTest = new ResourceStock();
    }
    
    @Test
    public void testHas() {
        // Setup
    	resourceStockUnderTest.add(Config.Resource.WOOD);
    	List<Config.Resource> resourceList = Arrays.asList(Config.Resource.WOOD);
    	assertTrue(resourceStockUnderTest.has(Config.Resource.WOOD, 1));
    }
    
    @Test
    public void testHasList(){
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.WOOD, Config.Resource.WOOD, Config.Resource.WOOL);
        resourceStockUnderTest.add(Config.Resource.WOOD, 2);
        resourceStockUnderTest.add(Config.Resource.WOOL, 1);
        assertTrue(resourceStockUnderTest.has(resourceList));
    }
    
    @Test
    public void testHasListFalse(){
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.WOOD, Config.Resource.WOOD, Config.Resource.WOOL);
        resourceStockUnderTest.add(Config.Resource.WOOD, 2);
        assertFalse(resourceStockUnderTest.has(resourceList));
    }
    
    @Test
    public void testHasListSettlement(){
        resourceStockUnderTest.add(Arrays.asList(Config.Resource.WOOD, Config.Resource.CLAY, Config.Resource.WOOL, Config.Resource.GRAIN));
        assertTrue(resourceStockUnderTest.has(Config.Structure.SETTLEMENT.getCosts()));
        assertFalse(resourceStockUnderTest.has(Config.Structure.CITY.getCosts()));
    }
    
    @Test
    public void testAddAndRemove() {
        // Setup
       resourceStockUnderTest.add(Config.Resource.STONE,3);
       
       resourceStockUnderTest.remove(Config.Resource.STONE, 1);
       
       assertTrue(resourceStockUnderTest.has(Config.Resource.STONE,2));
       
    }
    
    @Test
    public void testRemove() {
        // Setup
        resourceStockUnderTest.add(Config.Resource.STONE, 10);
        resourceStockUnderTest.remove(Config.Resource.STONE, 5);
    	assertTrue(resourceStockUnderTest.has(Config.Resource.STONE,5));
   
    	
        
    }
    
    @Test
    public void testremoveRandomResources() {
    	
    	//Setup
    	
    	resourceStockUnderTest.add(Config.Resource.STONE,10);
    	
    	List<Config.Resource> result=resourceStockUnderTest.removeRandomResources(5);
    	
    	List<Config.Resource> expected=Arrays.asList(Config.Resource.STONE,Config.Resource.STONE,Config.Resource.STONE,Config.Resource.STONE,Config.Resource.STONE);
    	
    	assertEquals(5,result.size());
    	
    	assertEquals(expected, result);
    	
    	assertEquals(5,resourceStockUnderTest.get(Config.Resource.STONE));
    }
    
    @Test
    public void testRemoveRandomResourcewithtwoResources() {
        resourceStockUnderTest.add(Config.Resource.STONE, 5);
        resourceStockUnderTest.add(Config.Resource.WOOL, 5);
        List<Config.Resource> result = resourceStockUnderTest.removeRandomResources(7);
        assertEquals(7, result.size());
        assertEquals(3, resourceStockUnderTest.total());
    }


    	   

}



