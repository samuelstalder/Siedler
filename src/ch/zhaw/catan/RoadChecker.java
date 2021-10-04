package ch.zhaw.catan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class looks for the longest road on the island.
 * It checks whether a longest road exists and if it exists counts the length of ti.
 *
 * @author Samuel Stalder
 * @version 06.12.2019
 */
public class RoadChecker {

    private final List<Road> roads;
    private static final int NO_PREVIOUS_ROAD_EXISTS = -1;

    /**
     * Solve problem of counting roads to get the longest route.
     * Takes the list of roads from a certain player.
     * @param roads all roads of a player
     */
    public RoadChecker(List<Road> roads) {
        this.roads = roads;
    }

    /**
     * get all neighbor-roads of a certain road
     * @param roadIndex index of the current road
     * @return Amount of neighbor-roads (index stored in a set)
     */
    protected Set<Integer> getNeighbors(int roadIndex) {
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < roads.size(); i++) {
            if (i != roadIndex) {
                //compares all points to find a match
                //neighbor roads has an equal start or end point
                if (
                        roads.get(i).getStart().equals(roads.get(roadIndex).getEnd()) ||
                                roads.get(i).getStart().equals(roads.get(roadIndex).getStart()) ||
                                roads.get(i).getEnd().equals(roads.get(roadIndex).getEnd()) ||
                                roads.get(i).getEnd().equals(roads.get(roadIndex).getStart())
                ) {
                    res.add(i);
                }
            }
        }
        return res;
    }

    /**
     * Finds all starting points of a road-route.
     * @return index of a road
     */
    protected List<Integer> findAllStartingPoints() {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < roads.size(); i++) {
            if (getNeighbors(i).size() <= 1) {
                res.add(i);
            }
        }
        return res;
    }

    /**
     * Get the route length for a certain starting point.
     * @param currentRoadIndex the index of the current road
     * @param previousRoadIndex the index of the previous road
     * @return the length of the route
     */
    protected int getLengthForStartingPoint(int currentRoadIndex, int previousRoadIndex) {
        Set<Integer> neighbors = getNeighbors(currentRoadIndex);
        neighbors.remove(previousRoadIndex);
        if(previousRoadIndex != NO_PREVIOUS_ROAD_EXISTS) {
            //remove all neighbors of the previous neighbor
            Set<Integer> neighborsOfNeighbor = getNeighbors(previousRoadIndex);
            neighbors.removeAll(neighborsOfNeighbor);
        }
        if (neighbors.size() == 0) {
            return 1;
        } else {
            int maxRouteLength = 0;
            for (int neighbor : neighbors) {
                maxRouteLength = Math.max(maxRouteLength, getLengthForStartingPoint(neighbor, currentRoadIndex));
            }
            return 1 + maxRouteLength;
        }
    }

    /**
     * Finds and returns the longest route
     * @return the longest route length of a certain player
     */
    public int getMaxLength() {
        int routeLength = 0;
        List<Integer> startingPoints = findAllStartingPoints();
        for (int i : startingPoints) {
            routeLength = Math.max(routeLength, getLengthForStartingPoint(i, NO_PREVIOUS_ROAD_EXISTS));
        }
        return routeLength;
    }

}
