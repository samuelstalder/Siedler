package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.zhaw.catan.Config.Structure.CITY;
import static ch.zhaw.catan.Config.Structure.ROAD;
import static ch.zhaw.catan.Config.Structure.SETTLEMENT;

/**
 * This class is the class with all the playing logic in the game.
 * It makes sure, all the commands and actions are valid and assigns it to the current user.
 * Uses the RoadChecker class for the logic of the longest road.
 *
 * @author Sophie Daenzer, Dario Zuellig, Samuel Stalder, Joël Plambeck
 * @version 06.12.2019
 */
public class SiedlerGame {

    public static final int THIEF_NUMBER = 7;
    private static final int FIRST_FACTION_INDEX = 0;
    private static final int INDEX_OFFSET = 1;
    protected static final List<Faction> factionTurnList = Arrays.asList(Faction.BLUE, Faction.GREEN, Faction.RED, Faction.YELLOW);
    public static final int POINTS_FOR_LONGEST_ROAD = 2;
    public static final int MIN_LENGTH_FOR_LONGEST_ROAD = 5;
    private int winPoints;
    protected int amountOfPlayers;
    private Player currentPlayer;
    private SiedlerBoardTextView view;
    private Bank bank = new Bank();
    protected Player playerWithLongestRoadRoute = null;
    private SiedlerBoard board = new SiedlerBoard();
    private HashMap<Faction, Player> factionPlayerHashMap = new HashMap<>();
    
    /**
     * Creates a new SiedlerGame object and initialises all the players and the board to play on.
     * It also sets the winPoint which define how many points are necessary to win the game.
     * @param winPoints the amount of win points necessary to win
     * @param players the amount of players
     */
    public SiedlerGame(int winPoints, int players) {
        initPlayers(players);
        initBoard();
        this.winPoints = winPoints;
    }

    /**
     * Gets faction of next index in factionTurnList.
     * Uses the modulo operator % to keep index in bounds.
     */
    public void switchToNextPlayer() {
        Faction nextFaction;
        int currentFactionIndex = factionTurnList.indexOf(currentPlayer.getFaction());
        nextFaction = factionTurnList.get((currentFactionIndex + INDEX_OFFSET) % amountOfPlayers);
        currentPlayer = factionPlayerHashMap.get(nextFaction);
    }

    /**
     * Gets faction of previous index in factionTurnList.
     * If factionIndex is {@link #FIRST_FACTION_INDEX} sets it to {@link #amountOfPlayers}
     */
    public void switchToPreviousPlayer() {
        Faction previousFaction;
        int currentFactionIndex = factionTurnList.indexOf(currentPlayer.getFaction());
        if (currentFactionIndex == FIRST_FACTION_INDEX) {currentFactionIndex = amountOfPlayers;}
        previousFaction = factionTurnList.get(currentFactionIndex - INDEX_OFFSET);
        currentPlayer = factionPlayerHashMap.get(previousFaction);
    }

    /**
     * List of all all factions in the correct playing order
     * @return all factions which are part of the game
     */
    public List<Faction> getPlayer() {
        List<Faction> factionList = new ArrayList<>();
        for (int i = 0; i < amountOfPlayers; i++){
            factionList.add(factionTurnList.get(i));
        }
        return factionList;
    }
    
    /**
     * Get the board object used in the game
     * @return board of the game
     */
    public SiedlerBoard getBoard() {
        return board;
    }
    
    /**
     * Get the current {@link Player} object
     * @return the current player
     */
    public Faction getCurrentPlayer() {
        return currentPlayer.getFaction();
    }
    
    /**
     * Get's the amount of a {@link Resource} in the {@link ResourceStock} of the current player
     * @param resource resource to get
     * @return amount of the resource in stock
     */
    public int getCurrentPlayerResourceStock(Resource resource) {
        return currentPlayer.getResourcestock().get(resource);
    }

    /**
     * Method to make current player pay a specified resource
     * @param resource the resource to pay
     * @param amount the amount he needs to pay
     * @return whether the transaction was successful
     */
    public boolean currentPlayerPay(Resource resource, int amount){
        return currentPlayer.pay(resource, amount);
    }
    
    /**
     * Method to make the current player pay all the resources in the specified list
     * @param resourceList list of resources to pay
     * @return whether the transaction was successful
     */
    public boolean currentPlayerPay(List<Resource> resourceList){
        return currentPlayer.pay(resourceList);
    }
    
    /**
     * Method to make the current player pay all the resources in the specified list
     * @param resourceList list of resources to pay
     * @return whether the transaction was successful
     */
    public boolean currentPlayerPayBank(List<Resource> resourceList){
        boolean transactionSuccessful = currentPlayerPay(resourceList);
        if (transactionSuccessful){
            addToBank(resourceList);
        }
        return transactionSuccessful;
    }

    /**
     * Method to make the {@link #currentPlayer} receive one resource
     * @param resource the resource to add
     */
    public void currentPlayerEarn(Resource resource){
        currentPlayer.earn(resource);
    }

    /**
     * Method to make {@link #currentPlayer} earn receive resources
     * @param resource the resource to add
     * @param amount the amount of resources
     */
    public void currentPlayerEarn(Resource resource, int amount){
        currentPlayer.earn(resource, amount);
    }
    
    /**
     * Get the current {@link Player} object
     * @return the current player
     */
    public Player getCurrentPlayerObject(){
        return currentPlayer;
    }

    /**
     * Builds a first {@link Settlement}. The corner where the {@link Settlement} is to be built must be empty, as well as the three neighbors corners.
     * Adds the {@link Settlement} on the HexBoard and to the current player's buildingList.
     * For the second {@link Settlement} the current player gets the resources from the fields around the new {@link Settlement}
     *
     * @param position corner where the settlement will be built
     * @param payout if the player gets the resources (only in the second round)
     * @return boolean if the built was successful
     */
    public boolean placeInitialSettlement(Point position, boolean payout) {
        boolean successful = isValidInitialSettlement(position);
          if (successful) {
              Settlement settlement = new Settlement(position, currentPlayer);
              constructBuilding(position, settlement);
              if (payout) {
                  List<Config.Land> fieldLandTypes = board.getFields(position);
                  for (Config.Land land : fieldLandTypes) {
                      Config.Resource resource = land.getResource();
                      currentPlayerEarn(resource);
                  }
              }
          }
        return successful;
    }

    private boolean isValidInitialSettlement(Point position){
        return board.hasCorner(position)
                && board.getNeighboursOfCorner(position).isEmpty()
                && board.getCorner(position) == null
                && isPointOnField(position);
    }
    
    private void constructBuilding(Point position, Settlement building){
        board.setCorner(position, building);
        currentPlayer.addBuilding(building);
        currentPlayer.addPoints(building.getWinPoints());
    }
    
    /**
     * Builds the first two {@link Road}. Player has to own one {@link Settlement} before. The edge where the {@link Road} is to be built must be empty
     * and must adjacent to the own {@link Settlement}.
     * Adds the {@link Road} on the HexBoard and to the current player's roadList.
     * @param roadStart point where the road starts
     * @param roadEnd point where the road ends
     * @return boolean if the built was successful
     */
    public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
        boolean successful = isValidRoad(roadStart, roadEnd);
        if (successful) {
            Road road = new Road(roadStart, roadEnd, currentPlayer);
            board.setEdge(roadStart, roadEnd, road);
            currentPlayer.addRoad(road);
        }
        return successful;
    }
    
    private boolean isValidRoad(Point roadStart, Point roadEnd){
        return board.hasEdge(roadStart, roadEnd)
                && board.getEdge(roadStart, roadEnd) == null
                && isPointOnField(roadStart)
                && isPointOnField(roadEnd)
                && isAllowedToBuildRoad(roadStart, roadEnd);
    }
    
    private boolean isAllowedToBuildRoad(Point roadStart, Point roadEnd){
        return ((isOwnerOf(roadStart) || isOwnerOf(roadEnd))
                || (hasRoadToThisPoint(roadStart) || hasRoadToThisPoint(roadEnd)));
    }
    
    private boolean isOwnerOf(Point point){
        boolean result = board.getCorner(point) != null;
        if (result) {
            result = board.getCorner(point).getOwner() == currentPlayer;
        }
        return result;
    }
    
    /**
     * Get's all the {@link Settlement} around the field with the number from the parameter dicethrow.
     * Checks whether its a {@link Settlement} or a {@link City} and gives the amount of win points.
     * @param dicethrow the number on the dice(s)
     * @return all the resources each faction gets, empty if nobody get's anything
     */
    public Map<Faction, List<Resource>> throwDice(int dicethrow) {
        Map<Faction, List<Resource>> resourceListForFaction = new HashMap<>();
        List<Point> fieldCenterPoints;
        Config.Land fieldLandType;
        List<Settlement> allAdjacentSettlementsOfField;
        List<Resource> resourceList;
        
        if (dicethrow != THIEF_NUMBER){
            fieldCenterPoints = view.getCenterPoints(dicethrow);
            for (Point fieldCenterPoint : fieldCenterPoints) {
                fieldLandType = board.getField(fieldCenterPoint);
                allAdjacentSettlementsOfField = board.getCornersOfField(fieldCenterPoint);
                for (Settlement settlement : allAdjacentSettlementsOfField) {
                    Faction owner = settlement.getOwner().getFaction();
                    resourceList = generateResourcesList(settlement, fieldLandType);
                    resourceList.addAll(resourceListForFaction.getOrDefault(owner, Collections.emptyList()));
                    resourceListForFaction.put(owner, resourceList);
                }
            }
        } else {
            halfResourceStock();
            resourceListForFaction = Collections.emptyMap();
        }
        return resourceListForFaction;
    }
    
    /**
     * Generates a list with resources for a {@link Settlement}.
     * @param settlement the settlement at the field
     * @param fieldLandType the field with the resource
     * @return the resources that the settlement gets
     */
    List<Resource> generateResourcesList(Settlement settlement, Config.Land fieldLandType) {
        List<Resource> resourceList = new ArrayList<>();
        for (int i = 0; i < settlement.getHarvest(); i++){
            resourceList.add(fieldLandType.getResource());
        }
        return resourceList;
    }
    
    /**
     *	Method to build a {@link Settlement}. Checks if the corner at the position exists. If the three neighbour corners are empty.
     *	If the player already owns a road adjacent. If the player has enough resources for the construction of the {@link Settlement} [WD, CL, WL, GR].
     *	Then adds the {@link Settlement} to the HexBoard. Adds the {@link Settlement} to the currentPlayer's list. Player pays the costs to the bank and gets one winPoint.
     * @param position corner where the settlement is to be built
     * @return boolean if the built was successful
     */
    public boolean buildSettlement(Point position) {
        boolean successful = isValidSettlement(position) && currentPlayerPayBank(SETTLEMENT.getCosts());
        if (successful) {
            Settlement settlement = new Settlement(position,currentPlayer);
            constructBuilding(position, settlement);
        }
        return successful;
    }
    
    private boolean isValidSettlement(Point position){
        return isValidInitialSettlement(position) && hasRoadToThisPoint(position);
    }
    
    /**
     *	Method to build a {@link Road}. Checks if the edge between the points exists. If the player has enough resources for
     *	the construction of the {@link Road} [WD, CL]. If the player already owns a {@link Settlement}/{@link City} adjacent.
     *	Then adds the {@link Road} to the HexBoard. Player pays the costs to the bank. Adds the {@link Road} to the currentPlayer's roadlist.
     * @param roadStart Point where the road starts
     * @param roadEnd Point where the road ends
     * @return boolean if the built was successful
     */
    public boolean buildRoad(Point roadStart, Point roadEnd) {
        boolean buildSuccessful = isValidRoad(roadStart, roadEnd) && currentPlayerPayBank(ROAD.getCosts());
            if(buildSuccessful){
                    Road road = new Road(roadStart,roadEnd,currentPlayer);
                    board.setEdge(roadStart,roadEnd,road);
                    currentPlayer.addRoad(road);
            }
        return buildSuccessful;
    }
    
    private boolean hasRoadToThisPoint(Point corner){
        List<Road> roadList = board.getAdjacentEdges(corner);
        for (Road road : roadList){
            if (road.getOwner() == currentPlayer){
                return true;
            }
        }
        return false;
    }
    
    /**
     *	Method to build a {@link City}. Checks if the corner at the position exists. If there is already a {@link Settlement} at this corner,
     *	which belongs to the currentPlayer. If the {@link Player} has enough resources for the construction of the {@link City} [ST, ST, ST, GR, GR].
     *	Then adds the {@link City} to the HexBoard. Adds the {@link City} to the currentPlayer's buildingList. Player pays the costs to the bank and gets two winPoints.
     * @param position corner where the city is to be built
     * @return boolean if the built was successful
     */
    public boolean buildCity(Point position) {
        Settlement currentStructure = board.getCorner(position);
        boolean validCity = currentStructure != null && !(currentStructure instanceof City) && isOwnerOf(position) && currentPlayerPayBank(CITY.getCosts());
        if (validCity) {
                City city = new City(position, currentPlayer);
                currentPlayer.removeBuilding(currentStructure);
                currentPlayer.removePoints(currentStructure.getWinPoints());
                constructBuilding(position, city);
        }
        return validCity;
    }
    
    /**
     * Allows a player to trade Resources with the bank in a 4:1 ratio.
     * The player offers 4 of a kind and gets 1 of a kind back.
     * If the player or the bank doesn't have enough in stock, nothing will be traded
     * @param offer what you want to sell (4)
     * @param want what you want to buy (1)
     * @return whether the trade was successful
     */
    public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
        boolean result = getCurrentPlayerResourceStock(offer) >= Bank.OFFER_AMOUNT_RATIO
                && bank.trade(offer, want)
                && currentPlayerPay(offer, Bank.OFFER_AMOUNT_RATIO);
        if (result){
            currentPlayerEarn(want, Bank.RETURN_AMOUNT_RATIO);
        }
        return result;
    }
    
    /**
     * Determines the winner of the game
     * If there is no winner yet, returns null
     * @return faction of winning player, null for no winner
     */
    public Faction getWinner() {
        Faction winner = null;
        for (Map.Entry<Faction, Player> entry : factionPlayerHashMap.entrySet()) {
            if (entry.getValue().getPoints() >= winPoints) {
                winner = entry.getKey();
            }
        }
        return winner;
    }
    
    /**
     * Method to remove four random resources from every player that has more than {@link #THIEF_NUMBER}
     * resources in his stock. The resources removed are handed over to the bank.
     */
    public void halfResourceStock() {
        for ( Map.Entry<Faction, Player> entry : factionPlayerHashMap.entrySet()){
            ResourceStock playerResourceStock = entry.getValue().getResourcestock();
            if (playerResourceStock.total() > THIEF_NUMBER){
                int amountToBeTakenAway = playerResourceStock.total() / 2;
                addToBank(playerResourceStock.removeRandomResources(amountToBeTakenAway));
            }
        }
    }
    
    /**
     * Adds resources in the list to the bank
     * @param resourcesToAdd list with all the resources
     */
    private void addToBank(List<Resource> resourcesToAdd){
        bank.getResourceStock().add(resourcesToAdd);
    }
    
    /**
     * Adds all resources in the list to the player associated to the faction
     * Note that you have to add the resource twice if the faction gets two resources.
     * @param map which faction gets which resources
     */
    public void addToResourceStock(Map<Faction, List<Resource>> map) {
        for (Map.Entry<Faction, List<Resource>> entry : map.entrySet()){
            ResourceStock playerResourceStock = factionPlayerHashMap.get(entry.getKey()).getResourcestock();
            playerResourceStock.add(entry.getValue());
        }
    }

    /**
     * Checks the current player route length and sets winning point for it
     */
    public void checkAndSetLongestRoad() {
        RoadChecker roadCheckerForCurrentPlayer = new RoadChecker(currentPlayer.getRoadList());
        if (roadCheckerForCurrentPlayer.getMaxLength() >= MIN_LENGTH_FOR_LONGEST_ROAD) {
            if (playerWithLongestRoadRoute == null) {
                playerWithLongestRoadRoute = currentPlayer;
                currentPlayer.addPoints(POINTS_FOR_LONGEST_ROAD);
            } else {
                RoadChecker roadCheckerForPlayerWithLongestRoad = new RoadChecker(playerWithLongestRoadRoute.getRoadList());
                if (roadCheckerForPlayerWithLongestRoad.getMaxLength() < roadCheckerForCurrentPlayer.getMaxLength()) {
                    playerWithLongestRoadRoute.removePoints(POINTS_FOR_LONGEST_ROAD);
                    currentPlayer.addPoints(POINTS_FOR_LONGEST_ROAD);
                }
            }
        }
    }
    
    /**
     * loops through the amount of players and creates a player object for each faction
     * @param amountOfPlayer how many players are playing
     */
    protected void initPlayers(int amountOfPlayer) {
        this.amountOfPlayers = amountOfPlayer;
        for (int i = 0; i < amountOfPlayers; i++){
            Faction faction = factionTurnList.get(i);
            factionPlayerHashMap.put(faction, new Player(faction));
        }
        currentPlayer = factionPlayerHashMap.get(factionTurnList.get(FIRST_FACTION_INDEX));
    }
    
    /**
     * initialises the board to play on
     */
    protected void initBoard() {
        view = new SiedlerBoardTextView(board);
    }
    
    /**
     * Returns a String of the text representation of the board
     * @return the board as a String
     */
    public String displayView() {
        return view.toString();
    }
    
    /**
     * Method for testing purposes to get the bank of the game
     * @return bank of the game
     */
    public Bank getBank(){
        return bank;
    }
    
    private boolean isPointOnField(Point point) {
        return getBoard().isPointOnField(point);
    }
}
