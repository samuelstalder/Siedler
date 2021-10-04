package ch.zhaw.catan;

/**
 * This class represents the the different stages of the game and is controlling who is allowed to play
 * It starts the game and goes through all the different stages until one player wins the game.
 * Execute this class to start the game.
 *
 * @author Samuel Stalder
 * @version 06.12.2019
 */
public class GameSequence {
    
    SiedlerGame siedler;
    InputOutput inputOutput = new InputOutput();
    Dice dice;
    private static final int POINTS_REQUIRED_FOR_VICTORY = 7;
    private int playerNumber;
    
    /**
     * This enum holds all the possible commands in the game
     * Each command has a string to print to console
     */
    public enum Commands {
        BUILD("build"), TRADE("trade"), FINISH_TURN("finish turn"), VIEW("show board"), STOCK("show resources");
        private String name;
        Commands(String name) {
            this.name = name;
        }
    
        /**
         * Get a string representation of the command
         * @return string to print
         */
        @Override
        public String toString() {
            return name;
        }
    }
    
    /**
     * Starts the game
     * and goes through the 3 official phases of the game Siedler of Catan
     * until a player wins the game
     */
    public void start() {
        inputOutput.welcomeMessage();
        phase0();
        phase1();
        phase2();
        while (siedler.getWinner() == null) {
            phase3();
        }
        inputOutput.announceWinner(siedler.getWinner());
    }
    
    /**
     * Phase 0 handles the event before the start defines amount of player and
     * amount of eyes on the dice
     */
    private void phase0() {
        playerNumber = inputOutput.getNumberOfPlayer();
        dice = new Dice(1, 6);
    }
    
    /**
     * Phase 1 initializes a new game.
     * Generates all fields and their numbers.
     * Generates all player and their factions.
     */
    private void phase1() {
        siedler = new SiedlerGame(POINTS_REQUIRED_FOR_VICTORY, playerNumber);
        inputOutput.printToConsole(siedler.displayView());
    }
    
    /**
     * In Phase 2 all player places their settlements and roads the sequence with
     * for player is: 1,2,3,4,4,3,2,1
     */
    private void phase2() {
        boolean payout = false;
        for (int i = 1; i <= (playerNumber * 2); i++) {
            inputOutput.printPlayersTurn(siedler.getCurrentPlayerObject());
            inputOutput.printToConsole("Place your initial settlement(s)");
            while (!siedler.placeInitialSettlement(inputOutput.getPosition(), payout)) {
                inputOutput.errorMessage();
            }
            inputOutput.confirmMessage();
            inputOutput.printToConsole(siedler.displayView());
            
            inputOutput.printToConsole("Place your initial road(s)");
            while (!siedler.placeInitialRoad(inputOutput.getStartPoint(), inputOutput.getEndPoint())) {
                inputOutput.errorMessage();
            }
            inputOutput.confirmMessage();
            inputOutput.printToConsole(siedler.displayView());
            if (i < playerNumber) {
                siedler.switchToNextPlayer();
            } else if (i == playerNumber) {
                // same player
                payout = true;
            } else if (playerNumber * 2 != i){
                siedler.switchToPreviousPlayer();
            }
        }
    }
    
    /**
     * In phase 3 the player throws the dice. It expends or half the RecourceStock
     * of certain player. After dice-throw the player is able to to build buildings
     * or trade with the bank.
     */
    private void phase3() {
        inputOutput.printPlayersTurn(siedler.getCurrentPlayerObject());
        int number = dice.throwDices(2);
        inputOutput.printDice(number);
        siedler.addToResourceStock(siedler.throwDice(number));
        
        boolean endOfTurn = false;
        while (!endOfTurn) {
            switch (inputOutput.askForNextStep()) {
                case BUILD:
                    buildingPhase();
                    break;
                case TRADE:
                    tradingPhase();
                    break;
                case VIEW:
                    inputOutput.printToConsole(siedler.displayView());
                    break;
                case STOCK:
                    inputOutput.printResourceStock(siedler.getCurrentPlayerObject().getResourcestock());
                    break;
                case FINISH_TURN:
                    siedler.switchToNextPlayer();
                    endOfTurn = true;
                    break;
                default:
                    inputOutput.errorMessage();
            }
        }
    }
    
    /**
     * The player choose between a road, settlement or city He have to define the
     * place of building
     */
    private void buildingPhase() {
        switch (inputOutput.askForStructure()) {
            case ROAD:
                if (!siedler.buildRoad(inputOutput.getStartPoint(), inputOutput.getEndPoint())) {
                    inputOutput.errorMessage();
                } else {
                    inputOutput.printToConsole(siedler.displayView());
                    inputOutput.confirmMessage();
                }
                siedler.checkAndSetLongestRoad();
                break;
            case SETTLEMENT:
                if (!siedler.buildSettlement(inputOutput.getEndPoint())) {
                    inputOutput.errorMessage();
                } else {
                    inputOutput.printToConsole(siedler.displayView());
                    inputOutput.confirmMessage();
                }
                break;
            case CITY:
                if (!siedler.buildCity(inputOutput.getPosition())) {
                    inputOutput.errorMessage();
                } else {
                    inputOutput.printToConsole(siedler.displayView());
                    inputOutput.confirmMessage();
                }
                break;
            default:
                inputOutput.errorMessage();
                break;
        }
    }
    
    /**
     * For the trade with the bank, the player defines offer and want.
     */
    private void tradingPhase() {
        inputOutput.printToConsole("Welcome to the bank, today's exchange rate is 4:1.");
        inputOutput.printResourceStock(siedler.getCurrentPlayerObject().getResourcestock());
        if (siedler.tradeWithBankFourToOne(inputOutput.getTradeOffer(), inputOutput.getTradeWant())){
            inputOutput.confirmMessage();
            inputOutput.printResourceStock(siedler.getCurrentPlayerObject().getResourcestock());
        } else {
            inputOutput.errorMessage();
        }
    }
    
    public static void main(String[] args) {
        GameSequence gameSequence = new GameSequence();
        gameSequence.start();
    }
}
