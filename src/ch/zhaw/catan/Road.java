package ch.zhaw.catan;

import java.awt.Point;
import java.util.List;

/**
 * This class models a {@link Road} object. In the settler of catan game,
 * the {@link Road} is one of the basic elements. A {@link Road} may only be built on
 * its own street or adjacent to its own {@link Settlement}/{@link City}.
 * The {@link Road} costs are: [1x CLAY | 1x WOOD]
 *
 * @version 05.12.2019
 * @author Sophie Daenzer
 */
public class Road {
    private Point startRoad, endRoad;
    private Player owner;

    /**
     *  Creates a  {@link Road} object with start-, end point and the owner
     * @param startRoad start point of road
     * @param endRoad end point of road
     * @param owner player who builds the road
     */
    public Road(Point startRoad, Point endRoad, Player owner){
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        this.owner = owner;
    }

    /**
     * The position on the board where the {@link Road} begins.
     * @return startRoad  Point where the road starts
     */
    public Point getStart() {
        return startRoad;
    }

    /**
     * The position on the board where the {@link Road} ends.
     * @return endRoad  Point where the road ends
     */
    public Point getEnd()
    {
        return endRoad;
    }

    /**
     * Returns the owner of the {@link Road}.
     * @return owner  of the road
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Returns a list with the necessary resources to build a {@link Road}.
     * @return List with resources to build a road
     */
    public List<Config.Resource> getCosts(){
        return Config.Structure.ROAD.getCosts();
    }
    
    /**
     * Prints the {@link ch.zhaw.catan.Config.Faction} of the owner in uppercase
     * @return the resulting string with length 2
     */
    @Override
    public String toString(){
        return getOwner().getFaction().toString().toUpperCase();
    }

}


