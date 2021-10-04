package ch.zhaw.catan;

import java.awt.Point;
import java.util.List;

/**
 * A {@link Settlement} is a building that can be built in the game.
 * At the beginning of the game, every player has to build two {@link Settlement}  and two {@link Road}.
 * A {@link Settlement}  can later be converted into a {@link City}. The {@link Config.Structure#SETTLEMENT} holds the costs.
 * The Player earns for every built {@link Settlement} {@link #WIN_POINTS_SETTLEMENT} winPoint.
 * A settlement receives {@link #HARVEST_SETTLEMENT} resource if an adjacent field has the same number as the thrown dice number.
 * {@link Settlement} is superclass of {@link City}.
 *
 * @version 05.12.2019
 * @author Sophie Daenzer
 */
public class Settlement {

    private Point position;
    private Player owner;
    private static final int WIN_POINTS_SETTLEMENT = 1;
    private static final int HARVEST_SETTLEMENT = 1;

    /**
     *  Creates a  {@link Settlement} object with a position and an owner
     * @param position the coordinates of the Settlement
     * @param owner the player who builds the settlement
     */
    public Settlement(Point position, Player owner){
        this.position = position;
        this.owner = owner;
    }


    /**
     * Returns the position of the {@link Settlement} on the board.
     * @return position of the settlement
     */
    public Point getPosition() {
        return position;
    }


    /**
     * Returns the owner of the {@link Settlement}.
     * @return owner  of the settlement
     */
    public Player getOwner() {
        return owner;
    }


    /**
     * Returns the winPoints. For every built {@link Settlement} the player gets 1 winPoints.
     * @return number of points for a settlement
     */
    public int getWinPoints() {
        return WIN_POINTS_SETTLEMENT;
    }

    /**
     * Returns the harvest value for a {@link Settlement}. When the player earns resources by throwing a dice,
     * the number changes if there is a {@link Settlement} or a {@link City} at this position.
     * @return harvest value one for settlement
     */
    public int getHarvest(){
        return HARVEST_SETTLEMENT;
    }
    
    /**
     * Returns a list with the necessary resources to build a {@link Settlement}.
     * @return List with resources to build a settlement
     */
    public List<Config.Resource> getCosts(){
        return Config.Structure.SETTLEMENT.getCosts();
    }
    
    /**
     * Prints the {@link ch.zhaw.catan.Config.Faction} of the owner in uppercase
     * @return the resulting string with length 2
     */
    @Override
    public String toString() {
        return getOwner().getFaction().toString().toUpperCase();
    }

}
