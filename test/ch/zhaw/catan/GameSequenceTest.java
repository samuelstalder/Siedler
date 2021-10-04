package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSequenceTest {

    /**
     * All methods are based on the gameSequence
     */
    private static final int POINTS_REQUIRED_FOR_VICTORY = 7;
    SiedlerGame siedlerGame;
    Dice dice;
    private int playerNumber;


    @BeforeEach
    void init() {
        GameSequence gameSequence = new GameSequence();
        Dice dice;
    }

    private void initPhase0() {
        playerNumber = 3;
        dice = new Dice(1, 6);
    }

    private void initPhase1() {
        siedlerGame = new SiedlerGame(POINTS_REQUIRED_FOR_VICTORY, playerNumber);
    }

    @Test
    void testPlaceRoadInSea() {
        initPhase0();
        initPhase1();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        //point is in water
        Point startPoint = new Point(3, 3);
        Point endPoint = new Point(4, 4);
        assertFalse(siedlerGame.placeInitialRoad(startPoint, endPoint));
    }

    @Test
    void testPlaceRoadOnLand() {
        initPhase0();
        initPhase1();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        //point is not in water
        Point startPoint = new Point(4, 4);
        Point endPoint = new Point(5, 3);
        assertTrue(siedlerGame.placeInitialRoad(startPoint, endPoint));
    }

    @Test
    void testPlaceRoadOnTopOfExistingRoad() {
        initPhase0();
        initPhase1();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        //point is not in water
        Point startPoint = new Point(4, 4);
        Point endPoint = new Point(5, 3);
        assertTrue(siedlerGame.placeInitialRoad(startPoint, endPoint));
        assertFalse(siedlerGame.placeInitialRoad(startPoint, endPoint));
    }

    @Test
    void testPlaceRoadFarAwayFromSettlement() {
        initPhase0();
        initPhase1();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        //point is far away
        Point startPoint = new Point(10, 10);
        Point endPoint = new Point(10, 12);
        assertFalse(siedlerGame.placeInitialRoad(startPoint, endPoint));
    }

    @Test
    void testPlaceSettlementInSea() {
        Point pointInSea = new Point(4, 0);
        initPhase0();
        initPhase1();
        assertFalse(siedlerGame.placeInitialSettlement(pointInSea, false));
    }

    @Test
    void testPlaceSettlementOnLand() {
        Point pointOnLand = new Point(4, 6);
        initPhase0();
        initPhase1();
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
    }

    @Test
    void testPlaceSettlementOnTopOfExistingBuilding() {
        Point pointOnLand = new Point(4, 6);
        initPhase0();
        initPhase1();
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        assertFalse(siedlerGame.placeInitialSettlement(pointOnLand, false));
    }

    @Test
    void testPlaceRoadTooCloseToOtherSettlement() {
        Point firstSettlement = new Point(4, 6);
        Point secondSettlement = new Point(5, 7);
        initPhase0();
        initPhase1();
        assertTrue(siedlerGame.placeInitialSettlement(firstSettlement, false));
        assertFalse(siedlerGame.placeInitialSettlement(secondSettlement, false));
    }

    @Test
    void testPlaceRoadEnoughCloseToOtherSettlement() {
        Point firstSettlement = new Point(4, 6);
        Point secondSettlement = new Point(5, 9);
        initPhase0();
        initPhase1();
        assertTrue(siedlerGame.placeInitialSettlement(firstSettlement, false));
        assertTrue(siedlerGame.placeInitialSettlement(secondSettlement, false));
    }

    @Test
    void testInitGameWith2Player() {
        siedlerGame = new SiedlerGame(7, 2);
        assertEquals(siedlerGame.amountOfPlayers, 2);
    }

    @Test
    void testInitGameWith4Player() {
        siedlerGame = new SiedlerGame(7, 4);
        assertEquals(siedlerGame.amountOfPlayers, 4);
    }

    //Testing building
    @Test
    void testBuildWithEnoughRecources() {
        initPhase0();
        initPhase1();
        freeGiftToCurrentPlayer();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        assertTrue(siedlerGame.buildCity(pointOnLand));
    }

    @Test
    void testBuildWithNoRecources() {
        initPhase0();
        initPhase1();
        Point pointOnLand = new Point(4, 4);
        assertTrue(siedlerGame.placeInitialSettlement(pointOnLand, false));
        assertFalse(siedlerGame.buildCity(pointOnLand));
    }

    @Test
    void testGameSimulationWith3Player() {
        initPhase0();
        initPhase1();
        phase2Simulation();
        phase3Simulation();
        System.out.println(new SiedlerBoardTextView(siedlerGame.getBoard()).toString());
    }

    private void phase2Simulation() {
        //player 1 (Blue)
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.BLUE);
        assertTrue(siedlerGame.placeInitialSettlement(new Point(6, 6), false));
        assertTrue(siedlerGame.placeInitialRoad(new Point(6, 6), new Point(7, 7)));
        siedlerGame.switchToNextPlayer();
        //player 2 (Green)
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.GREEN);
        assertTrue(siedlerGame.placeInitialSettlement(new Point(4, 4), false));
        assertTrue(siedlerGame.placeInitialRoad(new Point(4, 4), new Point(4, 6)));
        siedlerGame.switchToNextPlayer();
        //player 3 (Red)
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.RED);
        assertTrue(siedlerGame.placeInitialSettlement(new Point(3, 15), false));
        assertTrue(siedlerGame.placeInitialRoad(new Point(3, 15), new Point(4, 16)));
        assertTrue(siedlerGame.placeInitialSettlement(new Point(7, 15), true));
        assertTrue(siedlerGame.placeInitialRoad(new Point(7, 15), new Point(8, 16)));
        assertEquals(1, siedlerGame.getCurrentPlayerResourceStock(Config.Resource.STONE));
        assertEquals(1, siedlerGame.getCurrentPlayerResourceStock(Config.Resource.WOOD));
        assertEquals(0, siedlerGame.getCurrentPlayerResourceStock(Config.Resource.GRAIN));
        assertEquals(1, siedlerGame.getCurrentPlayerResourceStock(Config.Resource.CLAY));
        assertEquals(0, siedlerGame.getCurrentPlayerResourceStock(Config.Resource.WOOL));
        siedlerGame.switchToPreviousPlayer();
        //player 2
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.GREEN);
        assertTrue(siedlerGame.placeInitialSettlement(new Point(10, 10), true));
        assertTrue(siedlerGame.placeInitialRoad(new Point(10, 10), new Point(10, 12)));
        siedlerGame.switchToPreviousPlayer();
        //player 1
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.BLUE);
        //set wrong points
        assertFalse(siedlerGame.placeInitialSettlement(new Point(12, 6), true));
        assertFalse(siedlerGame.placeInitialRoad(new Point(12, 6), new Point(11, 7)));
        //correct points
        assertTrue(siedlerGame.placeInitialSettlement(new Point(12, 10), true));
        assertTrue(siedlerGame.placeInitialRoad(new Point(12, 10), new Point(12, 12)));
    }

    private void phase3Simulation() {
        //player 1
        currentPlayerDice();
        freeGiftToCurrentPlayer();
        assertTrue(siedlerGame.buildCity(new Point(12, 10)));
        assertEquals(3, siedlerGame.getCurrentPlayerObject().getPoints());
        siedlerGame.switchToNextPlayer();
        //player 2
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.GREEN);
        currentPlayerDice();
        freeGiftToCurrentPlayer();
        assertTrue(siedlerGame.buildRoad(new Point(10, 12), new Point(9, 13)));
        assertTrue(siedlerGame.buildSettlement(new Point(9, 13)));
        assertEquals(siedlerGame.getCurrentPlayerObject().getPoints(), 3);
        siedlerGame.switchToNextPlayer();
        //player 3
        assertEquals(siedlerGame.getCurrentPlayerObject().getFaction(), Config.Faction.RED);
        currentPlayerDice();
        freeGiftToCurrentPlayer();
        siedlerGame.checkAndSetLongestRoad();
        assertNull(siedlerGame.playerWithLongestRoadRoute);
        assertTrue(siedlerGame.buildRoad(new Point(8, 16), new Point(8, 18)));
        assertTrue(siedlerGame.buildRoad(new Point(8, 18), new Point(9, 19)));
        assertTrue(siedlerGame.buildRoad(new Point(9, 19), new Point(10, 18)));
        assertTrue(siedlerGame.buildRoad(new Point(10, 18), new Point(10, 16)));
        siedlerGame.checkAndSetLongestRoad();
        assertEquals(siedlerGame.getCurrentPlayer(), Config.Faction.RED);
        assertNull(siedlerGame.getWinner());
        assertTrue(siedlerGame.buildSettlement(new Point(10, 16)));
        assertTrue(siedlerGame.buildCity(new Point(10, 16)));
        assertTrue(siedlerGame.buildCity(new Point(3, 15)));
        assertTrue(siedlerGame.buildCity(new Point(7, 15)));
        assertEquals(Config.Faction.RED, siedlerGame.getWinner());
    }

    private void currentPlayerDice() {
        int number = dice.throwDices(2);
        siedlerGame.addToResourceStock(siedlerGame.throwDice(number));
    }

    private void freeGiftToCurrentPlayer() {
        siedlerGame.currentPlayerEarn(Config.Resource.WOOD, 20);
        siedlerGame.currentPlayerEarn(Config.Resource.WOOL, 20);
        siedlerGame.currentPlayerEarn(Config.Resource.CLAY, 20);
        siedlerGame.currentPlayerEarn(Config.Resource.STONE, 20);
        siedlerGame.currentPlayerEarn(Config.Resource.GRAIN, 20);
    }

}
