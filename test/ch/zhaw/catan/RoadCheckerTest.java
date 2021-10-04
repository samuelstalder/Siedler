package ch.zhaw.catan;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.Arrays;

public class RoadCheckerTest {

    @Test
    void testNoRoads() {
        RoadChecker checker = new RoadChecker(Arrays.asList());
        Assert.assertEquals(0, checker.getMaxLength());
    }

    @Test
    void testSingleRoad() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(3, 7), new Point(3, 9), null)
        ));
        Assert.assertEquals(1, checker.getMaxLength());
    }

    @Test
    void testRoadRouteOf3() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(3, 7), new Point(3, 9), null),
                new Road(new Point(3, 9), new Point(4, 10), null),
                new Road(new Point(4, 10), new Point(4, 12), null)
        ));
        Assert.assertEquals(3, checker.getMaxLength());
    }

    @Test
    void testSingleRoadRouteWithBranch() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(3, 7), new Point(3, 9), null),
                new Road(new Point(3, 9), new Point(4, 10), null),
                new Road(new Point(4, 10), new Point(4, 12), null),
                //new branch
                new Road(new Point(4, 10), new Point(5, 9), null),
                new Road(new Point(5, 9), new Point(5, 7), null)
        ));
        Assert.assertEquals(4, checker.getMaxLength());
    }

    @Test
    void testMultipleRoadRouteWithBranch() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(3, 7), new Point(3, 9), null),
                new Road(new Point(3, 9), new Point(4, 10), null),
                new Road(new Point(4, 10), new Point(4, 12), null),
                //new branch
                new Road(new Point(4, 10), new Point(5, 9), null),
                new Road(new Point(5, 9), new Point(5, 7), null),
                // separated road-route
                new Road(new Point(7, 9), new Point(6, 10), null),
                new Road(new Point(6, 10), new Point(6, 12), null),
                new Road(new Point(6, 12), new Point(7, 13), null)
        ));
        Assert.assertEquals(4, checker.getMaxLength());
    }

    //can't solve problem of cycle in road-route
    @Test
    void testRoadCycle() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(7, 9), new Point(6, 10), null),
                new Road(new Point(6, 10), new Point(6, 12), null),
                new Road(new Point(6, 12), new Point(7, 13), null),
                new Road(new Point(7, 13), new Point(8, 12), null),
                new Road(new Point(8, 12), new Point(8, 10), null),
                new Road(new Point(8, 10), new Point(7, 9), null)
        ));
        Assert.assertEquals(4, checker.getMaxLength());
    }

    @Test
    void testFindAllStartingPointsEmpty() {
        RoadChecker checker = new RoadChecker(Arrays.asList());
        Assert.assertEquals(0, checker.findAllStartingPoints().size());
    }

    @Test
    void testFindAllStartingPointsSingleRoad() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(7, 9), new Point(6, 10), null)
        ));
        Assert.assertEquals(1, checker.findAllStartingPoints().size());
    }

    @Test
    void testFindAllStartingPointsTwoRoads() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(7, 9), new Point(6, 10), null),
                new Road(new Point(6, 10), new Point(6, 12), null)
        ));
        Assert.assertEquals(2, checker.findAllStartingPoints().size());
    }

    @Test
    void testHasNeighborSingleRoad() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(6, 10), new Point(6, 12), null)
        ));
        Assert.assertEquals(0, checker.getNeighbors(0).size());
    }

    @Test
    void testHasNeighborTwoRoadsNeighbors() {
        RoadChecker checker = new RoadChecker(Arrays.asList(
                new Road(new Point(7, 9), new Point(6, 10), null),
                new Road(new Point(6, 10), new Point(6, 12), null)
        ));
        Assert.assertEquals(1, checker.getNeighbors(0).size());
    }

}
