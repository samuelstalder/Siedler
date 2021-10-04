package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTest {
    Bank bank;
    @BeforeEach
    void testSetUp() {
        bank = new Bank();
    }

    @Test
    void testTrade() {
        assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
        assertTrue(bank.trade(Config.Resource.WOOD, Config.Resource.WOOL));
    }
    
    @Test
    void testInitialization(){
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.WOOD), bank.getResourceStock().get(Config.Resource.WOOD));
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.STONE), bank.getResourceStock().get(Config.Resource.STONE));
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.GRAIN), bank.getResourceStock().get(Config.Resource.GRAIN));
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.WOOL), bank.getResourceStock().get(Config.Resource.WOOL));
        assertEquals(Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.CLAY), bank.getResourceStock().get(Config.Resource.CLAY));
    }
    
    @Test
    void testRunOutOfStock(){
        for (int i = 0; i < Config.INITIAL_RESOURCE_CARDS_BANK.get(Config.Resource.GRAIN); i++) {
            assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
        }
        assertFalse(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
    }
    
    @Test
    void testRunOutOfStockAndFillAgain(){
        testRunOutOfStock();
        //fill Grain again
        assertTrue(bank.trade(Config.Resource.GRAIN, Config.Resource.STONE));
        assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
        assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
        assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
        assertTrue(bank.trade(Config.Resource.STONE, Config.Resource.GRAIN));
    }
}