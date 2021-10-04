package ch.zhaw.catan;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.awt.Point;

/**
 * Class for all the input and output of the game.
 * Uses the TextIO library to print to console and read input with inputReaders (eg. enumReader)
 *
 * @author Joël Plambeck
 * @version 06.12.2019
 */
public class InputOutput {
	
	private static final String successMessage = "Got it!";
	private static final String errorMessage = "Ohoh, something went wrong. Please try again :-)";
	TextIO textIO = TextIoFactory.getTextIO();
	TextTerminal<?> textTerminal = textIO.getTextTerminal();
	
	private enum answerYN {
		YES, NO
	}
	
	/**
	 * Asks the player how many people are playing.
	 * Only allows numbers from {@link Config#MIN_NUMBER_OF_PLAYERS} to the length of {@link Config.Faction}
	 * @return the amount of players playing
	 */
	public int getNumberOfPlayer() {
		return textIO.newIntInputReader()
				.withMaxVal(Config.Faction.values().length)
				.withMinVal(Config.MIN_NUMBER_OF_PLAYERS)
				.read("How many players are playing?");
	}
	
	/**
	 * Ask's for x and y coordinate for desired position
	 * @return the position with x and y coordinates
	 */
	public Point getPosition() {
		int xPosition = textIO.newIntInputReader().read("x coordinate:");
		int yPosition = textIO.newIntInputReader().read("y coordinate:");
		return new Point(xPosition, yPosition);
	}
	
	/**
	 * Asks for x and y coordinate for desired start position
	 * @return the resulting position
	 */
	public Point getStartPoint() {
		textTerminal.println("--Startposition--");
		return getPosition();
	}
	
	/**
	 * Asks for x and y coordinate for desired end position
	 * @return the resulting position
	 */
	public Point getEndPoint() {
		textTerminal.println("--Endposition--");
		return getPosition();
	}
	
	/**
	 * Asks the player whether he/she wants to build
	 * @return whether he wants to build
	 */
	public boolean wantsToBuild() {
		return textIO.newBooleanInputReader()
				.withTrueInput(answerYN.YES.toString())
				.withFalseInput(answerYN.NO.toString())
				.read("Do you want to build something?");
	}
	
	/**
	 * Ask's the player what {@link ch.zhaw.catan.Config.Resource} he/she is offering
	 * @return the offered {@link ch.zhaw.catan.Config.Resource}
	 */
	public Config.Resource getTradeOffer() {
		return textIO.newEnumInputReader(Config.Resource.class).read("What do you offer?");
	}
	
	/**
	 * Ask's the player what {@link ch.zhaw.catan.Config.Resource} he/she wants
	 * @return the desired {@link ch.zhaw.catan.Config.Resource}
	 */
	public Config.Resource getTradeWant() {
		return textIO.newEnumInputReader(Config.Resource.class).read("What do you want?");
	}
	
	/**
	 * Prints confirmation message
	 */
	public void confirmMessage(){
		textTerminal.println(successMessage);
	}
	
	/**
	 * Prints error message
	 */
	public void errorMessage(){
		textTerminal.println(errorMessage);
	}
	
	/**
	 * Prints the specified string to the console
	 * @param message text to print
	 */
	public void printToConsole(String message){
		textTerminal.println(message);
	}
	
	/**
	 * Prints the specified {@link ResourceStock} to the console
	 * @param stock ResourceStock to print
	 */
	public void printResourceStock(ResourceStock stock){
		textTerminal.println("Your resources:");
		textTerminal.println(stock.toString());
	}
	
	/**
	 * Ask's the player which command he/she wants to do
	 * @return the chosen command
	 */
	public GameSequence.Commands askForNextStep(){
		return textIO.newEnumInputReader(GameSequence.Commands.class).read("What do you want to do?");
	}
	
	/**
	 * Asks what type of {@link ch.zhaw.catan.Config.Structure} you want to build
	 * @return the chosen structure
	 */
	public Config.Structure askForStructure(){
		return textIO.newEnumInputReader(Config.Structure.class).read("What do you want to build?");
	}
	
	/**
	 * Print's who's turn it is.
	 * @param player the player who's up next
	 */
	public void printPlayersTurn(Player player) {
		textTerminal.println("");
		textTerminal.println("-------------------");
		textTerminal.println("It's " + player.getFaction().name() + "'s turn");
	}
	
	/**
	 * Announces winner of the game
	 * @param faction the winner of the game
	 */
	public void announceWinner(Config.Faction faction){
		textTerminal.println("------------ The winner is: " + faction.name() + " ------------");
	}
	
	/**
	 * Prints welcome message
	 */
	public void welcomeMessage(){
		textTerminal.println("-- Hi and welcome to Siedler!");
		textTerminal.println("-- we hope you have a lot of fun playing!");
	}
	
	/**
	 * Method to print dice number
	 * @param diceNumber number on the dice
	 */
	public void printDice(int diceNumber) {
		textTerminal.println("Dice number is: " + diceNumber);
	}
}
