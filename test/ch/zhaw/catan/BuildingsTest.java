package ch.zhaw.catan;

import ch.zhaw.catan.Config.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BuildingsTest {
    Player tester;
    Settlement settlement;
    City city;

    @BeforeEach
    void setUp(){
        settlement = new Settlement(new Point(4,4),tester);
        city = new City(new Point(5,7),tester);
    }

    @Test
    void getterTest(){
        assertEquals(new Point(4,4), settlement.getPosition());
        assertEquals(2, city.getHarvest());
    }
    @Test
    void getBuildingsCostsTest(){
        assertEquals(List.of(Resource.WOOD,Resource.CLAY, Resource.WOOL,Resource.GRAIN), settlement.getCosts());
        assertEquals(List.of(Resource.STONE,Resource.STONE,Resource.STONE,Resource.GRAIN,Resource.GRAIN), city.getCosts());
    }

    @Test
    void getWinPointsTest(){
        assertEquals(1, settlement.getWinPoints());
        assertEquals(2, city.getWinPoints());
    }
}
