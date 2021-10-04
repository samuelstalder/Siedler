package ch.zhaw.catan;

import java.util.Random;

/**
 * A general Dice with a range from min (inclusive) and max (inclusive) that can return random numbers within the range.
 * This class uses the java.util.Random class to get random numbers in a range
 *
 * @author Joël Plambeck
 * @version 06.12.2019
 */
public class Dice {
    private final int min;
    private final int max;
    private final Random random = new Random();
    private static final int EXCLUSIVE_BOUND_OFFSET = 1;

    /**
     * Creates a dice with the provided range
     * @param min the lowest possible number (inlcusive)
     * @param max the highest possible number (inclusive)
     */
    public Dice(int min, int max){
        this.min = min;
        this.max = max;
    }

    /**
     * Returns a random number in the specified range (inclusive) of a dice
     * Only one dice is thrown.
     * @return the random number within the range of one dice
     */
    public int throwDice(){
        return random.nextInt(max - min + EXCLUSIVE_BOUND_OFFSET) + min;
    }

    /**
     * Throws multiple dices with the specified range.
     * All dices are identical.
     * If no dices (0) or less (-1) are thrown, returns 0.
     * @param amountOfDices how many dices should be thrown
     * @return the sum of all the numbers on all the dices.
     */
    public int throwDices(int amountOfDices){
        int endResult = 0;
        for (int i = 0; i < amountOfDices; i++){
            endResult += throwDice();
        }
        return endResult;
    }
}
