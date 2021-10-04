package ch.zhaw.catan;

import java.awt.Point;
import java.util.List;

/**
 * {@link City} is a subclass of {@link Settlement}. A {@link City} is a building that can be built in play.
 * It can only be built if there is already a {@link Settlement} on the same corner that belongs to the builder.
 * The {@link City} costs are: [3x STONE | 2x GRAIN]
 * The Player earns for every {@link City} 2 winPoints.
 *
 * @version 06.12.2019
 * @author Sophie Daenzer
 */

public class City extends Settlement {
    
    private static final int WIN_POINTS_CITY = 2;
    private static final int HARVEST_CITY = 2;

    /**
     * Creates a city object {@link City}
     * @param position position with the coordinates of the city
     * @param owner player who builds the {@link City}
     */
    public City(Point position, Player owner) {
        super(position,owner);
    }

    /**
     * Returns a list with the necessary resources to build a {@link City}.
     * @return List with resources to build a city
     */
    public List<Config.Resource> getCosts() {
        return Config.Structure.CITY.getCosts();
    }

    /**
     *  Returns the winPoints. For every built {@link City} the player gets 2 winPoints.
     *  @return number of points for a city
     */
    @Override
    public int getWinPoints() {
        return WIN_POINTS_CITY;
    }

    /**
     * When the player earns resources by throwing a dice, the amount doubles if there is a {@link City} at this position instead of a {@link Settlement}.
     * This doubling is the harvest.
     * @return harvest value two for city
     */
    @Override
    public int getHarvest(){
        return HARVEST_CITY;
    }
    
    /**
     * Prints the {@link City} so it can be recognised as a {@link City} on the board.
     * String is constructed frm the first character of the Faction the {@link City} belongs to
     * and a '+' to indicate it's a {@link City}
     * @return the resulting string with length 2
     */
    @Override
    public String toString() {
        return getOwner().getFaction().toString().toUpperCase().charAt(0) + "+";
    }
}
