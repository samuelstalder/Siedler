package ch.zhaw.catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RoadTest {
    Road road;
    Player tester;
    List<Config.Resource> roadCoasts = Config.Structure.ROAD.getCosts();

    @BeforeEach
    void setUp(){
    road = new Road(new Point(3,3),new Point(4,4),tester);
    }

    @Test
    void getterTest(){
        assertEquals(new Point(3,3),road.getStart());
        assertEquals(tester, road.getOwner());
        assertEquals(road.getCosts(),roadCoasts);
    }
}
