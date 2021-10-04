package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceTest {
    Dice dice;
    @BeforeEach
    void setUp() {
        dice = new Dice(1,6);
    }

    @Test
    void testThrowDice() {
        for (int i = 0; i < 10_000; i++){
            assertTrue(1 <= dice.throwDice() && dice.throwDice() <= 6);
        }
    }

    @Test
    void testThrowTwoDices() {
        for (int i = 0; i < 10_000; i++){
            assertTrue(1 <= dice.throwDices(2) && dice.throwDices(2) <= 12);
        }
    }

    @Test
    void testThrowSixDices() {
        for (int i = 0; i < 10_000; i++){
            assertTrue(6 <= dice.throwDices(6) && dice.throwDices(6) <= 36);
        }
    }

    @Test
    void testThrowDiceOutOfRange() {
        for (int i = 0; i < 10_000; i++){
            assertFalse(1 > dice.throwDice() && dice.throwDice() > 6);
        }
    }

    @Test
    void testThrowZeroDices() {
        for (int i = 0; i < 10_000; i++){
            assertEquals(0, dice.throwDices(0));
        }
    }

    @Test
    void testThrowMinusOneDices() {
        for (int i = 0; i < 10_000; i++){
            assertEquals(0, dice.throwDices(-1));
        }
    }
}