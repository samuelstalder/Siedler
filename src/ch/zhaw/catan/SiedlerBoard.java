package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoard;

import java.awt.Point;
import java.util.Map;

/**
 * This class inherits from {@link HexBoard} and initializes.
 * It adds all the fields to the board.
 * It knows which corners are on the island and which are in the water.
 *
 * @author Joël Plambeck
 * @version 04.12.2019
 */
public class SiedlerBoard extends HexBoard<Land, Settlement, Road, String> {

    /**
     * Creates a SiedlerBoard and adds all required fields to the board.
     * The fields are arranged according to the {@link Config} class.
     * Since it is an isle, it is surrounded with water.
     */
    public SiedlerBoard() {
        setFieldsFromConfig();
    }

    private void setFieldsFromConfig(){
        Map<Point, Land> fields = Config.getStandardLandPlacement();
        for (Map.Entry<Point, Land> field : fields.entrySet()) {
            addField(field.getKey(), field.getValue());
        }
    }
    
    /**
     * Checks whether the corner point is on the field or not
     * @param point the point to check
     * @return if the point is on land
     */
    public boolean isPointOnField(Point point){
        for (Land land : getFields(point)){
            if (!land.equals(Land.WATER)){
                return true;
            }
        }
        return false;
    }
}
