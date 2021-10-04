package ch.zhaw.catan;

import ch.zhaw.hexboard.HexBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SiedlerGameTest {
    Player currentPlayer;
    SiedlerGame game;
    HexBoard board;
    Bank bank;
    
    @BeforeEach
    void setUp() {
        game = new SiedlerGame(7, 2);
        currentPlayer = game.getCurrentPlayerObject();
        currentPlayer.getResourcestock().clearStock();
        bank = new Bank();
        board = game.getBoard();
    }
    
    @Test
    void tradeWithBankFourToOne() {
        currentPlayer.getResourcestock().add(Config.Resource.WOOD,5);
        assertTrue(game.tradeWithBankFourToOne(Config.Resource.WOOD, Config.Resource.CLAY));
        assertTrue(currentPlayer.getResourcestock().has(Config.Resource.CLAY, 1));
        assertFalse(currentPlayer.getResourcestock().has(Config.Resource.CLAY, 2));


    }

   @Test
   void testPlaceInitialStructures() {
       // player sets initial settlement and roads
       assertTrue(game.placeInitialSettlement(new Point(4, 4), false));
       assertTrue(game.placeInitialRoad(new Point(4, 4), new Point(4, 6)));
       assertTrue(game.placeInitialSettlement(new Point(5, 7), true));
       assertTrue(game.placeInitialRoad(new Point(4, 6), new Point(5, 7)));
       assertEquals(2, currentPlayer.getBuildingList().size());
       assertEquals(2, currentPlayer.getRoadList().size());
       // check payout  [1x WD | 1x ST | 1x GR]
       assertEquals(3, currentPlayer.getResourcestock().total());
       assertEquals(1, currentPlayer.getResourcestock().get(Config.Resource.WOOD));
       assertNotEquals(1,currentPlayer.getResourcestock().get(Config.Resource.WOOL));
   }



    @Test
    void testBuildStructures() {
        testPlaceInitialStructures();
        // player gets resources to build structures
        game.currentPlayerEarn(Config.Resource.WOOD,2);
        game.currentPlayerEarn(Config.Resource.CLAY,3);
        game.currentPlayerEarn(Config.Resource.WOOL);
        assertEquals(9, currentPlayer.getResourcestock().total());
        // build
        assertTrue(game.buildRoad(new Point(5, 7), new Point(5, 9)));
        assertTrue(game.buildRoad(new Point(5, 9), new Point(4, 10)));
        assertTrue(game.buildSettlement(new Point(4, 10)));
        // check if the cost of structure will be subtracted from resourcestock
        assertEquals(1,currentPlayer.getResourcestock().total());
        assertTrue(currentPlayer.getResourcestock().has(Config.Resource.WOOD, 0));
        // ckeck if the new structures gets the expected points and adds in current player's list
        assertEquals(3, currentPlayer.getPoints());
        assertEquals(4, currentPlayer.getRoadList().size());
        assertEquals(3, currentPlayer.getBuildingList().size());
    }

    @Test
    void testBuildCity() {
        testBuildStructures();
        // player gets resources to build a city
        game.currentPlayerEarn(Config.Resource.STONE,3);
        game.currentPlayerEarn(Config.Resource.GRAIN,2);
        // build city
        assertTrue(game.buildCity(new Point(5, 7)));
        assertEquals(4, currentPlayer.getPoints());
        // check if the city replace the old settlement in player's list
        assertEquals(3, currentPlayer.getBuildingList().size());
    }

    @Test
    void testInvalidBuilds(){
        testBuildCity();
       // player gets resources to build structures
       game.currentPlayerEarn(Config.Resource.WOOD,6);game.currentPlayerEarn(Config.Resource.CLAY,6);
       game.currentPlayerEarn(Config.Resource.WOOL,5);game.currentPlayerEarn(Config.Resource.GRAIN,5);
       game.currentPlayerEarn(Config.Resource.STONE,3);

       // try to build a settlement/road at a occupied position
       assertFalse(game.buildSettlement(new Point(4,4)));
       assertFalse(game.buildRoad(new Point(4,4),new Point(4,6)));
       // try to build a settlement/road at an invalid position
       assertFalse(game.placeInitialSettlement(new Point(10,0), false));
       assertFalse(game.placeInitialRoad(new Point(3,3), new Point(4,4)));
       assertFalse(  game.buildSettlement(new Point(1,2)));
       assertFalse(game.buildRoad(new Point(1,2), new Point(2,3)));
       // try to build a settlement/road in the water
       assertFalse(game.placeInitialSettlement(new Point(11,1), false));
       assertFalse(game.placeInitialRoad(new Point(11,1),new Point(11,3)));
       assertFalse(game.buildSettlement(new Point(9, 1)));
       assertFalse(game.buildRoad(new Point(3,3),new Point(4,4)));
       // try to build a settlement/city/road against the rules
       assertFalse(game.buildSettlement(new Point(4,6)));
       assertFalse(game.buildSettlement(new Point(9,7)));
       assertFalse(game.buildRoad(new Point(11,7), new Point(11,9)));
       assertFalse(game.buildRoad(new Point(4,4), new Point(3,3)));
       assertFalse(game.buildCity(new Point(7,13 )));
   }
   
   @Test
   void testSettlementAtCoast(){
        ResourceStock stock = new ResourceStock(Arrays.asList(Config.Resource.WOOD, Config.Resource.GRAIN));
        assertTrue(game.placeInitialSettlement(new Point(3,13), true));
        assertEquals(stock, game.getCurrentPlayerObject().getResourcestock());
   }
    
    @Test
    void testHalfResourceStock() {
        ResourceStock bankStock = game.getBank().getResourceStock();
        game.currentPlayerEarn(Config.Resource.STONE, 11);
        game.halfResourceStock();
        assertEquals(6, game.getCurrentPlayerResourceStock(Config.Resource.STONE));
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.STONE) + 5, bankStock.get(Config.Resource.STONE));
    }
    
    @Test
    void testHalfResourceStockLessThanSeven() {
        game.currentPlayerEarn(Config.Resource.WOOD, 7);
        game.halfResourceStock();
        assertEquals(7, game.getCurrentPlayerResourceStock(Config.Resource.WOOD));
    }
    
    @Test
    void testHalfResourceStockMultipleResources() {
        game.currentPlayerEarn(Config.Resource.STONE, 3);
        game.currentPlayerEarn(Config.Resource.WOOL, 3);
        game.currentPlayerEarn(Config.Resource.GRAIN, 3);
        int bankTotal = game.getBank().getResourceStock().total();
        game.halfResourceStock();
        assertEquals(5, game.getCurrentPlayerObject().getResourcestock().total());
        assertEquals(bankTotal + 4, game.getBank().getResourceStock().total());
    }
    
    @Test
    void testAddToResourceStock() {
        List<Config.Resource> resourceList = new ArrayList<>();
        resourceList.add(Config.Resource.STONE);
        resourceList.add(Config.Resource.STONE);
        resourceList.add(Config.Resource.WOOD);
        ResourceStock stock = new ResourceStock(resourceList);
        HashMap<Config.Faction, List<Config.Resource>> map = new HashMap<>();
        map.put(Config.Faction.BLUE, resourceList);
        game.addToResourceStock(map);
        assertEquals(stock, game.getCurrentPlayerObject().getResourcestock());
    }
    
    @Test
    void testAddToResourceStockEmptyList() {
        Map<Config.Faction, List<Config.Resource>> map = new HashMap<>();
        List<Config.Resource> resourceList = new ArrayList<>();
        map.put(currentPlayer.getFaction(), resourceList);
        game.addToResourceStock(map);
        assertEquals(new ResourceStock(), game.getCurrentPlayerObject().getResourcestock());
    }
    
    @Test
    void testAddToResourceStockEmptyMap() {
        Map<Config.Faction, List<Config.Resource>> map = Collections.emptyMap();
        game.addToResourceStock(map);
        assertEquals(new ResourceStock(), game.getCurrentPlayerObject().getResourcestock());
    }
    
    @Test
    void testGenerateResourceListCity(){
        City city = new City(new Point(4,2), new Player(Config.Faction.RED));
        List<Config.Resource> resourceList = game.generateResourcesList(city, Config.Land.FOREST);
        List<Config.Resource> expected = Arrays.asList(Config.Resource.WOOD, Config.Resource.WOOD);
        assertEquals(2, resourceList.size());
        assertEquals(expected, resourceList);
    }
    
    @Test
    void testGenerateResourceListSettlement(){
        Settlement settlement = new Settlement(new Point(4,2), new Player(Config.Faction.RED));
        List<Config.Resource> resourceList = game.generateResourcesList(settlement, Config.Land.FOREST);
        List<Config.Resource> expected = Arrays.asList(Config.Resource.WOOD);
        assertEquals(1, resourceList.size());
        assertEquals(expected, resourceList);
    }
    
    @Test
    void testThrowDiceSettlementAndCity(){
        game.currentPlayerEarn(Config.Resource.WOOD, 5);
        game.currentPlayerEarn(Config.Resource.WOOL, 5);
        game.currentPlayerEarn(Config.Resource.GRAIN, 5);
        game.currentPlayerEarn(Config.Resource.CLAY, 5);
        game.currentPlayerEarn(Config.Resource.STONE, 5);
        assertTrue(game.placeInitialSettlement(new Point(6, 6),false));
        assertTrue(game.buildRoad(new Point(6, 6), new Point(7, 7)));
        assertTrue(game.buildRoad(new Point(7, 9), new Point(7, 7)));
        assertTrue(game.buildSettlement(new Point(7, 9)));
        assertTrue(game.buildCity(new Point(7, 9)));
        Map<Config.Faction, List<Config.Resource>> expected = new HashMap<>();
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.STONE, Config.Resource.STONE, Config.Resource.STONE);
        expected.put(currentPlayer.getFaction(), resourceList);
        Map<Config.Faction, List<Config.Resource>> result = game.throwDice(4);
        assertEquals(expected, result);
    }
    
    @Test
    void testThrowDiceSettlement(){
        game.currentPlayerEarn(Config.Resource.WOOD, 3);
        game.currentPlayerEarn(Config.Resource.WOOL, 1);
        game.currentPlayerEarn(Config.Resource.GRAIN, 1);
        game.currentPlayerEarn(Config.Resource.CLAY, 3);
        assertTrue(game.placeInitialSettlement(new Point(6, 6),false));
        assertTrue(game.buildRoad(new Point(6, 6), new Point(7, 7)));
        assertTrue(game.buildRoad(new Point(7, 9), new Point(7, 7)));
        assertTrue(game.buildSettlement(new Point(7, 9)));
        Map<Config.Faction, List<Config.Resource>> expected = new HashMap<>();
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.STONE, Config.Resource.STONE);
        expected.put(currentPlayer.getFaction(), resourceList);
        Map<Config.Faction, List<Config.Resource>> result = game.throwDice(4);
        assertEquals(expected, result);
    }
    
    @Test
    void testThrowDiceThreeSettlements(){
        assertTrue(game.placeInitialSettlement(new Point(8, 6),false));
        assertTrue(game.placeInitialSettlement(new Point(7, 9),false));
        assertTrue(game.placeInitialSettlement(new Point(9, 9),false));
        Map<Config.Faction, List<Config.Resource>> expected = new HashMap<>();
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.GRAIN, Config.Resource.GRAIN, Config.Resource.GRAIN);
        expected.put(currentPlayer.getFaction(), resourceList);
        Map<Config.Faction, List<Config.Resource>> result = game.throwDice(5);
        assertEquals(expected, result);
    }
    
    @Test
    void testThrowDiceTwoSettlementsInDifferentPlaces(){
        assertTrue(game.placeInitialSettlement(new Point(8, 6),false));
        assertTrue(game.placeInitialSettlement(new Point(4, 12),false));
        Map<Config.Faction, List<Config.Resource>> expected = new HashMap<>();
        List<Config.Resource> resourceList = Arrays.asList(Config.Resource.WOOD, Config.Resource.GRAIN);
        expected.put(currentPlayer.getFaction(), resourceList);
        Map<Config.Faction, List<Config.Resource>> result = game.throwDice(5);
        assertEquals(expected, result);
    }
    
    @Test
    void testThrowDiceFalse(){
        assertTrue(game.placeInitialSettlement(new Point(8, 6),false));
        Map<Config.Faction, List<Config.Resource>> expected = new HashMap<>();
        Map<Config.Faction, List<Config.Resource>> result = game.throwDice(6);
        assertEquals(expected, result);
    }
    
    @Test
    void testInWater(){
        List<Point> waterPoints = new ArrayList<>();
        waterPoints.add(new Point(3, 1));
        waterPoints.add(new Point(4, 0));
        waterPoints.add(new Point(5, 1));
        waterPoints.add(new Point(6, 0));
        waterPoints.add(new Point(7, 1));
        waterPoints.add(new Point(8, 0));
        waterPoints.add(new Point(9, 1));
        waterPoints.add(new Point(10, 0));
        waterPoints.add(new Point(11, 1));
        waterPoints.add(new Point(11, 3));
        waterPoints.add(new Point(12, 4));
        waterPoints.add(new Point(12, 6));
        waterPoints.add(new Point(13, 7));
        waterPoints.add(new Point(13, 9));
        waterPoints.add(new Point(14, 10));
        waterPoints.add(new Point(14, 12));
        waterPoints.add(new Point(13, 13));
        waterPoints.add(new Point(13, 15));
        waterPoints.add(new Point(12, 16));
        waterPoints.add(new Point(12, 18));
        waterPoints.add(new Point(11, 19));
        waterPoints.add(new Point(11, 21));
        waterPoints.add(new Point(10, 22));
        waterPoints.add(new Point(8, 22));
        waterPoints.add(new Point(6, 22));
        waterPoints.add(new Point(4, 22));
        waterPoints.add(new Point(9, 21));
        waterPoints.add(new Point(7, 21));
        waterPoints.add(new Point(5, 21));
        waterPoints.add(new Point(3, 21));
        waterPoints.add(new Point(3, 19));
        waterPoints.add(new Point(2, 18));
        waterPoints.add(new Point(2, 16));
        waterPoints.add(new Point(1, 15));
        waterPoints.add(new Point(1, 13));
        waterPoints.add(new Point(0, 12));
        waterPoints.add(new Point(0, 10));
        waterPoints.add(new Point(1, 9));
        waterPoints.add(new Point(1, 7));
        waterPoints.add(new Point(2, 6));
        waterPoints.add(new Point(2, 4));
        waterPoints.add(new Point(3, 3));
        waterPoints.add(new Point(3, 1));
        for (Point waterPoint : waterPoints) {
            assertFalse(game.getBoard().isPointOnField(waterPoint));
        }
        assertTrue(game.getBoard().isPointOnField(new Point(6, 6)));
        assertTrue(game.getBoard().isPointOnField(new Point(4, 4)));
    }

    @Test
    void testSwitchToPreviousPlayer() {
        game = new SiedlerGame(7, 4);
        assertEquals(game.getCurrentPlayer(), Config.Faction.BLUE);
        game.switchToPreviousPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.YELLOW);
        game.switchToPreviousPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.RED);
        game.switchToPreviousPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.GREEN);
        game.switchToPreviousPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.BLUE);
    }

    @Test
    void testSwitchToNextPlayerWith4Player() {
        game = new SiedlerGame(7, 4);
        assertEquals(game.getCurrentPlayer(), Config.Faction.BLUE);
        game.switchToNextPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.GREEN);
        game.switchToNextPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.RED);
        game.switchToNextPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.YELLOW);
        game.switchToNextPlayer();
        assertEquals(game.getCurrentPlayer(), Config.Faction.BLUE);
    }
}
